package sample;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;


public class Main extends Application {
    int width = 25;
    int height = 25;
    Astaralgorithm alg = new Astaralgorithm(width, height);
    Button[][] buttons;
    boolean startSet = false;
    boolean targetSet;
    int startX;
    int startY;
    int targetX;
    int targetY;

    GridPane gridPane;

    File theFile;

    public void newMenu(Stage secondaryStage) {
        Stage primaryStage = new Stage();
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(20, 10, 20, 10));
        TextField definedWidth = new TextField();
        definedWidth.setPromptText("Width");
        TextField definedHeight = new TextField();
        definedHeight.setPromptText("Height");
        Button button = new Button("Go!");

        button.setOnAction(event -> {
                    reset();
                    this.width = Integer.valueOf(definedWidth.getText());
                    this.height = Integer.valueOf(definedHeight.getText());
                    alg = new Astaralgorithm(width, height);
                    try {
                        start(primaryStage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    secondaryStage.close();
                    theFile = null;
                }
        );

        vBox.getChildren().addAll(definedWidth, definedHeight, button);

        Scene scene = new Scene(vBox, 150, 150);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void saveAsFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Map File", "*.map"));
        setUserPath(fileChooser);
        theFile = fileChooser.showSaveDialog(stage);
    }

    public void openFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Map File", "*.map"));
        setUserPath(fileChooser);
        theFile = fileChooser.showOpenDialog(stage);
    }

    private void setUserPath(FileChooser fileChooser) {
        String userDirectoryString = System.getProperty("user.home");
        File userDirectory = new File(userDirectoryString + "/documents");
        if(!userDirectory.canRead()) {
            userDirectory = new File("c:/");
        }
        fileChooser.setInitialDirectory(userDirectory);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        BorderPane borderPane = new BorderPane();
        MenuBar menuBar = new MenuBar();
        Menu file = new Menu("File");
        Menu tool = new Menu("Tool");
        MenuItem newTiles = new MenuItem("New");
        MenuItem save = new MenuItem("Save");
        MenuItem saveAs = new MenuItem("Save As");
        MenuItem load = new MenuItem("Load");
        MenuItem setAllWalkable = new MenuItem("Set all Tiles to walkable");
        MenuItem go = new MenuItem("Calculate Path");
        MenuItem reset = new MenuItem("Reset");

        Slider  speed = new Slider();

        VBox vBox = new VBox();
        HBox hBox = new HBox();
        gridPane = new GridPane();

        gridPane.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        gridPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        gridPane.setPadding(new Insets(10));

        HBox.setHgrow(borderPane, Priority.ALWAYS);
        VBox.setVgrow(borderPane, Priority.ALWAYS);

        HBox.setHgrow(hBox, Priority.ALWAYS);
        VBox.setVgrow(hBox, Priority.ALWAYS);

        HBox.setHgrow(vBox, Priority.ALWAYS);
        VBox.setVgrow(vBox, Priority.ALWAYS);

        HBox.setHgrow(gridPane, Priority.ALWAYS);
        VBox.setVgrow(gridPane, Priority.ALWAYS);

        buttons = new Button[width][height];

        initializeButtons();

        updateTiles();

        newTiles.setOnAction(event ->
                newMenu(primaryStage)
        );

        save.setOnAction(event -> {
                    if (theFile == null) {
                        saveAsFile(new Stage());
                        FileHandling.save(theFile, alg);
                    } else {
                        FileHandling.save(theFile, alg);
                    }
                }
        );

        saveAs.setOnAction(event -> {
                    saveAsFile(new Stage());
                    FileHandling.save(theFile, alg);
                }
        );

        load.setOnAction(event -> {
                    openFile(new Stage());
                    alg = FileHandling.load(theFile);
                    this.width = alg.width;
                    this.height = alg.height;
                    for (int i = 0; i < width; i++) {
                        for (int j = 0; j < height; j++) {
                            if (alg.tile[i][j].isStart()) {
                                startX = i;
                                startY = j;
                                startSet = true;
                            }
                            if (alg.tile[i][j].isTarget()) {
                                targetX = i;
                                targetY = j;
                                targetSet = true;
                            }
                        }
                    }
                    primaryStage.close();
                    try {
                        start(primaryStage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );

        go.setOnAction(event -> {
                    alg.resetPath();
                    alg.setTarget(targetX, targetY);
                    alg.setDistances(targetX, targetY);
                    alg.cloning(startX, startY, targetX, targetY, buttons, (long) (1000 - 10 * Math.sqrt(speed.getValue())));
                    Thread thread = new Thread(new WaitForTarget(this));
                    thread.start();
                }
        );

        reset.setOnAction(event ->
                reset()
        );

        setAllWalkable.setOnAction(event ->
                setAllToWalkable()
        );

        file.getItems().addAll(newTiles, new SeparatorMenuItem(), save, saveAs, new SeparatorMenuItem(), load);
        tool.getItems().addAll(setAllWalkable, go, reset);
        menuBar.getMenus().addAll(file, tool);
        hBox.getChildren().addAll(gridPane);
        vBox.getChildren().addAll(hBox);
        borderPane.setTop(menuBar);
        borderPane.setCenter(vBox);

        speed.setMin(0);
        speed.setMax(10000);
        speed.setValue(5000);

        vBox.getChildren().add(speed);

        primaryStage.setTitle("Path Finding Tool");
        Scene scene = new Scene(borderPane, 1024, 768);

        speed.setPadding(new Insets(0, scene.getWidth()/3,5,scene.getWidth()/3));

        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                speed.setPadding(new Insets(0, scene.getWidth()/3,5,scene.getWidth()/3));
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    void markPath() {
        alg.getPath(startX, startY);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (alg.tile[i][j].isBelongsToPath() && !alg.tile[i][j].isStart() && !alg.tile[i][j].isTarget()) {
                    buttons[i][j].setStyle("-fx-base: #ff0000");
                }
            }
        }
    }

    private void setAllToWalkable() {
        for (int i = 0; i < alg.width; i++) {
            for (int j = 0; j < alg.height; j++) {
                alg.tile[i][j].setWalkable(true);
            }
        }
        updateTiles();
    }

    private void initializeButtons() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int x = i;
                int y = j;

                buttons[i][j] = new Button();
                HBox.setHgrow(buttons[i][j], Priority.ALWAYS);
                VBox.setVgrow(buttons[i][j], Priority.ALWAYS);
                gridPane.setHgrow(buttons[i][j], Priority.ALWAYS);
                gridPane.setVgrow(buttons[i][j], Priority.ALWAYS);
                buttons[i][j].setMinSize(1, 1);
                buttons[i][j].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                gridPane.add(buttons[i][j], i, j);

                buttons[i][j].setOnMouseClicked(event -> {
                            if (event.getButton() == MouseButton.PRIMARY) {
                                removeStartOrTarget(x, y);
                                setToWalkable(x, y);
                                alg.resetPath();
                                updateTiles();
                            } else if (event.getButton() == MouseButton.SECONDARY) {
                                setToStartOrTarget(x, y);
                                alg.resetPath();
                                updateTiles();
                            }
                        }
                );
            }
        }
    }

    private void removeStartOrTarget(int x, int y) {
        if (alg.tile[x][y].isStart()) {
            startSet = false;
            alg.tile[x][y].setStart(false);
        }
        if (alg.tile[x][y].isTarget()) {
            targetSet = false;
            alg.tile[x][y].setTarget(false);
        }
    }

    private void setToStartOrTarget(int x, int y) {
        if (alg.tile[x][y].walkable) {
            if (startSet && alg.tile[x][y].isTarget()) {
                targetSet = false;
                alg.tile[x][y].setTarget(false);
            } else if (startSet && targetSet && !alg.tile[x][y].isTarget()) {
                alg.tile[targetX][targetY].setTarget(false);
                alg.tile[x][y].setTarget(true);
                targetX = x;
                targetY = y;
            } else if (startSet && !targetSet && !alg.tile[x][y].isStart()) {
                targetSet = true;
                alg.tile[x][y].setTarget(true);
                targetX = x;
                targetY = y;
            } else if (startSet && alg.tile[x][y].isStart()) {
                startSet = false;
                alg.tile[x][y].setStart(false);
            } else if (!startSet) {
                startSet = true;
                alg.tile[x][y].setStart(true);
                startX = x;
                startY = y;
            }
            updateTiles();
        }
    }

    public void updateTiles() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (alg.tile[i][j].walkable) {
                    buttons[i][j].setStyle("-fx-base: #737373");
                    if (alg.tile[i][j].isStart()) {
                        buttons[i][j].setStyle("-fx-base: #ffcc66");
                    } else if (alg.tile[i][j].isTarget()) {
                        buttons[i][j].setStyle("-fx-base: #ff8c66");
                    }else if (alg.tile[i][j].isBelongsToPath()) {
                        buttons[i][j].setStyle("-fx-base: #ff0000");
                    }
                } else if (!alg.tile[i][j].walkable){
                    buttons[i][j].setStyle("-fx-base: #333333");
                }
            }
        }
        Cloning.foundTarget = false;
    }

    public void updateTile(int i, int j) {
        if (alg.tile[i][j].walkable) {
            buttons[i][j].setStyle("-fx-base: #737373");
            if (alg.tile[i][j].isStart()) {
                buttons[i][j].setStyle("-fx-base: #ffcc66");
            } else if (alg.tile[i][j].isTarget()) {
                buttons[i][j].setStyle("-fx-base: #ff8c66");
            }else if (alg.tile[i][j].isBelongsToPath()) {
                buttons[i][j].setStyle("-fx-base: #ff0000");
            }
        } else if (!alg.tile[i][j].walkable){
            buttons[i][j].setStyle("-fx-base: #333333");
        }
    }

    public void setToWalkable(int x, int y) {
        alg.tile[x][y].setWalkable(!alg.tile[x][y].walkable);
        updateTile(x, y);
    }

    public void reset() {
        buttons = new Button[width][height];
        initializeButtons();
        alg.reset();
        startSet = false;
        targetSet = false;
        startX = 0;
        startY = 0;
        targetX = 0;
        targetY = 0;
        updateTiles();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
