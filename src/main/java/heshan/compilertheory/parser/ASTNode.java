package heshan.compilertheory.parser;

import java.util.LinkedList;
import java.util.List;

public class ASTNode {
  private List<ASTNode> children;
  private String value;

  public ASTNode(String value) {
    this.value = value;
    children = new LinkedList<>();
  }

  public ASTNode(String value, ASTNode parent){
    this.value = value;
    children = new LinkedList<>();
    parent.addChild(this);
  }

  public void addChild(ASTNode node) {
    children.add(node);
  }

  public void addChildToHead(ASTNode node){
    children.add(0, node);
  }

  public List<ASTNode> getChildren() {
    return children;
  }

  public void dropChild(ASTNode node){
    children.remove(node);
  }

  @Override
  public String toString() {
    return String.format("%s(%d)", value, children.size());
  }
}
