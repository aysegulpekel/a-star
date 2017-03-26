package com.aysegulpekel.gui;

import com.aysegulpekel.core.Cell;
import com.aysegulpekel.core.Grid;
import com.aysegulpekel.logic.AStar;

import org.apache.log4j.Logger;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class Interface extends JFrame implements ActionListener {
    private static Dimension BOARD_SIZE = new Dimension(500, 500);
    private static final int CELL_SIZE = 20;
    private JButton start, reset;
    private GameBoard gameBoard;
    private Thread game;
    private static final Logger LOGGER = Logger.getLogger(Interface.class);

    /**
     * Creates the menu to choose start or reset the board
     */
    public Interface() {
        JMenuBar menu = new JMenuBar();
        setJMenuBar(menu);

        start = new JButton("Go!");
        start.addActionListener(this);

        reset = new JButton("Clear");
        reset.addActionListener(this);

        menu.add(start);
        menu.add(reset);

        gameBoard = new GameBoard();
        add(gameBoard);
    }

    /**
     * @param isBeingPlayed keeps the board running
     */
    public void setGameBeingPlayed(boolean isBeingPlayed) {
        if (isBeingPlayed) {
            start.setEnabled(false);
            game = new Thread(gameBoard);
            game.start();
        } else {
            start.setEnabled(true);
            game.interrupt();
            gameBoard.resetBoard();
        }
    }

    /**
     * Listens reset button to clean the board or start button to play
     *
     * @param e user action event performed on board
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(reset)) {
            gameBoard.resetBoard();
            gameBoard.repaint();
            setGameBeingPlayed(false);
        } else if (e.getSource().equals(start)) {
            setGameBeingPlayed(true);
        }
    }

    /**
     * Game board is visual representation class of th board with its listeners and runnable
     * application
     */
    private class GameBoard extends JPanel implements ComponentListener, MouseListener, MouseMotionListener, Runnable {
        // Array list to keep clicked points on board
        private ArrayList<Point> clickedPoints = new ArrayList<Point>(0);
        // Array list to keep shortest path's points on board
        private ArrayList<Point> pathPoints = new ArrayList<Point>(0);

        /**
         * Adds action listeners to the board
         */
        public GameBoard() {
            // Add resizing listener
            addComponentListener(this);
            addMouseListener(this);
            addMouseMotionListener(this);
        }

        /**
         * Overrides JComponent method
         * <p>
         * Points represents the clicked cells which are indicated with blue
         * and grid is created by drawLine and drawRect methods
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            try {
                for (Point newPoint : clickedPoints) {
                    int cellX = newPoint.x * CELL_SIZE;
                    int cellY = newPoint.y * CELL_SIZE;

                    // If it's the first point, it indicates the starting point with a green cell
                    if (newPoint.equals(clickedPoints.get(0))) {
                        g.setColor(Color.green);
                        g.fillRect(cellX, cellY, CELL_SIZE, CELL_SIZE);
                    }
                    // If it's the second point, it indicates the end point with a red cell
                    else if (newPoint.equals(clickedPoints.get(1))) {
                        g.setColor(Color.red);
                        g.fillRect(cellX, cellY, CELL_SIZE, CELL_SIZE);
                    }
                    // The rest indicates the obstacles with blue cells
                    else {
                        g.setColor(Color.blue);
                        g.fillRect(cellX, cellY, CELL_SIZE, CELL_SIZE);
                    }
                }
                // The shortest path colored with gray cells
                for (Point newPoint : pathPoints) {
                    int cellX = newPoint.x * CELL_SIZE;
                    int cellY = newPoint.y * CELL_SIZE;
                    g.setColor(Color.gray);
                    g.fillRect(cellX, cellY, CELL_SIZE, CELL_SIZE);
                }
            } catch (ConcurrentModificationException cme) {
                LOGGER.info(cme.getMessage(), cme);
            }

            // Draws vertical and horizontal lines
            g.setColor(Color.BLACK);
            g.drawRect(0, 0, BOARD_SIZE.width, BOARD_SIZE.height);

            for (int i = 0; i <= BOARD_SIZE.width; i += CELL_SIZE) {
                g.drawLine(i, 0, i, BOARD_SIZE.height);
            }

            for (int i = 0; i <= BOARD_SIZE.height; i += CELL_SIZE) {
                g.drawLine(0, i, BOARD_SIZE.width, i);
            }
        }

        // Adds a new clicked points to clickedPoints array and repaints
        public void addPoint(int x, int y) {
            if (!clickedPoints.contains(new Point(x, y))) {
                clickedPoints.add(new Point(x, y));
            }
            repaint();
        }

        // Calls add point with action's positions
        public void addPoint(MouseEvent me) {

            int x = me.getPoint().x / CELL_SIZE;
            int y = me.getPoint().y / CELL_SIZE;
            if ((x >= 0) && (x < BOARD_SIZE.width / CELL_SIZE) && (y >= 0)
                    && (y < BOARD_SIZE.height / CELL_SIZE) && start.isEnabled()) {
                addPoint(x, y);
            }
        }

        // Clears the board and the obstacles
        public void resetBoard() {
            clickedPoints.clear();
            pathPoints.clear();
        }

        public void componentResized(ComponentEvent e) {

        }

        public void componentMoved(ComponentEvent e) {

        }

        public void componentShown(ComponentEvent e) {

        }

        public void componentHidden(ComponentEvent e) {

        }

        public void mouseClicked(MouseEvent e) {

        }

        public void mousePressed(MouseEvent e) {

        }

        // Allows to get clicked when the mouse released
        public void mouseReleased(MouseEvent e) {
            addPoint(e);
        }

        public void mouseEntered(MouseEvent e) {

        }

        public void mouseExited(MouseEvent e) {
        }

        // Allows to get clicked when mouse dragged
        public void mouseDragged(MouseEvent e) {
            addPoint(e);
        }

        public void mouseMoved(MouseEvent e) {

        }

        /**
         * Initializes the board with start and the end points,
         * creates the obstacles from user clicks,
         * finds each cell's neighbours and runs the a star algorithm itself.
         */
        public void run() {
            int width = BOARD_SIZE.width / CELL_SIZE;
            int height = BOARD_SIZE.height / CELL_SIZE;

            Grid grid = new Grid(width, height);

            Cell start = null;
            Cell goal = null;

            // Injects clicked cells into the grid
            for (Point current : clickedPoints) {
                // First click indicates starting point
                if (current.equals(clickedPoints.get(0))) {
                    start = grid.getGrid()[(int) current.getX()][(int) current.getY()];
                }
                // Second click indicates ending point
                else if (current.equals(clickedPoints.get(1))) {
                    goal = grid.getGrid()[(int) current.getX()][(int) current.getY()];
                }
                // The rest indicates the obstacles
                else {
                    grid.getGrid()[current.x][current.y].setObstacle(true);
                }
            }

            // Finds all the neighbours for each cell of the grid
            grid.findNeighbours();

            // Create a runnable a star object
            AStar runnable = new AStar();
            // Run a star from starting point to the goal,
            // <path> is the resulting shortest path
            List<Cell> path = runnable.runAStar(start, goal);

            // If there is no path returned, user gets warned
            if (path.isEmpty()) {
                JOptionPane.showMessageDialog(null, "You can't go there :/");
            }

            // Colors the path found
            ArrayList<Point> coloredPath = new ArrayList<Point>(0);
            for (int i = 0; i < path.size(); i++) {
                if (path.get(i).equals(start) || path.get(i).equals(goal)) {
                    continue;
                }
                coloredPath.add(new Point(path.get(i).getX(), path.get(i).getY()));
            }
            pathPoints.addAll(coloredPath);

            // Repaint for each turn
            repaint();
        }
    }

    /**
     * Setup the interface specifics
     *
     * @param args main arguments
     */
    public static void main(String[] args) {
        JFrame game = new Interface();
        game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.setSize(500, 550);
        game.setResizable(false);
        game.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - game.getWidth()) / 2,
                (Toolkit.getDefaultToolkit().getScreenSize().height - game.getHeight()) / 2);
        game.setVisible(true);
        JOptionPane.showMessageDialog(null,
                "First click: starting point.\n" + "Second click: end point.\n \n" +
                        "And click more for the obstacles as you wish!!!");
    }
}