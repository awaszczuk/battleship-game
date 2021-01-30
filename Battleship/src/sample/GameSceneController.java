package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.control.Label;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class GameSceneController {

    private Ship activeShip;
    private Game game;

    private ArrayList<Rectangle> computerGridRectangles;
    private ArrayList<Rectangle> userGridRectangles;

    @FXML
    private Button menuButton;

    @FXML
    private Button startButton;

    @FXML
    private Button randomButton;

    @FXML
    private GridPane userGrid;

    @FXML
    private GridPane computerGrid;

    @FXML
    private Label userWinLabel;

    @FXML
    private Label computerWinLabel;

    @FXML
    private Rectangle shadowRectangle1;

    @FXML
    private Rectangle shadowRectangle2;

    public GameSceneController()
    {
        userGridRectangles = new ArrayList<>();
        computerGridRectangles = new ArrayList<>();
    }

    @FXML
    public void initialize() {

        // add water rectangles
        for(int i = 0; i < 10; i++)
        {
            for(int j = 0; j < 10; j++)
            {
                // adding users water rectangles
                Rectangle rec2 = new Rectangle(27, 27, SystemColor.water);
                rec2.setArcHeight(0);
                rec2.setStroke(Color.BLACK);
                userGrid.add(rec2, i, j);
                userGridRectangles.add(rec2);

                // adding computers water rectangles
                Rectangle rec1 = new Rectangle(27, 27, SystemColor.water);
                rec1.setArcHeight(0);
                rec1.setStroke(Color.BLACK);

                // adding mouse click event handler
                rec1.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event)
                    {
                        // user move(s)
                        if(game.gameState != GameState.userMove)
                            return;

                        // get clicked position
                        Rectangle clickedRectangle = (Rectangle) event.getSource();
                        clickedRectangle.setCursor(Cursor.DEFAULT);
                        int x = GridPane.getColumnIndex(clickedRectangle);
                        int y = GridPane.getRowIndex(clickedRectangle);
                        Position position = new Position(x, y);

                        // return if position already clicked
                        if(game.positionAlreadyClicked(position))
                            return;

                        // make the move
                        // if ship hit
                        if(game.userMove(position).hit)
                        {
                            // find clicked ship
                            Ship ship = game.findComputerShipByPosition(position);

                            Rectangle shipRectangle = new Rectangle(27, 27, SystemColor.ship);
                            shipRectangle.setArcHeight(0);
                            shipRectangle.setStroke(Color.BLACK);
                            computerGrid.add(shipRectangle, position.x, position.y);
                            ship.rectangles.add(shipRectangle);

                            // check if clicked ship is sunk
                            if(ship.isSunk())
                                ship.changeColor(SystemColor.sunkShip);

                            ImageView hitImage = new ImageView();
                            hitImage.setImage(new Image(getClass().getResource("hit.png").toString()));
                            computerGrid.add(hitImage, x, y);
                            GridPane.setHalignment(hitImage, HPos.CENTER);

                            // check if game won
                            checkIfWin();
                            return;
                        }

                        //if miss
                        ImageView missImage = new ImageView();
                        missImage.setImage(new Image(getClass().getResource("miss.png").toString()));
                        computerGrid.add(missImage, x, y);
                        GridPane.setHalignment(missImage, HPos.CENTER);

                        game.gameState = GameState.computerMove;

                        // computer move(s)
                        while(true)
                        {
                            // make random move
                            Shot computerShot = game.computerMove();

                            // make next moves if ship hit
                            if(computerShot.hit)
                            {
                                // check if ship sunk
                                Ship ship = game.findUserShipByPosition(computerShot.position);
                                if(ship.isSunk())
                                    ship.changeColor(SystemColor.sunkShip);

                                ImageView hitImage2 = new ImageView();
                                hitImage2.setImage(new Image(getClass().getResource("hit.png").toString()));
                                userGrid.add(hitImage2, computerShot.position.x, computerShot.position.y);
                                GridPane.setHalignment(hitImage2, HPos.CENTER);

                                // check if game won
                                checkIfWin();
                                if(game.gameState == GameState.end)
                                    return;
                            }
                            // return if miss
                            else
                            {
                                ImageView missImage2 = new ImageView();
                                missImage2.setImage(new Image(getClass().getResource("miss.png").toString()));
                                userGrid.add(missImage2, computerShot.position.x, computerShot.position.y);
                                GridPane.setHalignment(missImage2, HPos.CENTER);

                                game.gameState = GameState.userMove;
                                return;
                            }

                        }
                    }
                });

                computerGrid.add(rec1, i, j);
                computerGridRectangles.add(rec1);
            }
        }

        randomButtonClicked();
    }

    private void checkIfWin()
    {
        //check if user or computer won
        if(game.computerWon())
        {
            game.gameState = GameState.end;
            computerWinLabel.setVisible(true);
            shadowRectangle1.setVisible(true);
            shadowRectangle2.setVisible(true);

            for(Rectangle rec : computerGridRectangles)
                rec.setCursor(Cursor.DEFAULT);

        }
        else if(game.userWon())
        {
            game.gameState = GameState.end;
            userWinLabel.setVisible(true);
            shadowRectangle1.setVisible(true);
            shadowRectangle2.setVisible(true);

            for(Rectangle rec : computerGridRectangles)
                rec.setCursor(Cursor.DEFAULT);
        }
    }


    @FXML
    private void menuButtonClicked() throws IOException {

        //change scene to start scene
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = getClass().getResource("startScene.fxml");
        loader.setLocation(xmlUrl);
        Parent newRoot = loader.load();

        menuButton.getScene().setRoot(newRoot);
    }

    @FXML
    private void startButtonClicked() throws IOException {

        // set active ship to not active
        if(activeShip != null)
        {
            activeShip.changeColor(SystemColor.ship);
            activeShip = null;
        }

        // disable Random and Start buttons
        randomButton.setDisable(true);
        startButton.setDisable(true);

        // set ship rectangles cursor back to default
        for(Ship s : game.userShips)
            for(Rectangle rec : s.rectangles)
                rec.setCursor(Cursor.DEFAULT);

        // set water rectangles cursor to hand
        for(Rectangle rec : computerGridRectangles)
            rec.setCursor(Cursor.HAND);

        game.gameState = GameState.userMove;
    }

    @FXML
    private void randomButtonClicked() {

        // remove any previous ship rectangles
        if(game != null)
        {
            if(game.gameState != GameState.placingShips)
                return;

            for (Ship ship : game.userShips)
                for (Rectangle rec : ship.rectangles)
                    userGrid.getChildren().remove(rec);
        }

        // create new game (with random ships)
        game = new Game();

        // add user ship rectangles
        for (Ship ship : game.userShips)
            for (Position pos : ship.positions)
            {
                Rectangle rec = new Rectangle(27, 27, SystemColor.ship);
                rec.setArcHeight(0);
                rec.setStroke(Color.BLACK);
                rec.setCursor(Cursor.HAND);
                ship.rectangles.add(rec);

                // set mouse click handler (to move ships)
                rec.setOnMouseClicked(new EventHandler<MouseEvent>()
                {
                    public void handle(MouseEvent event)
                    {
                        // check if ships can be moved
                        if(game.gameState != GameState.placingShips)
                            return;

                        // get clicked rectangle
                        Rectangle activeRec = (Rectangle) event.getSource();

                        // find clicked ship
                        for (Ship ship : game.userShips)
                            if(ship.containsRectangle(activeRec))
                            {
                                activeShip = ship;
                                CheckIfMisplaced();
                                break;
                            }

                        // change orientation on right mouse button click
                        if(event.getButton() == MouseButton.SECONDARY)
                        {
                            activeShip.changeOrientation();
                            CheckIfMisplaced();
                        }

                        userGrid.requestFocus();
                    }
                });

                // add ship rectangle to grid
                userGrid.add(rec, pos.x, pos.y);
            }
    }

    @FXML
    private void newGameButtonClicked() throws IOException
    {
        // set the scene again to game scene
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = getClass().getResource("gameScene.fxml");
        loader.setLocation(xmlUrl);
        Parent newRoot = loader.load();

        menuButton.getScene().setRoot(newRoot);
    }

    @FXML
    private void KeyPressed(KeyEvent event)
    {
        // check if any ship is clicked and if it is placing time
        if (activeShip == null || game.gameState != GameState.placingShips)
            return;

        // move the ship
        switch (event.getCode())
        {
            case UP:
                activeShip.moveUp();
                break;

            case DOWN:
                activeShip.moveDown();
                break;

            case RIGHT:
                activeShip.moveRight();
                break;

            case LEFT:
                activeShip.moveLeft();
                break;

            case SPACE:
                activeShip.changeOrientation();
                break;
        }

        // check if any ships are misplaced
        CheckIfMisplaced();
    }

    // checks if any ship is misplaced and changes its color
    private boolean CheckIfMisplaced()
    {
        boolean isAnyMisplaced = false;

        for (Ship ship1 : game.userShips)
        {
            boolean isOneMisplaced = false;

            for (Ship ship2 : game.userShips)
            {
                if(ship1 != ship2 && ship1.overlaps(ship2))
                {
                    ship1.changeColor(SystemColor.errorShip);
                    ship2.changeColor(SystemColor.errorShip);

                    if(ship1 == activeShip)
                        ship1.changeColor(SystemColor.errorActiveShip);

                    else if(ship2 == activeShip)
                        ship2.changeColor(SystemColor.errorActiveShip);

                    isOneMisplaced = true;
                }
            }

            if(!isOneMisplaced)
            {
                if(ship1 == activeShip)
                    ship1.changeColor(SystemColor.activeShip);
                else
                    ship1.changeColor(SystemColor.ship);
            }
            else
                isAnyMisplaced = true;
        }

        if(isAnyMisplaced)
            startButton.setDisable(true);
        else
            startButton.setDisable(false);

        return isAnyMisplaced;
    }
}
