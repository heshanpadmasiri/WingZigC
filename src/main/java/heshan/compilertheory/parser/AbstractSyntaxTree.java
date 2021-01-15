package heshan.compilertheory.parser;

import java.util.Collections;

public class AbstractSyntaxTree {
  private ASTNode root;

  public AbstractSyntaxTree(ASTNode root) {
    this.root = root;
  }

  private void printNode(ASTNode node, int level) {
    System.out.println(
        String.format("%s%s", String.join("", Collections.nCopies(level, ". ")), node));
    for (ASTNode child : node.getChildren() ) {
       printNode(child, level+1);
    }
  }

  public void printTree() {
    printNode(root, 0);
  }


  public ASTNode getRoot() {
    return root;
  }
}
