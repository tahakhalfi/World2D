package project.managers;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import project.GNLApplication;
import project.hierarchies.instances.essentials.Experience;

public class CManager {

    private Stage STAGE;
    private Scene SCENE;
    private Pane ROOT;

    private Experience EXPERIENCE;

    public void config() {

        javafx.application.Application.launch(
                GNLApplication.class,
                (String[]) null
        );

    }

    public void start(Stage stage) {

        Pane root = new Pane();

        Scene scene = new Scene(
                root,
                1260,
                780
        );

        stage.setTitle("World2D");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        STAGE = stage;
        SCENE = scene;
        ROOT = root;

    }

    public Stage getStage() {
        return STAGE;
    }

    public Scene getScene() {
        return SCENE;
    }

    public Pane getRoot() {
        return ROOT;
    }

    public void setExperience(Experience experience) {
        this.EXPERIENCE = experience;
    }

    public Experience getExperience() {
        return this.EXPERIENCE;
    }

}
