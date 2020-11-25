package Syntax_and_Semantic_Analyzer;

import java.util.ArrayList;

public class TrueOrFalse {
    private ArrayList<Integer> trueEqu;
    private ArrayList<Integer> falseEqu;

    public TrueOrFalse(Integer trueEqu, Integer falseEqu) {
        this.trueEqu=new ArrayList<>();
        this.falseEqu=new ArrayList<>();
        this.trueEqu.add(trueEqu);
        this.falseEqu.add(falseEqu);
    }

    public ArrayList<Integer> getTrueEqu() {
        return trueEqu;
    }

    public void setTrueEqu(ArrayList<Integer> trueEqu) {
        for (Integer item:trueEqu){
            this.trueEqu.add(item);
        }
    }

    public void setTrueEqu(Integer trueEqu){
        this.trueEqu.add(trueEqu);
    }

    public ArrayList<Integer> getFalseEqu() {
        return falseEqu;
    }

    public void setFalseEqu(ArrayList<Integer> falseEqu) {
        for (Integer item:falseEqu){
            this.falseEqu.add(item);
        }
    }

    public void setFalseEqu(Integer falseEqu){
        this.falseEqu.add(falseEqu);
    }

    public TrueOrFalse() {
        this.trueEqu=new ArrayList<>();
        this.falseEqu=new ArrayList<>();

    }
}
