package heshan.compilertheory.parser;

import java.nio.file.Path;
import java.util.List;

public class Compiler {
    private List<String> program;
    private AbstractSyntaxTree ast;

    public Compiler(AbstractSyntaxTree ast) {
        this.ast = ast;
        compile();
    }

    private void compile(){
       if(ast.getRoot() == null){
           throw new RuntimeException("AST is empty");
       }
    }

    private List<String> getProgram(){
        return program;
    }

    private void toFile(Path outputPath){

    }
}
