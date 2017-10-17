import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.security.Key;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Controller {

    private Window stage;
    @FXML
    private Pane pane;
    @FXML
    private javafx.scene.control.Button popUp;


    @FXML
    public void buttonPressed(ActionEvent event) {
        System.out.println("Hello Wordld");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Znajdź plik");
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            openFile(selectedFile);
            String path = selectedFile.getPath();
            System.out.println(path);
        

        }
    }


    @FXML
    public void goButtonPressed(ActionEvent event) {
        boolean hasEmpty = true;

        for (Node node : pane.getChildren()) {
            if (node instanceof TextField && ((TextField) node).getText().length() == 0) {
                String unField = node.getId() + " nie jest wypelniony";
                System.out.println(node.getId() + " nie jest wypelniony");
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sample/PopUp2.fxml"));
                Parent root1 = null;
                try {
                    root1 = (Parent) fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Stage stage = new Stage();
                stage.setScene(new Scene(root1));
                stage.show();
                hasEmpty = false;
                break;
            }
        }
        if (hasEmpty == true) {
            //working on excel












            System.out.println("save the world");
        }
    }


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
        for (Node node : pane.getChildren()) {
            if ((node instanceof TextField) &&
                    (((TextField) node).getText().length() > 1 ||
                            ((TextField) node).getText().length() == 1 && !Character.isAlphabetic(((TextField) node).getText().charAt(0)))) {
                System.out.println("za długie lub nie literka");

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sample/PopUp.fxml"));
                Parent root1 = null;
                try {
                    root1 = (Parent) fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Stage stage = new Stage();
                stage.setScene(new Scene(root1));
                stage.show();
                ((TextField) node).setText("");
            }
        }
    }

    public void closeWindow(ActionEvent event) {
        Stage stage = (Stage) popUp.getScene().getWindow();
        stage.close();
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

