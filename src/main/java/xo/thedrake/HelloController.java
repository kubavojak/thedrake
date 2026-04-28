package xo.thedrake;

import javafx.application.Platform;
import javafx.event.ActionEvent;  // ← javafx, ne java.awt!
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloController {

    @FXML
    public void onPassAndPlay(ActionEvent event) {
        startNewWindow(event);
    }

    @FXML
    protected void onSinglePlayer() {}

    @FXML
    protected void onOnlineGame() {}

    @FXML
    protected void onSetting() {}

    @FXML
    protected void onExit() {
        Platform.exit();
    }

    private void startNewWindow(ActionEvent event) {
        try {
            Button btn = (Button) event.getSource();
            Stage currentStage = (Stage) btn.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("game-view.fxml"));
            Stage newStage = new Stage();
            newStage.setScene(new Scene(loader.load()));
            newStage.setMinWidth(1280);
            newStage.setMinHeight(720);
            newStage.setWidth(currentStage.getWidth());
            newStage.setHeight(currentStage.getHeight());
            newStage.setX(currentStage.getX());
            newStage.setY(currentStage.getY());
            newStage.setMaximized(currentStage.isMaximized());

            currentStage.close();
            newStage.show();



            currentStage.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}