package com.konrad.task_manager.zadanie1;

public abstract class Tree {
    protected String trunk;
    protected int branches;
    protected int height;

    protected Tree(String trunk, int branches, int height) {
        this.trunk = trunk;
        this.branches = branches;
        this.height = height;
    }

    public abstract void grow();

    @Override
    public String toString() {
        return ("Pień: " + trunk + ", Gałęzie: " + branches + ", Wysokość: " + height + "m");
    }
}