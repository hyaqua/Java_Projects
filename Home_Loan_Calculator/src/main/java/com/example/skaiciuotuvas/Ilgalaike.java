package com.example.skaiciuotuvas;

import data.Table;
import data.Tuple;

import java.util.ArrayList;
import java.util.List;

public class Ilgalaike extends Paskola{
    public Ilgalaike(double kaina, int laikas, double palukanos, boolean tipas) {
        super(kaina, laikas, palukanos, tipas);
    }
    @Override
    public ArrayList<Tuple> apskaiciuok(){
        if(tipas){
            return this.apskAnuitini();
        }
        return this.apskLinijini();
    }
    @Override
    protected ArrayList<Tuple> apskLinijini(){
        ArrayList<Tuple> timeline = new ArrayList<>();
        Tuple ret = super.apskLinijini().get(0);
        timeline.add(ret);
        for(int i = 1; i < laikas; i++){
            Tuple r = new Tuple();
            r.set(2, ret.get(2));
            r.set(1, (timeline.get(timeline.size()-1).get(3) * (palukanos/12)));
            r.set(0, r.get(1) + r.get(2));
            r.set(3, (timeline.get(timeline.size()-1).get(3) - r.get(2)));
            timeline.add(r);
        }
        if(timeline.get(timeline.size()-1).get(3) != 0){
            double add = timeline.get(timeline.size()-1).get(3);
            timeline.get(timeline.size()-1).set(2, timeline.get(timeline.size()-1).get(2) + add);
            timeline.get(timeline.size()-1).set(0, timeline.get(timeline.size()-1).get(0) + add);
            timeline.get(timeline.size()-1).set(3, 0.0);
        }
        return timeline;
    }
    @Override
    protected ArrayList<Tuple> apskAnuitini(){
        ArrayList<Tuple> timeline = new ArrayList<>();
        Tuple ret = super.apskAnuitini().get(0);
        timeline.add(ret);
        // 0 - Bendras mokestis, 1 - palukanu mokejimas 2 - paskolos atmokejimas 3 - paskolos likutis

        for(int i = 1; i < laikas; i++){
            Tuple r = new Tuple();
            r.set(0, ret.get(0));
            r.set(1, timeline.get(timeline.size()-1).get(3) * (palukanos/12.0));
            r.set(2, (r.get(0) - r.get(1)));
            r.set(3, (timeline.get(timeline.size()-1).get(3) - r.get(2)));
            timeline.add(r);
        }
        if(timeline.get(timeline.size()-1).get(3) != 0){
            double add = timeline.get(timeline.size()-1).get(3);
            timeline.get(timeline.size()-1).set(2, timeline.get(timeline.size()-1).get(2) + add);
            timeline.get(timeline.size()-1).set(0, timeline.get(timeline.size()-1).get(0) + add);
            timeline.get(timeline.size()-1).set(3, 0.0);
        }
        return timeline;
    }
    public Table getTable(){
        if(tipas){
            return new Table(this.apskAnuitini());
        }
        return new Table(this.apskLinijini());
    }
}
