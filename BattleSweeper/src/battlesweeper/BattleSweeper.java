package battlesweeper;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 *
 * @author Michael Linsinbigler
 */
public class BattleSweeper extends Application {

    public Stage window;
    private static GameLogic game;

    @Override
    public void start(Stage primaryStage) {

        window = primaryStage;

        //Look and feel
        setUserAgentStylesheet(STYLESHEET_CASPIAN);

        //Obtain difficulty selection from user
        DifficultySelection difficultySelectionWindow = new DifficultySelection();
        String userDifficultySelection = difficultySelectionWindow.display();

        if (userDifficultySelection.equals("")) {
            //No user selection made, close game
            System.exit(0);
        }

        //Set up game board
        game = new GameLogic(userDifficultySelection);

        //Finalize main window
        BorderPane brdrLayoutMain = new BorderPane();

        //Set up the menu bar
        ClientMenuBar appMenu = new ClientMenuBar();

        //Add all components to the main GUI window
        brdrLayoutMain.setTop(appMenu.getMenuObj());
        brdrLayoutMain.setCenter(game.getGameLayout());
        Scene scene = new Scene(brdrLayoutMain);

        window.setTitle("BattleSweeper");
        window.setScene(scene);
        window.show();
    }
    
    public static void changeDifficulty(String newDifficulty){
        game.setDifficulty(newDifficulty);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
