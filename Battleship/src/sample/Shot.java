package sample;

// stores information about the shot (position and hit or miss)
public class Shot
{
    public Position position;
    public boolean hit;

    public Shot(Position Position, boolean Hit)
    {
        position = Position;
        hit = Hit;
    }
}
