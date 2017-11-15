//import javafx.beans.value.ChangeListener;
//import javafx.beans.value.ObservableValue;
//import javafx.concurrent.Task;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.scene.Node;
//import javafx.scene.control.Alert;
//import javafx.scene.control.ProgressBar;
//import javafx.scene.control.TextField;
//import javafx.scene.input.KeyEvent;
//import javafx.scene.layout.Pane;
//import javafx.stage.FileChooser;
//import javafx.stage.Window;
//import org.apache.commons.lang.StringUtils;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
//import javafx.stage.Window;

//import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

//import javax.swing.*;
//import java.awt.*;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
public class CarricamentoService {

    public void errorSet(String alertMSG, Alert alertERROR) {
        alertERROR.setHeaderText(alertMSG);
        alertERROR.show();
    }

    public String cellTextSet(Cell cell, Row currentRow, int colToCopy) {

        cell = currentRow.getCell(colToCopy);
        cell.setCellType(CellType.STRING);

        return String.valueOf(cell);
    }
}











//    String excelFilePath = "";
//    Window stage;
//    int colToCopy;
//    Cell cell;
//    int stringProperLength;
//
//    @FXML
//    private TextField textField;
//    @FXML
//    private ProgressBar progressBar;
//
//    Task copyWorker;
//
//    @FXML
//    private Pane pane;
//
//
//    public void cleanButton(ActionEvent event) {
//        for (int i = 0; i < 14; i++) {
//            textField = (TextField) pane.lookup("#textField" + i);
//            textField.setText("");
//        }
//    }
//
//    @FXML
//    public void handleKeyPressed(KeyEvent keyEvent) {
//
//        Set<Character> set = new HashSet<>();
//
//        for (Node node : pane.getChildren()) {
//            if (node instanceof TextField) {
//                if ((((TextField) node).getText().length() > 1 ||
//                        ((TextField) node).getText().length() == 1 &&
//                                !(((TextField) node).getText().toLowerCase().matches("[a-n]|[0]")))) {
//                    String unField = "You can put only letters from a to n or 0!!!!! A to N are OK as well:)";
//                    Alert alert = new Alert(Alert.AlertType.ERROR);
//                    alert.setHeaderText(unField);
//                    alert.show();
//                    ((TextField) node).setText("");
//                    break;
//                } else if (((TextField) node).getText().length() == 1) {
//                    if (set.contains(((TextField) node).getText().charAt(0)) && !(((TextField) node).getText().matches("[0]"))) {
//                        String duplicate = "You duplicated the value";
//                        Alert alert = new Alert(Alert.AlertType.ERROR);
//                        alert.setHeaderText(duplicate);
//                        alert.show();
//                        ((TextField) node).setText("");
//                    } else {
//                        set.add(((TextField) node).getText().charAt(0));
//                    }
//                }
//            }
//        }
//
//
//    }
//
//    @FXML
//    public void goButtonPressed(ActionEvent event) {
//
//        boolean hasEmpty = true;
//        StringBuilder exportString = new StringBuilder("");
//        StringBuilder errorString = new StringBuilder("");
//        StringBuilder inputString = new StringBuilder("");
//        Alert alertERROR = new Alert(Alert.AlertType.ERROR);
//        Alert alertInformation = new Alert(Alert.AlertType.INFORMATION);
//        String alertMSG;
//
//
//        copyWorker = createWorker();
//        progressBar.progressProperty().unbind();
//        progressBar.progressProperty().bind(copyWorker.progressProperty());
//        copyWorker.messageProperty().addListener(new ChangeListener<String>() {
//            public void changed(ObservableValue<? extends String> observable,
//                                String oldValue, String newValue) {
//            }
//        });
//        new Thread(copyWorker).start();
//
//
//        for (Node node : pane.getChildren()) {
//            if (node instanceof javafx.scene.control.TextField && ((javafx.scene.control.TextField) node).getText().length() == 0) {
//                String unField = node.getId() + " nie jest wypelniony";
//                System.out.println(node.getId() + " nie jest wypelniony");
//                alertERROR = new Alert(Alert.AlertType.ERROR);
//                alertERROR.setHeaderText(unField);
//                alertERROR.show();
//                hasEmpty = false;
//                break;
//            }
//        }
//
//        for (int i = 0; i < 14; i++) {
//            textField = (TextField) pane.lookup("#textField" + i);
//            inputString.append(textField.getText().toLowerCase());
//        }
//
//
//        if (inputString.charAt(0) == '0' ||
//                inputString.charAt(2) == '0' ||
//                inputString.charAt(4) == '0' ||
//                inputString.charAt(6) == '0' ||
//                inputString.charAt(7) == '0' ||
//                inputString.charAt(8) == '0') {
//            String unField = "Textfields with PROGR_UDA \n FILIALE \n TIPO_DOC \n ANNI_CONS \n DATA INIZIO \n DATA FINE        cannot posses value '0', change it";
//            alertERROR = new Alert(Alert.AlertType.ERROR);
//            alertERROR.setHeaderText(unField);
//            alertERROR.show();
//            hasEmpty = false;
//        }
//        if (hasEmpty) {
//            DateFormat shortFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
//            DateFormat mediumFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
//            Date dateInizio = new Date();
//            Date dateFine = new Date();
//
//            try {
//                FileInputStream excelFile = new FileInputStream(new File(excelFilePath));
//                Workbook workbook = new XSSFWorkbook(excelFile);
//                Sheet datatypeSheet = workbook.getSheetAt(0);
//                Iterator<Row> iterator = datatypeSheet.iterator();
//                iterator.next();
//
//
//                Boolean mainPass = true;
//                Boolean errorPass = false;
//                int counter = 1;
//
//                while (iterator.hasNext()) {
//                    Row currentRow = iterator.next();
//                    counter += 1;
//                    for (int i = 0; i < 14; i++) {
//                        colToCopy = inputString.charAt(i) - 97;
//                        switch (i) {
//                            case 0:
//                                //PROG_UDA
//                                cell = currentRow.getCell(colToCopy);
//
//                                if (cell.getCellTypeEnum() == CellType.NUMERIC) {
//                                    if ((int) cell.getNumericCellValue() > 9999999 || (int) cell.getNumericCellValue() < 1000000) { //symbols as 1234567.0
//                                        errorString.append("UDA in line" + counter + "has more than 7 digits**");
//                                        mainPass = false;
//                                        errorPass = true;
//                                        break;
//                                    } else {
//                                        exportString.append(String.valueOf((int) cell.getNumericCellValue()) + "*");
//                                    }
//                                } else if (cell.getCellTypeEnum() == CellType.STRING) {
//                                    if (cell.getStringCellValue().length() != 7) {
//                                        errorString.append("UDA in line" + counter + "does not have 7 digits**");
//                                        mainPass = false;
//                                        errorPass = true;
//                                        break;
//                                    } else if (cell.getStringCellValue().length() == 7) {
//                                        exportString.append(cell.getStringCellValue() + "*");
//                                        break;
//                                    }
//                                }
//                            case 1:
//                                //DESCRIZIONE FILLIALE
//                                if (inputString.charAt(i) == '0') {
//                                    exportString.append(StringUtils.repeat(" ", 3) + "50");
//                                    break;
//                                } else {
//                                    cell = currentRow.getCell(colToCopy);
//                                    if (cell.toString().length() > 50) {
//                                        errorString.append("Descrizione Filliale in line " + counter + " is to long**");
//                                        errorPass = true;
//                                        mainPass = false;
//                                        break;
//                                    } else {
//                                        stringProperLength = 50 - cell.toString().length();
//                                        exportString.append(cell.toString() + StringUtils.repeat(" ", stringProperLength) + "*");
//                                        break;
//                                    }
//                                }
//                            case 2:
//                            case 4:
//                                //Filiale
//                                //TipoDoc
//                                cell = currentRow.getCell(colToCopy);
//                                if (cell.getCellTypeEnum() == CellType.NUMERIC) {
//                                    if ((int) cell.getNumericCellValue() > 99999) {
//                                        errorString.append("TipoDOC and Filiale in line " + counter + " has more than 5 numbers**");
//                                        errorPass = true;
//                                        mainPass = false;
//                                        break;
//                                    } else {
//                                        stringProperLength = 5 - String.valueOf((int) cell.getNumericCellValue()).length();
//                                        exportString.append(StringUtils.repeat("0", stringProperLength) + String.valueOf((int) cell.getNumericCellValue()) + "*");
//                                        break;
//                                    }
//                                } else if (cell.getCellTypeEnum() == CellType.STRING) {
//                                    if (cell.getStringCellValue().length() > 5) {
//                                        errorString.append("TipoDOC and Filiale in line " + counter + " has more than 5 numbers**");
//                                        errorPass = true;
//                                        mainPass = false;
//                                        break;
//                                    } else {
//                                        stringProperLength = 5 - cell.getStringCellValue().length();
//                                        exportString.append(StringUtils.repeat("0", stringProperLength) + cell.getStringCellValue() + "*");
//                                        break;
//                                    }
//                                }
//                            case 3:
//                                //Dislocazione
//                                if (inputString.charAt(i) == '0') {
//                                    exportString.append(StringUtils.repeat(" ", 3) + "*");
//                                    break;
//                                } else {
//                                    cell = currentRow.getCell(colToCopy);
//                                    if (cell.getStringCellValue().length() > 3) {
//                                        errorString.append("DISLOCAZIONE in line" + counter + " has more than 3 numbers**");
//                                        errorPass = true;
//                                        mainPass = false;
//                                        break;
//                                    } else {
//                                        stringProperLength = 3 - cell.getStringCellValue().length();
//                                        exportString.append(StringUtils.repeat("0", stringProperLength) + cell.getStringCellValue() + "*");
//                                        break;
//                                    }
//                                }
//                            case 5:
//                            case 9:
//                                //DESC TIPO
//                                //NOTE
//                                if (inputString.charAt(i) == '0') {
//                                    exportString.append(StringUtils.repeat(" ", 80) + "*");
//                                    break;
//                                } else {
//                                    cell = currentRow.getCell(colToCopy);
//                                    if (cell.toString().length() > 80) {
//                                        errorString.append("Some DescTipo or Note in line " + counter + " is too long**");
//                                        mainPass = false;
//                                        errorPass = true;
//                                        break;
//                                    } else {
//                                        stringProperLength = 80 - cell.toString().length();
//                                        exportString.append(cell.toString() + StringUtils.repeat(" ", stringProperLength) + "*");
//                                        break;
//                                    }
//                                }
//                            case 6:
//                                //ANNI CONS
//                                cell = currentRow.getCell(colToCopy);
//
//                                if (cell.getCellTypeEnum() == CellType.NUMERIC) {
//                                    if (cell.getNumericCellValue() > 99) {
//                                        errorString.append("ANNI CONSERVATIONI in line " + counter + " has more than 2 numbers**");
//                                        mainPass = false;
//                                        errorPass = true;
//                                        break;
//                                    } else {
//                                        stringProperLength = 2 - String.valueOf((int) cell.getNumericCellValue()).length();
//                                        exportString.append(StringUtils.repeat("0", stringProperLength) + ((int) cell.getNumericCellValue()) + "*");
//                                        break;
//                                    }
//                                } else if (cell.getCellTypeEnum() == CellType.STRING) {
//                                    if (cell.getStringCellValue().length() > 2) {
//                                        errorString.append("ANNI CONSERVATIONI in line " + counter + " has more than 2 numbers**");
//                                        mainPass = false;
//                                        errorPass = true;
//                                        break;
//                                    } else {
//                                        stringProperLength = 2 - String.valueOf((int) cell.getNumericCellValue()).length();
//                                        exportString.append(StringUtils.repeat("0", stringProperLength) + cell.getStringCellValue() + "*");
//                                        break;
//                                    }
//
//                                }
//
//                            case 7:
//                                //Data INIZIO
//                                cell = currentRow.getCell(colToCopy);
//                                String cellWithDateInizio = cell.toString();
//                                dateInizio = mediumFormat.parse(cellWithDateInizio);
//                                String shortDateInizio = shortFormat.format(mediumFormat.parse(cellWithDateInizio));
//                                exportString.append(shortDateInizio + "*");
//                                break;
//                            case 8:
//                                //DATA FINE
//                                cell = currentRow.getCell(colToCopy);
//                                String cellWithDateFine = cell.toString();
//                                dateFine = mediumFormat.parse(cellWithDateFine);
//                                String shortDateFine = shortFormat.format(mediumFormat.parse(cellWithDateFine));
//                                if (dateInizio.after(dateFine)) {
//                                    errorString.append("Data Fine in line " + counter + " is before Data Inizio**");
//                                    mainPass = false;
//                                    errorPass = true;
//                                } else {
//                                    exportString.append(shortDateFine + "*");
//                                }
//                                break;
//                            case 10:
//                            case 11:
//                            case 13:
//                                //NDG
//                                //codice practica
//                                //NDG1
//                                //16 znakow
//                                if (inputString.charAt(i) == '0') {
//                                    exportString.append(StringUtils.repeat("0", 16) + "*");
//                                    break;
//                                } else {
//                                    cell = currentRow.getCell(colToCopy);
//                                    stringProperLength = 16 - String.valueOf(((long) cell.getNumericCellValue())).length();
//                                    exportString.append(StringUtils.repeat("0", stringProperLength) + ((long) cell.getNumericCellValue()) + "*");
//                                    break;
//                                }
//                            case 12:
//                                //Denominazione
//                                if (inputString.charAt(i) == '0') {
//                                    exportString.append(StringUtils.repeat(" ", 64) + "*");
//                                    break;
//                                } else {
//                                    cell = currentRow.getCell(colToCopy);
//                                    if (cell.toString().length() > 64) {
//                                        errorString.append("DENOMINAZIONE in line " + counter + " has more than 64 symbols**");
//                                        mainPass = false;
//                                        errorPass = true;
//                                        break;
//                                    } else {
//                                        stringProperLength = 64 - cell.toString().length();
//                                        exportString.append(cell.toString() + StringUtils.repeat(" ", stringProperLength) + "*");
//                                        break;
//                                    }
//                                }
//                        }
//
//                    }
//                    if (errorPass) {
//                        errorString.append(System.lineSeparator());
//                        errorPass = false;
//                    }
//                    exportString.deleteCharAt(exportString.length() - 1).append(System.lineSeparator());
//                }
//
//
//                if (mainPass) {
//                    alertMSG = "Everything is OK :D Now you can check a file :))";
//                    alertInformation.setHeaderText(alertMSG);
//
//                    JFileChooser chooser = new JFileChooser();
//                    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//                    int option = chooser.showSaveDialog(null);
//                    File pathToSave = new File(chooser.getSelectedFile().getPath());
//                    String pathToSaveString = pathToSave.toString() + "\\C0.CRV.SAD.SAD1CAC0.FILEUDAI.txt";
//                    if (option == JFileChooser.APPROVE_OPTION) {
//                        try {
//                            Files.write(Paths.get(pathToSaveString), exportString.toString().getBytes());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    alertInformation.show();
//                } else {
//                    alertMSG = "The process occurs some ERRORS. Check CarricamentoMassivoERRORS";
//                    alertERROR.setHeaderText(alertMSG);
//
//                    JFileChooser chooser = new JFileChooser();
//                    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//                    int option = chooser.showSaveDialog(null);
//                    File pathToSave = new File(chooser.getSelectedFile().getPath());
//                    String pathToSaveString = pathToSave.toString() + "\\CarricamentoMassivoERRORS.txt";
//                    if (option == JFileChooser.APPROVE_OPTION) {
//                        try {
//                            Files.write(Paths.get(pathToSaveString), errorString.toString().getBytes());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    alertERROR.show();
//                }
//            } catch (FileNotFoundException e) {
//                alertMSG = "click on 'Znajdź plik' and find Carricamento massivo file";
//                alertERROR.setHeaderText(alertMSG);
//                alertERROR.show();
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (ParseException e) {
//                alertMSG = "something went wrong with date - parsing. Check datas in excel file";
//                alertERROR.setHeaderText(alertMSG);
//                alertERROR.show();
//                e.printStackTrace();
//                e.printStackTrace();
//            }
//        }
//
//    }
//
//    @FXML
//    public void buttonPressed(ActionEvent event) {
//
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Znajdź plik");
//        fileChooser.getExtensionFilters().addAll(
//                new FileChooser.ExtensionFilter("All FILES", "*.*"),
//                new FileChooser.ExtensionFilter("XLS", "*.xls"),
//                new FileChooser.ExtensionFilter("XLSX", "*.xlsx"));
//        File selectedFile = fileChooser.showOpenDialog(stage);
//        if (selectedFile != null) {
//            openFile(selectedFile);
//            excelFilePath = selectedFile.getPath();
//        }
//    }
//
//    private Task createWorker() {
//        return new Task() {
//            @Override
//            protected Object call() throws Exception {
//                for (int i = 0; i < 10; i++) {
//                    updateProgress(i + 1, 10);
//                }
//                return true;
//            }
//        };
//    }
//
//  @FXML
//    private void openFile(File selectedFile) {
//        try {
//            Desktop.getDesktop().open(selectedFile);
//        } catch (IOException ex) {
//            Logger.getLogger(FileChooser.class.getName()).log(
//                    Level.SEVERE, null, ex
//            );
//        }
//    }
//}