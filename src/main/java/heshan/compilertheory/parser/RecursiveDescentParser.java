package heshan.compilertheory.parser;

public class RecursiveDescentParser {
  private final SymbolTable symbolTable;
  private LexicalAnalyzer lexicalAnalyzer;
  private AbstractSyntaxTree abstractSyntaxTree;
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

  public AbstractSyntaxTree getAbstractSyntaxTree() {
    return abstractSyntaxTree;
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
    ASTNode node = new ASTNode("program");
    abstractSyntaxTree = new AbstractSyntaxTree(node);
    moveForward();
    Name(node);
    moveForward();
    checkTokenType(TokenType.COL);
    if (next.getType() == TokenType.CONST) {
      moveForward();
      Consts(node);
    } else {
      node.addChild(new ASTNode("consts"));
    }
    if (next.getType() == TokenType.TYPE) {
      moveForward();
      Types(node);
    } else {
      node.addChild(new ASTNode("types"));
    }
    if (next.getType() == TokenType.VAR) {
      moveForward();
      Dclns(node);
    } else {
      node.addChild(new ASTNode("dclns"));
    }
    if (next.getType() == TokenType.FUNCTION) {
      moveForward();
      SubProgs(node);
    } else {
      node.addChild(new ASTNode("subprogs"));
    }
    moveForward();
    Body(node);
    moveForward();
    Name(node);
    moveForward();
    checkTokenType(TokenType.DOT);
  }

  private void Consts(ASTNode parent) {
    if (current.getType() == TokenType.CONST) {
      ASTNode node = new ASTNode("consts", parent);
      moveForward();
      Const(node);
      while (next.getType() == TokenType.COMMA) {
        moveForward();
        checkTokenType(TokenType.COMMA);
        moveForward();
        Const(node);
      }
      moveForward();
      checkTokenType(TokenType.SCOL);
    }
  }

  private void Const(ASTNode parent) {
    ASTNode node = new ASTNode("const", parent);
    Name(node);
    moveForward();
    checkTokenType(TokenType.EQ);
    moveForward();
    ConstValue(node);
  }

  private void ConstValue(ASTNode parent) {
    ASTNode node;
    switch (current.getType()) {
      case NUMBER:
        node = new ASTNode("<integer>", parent);
        node.addChild(new ASTNode(current.getValue()));
        break;
      case CHAR:
        node = new ASTNode("<char>", parent);
        node.addChild(new ASTNode(current.getValue()));
        break;
      default:
        Name(parent);
    }
  }

  private void Types(ASTNode parent) {
    ASTNode node = new ASTNode("types", parent);
    moveForward();
    Type(node);
    moveForward();
    checkTokenType(TokenType.SCOL);
    while (next.getType() == TokenType.IDENTIFIER) {
      moveForward();
      Type(node);
      moveForward();
      checkTokenType(TokenType.SCOL);
    }
  }

  private void Type(ASTNode parent) {
    ASTNode node = new ASTNode("type", parent);
    Name(node);
    moveForward();
    checkTokenType(TokenType.EQ);
    moveForward();
    LitList(node);
  }

  private void LitList(ASTNode parent) {
    ASTNode node = new ASTNode("lit", parent);
    checkTokenType(TokenType.BRACK_OP);
    moveForward();
    Name(node);
    while (next.getType() == TokenType.COMMA) {
      moveForward();
      checkTokenType(TokenType.COMMA);
      moveForward();
      Name(node);
    }
    moveForward();
    checkTokenType(TokenType.BRACK_CL);
  }

  private void SubProgs(ASTNode parent) {
    ASTNode node = new ASTNode("subprogs", parent);
    checkTokenType(TokenType.FUNCTION);
    Fcn(node);
    while (next.getType() == TokenType.FUNCTION) {
      moveForward();
      Fcn(node);
    }
  }

  private void Fcn(ASTNode parent) {
    ASTNode node = new ASTNode("fcn", parent);
    checkTokenType(TokenType.FUNCTION);
    moveForward();
    Name(node);
    moveForward();
    checkTokenType(TokenType.BRACK_OP);
    moveForward();
    Params(node);
    moveForward();
    checkTokenType(TokenType.BRACK_CL);
    moveForward();
    checkTokenType(TokenType.COL);
    moveForward();
    Name(node);
    moveForward();
    checkTokenType(TokenType.SCOL);
    if (next.getType() == TokenType.CONST) {
      moveForward();
      Consts(node);
    } else {
      node.addChild(new ASTNode("consts"));
    }
    if (next.getType() == TokenType.TYPE) {
      moveForward();
      Types(node);
    } else {
      node.addChild(new ASTNode("types"));
    }
    if (next.getType() == TokenType.VAR) {
      moveForward();
      Dclns(node);
    } else {
      node.addChild(new ASTNode("dclns"));
    }
    moveForward();
    Body(node);
    moveForward();
    Name(node);
    moveForward();
    checkTokenType(TokenType.SCOL);
  }

  private void Params(ASTNode parent) {
    ASTNode node = new ASTNode("params", parent);
    Dcln(node);
    while (next.getType() == TokenType.SCOL) {
      moveForward();
      checkTokenType(TokenType.SCOL);
      moveForward();
      Dcln(node);
    }
  }

  private void Dclns(ASTNode parent) {
    ASTNode node = new ASTNode("dclns", parent);
    checkTokenType(TokenType.VAR);
    moveForward();
    Dcln(node);
    moveForward();
    checkTokenType(TokenType.SCOL);
    while (next.getType() == TokenType.IDENTIFIER) {
      moveForward();
      Dcln(node);
      moveForward();
      checkTokenType(TokenType.SCOL);
    }
  }

  private void Dcln(ASTNode parent) {
    ASTNode node = new ASTNode("var", parent);
    Name(node);
    while (next.getType() == TokenType.COMMA) {
      moveForward();
      checkTokenType(TokenType.COMMA);
      moveForward();
      Name(node);
    }
    moveForward();
    checkTokenType(TokenType.COL);
    moveForward();
    Name(node);
  }

  private void Body(ASTNode parent) {
    ASTNode node = new ASTNode("block", parent);
    checkTokenType(TokenType.BEGIN);
    if (isNextStatement()) {
      moveForward();
      Statement(node);
    } else {
      node.addChild(new ASTNode("<null>"));
    }
    while (next.getType() == TokenType.SCOL) {
      moveForward();
      checkTokenType(TokenType.SCOL);
      if (isNextStatement()) {
        moveForward();
        Statement(node);
      } else {
        node.addChild(new ASTNode("<null>"));
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

  private void Statement(ASTNode parent) {
    ASTNode node;
    switch (current.getType()) {
      case IDENTIFIER: // assignment
        Assignment(parent);
        break;
      case OUTPUT:
        node = new ASTNode("output", parent);
        moveForward();
        checkTokenType(TokenType.BRACK_OP);
        moveForward();
        OutExp(node);
        while (next.getType() == TokenType.COMMA) {
          moveForward();
          checkTokenType(TokenType.COMMA);
          moveForward();
          OutExp(node);
        }
        moveForward();
        checkTokenType(TokenType.BRACK_CL);
        break;
      case IF:
        node = new ASTNode("if", parent);
        moveForward();
        Expression(node);
        moveForward();
        checkTokenType(TokenType.THEN);
        if (isNextStatement()) {
          moveForward();
          Statement(node);
        } else {
          node.addChild(new ASTNode("<null>"));
        }
        if (next.getType() == TokenType.ELSE) {
          moveForward();
          checkTokenType(TokenType.ELSE);
          if (isNextStatement()) {
            moveForward();
            Statement(node);
          } else {
            node.addChild(new ASTNode("<null>"));
          }
        }
        break;
      case WHILE:
        node = new ASTNode("while", parent);
        moveForward();
        Expression(node);
        moveForward();
        checkTokenType(TokenType.DO);
        if (isNextStatement()) {
          moveForward();
          Statement(node);
        } else {
          node.addChild(new ASTNode("<null>"));
        }
        break;
      case REPEAT:
        node = new ASTNode("repeat", parent);
        if (isNextStatement()) {
          moveForward();
          Statement(node);
        } else {
          node.addChild(new ASTNode("<null>"));
        }
        while (next.getType() == TokenType.SCOL) {
          moveForward();
          checkTokenType(TokenType.SCOL);
          if (isNextStatement()) {
            moveForward();
            Statement(node);
          } else {
            node.addChild(new ASTNode("<null>"));
          }
        }
        moveForward();
        checkTokenType(TokenType.UNTIL);
        moveForward();
        Expression(node);
        break;
      case FOR:
        node = new ASTNode("for", parent);
        moveForward();
        checkTokenType(TokenType.BRACK_OP);
        if (next.getType() == TokenType.IDENTIFIER) {
          moveForward();
          ForStat(node);
        } else {
          node.addChild(new ASTNode("<null>"));
        }
        moveForward();
        checkTokenType(TokenType.SCOL);
        if (isNextPrimary()) {
          moveForward();
          ForExp(node);
        } else {
          node.addChild(new ASTNode("true"));
        }
        moveForward();
        checkTokenType(TokenType.SCOL);
        if (next.getType() == TokenType.IDENTIFIER) {
          moveForward();
          ForStat(node);
        } else {
          node.addChild(new ASTNode("<null>"));
        }
        moveForward();
        checkTokenType(TokenType.BRACK_CL);
        if (isNextStatement()) {
          moveForward();
          Statement(node);
        } else {
          node.addChild(new ASTNode("<null>"));
        }
        break;
      case LOOP:
        node = new ASTNode("loop", parent);
        if (isNextStatement()) {
          moveForward();
          Statement(node);
        } else {
          node.addChild(new ASTNode("<null>"));
        }
        while (next.getType() == TokenType.SCOL) {
          moveForward();
          checkTokenType(TokenType.SCOL);
          if (isNextStatement()) {
            moveForward();
            Statement(node);
          } else {
            node.addChild(new ASTNode("<null>"));
          }
        }
        moveForward();
        checkTokenType(TokenType.POOL);
        moveForward();
        break;
      case CASE:
        node = new ASTNode("case", parent);
        moveForward();
        Expression(node);
        moveForward();
        checkTokenType(TokenType.OF);
        moveForward();
        Caseclauses(node);
        if (next.getType() == TokenType.OTHERWISE) {
          moveForward();
          OtherwiseClause(node);
        }
        moveForward();
        checkTokenType(TokenType.END);
        break;
      case READ:
        node = new ASTNode("read", parent);
        moveForward();
        checkTokenType(TokenType.BRACK_OP);
        moveForward();
        Name(node);
        while (next.getType() == TokenType.COMMA) {
          moveForward();
          checkTokenType(TokenType.COMMA);
          moveForward();
          Name(node);
        }
        moveForward();
        checkTokenType(TokenType.BRACK_CL);
        break;
      case EXIT:
        parent.addChild(new ASTNode("exit"));
        break;
      case RETURN:
        node = new ASTNode("return", parent);
        moveForward();
        Expression(node);
        break;
      case BEGIN:
        Body(parent);
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + current.getType());
    }
  }

  private void OutExp(ASTNode parent) {
    ASTNode node;
    if (current.getType() == TokenType.STRING) {
      node = new ASTNode("string", parent);
      StringNode(node);
    } else {
      node = new ASTNode("integer", parent);
      Expression(node);
    }
  }

  private void StringNode(ASTNode parent) {
    parent.addChild(new ASTNode("<string>"));
    checkTokenType(TokenType.STRING);
  }

  private void Caseclauses(ASTNode parent) {
    Caseclause(parent);
    moveForward();
    checkTokenType(TokenType.SCOL);
    while (next.getType() == TokenType.NUMBER
        || next.getType() == TokenType.CHAR
        || next.getType() == TokenType.IDENTIFIER) {
      moveForward();
      Caseclause(parent);
      moveForward();
      checkTokenType(TokenType.SCOL);
    }
  }

  private void Caseclause(ASTNode parent) {
    ASTNode node = new ASTNode("case_clause", parent);
    CaseExpression(node);
    while (next.getType() == TokenType.COMMA) {
      moveForward();
      checkTokenType(TokenType.COMMA);
      moveForward();
      CaseExpression(node);
    }
    moveForward();
    checkTokenType(TokenType.COL);
    if (isNextStatement()) {
      moveForward();
      Statement(node);
    } else {
      node.addChild(new ASTNode("<null>"));
    }
  }

  private void CaseExpression(ASTNode parent) {
    if (next.getType() == TokenType.CASE_EXP) {
      ASTNode node = new ASTNode("..", parent);
      ConstValue(node);
      moveForward();
      checkTokenType(TokenType.CASE_EXP);
      moveForward();
      ConstValue(node);
    } else {
      ConstValue(parent);
    }
  }

  private void OtherwiseClause(ASTNode parent) {
    ASTNode node = new ASTNode("otherwise", parent);
    checkTokenType(TokenType.OTHERWISE);
    if (isNextStatement()) {
      moveForward();
      Statement(node);
    } else {
      node.addChild(new ASTNode("<null>"));
    }
  }

  private void Assignment(ASTNode parent) {
    ASTNode node;
    if (next.getType() == TokenType.ASSIGN) {
      node = new ASTNode("assign", parent);
      Name(node);
      moveForward();
      checkTokenType(TokenType.ASSIGN);
      moveForward();
      Expression(node);
    } else {
      node = new ASTNode("swap", parent);
      Name(node);
      moveForward();
      checkTokenType(TokenType.SWAP);
      moveForward();
      Name(node);
    }
  }

  private void ForStat(ASTNode parent) {
    checkTokenType(TokenType.IDENTIFIER);
    Assignment(parent);
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

  private void ForExp(ASTNode parent) {
    Expression(parent);
  }

  private void Expression(ASTNode parent) {
    ASTNode bufferNode = new ASTNode("buffer");
    Term(bufferNode);
    if (is_Expression(next)) {
      ASTNode newBufferNode = new ASTNode("buffer");
      moveForward();
      _Expression(newBufferNode);
      bufferNode.getChildren().forEach(newBufferNode.getChildren().get(0)::addChildToHead);
      bufferNode = newBufferNode;
    }
    bufferNode.getChildren().forEach(parent::addChild);
  }

  private boolean is_Expression(Token token) {
    TokenType type = token.getType();
    return type == TokenType.LEQ
        || type == TokenType.LT
        || type == TokenType.GEQ
        || type == TokenType.GT
        || type == TokenType.EQ
        || type == TokenType.NEQ;
  }

  private void _Expression(ASTNode bufferNode) {
    assert is_Expression(current);
    ASTNode node;
    String node_body;
    switch (current.getType()){
      case LEQ:
        node_body = "<=";
        break;
      case LT:
        node_body = "<";
        break;
      case GEQ:
        node_body = ">=";
        break;
      case GT:
        node_body = ">";
        break;
      case EQ:
        node_body = "=";
        break;
      case NEQ:
        node_body = "<>";
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + current.getType());
    }
    node = new ASTNode(node_body, bufferNode);
    moveForward();
    Term(node);
  }

  private void Term(ASTNode parent) {
    ASTNode bufferNode = new ASTNode("buffer");
    Factor(bufferNode);
    if (isNextPrimary()) {
      ASTNode newBufferNode = new ASTNode("buffer");
      moveForward();
      _Term(newBufferNode);
      bufferNode.getChildren().forEach(newBufferNode.getChildren().get(0)::addChildToHead);
      bufferNode = newBufferNode;
    }
    ASTNode node;
    switch (next.getType()) {
      case PLUS:
        node = new ASTNode("+", parent);
        bufferNode.getChildren().forEach(node::addChild);
        moveForward();
        checkTokenType(TokenType.PLUS);
        moveForward();
        Term(node);
        break;
      case MINUS:
        node = new ASTNode("-", parent);
        bufferNode.getChildren().forEach(node::addChild);
        moveForward();
        checkTokenType(TokenType.MINUS);
        moveForward();
        Term(node);
        break;
      case OR:
        node = new ASTNode("or", parent);
        bufferNode.getChildren().forEach(node::addChild);
        moveForward();
        checkTokenType(TokenType.OR);
        moveForward();
        Term(node);
        break;
      default:
        bufferNode.getChildren().forEach(parent::addChild);
    }
  }

  private void _Term(ASTNode parent) {
    ASTNode bufferNode = new ASTNode("buffer");
    Factor(bufferNode);
    if (isNextPrimary()) {
      ASTNode newBufferNode = new ASTNode("buffer");
      moveForward();
      _Term(newBufferNode);
      bufferNode.getChildren().forEach(newBufferNode.getChildren().get(0)::addChildToHead);
      bufferNode = newBufferNode;
    }
    bufferNode.getChildren().forEach(parent::addChild);
  }

  private void Factor(ASTNode parent) {
    ASTNode bufferNode = new ASTNode("buffer");
    Primary(bufferNode);
    if (isNextPrimary()) {
      moveForward();
      ASTNode newBufferNode = new ASTNode("buffer");
      _Factor(newBufferNode);
      bufferNode.getChildren().forEach(newBufferNode.getChildren().get(0)::addChildToHead);
      bufferNode = newBufferNode;
    }
    ASTNode node;
    switch (next.getType()) {
      case MULTIPLY:
        node = new ASTNode("*", parent);
        bufferNode.getChildren().forEach(node::addChild);
        moveForward();
        checkTokenType(TokenType.MULTIPLY);
        moveForward();
        Primary(node);
        break;
      case DIV:
        node = new ASTNode("/", parent);
        bufferNode.getChildren().forEach(node::addChild);
        moveForward();
        checkTokenType(TokenType.DIV);
        moveForward();
        Primary(node);
        break;
      case AND:
        node = new ASTNode("and", parent);
        bufferNode.getChildren().forEach(node::addChild);
        moveForward();
        checkTokenType(TokenType.AND);
        moveForward();
        Primary(node);
        break;
      case MOD:
        node = new ASTNode("mod", parent);
        bufferNode.getChildren().forEach(node::addChild);
        moveForward();
        checkTokenType(TokenType.MOD);
        moveForward();
        Primary(node);
        break;
      default:
        bufferNode.getChildren().forEach(parent::addChild);
    }
  }

  private void _Factor(ASTNode parent) {
    ASTNode bufferNode = new ASTNode("buffer", parent);
    Primary(bufferNode);
    if (isNextPrimary()) {
      ASTNode newBufferNode = new ASTNode("buffer", parent);
      moveForward();
      _Factor(newBufferNode);
      bufferNode.getChildren().forEach(newBufferNode.getChildren().get(0)::addChildToHead);
      parent.dropChild(bufferNode);
      bufferNode = newBufferNode;
    }
    bufferNode.getChildren().forEach(parent::addChild);
    parent.dropChild(bufferNode);
  }

  private void Primary(ASTNode parent) {
    ASTNode node;
    switch (current.getType()) {
      case MINUS:
        node = new ASTNode("-", parent);
        moveForward();
        Primary(node);
        break;
      case PLUS:
        node = new ASTNode("+", parent);
        moveForward();
        Primary(node);
        break;
      case NOT:
        node = new ASTNode("not", parent);
        moveForward();
        Primary(node);
        break;
      case EOF:
        node = new ASTNode("eof", parent);
        break;
      case NUMBER:
        node = new ASTNode("<integer>", parent);
        node.addChild(new ASTNode(current.getValue()));
        break;
      case CHAR:
        node = new ASTNode("<char>", parent);
        node.addChild(new ASTNode(current.getValue()));
        break;
      case IDENTIFIER:
        if (next.getType() == TokenType.BRACK_OP) {
          node = new ASTNode("call", parent);
          Name(node);
          moveForward();
          checkTokenType(TokenType.BRACK_OP);
          moveForward();
          Expression(node);
          while (next.getType() == TokenType.COMMA) {
            moveForward();
            checkTokenType(TokenType.COMMA);
            moveForward();
            Expression(node);
          }
          moveForward();
          checkTokenType(TokenType.BRACK_CL);
        } else {
          Name(parent);
        }
        break;
      case BRACK_OP:
        moveForward();
        Expression(parent);
        moveForward();
        checkTokenType(TokenType.BRACK_CL);
        break;
      case SUCC:
        node = new ASTNode("succ", parent);
        moveForward();
        checkTokenType(TokenType.BRACK_OP);
        moveForward();
        Expression(node);
        moveForward();
        checkTokenType(TokenType.BRACK_CL);
        break;
      case CHR:
        node = new ASTNode("chr", parent);
        moveForward();
        checkTokenType(TokenType.BRACK_OP);
        moveForward();
        Expression(node);
        moveForward();
        checkTokenType(TokenType.BRACK_CL);
        break;
      case ORD:
        node = new ASTNode("ord", parent);
        moveForward();
        checkTokenType(TokenType.BRACK_OP);
        moveForward();
        Expression(node);
        moveForward();
        checkTokenType(TokenType.BRACK_CL);
        break;
      case PRED:
        node = new ASTNode("pred", parent);
        moveForward();
        checkTokenType(TokenType.BRACK_OP);
        moveForward();
        Expression(node);
        moveForward();
        checkTokenType(TokenType.BRACK_CL);
        break;
    }
  }

  private void Name(ASTNode parent) {
    checkTokenType(TokenType.IDENTIFIER);
    ASTNode node = new ASTNode("<identifier>",parent);
    node.addChild(new ASTNode(current.getValue()));
  }
}
