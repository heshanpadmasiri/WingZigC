package heshan.compilertheory.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    void printTree(Path program_path, Path ast_path){
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
    void printTree() {
        Path program = Paths.get("", "src", "test", "resources", "winzig_01");
        Path ast = Paths.get("", "src", "test", "resources", "winzig_01.tree");
        printTree(program, ast);
    }

    @Test
    void getRoot() {
    }
}