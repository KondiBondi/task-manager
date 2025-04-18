package com.konrad.task_manager.zadanie1;

public class ConiferousTree extends Tree {
    private int needles;
    private int cones;

    public ConiferousTree(String trunk, int branches, int height, int needles) {
        super(trunk, branches, height);
        this.needles = needles;
        this.cones = 0;
    }

    @Override
    public void grow() {
        height += 2;
        branches += 1;
        needles += 200;
        System.out.println("Drzewo iglaste rośnie! Nowa wysokość: " + height + "m, igieł: " + needles);
    }

    public void produceCones() {
        cones += 5;
        System.out.println("Drzewo iglaste wyprodukowało szyszki. Liczba szyszek: " + cones);
    }
}
