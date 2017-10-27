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
    @FXML
    private javafx.scene.control.Button popUp;
    String path = "";
    @FXML
    private Node node;
    @FXML
    private TextField textField;

    @FXML
    public void buttonPressed(ActionEvent event) {
        System.out.println("Hello Wordld");
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
        String exportString = "";
        List<String> checkZeroList = new LinkedList<>();

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
            textField = (TextField) pane.lookup("#textField0");
            checkZeroList.add(textField.getText());
            textField = (TextField) pane.lookup("#textField2");
            checkZeroList.add(textField.getText());
            textField = (TextField) pane.lookup("#textField4");
            checkZeroList.add(textField.getText());
            textField = (TextField) pane.lookup("#textField6");
            checkZeroList.add(textField.getText());
            textField = (TextField) pane.lookup("#textField7");
            checkZeroList.add(textField.getText());
            textField = (TextField) pane.lookup("#textField8");

            if (checkZeroList.contains("0")) {
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
                Boolean caseCheck = true;
                while (iterator.hasNext()) {
                    Row currentRow = iterator.next();
                    for (int i = 0; i < 14; i++) {
                        textField = (TextField) pane.lookup("#textField" + i);
                        int colToCopy = (int) textField.getText().toLowerCase().charAt(0) - 97;
                        Cell cell = currentRow.getCell(colToCopy);
                        int stringProperLength;
                        String alertMSG = "";
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        switch (i) {
                            case 0:
                                //PROG_UDA
                                if (cell.toString().substring(0, cell.toString().length() - 2).length() != 7) {
                                    alertMSG = "At least 1 PROGR UDA does not have 7 symbols";
                                    alert.setHeaderText(alertMSG);
                                    alert.show();
                                    mainAllert = false;
                                    break;
                                }
                                exportString += cell.toString().substring(0, cell.toString().length() - 2) + "*";
                                break;
                            case 1:
                                //DESCRIZIONE FILLIALE
//                                if (exportString.length() > 52) {
//                                    alertMSG = "Some Descrizione Filliale is to long";
//                                    alert.setHeaderText(alertMSG);
//                                    alert.show();
//                                    mainAllert = false;
//                                    caseCheck = false;
//
//
//                                    break;
//                                }

                                stringProperLength = 50 - cell.toString().length();
                                exportString += cell.toString() + StringUtils.repeat(" ", stringProperLength) + "*";

                                break;
                            case 2:
                            case 4:
                                //Filiale
                                //TipoDoc
                                stringProperLength = 5 - cell.toString().substring(0, cell.toString().length() - 2).length();
                                exportString += StringUtils.repeat("0", stringProperLength) + cell.toString().substring(0, cell.toString().length() - 2) + "*";
//                                if (exportString.length() > 6) {
//                                    alertMSG = "Some Descrizione Filliale is to long";
//                                    alert.setHeaderText(alertMSG);
//                                    alert.show();
//                                    mainAllert = false;
//                                    break;
//                                }
                                break;
                            case 3:
                                //Dislocazione
                                stringProperLength = 3 - cell.toString().length();
                                exportString += StringUtils.repeat(" ", stringProperLength) + cell.toString() + "*";
//                                if (exportString.length() > 4) {
//                                    alertMSG = "Some Dislocazione is to long";
//                                    alert.setHeaderText(alertMSG);
//                                    alert.show();
//                                    mainAllert = false;
//                                    break;
//                                }
                                break;
                            case 5:
                            case 9:
                                //DESC TIPO
                                //NOTE
                                stringProperLength = 80 - cell.toString().length();
                                exportString += cell.toString() + StringUtils.repeat(" ", stringProperLength) + "*";
                                break;
                            case 6:
                                //ANNI CONS
                                exportString += cell.toString().substring(0, cell.toString().length() - 2) + "*";
//                                if (exportString.length() > 3) {
//                                    alertMSG = "ANNI CONSERVAZIONI is to long";
//                                    alert.setHeaderText(alertMSG);
//                                    alert.show();
//                                    mainAllert = false;
//                                    break;
//                                }
                                break;
                            case 7:
                            case 8:
                                //Data INIZIO
                                //DATA FINE

                                DateFormat shortFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                                DateFormat mediumFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                                String cellWithDate = cell.toString();
                                String shortDate = shortFormat.format(mediumFormat.parse(cellWithDate));

                                exportString += shortDate + "*";
                                break;
                            case 10:
                            case 11:
                            case 13:
                                //NDG
                                //codice practica
                                //NDG1
                                //16 znakow
                                if (textField.equals(0)) {
                                    exportString += StringUtils.repeat("0", 16) + "*";

                                } else {
                                    String bla = "" + Double.valueOf(cell.toString()).longValue();
                                    stringProperLength = 16 - bla.length();
                                    exportString += StringUtils.repeat("0", stringProperLength) + Double.valueOf(cell.toString()).longValue() + "*";
                                }
                                break;
                            case 12:
                                stringProperLength = 64 - cell.toString().length();
                                exportString += cell.toString() + StringUtils.repeat(" ", stringProperLength) + "*";
//                                if (exportString.length() > 65) {
//                                    alertMSG = "ANNI CONSERVAZIONI is to long";
//                                    alert.setHeaderText(alertMSG);
//                                    alert.show();
//                                    mainAllert = false;
//                                    break;
//                                }
                                break;
                        }
                        if (!mainAllert) {
                            break;
                        }
                    }
                    if (!mainAllert) {
                        break;
                    }
                    exportString = exportString.substring(0, exportString.length() - 1) + '\n';
                }
                if (mainAllert) {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    int option = chooser.showSaveDialog(null);
                    File path2 = new File(chooser.getSelectedFile().getPath());
                    if (option == JFileChooser.APPROVE_OPTION) {
                        try {
                            File file = new File(path2 + "/" + "test1.txt");
                            FileWriter fileWriter = new FileWriter(file);
                            fileWriter.write(exportString);
                            fileWriter.flush();
                            fileWriter.close();
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

                    System.out.println("za długie lub nie literka");
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

