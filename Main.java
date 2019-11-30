import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
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

import java.util.Arrays;
import java.util.Random;

import java.util.ArrayList;

public class Main extends Application {

    private class InfoPane extends BorderPane {
        private Text m_scoreLabel;
        private Text m_missedLabel;
        private Button m_reset;
        int moleSpot;


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

        public int getMoleSpot() {
            return moleSpot;
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
        stage.setMinHeight(820);
        stage.setMinWidth(680);
        stage.setMaxHeight(820);
        stage.setMaxWidth(680);

        BorderPane border = new BorderPane();
        //HSPane HSPane = new HSPane();

        border.setTop(addTopPane());
        border.setCenter(addGridPane());
        m_HSPane = new HSPane();
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

        ArrayList<Button> listOfbutts = new ArrayList<>(100);
        GridPane gridPane = new GridPane();

        gridPane.setVgap(4);
        gridPane.setHgap(4);

        for (int i = 0; i < 100; i++){
            listOfbutts.add(new Button());
            listOfbutts.get(i).setId("button-tile");
            listOfbutts.get(i).setMaxSize(60, 60);
            listOfbutts.get(i).setMinSize(60, 60);
        }

        Image mole = new Image(getClass().getResourceAsStream("mole.png"));
        Image hitMole = new Image(getClass().getResourceAsStream("hitMole.png"));

        for (int i = 0; i < 10;) {
            for (int j = 0; j < 10; j++){
                for (int k = 0; k < 10; k++){
                    gridPane.add(listOfbutts.get(i),j,k);
                    listOfbutts.get(i).setVisible(false);
                    i++;
                }
            }
        }

        final int[] moleSpot = {rand.nextInt(100)};
        listOfbutts.get(m_infoPane.getMoleSpot()).setVisible(true);
        listOfbutts.get(m_infoPane.getMoleSpot()).setGraphic(new ImageView(mole));

        final double[] speed = {2};
        Timeline molePop = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
            System.out.println(m_infoPane.getMoleSpot());
            listOfbutts.get(m_infoPane.getMoleSpot()).setVisible(false);
            System.out.println("new mole");
            m_infoPane.updateMoleSpot( rand.nextInt(100));

            listOfbutts.get(m_infoPane.getMoleSpot()).setVisible(true);
            listOfbutts.get(m_infoPane.getMoleSpot()).setGraphic(new ImageView(mole));
            miss++;
            m_infoPane.updateMiss(miss);
        }));

        EventHandler<MouseEvent> eventHitMole = new EventHandler<>() {
            @Override
            public void handle(MouseEvent e) {
                System.out.println(m_infoPane.getMoleSpot());
                //molePop.play();
                //molePop.setCycleCount(Timeline.INDEFINITE);
                listOfbutts.get(m_infoPane.getMoleSpot()).setVisible(true);
                listOfbutts.get(m_infoPane.getMoleSpot()).setGraphic(new ImageView(hitMole));
                System.out.println("Hit");
                m_infoPane.updateMoleSpot(rand.nextInt(100));
                listOfbutts.get(m_infoPane.getMoleSpot()).setVisible(true);
                listOfbutts.get(m_infoPane.getMoleSpot()).setGraphic(new ImageView(mole));
                // register event for the next button
                listOfbutts.get(m_infoPane.getMoleSpot()).addEventFilter(MouseEvent.MOUSE_CLICKED, this);
                score++;
                m_infoPane.updateScore(score);
                if (score > highScore) {
                    highScore = score;
                    m_HSPane.updateHighScore(score);
                }
            }
        };

        EventHandler<MouseEvent> eventResetScore = e -> {
            //clears all the hit moles
            for (int i = 0; i < 100; i++) {
                listOfbutts.get(i).setVisible(false);
            }
            //m_infoPane.updateMoleSpot(rand.nextInt(100));  this works but new random mole isnt clickable, i think may also be the reason the timetable moles arent clickable
            listOfbutts.get(m_infoPane.getMoleSpot()).setVisible(true); // sets first mole
            listOfbutts.get(m_infoPane.getMoleSpot()).setGraphic(new ImageView(mole)); //shows first clickable mole
            score = 0; // resets score
            m_infoPane.updateScore(score);
            //molePop.stop();
        };


        m_infoPane.m_reset.addEventFilter(MouseEvent.MOUSE_CLICKED,eventResetScore);
        molePop.setCycleCount(Timeline.INDEFINITE);
        molePop.play();
        listOfbutts.get(m_infoPane.getMoleSpot()).addEventFilter(MouseEvent.MOUSE_CLICKED,eventHitMole);      //THIS LINE(specifically molespot) IS NOT GETTING UPDATED WHEN MOLE MOVES!!!!!!!!!!!!!!

        gridPane.setAlignment(Pos.BOTTOM_CENTER);
        return gridPane;

    }

}

