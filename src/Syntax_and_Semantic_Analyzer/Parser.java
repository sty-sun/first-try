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

    public boolean IndexJudge(int tokenNum){
        if (tokenNum<size){
            return true;
        }
        else{
            System.out.println("程序已经运行到头了");
            return false;
        }
    }

    public void runParser(List list){
        size = list.tokens.size();
        Token token = new Token();
        token = list.tokens.get(tokenNum);
        System.out.println(token.toString());
        if (token.getName().equals("var")){
            tokenNum++;
            if (IndexJudge(tokenNum)) {
                Declear(list);
            }
        }
        if (!token.getName().equals("begin")){
            System.out.println("缺少begin");
        }
        else{

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
            if (token.getName().equals(";")){
                tokenNum++;
                if (IndexJudge(tokenNum)) {
                    token = list.tokens.get(tokenNum);
                }
            }
        }
    }

    /**
     * 变量声明语句分析
     * @param list
     */
    public void D_Declear(List list){
        //读入变量声明语句的第一个变量
        Token token = new Token();
        token = list.tokens.get(tokenNum);
        //判断第一个读入的token是否为标识符，进行匹配
        if (token.getCode() == 18){
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
            while(token.getName().equals(",")){
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
            if (token.getName().equals(":")){
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
    public void prinVList(){
        int length = vList.size();
        for (int i=0;i<length;i++){
            System.out.println(vList.get(i).toString());
        }
    }
}
