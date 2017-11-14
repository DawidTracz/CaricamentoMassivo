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

    private int textFieldInputProperLength;

    String cellText = StringUtils.EMPTY;

    @FXML
    public void buttonPressed(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Znajdź plik");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All FILES", "*.*"),
                new FileChooser.ExtensionFilter("XLS", "*.xls"),
                new FileChooser.ExtensionFilter("XLSX", "*.xlsx"));
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
                System.out.println(node.getId() + " nie jest wypelniony");
                alertERROR = new Alert(Alert.AlertType.ERROR);
                alertERROR.setHeaderText(unField);
                alertERROR.show();
                hasEmpty = false;
                break;
            }
        }

        for (int i = 0; i < 14; i++) {
            textField = (TextField) pane.lookup("#textField" + i);
            inputString.append(textField.getText().toLowerCase());
        }


        if (inputString.charAt(0) == '0' ||
                inputString.charAt(2) == '0' ||
                inputString.charAt(4) == '0' ||
                inputString.charAt(6) == '0' ||
                inputString.charAt(7) == '0' ||
                inputString.charAt(8) == '0') {
            String unField = "Textfields with PROGR_UDA \n FILIALE \n TIPO_DOC \n ANNI_CONS \n DATA INIZIO \n DATA FINE        cannot posses value '0', change it";
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
                    for (int i = 0; i < 14; i++) {
                        colToCopy = inputString.charAt(i) - 97;  // zamien 97 na chara
                        switch (i) {
                            case 0:
                                //PROG_UDA
                                cell = currentRow.getCell(colToCopy);


                                if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                    cellText = String.valueOf((int) cell.getNumericCellValue());
                                }
                                if (cell.getCellTypeEnum() == CellType.STRING) {
                                    cellText = cell.getStringCellValue();
                                }
                                if (cellText.length() != 7) {
                                    errorString.append("UDA in line" + counter + "does not have 7 digits**");
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
                                        errorString.append("Descrizione Filliale in line " + counter + " is to long**");
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
                                cell = currentRow.getCell(colToCopy);
                                textFieldInputProperLength = 5;
                                if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                    cellText = String.valueOf((int) cell.getNumericCellValue());
                                }
                                if (cell.getCellTypeEnum() == CellType.STRING) {
                                    cellText = cell.getStringCellValue();
                                }
                                if (cellText.length() > textFieldInputProperLength) {
                                    System.out.println(cellText);
                                    errorString.append("TipoDOC and Filiale in line " + counter + " has more than 5 numbers**");
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
                                        errorString.append("DISLOCAZIONE in line" + counter + " has more than 3 numbers**");
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
                                stringProperLength = 80;
                                if (inputString.charAt(i) == '0') {
                                    exportString.append(StringUtils.repeat(" ", stringProperLength)).append("*");
                                } else {
                                    cell = currentRow.getCell(colToCopy);
                                    if (cell.toString().length() > stringProperLength) {
                                        errorString.append("Some DescTipo or Note in line " + counter + " is too long**");
                                        mainPass = false;
                                        errorPass = true;
                                    } else {
                                        stringProperLength = stringProperLength - cell.toString().length();
                                        exportString.append(cell.toString()).append(StringUtils.repeat(" ", stringProperLength)).append("*");
                                    }
                                }
                                break;
                            case 6:
                                //ANNI CONS
                                cell = currentRow.getCell(colToCopy);
                                textFieldInputProperLength = 2;


                                if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                    cellText = String.valueOf((int) cell.getNumericCellValue());
                                }
                                if (cell.getCellTypeEnum() == CellType.STRING) {
                                    cellText = cell.getStringCellValue();
                                }
                                if (cellText.length() > textFieldInputProperLength) {
                                    errorString.append("ANNI CONSERVATIONI in line " + counter + " has more than 2 numbers**");
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
                                    errorString.append("Data Fine in line " + counter + " is before Data Inizio**");
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
                                    cellText = String.valueOf(((long) cell.getNumericCellValue()));
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
                                    cellText=cell.toString();
                                    if (cell.toString().length() > textFieldInputProperLength) {
                                        errorString.append("DENOMINAZIONE in line " + counter + " has more than 64 symbols**");
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
                    alertMSG = "Everything is OK :D Now you can check a file :))";
                    alertInformation.setHeaderText(alertMSG);

                    JFileChooser chooser = new JFileChooser();
                    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    int option = chooser.showSaveDialog(null);
                    File pathToSave = new File(chooser.getSelectedFile().getPath());
                    String pathToSaveString = pathToSave.toString() + "\\C0.CRV.SAD.SAD1CAC0.FILEUDAI.txt";
                    if (option == JFileChooser.APPROVE_OPTION) {
                        try {
                            Files.write(Paths.get(pathToSaveString), exportString.toString().getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    alertInformation.show();
                } else {
                    alertMSG = "The process occurs some ERRORS. Check CarricamentoMassivoERRORS";
                    alertERROR.setHeaderText(alertMSG);

                    JFileChooser chooser = new JFileChooser();
                    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    int option = chooser.showSaveDialog(null);
                    File pathToSave = new File(chooser.getSelectedFile().getPath());
                    String pathToSaveString = pathToSave.toString() + "\\CarricamentoMassivoERRORS.txt";
                    if (option == JFileChooser.APPROVE_OPTION) {
                        try {
                            Files.write(Paths.get(pathToSaveString), errorString.toString().getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    alertERROR.show();
                }
            } catch (FileNotFoundException e) {
                alertMSG = "click on 'Znajdź plik' and find Carricamento massivo file";
                alertERROR.setHeaderText(alertMSG);
                alertERROR.show();
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                alertMSG = "something went wrong with date - parsing. Check datas in excel file";
                alertERROR.setHeaderText(alertMSG);
                alertERROR.show();
                e.printStackTrace();
                e.printStackTrace();
            }
        }

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

        for (Node node : pane.getChildren()) {
            if (node instanceof TextField) {
                if ((((TextField) node).getText().length() > 1 ||
                        ((TextField) node).getText().length() == 1 &&
                                !(((TextField) node).getText().toLowerCase().matches("[a-n]|[0]")))) {
                    String unField = "You can put only letters from a to n or 0!!!!! A to N are OK as well:)";
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(unField);
                    alert.show();
                    ((TextField) node).setText(StringUtils.EMPTY);
                    break;
                } else if (((TextField) node).getText().length() == 1) {
                    if (set.contains(((TextField) node).getText().charAt(0)) && !(((TextField) node).getText().matches("[0]"))) {
                        String duplicate = "You duplicated the value";
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText(duplicate);
                        alert.show();
                        ((TextField) node).setText(StringUtils.EMPTY);
                    } else {
                        set.add(((TextField) node).getText().charAt(0));
                    }
                }
            }
        }
    }

    public void cleanButton(ActionEvent event) {
        for (int i = 0; i < 14; i++) {
            textField = (TextField) pane.lookup("#textField" + i);
            textField.setText(StringUtils.EMPTY);
        }
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

