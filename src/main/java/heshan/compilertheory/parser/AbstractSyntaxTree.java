package heshan.compilertheory.parser;

public class AbstractSyntaxTree {
    private ASTNode root;

    public AbstractSyntaxTree(ASTNode root) {
        this.root = root;
    }

    public void printTree(){}

    public ASTNode getRoot(){
        return root;
    }
}
