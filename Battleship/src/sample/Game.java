package sample;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

// stores whole game
public class Game
{
    public List<Ship> userShips; // stores user's ships
    public List<Ship> computerShips; // stores computer's ships

    private List<Shot> userShots; // stores user's shots
    private List<Shot> computerShots; // stores computer's shots

    public GameState gameState; // state of the game (beginning, user move, computer move, end)
    private List<Position> shotsAvailableToComputer; // positions left to shot by computer

    public Game()
    {
        userShips = new ArrayList<>();
        computerShips = new ArrayList<>();

        // adding random ships to user and computer
        addRandomShip(4, userShips);
        addRandomShip(3, userShips);
        addRandomShip(3, userShips);
        addRandomShip(2, userShips);
        addRandomShip(2, userShips);
        addRandomShip(2, userShips);
        addRandomShip(1, userShips);
        addRandomShip(1, userShips);
        addRandomShip(1, userShips);
        addRandomShip(1, userShips);

        addRandomShip(4, computerShips);
        addRandomShip(3, computerShips);
        addRandomShip(3, computerShips);
        addRandomShip(2, computerShips);
        addRandomShip(2, computerShips);
        addRandomShip(2, computerShips);
        addRandomShip(1, computerShips);
        addRandomShip(1, computerShips);
        addRandomShip(1, computerShips);
        addRandomShip(1, computerShips);


        userShots = new ArrayList<>();
        computerShots = new ArrayList<>();

        shotsAvailableToComputer = new ArrayList<>();
        for(int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++)
                shotsAvailableToComputer.add(new Position(i, j));

        gameState = GameState.placingShips;
    }

    // check if user won
    public boolean userWon()
    {
        for(Ship s : computerShips)
            if(!s.isSunk())
                return false;

        return true;
    }

    // check if computer won
    public boolean computerWon()
    {
        for(Ship s : userShips)
            if(!s.isSunk())
                return false;

        return true;
    }

    // find computer's ship
    public Ship findComputerShipByPosition(Position p)
    {
        for(Ship s : computerShips)
            if(s.containsPosition(p))
                return s;

        return null;
    }

    // find user's ship
    public Ship findUserShipByPosition(Position p)
    {
        for(Ship s : userShips)
            if(s.containsPosition(p))
                return s;

        return null;
    }

    // make user move
    public Shot userMove(Position position)
    {
        boolean hit = false;
        Ship ship = findComputerShipByPosition(position);

        if(ship != null)
        {
            ship.hit();
            hit = true;
        }

        Shot shot = new Shot(position, hit);
        userShots.add(shot);
        return shot;
    }

    // make computer move(s)
    public Shot computerMove()
    {
        boolean hit = false;
        int randomIndex = ThreadLocalRandom.current().nextInt(0, shotsAvailableToComputer.size());
        Position randomPosition = shotsAvailableToComputer.get(randomIndex);
        shotsAvailableToComputer.remove(randomIndex);

        Ship ship = findUserShipByPosition(randomPosition);
        if(ship != null)
        {
            ship.hit();
            hit = true;
            shotsAvailableToComputer.removeIf(pos -> pos.isEqual(new Position(randomPosition.x + 1, randomPosition.y + 1)));
            shotsAvailableToComputer.removeIf(pos -> pos.isEqual(new Position(randomPosition.x + 1, randomPosition.y - 1)));
            shotsAvailableToComputer.removeIf(pos -> pos.isEqual(new Position(randomPosition.x - 1, randomPosition.y + 1)));
            shotsAvailableToComputer.removeIf(pos -> pos.isEqual(new Position(randomPosition.x - 1, randomPosition.y - 1)));

            if(ship.isSunk())
                for(Position p : ship.positions)
                {
                    shotsAvailableToComputer.removeIf(pos -> pos.isEqual(new Position(p.x, p.y + 1)));
                    shotsAvailableToComputer.removeIf(pos -> pos.isEqual(new Position(p.x + 1, p.y)));
                    shotsAvailableToComputer.removeIf(pos -> pos.isEqual(new Position(p.x, p.y - 1)));
                    shotsAvailableToComputer.removeIf(pos -> pos.isEqual(new Position(p.x - 1, p.y)));
                }
        }

        Shot shot = new Shot(randomPosition, hit);
        computerShots.add(shot);
        return shot;
    }

    // check if position has already been clicked by the user
    public boolean positionAlreadyClicked(Position position)
    {
        return userShots.stream().anyMatch(shot -> position.isEqual(shot.position));
    }

    // add random ship of some size to list
    private void addRandomShip(int size, List<Ship> ships)
    {
        ArrayList<Ship> availableShips = new ArrayList<>();

        for(int i = 0; i < 10 - size; i++)
        {
            for(int j = 0; j < 10; j++)
            {
                ArrayList<Position> list = new ArrayList<>();

                for(int k = 0; k < size; k++)
                    list.add(new Position(i + k, j));

                Ship randomShip = new Ship(list);
                boolean isCorrectlyPlaced = true;

                for(Ship s : ships)
                    if(randomShip.overlaps(s))
                    {
                        isCorrectlyPlaced = false;
                        break;
                    }

                if(isCorrectlyPlaced)
                    availableShips.add(randomShip);
            }
        }

        for(int i = 0; i < 10; i++)
        {
            for(int j = 0; j < 10 - size; j++)
            {
                ArrayList<Position> list = new ArrayList<>();

                for(int k = 0; k < size; k++)
                    list.add(new Position(i, j + k));

                Ship randomShip = new Ship(list);
                boolean isCorrectlyPlaced = true;

                for(Ship s : ships)
                    if(randomShip.overlaps(s))
                    {
                        isCorrectlyPlaced = false;
                        break;
                    }

                if(isCorrectlyPlaced)
                    availableShips.add(randomShip);
            }
        }
        int randomPosition = ThreadLocalRandom.current().nextInt(0, availableShips.size());
        ships.add(availableShips.get(randomPosition));
    }
}
