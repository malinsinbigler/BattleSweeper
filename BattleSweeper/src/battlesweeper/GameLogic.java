package battlesweeper;

import guiElements.AlertBox;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * Main game logic
 *
 * @author Michael Linsinbigler
 */
class GameLogic {

    //Game settings
    private GridPane playArea;
    private VBox gameLayout;
    private ProgressBar winStatus;
    private boolean[][] boardData;
    private final int BOARD_SIZE;
    private final int MAX_NUM_OF_SHIPS;
    private int max_num_of_misses;

    //Image resources
    private Image waveImage;
    private Image shipImage;
    private HashMap<String, Image> arrowImageCollection;

    //Player specific stats
    private int playerMisses;
    private int foundShips;
    private Text movesRemainingStatus;

    public GameLogic(String difficulty) {
        //initialize the gameLayout which contains all sub game components
        gameLayout = new VBox(10);
        gameLayout.setAlignment(Pos.CENTER);

        //Game settings consistent across all modes of play
        BOARD_SIZE = 8;
        MAX_NUM_OF_SHIPS = 10;

        //Alter game settings based on difficulty
        switch (difficulty) {
            case "easy":
                max_num_of_misses = 40;
                break;
            case "medium":
                max_num_of_misses = 30;
                break;
            case "hard":
                max_num_of_misses = 20;
                break;
        }

        //Load needed resource files
        try {
            arrowImageCollection = new HashMap<>();
            waveImage = new Image(getClass().getResource("/resources/Waves.png").toURI().toString());
            shipImage = new Image(getClass().getResource("/resources/Ship.png").toURI().toString());

            arrowImageCollection.put("right", new Image(getClass().getResource("/resources/right.png").toURI().toString()));
            arrowImageCollection.put("left", new Image(getClass().getResource("/resources/left.png").toURI().toString()));
            arrowImageCollection.put("up", new Image(getClass().getResource("/resources/up.png").toURI().toString()));
            arrowImageCollection.put("down", new Image(getClass().getResource("/resources/down.png").toURI().toString()));
        } catch (URISyntaxException ex) {
            Logger.getLogger(GameLogic.class.getName()).log(Level.SEVERE, null, ex);
        }

        //initialize the game
        init();
    }

    private void init() {

        gameLayout.getChildren().clear();

        boardData = new boolean[BOARD_SIZE][BOARD_SIZE];
        playerMisses = 0;
        foundShips = 0;

        winStatus = new ProgressBar(0);

        movesRemainingStatus = new Text(100, 200, "");
        updatedPlayerStatus();

        movesRemainingStatus.setFont(new Font(20));

        playArea = new GridPane();
        playArea.setAlignment(Pos.CENTER);
        playArea.setPadding(new Insets(5, 5, 5, 5));

        //Initialize gameboard with no ships
        //Buttons are mapped to a grid (parallel 2D array)
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {

                boardData[row][col] = false;

                Button tile = new Button();

                //Need to preserve coordinates in final variables for use in lambda
                final int fRow = row;
                final int fCol = col;

                tile.setMinSize(50, 50);
                tile.setMaxSize(50, 50);

                //All tiles start with the open ocean icon
                if (waveImage != null) {
                    ImageView waveImageView = new ImageView(waveImage);
                    waveImageView.setFitWidth(50);
                    waveImageView.setFitHeight(50);
                    tile.setGraphic(waveImageView);
                }

                tile.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-background-color: Aqua;");
                tile.setOnAction(e -> {
                    tile.setDisable(true);

                    //If this button is mapped to a ship, register hit
                    if (boardData[fRow][fCol]) {
                        tile.setStyle("-fx-background-color: red");

                        //Have tile display ship image
                        if (shipImage != null) {
                            ImageView shipImageView = new ImageView(shipImage);
                            shipImageView.setFitWidth(50);
                            shipImageView.setFitHeight(50);
                            tile.setGraphic(shipImageView);
                        }

                        //remove this ship from the game data
                        //(This allows the hint system to find the next undiscovered ship)
                        boardData[fRow][fCol] = false;

                        foundShips++;

                        //Update progress bar
                        winStatus.setProgress((double) foundShips / MAX_NUM_OF_SHIPS);

                        //Check for the win condition
                        checkForWin();
                        
                    } else {

                        playerMisses++;

                        //Find the closest ship and apply the applicable arrow hint
                        String hint = closestShipFromPoint(fRow, fCol);

                        if (hint != null) {
                            ImageView shipImageView = new ImageView(arrowImageCollection.get(hint));
                            shipImageView.setFitWidth(50);
                            shipImageView.setFitHeight(50);
                            tile.setGraphic(shipImageView);
                        }
                    }

                    updatedPlayerStatus();

                    //Check to see if the player has lost the game
                    checkForLoss();

                });

                //Add tiles to the game grid
                playArea.add(tile, col, row, 1, 1);
            }
        }

        //Add ships to game
        randomlyAddShips();

        //Progress bar indicating progress to win state
        HBox winStatusRow = new HBox(5);
        winStatusRow.getChildren().addAll(new Label("Ships Found :"), winStatus);
        winStatusRow.setAlignment(Pos.CENTER);

        gameLayout.getChildren().addAll(movesRemainingStatus, winStatusRow, playArea);
    }

    /**
     * Check for win state
     */
    private void checkForWin() {
        if (foundShips == MAX_NUM_OF_SHIPS) {
            AlertBox loseAlert = new AlertBox("Congratulations", "You Win!", AlertType.INFORMATION);
            loseAlert.display();

            //reset the game
            reset();
        }
    }

    /**
     * Check for a loss state
     */
    private void checkForLoss() {

        if (playerMisses == max_num_of_misses) {
            AlertBox loseAlert = new AlertBox("You Lose...", "Don't fret, try again!", AlertType.INFORMATION);
            loseAlert.display();

            //reset the game
            reset();
        }

    }

    //Populate the gameboard with randomly placed ships
    private void randomlyAddShips() {

        int insertedShips = 0;

        //WARNING: potential for conflicts, but since we only have a few ships on a large board it will finish in minimal cycles.
        //Be carfeul when increasing ship count 
        while (insertedShips < MAX_NUM_OF_SHIPS) {
            int randomRow = new Random().nextInt(8);
            int randomCol = new Random().nextInt(8);

            //If a ship already exist in this location, try inserting again
            if (!boardData[randomRow][randomCol]) {
                boardData[randomRow][randomCol] = true;
                insertedShips++;
            }
        }
    }

    //Refresh any components with new status information based off current game state.
    private void updatedPlayerStatus() {
        movesRemainingStatus.setText("Misses : " + playerMisses + "/" + max_num_of_misses);
    }

    /**
     *
     * Return the direction of the next closest undiscovered ship from a given
     * coordinate
     *
     * @param row int - row index
     * @param col int - col index
     * @return String - "up", "down", "left", "right"
     */
    private String closestShipFromPoint(int row, int col) {

        int orbit = 1;

        while (orbit < BOARD_SIZE) {
            //Check cells directly right
            if ((col + orbit) < boardData[row].length) {
                if (boardData[row][col + orbit]) {
                    return "right";
                }
            }

            //Check cells below
            if ((row + orbit) < boardData.length) {
                if (boardData[row + orbit][col]) {
                    return "down";
                }
            }

            //Check cells directly left
            if ((col - orbit) >= 0) {
                if (boardData[row][col - orbit]) {
                    return "left";
                }
            }

            //Check cells above
            if ((row - orbit) >= 0) {
                if (boardData[row - orbit][col]) {
                    return "up";
                }
            }

            orbit++;
        }

        return null;
    }

    //Reset the game to starting settings
    private void reset() {
        init();
    }

    /**
     *
     * @return VBox containing full game container
     */
    public Pane getGameLayout() {
        return gameLayout;
    }

}
