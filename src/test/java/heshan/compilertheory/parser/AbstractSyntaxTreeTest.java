package heshan.compilertheory.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AbstractSyntaxTreeTest {

  SymbolTable symbolTable;

  @BeforeEach
  void setUp() {
    symbolTable = mock(SymbolTable.class);
    when(symbolTable.getNextKey()).thenReturn(0);
  }

  void printTree(Path program_path) {
    try {
      InputScanner scanner = new InputScanner(program_path);
      RecursiveDescentParser parser = new RecursiveDescentParser(symbolTable, scanner);
      parser.parse();
      AbstractSyntaxTree ast = parser.getAbstractSyntaxTree();
      ast.printTree();
    } catch (FileNotFoundException | FailedToMatchPatternException e) {
      assertNull(e);
    }
  }

  @Test
  void writeToFile() {
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
            RecursiveDescentParser parser = new RecursiveDescentParser(symbolTable, scanner);
            parser.parse();
            String ast_file_name = file.getName() + ".ast";
            Path ast_file = assetsRoot.resolve(ast_file_name);
            AbstractSyntaxTree ast = parser.getAbstractSyntaxTree();
            ast.toFile(ast_file);
          } catch (FailedToMatchPatternException | IOException e) {
            assertNull(e);
          }
        });
  }

  @Test
  void printTree() {
    Path assetsRoot = Paths.get("", "src", "test", "resources");
    Stream<File> inputFiles =
        Arrays.stream(Objects.requireNonNull(assetsRoot.toFile().listFiles()))
            .filter(
                filename ->
                    !(filename.toString().contains(".tree")
                        || filename.toString().contains(".test")));
    inputFiles.forEach(
        file -> {
          System.out.println(file);
          printTree(file.toPath());
        });
  }

  @Test
  void getRoot() {}
}
