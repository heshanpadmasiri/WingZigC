package heshan.compilertheory.parser;

public enum TokenType {
    // keywords
    PROGRAM,
    VAR,
    CONST,
    TYPE,
    FUNCTION,
    RETURN,
    BEGIN,
    END,
    OUTPUT,
    IF,
    THEN,
    ELSE,
    WHILE,
    DO,
    CASE,
    OF,
    OTHERWISE,
    REPEAT,
    FOR,
    UNTIL,
    LOOP,
    POOL,
    EXIT,
    READ,
    SUCC,
    PRED,
    CASE_EXP,

    //keyword functions
    CHR,
    ORD,

    //operators
    SWAP,
    ASSIGN,
    LEQ,
    NEQ,
    LT,
    GEQ,
    GT,
    EQ,
    MOD,
    AND,
    OR,
    NOT,
    PLUS,
    MINUS,
    MULTIPLY,
    DIV,

    IDENTIFIER,
    // CONST
    NUMBER,
    CHAR,
    STRING,

    //punctuations
    EOF,
    COL,
    SCOL,
    DOT,
    COMMA,
    BRACK_OP,
    BRACK_CL,

    FILE_END

}
