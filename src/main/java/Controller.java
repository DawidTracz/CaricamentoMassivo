import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.awt.Desktop;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
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
        }
        if (hasEmpty == true) {
            try {
                FileInputStream excelFile = new FileInputStream(new File(path));
                Workbook workbook = new XSSFWorkbook(excelFile);
                Sheet datatypeSheet = workbook.getSheetAt(0);
                Iterator<Row> iterator = datatypeSheet.iterator();
                iterator.next();
                while (iterator.hasNext()) {
                    Row currentRow = iterator.next();
                    for (int i = 0; i < 14; i++) {
                        textField = (TextField) pane.lookup("#textField" + i);
                        int colToCopy = (int) textField.getText().toLowerCase().charAt(0) - 97;
                        Cell cell = currentRow.getCell(colToCopy);
                        int stringProperLength;
                        switch (i) {
                            case 0:
                                if (cell.toString().substring(0, cell.toString().length() - 2).length() != 7) {
                                    String alertMSG = "At least 1 PROGR UDA does not have 7 symbols";
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setHeaderText(alertMSG);
                                    alert.show();
                                    break;
                                }
                                exportString += cell.toString().substring(0, cell.toString().length() - 2) + "*";
                                break;
                            case 1:
                                stringProperLength = 50 - cell.toString().length();
                                exportString += cell.toString() + StringUtils.repeat(" ", stringProperLength) + "*";
                                break;
                            case 2:
                            case 4:
                                stringProperLength = 5 - cell.toString().length();
                                exportString += StringUtils.repeat("0", stringProperLength) + cell.toString().substring(0, cell.toString().length() - 2) + "*";
                                break;
                            case 3:
                                stringProperLength = 3 - cell.toString().length();
                                exportString += StringUtils.repeat(" ", stringProperLength) + cell.toString() + "*";
                                break;
                            case 5:
                            case 9:
                                stringProperLength = 80 - cell.toString().length();
                                exportString += cell.toString() + StringUtils.repeat(" ", stringProperLength) + "*";
                                break;
                            case 6:
                                exportString += cell.toString().substring(0, cell.toString().length() - 2) + "*";
                                break;
                            case 7:
                            case 8:
                                exportString += cell.toString() + "*";
                                break;
                            case 10:
                            case 11:
                            case 13:
                                //NDG
                                //codice practica
                                //NDG1
                                exportString += cell.toString() + "*";
                                break;
                            case 12:
                                stringProperLength = 64 - cell.toString().length();
                                exportString += cell.toString() + StringUtils.repeat(" ", stringProperLength) + "*";
                        }

                    }
                    exportString += '\n';
                }


                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int option = chooser.showSaveDialog(null);
                File path2 = new File(chooser.getSelectedFile().getPath());
                if (option == JFileChooser.APPROVE_OPTION)
                {

                    try {
                        File file = new File(path2+"/"+"test1.txt");
                        FileWriter fileWriter = new FileWriter(file);
                        fileWriter.write(exportString);
                        fileWriter.flush();
                        fileWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


            }




            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
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

    public void handleKeyPressed(KeyEvent keyEvent) {
        for (Node node : pane.getChildren()) {
            if ((node instanceof TextField) &&
                    (((TextField) node).getText().length() > 1 ||
                            ((TextField) node).getText().length() == 1 && !Character.isAlphabetic(((TextField) node).getText().charAt(0)))) {
                System.out.println("za długie lub nie literka");
                String unField = "You can put only numerical and single symbol values!!!!!";
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(unField);
                alert.show();
                ((TextField) node).setText("");
            }
        }

    }


//    @FXML
//    public static void udaLimit(final TextField tf, final int maxLength) {
//        tf.textProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
//                if (tf.getText().length() > maxLength) {
//                    String s = tf.getText().substring(0, maxLength);
//                    tf.setText(s);
//                }
//            }
//        });
//    }


}

