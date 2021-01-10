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
    when(symbolTable.getNextKey()).thenReturn(0);
  }

  @Test
  void parse() {
    Path assetsRoot = Paths.get("", "src", "test", "resources");
    Stream<File> inputFiles =
        Arrays.stream(Objects.requireNonNull(assetsRoot.toFile().listFiles()))
            .filter(
                filename ->
                    !(filename.toString().contains(".tree")
                        || filename.toString().contains(".test")));
    inputFiles.forEach(file -> {
      InputScanner scanner = null;
      try {
        System.out.println(file);
        scanner = new InputScanner(file.toPath());
        RecursiveDescentParser parser = new RecursiveDescentParser(symbolTable, scanner);
        parser.parse();
      } catch (FileNotFoundException | FailedToMatchPatternException e) {
        assertNull(e);
      }
    });
  }
}
