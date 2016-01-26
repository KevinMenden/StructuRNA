package Selection;

import javafx.scene.control.TextField;

/**
 * Kevin Menden
 * Class keeps track of text selection
 * The variables are bound to the selected text
 */
public class SelectedText {
    //The text field
    private TextField textField;
    //Selected text
    private String selectedText;
    //Anchor position
    private int anchorPosition;
    //Caret position
    private int caretPosition;

    //Create instance
    public SelectedText(TextField textField){
        this.textField = textField;
        initBindings();
    }

    /*
    Set up bindings for text selection
     */
    private void initBindings(){
        textField.selectedTextProperty().addListener(observable -> {
            this.anchorPosition = textField.getAnchor();
            this.caretPosition = textField.getCaretPosition();
        });
    }

    public int getAnchorPosition() {
        return anchorPosition;
    }

    public void setAnchorPosition(int anchorPosition) {
        this.anchorPosition = anchorPosition;
    }

    public int getCaretPosition() {
        return caretPosition;
    }

    public void setCaretPosition(int caretPosition) {
        this.caretPosition = caretPosition;
    }

    public String getSelectedText() {
        return selectedText;
    }

    public void setSelectedText(String selectedText) {
        this.selectedText = selectedText;
    }

    public TextField getTextField() {
        return textField;
    }

    public void setTextField(TextField textField) {
        this.textField = textField;
    }
}
