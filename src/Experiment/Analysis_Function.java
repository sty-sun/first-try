package Experiment;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.ConsoleHandler;

public class Analysis_Function {
    /**
     * 错误列表变量
     */
    public static ArrayList<WrongList> list = new ArrayList<WrongList>();

    public static void preProcess(String string) {
        //可以替换大部分空白字符， 不限于空格 ；
        string = string.replaceAll("\\s*", "");
    }

    /**
     * 字符判断程序
     * @param readMap
     */
    public static void judge(Map<Integer, String> readMap){
        //line里面存储的是读到的第几行，line从1开始的；
        for (int line=1;line<=readMap.size();line++) {
            String string = readMap.get(line);
            //将这一行当中所有的字符全部存储在一个字符数组中；
            char[] chars = new char[string.length()];
            string.getChars(0, string.length(), chars, 0);
            for (int i = 0; i < chars.length; i++) {
                char judgeChar = chars[i];
                //关键字和标识符分析程序
                if (Character.isLowerCase(judgeChar) || Character.isUpperCase(judgeChar)) {
                    i=LetterFunction(i,chars);
                } else if (Character.isDigit(judgeChar)) {
                    i = DigitFunction(chars,i,line);
                    ErrorMessage();
                } else {
                    OtherLetterFunction(chars,i,line);
                }
            }
        }
    }

    /**
     *
     * @param head
     * @param chars
     * @return
     */
    public static int LetterFunction(int head,char[] chars){
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
            judgeNum=Inernal_Code.getNum(judgeStr);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if (judgeNum!=0){
            System.out.println(Inernal_Code.getWord(judgeNum));
        }
        return rear-1;
    }
    /**
     *数字分析程序
     */
    public static int DigitFunction(char[] chars,int circle,int line){
        //num指的是符合数字规范的最后一位
        int num = 0;
        //整个包含数字的字符串的最后一位
        int end;
        boolean flagPoint = false;
        boolean flagLetter = false;
        String string;
        //for (int i=circle;i<chars.length;i++){
        int i = circle;
        while (i < chars.length){
            if (Character.isDigit(chars[i])){
                //当前字符是数字就继续向下走
                i++;
                continue;
            }
            else if (Character.isLetter(chars[i])){
                //如果当前字符是字母，那么截去后面的
                if (!flagLetter) {
                    flagLetter = true;
                    num = i;
                    if (!flagPoint) {
                        WrongList wrong = new WrongList(line, "数字开头的数字、字母串");
                        list.add(wrong);
                    }
                    else {
                        WrongList wrong = new WrongList(line,"实数的小数部分出现字母");
                        list.add(wrong);
                    }
                    string = String.copyValueOf(chars, circle, num);
                    System.out.println(string);
                }
                //已经不是第一个字母了，就不必输出了
                else{
                    i++;
                    continue;
                }
            }
            else if (chars[i]=='.'){
                //碰到第一个小数点的情况
                if (!flagPoint){
                    flagPoint = true;
                    i++;
                    continue;
                }
                //碰到第二个小数点需要截去
                else{
                    num = i;
                    WrongList wrong = new WrongList(line,"实数中出现两个小数点");
                    list.add(wrong);
                }
                string = String.copyValueOf(chars,circle,num);
                System.out.println(string);
            }
            //不是字母也不是小数点，即代表着该标识符代表的数字已经全部录完
            else{

                break;
            }
            i++;
        }
        return i;
    }

    /**
     *
     * @param chars
     * @param head
     * @param line
     */
    public static void OtherLetterFunction(char[] chars,int head,int line){
        System.out.println(chars[head]);
    }

    /**
     * 错误列表打印
     */
    public static void ErrorMessage(){
        int count = list.size();
        if (count == 0){
            System.out.println("小伙子挺厉害的啊，再接再厉！");
        }
        else {
            System.out.println("总共出现" + count + "个错误");
            for (int i = 0; i < count; i++) {
                WrongList wrong = new WrongList();
                wrong = list.get(i);
                System.out.println("    出现的错误所在行为:"+wrong.getLine()+",错误编号是"+(i+1)+",错误说明为:"+wrong.getDescription()+",问题已改正");
            }
        }
    }
}
