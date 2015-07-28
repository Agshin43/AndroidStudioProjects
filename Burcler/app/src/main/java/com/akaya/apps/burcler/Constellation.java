package com.akaya.apps.burcler;

import java.util.ArrayList;

/**
 * Created by agshin on 7/21/15.
 */
public class Constellation {
    public ConstellationType type;
    public String description;
    ArrayList<Accordance> accordanceArray;

    public Constellation(String data) {
//        this.description = description;
//        this.type = ConstellationType.valueOf(name);
    }

    public Constellation()
    {
        accordanceArray = new ArrayList<Accordance>();
    }
}
