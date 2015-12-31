package com.agshin.brightpolicelights;

import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class Pattern {
    private ArrayList<PatternElement> elements;
    private String name;
    private int sirenId;

    public Pattern(ArrayList<PatternElement> elements, String name) {
        this.elements = elements;
        this.name = name;
    }

    public void setSirenId(int sirenId) {
        this.sirenId = sirenId;
    }

    public int getSirenId() {
        return sirenId;
    }

    public Pattern(String pattern, String separator1, String separator2, String elementSeparator){
        ArrayList<PatternElement> elements = new ArrayList<PatternElement>();
        String[] list = pattern.split(separator1);
//        if(list.length == 0)
        Log.i("",">>>>>> "+pattern);
        String[] elementList = list[1].split(elementSeparator);

        for (int i = 0; i < elementList.length; i++){
            elements.add(new PatternElement(elementList[i], separator2));
        }
        this.name = list[0];
        if(list.length > 2){
            this.setSirenId(Integer.valueOf(list[2]));
        } else {
            this.setSirenId(4);
        }
        Log.i("TAGI","Patterns siren id = " + sirenId);
//        Toast.makeText(, patt)

        this.elements = elements;
    }

    public ArrayList<PatternElement> getElements() {
        return elements;
    }

    public String getName() {
        return name;
    }

    public void setElements(ArrayList<PatternElement> elements) {
        this.elements = elements;
    }

    public void setName(String name) {
        this.name = name;
    }
     
    public String toString(String separator1, String separator2, String elementSeparator){
        String ret = name+separator1;
        for (int i = 0; i < elements.size(); i++){
            ret += elements.get(i).toString(separator2);
            if(i < elements.size() - 1){
                ret += elementSeparator;
            }
        }
        ret += separator1 + sirenId;

        return ret;
    }


}
