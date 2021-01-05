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

  @Override
  public boolean equals(Object obj) {
    if (obj.getClass() != this.getClass()) {
      return false;
    }
    Token other = (Token) obj;
    return (other.getType() == this.getType())
        && (other.getId() == this.getId())
        && (other.getValue() == this.getValue());
  }

  @Override
  public String toString() {
    return String.format("Token(%s %d %s)", type, id, value);
  }

  public String getValue() {
    return value;
  }

  protected Token clone() {
    return new Token(type, id, value);
  }
}
