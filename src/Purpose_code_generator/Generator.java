package Purpose_code_generator;

import Lexical_analyzer.InernalCode;
import Lexical_analyzer.WrongList;
import Syntax_and_Semantic_Analyzer.EQU;
import Lexical_analyzer.List;
import java.util.ArrayList;

public class Generator {
    public void Scan(ArrayList<EQU> equs,List list){
        ArrayList<GenStruct> GenS = new ArrayList<GenStruct>();
        int length = equs.size();
        int count = 0;
        //遍历一遍初始四元式并第一次初始化新四元式
        while (count < length){
            EQU equ = new EQU();
            equ = equs.get(count);
            GenStruct genStruct = new GenStruct(equ.getOp1(), equ.getOp2(), equ.getResult());
            int op = equ.getOp();
            char[] chars = new char[4];
            String str = null;
            switch (op){
                case 51:{
                    str = ":=";
                    chars = str.toCharArray();
                    genStruct.setOp(chars);
                    break;
                }
                case 43:{
                    str = "+";
                    chars = str.toCharArray();
                    genStruct.setOp(chars);
                    break;
                }
                case 45:{
                    str = "-";
                    chars = str.toCharArray();
                    genStruct.setOp(chars);
                    break;
                }
                case 41:{
                    str = "*";
                    chars = str.toCharArray();
                    genStruct.setOp(chars);
                    break;
                }
                case 48:{
                    str = "/";
                    chars = str.toCharArray();
                    genStruct.setOp(chars);
                    break;
                }
                case 53:{
                    str = "j<";
                    chars = str.toCharArray();
                    genStruct.setOp(chars);
                    break;
                }
                case 54:{
                    str = "j<=";
                    chars = str.toCharArray();
                    genStruct.setOp(chars);
                    break;
                }
                case 57:{
                    str = "j>";
                    chars = str.toCharArray();
                    genStruct.setOp(chars);
                    break;
                }
                case 58:{
                    str = "j>=";
                    chars = str.toCharArray();
                    genStruct.setOp(chars);
                    break;
                }
                case 56:{
                    str = "j=";
                    chars = str.toCharArray();
                    genStruct.setOp(chars);
                    break;
                }
                case 52:{
                    str = "j";
                    chars = str.toCharArray();
                    genStruct.setOp(chars);
                    break;
                }
                case 55:{
                    str = "j<>";
                    chars = str.toCharArray();
                    genStruct.setOp(chars);
                    break;
                }
                default:{
                    break;
                }
            }
            int judgeNum = 0;

        }
    }
}
