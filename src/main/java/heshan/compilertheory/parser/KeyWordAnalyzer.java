package heshan.compilertheory.parser;

public class KeyWordAnalyzer extends AbstractLexicalAnalyzer{
    public KeyWordAnalyzer(SymbolTable symbolTable) {
        super(symbolTable);
    }

    @Override
    public Token matchPattern(String token) throws FailedToMatchPatternException {
        int id = -1;
        TokenType type;
        switch (token){
            case "program":
                type = TokenType.PROGRAM;
                break;
            case "var":
                type = TokenType.VAR;
                break;
            case "const":
                type = TokenType.CONST;
                break;
            case "type":
                type = TokenType.TYPE;
                break;
            case "function":
                type = TokenType.FUNCTION;
                break;
            case "return":
                type = TokenType.RETURN;
                break;
            case "begin":
                type = TokenType.BEGIN;
                break;
            case "end":
                type = TokenType.END;
                break;
            case "output":
                type = TokenType.OUTPUT;
                break;
            case "if":
                type = TokenType.IF;
                break;
            case "then":
                type = TokenType.THEN;
                break;
            case "else":
                type = TokenType.ELSE;
                break;
            case "while":
                type = TokenType.WHILE;
                break;
            case "do":
                type = TokenType.DO;
                break;
            case "case":
                type = TokenType.CASE;
                break;
            case "of":
                type = TokenType.OF;
                break;
            case "otherwise":
                type = TokenType.OTHERWISE;
                break;
            case "repeat":
                type = TokenType.REPEAT;
                break;
            case "for":
                type = TokenType.FOR;
                break;
            case "until":
                type = TokenType.UNTIL;
                break;
            case "loop":
                type = TokenType.LOOP;
                break;
            case "pool":
                type = TokenType.POOL;
                break;
            case "exit":
                type = TokenType.EXIT;
                break;
            case "read":
                type = TokenType.READ;
                break;
            case "succ":
                type = TokenType.SUCC;
                break;
            case "pred":
                type = TokenType.PRED;
                break;
            case "chr":
                type = TokenType.CHR;
                break;
            case "ord":
                type = TokenType.ORD;
                break;
            case "..":
                type = TokenType.CASE_EXP;
                break;
            default:
                throw new FailedToMatchPatternException();
        }
        return new Token(type, id, token);
    }
}
