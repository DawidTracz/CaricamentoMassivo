import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.awt.Desktop;
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
    private Window stage;
    @FXML
    private Pane pane;
    String path = "";
    @FXML
    private Node node;
    @FXML
    private TextField textField;

    @FXML
    public void buttonPressed(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Znajdź plik");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All FILES", "*.*"),
                new FileChooser.ExtensionFilter("XLS", "*.xls"),
                new FileChooser.ExtensionFilter("XLSX", "*.xlsx"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            openFile(selectedFile);
            path = selectedFile.getPath();
        }
    }

    @FXML
    public void goButtonPressed(ActionEvent event) {
        boolean hasEmpty = true;
        StringBuilder exportString = new StringBuilder("");
        StringBuilder inputString = new StringBuilder("");
        for (Node node : pane.getChildren()) {
            if (node instanceof TextField && ((TextField) node).getText().length() == 0) {
                String unField = node.getId() + " nie jest wypelniony";
                System.out.println(node.getId() + " nie jest wypelniony");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(unField);
                alert.show();
                hasEmpty = false;
                break;
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
                String unField = "PROGR_UDA \n FILIALE \n TIPO_DOC \n ANNI_CONS \n DATA INIZIO \n DATA FINE        cannot posses value '0', change it";
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(unField);
                alert.show();
                hasEmpty = false;
                break;
            }
        }


        if (hasEmpty == true) {
            try {
                FileInputStream excelFile = new FileInputStream(new File(path));
                Workbook workbook = new XSSFWorkbook(excelFile);
                Sheet datatypeSheet = workbook.getSheetAt(0);
                Iterator<Row> iterator = datatypeSheet.iterator();
                iterator.next();
                Boolean mainAllert = true;

                while (iterator.hasNext() && mainAllert == true) {
                    Row currentRow = iterator.next();
                    for (int i = 0; i < 14; i++) {
                        int colToCopy = inputString.charAt(i) - 97;
                        Cell cell = currentRow.getCell(colToCopy);
                        int stringProperLength;
                        String alertMSG;
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        switch (i) {
                            case 0:
                                //PROG_UDA
                                if ((int) cell.getNumericCellValue() > 9999999 || (int) cell.getNumericCellValue() < 1000000 || cell.toString().length() > 9 || cell.toString().length() < 7) { //symbols as 1234567.0
                                    alertMSG = "UDA cannot have more or less than 7 digits";
                                    alert.setHeaderText(alertMSG);
                                    alert.show();
                                    mainAllert = false;
                                    break;
                                } else if (cell.toString().length() == 9) {
                                    exportString.append(String.valueOf((int) cell.getNumericCellValue()) + "*");
                                    break;
                                } else if (cell.toString().length() == 7) {
                                    exportString.append(cell.toString() + "*");
                                    break;
                                }
                            case 1:
                                //DESCRIZIONE FILLIALE
                                if (cell.toString().length() > 50) {
                                    alertMSG = "Some Descrizione Filliale is to long";
                                    alert.setHeaderText(alertMSG);
                                    alert.show();
                                    mainAllert = false;
                                    break;
                                } else {
                                    stringProperLength = 50 - cell.toString().length();
                                    exportString.append(cell.toString() + StringUtils.repeat(" ", stringProperLength) + "*");
                                }
                                break;
                            case 2:
                            case 4:
                                //Filiale
                                //TipoDoc
                                if ((int) cell.getNumericCellValue() > 99999) {
                                    alertMSG = "TipoDOC and Filiale can have maximum 5 numbers";
                                    alert.setHeaderText(alertMSG);
                                    alert.show();
                                    mainAllert = false;
                                    break;
                                } else {
                                    stringProperLength = 5 - String.valueOf((int) cell.getNumericCellValue()).length();
                                    exportString.append(StringUtils.repeat("0", stringProperLength) + String.valueOf((int) cell.getNumericCellValue()) + "*");

                                    break;
                                }
                            case 3:
                                //Dislocazione

                                if (inputString.charAt(i) == 0) {
                                    exportString.append(StringUtils.repeat(" ", 3) + "*");
                                    break;
                                } else if (cell.getStringCellValue().length() > 3) {
                                    alertMSG = "DISLOCAZIONE can have maximum 3 numbers";
                                    alert.setHeaderText(alertMSG);
                                    alert.show();
                                    mainAllert = false;
                                    break;
                                } else {
                                    stringProperLength = 3 - cell.getStringCellValue().length();
                                    exportString.append(StringUtils.repeat("0", stringProperLength) + cell.getStringCellValue() + "*");

                                    break;
                                }
                            case 5:
                            case 9:
                                //DESC TIPO
                                //NOTE
                                if (inputString.charAt(i) == 0) {
                                    exportString.append(StringUtils.repeat(" ", 80) + "*");
                                    break;
                                } else if (cell.toString().length() > 80) {
                                    alertMSG = "Some DescTipo or Note is too long";
                                    alert.setHeaderText(alertMSG);
                                    alert.show();
                                    mainAllert = false;
                                    break;
                                } else {
                                    stringProperLength = 80 - cell.toString().length();
                                    exportString.append(cell.toString() + StringUtils.repeat(" ", stringProperLength) + "*");
                                    break;
                                }
                            case 6:
                                //ANNI CONS
                                if ((int) cell.getNumericCellValue() > 99) {
                                    alertMSG = "ANNI CONSERVATIONI CAN BE MAX 2 NUMBERED";
                                    alert.setHeaderText(alertMSG);
                                    alert.show();
                                    mainAllert = false;
                                    break;
                                } else {
                                    stringProperLength = 2 - String.valueOf((int) cell.getNumericCellValue()).length();
                                    exportString.append(StringUtils.repeat("0", stringProperLength) + String.valueOf((int) cell.getNumericCellValue()) + "*");

                                    break;
                                }
                            case 7:
                            case 8:
                                //Data INIZIO
                                //DATA FINE
                                DateFormat shortFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                                DateFormat mediumFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                                String cellWithDate = cell.toString();
                                String shortDate = shortFormat.format(mediumFormat.parse(cellWithDate));

                                exportString.append(shortDate + "*");
                                break;
                            case 10:
                            case 11:
                            case 13:
                                //NDG
                                //codice practica
                                //NDG1
                                //16 znakow
                                if (inputString.charAt(i) == 0) {
                                    exportString.append(StringUtils.repeat("0", 16) + "*");
                                    break;
                                } else {
                                    stringProperLength = 16 - String.valueOf(((long) cell.getNumericCellValue())).length();
                                    exportString.append(StringUtils.repeat("0", stringProperLength) + ((long) cell.getNumericCellValue()) + "*");
                                    break;
                                }
                            case 12:
                                //Denominazione
                                if (inputString.charAt(i) == 0) {
                                    exportString.append(StringUtils.repeat(" ", 64) + "*");
                                    break;
                                } else if (cell.toString().length() > 64) {
                                    alertMSG = "DENOMINAZIONE może mieć max 64 znaki";
                                    alert.setHeaderText(alertMSG);
                                    alert.show();
                                    mainAllert = false;
                                    break;
                                } else {
                                    stringProperLength = 64 - cell.toString().length();
                                    exportString.append(cell.toString() + StringUtils.repeat(" ", stringProperLength) + "*");
                                    break;
                                }
                        }
                        if (!mainAllert) {
                            break;
                        }
                    }
                    if (!mainAllert) {
                        break;
                    }
                    exportString.deleteCharAt(exportString.length() - 1).append(System.lineSeparator());
                }
                if (mainAllert) {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    int option = chooser.showSaveDialog(null);
                    File path2 = new File(chooser.getSelectedFile().getPath());
                    String path2String = path2.toString() + "\\test.txt";
                    if (option == JFileChooser.APPROVE_OPTION) {
                        try {
                            Files.write(Paths.get(path2String), exportString.toString().getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
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

    @FXML
    public void handleKeyPressed(KeyEvent keyEvent) {

        Set<Character> set = new HashSet<Character>();

        for (Node node : pane.getChildren()) {
            if (node instanceof TextField) {
                if ((((TextField) node).getText().length() > 1 ||
                        ((TextField) node).getText().length() == 1 && !(((TextField) node).getText().toLowerCase().matches("[a-n]|[0]")))) {

                    String unField = "You can put only letters from a to n or 0!!!!! A to N are OK as well:)";
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(unField);
                    alert.show();
                    ((TextField) node).setText("");
                    break;
                } else if (((TextField) node).getText().length() == 1) {
                    if (set.contains(((TextField) node).getText().charAt(0)) && !(((TextField) node).getText().matches("[0]"))) {
                        String duplicate = "You duplicated values";
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText(duplicate);
                        alert.show();
                        ((TextField) node).setText("");
                    } else {
                        set.add(((TextField) node).getText().charAt(0));
                    }
                }
            }
        }
    }

}

