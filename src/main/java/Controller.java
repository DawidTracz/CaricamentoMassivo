import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class Controller {

    @FXML
    private Pane pane;

    @FXML
    private TextField textField;

    @FXML
    private ProgressBar progressBar;

    private int colToCopy;

    private Cell cell;

    private int stringProperLength;

    private Task copyWorker;

    private String excelFilePath = StringUtils.EMPTY;

    private String shortFormatString = "yyyy-MM-dd";

    private String mediumFormatString = "dd-MMM-yyyy";

    private static int textFieldInputProperLength;

    String cellText = StringUtils.EMPTY;

    int numberOfTextFields = 14;

    CarricamentoService carricamentoService = new CarricamentoService();

    @FXML
    public void buttonPressed(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Znajdź plik");
        Map.Entry<String, String>[] extensionsArray = new Map.Entry[]{
                new AbstractMap.SimpleEntry("ALL FILES", "*.*"),
                new AbstractMap.SimpleEntry("XLS", "*.xls"),
                new AbstractMap.SimpleEntry("XLSX", "*.xlsx")};

        fileChooser.getExtensionFilters().addAll
                (Arrays.stream(extensionsArray).map(entry-> new FileChooser.ExtensionFilter(entry.getKey(), entry.getValue())).collect(Collectors.toList()));

        File selectedFile = fileChooser.showOpenDialog(pane.getScene().getWindow());
        if (selectedFile != null) {
            openFile(selectedFile);
            excelFilePath = selectedFile.getPath();
        }
    }

    @FXML
    public void goButtonPressed(ActionEvent event) {
        boolean hasEmpty = true;
        StringBuilder exportString = new StringBuilder(StringUtils.EMPTY);
        StringBuilder errorString = new StringBuilder(StringUtils.EMPTY);
        StringBuilder inputString = new StringBuilder(StringUtils.EMPTY);
        Alert alertERROR = new Alert(Alert.AlertType.ERROR);
        Alert alertInformation = new Alert(Alert.AlertType.INFORMATION);
        String alertMSG;
        copyWorker = createWorker();
        progressBar.progressProperty().unbind();
        progressBar.progressProperty().bind(copyWorker.progressProperty());
        copyWorker.messageProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
            }
        });

        new Thread(copyWorker).start();

        for (Node node : pane.getChildren()) {
            if (node instanceof TextField && ((TextField) node).getText().length() == 0) {
                String unField = node.getId() + " nie jest wypelniony";
                alertERROR = new Alert(Alert.AlertType.ERROR);
                carricamentoService.errorSet(unField, alertERROR);
                hasEmpty = false;
                break;
            }
        }

        for (int i = 0; i < numberOfTextFields; i++) {
            textField = (TextField) pane.lookup("#textField" + i);
            inputString.append(textField.getText().toLowerCase());
        }


        if (inputString.charAt(0) == '0' ||
                inputString.charAt(2) == '0' ||
                inputString.charAt(4) == '0' ||
                inputString.charAt(6) == '0' ||
                inputString.charAt(7) == '0' ||
                inputString.charAt(8) == '0') {
            String unField = "Textfields with PROGR_UDA \n FILIALE \n TIPO_DOC \n ANNI_CONS \n DATA INIZIO \n DATA FINE        cannot posses value '0', change it!!!";
            alertERROR = new Alert(Alert.AlertType.ERROR);
            alertERROR.setHeaderText(unField);
            alertERROR.show();
            hasEmpty = false;
        }

        if (hasEmpty) {

            DateFormat shortFormat = new SimpleDateFormat(shortFormatString, Locale.ENGLISH);
            DateFormat mediumFormat = new SimpleDateFormat(mediumFormatString, Locale.ENGLISH);
            Date dateInizio = new Date();
            Date dateFine;

            try {
                FileInputStream excelFile = new FileInputStream(new File(excelFilePath));
                Workbook workbook = new XSSFWorkbook(excelFile);
                Sheet datatypeSheet = workbook.getSheetAt(0);
                Iterator<Row> iterator = datatypeSheet.iterator();
                iterator.next();

                Boolean mainPass = true;
                Boolean errorPass = false;

                int counter = 1;

                while (iterator.hasNext()) {
                    Row currentRow = iterator.next();
                    counter += 1;
                    for (int i = 0; i < numberOfTextFields; i++) {
                        colToCopy = inputString.charAt(i) - 'a';
                        switch (i) {
                            case 0:
                                //PROG_UDA
                                textFieldInputProperLength = 7;
                                cellText = carricamentoService.cellTextSet(cell, currentRow, colToCopy);
                                if (cellText.length() != textFieldInputProperLength) {
                                    errorString.append("UDA in line").append(counter).append("does not have 7 digits**");
                                    mainPass = false;
                                    errorPass = true;
                                } else {
                                    exportString.append(cellText).append("*");
                                }
                                break;
                            case 1:
                                //DESCRIZIONE FILLIALE

                                cell = currentRow.getCell(colToCopy);
                                textFieldInputProperLength = 50;

                                if (inputString.charAt(i) == '0') {
                                    exportString.append(StringUtils.repeat(" ", textFieldInputProperLength));
                                } else {
                                    if (cell.toString().length() > textFieldInputProperLength) {
                                        errorString.append("Descrizione Filliale in line ").append(counter).append(" is to long**");
                                        errorPass = true;
                                        mainPass = false;
                                    } else {
                                        stringProperLength = textFieldInputProperLength - cell.toString().length();
                                        exportString.append(cell.toString()).append(StringUtils.repeat(" ", stringProperLength)).append("*");
                                    }
                                }
                                break;
                            case 2:
                            case 4:
                                //Filiale
                                //TipoDoc

                                textFieldInputProperLength = 5;
                                cellText = carricamentoService.cellTextSet(cell, currentRow, colToCopy);

                                if (cellText.length() > textFieldInputProperLength) {

                                    errorString.append("TipoDOC and Filiale in line ").append(counter).append(" has more than 5 numbers**");
                                    errorPass = true;
                                    mainPass = false;

                                } else {
                                    stringProperLength = textFieldInputProperLength - cellText.length();
                                    exportString.append(StringUtils.repeat("0", stringProperLength)).append(cellText).append("*");
                                }
                                break;
                            case 3:
                                //Dislocazione
                                textFieldInputProperLength = 3;
                                if (inputString.charAt(i) == '0') {
                                    exportString.append(StringUtils.repeat(" ", textFieldInputProperLength)).append("*");
                                } else {
                                    cell = currentRow.getCell(colToCopy);
                                    if (cell.getStringCellValue().length() > textFieldInputProperLength) {
                                        errorString.append("DISLOCAZIONE in line").append(counter).append(" has more than 3 numbers**");
                                        errorPass = true;
                                        mainPass = false;
                                    } else {
                                        stringProperLength = textFieldInputProperLength - cell.getStringCellValue().length();
                                        exportString.append(StringUtils.repeat("0", stringProperLength)).append(cell.getStringCellValue()).append("*");
                                    }
                                }
                                break;
                            case 5:
                            case 9:
                                //DESC TIPO
                                //NOTE
                                textFieldInputProperLength = 80;
                                if (inputString.charAt(i) == '0') {
                                    exportString.append(StringUtils.repeat(" ", stringProperLength)).append("*");
                                } else {
                                    cell = currentRow.getCell(colToCopy);
                                    if (cell.toString().length() > textFieldInputProperLength) {
                                        errorString.append("Some DescTipo or Note in line ").append(counter).append(" is too long**");
                                        mainPass = false;
                                        errorPass = true;
                                    } else {
                                        stringProperLength = textFieldInputProperLength - cell.toString().length();
                                        exportString.append(cell.toString()).append(StringUtils.repeat(" ", stringProperLength)).append("*");
                                    }
                                }
                                break;
                            case 6:
                                //ANNI CONS
                                textFieldInputProperLength = 2;
                                cellText = carricamentoService.cellTextSet(cell, currentRow, colToCopy);
                                if (cellText.length() > textFieldInputProperLength) {
                                    errorString.append("ANNI CONSERVATIONI in line ").append(counter).append(" has more than 2 numbers**");
                                    mainPass = false;
                                    errorPass = true;
                                } else {
                                    stringProperLength = textFieldInputProperLength - cellText.length();
                                    exportString.append(StringUtils.repeat("0", stringProperLength)).append(cellText).append("*");
                                }
                                break;
                            case 7:
                                //Data INIZIO
                                cell = currentRow.getCell(colToCopy);
                                String cellWithDateInizio = cell.toString();
                                dateInizio = mediumFormat.parse(cellWithDateInizio);
                                String shortDateInizio = shortFormat.format(mediumFormat.parse(cellWithDateInizio));
                                exportString.append(shortDateInizio).append("*");
                                break;
                            case 8:
                                //DATA FINE
                                cell = currentRow.getCell(colToCopy);
                                String cellWithDateFine = cell.toString();
                                dateFine = mediumFormat.parse(cellWithDateFine);
                                String shortDateFine = shortFormat.format(mediumFormat.parse(cellWithDateFine));
                                if (dateInizio.after(dateFine)) {
                                    errorString.append("Data Fine in line ").append(counter).append(" is before Data Inizio**");
                                    mainPass = false;
                                    errorPass = true;
                                } else {
                                    exportString.append(shortDateFine).append("*");
                                }
                                break;
                            case 10:
                            case 11:
                            case 13:
                                //NDG
                                //codice practica
                                //NDG1
                                //16 znakow
                                textFieldInputProperLength = 16;
                                if (inputString.charAt(i) == '0') {
                                    exportString.append(StringUtils.repeat("0", textFieldInputProperLength)).append("*");
                                } else {

                                    cell = currentRow.getCell(colToCopy);
                                    cellText = carricamentoService.cellTextSet(cell, currentRow, colToCopy);
                                   // cellText = String.valueOf(((long) cell.getNumericCellValue()));
                                    stringProperLength = textFieldInputProperLength - cellText.length();
                                    exportString.append(StringUtils.repeat("0", stringProperLength)).append(cellText).append("*");
                                }
                                break;
                            case 12:
                                //Denominazione
                                textFieldInputProperLength = 64;
                                if (inputString.charAt(i) == '0') {
                                    exportString.append(StringUtils.repeat(" ", textFieldInputProperLength)).append("*");
                                } else {
                                    cell = currentRow.getCell(colToCopy);
                                    cellText = cell.toString();
                                    if (cell.toString().length() > textFieldInputProperLength) {
                                        errorString.append("DENOMINAZIONE in line ").append(counter).append(" has more than 64 symbols**");
                                        mainPass = false;
                                        errorPass = true;
                                    } else {
                                        stringProperLength = textFieldInputProperLength - cellText.length();
                                        exportString.append(cellText).append(StringUtils.repeat(" ", stringProperLength)).append("*");
                                    }
                                }
                                break;
                        }
                    }
                    if (errorPass) {
                        errorString.append(System.lineSeparator());
                        errorPass = false;
                    }
                    exportString.deleteCharAt(exportString.length() - 1).append(System.lineSeparator());
                }

                if (mainPass) {
                    alertText(alertInformation, "Everything is OK :D Now you can check a file :))", exportString, "\\C0.CRV.SAD.SAD1CAC0.FILEUDAI.txt");
                } else {
                    alertText(alertERROR, "The process occurs some ERRORS. Check CarricamentoMassivoERRORS", errorString, "\\CarricamentoMassivoERRORS.txt");
                }
            } catch (FileNotFoundException e) {
                alertMSG = "click on 'Znajdź plik' and find Carricamento massivo file";
                carricamentoService.errorSet(alertMSG, alertERROR);
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                alertMSG = "something went wrong with date - parsing. Check datas in excel file";
                carricamentoService.errorSet(alertMSG, alertERROR);
                e.printStackTrace();
            }
        }

    }
    private void alertText(Alert alert, String alertTitle, StringBuilder errorString, String outputFileName) {
        alert.setHeaderText(alertTitle);

        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int option = chooser.showSaveDialog(null);
        File pathToSave = new File(chooser.getSelectedFile().getPath());
        String pathToSaveString = pathToSave.toString() + outputFileName;
        if (option == JFileChooser.APPROVE_OPTION) {
            try {
                Files.write(Paths.get(pathToSaveString), errorString.toString().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        alert.show();
    }

    private Task createWorker() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                for (int i = 0; i < 10; i++) {
                    updateProgress(i + 1, 10);
                }
                return true;
            }
        };
    }

    @FXML
    public void handleKeyPressed(KeyEvent keyEvent) {
        Set<Character> set = new HashSet<>();
        System.out.println(keyEvent.getSource());
        carricamentoService.handleKeyPressedMethod(pane, set, carricamentoService);
    }

    public void cleanButton(ActionEvent event) {
        carricamentoService.textFieldsClearMethod(numberOfTextFields, textField, pane);
    }

    @FXML
    private void openFile(File selectedFile) {
        try {

            Desktop.getDesktop().open(selectedFile);
        } catch (IOException ex) {
            Logger.getLogger(FileChooser.class.getName()).log(
                    Level.SEVERE, null, ex
            );
        }
    }
}

