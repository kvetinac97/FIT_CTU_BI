package thedrake.ui;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.Window;
import thedrake.*;

import java.util.List;
import java.util.Optional;

public class TitleScreenController implements BoardViewContext {

    @FXML
    public void showDialog () {
        new Alert( Alert.AlertType.ERROR, "This function is not implemented.", ButtonType.OK ).show();
    }

    @FXML
    public void exit () {
        System.exit(0);
    }

    // Help function, create vertical box
    private VBox createVBox ( Pos pos ) {
        VBox box = new VBox();
        box.setPrefWidth(100);
        box.setPrefHeight(200);
        box.setAlignment(pos);
        return box;
    }

    // Help function, create main border pane
    private BorderPane createBorderPane () {
        BorderPane borderPane = new BorderPane();
        BoardView boardView = new BoardView( new StandardDrakeSetup().startState(new Board(TheDrakeApp.DIMENSION)), this );

        borderPane.setPrefWidth(1000);
        borderPane.setPrefHeight(600);

        borderPane.setLeft(createVBox(Pos.CENTER_LEFT));
        borderPane.setCenter(boardView);
        borderPane.setRight(createVBox(Pos.CENTER_RIGHT));

        BorderPane.setMargin(borderPane.getLeft(), new Insets(0, 0, 0, 25));
        BorderPane.setMargin(borderPane.getRight(), new Insets(0, 25, 0, 0));

        redrawStack(boardView);
        return borderPane;
    }

    @FXML
    public void startGame ( MouseEvent event ) {
        Stage stage = new Stage();
        stage.setTitle("TheDrake");
        stage.setScene(new Scene(createBorderPane()));
        stage.show(); // start new game

        if ( event != null )
            ( (Node) event.getSource() ).getScene().getWindow().hide(); // hide previous window
    }

    // Handles game end because of draw / win
    private void handleEndGame ( Window window, GameState state ) {
        boolean win = state.result() == GameResult.VICTORY;
        boolean blueWin = state.armyNotOnTurn().side() == PlayingSide.BLUE;

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game over!");
        alert.setHeaderText(
                ( win ? "Congratulations! " + ( blueWin ? "Blue" : "Orange" ) + " wins!" : "It's a draw!" )
        );
        alert.setContentText("Tap Close to return to title screen, tap Next to play again!");

        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(ButtonType.CLOSE, ButtonType.NEXT);

        Optional<ButtonType> result = alert.showAndWait();

        // Return tot title screen
        if ( result.isEmpty() || result.get() == ButtonType.CLOSE ) {
            Stage stage = new Stage();
            stage.setScene(window.getScene());
            window.hide();
            TheDrakeApp.setTitleScreen(stage, getClass().getResource("title.fxml"));
            return;
        }

        // Reset game
        window.hide();
        startGame(null);
    }

    // Generates specific part of children ( stack | captured )
    private void generateFromList ( VBox box, List<Troop> list, boolean isStack ) {
        // Title
        if ( !list.isEmpty() ) {
            Label label = new Label("\n" + ( isStack ? "Stack" : "Captured" ) + ":\n\n");
            label.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            label.setAlignment(Pos.TOP_CENTER);
            box.getChildren().add(label);
        }

        // Set text
        list.forEach( troop -> {
            Label label = new Label(troop.name());

            if ( isStack && troop.equals(list.get(0)) ) // first
                label.setTextFill(new Color(1, 0, 0, 1));

            label.setFont(Font.font("Arial", FontWeight.NORMAL, 15));
            box.getChildren().add(label);
        } );
    }

    // Generates VBox (stack | on turn | captured) children
    private void generateChildren ( VBox box, GameState state, PlayingSide side ) {
        // Remove previous children
        box.getChildren().clear();
        generateFromList(box, state.army(side).stack(), true);

        // On turn
        if ( state.armyOnTurn().side().equals(side) ) {
            Label label = new Label("\nOn turn\n\n");
            label.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            box.getChildren().add(label);
        }

        generateFromList(box, state.army(side).captured(), false);
    }

    @Override
    public void redrawStack ( BoardView view ) {
        BorderPane root = (BorderPane) view.getParent();
        GameState state = view.state();

        // Win or draw
        if ( state.result() != GameResult.IN_PLAY ) {
            handleEndGame ( view.getScene().getWindow(), state );
            return;
        }

        // Restore all move images
        for ( Node n : view.getChildren() ) {
            TileView tileView = (TileView) n;
            tileView.moveImage().setVisible( tileView.move() != null );
        }

        generateChildren ( (VBox) root.getLeft(), state, PlayingSide.BLUE );
        generateChildren ( (VBox) root.getRight(), state, PlayingSide.ORANGE );
    }

}
