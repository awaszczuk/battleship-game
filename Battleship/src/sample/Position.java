package sample;

// stores x and y coordinates (in GridPane)
public class Position
{
    public int x;
    public int y;

    public Position(int X, int Y)
    {
        x = X;
        y = Y;
    }

    public boolean isEqual(Position position)
    {
        return position.x == x && position.y == y;
    }
}
