import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Main class of the <code>ControllerClient</code>.
 *
 * @author Gao Manlin
 * @since 1.8
 *
 */
public class Main extends Application {
    /**
     * A stage.
     */
    private Stage primaryStage;
    /**
     * Object of the ControllerClient used to get main.
     */
    private ControllerClient controllerClient;
    /**
     * Main method.
     *
     * @param args
     *        Used to launch the command.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Start the stage.
     *
     * @param primaryStage
     *        A primary stage used to set up the stage.
     *
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage=primaryStage;
        this.primaryStage.setTitle("Client window");
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass()
                    .getResource("Window.fxml"));
            AnchorPane pane = (AnchorPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(pane);
            primaryStage.setScene(scene);

            scene.getStylesheets().add(
                    getClass().getResource("window.css")
                            .toExternalForm());

            // Give the controllerClient access to the main app.
            controllerClient = loader.getController();
            controllerClient.setMain(this);

            primaryStage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Return primary stage of the main.
     *
     * @return Primary stage of the main.
     *
     */
    public Stage getPrimaryStage(){
        return primaryStage;
    }
}
