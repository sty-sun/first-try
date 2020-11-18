package Syntax_and_Semantic_Analyzer;

import Lexical_analyzer.List;
import Lexical_analyzer.Token;

public class Parser {
    public static int tokenNum = 0;
    public void runParser(List list){
        Token token = new Token();
        token = list.tokens.get(tokenNum);
        System.out.println(token.toString());
        if (token.getName().equals("var")){
            tokenNum++;
            Declear(list);
        }
        if (!token.getName().equals("begin")){
            System.out.println("缺少begin");
        }
        else{

        }
    }
    public void Declear(List list){

    }

}
