package battlesweeper;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * Pop up window to obtain the difficulty selection.
 * 
 * @author Michael Linsinbigler
 */
public class DifficultySelection {

    private String answer;
    Stage window;

    public DifficultySelection() {
        answer = "";
    }

    public String display() {
        
        //init the window and layout
        window = new Stage();
        VBox layout = new VBox(5);
        layout.setAlignment(Pos.CENTER);
        
        Scene scene = new Scene(layout, 350, 150);
        
        window.setTitle("BattleSweeper - Select Difficulty");

        //Construct window components
        Button btnEasy = new Button("Easy");
        btnEasy.setOnAction(e -> {
            answer = "easy";
            close();
        });

        Button btnMedium = new Button("Medium");
        btnMedium.setOnAction(e -> {
            answer = "medium";
            close();
        });

        Button btnHard = new Button("Hard");
        btnHard.setOnAction(e -> {
            answer = "hard";
            close();
        });

        Label lblSpacing = new Label("");

        //Add components to layout
        layout.getChildren().addAll(btnEasy, btnMedium, btnHard, lblSpacing);

        window.setScene(scene);
        window.showAndWait();
        return answer;
    }
    
    private void close(){
        window.close();
    }

}
