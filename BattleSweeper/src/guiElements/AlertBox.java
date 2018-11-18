package guiElements;

/**
 * An Alert Box that is useful for alerting the user of a small bit of
 * information.
 * 
 * @author Michael Linsinbigler
 */
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class AlertBox {

    private String title;
    private String message;
    private Alert.AlertType aType;

    /**
     *
     * @param title - Title of the alert box window
     * @param message - Message to be displayed
     * @param at - The AlertType. This will configure the visual icon shown.
     */
    public AlertBox(String title, String message, Alert.AlertType at) {
        this.title = title;
        this.message = message;
        this.aType = at;

    }

    /**
     * Displays the AlertBox to the user and freezes code execution until the
     * user acknowledges the message.
     */
    public void display() {

        Alert alert = new Alert(aType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        stage.showAndWait();
    }

}

