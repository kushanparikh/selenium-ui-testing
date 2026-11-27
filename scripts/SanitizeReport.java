import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Removes Surefire's <properties> blocks from JUnit XML, in place.
 *
 * Why: Surefire's enablePropertiesElement=false is ignored when a test is rerun (the flaky path), and the block
 * holds every JVM system property: user.home, java.io.tmpdir, repoRoot... Those are machine paths and usernames,
 * which the utr contract forbids. CI runs this before uploading utr-results.
 *
 * Usage: java scripts/SanitizeReport.java <junitDir>
 */
public class SanitizeReport {

    private static final Pattern PROPERTIES = Pattern.compile("(?s)[ \\t]*<properties>.*?</properties>\\R?");

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("usage: java scripts/SanitizeReport.java <junitDir>");
            System.exit(2);
        }
        List<Path> files;
        try (Stream<Path> s = Files.list(Path.of(args[0]))) {
            files = s.filter(p -> p.getFileName().toString().matches("TEST-.*\\.xml")).sorted().toList();
        }
        int changed = 0;
        for (Path file : files) {
            String text = Files.readString(file);
            String cleaned = PROPERTIES.matcher(text).replaceAll("");
            if (!cleaned.equals(text)) {
                Files.writeString(file, cleaned);
                changed++;
            }
        }
        System.out.println("Sanitized " + changed + " of " + files.size() + " files");
    }
}
