package com.aysegulpekel.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Grid object is a Cell matrix, also holds
 * the helper methods to initialize the grid and
 * trace the neighbours of each cell in the grid
 */
public class Grid {

    private int width;
    private int height;
    private Cell[][] grid;

    /**
     * Grid is created with its dimensions
     * @param width horizontal dimension
     * @param height vertical dimension
     */
    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = initialize();
    }

    public Cell[][] getGrid() {
        return grid;
    }

    public void setGrid(Cell[][] grid) {
        this.grid = grid;
    }

    /**
     * Creates the grid with its dimensions and cell objects.
     * @return initialized grid with its cells
     */
    private Cell[][] initialize() {

        Cell[][] grid = new Cell[width][height];

        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                grid[x][y] = new Cell();

                grid[x][y].setX(x);
                grid[x][y].setY(y);

                grid[x][y].setgScore(0);
                grid[x][y].setfScore(0);
            }
        }
        return grid;
    }

    // Traces the grid and assigns each cell's neighbours list to themselves
    public void findNeighbours() {

        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                int border = grid.length - 1;

                List<Cell> neighbors = new ArrayList<Cell>();

                // Controls to watch out each border and obstacles around
                if (x > 0 && y > 0 && !grid[x - 1][y - 1].isObstacle()) {
                    neighbors.add(grid[x - 1][y - 1]);
                }

                if (x > 0 && !grid[x - 1][y].isObstacle()) {
                    neighbors.add(grid[x - 1][y]);
                }

                if (x > 0 && y < border && !grid[x - 1][y + 1].isObstacle()) {
                    neighbors.add(grid[x - 1][y + 1]);
                }

                if (y > 0 && !grid[x][y - 1].isObstacle()) {
                    neighbors.add(grid[x][y - 1]);
                }

                if (y < border && !grid[x][y + 1].isObstacle()) {
                    neighbors.add(grid[x][y + 1]);
                }

                if (x < border && y > 0 && !grid[x + 1][y - 1].isObstacle()) {
                    neighbors.add(grid[x + 1][y - 1]);
                }

                if (x < border && !grid[x + 1][y].isObstacle()) {
                    neighbors.add(grid[x + 1][y]);
                }

                if (x < border && y < border && !grid[x + 1][y + 1].isObstacle()) {
                    neighbors.add(grid[x + 1][y + 1]);
                }

                if (x > 0 && y > 0 && neighbors.contains(grid[x - 1][y - 1]) &&
                        grid[x - 1][y].isObstacle() && grid[x][y - 1].isObstacle()) {
                    neighbors.remove(grid[x - 1][y - 1]);
                }

                if (x > 0 && y < border && neighbors.contains(grid[x - 1][y + 1]) &&
                        grid[x - 1][y].isObstacle() && grid[x][y + 1].isObstacle()) {
                    neighbors.remove(grid[x - 1][y + 1]);
                }

                if (x < border && y > 0 && neighbors.contains(grid[x + 1][y - 1]) &&
                        grid[x][y - 1].isObstacle() && grid[x + 1][y].isObstacle()) {
                    neighbors.remove(grid[x + 1][y - 1]);
                }

                if (x < border && y < border && neighbors.contains(grid[x + 1][y + 1]) &&
                        grid[x + 1][y].isObstacle() && grid[x][y + 1].isObstacle()) {
                    neighbors.remove(grid[x + 1][y + 1]);
                }

                grid[x][y].setNeighbors(neighbors);
            }
        }
    }
}