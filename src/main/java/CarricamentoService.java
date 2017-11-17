import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import java.util.Set;

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

    public void textFieldsClearMethod (int numberOfTextFields, TextField textField, Pane pane){
        for (int i = 0; i < numberOfTextFields; i++) {
            textField = (TextField) pane.lookup("#textField" + i);
            textField.setText(StringUtils.EMPTY);
        }
    }

    public void handleKeyPressedMethod (Pane pane, Set set, CarricamentoService carricamentoService){

        for (Node node : pane.getChildren()) {
            if (node instanceof TextField) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                if ((((TextField) node).getText().length() > 1 ||
                        ((TextField) node).getText().length() == 1 &&
                                !(((TextField) node).getText().toLowerCase().matches("[a-n]|[0]")))) {
                    String unField = "You can put only letters from a to n or 0!!!!! A to N are OK as well:)";
                    carricamentoService.errorSet(unField, alert);
                    ((TextField) node).setText(StringUtils.EMPTY);
                    break;
                } else if (((TextField) node).getText().length() == 1) {
                    if (set.contains(((TextField) node).getText().charAt(0)) && !(((TextField) node).getText().matches("[0]"))) {
                        String duplicate = "You duplicated the value";

                        carricamentoService.errorSet(duplicate, alert);
                        ((TextField) node).setText(StringUtils.EMPTY);
                    } else {
                        set.add(((TextField) node).getText().charAt(0));
                    }
                }
            }
        }
    }

}



