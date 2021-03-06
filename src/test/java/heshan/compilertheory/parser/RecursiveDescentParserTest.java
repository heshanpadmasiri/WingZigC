package heshan.compilertheory.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RecursiveDescentParserTest {

    SymbolTable symbolTable;

    @BeforeEach
    void setUp() {
        symbolTable = mock(SymbolTable.class);
    }

    @Test
    void parse() {
        Path assetsRoot = Paths.get("", "src", "test", "resources");
        Stream<File> inputFiles =
                Arrays.stream(Objects.requireNonNull(assetsRoot.toFile().listFiles()))
                        .filter(
                                filename ->
                                        !(filename.toString().contains(".tree")
                                                || filename.toString().contains(".ast")
                                                || filename.toString().contains(".o")
                                                || filename.toString().contains(".test")));
        inputFiles.forEach(file -> {
            System.out.println(file.toString());
            InputScanner scanner = null;
            try {
                scanner = new InputScanner(file.toPath());
                RecursiveDescentParser parser = new RecursiveDescentParser(scanner);
                parser.parse();
            } catch (FileNotFoundException | FailedToMatchPatternException e) {
                assertNull(e);
            }
        });
    }
}
