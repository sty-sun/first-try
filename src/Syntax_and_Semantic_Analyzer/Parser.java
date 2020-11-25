package Syntax_and_Semantic_Analyzer;

import Lexical_analyzer.List;
import Lexical_analyzer.Symble;
import Lexical_analyzer.Token;

import java.util.ArrayList;

public class Parser {
    public static int tokenNum = 0;
    public static int offset = 0;
    public static int size = 0;
    public static ArrayList<Variable_stack> vList = new ArrayList<Variable_stack>();
    public static ArrayList<EQU> equList = new ArrayList<>();
    public static int equNum = 0;
    public static int symNum = 0;
    public static int tempNum = 0;

    public static boolean IndexJudge(int tokenNum) {
        if (tokenNum < size) {
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<EQU> runParser(List list) {
        size = list.tokens.size();
        symNum = list.symbles.size();
        Token token = new Token();
        token = list.tokens.get(tokenNum);
        if (token.getName().equals("var")) {
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                Declear(list);
            }
        }
        token = list.tokens.get(tokenNum);
        if (!token.getName().equals("begin")) {
            System.out.println("缺少begin");
        } else {
            //begin
            S(list);
        }
        return equList;
    }

    public void Declear(List list) {
        Token token = new Token();
        if (IndexJudge(tokenNum)){
            token = list.tokens.get(tokenNum);
        }
        while (!token.getName().equals("begin")) {
            D_Declear(list);
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                token = list.tokens.get(tokenNum);
            } else {
                System.out.println("程序已经到头了");
                break;
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
        if (IndexJudge(tokenNum)) {
            token = list.tokens.get(tokenNum);
        }
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
                } else {
                    break;
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

    /**
     * S
     *
     * @param list
     */
    public static TrueOrFalse S(List list) {
        //S.next其中true作为next
        TrueOrFalse Snext = new TrueOrFalse();
        if (list.tokens.get(tokenNum).getName().equals("begin")) {
            //begin
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                Snext = L(list);
                if (list.tokens.get(tokenNum).getCode() == 6) {
                    //6 end结束
                    System.out.println("顺利结束");
                    return Snext;
                }
            }
        } else if (list.tokens.get(tokenNum).getName().equals("if")) {
            //if
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                //B布尔表达式
                //E.tof
                TrueOrFalse trueOrFalse = B(list);
                if (IndexJudge(tokenNum)) {
                    if (list.tokens.get(tokenNum).getCode() == 14) {
                        //then
                        int m = equNum;
                        backPatch(trueOrFalse.getTrueEqu(), m);
                        tokenNum++;
                        if (IndexJudge(tokenNum)) {
                            //S1.next
                            TrueOrFalse next1 = S(list);
                            s1(list, trueOrFalse, Snext, next1);
                            return Snext;
                        }
                    }
                }
            }
        } else if (list.tokens.get(tokenNum).getName().equals("while")) {
            //while
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                //E.tof
//                int m1=equNum+1;
                int m1 = equNum;
                TrueOrFalse trueOrFalse = B(list);
                if (IndexJudge(tokenNum)) {
                    if (list.tokens.get(tokenNum).getCode() == 4) {
                        //do
                        int m2 = equNum;
                        tokenNum++;
                        if (IndexJudge(tokenNum)) {
                            //S1.next
                            TrueOrFalse next1 = S(list);
                            backPatch(next1.getTrueEqu(), m1);
                            backPatch(trueOrFalse.getTrueEqu(), m2);
                            Snext.setTrueEqu(trueOrFalse.getFalseEqu());
                            //j,_,_,M1.quad
                            nextQuad();
                            equList.get(equNum).setOp(52);
                            equList.get(equNum).setOp1(-1);
                            equList.get(equNum).setOp2(-1);
                            equList.get(equNum).setResult(m1);
                            equNum++;
                            return Snext;
                        }
                    }
                }
            }
        } else if (list.tokens.get(tokenNum).getCode() == 18) {
            //i:=E
            int temp = tokenNum;
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                if (list.tokens.get(tokenNum).getCode() == 31) {
                    //:=
                    tokenNum++;
                    if (IndexJudge(tokenNum)) {
                        Place Eplace = E(list);
                        nextQuad();
                        equList.get(equNum).setOp(51);
                        equList.get(equNum).setOp1(Eplace.getAddress());
                        equList.get(equNum).setOp2(-1);
                        equList.get(equNum).setResult(list.tokens.get(temp).getAddress());
                        equNum++;
                        return Snext;
                    }
                }
            }
        }
        return Snext;
    }

    /**
     * E 算术表达式
     *
     * @param list
     */
    private static Place E(List list) {
        Place Fplace = F(list);
        Place E1place = E1(list, Fplace);
        return E1place;
    }

    /**
     * E`
     *
     * @param list
     */
    private static Place E1(List list, Place outPlace) {
        if (list.tokens.get(tokenNum).getCode() == 23) {
            //+
            Place place = new Place();
            newTmep(list, place);
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                Place Fplace = F(list);
                Place E1place = E1(list, Fplace);
                nextQuad();
                equList.get(equNum).setOp(43);
                equList.get(equNum).setOp1(outPlace.getAddress());
                equList.get(equNum).setOp2(E1place.getAddress());
                equList.get(equNum).setResult(place.getAddress());
                equNum++;
                return place;
            }
        } else if (list.tokens.get(tokenNum).getCode() == 24) {
            //-
            Place place = new Place();
            newTmep(list, place);
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                Place Fplace = F(list);
                Place E1place = E1(list, Fplace);
                equNum++;
                nextQuad();
                equList.get(equNum).setOp(45);
                equList.get(equNum).setOp1(outPlace.getAddress());
                equList.get(equNum).setOp2(E1place.getAddress());
                equList.get(equNum).setResult(place.getAddress());
                return place;
            }
        } else {
            //空
            return outPlace;
        }
        return null;
    }

    /**
     * F 项
     *
     * @param list
     */
    private static Place F(List list) {
        Place Gplace;
        Gplace = G(list);
        Place F1place = F1(list, Gplace);
        return F1place;
    }

    /**
     * F`
     *
     * @param list
     */
    private static Place F1(List list, Place outPlace) {
        if (list.tokens.get(tokenNum).getCode() == 25) {
            Place place = new Place();
            newTmep(list, place);
            //*
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                Place Gplace;
                Gplace = G(list);
                Place F1place;
                F1place = F1(list, Gplace);
                equNum++;
                nextQuad();
                equList.get(equNum).setOp(41);
                equList.get(equNum).setOp1(outPlace.getAddress());
                equList.get(equNum).setOp2(F1place.getAddress());
                equList.get(equNum).setResult(place.getAddress());
                return place;
            }
        } else if (list.tokens.get(tokenNum).getCode() == 26) {
            //除/
            Place place = new Place();
            newTmep(list, place);
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                Place Gplace;
                Gplace = G(list);
                Place F1place;
                F1place = F1(list, Gplace);
                equNum++;
                nextQuad();
                equList.get(equNum).setOp(48);
                equList.get(equNum).setOp1(outPlace.getAddress());
                equList.get(equNum).setOp2(F1place.getAddress());
                equList.get(equNum).setResult(place.getAddress());
                return place;
            }
        } else {
            //空
            return outPlace;
        }
        return null;
    }

    /**
     * G 因子
     *
     * @param list
     */
    private static Place G(List list) {
        Place place = new Place();
        if (list.tokens.get(tokenNum).getCode() == 21) {
            //(左括号
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                place = E(list);
                tokenNum++;
                if (IndexJudge(tokenNum)) {
                    if (list.tokens.get(tokenNum).getCode() == 22) {
                        //右括号
                        tokenNum++;
                        if (IndexJudge(tokenNum)) {
                            return place;
                        }
                    }
                }
            }
        } else {
            place = H(list);
            return place;
        }
        return place;
    }

    /**
     * H 算术量
     *
     * @param list
     */
    private static Place H(List list) {
        Place place = new Place();
        if (list.tokens.get(tokenNum).getCode() == 18) {
            //标识符id
            place.setAddress(list.tokens.get(tokenNum).getAddress());
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                return place;
            }

        } else if (list.tokens.get(tokenNum).getCode() == 19) {
            //整数
            place.setAddress(list.tokens.get(tokenNum).getAddress());
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                return place;
            }
        } else if (list.tokens.get(tokenNum).getCode() == 20) {
            //实数
            place.setAddress(list.tokens.get(tokenNum).getAddress());
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                return place;
            }
        }
        return place;
    }

    /**
     * s`
     *
     * @param list
     */
    private static void s1(List list, TrueOrFalse trueOrFalse, TrueOrFalse Snext, TrueOrFalse next1) {
        if (list.tokens.get(tokenNum).getCode() == 5) {
            //else
            int n = equNum;
            nextQuad();
            equList.get(equNum).setOp(52);
            equList.get(equNum).setOp1(-1);
            equList.get(equNum).setOp2(-1);
            equList.get(equNum).setResult(0);
            equNum++;
            int m2 = equNum;
            backPatch(trueOrFalse.getFalseEqu(), m2);
            //S2.next
            tokenNum++;
            if (IndexJudge(tokenNum)){
                TrueOrFalse next2 = S(list);
                Snext.setTrueEqu(n);
                Snext.setTrueEqu(next2.getTrueEqu());
                Snext.setTrueEqu(next1.getTrueEqu());
            }
        } else {
            //空
            Snext.setTrueEqu(trueOrFalse.getFalseEqu());
            Snext.setTrueEqu(next1.getTrueEqu());
            return;
        }
    }

    /**
     * L
     *
     * @param list
     */
    public static TrueOrFalse L(List list) {
        TrueOrFalse Lnext = new TrueOrFalse();
        Lnext = S(list);
        if (list.tokens.get(tokenNum).getCode() == 30) {
            //;
            tokenNum++;
            int m = equNum;
            backPatch(Lnext.getTrueEqu(), m);
            if (IndexJudge(tokenNum)) {
                Lnext = L(list);
                return Lnext;
            }
        } else {
            return Lnext;
        }
        return null;
    }

    /**
     * B布尔表达式
     *
     * @param list
     */
    public static TrueOrFalse B(List list) {
        //E1.tof
        TrueOrFalse trueOrFalse1;
        trueOrFalse1 = C(list);
        TrueOrFalse EtrueOrFalse = B1(list, trueOrFalse1);
        return EtrueOrFalse;
    }

    /**
     * B`
     *
     * @param list
     */
    private static TrueOrFalse B1(List list, TrueOrFalse trueOrFalse1) {
        if (list.tokens.get(tokenNum).getCode() == 11) {
            //E.tof
            TrueOrFalse EtrueOrFalse = new TrueOrFalse();
            //11 or
            int m = equNum;
            backPatch(trueOrFalse1.getFalseEqu(), m);
            //E2.tof
            TrueOrFalse trueOrFalse2;
            //E11.tof
            TrueOrFalse trueOrFalse11;
            trueOrFalse11 = C(list);
            trueOrFalse2 = B1(list, trueOrFalse11);
            EtrueOrFalse.setTrueEqu(trueOrFalse1.getTrueEqu());
            EtrueOrFalse.setTrueEqu(trueOrFalse2.getTrueEqu());
            EtrueOrFalse.setFalseEqu(trueOrFalse2.getFalseEqu());
            return EtrueOrFalse;
        } else {
            return trueOrFalse1;
        }
    }

    /**
     * C布尔项
     *
     * @param list
     */
    private static TrueOrFalse C(List list) {
        //E.trueorfalse
        TrueOrFalse EtrueOrFalse;
        //E1.trueorfalse
        TrueOrFalse trueOrFalse1;
        trueOrFalse1 = D(list);
        EtrueOrFalse = C1(list, trueOrFalse1);
        return EtrueOrFalse;
    }

    /**
     * C`
     *
     * @param list
     */
    private static TrueOrFalse C1(List list, TrueOrFalse trueOrFalse1) {
        if (list.tokens.get(tokenNum).getCode() == 1) {
            //1 and
            //E.trueorfalse
            TrueOrFalse ETrueOrFalse = new TrueOrFalse();
            int m = equNum;
            backPatch(trueOrFalse1.getTrueEqu(), m);
            //E11.trueorfalse
            tokenNum++;
            if (IndexJudge(tokenNum)){
                TrueOrFalse trueOrFalse11 = D(list);
                TrueOrFalse trueOrFalse2 = C1(list, trueOrFalse11);
                //E.true=E2.true
                ETrueOrFalse.setTrueEqu(trueOrFalse2.getTrueEqu());
                //E.false=merge
                ETrueOrFalse.setFalseEqu(trueOrFalse1.getFalseEqu());
                ETrueOrFalse.setFalseEqu(trueOrFalse2.getFalseEqu());
                return ETrueOrFalse;
            }
        } else {
            //空
            return trueOrFalse1;
        }
        return null;
    }

    /**
     * D布尔因子
     *
     * @param list
     */
    private static TrueOrFalse D(List list) {
        //not D
        if (list.tokens.get(tokenNum).getName().equals("not")) {
            tokenNum++;
            TrueOrFalse trueOrFalse = new TrueOrFalse();
            TrueOrFalse trueOrFalse1 = null;
            if (IndexJudge(tokenNum)) {
                trueOrFalse1 = D(list);
                trueOrFalse.setTrueEqu(trueOrFalse1.getFalseEqu());
                trueOrFalse.setFalseEqu(trueOrFalse1.getTrueEqu());
                return trueOrFalse;
            }
        } else {
            TrueOrFalse trueOrFalse;
            trueOrFalse = X(list);
            return trueOrFalse;
        }
        return null;
    }

    /**
     * X布尔量
     *
     * @param list
     */
    private static TrueOrFalse X(List list) {
        if (list.tokens.get(tokenNum).getCode() == 15) {
            //机内码15 true
            //j
            TrueOrFalse trueOrFalse = new TrueOrFalse(equNum, -1);
            nextQuad();
            int tempNum = equNum;
            equList.get(equNum).setOp(52);
            equList.get(equNum).setResult(0);
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                return trueOrFalse;
            }
        } else if (list.tokens.get(tokenNum).getCode() == 7) {
            //机内码7 false
            //j
            TrueOrFalse trueOrFalse = new TrueOrFalse(-1, equNum);
            int tempNum = equNum;
            nextQuad();
            equList.get(equNum).setOp(52);
            equList.get(equNum).setResult(0);
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                return trueOrFalse;
            }
        } else if (list.tokens.get(tokenNum).getCode() == 18) {
            //机内码18 标识符id
            TrueOrFalse trueOrFalse = new TrueOrFalse();
            trueOrFalse.setTrueEqu(equNum);
            nextQuad();
            equList.get(equNum).setOp1(list.tokens.get(tokenNum).getAddress());
            equList.get(equNum).setResult(0);
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                trueOrFalse = Y(list, trueOrFalse);
                return trueOrFalse;
            }
        } else if (list.tokens.get(tokenNum).getCode() == 21) {
            //机内码21 左括号(
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                TrueOrFalse trueOrFalse = new TrueOrFalse();
                trueOrFalse = B(list);
                tokenNum++;
                if (IndexJudge(tokenNum)) {
                    if (list.tokens.get(tokenNum).getCode() == 22) {
                        //22 右括号)
                        return trueOrFalse;
                    } else {
                        //报错
                    }
                }

            }

        }
        return null;
    }

    /**
     * Y关系表达式
     *
     * @param list
     * @param trueOrFalse
     */
    private static TrueOrFalse Y(List list, TrueOrFalse trueOrFalse) {
        trueOrFalse = R(list, trueOrFalse);
        if (list.tokens.get(tokenNum).getCode() == 18) {
            //18 标识符
            equList.get(equNum).setOp2(list.tokens.get(tokenNum).getAddress());
            equNum++;
            tokenNum++;
            //.false
            trueOrFalse.setFalseEqu(equNum);
            nextQuad();
            equList.get(equNum).setOp(52);
            equList.get(equNum).setOp1(-1);
            equList.get(equNum).setOp2(-1);
            equList.get(equNum).setResult(0);
            equNum++;
            return trueOrFalse;
        } else {
            //报错
        }
        return trueOrFalse;

    }

    /**
     * R关系运算符
     *
     * @param list
     */
    private static TrueOrFalse R(List list, TrueOrFalse trueOrFalse) {
        if (list.tokens.get(tokenNum).getCode() == 34) {
            //34 <
            equList.get(equNum).setOp(53);
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                return trueOrFalse;
            }
        } else if (list.tokens.get(tokenNum).getCode() == 33) {
            //33 <=
            equList.get(equNum).setOp(54);
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                return trueOrFalse;
            }

        } else if (list.tokens.get(tokenNum).getCode() == 32) {
            //32 =
            equList.get(equNum).setOp(56);
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                return trueOrFalse;
            }

        } else if (list.tokens.get(tokenNum).getCode() == 37) {
            //37 >=
            equList.get(equNum).setOp(58);
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                return trueOrFalse;
            }

        } else if (list.tokens.get(tokenNum).getCode() == 36) {
            //36 >
            equList.get(equNum).setOp(57);
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                return trueOrFalse;
            }

        } else if (list.tokens.get(tokenNum).getCode() == 35) {
            //35 <>
            equList.get(equNum).setOp(55);
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                return trueOrFalse;
            }

        } else {
            //本来应该是jnz暂时用j代替
            equList.get(equNum).setOp(52);
            equList.get(equNum).setOp2(-1);
            equNum++;
            trueOrFalse.setFalseEqu(equNum);
            int equTemp = equNum;
            nextQuad();
            equList.get(equNum).setOp(52);
            equList.get(equNum).setOp1(-1);
            equList.get(equNum).setOp2(-1);
            equList.get(equNum).setResult(0);
            equNum++;
            return trueOrFalse;
        }
        return trueOrFalse;
    }

    public static void nextQuad() {
        EQU equ = new EQU();
        equ.setLabel(equNum);
        equList.add(equ);
    }

    public static void backPatch(ArrayList<Integer> trueOrFalse, int m) {
        for (Integer item : trueOrFalse) {
            //回填
            equList.get(item).setResult(m);
        }
    }

    public static void newTmep(List list, Place place) {
        String temp = "T" + tempNum;
        tempNum++;
        symNum++;
        Symble symble = new Symble();
        symble.setName(temp);
        symble.setNumber(symNum);
        //0表示临时变量
        symble.setType(0);
        list.symbles.add(symble);
        place.setAddress(symNum);
    }


}
