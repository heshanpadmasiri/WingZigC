package heshan.compilertheory.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class CompilerTest {
    Compiler compiler;
    private List<String> readFile(File file){
        Scanner s = null;
        List<String> body = new LinkedList<>();
        try {
            s = new Scanner(file);
            while (s.hasNextLine()){
                body.add(s.nextLine());
            }
            s.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return body;
    }

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
        List<String> expectedOutput = readFile(Paths.get("", "src", "test", "resources", "compile_test_expected.o").toFile());
        assertEquals(expectedOutput, program);
    }

    @Test
    void toFile() {
    }

}