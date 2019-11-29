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
        public InfoPane() {
            System.out.println("Score: " + score);
            m_scoreLabel = new Text(" Score: 0");  //label score
            m_missedLabel = new Text("Miss: 0 ");  //label missed
            m_scoreLabel.setId("font-label");
            m_missedLabel.setId("font-label");

            setLeft(m_scoreLabel);
            setRight(m_missedLabel);
        }

        public void updateScore(int score) {
            m_scoreLabel.setText(" Score: " + score);
        }
        public void updateMiss(int miss) {
            m_missedLabel.setText("Miss: " + miss + " ");
        }

        private Text m_scoreLabel;
        private Text m_missedLabel;
    }

    private int score = 0;
    private int highScore = 0;
    private int miss = 0;

    private InfoPane m_infoPane;

    @Override // Override the start method in the Application class

    public void start(Stage stage) {
        stage.setMinHeight(820);
        stage.setMinWidth(680);
        stage.setMaxHeight(820);
        stage.setMaxWidth(680);

        BorderPane border = new BorderPane();

        border.setTop(addTopPane());
        border.setCenter(addGridPane());
        border.setBottom(addHSPane(0));

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

    private GridPane addGridPane() {
        Random rand = new Random();

        ArrayList<Button> listOfbutts = new ArrayList<>(100);
        GridPane gridPane = new GridPane();

        gridPane.setVgap(4);
        gridPane.setHgap(4);

        for (int i = 0; i < 100; i++){
            listOfbutts.add(new Button("Tile"));
            listOfbutts.get(i).setMaxSize(60, 60);
            listOfbutts.get(i).setMinSize(60, 60);
        }

        Image mole = new Image(getClass().getResourceAsStream("mole.png"));

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
        listOfbutts.get(moleSpot[0]).setVisible(true);
        listOfbutts.get(moleSpot[0]).setGraphic(new ImageView(mole));

        final double[] speed = {2};




//        EventHandler<MouseEvent> eventHandler1 = new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent e) {
//                listOfbutts.get(moleSpot[0]).setVisible(false);
//                System.out.println("Hit");
//                // unregister event from this button
//                //listOfbutts.get(moleSpot[0]).removeEventFilter(MouseEvent.MOUSE_CLICKED,this);  // or whatever
//                moleSpot[0] = rand.nextInt(100);
//                listOfbutts.get(moleSpot[0]).setVisible(true);
//                listOfbutts.get(moleSpot[0]).setGraphic(new ImageView(mole));
//                // register event for the next button
//                listOfbutts.get(moleSpot[0]).addEventFilter(MouseEvent.MOUSE_CLICKED, this);
//                score++;
//                m_infoPane.updateScore(score);
//                //speed *= .9;
//            }
//        };



        Timeline molePop = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
            listOfbutts.get(moleSpot[0]).setVisible(false);
            System.out.println("new mole");
            System.out.println("speed: " + Arrays.toString(speed));
            //listOfbutts.get(moleSpot[0]).addEventFilter(MouseEvent.MOUSE_CLICKED,eventHandler1 );
            moleSpot[0] = rand.nextInt(100);
            listOfbutts.get(moleSpot[0]).setVisible(true);
            listOfbutts.get(moleSpot[0]).setGraphic(new ImageView(mole));
            m_infoPane.updateMiss(miss);
        }));

        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                listOfbutts.get(moleSpot[0]).setVisible(false);
                System.out.println("Hit");
                // unregister event from this button
                //listOfbutts.get(moleSpot[0]).removeEventFilter(MouseEvent.MOUSE_CLICKED,this);  // or whatever
                moleSpot[0] = rand.nextInt(100);
                listOfbutts.get(moleSpot[0]).setVisible(true);
                listOfbutts.get(moleSpot[0]).setGraphic(new ImageView(mole));
                // register event for the next button
                //listOfbutts.get(moleSpot[0]).addEventFilter(MouseEvent.MOUSE_CLICKED, this);
                score++;
                m_infoPane.updateScore(score);
                //speed *= .9;
                molePop.setCycleCount(Timeline.INDEFINITE);
                molePop.play();
            }
        };
        listOfbutts.get(moleSpot[0]).addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);



        gridPane.setAlignment(Pos.BOTTOM_CENTER);
        return gridPane;

    }

    private StackPane addHSPane(int highScore){
        StackPane HSPane = new StackPane();
        Text banner = new Text(" High Score: " + highScore);  //label score
        banner.setId("font-label-title");
        HSPane.getChildren().addAll(banner);

        return HSPane;
    }

}

