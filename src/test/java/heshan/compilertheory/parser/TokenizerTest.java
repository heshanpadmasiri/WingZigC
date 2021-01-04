package heshan.compilertheory.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TokenizerTest {
    Pattern whiteSpace = Pattern.compile("\\s+");

    @org.junit.jupiter.api.Test
    void canReadAllInputs(){
        Path assetsRoot = Paths.get("", "src", "test", "resources");
        Stream<File> inputFiles = Arrays.stream(Objects.requireNonNull(assetsRoot.toFile().listFiles()))
                .filter(filename -> !(filename.toString().contains(".tree") || filename.toString().contains(".test")));
        inputFiles.forEach(file -> {
            try {
                InputScanner scanner = new InputScanner(file.toPath());
                Tokenizer tokenizer = new Tokenizer(scanner);
                while(tokenizer.hasNext()){
                    String token = tokenizer.next();
                    assertFalse(whiteSpace.matcher(token).matches(),
                            String.format("Whitespaces detected in token \"%s\"", token)); // no empty strings
                }
            } catch (FileNotFoundException e) {
                assertNull(e);
            }

        });
    }

    @org.junit.jupiter.api.Test
    void canReadTestInput(){
        Path filePath = Paths.get("", "src", "test", "resources", "inputScannerTest.test");
        List<String> expectedTokens = Arrays.asList("Test", "test", "id", "=", "id", "+", "id", ">", "1");
        try {
            Tokenizer tokenizer = new Tokenizer(new InputScanner(filePath));
            List<String> tokens = new LinkedList<>();
            while(tokenizer.hasNext()){
                String token = tokenizer.next();
                tokens.add(token);
            }
            assertEquals(expectedTokens, tokens, "Invalid token generation for test input");
        } catch (FileNotFoundException e) {
            assertNull(e);
        }
    }
}