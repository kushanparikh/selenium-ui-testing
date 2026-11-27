import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Checks Surefire JUnit XML against the utr contract (README "utr contract").
 *
 * Usage: java scripts/VerifyReport.java <junitDir> [--expect-flaky] [--expect-errors]
 * Exit 0 when every check passes; exit 1 with one line per violation.
 */
public class VerifyReport {

    private static final List<Pattern> ABSOLUTE_PATHS = List.of(
            Pattern.compile("(?<![A-Za-z])[A-Za-z]:\\\\"),
            Pattern.compile("/Users/"),
            Pattern.compile("/home/"),
            Pattern.compile("/private/"),
            Pattern.compile("/var/folders/"),
            Pattern.compile("D:\\\\a\\\\"));
    private static final Pattern BASE64_RUN = Pattern.compile("[A-Za-z0-9+/=]{1025,}");
    private static final Pattern ATTACHMENT = Pattern.compile("\\[\\[ATTACHMENT\\|([^\\]]*)\\]\\]");
    private static final Pattern CLASSNAME = Pattern.compile("^io\\.github\\.kushanparikh\\.selenium\\.[\\w.$]+$");

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("usage: java scripts/VerifyReport.java <junitDir> [--expect-flaky] [--expect-errors]");
            System.exit(2);
        }
        Path dir = Path.of(args[0]);
        boolean expectFlaky = List.of(args).contains("--expect-flaky");
        boolean expectErrors = List.of(args).contains("--expect-errors");
        List<String> violations = new ArrayList<>();

        List<Path> files;
        try (Stream<Path> s = Files.isDirectory(dir) ? Files.list(dir) : Stream.empty()) {
            files = s.filter(p -> p.getFileName().toString().matches("TEST-.*\\.xml")).sorted().toList();
        }
        if (files.isEmpty()) {
            violations.add(dir + ": no TEST-*.xml files");
        }

        int testcases = 0;
        int flaky = 0;
        int errors = 0;
        for (Path file : files) {
            String name = file.getFileName().toString();
            String text = Files.readString(file);
            Document doc;
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
                doc = factory.newDocumentBuilder().parse(file.toFile());
            } catch (Exception e) {
                violations.add(name + ": not well-formed XML (" + e.getMessage() + ")");
                continue;
            }
            Element root = doc.getDocumentElement();
            if (!"testsuite".equals(root.getTagName())) {
                violations.add(name + ": root element is <" + root.getTagName() + ">, expected <testsuite>");
            }
            NodeList cases = doc.getElementsByTagName("testcase");
            if (cases.getLength() == 0) {
                violations.add(name + ": no <testcase> elements");
            }
            testcases += cases.getLength();
            if (doc.getElementsByTagName("properties").getLength() > 0) {
                violations.add(name + ": contains a <properties> element");
            }
            for (Pattern p : ABSOLUTE_PATHS) {
                Matcher m = p.matcher(text);
                if (m.find()) {
                    violations.add(name + ": absolute path '" + p.pattern() + "' near: " + excerpt(text, m.start()));
                }
            }
            if (BASE64_RUN.matcher(text).find()) {
                violations.add(name + ": contains an inline base64-like run longer than 1024 chars");
            }
            Matcher a = ATTACHMENT.matcher(text);
            while (a.find()) {
                String ref = a.group(1);
                if (ref.startsWith("/") || ref.contains("\\") || !ref.startsWith("results/attachments/")) {
                    violations.add(name + ": bad attachment reference '" + ref + "'");
                }
            }
            for (int i = 0; i < cases.getLength(); i++) {
                Element tc = (Element) cases.item(i);
                String cn = tc.getAttribute("classname");
                if (!CLASSNAME.matcher(cn).matches()) {
                    violations.add(name + ": testcase classname '" + cn + "' does not match " + CLASSNAME.pattern());
                }
            }
            flaky += doc.getElementsByTagName("flakyFailure").getLength();
            errors += doc.getElementsByTagName("error").getLength();
        }

        if (expectFlaky && flaky == 0) {
            violations.add(dir + ": expected at least one <flakyFailure>, found none");
        }
        if (expectErrors && errors == 0) {
            violations.add(dir + ": expected at least one <error>, found none");
        }

        if (violations.isEmpty()) {
            System.out.println("OK: " + files.size() + " files, " + testcases + " testcases, "
                    + flaky + " flakyFailure, " + errors + " error");
            return;
        }
        violations.forEach(v -> System.out.println("FAIL: " + v));
        System.exit(1);
    }

    private static String excerpt(String text, int at) {
        int from = Math.max(0, at - 20);
        int to = Math.min(text.length(), at + 40);
        return text.substring(from, to).replaceAll("\\s+", " ");
    }
}
