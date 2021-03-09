package heshan.compilertheory.parser;

import java.nio.file.Path;
import java.util.List;

public class Compiler {
    private List<String> program;
    private AbstractSyntaxTree ast;
    private SymbolTable symbolTable;

    public Compiler(AbstractSyntaxTree ast) {
        this.ast = ast;
        symbolTable = new SymbolTable();
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
