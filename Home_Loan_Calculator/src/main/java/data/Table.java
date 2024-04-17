package data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

public class Table {
    ObservableList<Tuple> table;

    public Table(ArrayList<Tuple> tabler) {
        table = FXCollections.observableArrayList();
        for (Tuple t:tabler) {
            table.add(t);
        }
    }
    public ObservableList<Tuple> getTable() {
        return table;
    }

    public void setTable(ObservableList<Tuple> table) {
        this.table = table;
    }
    public void initTable(TableView<Tuple> Stalas, TableColumn<Tuple, Double> c1, TableColumn<Tuple, Double> c2, TableColumn<Tuple, Double> c3, TableColumn<Tuple, Double> c4){
        c1.setCellValueFactory(new PropertyValueFactory<>("total"));
        c2.setCellValueFactory(new PropertyValueFactory<>("interest"));
        c3.setCellValueFactory(new PropertyValueFactory<>("repayment"));
        c4.setCellValueFactory(new PropertyValueFactory<>("remainder"));
        Stalas.setItems(table);
    }
}
