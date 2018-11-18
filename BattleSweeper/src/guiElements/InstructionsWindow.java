package guiElements;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * Window to display game instructions
 *
 * @author Michael Linsinbigler
 */
public class InstructionsWindow {

    Stage primaryStage;

    public void display() {

        primaryStage = new Stage();
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.setTitle("Game Instructions");

        primaryStage.setHeight(300);
        primaryStage.setWidth(350);
        Group root = new Group();
        Scene scene = new Scene(root, Color.WHITE);

        primaryStage.setOnCloseRequest(e -> {
            e.consume(); //Prevent default action of close
            closeWindow();
        });

        BorderPane mainPane = new BorderPane();
        root.getChildren().add(mainPane);

        ScrollPane content = new ScrollPane();
        Text instructions = new Text("");
        instructions.setFont(Font.font("Monospaced"));

        StringBuilder errorContent = new StringBuilder();

        errorContent.append("Instructions:").append(System.lineSeparator());
        errorContent.append("Click on a wave tile to reveal what is hidden underneath. A revealed tile can contain either a Ship, Hint, or Open Ocean. A Hint is a special arrow that will reveal the direction to the next closest undiscovered Ship.").append(System.lineSeparator()).append(System.lineSeparator());

        errorContent.append("GOAL:").append(System.lineSeparator());
        errorContent.append("Find the 10 hidden ships before running out of moves.").append(System.lineSeparator()).append(System.lineSeparator());

        instructions.setText(errorContent.toString());

        instructions.wrappingWidthProperty().bind(primaryStage.widthProperty().subtract(40));
//        content.prefWidthProperty().bind(primaryStage.widthProperty().subtract(80));

        content.setContent(instructions);
        content.setPadding(new Insets(10, 10, 10, 10));
        content.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
//        content.prefHeightProperty().bind(primaryStage.heightProperty().subtract(75));

        ContextMenu contextMenu = new ContextMenu();
        MenuItem copyAction = new MenuItem("Copy");

        contextMenu.getItems().add(copyAction);

        copyAction.setOnAction(e -> {

            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent CPcontent = new ClipboardContent();
            CPcontent.putString(instructions.getText());
            clipboard.setContent(CPcontent);

        });

        content.setContextMenu(contextMenu);

        VBox vb = new VBox();
        vb.getChildren().addAll(content);
        vb.setAlignment(Pos.CENTER);

        //Different padding on elements due to a rendering bug on Mac vs Windows
        if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0) {
            vb.setPadding(new Insets(0, 0, 20, 2));
        } else {
            vb.setPadding(new Insets(0, 0, 20, 10));
        }

        mainPane.setTop(vb);

        final Button okButton = new Button("Ok");
        okButton.setOnAction(e -> {
            closeWindow();
        });
        final HBox hb2 = new HBox();
        hb2.setSpacing(5);
        hb2.setAlignment(Pos.CENTER);
        hb2.getChildren().addAll(okButton);
        mainPane.setBottom(hb2);

        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private void closeWindow() {
        primaryStage.close();
    }

}
