/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author OFFICE
 */
public class RechMissionCongé implements Initializable {

    @FXML
    private JFXRadioButton Cong;
    @FXML
    private JFXRadioButton mission;
    @FXML
    private JFXComboBox<?> Personnel;
    @FXML
    private JFXDatePicker DateDeb;
    @FXML
    private JFXDatePicker DateFin;
    @FXML
    private JFXButton ButtRech;
    @FXML
    private JFXButton Annule;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void Congé(ActionEvent event) {
    }

    @FXML
    private void Mission(ActionEvent event) {
    }

    @FXML
    private void Rechercher(ActionEvent event) {
    }

    @FXML
    private void Annuler(ActionEvent event) {
    }
    
}
