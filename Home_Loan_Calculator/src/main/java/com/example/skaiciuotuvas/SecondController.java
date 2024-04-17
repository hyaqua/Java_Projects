package com.example.skaiciuotuvas;

import data.Table;
import data.Tuple;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class SecondController implements Initializable {

    Paskola p;
    @FXML
    private TableView<Tuple> Stalas;
    @FXML
    private TableColumn<Tuple, Double> c1;

    @FXML
    private TableColumn<Tuple, Double> c2;

    @FXML
    private TableColumn<Tuple, Double> c3;

    @FXML
    private TableColumn<Tuple, Double> c4;

    @FXML
    private Button btn1;

    @FXML
    private Tab grafikas;

    @FXML
    private ComboBox<Integer> men;

    @FXML
    private ComboBox<Integer> metai;

    @FXML
    private TextField palukanos;

    @FXML
    private TextField paskola;

    @FXML
    private TextField proc;

    @FXML
    private TextField rez;

    @FXML
    private Tab skaiciuotuvas;

    @FXML
    private TextField suma;

    @FXML
    private ComboBox<String> tipas;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        grafikas.setDisable(true);
        men.setItems(FXCollections.observableArrayList(0,1,2,3,4,5,6,7,8,9,10,11));
        metai.setItems(FXCollections.observableArrayList(1));
        for(int i = 2; i <= 50; i++){
            metai.getItems().add(i);
        }
        tipas.setItems(FXCollections.observableArrayList("Linijinis","Anuitinis"));
    }
    @FXML
    void calculate(ActionEvent event) {
            grafikas.setDisable(false);
            double kaina = Double.parseDouble(suma.getText());
            int laikas = metai.getValue() * 12 + men.getValue();
            double pal = Double.parseDouble(proc.getText()) / 100;
            boolean t = tipas.getValue().equals("Anuitinis");
            p = new Ilgalaike(kaina,laikas,pal,t);
            Tuple values = p.apskaiciuok().get(0);
            rez.setText(values.get(0) + "");
            palukanos.setText(values.get(1) + "");
            paskola.setText(values.get(2) + "");
            ((Ilgalaike)p).apskaiciuok();
            ((Ilgalaike) p).getTable().initTable(Stalas, c1, c2, c3, c4);
    }
}
