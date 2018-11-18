package guiElements;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * GUI component useful for gathering user confirmation
 * 
 * @author Michael Linsinbigler
 */
public class ConfirmBox {
    private final String title;
    private final String message;
    private final int width;
    private final int height;
    private boolean answer;

    public ConfirmBox(String title, String message, int width, int height) {
        this.title = title;
        this.message = message;
        this.width = width;
        this.height = height;
    }

    public boolean display() {

        Stage window = new Stage();

        // lock this window to force user action
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(width);
        window.setMinHeight(height);

        Label label1 = new Label("   " + message + "   ");

        // Create two buttons
        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");

        yesButton.setOnAction(e -> {
            answer = true;
            window.close();
        });

        yesButton.defaultButtonProperty().bind(yesButton.focusedProperty());

        noButton.setOnAction(e -> {
            answer = false;
            window.close();
        });
        
        noButton.defaultButtonProperty().bind(noButton.focusedProperty());

        HBox btnLayout = new HBox(10);
        btnLayout.getChildren().addAll(noButton, yesButton);
        btnLayout.setAlignment(Pos.CENTER);

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(5, 5, 5, 5));
        layout.getChildren().addAll(label1, btnLayout);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return answer;

    }
}
