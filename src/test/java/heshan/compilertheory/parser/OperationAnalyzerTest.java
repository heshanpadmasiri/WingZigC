package heshan.compilertheory.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class OperationAnalyzerTest {

    SymbolTable symbolTable;

    @BeforeEach
    void setUp() {
        symbolTable = mock(SymbolTable.class);
    }

    @Test
    void matchPattern() {
        OperationAnalyzer operationAnalyzer = new OperationAnalyzer(symbolTable);
        Map<String, TokenType> tokenToType = Stream.of(new Object[][] {
                {":=:", TokenType.SWAP},
                {":=", TokenType.ASSIGN},
                {"<=", TokenType.LEQ},
                {"<>", TokenType.NEQ},
                {"<", TokenType.LT},
                {">=", TokenType.GEQ},
                {">", TokenType.GT},
                {"=", TokenType.EQ},
                {"mod", TokenType.MOD},
                {"and", TokenType.AND},
                {"or", TokenType.OR},
                {"not", TokenType.NOT},
                {"+", TokenType.PLUS},
                {"-", TokenType.MINUS},
                {"*", TokenType.MULTIPLY},
                {"/", TokenType.DIV},
        }).collect(Collectors.toMap(data -> (String) data[0], data-> (TokenType) data[1]));
        tokenToType.forEach((token, type) -> {
            try {
                Token t = operationAnalyzer.matchPattern(token);
                assertEquals(t.getType(), type);
                assertEquals(t.getValue(), token);
                assertEquals(t.getId(), -1);
            } catch (FailedToMatchPatternException e) {
                assertNull(e);
            }
        });
    }
}