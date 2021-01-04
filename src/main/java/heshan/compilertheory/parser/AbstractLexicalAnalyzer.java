package heshan.compilertheory.parser;

public abstract class AbstractLexicalAnalyzer {
    SymbolTable symbolTable;

    public AbstractLexicalAnalyzer(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    public abstract Token matchPattern(String string) throws FailedToMatchPatternException;
}
