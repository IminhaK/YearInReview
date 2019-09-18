package net.bloop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.bloop.Riot.APIKey;
import no.stelar7.api.l4j8.basic.cache.impl.FileSystemCacheProvider;
import no.stelar7.api.l4j8.basic.calling.DataCall;
import no.stelar7.api.l4j8.impl.L4J8;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Stage stage;
    public static L4J8 api;

    @Override
    public void start(Stage stageIn) throws IOException {
        scene = new Scene(loadFXML("primary"));
        stage = stageIn;
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    static void setScene(Scene sceneIn){
        stage.setScene(sceneIn);
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        api = new L4J8(APIKey.apiCredentials);
        int hours = 24;
        int ttl = hours * 60 * 60 * 1000;
        DataCall.setCacheProvider(new FileSystemCacheProvider(ttl));

        launch();
    }
}