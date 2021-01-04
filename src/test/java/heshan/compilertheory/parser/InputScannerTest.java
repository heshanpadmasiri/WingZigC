package heshan.compilertheory.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class InputScannerTest {
    Pattern multipleWhiteSpaces = Pattern.compile("\\s{2,}");

    @org.junit.jupiter.api.Test
    void canReadAllInputs(){
        Path assetsRoot = Paths.get("", "src", "test", "resources");
        Stream<File> inputFiles = Arrays.stream(Objects.requireNonNull(assetsRoot.toFile().listFiles())).filter(filename -> !filename.toString().contains(".tree"));
        inputFiles.forEach(file -> {
            try {
                InputScanner scanner = new InputScanner(file.toPath());
                while(scanner.hasNext()){
                    String token = scanner.next();
                    assertFalse(multipleWhiteSpaces.matcher(token).matches(),
                            String.format("multiple whitespaces detected in token \"%s\"", token)); // no empty strings
                    assertFalse(token.contains("#"),
                            String.format("line comment detected in token \"%s\"", token)); // no line comments
                    assertFalse(token.contains("{") || token.contains("}"),
                            String.format("block comment detected in token \"%s\"", token)); // no block comments
                }
            } catch (FileNotFoundException e) {
                assertNull(e);
            }

        });
    }
}
