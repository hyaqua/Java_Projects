package com.example.lab_6;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

public class HelloController {

    @FXML
    private TableView<Person> Table;
    @FXML
    private TableColumn<Person, Integer> id;
    @FXML
    private TableColumn<Person, String> first_name;
    @FXML
    private TableColumn<Person, String> last_name;
    @FXML
    private TableColumn<Person, String> email;
    @FXML
    private TableColumn<Person, String> gender;
    @FXML
    private TableColumn<Person, String> country;
    @FXML
    private TableColumn<Person, String> domain;
    @FXML
    private TableColumn<Person, LocalDate> birth_date;
    @FXML
    private DatePicker fromDate;
    @FXML
    private DatePicker toDate;
    @FXML
    private ChoiceBox<String> sortField;
    @FXML
    private ChoiceBox<String> Direction;
    private ObservableList<Person> data;
    private ObservableList<Person> filtered;
    @FXML
    private ScrollPane scroll;

    public void addPerson(Person person){
        Platform.runLater(() -> {
            Table.getItems().add(person);
            data.add(person);
        });
    }
    public void initialize() {
        Dialog<String> dialog = new TextInputDialog("");
        dialog.setGraphic(null);
        dialog.setTitle("Sleep Time");
        dialog.setHeaderText("Enter how many milliseconds to sleep");
        Optional<String> res = dialog.showAndWait();
        int millis = 0;
        if(res.isPresent()){
            millis = Integer.parseInt(res.get());
        }
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        first_name.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        last_name.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        country.setCellValueFactory(new PropertyValueFactory<>("country"));
        domain.setCellValueFactory(new PropertyValueFactory<>("domainName"));
        birth_date.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        sortField.getItems().addAll("ID", "First Name", "Last name", "Email", "Gender", "Country", "Domain", "Birth date");
        Direction.getItems().addAll("Ascending", "Descending");
        sortField.setValue("ID");
        Direction.setValue("Ascending");

        data = FXCollections.observableArrayList();
        fromDate.setValue(LocalDate.of(1900,1,1));
        toDate.setValue(LocalDate.now());
        loadDataFromFile("MOCK_DATA1.csv", millis);
        loadDataFromFile("MOCK_DATA2.csv", millis);
        loadDataFromFile("MOCK_DATA3.csv", millis);
    }
    private void loadDataFromFile(String filePath, int millis){
        DataReader dataReader;
        dataReader = new DataReader(new File(filePath), this, millis);
        Thread t = new Thread(dataReader);
        t.start();
    }

    public void filterButton(ActionEvent actionEvent) {
        LocalDate from = fromDate.getValue();
        LocalDate to = toDate.getValue();
        filtered = data.stream()
                .filter(person -> {
                LocalDate bday = person.getBirthDate();
                return bday.isAfter(from.minusDays(1)) && bday.isBefore(to.plusDays(1));
                }).sorted(Comparator.comparing(Person::getBirthDate)).collect(Collectors.toCollection(FXCollections::observableArrayList));
        Table.setItems(filtered);
    }

    public void sortButton(ActionEvent actionEvent) {
        String field = sortField.getValue();
        String dir = Direction.getValue();
        Comparator<Person> comparator = switch (field) {
            case "ID" -> Comparator.comparing(Person::getId);
            case "Last name" -> Comparator.comparing(Person::getLastName);
            case "Email" -> Comparator.comparing(Person::getEmail);
            case "Gender" -> Comparator.comparing(Person::getGender);
            case "Country" -> Comparator.comparing(Person::getCountry);
            case "Domain" -> Comparator.comparing(Person::getDomainName);
            case "Birth date" -> Comparator.comparing(Person::getBirthDate);
            default -> Comparator.comparing(Person::getFirstName);
        };
        if(dir.equals("Descending")){
            comparator = comparator.reversed();
        }
        if(filtered == null){
            data = data.stream().sorted(comparator).collect(Collectors.toCollection(FXCollections::observableArrayList));
            Table.setItems(data);
        } else {
            filtered = filtered.stream().sorted(comparator).collect(Collectors.toCollection(FXCollections::observableArrayList));
            Table.setItems(filtered);
        }
    }
}