package sample;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import java.util.List;

// stores information about the ship (size, positions, hits)
public class Ship
{
    private int size;
    private int hitCount;   // number of hits
    public List<Position> positions; // ship's positions
    public List<Rectangle> rectangles; // ship's rectangles (used in graphic interface)

    public Ship(List<Position> p)
    {
        size = p.size();
        hitCount = 0;
        positions = new ArrayList<>(p);
        rectangles = new ArrayList<>();
    }

    // check if ship contains position
    public boolean containsPosition(Position position)
    {
        for (Position p : positions)
            if(p.isEqual(position)) return true;

        return false;
    }

    // check if ship contains rectangle
    public boolean containsRectangle(Rectangle rec)
    {
        for (Rectangle r : rectangles)
            if(rec == r) return true;

        return false;
    }

    // hit ship
    public void hit()
    {
        hitCount++;
    }

    // check if ship is sunk
    public boolean isSunk()
    {
        return hitCount >= size;
    }

    // change color in GUI
    public void changeColor(Paint color)
    {
        for (Rectangle rec : rectangles)
            rec.setFill(color);
    }

    // move ship one position up
    public void moveUp()
    {
        for (Position p : positions)
            if(p.y <= 0)
                return;

        for (Rectangle rec : rectangles)
        {
            int y = GridPane.getRowIndex(rec);
            GridPane.setRowIndex(rec, y - 1);
        }

        for (Position p : positions)
            p.y--;
    }

    // move ship one position down
    public void moveDown()
    {
        for (Position p : positions)
            if(p.y >= 9)
                return;

        for (Rectangle rec : rectangles)
        {
            int y = GridPane.getRowIndex(rec);
            GridPane.setRowIndex(rec, y + 1);
        }

        for (Position p : positions)
            p.y++;
    }

    // move ship one position left
    public void moveLeft()
    {
        for (Position p : positions)
            if(p.x <= 0)
                return;

        for (Rectangle rec : rectangles)
        {
            int x = GridPane.getColumnIndex(rec);
            GridPane.setColumnIndex(rec, x - 1);
        }

        for (Position p : positions)
            p.x--;
    }

    // move ship one position right
    public void moveRight()
    {
        for (Position p : positions)
            if(p.x >= 9)
                return;

        for (Rectangle rec : rectangles)
        {
            int x = GridPane.getColumnIndex(rec);
            GridPane.setColumnIndex(rec, x + 1);
        }

        for (Position p : positions)
            p.x++;
    }

    // check if position is neighbour of the ship
    public boolean bordersOn(Position position)
    {
        int x1 = position.x;
        int y1 = position.y;

        for (Position p : positions)
        {
            int x2 = p.x;
            int y2 = p.y;
            boolean rows = y1 == y2 || y1 == y2 + 1 || y1 == y2 - 1;

            if(x1 == x2)
            {
                if(rows)
                    return true;
            }
            else if(x1 == x2 + 1)
            {
                if(rows)
                    return true;
            }
            else if(x1 == x2 - 1)
            {
                if(rows)
                    return true;
            }
        }
        return false;
    }

    // check if two ships are misplaced
    public boolean overlaps(Ship ship)
    {
        if(this == ship)
            return false;

        for (Position p : positions)
            if(ship.containsPosition(p) || ship.bordersOn(p))
                return true;

        return false;
    }

    // change orientation of ship (vertical or horizontal)
    public void changeOrientation()
    {
        int x = positions.get(0).x;
        int y = positions.get(0).y;

        int tempX = x;
        int tempY = y;

        for(Position p : positions)
        {
            if(p.x == x)
            {
                if(tempX < 0 || tempX > 9)
                    return;
            }
            else if(p.y == y)
            {
                if(tempY < 0 || tempY > 9)
                    return;
            }

            tempX++;
            tempY++;
        }

        tempX = x;
        tempY = y;

        for(Rectangle rec : rectangles)
        {
            if(GridPane.getColumnIndex(rec) == x)
            {
                GridPane.setColumnIndex(rec, tempX);
                GridPane.setRowIndex(rec, y);
            }
            else if(GridPane.getRowIndex(rec) == y)
            {
                GridPane.setRowIndex(rec, tempY);
                GridPane.setColumnIndex(rec, x);
            }

            tempX++;
            tempY++;
        }

        tempX = x;
        tempY = y;

        for(Position p : positions)
        {
            if(p.x == x)
            {
                p.x = tempX;
                p.y = y;
            }
            else if(p.y == y)
            {
                p.y = tempY;
                p.x = x;
            }

            tempX++;
            tempY++;
        }
    }
}
