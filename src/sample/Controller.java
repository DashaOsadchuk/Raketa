package sample;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import sample.Physics.Rigidbody;

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane backplane;

    @FXML
    private Button startBtn;

    @FXML
    private ImageView previevImage;

    @FXML
    private Text previevText;

    @FXML
    private Canvas canvas;

    Rocket player;
    private boolean useThrust = false, isRunning = false;
    double planetH;
    int counter = 0;
    Timeline timeline;

    @FXML
    void initialize() {
        assert backplane != null : "fx:id=\"backplane\" was not injected: check your FXML file 'sample.fxml'.";
        assert startBtn != null : "fx:id=\"startBtn\" was not injected: check your FXML file 'sample.fxml'.";
        assert previevImage != null : "fx:id=\"previevImage\" was not injected: check your FXML file 'sample.fxml'.";
        assert previevText != null : "fx:id=\"previevText\" was not injected: check your FXML file 'sample.fxml'.";
        assert canvas != null : "fx:id=\"canvas\" was not injected: check your FXML file 'sample.fxml'.";
        startBtn.setOnAction(event ->{
            if (!isRunning) run();
        });
        startBtn.setOnMouseClicked(mouseEvent -> {
            if (isRunning) {
                useThrust = !useThrust;
            }
        });
    }

    private void run() {
        isRunning = true;
        previevImage.setVisible(false);
        previevText.setVisible(false);
        timeline = new Timeline(new KeyFrame(Duration.millis(20), event -> Update()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        player = new Rocket(5000, 20000, -500, 450, 2500);
        startBtn.setText("Engines: off");
        startBtn.setFont(new Font(19));
        useThrust = false;
    }

    private void Update() {
        Rigidbody rb = player.getRB();
        double w = canvas.getWidth(), h = canvas.getHeight();
        planetH = Math.pow(20000-rb.y(), 1.5)/2900; // смещение отрисовки планеты относительно высоты игрока
        Image rocket;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0,0,w,h);
        if (useThrust && player.GetFuel() > 0) {
            player.thrust();
            rocket = new Image(getClass().getResource("assets/Rocket_f.png").toString());
            startBtn.setText("Engines: On");
        } else {
            rocket = new Image(getClass().getResource("assets/Rocket_n.png").toString());
            startBtn.setText("Engines: Off");
        }
        rb.addForce(-10000000/(rb.y()*rb.y()+1000000)/50, false);
        rb.Update();
        gc.setFill(new Color(0.5,0.8,0,1));
        gc.fillOval(w/4-planetH, h-planetH/2+5,w/2+planetH*2, 10+planetH);
        player.draw(gc, rocket, w ,h);
        gc.setLineWidth(1);
        gc.setFont(new Font(20));
        gc.strokeText("Fuel: "+round(player.GetFuel(),1),5,20,200);
        gc.strokeText("Position: "+round(rb.y(), 1),140,20,200);
        gc.strokeText("Speed: "+round(rb.Speed(), 1),330,20,200);
        gc.strokeText("Condition to land properly: speed < 10",50,50,400);
        if (rb.y() <= 0) {
            gc.clearRect(0,0,w,h);
            isRunning = false;
            previevImage.setVisible(true);
            previevText.setVisible(true);
            if (rb.Speed() <= -10) {
                previevText.setText("You lose!");
                previevText.setFont(new Font(35));
                previevText.setFill(new Color(0,0,0,1));
            } else {
                previevText.setText("You win!");
                previevText.setFont(new Font(55));
                previevText.setFill(new Color(0.3,0.6,1,1));
            }
            timeline.stop();
            startBtn.setFont(new Font(30));
            startBtn.setText("Start");
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}