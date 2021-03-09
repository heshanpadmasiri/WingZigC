package heshan.compilertheory.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CompilerTest {
    Compiler compiler;

    @BeforeEach
    void setUp() {
        Path program_path = Paths.get("", "src", "test", "resources", "compile_test.test");
        try {
            InputScanner scanner = new InputScanner(program_path);
            RecursiveDescentParser parser = new RecursiveDescentParser(scanner);
            parser.parse();
            AbstractSyntaxTree ast = parser.getAbstractSyntaxTree();
            compiler = new Compiler(ast);
        } catch (FileNotFoundException | FailedToMatchPatternException e) {
            assertNull(e);
        }
    }

    @Test
    void getProgram() {
        List<String> program = compiler.getProgram();
        for (String line : program) {
            System.out.println(line);
        }
    }

    @Test
    void toFile() {
    }

}