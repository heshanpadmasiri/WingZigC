package heshan.compilertheory.parser;

public class RecursiveDescentParser {
  private final SymbolTable symbolTable;
  private LexicalAnalyzer lexicalAnalyzer;
  private Token current;
  private Token next;

  public RecursiveDescentParser(SymbolTable symbolTable, LexicalAnalyzer lexicalAnalyzer) {
    this.symbolTable = symbolTable;
    this.lexicalAnalyzer = lexicalAnalyzer;
  }

  private void moveForward() {
    try {
      current = lexicalAnalyzer.getToken();
    } catch (FailedToMatchPatternException e) {
      throw new RuntimeException("Failed to recognize next token");
    }
    next = lexicalAnalyzer.peekNextToken();
  }

  public void parse() {}

  private void checkTokenType(TokenType type) {
    assert current.getType() == type;
  }

  private void winzig() {
    checkTokenType(TokenType.PROGRAM);
    moveForward();
    Name();
  }

  private void Consts() {
    if (current.getType() == TokenType.CONST) {
      moveForward();
      Const();
      while (next.getType() == TokenType.COMMA){
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
    if (current.getType() == TokenType.TYPE) {
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
    while (next.getType() == TokenType.COMMA){
      moveForward();
      checkTokenType(TokenType.COMMA);
      moveForward();
      Name();
    }
    moveForward();
    checkTokenType(TokenType.BRACK_CL);
  }

  private void SubProgs() {
    while (current.getType() == TokenType.FUNCTION) {
      Fcn();
      moveForward();
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
    moveForward();
    Consts();
    moveForward();
    Types();
    moveForward();
    Dclns();
    moveForward();
    Body();
    moveForward();
    Name();
    moveForward();
    checkTokenType(TokenType.SCOL);
  }

  private void Params() {
    Dclns();
    while (next.getType() == TokenType.SCOL){
      moveForward();
      checkTokenType(TokenType.SCOL);
      moveForward();
      Dclns();
    }
  }

  private void Dclns() {
    if (current.getType() == TokenType.VAR) {
      moveForward();
      Dclns();
      moveForward();
      checkTokenType(TokenType.SCOL);
      if (next.getType() == TokenType.VAR) {
        moveForward();
        Dclns();
      }
    }
  }

  private void Dcln() {
    Name();
    while (next.getType() == TokenType.COMMA){
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
    moveForward();
    Statement();
    while (next.getType() == TokenType.SCOL){
      moveForward();
      checkTokenType(TokenType.SCOL);
      moveForward();
      Statement();
    }
    moveForward();
    checkTokenType(TokenType.END);
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
        while (next.getType() == TokenType.COMMA){
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
        moveForward();
        Statement();
        if (next.getType() == TokenType.ELSE) {
          moveForward();
          checkTokenType(TokenType.ELSE);
          moveForward();
          Statement();
        }
        break;
      case WHILE:
        moveForward();
        Expression();
        moveForward();
        checkTokenType(TokenType.DO);
        moveForward();
        Statement();
        break;
      case REPEAT:
        moveForward();
        Statement();
        while (next.getType() == TokenType.SCOL){
          moveForward();
          checkTokenType(TokenType.SCOL);
          moveForward();
          Statement();
        }
        moveForward();
        checkTokenType(TokenType.UNTIL);
        moveForward();
        Expression();
        break;
      case FOR:
        moveForward();
        checkTokenType(TokenType.BRACK_OP);
        moveForward();
        ForStat();
        moveForward();
        checkTokenType(TokenType.SCOL);
        moveForward();
        ForExp();
        checkTokenType(TokenType.SCOL);
        moveForward();
        ForStat();
        moveForward();
        checkTokenType(TokenType.BRACK_CL);
        moveForward();
        Statement();
        break;
      case LOOP:
        moveForward();
        Statement();
        while (next.getType() == TokenType.SCOL){
          moveForward();
          checkTokenType(TokenType.SCOL);
          moveForward();
          Statement();
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
        moveForward();
        OtherwiseClause();
        moveForward();
        checkTokenType(TokenType.END);
        break;
      case READ:
        moveForward();
        checkTokenType(TokenType.BRACK_OP);
        moveForward();
        Name();
        while (next.getType() == TokenType.COMMA){
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
    do {
      Caseclause();
      moveForward();
      checkTokenType(TokenType.SCOL);
    } while (next.getType() == TokenType.NUMBER
        || next.getType() == TokenType.CHAR
        || next.getType() == TokenType.IDENTIFIER);
  }

  private void Caseclause() {
    CaseExpression();
    while (next.getType() == TokenType.COMMA){
      moveForward();
      checkTokenType(TokenType.COMMA);
      moveForward();
      CaseExpression();
    }
    moveForward();
    checkTokenType(TokenType.COL);
    moveForward();
    Statement();
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
    if (current.getType() == TokenType.OTHERWISE) {
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
    if (current.getType() == TokenType.IDENTIFIER) {
      Assignment();
    }
  }

  private boolean isPrimary() {
    return current.getType() == TokenType.MINUS
        || current.getType() == TokenType.PLUS
        || current.getType() == TokenType.NOT
        || current.getType() == TokenType.EOF
        || current.getType() == TokenType.IDENTIFIER
        || current.getType() == TokenType.NUMBER
        || current.getType() == TokenType.CHAR
        || current.getType() == TokenType.BRACK_OP
        || current.getType() == TokenType.SUCC
        || current.getType() == TokenType.PRED
        || current.getType() == TokenType.CHR
        || current.getType() == TokenType.ORD;
  }

  private void ForExp() {
    if (isPrimary()) {
      Expression();
    }
  }

  private void Expression() {
    switch (next.getType()) {
      case LEQ:
        Term();
        moveForward();
        checkTokenType(TokenType.LEQ);
        moveForward();
        Term();
        break;
      case LT:
        Term();
        moveForward();
        checkTokenType(TokenType.LT);
        moveForward();
        Term();
        break;
      case GEQ:
        Term();
        moveForward();
        checkTokenType(TokenType.GEQ);
        moveForward();
        Term();
        break;
      case GT:
        Term();
        moveForward();
        checkTokenType(TokenType.GT);
        moveForward();
        Term();
        break;
      case EQ:
        Term();
        moveForward();
        checkTokenType(TokenType.EQ);
        moveForward();
        Term();
        break;
      case NEQ:
        Term();
        moveForward();
        checkTokenType(TokenType.NEQ);
        moveForward();
        Term();
        break;
      default:
        Term();
    }
  }

  private void Term() {
    switch (next.getType()) {
      case PLUS:
        Term();
        moveForward();
        checkTokenType(TokenType.PLUS);
        moveForward();
        Term();
        break;
      case MINUS:
        Term();
        moveForward();
        checkTokenType(TokenType.MINUS);
        moveForward();
        Term();
        break;
      case OR:
        Term();
        moveForward();
        checkTokenType(TokenType.OR);
        moveForward();
        Term();
        break;
      default:
        Factor();
    }
  }

  private void Factor() {
    switch (next.getType()) {
      case MULTIPLY:
        Factor();
        moveForward();
        checkTokenType(TokenType.MULTIPLY);
        moveForward();
        Primary();
        break;
      case DIV:
        Factor();
        moveForward();
        checkTokenType(TokenType.DIV);
        moveForward();
        Primary();
        break;
      case AND:
        Factor();
        moveForward();
        checkTokenType(TokenType.AND);
        moveForward();
        Primary();
        break;
      case MOD:
        Factor();
        moveForward();
        checkTokenType(TokenType.MOD);
        moveForward();
        Primary();
        break;
      default:
        Primary();
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
          while (next.getType() == TokenType.COMMA){
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
