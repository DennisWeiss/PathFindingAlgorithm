package sample;

import javafx.scene.control.Button;
import java.util.ArrayList;

public class Cloning implements Runnable {
    int startX;
    int startY;
    Tile[][] maze;
    int endX;
    int endY;
    Direction direction;
    long speed;

    Button[][] buttons;

    static boolean foundTarget = false;

    @Override
    public void run() {
        try {
            searchPath(startX, startY, maze, endX, endY, direction);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }

    }

    public Cloning(int startX, int startY, Tile[][] maze, int endX, int endY, Direction direction, Button[][] buttons, long speed) {
        this.startX = startX;
        this.startY = startY;
        this.maze = maze;
        this.endX = endX;
        this.endY = endY;
        this.direction = direction;
        this.buttons = buttons;
        this.speed = speed;
    }

    public synchronized void searchPath(int startX, int startY, Tile[][] maze, int endX, int endY, Direction direction) {

        try {
            Thread.sleep(speed);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (startX == endX && startY == endY) {
            //System.out.println("Found target!!!");
            foundTarget = true;
        } else if (!foundTarget) {
            //System.out.printf("invoked Thread: endX=%d endY=%d\n", endX, endY);

            //System.out.println(startX + " " + startY);
            //System.out.println(direction);
            if (direction != Direction.STAY) {
                markGreen(startX, startY);
            }
            ArrayList<Direction> possibleDirections = new ArrayList<>();
            try {
                if (maze[startX + 1][startY].walkable && !maze[startX + 1][startY].isMarked() && direction != Direction.LEFT) {
                    possibleDirections.add(Direction.RIGHT);
                }
            } catch (ArrayIndexOutOfBoundsException e) {

            }
            try {
                if (maze[startX - 1][startY].walkable && !maze[startX - 1][startY].isMarked() && direction != Direction.RIGHT) {
                    possibleDirections.add(Direction.LEFT);
                }
            } catch (ArrayIndexOutOfBoundsException e) {

            }
            try {
                if (maze[startX][startY - 1].walkable && !maze[startX][startY - 1].isMarked() && direction != Direction.DOWN) {
                    possibleDirections.add(Direction.UP);
                }
            } catch (ArrayIndexOutOfBoundsException e) {

            }
            try {
                if (maze[startX][startY + 1].walkable && !maze[startX][startY + 1].isMarked() && direction != Direction.UP) {
                    possibleDirections.add(Direction.DOWN);
                }
            } catch (ArrayIndexOutOfBoundsException e) {

            }

            if (possibleDirections.size() == 1) {
                if (possibleDirections.get(0) == Direction.RIGHT) {
                    searchPath(startX + 1, startY, maze, endX, endY, Direction.RIGHT);
                } else if (possibleDirections.get(0) == Direction.LEFT) {
                    searchPath(startX - 1, startY, maze, endX, endY, Direction.LEFT);
                } else if (possibleDirections.get(0) == Direction.UP) {
                    searchPath(startX, startY - 1, maze, endX, endY, Direction.UP);
                } else if (possibleDirections.get(0) == Direction.DOWN) {
                    searchPath(startX, startY + 1, maze, endX, endY, Direction.DOWN);
                }
            } else if (possibleDirections.size() > 1) {
                Thread[] threads = new Thread[possibleDirections.size() - 1];
                for (int i = 1; i < possibleDirections.size(); i++) {
                    if (possibleDirections.get(i) == Direction.RIGHT) {
                        threads[i - 1] = new Thread(new Cloning(startX + 1, startY, maze, endX, endY, Direction.RIGHT, buttons, speed));
                    } else if (possibleDirections.get(i) == Direction.LEFT) {
                        threads[i - 1] = new Thread(new Cloning(startX - 1, startY, maze, endX, endY, Direction.LEFT, buttons, speed));
                    } else if (possibleDirections.get(i) == Direction.UP) {
                        threads[i - 1] = new Thread(new Cloning(startX, startY - 1, maze, endX, endY, Direction.UP, buttons, speed));
                    } else if (possibleDirections.get(i) == Direction.DOWN) {
                        threads[i - 1] = new Thread(new Cloning(startX, startY + 1, maze, endX, endY, Direction.DOWN, buttons, speed));
                    }
                }
                for (int i = 0; i < threads.length; i++) {
                    threads[i].start();
                }
                for (int i = 0; i < threads.length; i++)
                    if (possibleDirections.get(0) == Direction.RIGHT) {
                        searchPath(startX + 1, startY, maze, endX, endY, Direction.RIGHT);
                    } else if (possibleDirections.get(0) == Direction.LEFT) {
                        searchPath(startX - 1, startY, maze, endX, endY, Direction.LEFT);
                    } else if (possibleDirections.get(0) == Direction.UP) {
                        searchPath(startX, startY - 1, maze, endX, endY, Direction.UP);
                    } else if (possibleDirections.get(0) == Direction.DOWN) {
                        searchPath(startX, startY + 1, maze, endX, endY, Direction.DOWN);
                    }
            }

        }

    }

    void markGreen(int x, int y) {
        if (!foundTarget) {
            if (!maze[x][y].isStart() && !maze[x][y].isTarget()) {
                maze[x][y].mark();
                buttons[x][y].setStyle("-fx-base: #00FF00");
            }
        }
    }
}
