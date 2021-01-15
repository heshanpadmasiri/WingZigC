package heshan.compilertheory.parser;

import java.util.LinkedList;
import java.util.List;

public class ASTNode {
  private List<ASTNode> children;
  private Token token;

  public ASTNode(Token token) {
    this.token = token;
  }

  public ASTNode() {
    children = new LinkedList<>();
  }

  public void addChild(ASTNode node) {
    children.add(node);
  }

  public List<ASTNode> getChildren() {
    return children;
  }

  @Override
  public String toString() {
    return String.format("%s(%d)", token.getType(), children.size());
  }
}
