import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.time.*;
import java.util.Timer;
import java.util.*;

public class Main extends Application {

    private class InfoPane extends BorderPane {
        private Text m_scoreLabel;
        private Text m_missedLabel;
        private Button m_reset;
        private int moleSpot;
        public long startTime = 0;
        public long stopTime = 0;


        public InfoPane() {
            System.out.println("Score: " + score);
            m_scoreLabel = new Text(" Score: 0");  //label score
            m_missedLabel = new Text("Miss: 0 ");  //label missed
            m_scoreLabel.setId("font-label");
            m_missedLabel.setId("font-label");
            m_reset = new Button("Reset");
            m_reset.setMaxSize(70,30);
            m_reset.setId("button-reset");

            setLeft(m_scoreLabel);
            setCenter(m_reset);
            setRight(m_missedLabel);

            Random rand = new Random();
            final int[] moleSpot;

        }

        public void updateScore(int score) {
            m_scoreLabel.setText(" Score: " + score);
        }
        public void updateMiss(int miss) {
            m_missedLabel.setText("Miss: " + miss + " ");
        }
        public void updateMoleSpot(int spot) {
            moleSpot = spot;
        }
    }

    private class HSPane extends BorderPane {
        private Text m_HSLabel;
        public HSPane() {
            System.out.println("High Score: " + highScore);
            m_HSLabel = new Text(" High Score: 0");  //label highscore
            m_HSLabel.setId("font-label");

            setLeft(m_HSLabel);
        }

        public void updateHighScore(int highScore) {
            m_HSLabel.setText(" High Score: " + highScore);
        }
    }

    private int score = 0;
    private int highScore = 0;
    private int miss = 0;

    private InfoPane m_infoPane;
    private HSPane m_HSPane;

    @Override // Override the start method in the Application class
    public void start(Stage stage) {
       // read high score file and update highscore(0 if none)
        m_HSPane = new HSPane();
        highScore = readHighScore(0);
        m_HSPane.updateHighScore(highScore);

        stage.setMinHeight(820);
        stage.setMinWidth(680);
        stage.setMaxHeight(820);
        stage.setMaxWidth(680);

        BorderPane border = new BorderPane();


        border.setTop(addTopPane());
        border.setCenter(addGridPane());

        border.setBottom(m_HSPane);

        //Creating a scene object
        Scene scene = new Scene(border);
        scene.getStylesheets().add("./styles.css");

        //Setting title to the Stage
        stage.setTitle("Whack-o-Mole");

        //Adding scene to the stage
        stage.setScene(scene);

        //Displaying the contents of the stage
        stage.show();
    }


    public static void main(String[] args) {
        Application.launch(args);
    }

    private BorderPane addTopPane(){
        BorderPane topPane = new BorderPane();
        topPane.setTop(addBannerPane());
        //InfoPane m_infoPane = new InfoPane();
        m_infoPane = addInfoPane();
        topPane.setCenter(m_infoPane);

        return topPane;
    }

    private StackPane addBannerPane(){
        StackPane bannerPane = new StackPane();
        Text banner = new Text("Whack-a-Mole ");  //label score
        banner.setId("font-label-title");
        bannerPane.getChildren().addAll(banner);

        return bannerPane;
    }

    private InfoPane addInfoPane(){
        InfoPane infoPane = new InfoPane();
        return infoPane;
    }

    private HSPane addHSPane(){
        HSPane HSPane = new HSPane();
        return HSPane;
    }

    private GridPane addGridPane() {
        Random rand = new Random();

        ArrayList<Button> listOfButts = new ArrayList<>(100);
        GridPane gridPane = new GridPane();

        gridPane.setVgap(4);
        gridPane.setHgap(4);

        for (int i = 0; i < 100; i++){
            listOfButts.add(new Button());
            listOfButts.get(i).setId("button-tile");
            listOfButts.get(i).setMaxSize(60, 60);
            listOfButts.get(i).setMinSize(60, 60);
        }

        Image mole = new Image(getClass().getResourceAsStream("mole.png"));
        Image hitMole = new Image(getClass().getResourceAsStream("hitMole.png"));

        int spot = 0;
        for (int j = 0; j < 10; j++){
            for (int k = 0; k < 10; k++){
                gridPane.add(listOfButts.get(spot),j,k);
                listOfButts.get(spot).setVisible(false);
                spot++;
            }
        }

        //final int[] moleSpot = {rand.nextInt(100)};
        listOfButts.get(m_infoPane.moleSpot).setVisible(true);
        listOfButts.get(m_infoPane.moleSpot).setGraphic(new ImageView(mole));


        Timeline molePop = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
            //listOfButts.get(m_infoPane.moleSpot).addEventFilter(MouseEvent.MOUSE_CLICKED,eventHitMole);
            listOfButts.get(m_infoPane.moleSpot).setVisible(false);
            System.out.println("new mole");m_infoPane.moleSpot = rand.nextInt(100);
            listOfButts.get(m_infoPane.moleSpot).setVisible(true);
            listOfButts.get(m_infoPane.moleSpot).setGraphic(new ImageView(mole));
            miss++;
            m_infoPane.updateMiss(miss);
        }));

        EventHandler<MouseEvent> eventHitMole = new EventHandler<>() {
            @Override
            public void handle(MouseEvent e) {
                molePop.stop();
//                pop(rand, listOfButts, mole, this);
                m_infoPane.stopTime = System.currentTimeMillis();
                System.out.println("Elapsed time was " + (m_infoPane.stopTime - m_infoPane.startTime) + " miliseconds.");
                //molePop.setCycleCount(Timeline.INDEFINITE);
                //molePop.play();
                listOfButts.get(m_infoPane.moleSpot).removeEventFilter(MouseEvent.MOUSE_CLICKED, this);
                listOfButts.get(m_infoPane.moleSpot).setGraphic(new ImageView(hitMole));
                System.out.println("Hit");
                m_infoPane.moleSpot = rand.nextInt(100);
                listOfButts.get(m_infoPane.moleSpot).setVisible(true);
                listOfButts.get(m_infoPane.moleSpot).setGraphic(new ImageView(mole));
                // register event for the next button
                listOfButts.get(m_infoPane.moleSpot).addEventFilter(MouseEvent.MOUSE_CLICKED, this);
                score++;
                m_infoPane.updateScore(score);
                if (score > highScore) {
                    highScore = score;
                    m_HSPane.updateHighScore(score);
                    try {
                        writeHighScore(score);   // writes highscore to file
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                m_infoPane.startTime = System.currentTimeMillis();
                molePop.setCycleCount(Timeline.INDEFINITE);
                molePop.play();
            }

        };


        EventHandler<MouseEvent> eventResetScore = e -> {
            //clears all the hit moles
            listOfButts.get(m_infoPane.moleSpot).addEventFilter(MouseEvent.MOUSE_CLICKED,eventHitMole);
            for (int i = 0; i < 100; i++) {
                listOfButts.get(i).setVisible(false);
            }
            //m_infoPane.moleSpot = rand.nextInt(100);  this works but new random mole isnt clickable, i think may also be the reason the timetable moles arent clickable
            listOfButts.get(m_infoPane.moleSpot).setVisible(true); // sets first mole
            listOfButts.get(m_infoPane.moleSpot).setGraphic(new ImageView(mole)); //shows first clickable mole
            miss = 0; // resets miss
            m_infoPane.updateMiss(miss);// resets score
            score = 0; // resets score
            m_infoPane.updateScore(score);// resets score
            molePop.stop();
        };





        listOfButts.get(m_infoPane.moleSpot).addEventFilter(MouseEvent.MOUSE_CLICKED,eventHitMole);
        m_infoPane.m_reset.addEventFilter(MouseEvent.MOUSE_CLICKED,eventResetScore);


        gridPane.setAlignment(Pos.BOTTOM_CENTER);
        return gridPane;

    }

//    private void pop(Random rand, ArrayList<Button> listOfButts, Image mole, EventHandler<MouseEvent> eventHitMole) {
//        Timeline molePop = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
//            listOfButts.get(m_infoPane.moleSpot).addEventFilter(MouseEvent.MOUSE_CLICKED,eventHitMole);
//            listOfButts.get(m_infoPane.moleSpot).setVisible(false);
//            System.out.println("new mole");
//            m_infoPane.moleSpot = rand.nextInt(100);
//            listOfButts.get(m_infoPane.moleSpot).setVisible(true);
//            listOfButts.get(m_infoPane.moleSpot).setGraphic(new ImageView(mole));
//            miss++;
//            m_infoPane.updateMiss(miss);
//        }));
//    }

    public static void writeHighScore(int highScore) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream("highScore.txt"))) {
            dos.writeInt(highScore);
        }
    }


    public static int readHighScore(int valueIfNotFound) {
        if (!new File("highScore.txt").canRead()) return valueIfNotFound;
        try (DataInputStream dis = new DataInputStream(new FileInputStream("highScore.txt"))) {
            return dis.readInt();
        } catch (IOException ignored) {
            return valueIfNotFound;
        }
    }

}