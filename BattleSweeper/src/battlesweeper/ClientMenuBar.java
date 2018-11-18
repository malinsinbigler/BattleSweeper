package battlesweeper;

import guiElements.ConfirmBox;
import guiElements.InstructionsWindow;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

/**
 *
 * Menu bar for the application's main window
 * 
 * @author Michael Linsinbigler
 */
public class ClientMenuBar {
    private final MenuBar menuBar;

    public ClientMenuBar() {
        menuBar = new MenuBar();
        init();
    }

    /**
     *
     * @return The MenuBar object
     */
    public MenuBar getMenuObj() {
        return menuBar;
    }

    /**
     * Initialize the menu bar with all elements
     */
    private void init() {

        // --- Menu Actions
        Menu menuActions = new Menu("Actions");
        
        //Difficulty menu
        Menu menuChangeDifficulty = new Menu("Change Difficulty");
        
        MenuItem subMenuEasy = new MenuItem("Easy");
        subMenuEasy.setOnAction(e -> {
            BattleSweeper.changeDifficulty("easy");
        });
        
        MenuItem subMenuMedium = new MenuItem("Medium");
        subMenuMedium.setOnAction(e -> {
            BattleSweeper.changeDifficulty("medium");
        });
        
        MenuItem subMenuHard = new MenuItem("Hard");
        subMenuHard.setOnAction(e -> {
            BattleSweeper.changeDifficulty("hard");
        });
        
        menuChangeDifficulty.getItems().addAll(subMenuEasy, subMenuMedium, subMenuHard);
        
        MenuItem menuInstructions = new MenuItem("Instructions");
        menuInstructions.setOnAction(e -> {
            InstructionsWindow instructions = new InstructionsWindow();
            instructions.display();
        });
        
        MenuItem menuExit = new MenuItem("Exit");
        menuExit.setOnAction(e -> {
            this.closeProgram();
        });
        
        menuActions.getItems()
                .addAll(menuChangeDifficulty, menuInstructions, menuExit);

        //Add all main selectors to the menu bar
        menuBar.getMenus()
                .addAll(menuActions);

    }

    private void closeProgram() {
        ConfirmBox cb = new ConfirmBox("Closing...", "Are you sure you want to exit?", 350, 100);
        boolean answer = cb.display();

        if (answer) {
            System.out.println("Closing Program");
            Stage window = (Stage) menuBar.getScene().getWindow();
            window.close();
        }

    }
}
