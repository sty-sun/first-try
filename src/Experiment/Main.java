package Experiment;

import java.awt.image.BufferedImageFilter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.annotation.Annotation;
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
        Analysis_Function.judge(readMap);
        //System.out.println(readMap.get(3));
    }
}
