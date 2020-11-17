package Experiment;

public enum InernalCode {
    AND("and",1),
    BEGIN("begin",2),
    BOOL("bool",3),
    DO("do",4),
    ELSE("else",5),
    END("end",6),
    FALSE("false",7),
    IF("if",8),
    INTEGER("integer",9),
    NOT("not",10),
    OR("or",11),
    PROGRAM("program",12),
    REAL("real",13),
    THEN("then",14),
    TRUE("true",15),
    VAR("var",16),
    WHILE("while",17),
    SYMPLE("标识符",18),
    INT("整数",19),
    REALL("实数",20),
    LIFT("(",21),
    RIGHT(")",22),
    ADD("+",23),
    SUB("-",24),
    MUL("*",25),
    DIV("/",26),
    POINT(".",27),
    COMMA(",",28),
    COLON(":",29),
    SEMICOLON(";",30),
    ASSIGNMENT(":=",31),
    EQUAL("=",32),
    LE("<=",33),
    LESS("<",34),
    UNEQUAL("<>",35),
    BIGGER(">",36),
    BE(">=",37);
    private final String word;
    private final int num;
    InernalCode(String word, int num) {
        this.word = word;
        this.num = num;
    }

    public int getNum() {
        return num;
    }

    public String getWord() {
        return word;
    }

    public static String getWord(int num){
        for (InernalCode inernal_code:values()){
            if (inernal_code.getNum() == num){
                return inernal_code.getWord();
            }
        }
        throw new RuntimeException("没有找到对应的枚举");
    }

    public static int getNum(String word){
        for (InernalCode inernal_code:values()){
            if (inernal_code.getWord().equals(word)){
                return inernal_code.getNum();
            }
        }
        throw new RuntimeException("没有找到对应的枚举");
    }
}
