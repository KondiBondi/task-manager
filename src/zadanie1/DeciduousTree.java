package com.konrad.task_manager.zadanie1;

public class DeciduousTree extends Tree {
    private int leaves;

    public DeciduousTree(String trunk, int branches, int height, int leaves) {
        super(trunk, branches, height);
        this.leaves = leaves;
    }

    @Override
    public void grow() {
        height += 1;
        branches += 2;
        leaves += 100;
        System.out.println("Drzewo liściaste rośnie! Nowa wysokość: " + height + "m, liści: " + leaves);
    }

    public void shedLeaves() {
        leaves = 0;
        System.out.println("Drzewo liściaste zrzuciło liście.");
    }
}