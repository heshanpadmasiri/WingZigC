package heshan.compilertheory.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.mock;

class KeyWordAnalyzerTest {

    SymbolTable symbolTable;

    @BeforeEach
    void setUp() {
        symbolTable = mock(SymbolTable.class);
    }

    @Test
    void matchPattern() {
        KeyWordAnalyzer keyWordAnalyzer = new KeyWordAnalyzer(symbolTable);
        Map<String, TokenType> tokenToType = Stream.of(new Object[][] {
                {"program", TokenType.PROGRAM},
                {"var", TokenType.VAR},
                {"const", TokenType.CONST},
                {"type", TokenType.TYPE},
                {"function", TokenType.FUNCTION},
                {"return", TokenType.RETURN},
                {"begin", TokenType.BEGIN},
                {"end", TokenType.END},
                {"output", TokenType.OUTPUT},
                {"if", TokenType.IF},
                {"then", TokenType.THEN},
                {"else", TokenType.ELSE},
                {"while", TokenType.WHILE},
                {"do", TokenType.DO},
                {"case", TokenType.CASE},
                {"of", TokenType.OF},
                {"..", TokenType.CASE_EXP},
                {"otherwise", TokenType.OTHERWISE},
                {"repeat", TokenType.REPEAT},
                {"for", TokenType.FOR},
                {"until", TokenType.UNTIL},
                {"loop", TokenType.LOOP},
                {"pool", TokenType.POOL},
                {"exit", TokenType.EXIT},
                {"read", TokenType.READ},
                {"succ", TokenType.SUCC},
                {"pred", TokenType.PRED},
                {"chr", TokenType.CHR},
                {"ord", TokenType.ORD},
        }).collect(Collectors.toMap(data -> (String) data[0], data ->(TokenType) data[1]));
        tokenToType.forEach((token, type) -> {
            try {
                Token t = keyWordAnalyzer.matchPattern(token);
                assertEquals(t.getType(), type);
                assertEquals(t.getValue(), token);
                assertEquals(t.getId(), -1);
            } catch (FailedToMatchPatternException e) {
                assertNull(e);
            }
        });
    }
}