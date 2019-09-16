package net.minecraft.pathfinding;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class Path
{
    /** The actual points in the path */
    private final PathPoint[] points;
    private PathPoint[] openSet = new PathPoint[0];
    private PathPoint[] closedSet = new PathPoint[0];

    /** PathEntity Array Index the Entity is currently targeting */
    private int currentPathIndex;

    /** The total length of the path */
    private int pathLength;

    public Path(PathPoint[] pathpoints)
    {
        this.points = pathpoints;
        this.pathLength = pathpoints.length;
    }

    /**
     * Directs this path to the next point in its array
     */
    public void incrementPathIndex()
    {
        ++this.currentPathIndex;
    }

    /**
     * Returns true if this path has reached the end
     */
    public boolean isFinished()
    {
        return this.currentPathIndex >= this.pathLength;
    }

    @Nullable

    /**
     * returns the last PathPoint of the Array
     */
    public PathPoint getFinalPathPoint()
    {
        return this.pathLength > 0 ? this.points[this.pathLength - 1] : null;
    }

    /**
     * return the PathPoint located at the specified PathIndex, usually the current one
     */
    public PathPoint getPathPointFromIndex(int index)
    {
        return this.points[index];
    }

    public void setPoint(int index, PathPoint point)
    {
        this.points[index] = point;
    }

    public int getCurrentPathLength()
    {
        return this.pathLength;
    }

    public void setCurrentPathLength(int length)
    {
        this.pathLength = length;
    }

    public int getCurrentPathIndex()
    {
        return this.currentPathIndex;
    }

    public void setCurrentPathIndex(int currentPathIndexIn)
    {
        this.currentPathIndex = currentPathIndexIn;
    }

    /**
     * Gets the vector of the PathPoint associated with the given index.
     */
    public Vec3d getVectorFromIndex(Entity entityIn, int index)
    {
        double d0 = (double)this.points[index].x + (double)((int)(entityIn.width + 1.0F)) * 0.5D;
        double d1 = (double)this.points[index].y;
        double d2 = (double)this.points[index].z + (double)((int)(entityIn.width + 1.0F)) * 0.5D;
        return new Vec3d(d0, d1, d2);
    }

    /**
     * returns the current PathEntity target node as Vec3D
     */
    public Vec3d getPosition(Entity entityIn)
    {
        return this.getVectorFromIndex(entityIn, this.currentPathIndex);
    }

    public Vec3d getCurrentPos()
    {
        PathPoint pathpoint = this.points[this.currentPathIndex];
        return new Vec3d((double)pathpoint.x, (double)pathpoint.y, (double)pathpoint.z);
    }

    /**
     * Returns true if the EntityPath are the same. Non instance related equals.
     */
    public boolean isSamePath(Path pathentityIn)
    {
        if (pathentityIn == null)
        {
            return false;
        }
        else if (pathentityIn.points.length != this.points.length)
        {
            return false;
        }
        else
        {
            for (int i = 0; i < this.points.length; ++i)
            {
                if (this.points[i].x != pathentityIn.points[i].x || this.points[i].y != pathentityIn.points[i].y || this.points[i].z != pathentityIn.points[i].z)
                {
                    return false;
                }
            }

            return true;
        }
    }
}
