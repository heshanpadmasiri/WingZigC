package heshan.compilertheory.parser;

public class Token {
    private TokenType type;
    private int id;
    private String value;

    public Token(TokenType type, int id, String value) {
        this.type = type;
        this.id = id;
        this.value = value;
    }

    public TokenType getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }
}
