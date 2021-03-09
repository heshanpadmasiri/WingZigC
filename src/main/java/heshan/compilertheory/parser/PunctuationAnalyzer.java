package heshan.compilertheory.parser;

public class PunctuationAnalyzer extends AbstractLexicalAnalyzer{

    @Override
    public Token matchPattern(String tokenValue) throws FailedToMatchPatternException {
        int id = -1;
        TokenType type;
        switch (tokenValue){
            case "eof":
                type = TokenType.EOF;
                break;
            case ":":
                type = TokenType.COL;
                break;
            case ";":
                type = TokenType.SCOL;
                break;
            case ".":
                type = TokenType.DOT;
                break;
            case ",":
                type = TokenType.COMMA;
                break;
            case "(":
                type = TokenType.BRACK_OP;
                break;
            case ")":
                type = TokenType.BRACK_CL;
                break;
            default:
                throw new FailedToMatchPatternException();
        }
        return new Token(type, id, tokenValue);
    }
}
