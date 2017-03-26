package com.aysegulpekel.logic;

import com.aysegulpekel.core.Cell;

import java.util.*;

import static java.lang.Math.abs;

/**
 * A star holds the algorithm basics.
 * Runs the algorithm with the help of
 * the heuristic distance calculation,
 * distance between cells and their neighbours
 * and the lowest f scores examination.
 */
public class AStar {

    // Vertical or horizontal directions cost 10 units
    private static final int STRAIGHT_COST = 10;

    // Diagonal directions cost 14 units,
    // which applies the Pythagorean theorem (10^2 + 10^2 ~ 14^2)
    private static final int DIAGONAL_COST = 14;

    /**
     * Runs the main logic
     *
     * @param start the starting cell
     * @param goal  target cell to arrive
     * @return List of cells which indicate the shortest path from start to goal
     */
    public List<Cell> runAStar(Cell start, Cell goal) {
        // Open set keeps the unvisited cells
        List<Cell> openSet = new ArrayList<Cell>();

        // Closed set keeps the visited cells
        List<Cell> closedSet = new ArrayList<Cell>();

        // Keeps track of visited cells with parent cells
        Map<Cell, Cell> cameFrom = new HashMap<Cell, Cell>();

        openSet.add(start);

        // Calculate starting cell's f score
        start.setfScore(heuristicCostEstimate(start, goal));

        // Loop through the open set traverse the grid
        while (!openSet.isEmpty()) {
            // Pick the lowest f scored
            Cell current = getLowestFScored(start, openSet);
            // Check if it's the goal and return the path if so
            if (current.getX() == goal.getX() && current.getY() == goal.getY()) {
                return reconstructPath(cameFrom, current);
            }

            // Keep track of processed and unprocessed cells
            openSet.remove(current);
            closedSet.add(current);

            // For each neighbour of the current cell, compute the distances
            for (int i = 0; i < current.getNeighbors().size(); i++) {
                Cell neighbor = current.getNeighbors().get(i);

                if (closedSet.contains(neighbor)) {
                    continue;
                }

                // Tentative gScore is the metric to decide if going from current cell costs less or not
                int tentativeGScore = current.getgScore() + distanceBetween(current, neighbor);
                if (!openSet.contains(neighbor)) {
                    openSet.add(neighbor);
                }
                // If any neighbour of the current costs less than current cell's tentative score,
                // then it's not better to pass by to that current cell, so skip and look for other paths.
                else if (tentativeGScore >= neighbor.getgScore()) {
                    continue;
                }

                // Save the previous cell for each passed cell
                cameFrom.put(neighbor, current);

                // Set the corresponding scores
                neighbor.setgScore(tentativeGScore);
                neighbor.setfScore(neighbor.getgScore() + heuristicCostEstimate(neighbor, goal));
            }
        }
        // If there is no path to goal, return an empty path
        return Collections.emptyList();
    }


    /**
     * Arranges the passed cells from starting to goal
     *
     * @param cameFrom Map for passed cells and their previous cells
     * @param current  Arriving cell (equals to the goal cell)
     * @return List of shortest path's cells
     */
    private List<Cell> reconstructPath(Map<Cell, Cell> cameFrom, Cell current) {
        List<Cell> totalPath = new ArrayList<Cell>();
        totalPath.add(current);
        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            totalPath.add(current);
        }

        return totalPath;
    }

    /**
     * Givest the distance between the neighbor and the current,
     * straight cost and diagonal costs respectively
     *
     * @param current  Current cell
     * @param neighbor Any neighbour of the current cell
     * @return distance from current to neighbour
     */
    private int distanceBetween(Cell current, Cell neighbor) {
        if (current.getX() == neighbor.getX() || current.getY() == neighbor.getY()) {
            return STRAIGHT_COST;
        } else {
            return DIAGONAL_COST;
        }
    }

    /**
     * Gets the lowest fScored cell from unprocessed cell list
     *
     * @param current Current cell
     * @param openSet Unprocessed cells list
     * @return Lowest fScored cell
     */
    private Cell getLowestFScored(Cell current, List<Cell> openSet) {

        Cell minCell = current;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < openSet.size(); i++) {
            if (openSet.get(i).getfScore() < min) {
                min = openSet.get(i).getfScore();
                minCell = openSet.get(i);
            }
        }
        return minCell;
    }

    /**
     * Calculates manhattan distance between given cell and the current cell
     *
     * @param current Current cell
     * @param next    Given cell
     * @return Manhattan distance from current to next
     */
    private int heuristicCostEstimate(Cell current, Cell next) {

        return abs(current.getX() - next.getX()) * 10 + abs(current.getY() - next.getY()) * 10;
    }
}