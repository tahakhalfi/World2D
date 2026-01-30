package project;

import javafx.application.Application;

import javafx.stage.Stage;
import project.launchers.Client;

import java.io.IOException;

public class GNLApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Client.getManager().start(stage);
    }

}
