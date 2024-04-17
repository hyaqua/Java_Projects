package com.example.skaiciuotuvas;

import data.Tuple;

import java.util.ArrayList;
import java.util.Collections;
public class Paskola {
    double kaina;
    int laikas;
    double palukanos;
    boolean tipas;
    Paskola(double k, int l, double p, boolean t){
        kaina = k;
        laikas = l;
        palukanos = p;
        tipas = t;
    }

    public ArrayList<Tuple> apskaiciuok(){
        if(tipas){
            return this.apskAnuitini();
        }
        return this.apskLinijini();
    }
    
    protected ArrayList<Tuple> apskAnuitini(){
        Tuple ret = new Tuple();
         // ret 0 kiek mokesi kas menesi
        ret.set(0,((kaina * (palukanos / 12)) / (1 - Math.pow((1 + (palukanos / 12)), -1*laikas))));
        ret.set(1,(kaina * (palukanos / 12.0))); // ret 1 palukanos
        ret.set(2,(ret.get(0) - ret.get(1))); // ret 2 kiek i paskola
        ret.set(3, (kaina - ret.get(2)));
        return new ArrayList<>(Collections.singletonList(ret));
    }
    protected ArrayList<Tuple> apskLinijini(){
        Tuple ret = new Tuple();
        ret.set(2,kaina / (laikas));
        ret.set(1,(kaina * (palukanos/12))); // ret 1 palukanos
        ret.set(0,(ret.get(1) + ret.get(2)));
        ret.set(3, (kaina - ret.get(2)));
        return new ArrayList<>(Collections.singletonList(ret));
    }
    
}
