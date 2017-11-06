//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.stage.FileChooser;
//import javafx.stage.Window;
//
//import java.awt.*;
//import java.io.File;
//import java.io.IOException;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//public class CarricamentoService {
//
//    String excelFilePath = "";
//    Window stage;
//
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
//
//    @FXML
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