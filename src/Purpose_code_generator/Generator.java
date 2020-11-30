package Purpose_code_generator;

import Lexical_analyzer.InernalCode;
import Lexical_analyzer.Symble;
import Lexical_analyzer.WrongList;
import Syntax_and_Semantic_Analyzer.EQU;
import Lexical_analyzer.List;
import java.util.ArrayList;

public class Generator {
    public ArrayList<ObjectCode_Stack> objectCode_stacks = new ArrayList<ObjectCode_Stack>();
    public Register bx = new Register(false,"bx");
    public Register dx = new Register(false,"dx");
    public static ArrayList<String> rValueBx = new ArrayList<String>();
    public static ArrayList<String> rValueDx = new ArrayList<String>();
    public static ArrayList[] aValue;
    public static int[] info;
    /**
     * 扫描语法语义分析器生成的四元式，将其改造成目的代码生成器所需要的格式
     * @param equs
     * @param list
     */
    public void Scan(ArrayList<EQU> equs, List list, ArrayList<Symble> symbles){
        ArrayList<Symble> symbles1 = new ArrayList<>();
        for (int i=0;i<symbles.size();i++){
            if (symbles.get(i).getType()==18){
                symbles1.add(symbles.get(i));
            }
        }
        //aValue初始化
        aValue = new ArrayList[symbles1.size()];
        for (int i=0;i<aValue.length;i++){
            aValue[i].add(symbles1.get(i).getName());
        }
        //info初始化
        info = new int[symbles1.size()];
        ArrayList<GenStruct> GenStack = new ArrayList<GenStruct>();
        int length = equs.size();
        int count = 0;
        //遍历一遍初始四元式并第一次初始化新四元式(初始化label，op1，op2，result)
        while (count < length){
            EQU equ = new EQU();
            equ = equs.get(count);
            GenStruct genStruct = new GenStruct(equ.getLabel(),equ.getOp1(), equ.getOp2(), equ.getResult());
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
            GenStack.add(genStruct);
            count++;
        }
        length = GenStack.size();
        count = 0;
        //入口置一
        while (count<length){
            //代码序列的第一条语句是入口语句
            if (count == 0){
                GenStack.get(count).setOut_port(1);
                count++;
                GenStack.get(count).setOut_port(1);
            }
            else {
                //跳转指令跳转到的四元式是入口语句
                if (equs.get(count).getOp()<=58&&equs.get(count).getOp()>=52){
                    int result = equs.get(count).getResult();
                    //最后的代码结尾是空的
                    if (result < length) {
                        GenStack.get(result).setOut_port(1);
                        //跳转指令的下一条语句也是入口语句
                    }
                    else {
                        count++;
                        continue;
                    }
                    count++;
                    if (count < length) {
                        GenStack.get(count).setOut_port(1);
                    }
                }
                else {
                    count++;
                }
            }
        }
        Generate(GenStack,list);
    }

    /**
     * 寄存器分配策略
     * @param Name
     */
    public String judgeRegisterName(String Name){
        if ( (!bx.isStatus()) || ((bx.isStatus())&&(bx.getName().equals(Name)))){
            bx.setStatus(true);
            return "bx";
        }
        else if ((!dx.isStatus())|| ((dx.isStatus())&&(dx.getName().equals(Name)))){
            dx.setStatus(true);
            return "dx";
        }
        else {
            bx.setName(Name);
            bx.setStatus(true);
            return "bx";
        }
    }

    /**
     * 目标代码生成策略
     * @param genStacks
     * @param list
     */
    public void Generate(ArrayList<GenStruct> genStacks,List list){
        int length = genStacks.size();
        int count = 0;
        while (count < length){
            //label (op,left,right,object)
            int label = genStacks.get(count).getLabel();
            //op
            String str = String.valueOf(genStacks.get(count).getOp());
            //left
            String leftName;
            int addr1 = genStacks.get(count).getAddr1();
            if (addr1!=-1) {
                leftName = list.symbles.get(addr1-1).getName();
            }
            else {
                leftName = "null";
            }
            //right
            String rightName;
            int addr2 = genStacks.get(count).getAddr2();
            if (addr2!=-1) {
                rightName = list.symbles.get(addr2-1).getName();
            }
            else {
                rightName = "null";
            }

            switch (str){
                //(:=,left,null,object)
                case ":=":{
                    //lebel
                    ObjectCode_Stack objectCode_stack = new ObjectCode_Stack();
                    objectCode_stack.setLabel(label);
                    objectCode_stack.setOp("null");
                    objectCode_stack.setSop("null");
                    objectCode_stack.setOop("null");
                    objectCode_stacks.add(objectCode_stack);
                    //(mov,r,left)
                    ObjectCode_Stack objectCode_stack1 = new ObjectCode_Stack();
                    objectCode_stack1.setOp("mov");
                    String Oop = judgeRegisterName(leftName);
                    objectCode_stack1.setOop(Oop);
                    objectCode_stack1.setSop(leftName);
                    objectCode_stacks.add(objectCode_stack1);
                    //非跳转的情况，object为syble表里面新加的数字
                    String objectName;
                    int result = genStacks.get(count).getResult();
                    objectName = list.symbles.get(result-1).getName();
                    //(mov,object,r)
                    ObjectCode_Stack objectCode_stack2 = new ObjectCode_Stack();
                    objectCode_stack2.setOp("mov");
                    objectCode_stack2.setOop(objectName);
                    String Sop = judgeRegisterName(objectName);
                    objectCode_stack2.setSop(Sop);
                    objectCode_stacks.add(objectCode_stack2);
                    break;
                }
                //(+,left,right,object)
                case "+":{
                    //lebel
                    ObjectCode_Stack objectCode_stack = new ObjectCode_Stack();
                    objectCode_stack.setLabel(label);
                    objectCode_stack.setOp("null");
                    objectCode_stack.setSop("null");
                    objectCode_stack.setOop("null");
                    objectCode_stacks.add(objectCode_stack);
                    //(mov,r,left)
                    ObjectCode_Stack objectCode_stack1 = new ObjectCode_Stack();
                    objectCode_stack1.setOp("mov");
                    objectCode_stack1.setOop("ax");
                    objectCode_stack1.setSop(leftName);
                    objectCode_stacks.add(objectCode_stack1);
                    //(add,r,right)
                    ObjectCode_Stack objectCode_stack2 = new ObjectCode_Stack();
                    objectCode_stack2.setOp("add");
                    objectCode_stack2.setOop("ax");
                    objectCode_stack2.setSop(rightName);
                    objectCode_stacks.add(objectCode_stack2);
                    //非跳转的情况，object为syble表里面新加的数字
                    String objectName;
                    int result = genStacks.get(count).getResult();
                    objectName = list.symbles.get(result-1).getName();
                    //(mov,object,r)
                    ObjectCode_Stack objectCode_stack3 = new ObjectCode_Stack();
                    objectCode_stack3.setOp("mov");
                    objectCode_stack3.setOop(objectName);
                    objectCode_stack3.setSop("ax");
                    objectCode_stacks.add(objectCode_stack3);
                    break;
                }
                //(-,left,right,object)
                case "-":{
                    //lebel
                    ObjectCode_Stack objectCode_stack = new ObjectCode_Stack();
                    objectCode_stack.setLabel(label);
                    objectCode_stack.setOp("null");
                    objectCode_stack.setSop("null");
                    objectCode_stack.setOop("null");
                    objectCode_stacks.add(objectCode_stack);
                    //(mov,r,left)
                    ObjectCode_Stack objectCode_stack1 = new ObjectCode_Stack();
                    objectCode_stack1.setOp("mov");
                    String Oop = judgeRegisterName(leftName);
                    objectCode_stack1.setOop(Oop);
                    objectCode_stack1.setSop(leftName);
                    objectCode_stacks.add(objectCode_stack1);
                    //(dec,r,right)
                    ObjectCode_Stack objectCode_stack2 = new ObjectCode_Stack();
                    objectCode_stack2.setOp("dec");
                    objectCode_stack2.setOop(Oop);
                    objectCode_stack2.setSop(rightName);
                    objectCode_stacks.add(objectCode_stack2);
                    //非跳转的情况，object为syble表里面新加的数字
                    String objectName;
                    int result = genStacks.get(count).getResult();
                    objectName = list.symbles.get(result-1).getName();
                    //(mov,object,r)
                    ObjectCode_Stack objectCode_stack3 = new ObjectCode_Stack();
                    objectCode_stack3.setOp("mov");
                    objectCode_stack3.setOop(objectName);
                    objectCode_stack3.setSop(Oop);
                    objectCode_stacks.add(objectCode_stack3);
                    break;
                }
                //(*,left,right,object)
                case "*":{
                    //lebel
                    ObjectCode_Stack objectCode_stack = new ObjectCode_Stack();
                    objectCode_stack.setLabel(label);
                    objectCode_stack.setOp("null");
                    objectCode_stack.setSop("null");
                    objectCode_stack.setOop("null");
                    objectCode_stacks.add(objectCode_stack);
                    //(mov,r,left)
                    ObjectCode_Stack objectCode_stack1 = new ObjectCode_Stack();
                    objectCode_stack1.setOp("mov");
                    String Oop = judgeRegisterName(leftName);
                    objectCode_stack1.setOop(Oop);
                    objectCode_stack1.setSop(leftName);
                    objectCode_stacks.add(objectCode_stack1);
                    //(mul,r,right)
                    ObjectCode_Stack objectCode_stack2 = new ObjectCode_Stack();
                    objectCode_stack2.setOp("mul");
                    objectCode_stack2.setOop(Oop);
                    objectCode_stack2.setSop(rightName);
                    objectCode_stacks.add(objectCode_stack2);
                    //非跳转的情况，object为syble表里面新加的数字
                    String objectName;
                    int result = genStacks.get(count).getResult();
                    objectName = list.symbles.get(result-1).getName();
                    //(mov,object,r)
                    ObjectCode_Stack objectCode_stack3 = new ObjectCode_Stack();
                    objectCode_stack3.setOp("mov");
                    objectCode_stack3.setOop(objectName);
                    objectCode_stack3.setSop(Oop);
                    objectCode_stacks.add(objectCode_stack3);
                    break;
                }
                //(/,left,right,object)
                case "/":{
                    //lebel
                    ObjectCode_Stack objectCode_stack = new ObjectCode_Stack();
                    objectCode_stack.setLabel(label);
                    objectCode_stack.setOp("null");
                    objectCode_stack.setSop("null");
                    objectCode_stack.setOop("null");
                    objectCode_stacks.add(objectCode_stack);
                    //(mov,r,left)
                    ObjectCode_Stack objectCode_stack1 = new ObjectCode_Stack();
                    objectCode_stack1.setOp("mov");
                    String Oop = judgeRegisterName(leftName);
                    objectCode_stack1.setOop(Oop);
                    objectCode_stack1.setSop(leftName);
                    objectCode_stacks.add(objectCode_stack1);
                    //(div,r,right)
                    ObjectCode_Stack objectCode_stack2 = new ObjectCode_Stack();
                    objectCode_stack2.setOp("div");
                    objectCode_stack2.setOop(Oop);
                    objectCode_stack2.setSop(rightName);
                    objectCode_stacks.add(objectCode_stack2);
                    //非跳转的情况，object为syble表里面新加的数字
                    String objectName;
                    int result = genStacks.get(count).getResult();
                    objectName = list.symbles.get(result-1).getName();
                    //(mov,object,r)
                    ObjectCode_Stack objectCode_stack3 = new ObjectCode_Stack();
                    objectCode_stack3.setOp("mov");
                    objectCode_stack3.setOop(objectName);
                    objectCode_stack3.setSop(Oop);
                    objectCode_stacks.add(objectCode_stack3);
                    break;
                }
                //(j,null,null,object)
                case "j":{
                    //lebel
                    ObjectCode_Stack objectCode_stack = new ObjectCode_Stack();
                    objectCode_stack.setLabel(label);
                    objectCode_stack.setOp("null");
                    objectCode_stack.setSop("null");
                    objectCode_stack.setOop("null");
                    objectCode_stacks.add(objectCode_stack);
                    //跳转的情况，object直接就是地址
                    String objectName = String.valueOf(genStacks.get(count).getResult());
                    //(mov,r,object)
                    ObjectCode_Stack objectCode_stack1 = new ObjectCode_Stack();
                    objectCode_stack1.setOp("mov");
                    String Oop = judgeRegisterName(leftName);
                    objectCode_stack1.setOop(Oop);
                    objectCode_stack1.setSop(objectName);
                    objectCode_stacks.add(objectCode_stack1);
                    //(jmp ax)
                    ObjectCode_Stack objectCode_stack2 = new ObjectCode_Stack();
                    objectCode_stack2.setOp("jmp");
                    objectCode_stack2.setOop(Oop);
                    objectCode_stack2.setSop("null");
                    objectCode_stacks.add(objectCode_stack2);
                    break;
                }
                //(j<,left,right,object)
                case "j<":{
                    //lebel
                    ObjectCode_Stack objectCode_stack = new ObjectCode_Stack();
                    objectCode_stack.setLabel(label);
                    objectCode_stack.setOp("null");
                    objectCode_stack.setSop("null");
                    objectCode_stack.setOop("null");
                    objectCode_stacks.add(objectCode_stack);
                    //(mov r1,left)
                    ObjectCode_Stack objectCode_stack1 = new ObjectCode_Stack();
                    objectCode_stack1.setOp("mov");
                    String r1 = judgeRegisterName(leftName);
                    objectCode_stack1.setOop(r1);
                    objectCode_stack1.setSop(leftName);
                    objectCode_stacks.add(objectCode_stack1);
                    //(mov r2,right)
                    ObjectCode_Stack objectCode_stack2 = new ObjectCode_Stack();
                    objectCode_stack2.setOp("mov");
                    String r2 = judgeRegisterName(rightName);
                    objectCode_stack2.setOop(r2);
                    objectCode_stack2.setSop(rightName);
                    objectCode_stacks.add(objectCode_stack2);
                    //(cmp,r1,r2)
                    ObjectCode_Stack objectCode_stack3 = new ObjectCode_Stack();
                    objectCode_stack3.setOp("cmp");
                    objectCode_stack3.setOop(r1);
                    objectCode_stack3.setSop(r2);
                    objectCode_stacks.add(objectCode_stack3);
                    //跳转的情况，object直接就是地址
                    String objectName = String.valueOf(genStacks.get(count).getResult());
                    //(jl,object)
                    ObjectCode_Stack objectCode_stack4 = new ObjectCode_Stack();
                    objectCode_stack4.setOp("jl");
                    objectCode_stack4.setOop(objectName);
                    objectCode_stack4.setSop("null");
                    objectCode_stacks.add(objectCode_stack4);
                    break;
                }
                //(j<=,left,right,object)
                case "j<=":{
                    //lebel
                    ObjectCode_Stack objectCode_stack = new ObjectCode_Stack();
                    objectCode_stack.setLabel(label);
                    objectCode_stack.setOp("null");
                    objectCode_stack.setSop("null");
                    objectCode_stack.setOop("null");
                    objectCode_stacks.add(objectCode_stack);
                    //(mov r1,left)
                    ObjectCode_Stack objectCode_stack1 = new ObjectCode_Stack();
                    objectCode_stack1.setOp("mov");
                    String r1 = judgeRegisterName(leftName);
                    objectCode_stack1.setOop(r1);
                    objectCode_stack1.setSop(leftName);
                    objectCode_stacks.add(objectCode_stack1);
                    //(mov r2,right)
                    ObjectCode_Stack objectCode_stack2 = new ObjectCode_Stack();
                    objectCode_stack2.setOp("mov");
                    String r2 = judgeRegisterName(rightName);
                    objectCode_stack2.setOop(r2);
                    objectCode_stack2.setSop(rightName);
                    objectCode_stacks.add(objectCode_stack2);
                    //(cmp,r1,r2)
                    ObjectCode_Stack objectCode_stack3 = new ObjectCode_Stack();
                    objectCode_stack3.setOp("cmp");
                    objectCode_stack3.setOop(r1);
                    objectCode_stack3.setSop(r2);
                    objectCode_stacks.add(objectCode_stack3);
                    //跳转的情况，object直接就是地址
                    String objectName = String.valueOf(genStacks.get(count).getResult());
                    //(jle,object)
                    ObjectCode_Stack objectCode_stack4 = new ObjectCode_Stack();
                    objectCode_stack4.setOp("jle");
                    objectCode_stack4.setOop(objectName);
                    objectCode_stack4.setSop("null");
                    objectCode_stacks.add(objectCode_stack4);
                    break;
                }
                //(j>,left,right,object)
                case "j>":{
                    //lebel
                    ObjectCode_Stack objectCode_stack = new ObjectCode_Stack();
                    objectCode_stack.setLabel(label);
                    objectCode_stack.setOp("null");
                    objectCode_stack.setSop("null");
                    objectCode_stack.setOop("null");
                    objectCode_stacks.add(objectCode_stack);
                    //(mov r1,left)
                    ObjectCode_Stack objectCode_stack1 = new ObjectCode_Stack();
                    objectCode_stack1.setOp("mov");
                    String r1 = judgeRegisterName(leftName);
                    objectCode_stack1.setOop(r1);
                    objectCode_stack1.setSop(leftName);
                    objectCode_stacks.add(objectCode_stack1);
                    //(mov r2,right)
                    ObjectCode_Stack objectCode_stack2 = new ObjectCode_Stack();
                    objectCode_stack2.setOp("mov");
                    String r2 = judgeRegisterName(rightName);
                    objectCode_stack2.setOop(r2);
                    objectCode_stack2.setSop(rightName);
                    objectCode_stacks.add(objectCode_stack2);
                    //(cmp,r1,r2)
                    ObjectCode_Stack objectCode_stack3 = new ObjectCode_Stack();
                    objectCode_stack3.setOp("cmp");
                    objectCode_stack3.setOop(r1);
                    objectCode_stack3.setSop(r2);
                    objectCode_stacks.add(objectCode_stack3);
                    //跳转的情况，object直接就是地址
                    String objectName = String.valueOf(genStacks.get(count).getResult());
                    //(jg,object)
                    ObjectCode_Stack objectCode_stack4 = new ObjectCode_Stack();
                    objectCode_stack4.setOp("jg");
                    objectCode_stack4.setOop(objectName);
                    objectCode_stack4.setSop("null");
                    objectCode_stacks.add(objectCode_stack4);
                    break;
                }
                //(j>=,left,right,object)
                case "j>=":{
                    //lebel
                    ObjectCode_Stack objectCode_stack = new ObjectCode_Stack();
                    objectCode_stack.setLabel(label);
                    objectCode_stack.setOp("null");
                    objectCode_stack.setSop("null");
                    objectCode_stack.setOop("null");
                    objectCode_stacks.add(objectCode_stack);
                    //(mov r1,left)
                    ObjectCode_Stack objectCode_stack1 = new ObjectCode_Stack();
                    objectCode_stack1.setOp("mov");
                    String r1 = judgeRegisterName(leftName);
                    objectCode_stack1.setOop(r1);
                    objectCode_stack1.setSop(leftName);
                    objectCode_stacks.add(objectCode_stack1);
                    //(mov r2,right)
                    ObjectCode_Stack objectCode_stack2 = new ObjectCode_Stack();
                    objectCode_stack2.setOp("mov");
                    String r2 = judgeRegisterName(rightName);
                    objectCode_stack2.setOop(r2);
                    objectCode_stack2.setSop(rightName);
                    objectCode_stacks.add(objectCode_stack2);
                    //(cmp,r1,r2)
                    ObjectCode_Stack objectCode_stack3 = new ObjectCode_Stack();
                    objectCode_stack3.setOp("cmp");
                    objectCode_stack3.setOop(r1);
                    objectCode_stack3.setSop(r2);
                    objectCode_stacks.add(objectCode_stack3);
                    //跳转的情况，object直接就是地址
                    String objectName = String.valueOf(genStacks.get(count).getResult());
                    //(jge,object)
                    ObjectCode_Stack objectCode_stack4 = new ObjectCode_Stack();
                    objectCode_stack4.setOp("jge");
                    objectCode_stack4.setOop(objectName);
                    objectCode_stack4.setSop("null");
                    objectCode_stacks.add(objectCode_stack4);
                    break;
                }
                //(j=,left,right,object)
                case "j=":{
                    //lebel
                    ObjectCode_Stack objectCode_stack = new ObjectCode_Stack();
                    objectCode_stack.setLabel(label);
                    objectCode_stack.setOp("null");
                    objectCode_stack.setSop("null");
                    objectCode_stack.setOop("null");
                    objectCode_stacks.add(objectCode_stack);
                    //(mov r1,left)
                    ObjectCode_Stack objectCode_stack1 = new ObjectCode_Stack();
                    objectCode_stack1.setOp("mov");
                    String r1 = judgeRegisterName(leftName);
                    objectCode_stack1.setOop(r1);
                    objectCode_stack1.setSop(leftName);
                    objectCode_stacks.add(objectCode_stack1);
                    //(mov r2,right)
                    ObjectCode_Stack objectCode_stack2 = new ObjectCode_Stack();
                    objectCode_stack2.setOp("mov");
                    String r2 = judgeRegisterName(rightName);
                    objectCode_stack2.setOop(r2);
                    objectCode_stack2.setSop(rightName);
                    objectCode_stacks.add(objectCode_stack2);
                    //(cmp,r1,r2)
                    ObjectCode_Stack objectCode_stack3 = new ObjectCode_Stack();
                    objectCode_stack3.setOp("cmp");
                    objectCode_stack3.setOop(r1);
                    objectCode_stack3.setSop(r2);
                    objectCode_stacks.add(objectCode_stack3);
                    //跳转的情况，object直接就是地址
                    String objectName = String.valueOf(genStacks.get(count).getResult());
                    //(je,r)
                    ObjectCode_Stack objectCode_stack4 = new ObjectCode_Stack();
                    objectCode_stack4.setOp("je");
                    objectCode_stack4.setOop(objectName);
                    objectCode_stack4.setSop("null");
                    objectCode_stacks.add(objectCode_stack4);
                    break;
                }
                //(j<>,left,right,object)
                case "j<>":{
                    //lebel
                    ObjectCode_Stack objectCode_stack = new ObjectCode_Stack();
                    objectCode_stack.setLabel(label);
                    objectCode_stack.setOp("null");
                    objectCode_stack.setSop("null");
                    objectCode_stack.setOop("null");
                    objectCode_stacks.add(objectCode_stack);
                    //(mov r1,left)
                    ObjectCode_Stack objectCode_stack1 = new ObjectCode_Stack();
                    objectCode_stack1.setOp("mov");
                    String r1 = judgeRegisterName(leftName);
                    objectCode_stack1.setOop(r1);
                    objectCode_stack1.setSop(leftName);
                    objectCode_stacks.add(objectCode_stack1);
                    //(mov r2,right)
                    ObjectCode_Stack objectCode_stack2 = new ObjectCode_Stack();
                    objectCode_stack2.setOp("mov");
                    String r2 = judgeRegisterName(rightName);
                    objectCode_stack2.setOop(r2);
                    objectCode_stack2.setSop(rightName);
                    objectCode_stacks.add(objectCode_stack2);
                    //(cmp,r1,r2)
                    ObjectCode_Stack objectCode_stack3 = new ObjectCode_Stack();
                    objectCode_stack3.setOp("cmp");
                    objectCode_stack3.setOop(r1);
                    objectCode_stack3.setSop(r2);
                    objectCode_stacks.add(objectCode_stack3);
                    //跳转的情况，object直接就是地址
                    String objectName = String.valueOf(genStacks.get(count).getResult());
                    //(jne,object)
                    ObjectCode_Stack objectCode_stack4 = new ObjectCode_Stack();
                    objectCode_stack4.setOp("jne");
                    objectCode_stack4.setOop(objectName);
                    objectCode_stack4.setSop("null");
                    objectCode_stacks.add(objectCode_stack4);
                    break;
                }
                default:{
                    break;
                }
            }
            count++;
        }
    }
    public void codePrint(){
        System.out.println("目标代码生成：");
        int length = objectCode_stacks.size();
        for (int i=0;i<length;i++){
            System.out.println(objectCode_stacks.get(i).toString());
        }
    }
}
