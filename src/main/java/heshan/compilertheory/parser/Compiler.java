package heshan.compilertheory.parser;

import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Compiler {
    private List<String> program;
    private AbstractSyntaxTree ast;
    private SymbolTable symbolTable;

    public Compiler(AbstractSyntaxTree ast) {
        this.ast = ast;
        symbolTable = new SymbolTable();
        program = new LinkedList<>();
        compile(ast.getRoot(), new AttributeNode(0, 0));
    }

    private void compile(ASTNode astNode, AttributeNode attributeNode) {
        String code = null;
        int n = attributeNode.getNext();
        int top = attributeNode.getTop();
        switch (astNode.getValue()) {
            case "<char>":
            case "<integer>":
                int value = Integer.parseInt(astNode.getChildren().get(0).getValue());
                code = String.format("lit %d", value);
                n = attributeNode.getNext() + 1;
                top = attributeNode.getTop() + 1;
                break;
            case "<identifier>":
                String name = astNode.getChildren().get(0).getValue();
                code = String.format("load %d", symbolTable.getLocation(name));
                n = attributeNode.getNext() + 1;
                top = attributeNode.getTop() + 1;
                break;
            case "const":
                AttributeNode valueNode = new AttributeNode(attributeNode);
                compile(astNode.getChildren().get(1), valueNode);
                String variableName = astNode.getChildren().get(0).getChildren().get(0).getValue();
                symbolTable.addSymbol(variableName, DataType.INT, valueNode.top);
                n = valueNode.next;
                top = valueNode.top;
                break;
            case "consts":
                AttributeNode last = attributeNode;
                for (ASTNode constNode : astNode.getChildren()) {
                    last = new AttributeNode(last);
                    compile(constNode, last);
                }
                n = last.next;
                top = last.top;
                break;
            case "-":
                if (astNode.getChildren().size() == 1) {
                    // primary
                    AttributeNode child = new AttributeNode(attributeNode);
                    compile(astNode.getChildren().get(0), child);
                    code = "neg";
                    n = child.next + 1;
                    top = child.top;
                } else {
                    // term
                    AttributeNode termNode = new AttributeNode(attributeNode);
                    compile(astNode.getChildren().get(0), termNode);
                    AttributeNode factorNode = new AttributeNode(termNode);
                    compile(astNode.getChildren().get(1), factorNode);
                    code = "sub";
                    n = factorNode.getNext() + 1;
                    top = factorNode.top - 1;
                }
                break;
            case "+":
                if (astNode.getChildren().size() == 1) {
                    //primary
                    AttributeNode child = new AttributeNode(attributeNode);
                    compile(astNode.getChildren().get(0), child);
                    n = child.next;
                    top = child.top;
                } else {
                    // term
                    AttributeNode termNode = new AttributeNode(attributeNode);
                    compile(astNode.getChildren().get(0), termNode);
                    AttributeNode factorNode = new AttributeNode(termNode);
                    compile(astNode.getChildren().get(1), factorNode);
                    code = "add";
                    n = factorNode.getNext() + 1;
                    top = factorNode.top - 1;
                }
                break;
            case "not":
                AttributeNode child = new AttributeNode(attributeNode);
                compile(astNode.getChildren().get(0), child);
                code = "not";
                n = child.next + 1;
                top = child.top;
                break;
            case "*":
            case "/":
            case "mod":
            case "and":
                String op;
                switch (astNode.getValue()) {
                    case "*":
                        op = "mul";
                        break;
                    case "/":
                        op = "div";
                        break;
                    case "mod":
                        op = "mod";
                        break;
                    case "and":
                        op = "and";
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + astNode.getValue());
                }
                AttributeNode factorNode = new AttributeNode(attributeNode);
                compile(astNode.getChildren().get(0), factorNode);
                AttributeNode primaryNode = new AttributeNode(factorNode);
                compile(astNode.getChildren().get(1), primaryNode);
                code = op;
                n = primaryNode.next + 1;
                top = primaryNode.top - 1;
                break;
            case "or":
            case "<=":
            case "<":
            case ">=":
            case ">":
            case "=":
            case "<>":
                AttributeNode leftChild = new AttributeNode(attributeNode);
                compile(astNode.getChildren().get(0), leftChild);
                AttributeNode rightChild = new AttributeNode(leftChild);
                compile(astNode.getChildren().get(1), rightChild);
                switch (astNode.getValue()) {
                    case "or":
                        code = "or";
                        break;
                    case "<=":
                        code = "leq";
                        break;
                    case "<":
                        code = "lt";
                        break;
                    case ">=":
                        code = "geq";
                        break;
                    case ">":
                        code = "gt";
                        break;
                    case "=":
                        code = "eq";
                        break;
                    case "<>":
                        code = "neq";
                        break;
                }
                n = rightChild.getNext() + 1;
                top = rightChild.top - 1;
                break;
            case "assign":
                String var_name = astNode.getChildren().get(0).getChildren().get(0).getValue();
                AttributeNode exp_node = new AttributeNode(attributeNode);
                compile(astNode.getChildren().get(1), exp_node);
                code = String.format("save %d", symbolTable.getLocation(var_name));
                n = exp_node.getNext() + 1;
                top = exp_node.top - 1;
                break;
            case "var":
                int _n = astNode.getChildren().size();
                List<String> variableNames = astNode.getChildren()
                        .subList(0, _n - 1).stream()
                        .map(node -> node.getChildren().get(0).getValue()).collect(Collectors.toList());
                String type = astNode.getChildren().get(_n - 1).getChildren().get(0).getValue();
                top = attributeNode.top;
                for (String _variableName : variableNames) {
                    DataType dataType = type.equals("integer") ? DataType.INT : DataType.CHAR;
                    symbolTable.addSymbol(_variableName, dataType, top + 1);
                    top++;
                    program.add("lit -1");
                }
                break;
            case "block":
            case "dclns":
                AttributeNode _last = attributeNode;
                for (ASTNode _child : astNode.getChildren()) {
                    _last = new AttributeNode(_last);
                    compile(_child, _last);
                }
                n = _last.getNext();
                top = _last.top;
                break;
            case "program":
                List<ASTNode> children = astNode.getChildren();
                AttributeNode constNode = new AttributeNode(attributeNode);
                compile(children.get(1), constNode);
                AttributeNode dclnsNode = new AttributeNode(constNode);
                compile(children.get(3), dclnsNode);
                AttributeNode bodyNode = new AttributeNode(dclnsNode);
                compile(children.get(5), bodyNode);
                code = "stop";
                n = bodyNode.getNext() + 1;
                top = bodyNode.top;
                break;
            default:
                if (astNode.getChildren().size() == 0) {
                    System.out.println("Silently ignoring node " + astNode.toString());
                } else {
                    throw new IllegalStateException("Unexpected value: " + astNode.getValue());
                }
        }
        if (code != null) {
            program.add(code);
        }
        attributeNode.update(code, n, top);
    }

    public List<String> getProgram() {
        return program;
    }

    public void toFile(Path outputPath) {

    }


    private class AttributeNode {
        String code;
        int next;
        int top;

        public AttributeNode(int next, int top) {
            this.next = next;
            this.top = top;
        }

        public AttributeNode(AttributeNode other) {
            this.next = other.next;
            this.top = other.top;
        }

        public String getCode() {
            return code;
        }


        public int getNext() {
            return next;
        }


        public int getTop() {
            return top;
        }

        private void update(String code, int next, int top) {
            this.code = code;
            this.next = next;
            this.top = top;
        }

    }
}

