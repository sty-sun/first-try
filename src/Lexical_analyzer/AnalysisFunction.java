package Lexical_analyzer;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

public class AnalysisFunction {
    /**
     * 错误列表变量
     */
    public static ArrayList<WrongList> wrongLists = new ArrayList<WrongList>();
    //符号表序号
    public static int symbleNum=1;
    //token表序号
    public static int tokenNum=1;

    public static void preProcess(String string) {
        //可以替换大部分空白字符， 不限于空格 ；
        string = string.replaceAll("\\s*", "");
    }

    /**
     * 字符判断程序
     * @param readMap
     */
    public List judge(Map<Integer, String> readMap){
        //存放token表和symble表的类实例化
        List list=new List();
        //line里面存储的是读到的第几行，line从1开始的；
        for (int line=1;line<=readMap.size();line++) {
            String string = readMap.get(line);
            //将这一行当中所有的字符全部存储在一个字符数组中；
            char[] chars = new char[string.length()];
            string.getChars(0, string.length(), chars, 0);
            for (int i = 0; i < chars.length; i++) {
                //关键字和标识符分析程序
                if (Character.isLowerCase(chars[i]) || Character.isUpperCase(chars[i])) {
                    i=LetterFunction(i,chars,list);
                } else if (Character.isDigit(chars[i])) {
                    i = DigitFunction(chars,i,line,list);
                } else {
                    if (chars[i]==' '){

                    }else {
                        i = OtherLetterFunction(chars,line, i, list);
                    }
                }
            }
        }
        ErrorMessage();
        return list;
    }

    /**
     *关键字或标识符判断
     * @param head 单词开始下标
     * @param chars 要识别的行
     * @param list 表实例
     * @return
     */
    public static int LetterFunction(int head,char[] chars,List list){
        //单词最后一个字符的下标
        int rear=head;
        //识别出的字符串
        String judgeStr="";
        //字符串对应的枚举类中的编号，初始为0表示不在枚举类中
        int judgeNum=0;
        //字符串长度
        int strLen=0;
        for (int i=head;i<chars.length;i++){
            if (Character.isLowerCase(chars[i])||Character.isUpperCase(chars[i])){
                rear=i+1;
            }else if (Character.isDigit(chars[i])){
                rear=i+1;
            }else{
                break;
            }
        }
        strLen=rear-head;
        judgeStr=judgeStr.copyValueOf(chars,head,strLen);
        //根据匹配结果判断是标识符还是关键字，相应的存储进token和symble表
        try {
            judgeNum= InernalCode.getNum(judgeStr);
        } catch (Exception e) {
            //是标识符
            //symble
            Symble symble=new Symble();
            symble.setNumber(symbleNum);
            symble.setName(judgeStr);
            //18为标识符
            symble.setType(18);
            list.symbles.add(symble);
            //token
            Token token=new Token();
            token.setLabel(tokenNum);
            tokenNum++;
            token.setName(judgeStr);
            token.setCode(18);
            token.setAddress(symbleNum);
            symbleNum++;
            list.tokens.add(token);
        }
        if (judgeNum!=0){
            //是关键字
            Token token=new Token();
            token.setLabel(tokenNum);
            tokenNum++;
            token.setName(judgeStr);
            token.setCode(judgeNum);
            token.setAddress(-1);
            list.tokens.add(token);
        }
        return rear-1;
    }

    /**
     * 数字分析
     * @param chars
     * @param circle
     * @param line
     * @param list 表实例
     * @return
     */
    public static int DigitFunction(char[] chars,int circle,int line,List list){
        //num指的是符合数字规范的最后一位
        int num = 0;
        //整个包含数字的字符串的最后一位
        int end;
        boolean flagPoint = false;
        boolean flagLetter = false;
        boolean flagWrong = false;
        //最后符合标准的数字字符串
        String string = "1";
        //for (int i=circle;i<chars.length;i++){
        int i = circle;
        while (i < chars.length) {
            if (Character.isDigit(chars[i])) {
                //当前字符是数字就继续向下走
                i++;
                continue;
            } else if (Character.isLetter(chars[i])) {
                //如果当前字符是字母，那么截去后面的
                if (!flagLetter) {
                    flagLetter = true;
                    flagWrong = true;
                    num = i;
                    if (!flagPoint) {
                        WrongList wrong = new WrongList(line, "数字开头的数字、字母串");
                        wrongLists.add(wrong);
                    } else {
                        WrongList wrong = new WrongList(line, "实数的小数部分出现字母");
                        wrongLists.add(wrong);
                    }
                }
                //已经不是第一个字母了，就不必输出了
                else {
                    i++;
                    continue;
                }
            } else if (chars[i] == '.') {
                //碰到第一个小数点的情况
                if (!flagPoint) {
                    flagPoint = true;
                    i++;
                    continue;
                }
                //碰到第二个小数点需要截去
                else {
                    flagWrong = true;
                    num = i-1;
                    WrongList wrong = new WrongList(line, "实数中出现两个小数点");
                    wrongLists.add(wrong);
                }
            }
            //不是字母也不是小数点，即代表着该标识符代表的数字已经全部录完
            else {
                if (flagWrong == true) {
                    string = String.copyValueOf(chars, circle, num - circle);
                    break;
                }
                else {
                    num = i;
                    string = String.copyValueOf(chars,circle,num - circle);
                    break;
                }
            }
        }
        //Symble
        Symble symble = new Symble();
        symble.setNumber(symbleNum);
        //Token
        Token token = new Token();
        token.setLabel(tokenNum);
        token.setAddress(symbleNum);
        if (flagPoint == false){
            //判断该数为整数
            int number = Integer.parseInt(string);
            string = String.copyValueOf(chars, circle, num - circle);
            symble.setName(string);
            symble.setType(19);
            token.setName(string);
            token.setCode(19);
        }
        else {
            //判断该数为小数
            double number = Double.parseDouble(string);
            string = String.copyValueOf(chars, circle, num - circle);
            symble.setName(string);
            symble.setType(20);
            token.setName(string);
            token.setCode(20);
        }
        symbleNum++;
        tokenNum++;
        list.symbles.add(symble);
        list.tokens.add(token);
        return i-1;
    }

    /**
     *其他符号判断
     * @param chars
     * @param head
     * @param list
     */
    public static int OtherLetterFunction(char[] chars,int line,int head,List list){
        //对应机内码
        int judgeNum=0;
        //出错标识符
        boolean err=false;
        //符号结束下标
        int rear=head;
        String judgeStr=chars[head]+"";
        try {
            judgeNum = InernalCode.getNum(judgeStr);
        } catch (Exception e) {
            //出错情况(符号不在给出的单词表中)，请孙天宇同学添加非法字符错误
            WrongList wrong = new WrongList(line,"出现非法字符,非法字符为"+judgeStr);
            wrongLists.add(wrong);
            err=true;
        }
        //没出错
        if (!err){
            //判断是否是两个符号
            //>=
            if (judgeStr.equals(">")){
                if (chars[rear+1]=='='){
                    rear++;
                    judgeStr+=chars[rear]+"";
                }
            }
            //<=或<>
            if (judgeStr.equals("<")){
                if (chars[rear+1]=='='||chars[rear+1]=='>'){
                    rear++;
                    judgeStr+=chars[rear]+"";
                }
            }
            //:=
            if (judgeStr.equals(":")){
                if (chars[rear+1]=='='){
                    rear++;
                    judgeStr+=chars[rear]+"";
                }
            }
            judgeNum=InernalCode.getNum(judgeStr);
            //添加到token表中
            Token token=new Token();
            token.setLabel(tokenNum);
            tokenNum++;
            token.setName(judgeStr);
            token.setCode(judgeNum);
            token.setAddress(-1);
            list.tokens.add(token);
        }
        return rear;
    }

    /**
     * 错误列表打印
     */
    public static void ErrorMessage(){
        int count = wrongLists.size();
        if (count == 0){
            System.out.println("小伙子挺厉害的啊，再接再厉！");
        }
        else {
            System.out.println("总共出现" + count + "个错误");
            for (int i = 0; i < count; i++) {
                WrongList wrong = new WrongList();
                wrong = wrongLists.get(i);
                System.out.println("    出现的错误所在行为:"+wrong.getLine()+",错误编号是"+(i+1)+",错误说明为:"+wrong.getDescription()+",问题已改正");
            }
        }
    }
    
}
