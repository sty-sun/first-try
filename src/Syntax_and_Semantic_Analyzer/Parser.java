package Syntax_and_Semantic_Analyzer;

import Lexical_analyzer.List;
import Lexical_analyzer.Token;

import javax.lang.model.element.VariableElement;
import java.util.ArrayList;

public class Parser {
    public static int tokenNum = 0;
    public static int offset = 0;
    public static int size = 0;
    public static ArrayList<Variable_stack> vList = new ArrayList<Variable_stack>();

    public static boolean IndexJudge(int tokenNum) {
        if (tokenNum < size) {
            return true;
        } else {
            System.out.println("程序已经运行到头了");
            return false;
        }
    }

    public void runParser(List list) {
        size = list.tokens.size();
        Token token = new Token();
        token = list.tokens.get(tokenNum);
        System.out.println(token.toString());
        if (token.getName().equals("var")) {
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                Declear(list);
            }
        }
        if (!token.getName().equals("begin")) {
            System.out.println("缺少begin");
        } else {
            //begin
            S(list);

        }
    }

    public void Declear(List list) {
        Token token = new Token();
        token = list.tokens.get(tokenNum);
        while (!token.getName().equals("begin")) {
            D_Declear(list);
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                token = list.tokens.get(tokenNum);
            }
            if (token.getName().equals(";")) {
                tokenNum++;
                if (IndexJudge(tokenNum)) {
                    token = list.tokens.get(tokenNum);
                }
            }
        }
    }

    /**
     * 变量声明语句分析
     *
     * @param list
     */
    public void D_Declear(List list) {
        //读入变量声明语句的第一个变量
        Token token = new Token();
        token = list.tokens.get(tokenNum);
        //判断第一个读入的token是否为标识符，进行匹配
        if (token.getCode() == 18) {
            int begin = offset;
            Variable_stack variable_stack = new Variable_stack();
            variable_stack.setIdName(token.getName());
            vList.add(variable_stack);
            offset++;
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                token = list.tokens.get(tokenNum);
            }
            //看是否还有同类型的其他变量一起声明
            while (token.getName().equals(",")) {
                tokenNum++;
                if (IndexJudge(tokenNum)) {
                    token = list.tokens.get(tokenNum);
                }
                Variable_stack variable_stack1 = new Variable_stack();
                variable_stack1.setIdName(token.getName());
                vList.add(variable_stack1);
                offset++;
                tokenNum++;
                if (IndexJudge(tokenNum)) {
                    token = list.tokens.get(tokenNum);
                }
            }
            //判断是否为冒号进行类型判断
            if (token.getName().equals(":")) {
                tokenNum++;
                if (IndexJudge(tokenNum)) {
                    token = list.tokens.get(tokenNum);
                }
                //判断是否符合标准的类型（bool，integer，real）
                if (token.getCode() == 3 || token.getCode() == 9 || token.getCode() == 13) {
                    for (int i = begin; i < offset; i++) {
                        vList.get(i).setType(token.getName());
                        vList.get(i).setOffset(i);
                    }
                }
            }
        }
    }

    public void prinVList() {
        int length = vList.size();
        for (int i = 0; i < length; i++) {
            System.out.println(vList.get(i).toString());
        }
    }

    public void LBegin(List list) {

    }

    /**
     * S
     *
     * @param list
     */
    public static void S(List list) {
        if (list.tokens.get(tokenNum).getName().equals("begin")) {
            //begin
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                L(list);
                if (list.tokens.get(tokenNum).getCode() == 6) {
                    //6 end结束
                    System.out.println("顺利结束");
                    return;
                }
            }
        } else if (list.tokens.get(tokenNum).getName().equals("if")) {
            //if
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                //B布尔表达式
                B(list);
                tokenNum++;
                if (IndexJudge(tokenNum)) {
                    if (list.tokens.get(tokenNum).getCode() == 14) {
                        //then
                        tokenNum++;
                        if (IndexJudge(tokenNum)) {
                            S(list);
                            s1(list);
                            return;
                        }
                    }
                }
            }
        } else if (list.tokens.get(tokenNum).getName().equals("while")) {
            //while
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                B(list);
                tokenNum++;
                if (IndexJudge(tokenNum)) {
                    if (list.tokens.get(tokenNum).getCode() == 4) {
                        //do
                        tokenNum++;
                        if (IndexJudge(tokenNum)) {
                            S(list);
                            return;
                        }
                    }
                }
            }
        } else if (list.tokens.get(tokenNum).getCode() == 18) {
            //i:=E
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                if (list.tokens.get(tokenNum).getCode() == 31) {
                    //:=
                    tokenNum++;
                    if (IndexJudge(tokenNum)) {
                        E(list);
                        return;
                    }
                }
            }
        }
    }

    /**
     * E 算术表达式
     *
     * @param list
     */
    private static void E(List list) {
        F(list);
        E1(list);
        return;
    }

    /**
     * E`
     *
     * @param list
     */
    private static void E1(List list) {
        if (list.tokens.get(tokenNum).getCode() == 23) {
            //+
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                F(list);
                E1(list);
                return;
            }
        } else if (list.tokens.get(tokenNum).getCode() == 24) {
            //-
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                F(list);
                E1(list);
                return;
            }
        } else {
            //空
            return;
        }
    }

    /**
     * F 项
     *
     * @param list
     */
    private static void F(List list) {
        G(list);
        F1(list);
        return;
    }

    /**
     * F`
     *
     * @param list
     */
    private static void F1(List list) {
        if (list.tokens.get(tokenNum).getCode() == 25) {
            //*
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                G(list);
                F1(list);
                return;
            }
        } else if (list.tokens.get(tokenNum).getCode() == 26) {
            //除/
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                G(list);
                F1(list);
                return;
            }
        } else {
            //空
            return;
        }
    }

    /**
     * G 因子
     *
     * @param list
     */
    private static void G(List list) {
        if (list.tokens.get(tokenNum).getCode() == 21) {
            //(左括号
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                E(list);
                tokenNum++;
                if (IndexJudge(tokenNum)) {
                    if (list.tokens.get(tokenNum).getCode() == 22) {
                        //右括号
                        tokenNum++;
                        if (IndexJudge(tokenNum)) {
                            return;
                        }
                    }
                }
            }
        } else {
            H(list);
            return;
        }

    }

    /**
     * H 算术量
     *
     * @param list
     */
    private static void H(List list) {
        if (list.tokens.get(tokenNum).getCode() == 18) {
            //标识符id
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                return;
            }

        } else if (list.tokens.get(tokenNum).getCode() == 19) {
            //整数
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                return;
            }
        } else if (list.tokens.get(tokenNum).getCode() == 20) {
            //实数
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                return;
            }
        }
    }

    /**
     * s`
     *
     * @param list
     */
    private static void s1(List list) {
        if (list.tokens.get(tokenNum).getCode() == 5) {
            //else
            S(list);
        } else {
            //空
            return;
        }
    }

    /**
     * L
     *
     * @param list
     */
    public static void L(List list) {
        S(list);
        if (list.tokens.get(tokenNum).getCode() == 30) {
            //;
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                L(list);
                return;
            }
        } else {
            return;
        }
    }

    /**
     * B布尔表达式
     *
     * @param list
     */
    public static void B(List list) {
        C(list);
        B1(list);
        return;
    }

    /**
     * B`
     *
     * @param list
     */
    private static void B1(List list) {
        if (list.tokens.get(tokenNum).getCode() == 11) {
            //11 or
            C(list);
            B1(list);
            return;
        } else {
            return;
        }
    }

    /**
     * C布尔项
     *
     * @param list
     */
    private static void C(List list) {
        D(list);
        C1(list);
        return;
    }

    /**
     * C`
     *
     * @param list
     */
    private static void C1(List list) {
        if (list.tokens.get(tokenNum).getCode() == 1) {
            //1 and
            D(list);
            C1(list);
            return;
        } else {
            //空
            return;
        }

    }

    /**
     * D布尔因子
     *
     * @param list
     */
    private static void D(List list) {
        //not D
        if (list.tokens.get(tokenNum).getName().equals("not")) {
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                D(list);
                return;
            }
        } else {
            X(list);
            return;
        }
    }

    /**
     * X布尔量
     *
     * @param list
     */
    private static void X(List list) {
        if (list.tokens.get(tokenNum).getCode() == 15) {
            //机内码15 true
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                return;
            }
        } else if (list.tokens.get(tokenNum).getCode() == 7) {
            //机内码7 false
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                return;
            }
        } else if (list.tokens.get(tokenNum).getCode() == 18) {
            //机内码18 标识符id
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                Y(list);
                return;
            }
        } else if (list.tokens.get(tokenNum).getCode() == 21) {
            //机内码21 左括号(
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                B(list);
                tokenNum++;
                if (IndexJudge(tokenNum)) {
                    if (list.tokens.get(tokenNum).getCode() == 22) {
                        //22 右括号)
                        return;
                    } else {
                        //报错
                    }
                }

            }

        } else {
            Y(list);
            return;
        }

    }

    /**
     * Y关系表达式
     *
     * @param list
     */
    private static void Y(List list) {
        R(list);
        if (list.tokens.get(tokenNum).getCode() == 18) {
            //18 标识符
            return;
        } else {
            //报错
        }
        return;

    }

    /**
     * R关系运算符
     *
     * @param list
     */
    private static void R(List list) {
        if (list.tokens.get(tokenNum).getCode() == 34) {
            //34 <
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                return;
            }

        } else if (list.tokens.get(tokenNum).getCode() == 33) {
            //33 <=
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                return;
            }

        } else if (list.tokens.get(tokenNum).getCode() == 32) {
            //32 =
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                return;
            }

        } else if (list.tokens.get(tokenNum).getCode() == 37) {
            //37 >=
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                return;
            }

        } else if (list.tokens.get(tokenNum).getCode() == 36) {
            //36 >
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                return;
            }

        } else if (list.tokens.get(tokenNum).getCode() == 35) {
            //35 <>
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                return;
            }

        } else {
            return;
        }
    }


}
