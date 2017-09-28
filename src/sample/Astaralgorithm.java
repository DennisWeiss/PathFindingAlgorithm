package sample;

import javafx.scene.control.Button;

/**
 * Created by weiss on 12/21/2016.
 */
public class Astaralgorithm {
    int width;
    int height;

    Tile[][] tile;

    public Astaralgorithm(int width, int height) {
        this.width = width;
        this.height = height;
        this.tile = new Tile[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                tile[i][j] = new Tile();
            }
        }
    }

    public void reset() {
        this.tile = new Tile[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                tile[i][j] = new Tile();
            }
        }
    }

    public void setTarget(int x, int y) {
        tile[x][y].setTarget(true);
        tile[x][y].setAwayFromTarget(0);
    }

    public void setDistances(int x, int y) {

        /*if (x > 0 && y > 0) {
            if (tile[x-1][y-1].walkable && tile[x-1][y-1].awayFromTarget - tile[x][y].awayFromTarget > Math.sqrt(2)) {
                tile[x-1][y-1].setAwayFromTarget(tile[x][y].getAwayFromTarget() + Math.sqrt(2));
                setDistances(x-1, y-1);
            }
        }*/
        if (x > 0) {
            if (tile[x-1][y].walkable && tile[x-1][y].awayFromTarget - tile[x][y].awayFromTarget > 1) {
                tile[x-1][y].setAwayFromTarget(tile[x][y].getAwayFromTarget() + 1);
                setDistances(x-1, y);
            }
        }
        /*if (x > 0 && y < height - 1) {
            if (tile[x-1][y+1].walkable && tile[x-1][y+1].awayFromTarget - tile[x][y].awayFromTarget > Math.sqrt(2)) {
                tile[x-1][y+1].setAwayFromTarget(tile[x][y].getAwayFromTarget() + Math.sqrt(2));
                setDistances(x-1, y+1);
            }
        }*/
        if (y > 0) {
            if (tile[x][y-1].walkable && tile[x][y-1].awayFromTarget - tile[x][y].awayFromTarget > 1) {
                tile[x][y-1].setAwayFromTarget(tile[x][y].getAwayFromTarget() + 1);
                setDistances(x, y-1);
            }
        }
        if (y < height - 1) {
            if (tile[x][y+1].walkable && tile[x][y+1].awayFromTarget - tile[x][y].awayFromTarget > 1) {
                tile[x][y+1].setAwayFromTarget(tile[x][y].getAwayFromTarget() + 1);
                setDistances(x, y+1);
            }
        }
        /*if (x < width - 1 && y > 0) {
            if (tile[x+1][y-1].walkable && tile[x+1][y-1].awayFromTarget - tile[x][y].awayFromTarget > Math.sqrt(2)) {
                tile[x+1][y-1].setAwayFromTarget(tile[x][y].getAwayFromTarget() + Math.sqrt(2));
                setDistances(x+1, y-1);
            }
        }*/
        if (x < width - 1) {
            if (tile[x+1][y].walkable && tile[x+1][y].awayFromTarget - tile[x][y].awayFromTarget > 1) {
                tile[x+1][y].setAwayFromTarget(tile[x][y].getAwayFromTarget() + 1);
                setDistances(x+1, y);
            }
        }
        /*if (x < width - 1 && y < height - 1) {
            if (tile[x + 1][y + 1].walkable && tile[x + 1][y + 1].awayFromTarget - tile[x][y].awayFromTarget > Math.sqrt(2)) {
                tile[x + 1][y + 1].setAwayFromTarget(tile[x][y].getAwayFromTarget() + Math.sqrt(2));
                setDistances(x + 1, y + 1);
            }
        }*/
    }

    public void setStart(int x, int y) {
        tile[x][y].setStart(true);
    }

    public void resetPath() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                tile[i][j].setAwayFromTarget(Double.MAX_VALUE);
                tile[i][j].setBelongsToPath(false);
                tile[i][j].setMarked(false);
            }
        }
    }

    public void getPath(int startX, int startY) {
        double awayFromTarget = Double.MAX_VALUE;
        int indexX = startX;
        int indexY = startY;

        if (!tile[startX][startY].isTarget()) {
            /*if (startX > 0 && startY > 0) {
                if (tile[startX-1][startY-1].walkable && tile[startX-1][startY-1].getAwayFromTarget() < awayFromTarget) {
                    awayFromTarget = tile[startX-1][startY-1].getAwayFromTarget();
                    indexX = startX - 1;
                    indexY = startY - 1;
                }
            }*/
            if (startX > 0) {
                if (tile[startX-1][startY].walkable && tile[startX-1][startY].getAwayFromTarget() < awayFromTarget) {
                    awayFromTarget = tile[startX-1][startY].getAwayFromTarget();
                    indexX = startX - 1;
                    indexY = startY;
                }
            }
            /*if (startX > 0 && startY < height - 1) {
                if (tile[startX-1][startY+1].walkable && tile[startX-1][startY+1].getAwayFromTarget() < awayFromTarget) {
                    awayFromTarget = tile[startX-1][startY+1].getAwayFromTarget();
                    indexX = startX - 1;
                    indexY = startY + 1;
                }
            }*/
            if (startY > 0) {
                if (tile[startX][startY-1].walkable && tile[startX][startY-1].getAwayFromTarget() < awayFromTarget) {
                    awayFromTarget = tile[startX][startY-1].getAwayFromTarget();
                    indexX = startX;
                    indexY = startY - 1;
                }
            }
            if (startY < height - 1) {
                if (tile[startX][startY+1].walkable && tile[startX][startY+1].getAwayFromTarget() < awayFromTarget) {
                    awayFromTarget = tile[startX][startY+1].getAwayFromTarget();
                    indexX = startX;
                    indexY = startY + 1;
                }
            }
            /*if (startX < width - 1 && startY > 0) {
                if (tile[startX+1][startY-1].walkable && tile[startX+1][startY-1].getAwayFromTarget() < awayFromTarget) {
                    awayFromTarget = tile[startX+1][startY-1].getAwayFromTarget();
                    indexX = startX + 1;
                    indexY = startY - 1;
                }
            }*/
            if (startX < width - 1) {
                if (tile[startX+1][startY].walkable && tile[startX+1][startY].getAwayFromTarget() < awayFromTarget) {
                    awayFromTarget = tile[startX+1][startY].getAwayFromTarget();
                    indexX = startX + 1;
                    indexY = startY;
                }
            }
            /*if (startX < width - 1 && startY < height - 1) {
                if (tile[startX+1][startY+1].walkable && tile[startX+1][startY+1].getAwayFromTarget() < awayFromTarget) {
                    awayFromTarget = tile[startX+1][startY+1].getAwayFromTarget();
                    indexX = startX + 1;
                    indexY = startY + 1;
                }
            }*/
            tile[indexX][indexY].setBelongsToPath(true);
            getPath(indexX, indexY);
        }
    }

    public void cloning(int startX, int startY, int endX, int endY, Button[][] buttons, long speed) {
        Thread thread = new Thread(new Cloning(startX, startY, tile, endX, endY, Direction.STAY, buttons, speed));
        thread.start();
    }
}
