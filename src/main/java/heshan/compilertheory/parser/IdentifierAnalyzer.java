package heshan.compilertheory.parser;

import java.util.regex.Pattern;

public class IdentifierAnalyzer extends AbstractLexicalAnalyzer {
  Pattern identifier = Pattern.compile("[a-zA-Z_]\\w*");
  Pattern number = Pattern.compile("\\d++");
  Pattern character = Pattern.compile("'.{0,1}'");
  Pattern string = Pattern.compile("\".*\"");


  @Override
  public Token matchPattern(String tokenValue) throws FailedToMatchPatternException {
    TokenType type;
    if (number.matcher(tokenValue).matches()) {
      type = TokenType.NUMBER;
    } else if (character.matcher(tokenValue).matches()) {
      type = TokenType.CHAR;
    } else if (string.matcher(tokenValue).matches()) {
      type = TokenType.STRING;
    } else if (identifier.matcher(tokenValue).matches()) {
      type = TokenType.IDENTIFIER;
    } else {
      throw new FailedToMatchPatternException();
    }
    return new Token(type, 0, tokenValue);
  }
}
