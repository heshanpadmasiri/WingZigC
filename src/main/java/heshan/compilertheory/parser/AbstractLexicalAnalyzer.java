package heshan.compilertheory.parser;

public abstract class AbstractLexicalAnalyzer {

    public AbstractLexicalAnalyzer() {
    }

    public abstract Token matchPattern(String string) throws FailedToMatchPatternException;
}
