package Main;

import Reports.CongéImp;
import Reports.ContratOP;
import Reports.ImpOutii;
import Reports.ImpPieceDem;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;



public class MainProject extends Application 
{
    @Override
    public void start(Stage stage) throws Exception 
    {
       // stage.initStyle(StageStyle.UNDECORATED);
        
       Parent root = FXMLLoader.load(getClass().getResource("/Interfaces/Connexion.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        
    }

   
    public static void main(String[] args) {
        launch(args);
    }
    
}
