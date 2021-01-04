package heshan.compilertheory.parser;

import java.util.regex.Pattern;

public class IdentifierAnalyzer extends AbstractLexicalAnalyzer{
    Pattern identifier = Pattern.compile("[a-zA-Z_]\\w*");
    public IdentifierAnalyzer(SymbolTable symbolTable) {
        super(symbolTable);
    }

    @Override
    public Token matchPattern(String tokenValue) throws FailedToMatchPatternException {
        if(!identifier.matcher(tokenValue).matches()){
           throw new FailedToMatchPatternException();
        }
        int id = symbolTable.getNextKey();
        TokenType type = TokenType.IDENTIFIER;
        symbolTable.upsert(id, null);
        return new Token(type, id, tokenValue);
    }
}
