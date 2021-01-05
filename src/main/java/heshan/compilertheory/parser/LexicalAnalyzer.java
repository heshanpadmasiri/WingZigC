package heshan.compilertheory.parser;

public class LexicalAnalyzer extends AbstractLexicalAnalyzer {
  KeyWordAnalyzer keyWordAnalyzer;
  OperationAnalyzer operationAnalyzer;
  PunctuationAnalyzer punctuationAnalyzer;
  IdentifierAnalyzer identifierAnalyzer;
  Token current;
  Token next;
  final Token END = new Token(TokenType.FILE_END, -1, "");
  Tokenizer tokenizer;

  public LexicalAnalyzer(SymbolTable symbolTable, InputScanner inputScanner)
      throws FailedToMatchPatternException {
    super(symbolTable);
    keyWordAnalyzer = new KeyWordAnalyzer(symbolTable);
    operationAnalyzer = new OperationAnalyzer(symbolTable);
    punctuationAnalyzer = new PunctuationAnalyzer(symbolTable);
    identifierAnalyzer = new IdentifierAnalyzer(symbolTable);
    this.tokenizer = new Tokenizer(inputScanner);
    firstMove();
  }

  private void firstMove() throws FailedToMatchPatternException {
    if (tokenizer.hasNext()) {
      next = matchPattern(tokenizer.next());
    } else {
      next = END;
    }
  }

  private void moveForward() throws FailedToMatchPatternException {
    current = next;
    if (tokenizer.hasNext()) {
      next = matchPattern(tokenizer.next());
    } else {
      next = END;
    }
  }

  @Override
  public Token matchPattern(String tokenValue) throws FailedToMatchPatternException {
    Token token;
    try {
      token = keyWordAnalyzer.matchPattern(tokenValue);
    } catch (FailedToMatchPatternException e) {
      try {
        token = operationAnalyzer.matchPattern(tokenValue);
      } catch (FailedToMatchPatternException failedToMatchPatternException) {
        try {
          token = punctuationAnalyzer.matchPattern(tokenValue);
        } catch (FailedToMatchPatternException toMatchPatternException) {
          token = identifierAnalyzer.matchPattern(tokenValue);
        }
      }
    }
    return token;
  }

  public Token getToken() throws FailedToMatchPatternException {
    moveForward();
    return current;
  }

  public Token peekNextToken() {
    return next.clone();
  }
}
