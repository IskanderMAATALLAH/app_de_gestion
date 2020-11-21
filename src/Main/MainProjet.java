package Main;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;



public class MainProjet extends Application 
{
    
    @Override
    public void start(Stage stage) throws Exception 
    {   
        stage.initStyle(StageStyle.UNDECORATED);
        
        Parent root = FXMLLoader.load(getClass().getResource("/Interfaces/Connexion.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
     
    }

   
    public static void main(String[] args) throws ClassNotFoundException {
        launch(args);
    }
    
}