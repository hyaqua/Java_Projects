package com.example.studentai;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import org.controlsfx.control.*;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FirstController implements Initializable {

    private Students students;
    @FXML
    private TextField name;
    @FXML
    private TextField id;
    @FXML
    private Button addStudent;
    @FXML
    private Button addStudent1;
    @FXML
    private TextField groupName;
    @FXML
    private CheckListView studentList1;
    @FXML
    private TableView Stalas;
    @FXML
    private TableColumn<Student, String> staloVardas;
    @FXML
    private TableColumn<Student, String> staloID;
    @FXML
    private TableColumn<Student, Boolean> staloLankomumas;
    @FXML
    private CheckBox intervalas;
    @FXML
    private Label labelis;
    @FXML
    private CheckListView GroupStudentList1;
    @FXML
    private Button groupAddStudents;
    @FXML
    private ChoiceBox selectedGroup1;
    @FXML
    private CheckListView GroupStudentList;
    @FXML
    private DatePicker GroupDate;
    @FXML
    private Button groupSetAttendance;
    @FXML
    private ChoiceBox selectedGroup;
    @FXML
    private DatePicker date1;
    @FXML
    private Button setAttendence1;
    @FXML
    private Pane addStudentsTab;
    @FXML
    private Tab setAttendanceTab;
    @FXML
    private TabPane tabai;
    @FXML
    private DatePicker pirmaData;
    @FXML
    private DatePicker antraData;
    @FXML
    private Button start;
    @FXML
    private Button export;
    @FXML
    private Button importuoti;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        students = new Students();
        tabai.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                updateTabs();
            }
        });
    }
    @FXML
    public void StudentAdd(ActionEvent actionEvent) {
        students.addStudent(new Student(name.getText(),id.getText()));
    }

    @FXML
    public void interv(ActionEvent actionEvent) {
        labelis.setVisible(intervalas.isSelected());
        antraData.setVisible(intervalas.isSelected());
    }

    @FXML
    public void addGroup(ActionEvent actionEvent) {
        students.addGrupes(new Grupes(groupName.getText()));
    }

    @FXML
    public void addStudentsToGroup(ActionEvent actionEvent) {
        ObservableList list = GroupStudentList1.getCheckModel().getCheckedItems();
        for (Object o : list) {
            students.addToGroup((Grupes) selectedGroup1.getSelectionModel().getSelectedItem(), (Student) o);

        }
    }

    @FXML
    public void GroupStudentsAdd(ActionEvent actionEvent) { // setAttendanceGroup
        ObservableList list = GroupStudentList.getCheckModel().getCheckedItems();

        students.setAttendance(GroupDate.getValue(), new ArrayList<Student>(list));
    }

    @FXML
    public void studentsAdd(ActionEvent actionEvent) { //   setAttendance
        ObservableList list = studentList1.getCheckModel().getCheckedItems();

        students.setAttendance(date1.getValue(), new ArrayList<Student>(list));
    }

    public void updateTabs() {
        studentList1.getItems().clear();
        GroupStudentList1.getItems().clear();
        ObservableList<Student> list = students.getObservableStudentList();
        ObservableList<Grupes> groups = students.getObservableGrupesList();
        studentList1.setItems(list);
        GroupStudentList1.setItems(list);
        selectedGroup1.setItems(groups);
        selectedGroup.setItems(groups);
    }

    @FXML
    public void ChangeStudentList(ActionEvent actionEvent) {
        if(selectedGroup.getSelectionModel().getSelectedItem() != null){
            ObservableList<Student> list = students.getObservableStudentList();
            list.removeIf(s -> !s.hasGroup(selectedGroup.getSelectionModel().getSelectedItem().toString()));
            GroupStudentList.getItems().clear();
            GroupStudentList.getItems().addAll(list);
        }
    }
    @FXML
    public void showTable(ActionEvent actionEvent) {
        students.sort();
        staloVardas.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));
        staloID.setCellValueFactory(new PropertyValueFactory<Student, String>("id"));
        staloLankomumas.setCellValueFactory(param -> new SimpleBooleanProperty(param.getValue().getAttendance(pirmaData.getValue())));
        staloLankomumas.setText(pirmaData.getValue().toString());
        if(intervalas.isSelected()) {
            for (LocalDate date = pirmaData.getValue().plusDays(1); date.isBefore(antraData.getValue().plusDays(1)); date = date.plusDays(1)) {
                TableColumn<Student, Boolean> tableColumn = new TableColumn<Student, Boolean>(date.toString());
                LocalDate finalDate = date;
                tableColumn.setCellValueFactory(param -> new SimpleBooleanProperty(param.getValue().getAttendance(finalDate)));
                tableColumn.setPrefWidth(staloLankomumas.getPrefWidth());
                Stalas.getColumns().add(tableColumn);
            }
        }
        Stalas.getColumns().add(new TableColumn<>("Grupes"));
        List<Grupes> grupes = students.getGrupesList();
        for (Grupes grupe : grupes) {
            TableColumn<Student, Boolean> tableColumn = new TableColumn<Student, Boolean>(grupe.getName());
            tableColumn.setCellValueFactory(param -> new SimpleBooleanProperty(param.getValue().hasGroup(grupe.getName())));
            Stalas.getColumns().add(tableColumn);
        }
        Stalas.setItems(students.getObservableStudentList());
    }

    @FXML
    public void RunExport(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF files", "*.pdf"),
                new FileChooser.ExtensionFilter("Excel files", "*.xlsx"),
                new FileChooser.ExtensionFilter("CSV files", "*.csv")
        );
        File file = fileChooser.showSaveDialog(null);
        fileExporter.export(Stalas, students, pirmaData.getValue(), antraData.getValue(), file.getAbsolutePath());
    }

    @FXML
    public void RunImport(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel files", "*.xlsx"),
                new FileChooser.ExtensionFilter("CSV files", "*.csv")
        );
        File file = fileChooser.showOpenDialog(null);
        students = fileExporter.importer(file.getAbsolutePath());
    }
}
