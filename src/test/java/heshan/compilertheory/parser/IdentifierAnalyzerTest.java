package heshan.compilertheory.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class IdentifierAnalyzerTest {

    SymbolTable symbolTable;
    @BeforeEach
    void setUp() {
        symbolTable = mock(SymbolTable.class);
        when(symbolTable.getNextKey()).thenReturn(0);
    }

    @Test
    void matchPattern() {
        IdentifierAnalyzer identifierAnalyzer = new IdentifierAnalyzer(symbolTable);
        Map<String, Boolean> idSuccess = Stream.of(new Object[][] {
                {"h", true},
                {"H", true},
                {"_", true},
                {"7", false},
                {"hH", true},
                {"hh", true},
                {"h_", true},
                {"h7", true},
                {"HH", true},
                {"Hh", true},
                {"H_", true},
                {"H7", true},
                {"_H", true},
                {"_h", true},
                {"__", true},
                {"_7", true},
                {"7H", false},
                {"7h", false},
                {"7_", false},
                {"77", false},
        }).collect(Collectors.toMap(data -> (String) data[0], data-> (Boolean) data[1]));
        idSuccess.forEach((token, success) -> {
            try {
                Token t = identifierAnalyzer.matchPattern(token);
                assertTrue(success);
                assertEquals(t.getType(), TokenType.IDENTIFIER);
                assertEquals(t.getValue(), token);
                assertEquals(t.getId(), 0);
            } catch (FailedToMatchPatternException e) {
                if(success){
                    assertNull(e);
                } else {
                    assertNotNull(e);
                }
            }
        });
    }
}