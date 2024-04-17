package com.example.studentai;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.itextpdf.text.Document;

import java.io.*;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class fileExporter {
    public static void export(TableView<Student> tableview, Students students, LocalDate dateFrom, LocalDate dateUntil, String fileName){
        if(fileName.endsWith(".xlsx")) {
            try {
                ExportToExcel(tableview, "Attendance", fileName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if(fileName.endsWith(".csv")){
            try {
                ExportToCSV(students, dateFrom, dateUntil, fileName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if(fileName.endsWith(".pdf")){
            exportToPDF(students, dateFrom, dateUntil, fileName);
        }
    }

    public static Students importer(String fileName) {
        if(fileName.endsWith(".xlsx")){
            try {
                return ImportFromExcel(fileName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if(fileName.endsWith(".csv")){
            return importFromCSV(fileName);
        }
        return null;
    }


    private static void ExportToExcel(TableView<Student> tableView, String sheetName, String filePath) throws IOException{
        try (Workbook workbook = new XSSFWorkbook()){
            Sheet sheet = workbook.createSheet(sheetName);
            ObservableList<TableColumn<Student, ?>> columns = tableView.getColumns();
            Row headerRow = sheet.createRow(0);
            for(int i = 0; i < columns.size(); i++){
                headerRow.createCell(i).setCellValue(columns.get(i).getText());
            }
            ObservableList<Student> items = tableView.getItems();
            for(int rowIdx = 0; rowIdx < items.size(); rowIdx++){
                Row row = sheet.createRow(rowIdx+1);
                Student item = items.get(rowIdx);
                for(int colIdx = 0; colIdx < columns.size(); colIdx++){
                    Object cellValue = columns.get(colIdx).getCellData(item);
                    if(cellValue != null){
                        row.createCell(colIdx).setCellValue(cellValue.toString());
                    }
                }
            }

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
        }
    }
    private static void ExportToCSV(Students students, LocalDate dateFrom, LocalDate dateUntil, String filePath) throws IOException {
        try (FileWriter fos = new FileWriter(filePath)) {
            fos.write("Vardas, ID,");
            for (LocalDate date = dateFrom; date.isBefore(dateUntil.plusDays(1)); date = date.plusDays(1)) {
                fos.write(date.toString() + ",");
            }
            fos.write("Grupes,");
            List<Grupes> grupesList = students.getGrupesList();
            for(int i = 0; i < grupesList.size(); i++){
                fos.write(grupesList.get(i).getName());
                if(i != grupesList.size()-1){
                    fos.write(",");
                }
            }
            fos.write("\n");
            List<Student> studentList = students.getStudentList();
            for(int i = 0; i < studentList.size(); i++){
                fos.write(studentList.get(i).getName() + "," + studentList.get(i).getId() + ",");
                for (LocalDate date = dateFrom; date.isBefore(dateUntil.plusDays(1)); date = date.plusDays(1)) {
                    fos.write(studentList.get(i).getAttendance(date) + ",");
                }
                fos.write(",");
                for(int j = 0; j < grupesList.size(); j++){
                    fos.write(studentList.get(i).hasGroup(grupesList.get(j).getName()) + "");
                    if(j != grupesList.size()-1){
                        fos.write(",");
                    }
                }
                fos.write("\n");
            }
            fos.close();
        }
    }
    private static Students ImportFromExcel(String filePath) throws IOException{
        Students imported = new Students();
        try (FileInputStream fis = new FileInputStream(filePath)) {
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(0);
            Iterator<Cell> cellIterator = row.cellIterator();
            while(!cellIterator.next().getStringCellValue().equals("Grupes")){
            }
            while(cellIterator.hasNext()){
                Cell cell = cellIterator.next();
                imported.addGrupes(new Grupes(cell.getStringCellValue()));
            }
            Iterator<Row> rowIterator = sheet.rowIterator();
            rowIterator.next();
            while (rowIterator.hasNext()){
                row = rowIterator.next();
                imported.addStudent(new Student(row.getCell(0).getStringCellValue(), row.getCell(1).getStringCellValue()));
            }
            int dateIdx = 2;
            rowIterator = sheet.rowIterator();
            while(!sheet.getRow(0).getCell(dateIdx).getStringCellValue().equals("Grupes")){
                row = rowIterator.next();
                if(!row.getCell(dateIdx).getStringCellValue().equals("Grupes")){
                    LocalDate date = LocalDate.parse(row.getCell(dateIdx).getStringCellValue());
                    int studentIdx = 0;
                    while (rowIterator.hasNext()){
                        row = rowIterator.next();
                        if(row.getCell(dateIdx).getStringCellValue().equals("true")){
                            imported.setAttendance(date, studentIdx);
                        }
                        studentIdx++;
                    }
                    dateIdx++;
                    rowIterator = sheet.rowIterator();
                }
            }
            if(sheet.getRow(0).getCell(dateIdx).getStringCellValue().equals("Grupes")){
                dateIdx++;
            }
            int grupesIdx = dateIdx;
            rowIterator = sheet.rowIterator();
            while(grupesIdx < sheet.getRow(0).getLastCellNum()){
                row = rowIterator.next();
                int studentIdx = 0;
                while(rowIterator.hasNext()){
                    row = rowIterator.next();
                    if(row.getCell(grupesIdx).getStringCellValue().equals("true")){
                        imported.addToGroup(imported.getGrupe(sheet.getRow(0).getCell(grupesIdx).getStringCellValue()), imported.getStudent(row.getCell(1).getStringCellValue()));
                    }
                }
                grupesIdx++;
                rowIterator = sheet.rowIterator();
            }
        }
        ObservableList<Student> students = imported.getObservableStudentList();
        for(Student student : students){
            for(int i = 2; i < imported.getGrupesList().size()+2; i++){
                System.out.println(student.ListDates());
            }
        }
        return imported;
    }
    private static Students importFromCSV(String filePath){
        try (Scanner sc = new Scanner(new File(filePath))) {
            String[] header = sc.nextLine().split("[,]", 0);
            Students students = new Students();
            int i = 0;
            while(!header[i].equals("Grupes")){
                System.out.println(header[i]);
                i += 1;
                if(!(i < header.length - 1)){
                    break;
                }
            }
            for(i++; i < header.length; i++){
                System.out.println(header[i]);
                students.addGrupes(new Grupes(header[i]));
            }
            while (sc.hasNextLine()){
                String[] student = sc.nextLine().split(",");
                students.addStudent(new Student(student[0], student[1]));
                List<Student> studentsList = students.getStudentList();
                System.out.println(studentsList);
                int j;
                for(j = 2; student[j] != ""; j++){
                    if(student[j].equals("true")){
                        students.setAttendance(LocalDate.parse(header[j]), studentsList.size()-1);
                    }
                }
                for(j++; j < header.length; j++){
                    if(student[j].equals("true")){
                        students.addToGroup(students.getGrupe(header[j]), studentsList.get(studentsList.size()-1));
                    }
                }
            }
            sc.close();
            return students;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    private static void exportToPDF(Students students, LocalDate dateFrom, LocalDate dateUntil, String filePath){
        int colCount = 2;
        for (LocalDate date = dateFrom; date.isBefore(dateUntil.plusDays(1)); date = date.plusDays(1)) {
            colCount++;
        }
        Document document = new Document();
        try  {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            PdfPTable table = new PdfPTable(colCount);
            table.addCell("Vardas");
            table.addCell("ID");
            for(LocalDate date = dateFrom; date.isBefore(dateUntil.plusDays(1)); date = date.plusDays(1)){
                table.addCell(date.toString());
            }
            students.getStudentList();
            for(Student student : students.getStudentList()){
                table.addCell(student.getName());
                table.addCell(student.getId());
                for(LocalDate date = dateFrom; date.isBefore(dateUntil.plusDays(1)); date = date.plusDays(1)){
                    table.addCell(student.getAttendance(date) + "");
                }
            }
            document.add(table);
            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
