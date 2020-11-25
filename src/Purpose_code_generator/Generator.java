package Purpose_code_generator;

import Lexical_analyzer.InernalCode;
import Lexical_analyzer.WrongList;
import Syntax_and_Semantic_Analyzer.EQU;
import Lexical_analyzer.List;
import java.util.ArrayList;

public class Generator {
    public ArrayList<ObjectCode_Stack> objectCode_stacks = new ArrayList<ObjectCode_Stack>();

    /**
     * 扫描语法语义分析器生成的四元式，将其改造成目的代码生成器所需要的格式
     * @param equs
     * @param list
     */
    public void Scan(ArrayList<EQU> equs,List list){
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
            count++;
        }
        length = GenStack.size();
        count = 0;
        while (count<length){
            //代码序列的第一条语句是入口语句
            if (count == 0){
                GenStack.get(count).setOut_port(1);
            }
            else {
                //跳转指令跳转到的四元式是入口语句
                if (equs.get(count).getOp()<=58&&equs.get(count).getOp()>=52){
                    int result = equs.get(count).getResult();
                    GenStack.get(result).setOut_port(1);
                    //跳转指令的下一条语句也是入口语句
                    count++;
                    GenStack.get(count).setOut_port(1);
                }
            }
            count++;
        }
    }
    public Register bx = new Register(false,"bx");
    public Register dx = new Register(false,"dx");

    /**
     * 寄存器分配策略
     * @param Name
     */
    public String judgeRegisterName(String Name){
        if ( (!bx.isStatus()) || ((bx.isStatus())&&(bx.getName().equals(Name)))){
            return "bx";
        }
        else if ((!dx.isStatus())|| ((dx.isStatus())&&(dx.getName().equals(Name)))){
            return "dx";
        }
        else {

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
            //(op,left,right,object)
            //op
            String str = genStacks.get(count).getOp().toString();
            //left
            String leftName = list.symbles.get(genStacks.get(count).getAddr1()).getName();
            //right
            String rightName = list.symbles.get(genStacks.get(count).getAddr2()).getName();
            //object
            String objectName = list.symbles.get(genStacks.get(count).getResult()).getName();
            switch (str){
                //(:=,left,null,object)即
                case ":=":{
                    //(mov,r,left)
                    ObjectCode_Stack objectCode_stack1 = new ObjectCode_Stack();
                    objectCode_stack1.setOp("mov");
                    String Oop = judgeRegisterName(leftName);
                    objectCode_stack1.setOop(Oop);
                    objectCode_stack1.setSop(leftName);
                    objectCode_stacks.add(objectCode_stack1);
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
                    //(mov,r,left)
                    ObjectCode_Stack objectCode_stack1 = new ObjectCode_Stack();
                    objectCode_stack1.setOp("mov");
                    String Oop = judgeRegisterName(leftName);
                    objectCode_stack1.setOop(Oop);
                    objectCode_stack1.setSop(leftName);
                    objectCode_stacks.add(objectCode_stack1);
                    //(add,r,right)
                    ObjectCode_Stack objectCode_stack2 = new ObjectCode_Stack();
                    objectCode_stack2.setOp("ADD");
                    objectCode_stack2.setOop(Oop);
                    objectCode_stack2.setSop(rightName);
                    objectCode_stacks.add(objectCode_stack2);
                    //(mov,object,r)
                    ObjectCode_Stack objectCode_stack3 = new ObjectCode_Stack();
                    objectCode_stack3.setOp("Mov");
                    objectCode_stack3.setOop(objectName);
                    objectCode_stack3.setSop(Oop);
                    objectCode_stacks.add(objectCode_stack3);
                    break;
                }
                //(-,left,right,object)
                case "-":{
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
                    objectCode_stacks.add(objectCode_stack2);
                    break;
                }
                //(j<,left,right,object)
                case "j<":{
                    //(cmp,r1,r2)
                    ObjectCode_Stack objectCode_stack1 = new ObjectCode_Stack();
                    objectCode_stack1.setOp("cmp");
                    String Oop = judgeRegisterName(leftName);
                    objectCode_stack1.setOop(Oop);
                    String Sop = judgeRegisterName(rightName);
                    objectCode_stack1.setSop(Sop);
                    objectCode_stacks.add(objectCode_stack1);
                    //(jl,object)
                    ObjectCode_Stack objectCode_stack2 = new ObjectCode_Stack();
                    objectCode_stack2.setOp("jl");
                    objectCode_stack2.setOop(objectName);
                    objectCode_stack2.setSop("null");
                    objectCode_stacks.add(objectCode_stack2);
                    break;
                }
                case "j<=":{
                    //(cmp,r1,r2)
                    ObjectCode_Stack objectCode_stack1 = new ObjectCode_Stack();
                    objectCode_stack1.setOp("cmp");
                    String Oop = judgeRegisterName(leftName);
                    objectCode_stack1.setOop(Oop);
                    String Sop = judgeRegisterName(rightName);
                    objectCode_stack1.setSop(Sop);
                    objectCode_stacks.add(objectCode_stack1);
                    //(jle,object)
                    ObjectCode_Stack objectCode_stack2 = new ObjectCode_Stack();
                    objectCode_stack2.setOp("jle");
                    objectCode_stack2.setOop(objectName);
                    objectCode_stack2.setSop("null");
                    objectCode_stacks.add(objectCode_stack2);
                    break;
                }
                case "j>":{
                    //(cmp,r1,r2)
                    ObjectCode_Stack objectCode_stack1 = new ObjectCode_Stack();
                    objectCode_stack1.setOp("cmp");
                    String Oop = judgeRegisterName(leftName);
                    objectCode_stack1.setOop(Oop);
                    String Sop = judgeRegisterName(rightName);
                    objectCode_stack1.setSop(Sop);
                    objectCode_stacks.add(objectCode_stack1);
                    //(jg,object)
                    ObjectCode_Stack objectCode_stack2 = new ObjectCode_Stack();
                    objectCode_stack2.setOp("jg");
                    objectCode_stack2.setOop(objectName);
                    objectCode_stack2.setSop("null");
                    objectCode_stacks.add(objectCode_stack2);
                    break;
                }
                case "j>=":{
                    //(cmp,r1,r2)
                    ObjectCode_Stack objectCode_stack1 = new ObjectCode_Stack();
                    objectCode_stack1.setOp("cmp");
                    String Oop = judgeRegisterName(leftName);
                    objectCode_stack1.setOop(Oop);
                    String Sop = judgeRegisterName(rightName);
                    objectCode_stack1.setSop(Sop);
                    objectCode_stacks.add(objectCode_stack1);
                    //(jge,object)
                    ObjectCode_Stack objectCode_stack2 = new ObjectCode_Stack();
                    objectCode_stack2.setOp("jge");
                    objectCode_stack2.setOop(objectName);
                    objectCode_stack2.setSop("null");
                    objectCode_stacks.add(objectCode_stack2);
                    break;
                }
                case "j=":{
                    //(cmp,r1,r2)
                    ObjectCode_Stack objectCode_stack1 = new ObjectCode_Stack();
                    objectCode_stack1.setOp("cmp");
                    String Oop = judgeRegisterName(leftName);
                    objectCode_stack1.setOop(Oop);
                    String Sop = judgeRegisterName(rightName);
                    objectCode_stack1.setSop(Sop);
                    objectCode_stacks.add(objectCode_stack1);
                    //(je,object)
                    ObjectCode_Stack objectCode_stack2 = new ObjectCode_Stack();
                    objectCode_stack2.setOp("je");
                    objectCode_stack2.setOop(objectName);
                    objectCode_stack2.setSop("null");
                    objectCode_stacks.add(objectCode_stack2);
                    break;
                }
                case "j<>":{
                    //(cmp,r1,r2)
                    ObjectCode_Stack objectCode_stack1 = new ObjectCode_Stack();
                    objectCode_stack1.setOp("cmp");
                    String Oop = judgeRegisterName(leftName);
                    objectCode_stack1.setOop(Oop);
                    String Sop = judgeRegisterName(rightName);
                    objectCode_stack1.setSop(Sop);
                    objectCode_stacks.add(objectCode_stack1);
                    //(jne,object)
                    ObjectCode_Stack objectCode_stack2 = new ObjectCode_Stack();
                    objectCode_stack2.setOp("jne");
                    objectCode_stack2.setOop(objectName);
                    objectCode_stack2.setSop("null");
                    objectCode_stacks.add(objectCode_stack2);
                    break;
                }
                default:{
                    break;
                }
            }
            count++;
        }
    }
}
