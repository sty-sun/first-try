package Experiment;

import java.util.ArrayList;
import java.util.Map;

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
        for (int line=1;line<=readMap.size();line++) {
            String string = readMap.get(line);
            char[] chars = new char[string.length()];
            string.getChars(0, string.length(), chars, 0);
            for (int i = 0; i < chars.length; i++) {
                char judgeChar = chars[i];
                //关键字和标识符分析程序
                if (Character.isLowerCase(judgeChar) || Character.isUpperCase(judgeChar)) {
                    LetterFunction();
                } else if (Character.isDigit(judgeChar)) {
                    i = DigitFunction(chars,i,line);
                    ErrorMessage();
                } else {
                    //OtherLetterFunction();
                }
            }
        }
    }
    public static void LetterFunction(){

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
    public static void OtherLetterFunction(char[] chars,int circle,int line){

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
