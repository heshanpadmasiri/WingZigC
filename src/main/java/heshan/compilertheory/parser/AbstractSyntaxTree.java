package heshan.compilertheory.parser;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
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

  private void writeNode(ASTNode node, int level, FileWriter fileWriter) throws IOException {
    fileWriter.write(
            String.format("%s%s\n", String.join("", Collections.nCopies(level, ". ")), node));
    for (ASTNode child : node.getChildren() ) {
      writeNode(child, level+1, fileWriter);
    }
  }

  public void printTree() {
    printNode(root, 0);
  }

  public void toFile(Path outputFile) throws IOException {
   if(outputFile.toFile().exists()){
     System.out.println("Overwriting existing output file");
     outputFile.toFile().delete();
   }
   FileWriter fileWriter = new FileWriter(outputFile.toString());
   writeNode(root, 0, fileWriter);
   fileWriter.close();
  }

  public ASTNode getRoot() {
    return root;
  }
}
