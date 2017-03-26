package com.aysegulpekel.core;


import java.util.ArrayList;
import java.util.List;

/**
 * Cell is the unit object on the board
 */
public class Cell {
    // Location on board as x, y coordinates
    private int x;
    private int y;

    // The cost of going from the cell's location to goal
    private int gScore = Integer.MAX_VALUE;

    // The cost is calculated by the algorithm's heuristic
    // basically consists of gScore + calculated heuristic cost (see logic/AStar.java)
    private int fScore = Integer.MAX_VALUE;

    // Boolean status for if the cell is obstacle or not
    private boolean obstacle = false;

    List<Cell> neighbors = new ArrayList<Cell>();

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getgScore() {
        return gScore;
    }

    public void setgScore(int gScore) {
        this.gScore = gScore;
    }

    public int getfScore() {
        return fScore;
    }

    public void setfScore(int fScore) {
        this.fScore = fScore;
    }

    public List<Cell> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(List<Cell> neighbors) {
        this.neighbors = neighbors;
    }

    public boolean isObstacle() {
        return obstacle;
    }

    public void setObstacle(boolean obstacle) {
        this.obstacle = obstacle;
    }
}
