package heshan.compilertheory.parser;

public class OperationAnalyzer extends AbstractLexicalAnalyzer {
    public OperationAnalyzer(SymbolTable symbolTable) {
        super(symbolTable);
    }

    @Override
    public Token matchPattern(String token_value) throws FailedToMatchPatternException {
        int id = -1;
        TokenType type;
        switch (token_value){
            case ":=:":
                type = TokenType.SWAP;
                break;
            case ":=":
                type = TokenType.ASSIGN;
                break;
            case "=":
                type = TokenType.EQ;
                break;
            case "<=":
                type = TokenType.LEQ;
                break;
            case "<>":
                type = TokenType.NEQ;
                break;
            case "<":
                type = TokenType.LT;
                break;
            case ">=":
                type = TokenType.GEQ;
                break;
            case ">":
                type = TokenType.GT;
                break;
            case "mod":
                type = TokenType.MOD;
                break;
            case "and":
                type = TokenType.AND;
                break;
            case "or":
                type = TokenType.OR;
                break;
            case "not":
                type = TokenType.NOT;
                break;
            case "+":
                type = TokenType.PLUS;
                break;
            case "-":
                type = TokenType.MINUS;
                break;
            case "*":
                type = TokenType.MULTIPLY;
                break;
            case "/":
                type = TokenType.DIV;
                break;
            default:
                throw new FailedToMatchPatternException();
        }
        return new Token(type, id, token_value);
    }
}
