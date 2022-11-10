package view;

import controller.Controller;
import controller.Singleton;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import model.Coins;
import model.GoldPicker;
import model.Spot;
import sample.Main;

import java.util.ArrayList;

public class GraphicView {

    private BorderPane root;

    // start, stop, pause ,restart
    // threads comboBox
    Button start, stop;
    TextField threads_input;

    AnchorPane anchorPane;
    Rectangle[][] rectangles;

    Paint food_paint;

    Timeline timeline = new Timeline();

    VBox leftSide;

    Label timer_text;

    class GoldThread extends HBox{
        Paint paint;
        String name;
        StringProperty score;
        Button pause;

        boolean isPaused = false;

        GoldThread(String name, StringProperty _score){
            paint = new ImagePattern(Main.load_image(name.concat(".png")));
            score = new SimpleStringProperty();
            score.bind(_score);
            this.name = name;
            pause = Main.getButton("Pause","side_btn", e->{
               //TODO: toggle the button into pause or resume according to state of thread.
                if(!isPaused){
                    pause.setText("Resume");
                    //TODO: call method in controller to pause the thread.
                    for (GoldPicker goldPicker : Singleton.getInstance().controller.getGoldPickers()) {
                        if(goldPicker.getName().equals(name)) {
                            goldPicker.getThread().suspend();
                            break;
                        }
                    }
                }else{
                    pause.setText("Pause");
                    //TODO: call method in controller to resume the thread.
                    for (GoldPicker goldPicker : Singleton.getInstance().controller.getGoldPickers()) {
                        if(goldPicker.getName().equals(name)) {
                            goldPicker.getThread().resume();
                            break;
                        }
                    }
                }

                isPaused = !isPaused;
            });

            setAlignment(Pos.CENTER);
            setSpacing(20);
            setup();
        }

        Label score_label;
        void setup(){
            Rectangle rect = new Rectangle();
            rect.setWidth(Controller.cell_w);
            rect.setHeight(Controller.cell_h);
            rect.setFill(paint);

            Label name_label = new Label(name);
            score_label = new Label(score.get());

            name_label.setId("side_label");
            score_label.setId("side_label");

            score_label.setText("Score: 00");
            getChildren().addAll(rect,name_label,score_label,pause);
        }
        void updateScore(){
            score_label.setText(score.get());
        }
    }

    ArrayList<GoldThread> goldThreads = new ArrayList<>();

    int seconds = 0,milliSeconds = 0;
    public GraphicView(){

        food_paint = new ImagePattern(Main.load_image("coin.png"));

        initialize();
        anchorPane = new AnchorPane();
        root.setCenter(anchorPane);
        anchorPane.setPrefSize(Controller.width,Controller.height);
        initialize_background();
        Singleton.getInstance().controller.spawn_foods();
        updateBackground();

        KeyFrame keyFrame = new KeyFrame(Duration.millis(250),e->{
           updateBackground();
           if(milliSeconds >= 1000){
               seconds++;
               milliSeconds = 0;
           }
           milliSeconds += 250;

           timer_text.setText(
                   "Timer: "+((seconds/60) >= 9 ? (seconds/60)+":"+(seconds%60 >= 10 ? seconds%60 : "0"+seconds%60) : "0"+(seconds/60)+
                           ":"+(seconds%60 >= 10 ? seconds%60 : "0"+seconds%60))
           );
           System.out.println("Coins Left Targeted: "+Singleton.getInstance().controller.getTargeted_foods().size());
           System.out.println("Coins Left untarget: "+Singleton.getInstance().controller.getUntarget_food().size());
        });
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.getKeyFrames().add(keyFrame);

        leftSide = new VBox();
        leftSide.setAlignment(Pos.CENTER);
        leftSide.setSpacing(20);
        leftSide.setPrefSize(350,Controller.height);
        root.setLeft(leftSide);
    }

    boolean isStarted = false;

    void initialize(){
        root = new BorderPane();
        threads_input = new TextField();
        threads_input.setPromptText("00");

        start = Main.getButton("Start","btn",e->{
           //TODO start the threads.
            if(!isStarted) {
                if (threads_input.getText().isEmpty()) return;
                try {
                    int num = Integer.parseInt(threads_input.getText());
                    if (num <= 0) return;
                    Singleton.getInstance().controller.startThreads(num);
                    timeline.play();
                    for (GoldPicker goldPicker : Singleton.getInstance().controller.getGoldPickers()) {
                        GoldThread thread = new GoldThread(goldPicker.getName(), goldPicker.score_strProperty());
                        goldThreads.add(thread);
                        leftSide.getChildren().add(thread);
                    }
                } catch (NumberFormatException numberFormatException) {
                    numberFormatException.printStackTrace();
                }
                start.setText("Restart");
            }else{
                //TODO: restart
                timeline.pause();
                start.setText("Start");
                anchorPane.getChildren().clear();
                for (GoldPicker goldPicker : Singleton.getInstance().controller.getGoldPickers()) {
                    goldPicker.getThread().stop();
                }
                initialize_background();
                Singleton.getInstance().controller.getUntarget_food().clear();
                Singleton.getInstance().controller.getTargeted_foods().clear();
                Singleton.getInstance().controller.spawn_foods();
                milliSeconds = 0;
                seconds = 0;
                goldThreads.clear();
                Singleton.getInstance().controller.getGoldPickers().clear();
                Singleton.getInstance().controller.getPickers().clear();
                leftSide.getChildren().clear();
                updateBackground();
            }
            isStarted = !isStarted;
        });
        stop = Main.getButton("Stop","btn",e->{
            //TODO: stop all the threads
            for (GoldPicker goldPicker : Singleton.getInstance().controller.getGoldPickers()) {
                goldPicker.getThread().stop();
            }
        });

        timer_text = new Label("Timer: 00:00");

        HBox top = new HBox();
        top.setAlignment(Pos.CENTER);
        top.setSpacing(20);
        top.getChildren().addAll(threads_input,start,stop,timer_text);

        root.setTop(top);
    }

    void initialize_background(){
        Singleton.getInstance().controller.setup_background();
        rectangles = new Rectangle[Controller.col][Controller.row];
        for(int i = 0; i < Controller.col; i++){
            for(int j = 0; j<Controller.row; j++){
                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.WHITE);
                rectangle.setStroke(Color.GREY);
                Spot spot = Singleton.getInstance().controller.getSpots()[i][j];
                rectangle.setWidth(spot.getWidth());
                rectangle.setHeight(spot.getHeight());
                rectangle.setLayoutX(spot.getI()*spot.getWidth());
                rectangle.setLayoutY(spot.getJ()*spot.getHeight());
                anchorPane.getChildren().add(rectangle);
                rectangles[i][j] = rectangle;
            }
        }
    }

    void updateBackground(){
        //TODO: reset the background
        for(int i = 0; i< Controller.col; i++){
            for(int j = 0; j<Controller.row; j++){
                rectangles[i][j].setFill(Color.WHITE);
                rectangles[i][j].setStroke(Color.GREY);
                if(Singleton.getInstance().controller.getSpots()[i][j].isObstacle()) rectangles[i][j].setFill(Color.RED);
            }
        }

        for (Coins coins : Singleton.getInstance().controller.getTargeted_foods()) {
            rectangles[coins.getX()][coins.getY()].setFill(food_paint);
        }

        int index = 1;
        for (Controller.Pos picker : Singleton.getInstance().controller.getPickers()) {
            if(index >= 10) index = 1;
            rectangles[picker.getX()][picker.getY()].setFill(new ImagePattern(Main.load_image("goldPicker_"+(index++)+".png")));
        }

        for (GoldThread goldThread : goldThreads) {
            goldThread.updateScore();
        }
    }

    public BorderPane getRoot() {
        return root;
    }
}
