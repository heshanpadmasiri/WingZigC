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

class LexicalAnalyzerTest {


  SymbolTable symbolTable;
  @BeforeEach
  void setUp() {
    symbolTable = mock(SymbolTable.class);
    when(symbolTable.getNextKey()).thenReturn(0);
  }

  @Test
  void matchPattern() {
    Path assetsRoot = Paths.get("", "src", "test", "resources");
    Stream<File> inputFiles =
        Arrays.stream(Objects.requireNonNull(assetsRoot.toFile().listFiles()))
            .filter(
                filename ->
                    !(filename.toString().contains(".tree")
                        || filename.toString().contains(".test")));
    inputFiles.forEach(
        file -> {
          try {
            InputScanner scanner = new InputScanner(file.toPath());
            Tokenizer tokenizer = new Tokenizer(scanner);
            LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(symbolTable, scanner);
            while (tokenizer.hasNext()) {
              String token = tokenizer.next();
              Token t = lexicalAnalyzer.matchPattern(token);
              assertNotNull(t.getType());
              assertEquals(t.getValue(),token);
            }
          } catch (FileNotFoundException | FailedToMatchPatternException e) {
            assertNull(e);
          }
        });
  }

  @Test
  void getToken() {}

  @Test
  void peekNextToken() {}
}
