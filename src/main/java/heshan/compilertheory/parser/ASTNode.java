package heshan.compilertheory.parser;

import java.util.LinkedList;
import java.util.List;

public class ASTNode {
  private List<ASTNode> children;
  private String value;

  public ASTNode(String value) {
    this.value = value;
  }

  public ASTNode() {
    children = new LinkedList<>();
  }

  //todo: add an special case for identifiers
  public void addChild(ASTNode node) {
    children.add(node);
  }

  public List<ASTNode> getChildren() {
    return children;
  }

  @Override
  public String toString() {
    return String.format("%s(%d)", value, children.size());
  }
}
