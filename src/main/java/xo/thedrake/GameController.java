package xo.thedrake;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class GameController {
    @FXML
    public Canvas canvas;

    @FXML
    public void initialize() {
        GraphicsContext gc = canvas.getGraphicsContext2D();


        gc.setFill(Color.DARKRED);
        gc.fillRect(100, 100, 200, 150);

        gc.setStroke(Color.GOLD);
        gc.strokeRect(100, 100, 200, 150);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Georgia", 24));
        gc.fillText("Hello Dragon!", 120, 180);

        gc.drawImage(new Image("image.png"), 50, 50);
    }
}
