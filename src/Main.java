import Lexical_analyzer.AnalysisFunction;
import Lexical_analyzer.List;
import Lexical_analyzer.Symble;
import Lexical_analyzer.Token;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {
    ArrayList<Token> tList = new ArrayList<Token>();
    ArrayList<Symble> sList = new ArrayList<Symble>();
    public static void main(String[] args) throws IOException {
	// write your code here
        //nice try
        File file = new File("L语言程序.txt");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String str;
        Integer circle =1;
        Map<Integer, String> readMap = new HashMap();
        while ((str = bufferedReader.readLine()) != null) {
            readMap.put(circle,str);
            circle++;
        }
        AnalysisFunction analysisFunction=new AnalysisFunction();
        List list = analysisFunction.judge(readMap);
        for (Token token:list.tokens){
            System.out.println(token.toString());
        }
        System.out.println("--------------------------------");
        for (Symble symble:list.symbles){
            System.out.println(symble.toString());
        }


    }
}