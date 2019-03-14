package app;

import bank.Transaction;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.util.List;

class HistoryPane extends VBox {

    private List<Transaction> history;
    private int ind;

    private Label indexLabel;
    private Label text;
    private HBox buttonsHolder;
    private Button prevButton;
    private Button nextButton;

    HistoryPane(List<Transaction> history) {
        this.history = history;
        if(history.size() > 0)
            ind = history.size()-1;
        else
            ind = 0;
        initialize();
    }

    private void initialize() {
        setSpacing(4);
        initializeLabels();
        setAlignment(Pos.CENTER);
        initializeButtons();
    }
    private void initializeLabels() {
        indexLabel = new Label("0/0");
        indexLabel.setAlignment(Pos.CENTER);
        indexLabel.setTextAlignment(TextAlignment.CENTER);
        indexLabel.setFont(Font.font("System",FontWeight.BOLD,24));
        setMargin(indexLabel, new Insets(24,24,0,24));
        getChildren().add(indexLabel);

        String initialText = "No history";
        if(history.size() > 0) {
            indexLabel.setText((history.size()-ind) + "/" + history.size());
            initialText = history.get(ind).toString();
        }
        text = new Label(initialText);
        text.setAlignment(Pos.TOP_LEFT);
        text.setTextAlignment(TextAlignment.LEFT);
        text.setWrapText(true);
        text.setFont(new Font(24));
        text.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
        getChildren().add(text);
        setVgrow(text, Priority.ALWAYS);
        setMargin(text, new Insets(20,24,20,24));
    }

    private void initializeButtons() {
        buttonsHolder = new HBox(5);
        buttonsHolder.setAlignment(Pos.CENTER);

        prevButton = new Button("<");
        initButton(prevButton);
        if(ind==0) prevButton.setDisable(true);

        prevButton.setOnAction(event -> {
            nextButton.setDisable(false);
            if(--ind == 0)
                prevButton.setDisable(true);
            indexLabel.setText((history.size()-ind) + "/" + history.size());
            text.setText(history.get(ind).toString());
        });


        nextButton = new Button(">");
        initButton(nextButton);
        nextButton.setDisable(true);

        nextButton.setOnAction(event -> {
            prevButton.setDisable(false);
            if(++ind == history.size()-1)
                nextButton.setDisable(true);
            indexLabel.setText((history.size()-ind) + "/" + history.size());
            text.setText(history.get(ind).toString());
        });

        getChildren().add(buttonsHolder);
        setMargin(buttonsHolder, new Insets(4,10,16,10));
    }
    private void initButton(Button button) {
        button.setFont(Font.font("System", FontWeight.EXTRA_BOLD, 24));
        button.setTextAlignment(TextAlignment.CENTER);
        button.setAlignment(Pos.CENTER);
        buttonsHolder.getChildren().add(button);
        HBox.setMargin(button, new Insets(8));
    }
}
