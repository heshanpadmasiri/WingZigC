package heshan.compilertheory.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class PunctuationAnalyzerTest {

    SymbolTable symbolTable;

    @BeforeEach
    void setUp() {
        symbolTable = mock(SymbolTable.class);
    }

    @Test
    void matchPattern() {
        PunctuationAnalyzer punctuationAnalyzer = new PunctuationAnalyzer(symbolTable);
        Map<String, TokenType> tokenToType = Stream.of(new Object[][] {
                {"eof", TokenType.EOF},
                {":", TokenType.COL},
                {";", TokenType.SCOL},
                {".", TokenType.DOT},
                {",", TokenType.COMMA},
                {"(", TokenType.BRACK_OP},
                {")", TokenType.BRACK_CL},
        }).collect(Collectors.toMap(data -> (String) data[0], data-> (TokenType) data[1]));
        tokenToType.forEach((token, type) -> {
            try {
                Token t = punctuationAnalyzer.matchPattern(token);
                assertEquals(t.getType(), type);
                assertEquals(t.getValue(), token);
                assertEquals(t.getId(), -1);
            } catch (FailedToMatchPatternException e) {
                assertNull(e);
            }
        });
    }
}