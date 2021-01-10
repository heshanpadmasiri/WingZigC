package heshan.compilertheory.parser;

public class RecursiveDescentParser {
  private final SymbolTable symbolTable;
  private LexicalAnalyzer lexicalAnalyzer;
  private Token current;
  private Token next;

  public RecursiveDescentParser(SymbolTable symbolTable, InputScanner inputScanner)
      throws FailedToMatchPatternException {
    this.symbolTable = symbolTable;
    this.lexicalAnalyzer = new LexicalAnalyzer(symbolTable, inputScanner);
  }

  private void moveForward() {
    try {
      current = lexicalAnalyzer.getToken();
    } catch (FailedToMatchPatternException e) {
      throw new RuntimeException("Failed to recognize next token");
    }
    next = lexicalAnalyzer.peekNextToken();
  }

  public void parse() {
    moveForward();
    Winzig();
  }

  private void checkTokenType(TokenType type) {
    assert current.getType() == type;
  }

  private void Winzig() {
    checkTokenType(TokenType.PROGRAM);
    moveForward();
    Name();
    moveForward();
    checkTokenType(TokenType.COL);
    if (next.getType() == TokenType.CONST) {
      moveForward();
      Consts();
    }
    if (next.getType() == TokenType.TYPE) {
      moveForward();
      Types();
    }
    if (next.getType() == TokenType.VAR) {
      moveForward();
      Dclns();
    }
    if (next.getType() == TokenType.FUNCTION) {
      moveForward();
      SubProgs();
    }
    moveForward();
    Body();
    moveForward();
    Name();
    moveForward();
    checkTokenType(TokenType.DOT);
  }

  private void Consts() {
    if (current.getType() == TokenType.CONST) {
      moveForward();
      Const();
      while (next.getType() == TokenType.COMMA) {
        moveForward();
        checkTokenType(TokenType.COMMA);
        moveForward();
        Const();
      }
      moveForward();
      checkTokenType(TokenType.SCOL);
    }
  }

  private void Const() {
    Name();
    moveForward();
    checkTokenType(TokenType.EQ);
    moveForward();
    ConstValue();
  }

  private void ConstValue() {
    switch (current.getType()) {
      case NUMBER:
      case CHAR:
        break;
      default:
        Name();
        moveForward();
        checkTokenType(TokenType.SCOL);
    }
  }

  private void Types() {
    moveForward();
    Type();
    moveForward();
    checkTokenType(TokenType.SCOL);
    while (next.getType() == TokenType.IDENTIFIER) {
      moveForward();
      Type();
      moveForward();
      checkTokenType(TokenType.SCOL);
    }
  }

  private void Type() {
    Name();
    moveForward();
    checkTokenType(TokenType.EQ);
    moveForward();
    LitList();
  }

  private void LitList() {
    checkTokenType(TokenType.BRACK_OP);
    moveForward();
    Name();
    while (next.getType() == TokenType.COMMA) {
      moveForward();
      checkTokenType(TokenType.COMMA);
      moveForward();
      Name();
    }
    moveForward();
    checkTokenType(TokenType.BRACK_CL);
  }

  private void SubProgs() {
    checkTokenType(TokenType.FUNCTION);
    Fcn();
    while (next.getType() == TokenType.FUNCTION) {
      moveForward();
      Fcn();
    }
  }

  private void Fcn() {
    checkTokenType(TokenType.FUNCTION);
    moveForward();
    Name();
    moveForward();
    checkTokenType(TokenType.BRACK_OP);
    moveForward();
    Params();
    moveForward();
    checkTokenType(TokenType.BRACK_CL);
    moveForward();
    checkTokenType(TokenType.COL);
    moveForward();
    Name();
    moveForward();
    checkTokenType(TokenType.SCOL);
    if (next.getType() == TokenType.CONST) {
      moveForward();
      Consts();
    }
    if (next.getType() == TokenType.TYPE) {
      moveForward();
      Types();
    }
    if (next.getType() == TokenType.VAR) {
      moveForward();
      Dclns();
    }
    moveForward();
    Body();
    moveForward();
    Name();
    moveForward();
    checkTokenType(TokenType.SCOL);
  }

  private void Params() {
    Dcln();
    while (next.getType() == TokenType.SCOL) {
      moveForward();
      checkTokenType(TokenType.SCOL);
      moveForward();
      Dcln();
    }
  }

  private void Dclns() {
    checkTokenType(TokenType.VAR);
    moveForward();
    Dcln();
    moveForward();
    checkTokenType(TokenType.SCOL);
    while (next.getType() == TokenType.IDENTIFIER) {
      moveForward();
      Dcln();
      moveForward();
      checkTokenType(TokenType.SCOL);
    }
  }

  private void Dcln() {
    Name();
    while (next.getType() == TokenType.COMMA) {
      moveForward();
      checkTokenType(TokenType.COMMA);
      moveForward();
      Name();
    }
    moveForward();
    checkTokenType(TokenType.COL);
    moveForward();
    Name();
  }

  private void Body() {
    checkTokenType(TokenType.BEGIN);
    if(isNextStatement()){
      moveForward();
      Statement();
    }
    while (next.getType() == TokenType.SCOL) {
      moveForward();
      checkTokenType(TokenType.SCOL);
      if (isNextStatement()){
        moveForward();
        Statement();
      }
    }
    moveForward();
    checkTokenType(TokenType.END);
  }

  private boolean isNextStatement() {
    TokenType nextType = next.getType();
    return nextType == TokenType.IDENTIFIER
        || nextType == TokenType.OUTPUT
        || nextType == TokenType.IF
        || nextType == TokenType.WHILE
        || nextType == TokenType.REPEAT
        || nextType == TokenType.FOR
        || nextType == TokenType.LOOP
        || nextType == TokenType.CASE
        || nextType == TokenType.READ
        || nextType == TokenType.EXIT
        || nextType == TokenType.RETURN
        || nextType == TokenType.BEGIN;
  }

  private void Statement() {
    switch (current.getType()) {
      case IDENTIFIER: // assignment
        Assignment();
        break;
      case OUTPUT:
        moveForward();
        checkTokenType(TokenType.BRACK_OP);
        moveForward();
        OutExp();
        while (next.getType() == TokenType.COMMA) {
          moveForward();
          checkTokenType(TokenType.COMMA);
          moveForward();
          OutExp();
        }
        moveForward();
        checkTokenType(TokenType.BRACK_CL);
        break;
      case IF:
        moveForward();
        Expression();
        moveForward();
        checkTokenType(TokenType.THEN);
        if (isNextStatement()) {
          moveForward();
          Statement();
        }
        if (next.getType() == TokenType.ELSE) {
          moveForward();
          checkTokenType(TokenType.ELSE);
          if (isNextStatement()) {
            moveForward();
            Statement();
          }
        }
        break;
      case WHILE:
        moveForward();
        Expression();
        moveForward();
        checkTokenType(TokenType.DO);
        if (isNextStatement()) {
          moveForward();
          Statement();
        }
        break;
      case REPEAT:
        if (isNextStatement()) {
          moveForward();
          Statement();
        }
        while (next.getType() == TokenType.SCOL) {
          moveForward();
          checkTokenType(TokenType.SCOL);
          if (isNextStatement()) {
            moveForward();
            Statement();
          }
        }
        moveForward();
        checkTokenType(TokenType.UNTIL);
        moveForward();
        Expression();
        break;
      case FOR:
        moveForward();
        checkTokenType(TokenType.BRACK_OP);
        if (next.getType() == TokenType.IDENTIFIER) {
          moveForward();
          ForStat();
        }
        moveForward();
        checkTokenType(TokenType.SCOL);
        if (isNextPrimary()) {
          moveForward();
          ForExp();
        }
        moveForward();
        checkTokenType(TokenType.SCOL);
        if (next.getType() == TokenType.IDENTIFIER) {
          moveForward();
          ForStat();
        }
        moveForward();
        checkTokenType(TokenType.BRACK_CL);
        if (isNextStatement()) {
          moveForward();
          Statement();
        }
        break;
      case LOOP:
        if (isNextStatement()) {
          moveForward();
          Statement();
        }
        while (next.getType() == TokenType.SCOL) {
          moveForward();
          checkTokenType(TokenType.SCOL);
          if (isNextStatement()) {
            moveForward();
            Statement();
          }
        }
        moveForward();
        checkTokenType(TokenType.POOL);
        moveForward();
        break;
      case CASE:
        moveForward();
        Expression();
        moveForward();
        checkTokenType(TokenType.OF);
        moveForward();
        Caseclauses();
        if (next.getType() == TokenType.OTHERWISE){
          moveForward();
          OtherwiseClause();
        }
        moveForward();
        checkTokenType(TokenType.END);
        break;
      case READ:
        moveForward();
        checkTokenType(TokenType.BRACK_OP);
        moveForward();
        Name();
        while (next.getType() == TokenType.COMMA) {
          moveForward();
          checkTokenType(TokenType.COMMA);
          moveForward();
          Name();
        }
        moveForward();
        checkTokenType(TokenType.BRACK_CL);
        break;
      case EXIT:
        break;
      case RETURN:
        moveForward();
        Expression();
        break;
      case BEGIN:
        Body();
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + current.getType());
    }
  }

  private void OutExp() {
    if (current.getType() == TokenType.STRING) {
      StringNode();
    } else {
      Expression();
    }
  }

  private void StringNode() {
    checkTokenType(TokenType.STRING);
  }

  private void Caseclauses() {
    Caseclause();
    moveForward();
    checkTokenType(TokenType.SCOL);
    while (next.getType() == TokenType.NUMBER
        || next.getType() == TokenType.CHAR
        || next.getType() == TokenType.IDENTIFIER) {
      moveForward();
      Caseclause();
      moveForward();
      checkTokenType(TokenType.SCOL);
    }
  }

  private void Caseclause() {
    CaseExpression();
    while (next.getType() == TokenType.COMMA) {
      moveForward();
      checkTokenType(TokenType.COMMA);
      moveForward();
      CaseExpression();
    }
    moveForward();
    checkTokenType(TokenType.COL);
    if(isNextStatement()){
      moveForward();
      Statement();
    }
  }

  private void CaseExpression() {
    if (next.getType() == TokenType.CASE_EXP) {
      ConstValue();
      moveForward();
      checkTokenType(TokenType.CASE_EXP);
      moveForward();
      ConstValue();
    } else {
      ConstValue();
    }
  }

  private void OtherwiseClause() {
    checkTokenType(TokenType.OTHERWISE);
    if (isNextStatement()){
      moveForward();
      Statement();
    }
  }

  private void Assignment() {
    if (next.getType() == TokenType.ASSIGN) {
      Name();
      moveForward();
      checkTokenType(TokenType.ASSIGN);
      moveForward();
      Expression();
    } else {
      Name();
      moveForward();
      checkTokenType(TokenType.SWAP);
      moveForward();
      Name();
    }
  }

  private void ForStat() {
    checkTokenType(TokenType.IDENTIFIER);
    Assignment();
  }

  private boolean isNextPrimary() {
    return isPrimary(next);
  }

  private boolean isPrimary(Token token) {
    return token.getType() == TokenType.MINUS
        || token.getType() == TokenType.PLUS
        || token.getType() == TokenType.NOT
        || token.getType() == TokenType.EOF
        || token.getType() == TokenType.IDENTIFIER
        || token.getType() == TokenType.NUMBER
        || token.getType() == TokenType.CHAR
        || token.getType() == TokenType.BRACK_OP
        || token.getType() == TokenType.SUCC
        || token.getType() == TokenType.PRED
        || token.getType() == TokenType.CHR
        || token.getType() == TokenType.ORD;
  }

  private boolean isPrimary() {
    return isPrimary(current);
  }

  private void ForExp() {
    Expression();
  }

  private void Expression() {
    Term();
    if (is_Expression(next)){
      moveForward();
      _Expression();
    }
  }

  private boolean is_Expression(Token token){
    TokenType type = token.getType();
    return type == TokenType.LEQ || type == TokenType.LT || type == TokenType.GEQ || type == TokenType.GT || type == TokenType.EQ || type == TokenType.NEQ;
  }

  private void _Expression(){
    assert is_Expression(current);
    moveForward();
    Term();
  }

  private void Term() {
    Factor();
    if (isNextPrimary()){
      moveForward();
      _Term();
    }
    switch (next.getType()) {
      case PLUS:
        checkTokenType(TokenType.PLUS);
        moveForward();
        Term();
        break;
      case MINUS:
        checkTokenType(TokenType.MINUS);
        moveForward();
        Term();
        break;
      case OR:
        checkTokenType(TokenType.OR);
        moveForward();
        Term();
        break;
    }
  }

  private void _Term(){
    Factor();
    if (isNextPrimary()){
      moveForward();
      _Term();
    }
  }

  private void Factor() {
    Primary();
    if (isNextPrimary()){
      moveForward();
      _Factor();
    }
    switch (next.getType()) {
      case MULTIPLY:
        moveForward();
        checkTokenType(TokenType.MULTIPLY);
        moveForward();
        Primary();
        break;
      case DIV:
        moveForward();
        checkTokenType(TokenType.DIV);
        moveForward();
        Primary();
        break;
      case AND:
        moveForward();
        checkTokenType(TokenType.AND);
        moveForward();
        Primary();
        break;
      case MOD:
        moveForward();
        checkTokenType(TokenType.MOD);
        moveForward();
        Primary();
        break;
    }
  }

  private void _Factor(){
    Primary();
    if (isNextPrimary()){
      moveForward();
      _Factor();
    }
  }

  private void Primary() {
    switch (current.getType()) {
      case MINUS:
      case PLUS:
      case NOT:
        moveForward();
        Primary();
        break;
      case EOF:
      case NUMBER:
      case CHAR:
        break;
      case IDENTIFIER:
        if (next.getType() == TokenType.BRACK_OP) {
          Name();
          moveForward();
          checkTokenType(TokenType.BRACK_OP);
          moveForward();
          Expression();
          while (next.getType() == TokenType.COMMA) {
            moveForward();
            checkTokenType(TokenType.COMMA);
            moveForward();
            Expression();
          }
          moveForward();
          checkTokenType(TokenType.BRACK_CL);
        } else {
          Name();
        }
        break;
      case BRACK_OP:
        moveForward();
        Expression();
        moveForward();
        checkTokenType(TokenType.BRACK_CL);
        break;
      case SUCC:
      case CHR:
      case ORD:
      case PRED:
        moveForward();
        checkTokenType(TokenType.BRACK_OP);
        moveForward();
        Expression();
        moveForward();
        checkTokenType(TokenType.BRACK_CL);
        break;
    }
  }

  private void Name() {
    checkTokenType(TokenType.IDENTIFIER);
  }
}
