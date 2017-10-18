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
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            openFile(selectedFile);
            path = selectedFile.getPath();
        }
    }


    @FXML
    public void goButtonPressed(ActionEvent event) {
        boolean hasEmpty = true;

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
            if (hasEmpty == true) {
                String exportString = "";
                System.out.println("save the world");
                try {
                    FileInputStream excelFile = new FileInputStream(new File(path));
                    Workbook workbook = new XSSFWorkbook(excelFile);
                    Sheet datatypeSheet = workbook.getSheetAt(0);
                    Iterator<Row> iterator = datatypeSheet.iterator();
                    iterator.next();

                    while (iterator.hasNext()) {
                        Row currentRow = iterator.next();
                     //   Iterator<Cell> cellIterator = currentRow.iterator();
                            for (int i = 0; i < 14; i++) {
                                textField = (TextField) pane.lookup("#textField" + i);
                                int colToCopy = ((int) textField.getText().toLowerCase().charAt(0)) - 97;

                                    Cell cell = currentRow.getCell(colToCopy);
                                    exportString+=cell+"--";
                                    System.out.println(cell);

                            
                        }
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("1");
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

//        fileChooser.getExtensionFilters().addAll(
//                new FileChooser.ExtensionFilter("All Images", "*.*"),
//                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
//                new FileChooser.ExtensionFilter("GIF", "*.gif"),
//                new FileChooser.ExtensionFilter("BMP", "*.bmp"),
//                new FileChooser.ExtensionFilter("PNG", "*.png")

}

