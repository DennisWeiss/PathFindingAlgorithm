package sample;

/**
 * Created by weiss on 12/21/2016.
 */
public class Tile {
    boolean walkable;
    double awayFromTarget;
    boolean belongsToPath;
    boolean isStart;
    boolean isTarget;
    boolean marked;

    public Tile() {
        this.walkable = false;
        this.awayFromTarget = Double.MAX_VALUE;
        this.belongsToPath = false;
        this.isStart = false;
        this.isTarget = false;
        this.marked = false;
    }

    public Tile(boolean walkable, double awayFromTarget, boolean belongsToPath, boolean isStart, boolean isTarget, boolean marked) {
        this.walkable = walkable;
        this.awayFromTarget = awayFromTarget;
        this.belongsToPath = belongsToPath;
        this.isStart = isStart;
        this.isTarget = isTarget;
        this.marked = marked;
    }

    public void setAwayFromTarget(double distance) {
        this.awayFromTarget = distance;
    }

    public double getAwayFromTarget() {
        return awayFromTarget;
    }

    public void setTarget(boolean target) {
        isTarget = target;
    }

    public void setStart(boolean start) {
        isStart = start;
    }

    public void setBelongsToPath(boolean belongsToPath) {
        this.belongsToPath = belongsToPath;
    }

    public boolean isTarget() {
        return isTarget;
    }

    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }

    public boolean isStart() {
        return isStart;
    }

    public boolean isBelongsToPath() {
        return belongsToPath;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    public void mark() {
        this.marked = true;
    }
}
