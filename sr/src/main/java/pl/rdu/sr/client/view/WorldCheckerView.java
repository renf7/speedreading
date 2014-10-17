package pl.rdu.sr.client.view;
import static javafx.geometry.HPos.RIGHT;
import static pl.rdu.sr.Constant.HEADER_FONT;
import static pl.rdu.sr.EnumMsg.MSG_WORDCHECK_BUTTON_START;
import static pl.rdu.sr.EnumMsg.MSG_WORDCHECK_LABEL_SNAPSHOT_WORD;
import static pl.rdu.sr.EnumMsg.MSG_WORDCHECK_LABEL_USER_WORD;
import static pl.rdu.sr.EnumMsg.MSG_WORDCHECK_SCENETITLE;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

@SuppressWarnings("restriction")
@Component
public class WorldCheckerView extends AbstractView {
    private final GridPane parent = new GridPane();
    private final Text scenetitle = new Text(MSG_WORDCHECK_SCENETITLE.getMsg());
    private final Label givenWordLabel = new Label(MSG_WORDCHECK_LABEL_SNAPSHOT_WORD.getMsg());
    private final TextField givenWordInput = new TextField();
    private final Label userWordLabel = new Label(MSG_WORDCHECK_LABEL_USER_WORD.getMsg());
    private final TextField userWordInput = new TextField();
    private final Button btn = new Button(MSG_WORDCHECK_BUTTON_START.getMsg());
    private final HBox hbBtn = new HBox(10);
    private final Text actiontarget = new Text();
    
    public static final String FX_CSS_TEXTFIELD_COLOR_WHITE = "-fx-text-inner-color: white;";
    public static final String FX_CSS_TEXTFIELD_COLOR_BLACK = "-fx-text-inner-color: black;";
    
    @PostConstruct
    public void init() {
        
        parent.setAlignment(Pos.CENTER);
        parent.setHgap(10);
        parent.setVgap(10);
        parent.setPadding(new Insets(25, 25, 25, 25));

        scenetitle.setFont(HEADER_FONT);
        parent.add(scenetitle, 0, 0, 2, 1);

        parent.add(givenWordLabel, 0, 1);

        parent.add(getGivenWord(), 1, 1);

        parent.add(userWordLabel, 0, 2);

        parent.add(getUserWord(), 1, 2);

        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(getBtn());
        parent.add(hbBtn, 1, 4);

        parent.add(actiontarget, 0, 6);
        actiontarget.setId("actiontarget");

        GridPane.setColumnSpan(actiontarget, 2);
        GridPane.setHalignment(actiontarget, RIGHT);
    }
    
    @Override
    public Parent getParent() {
        return parent;
    }

    public Button getBtn() {
        return btn;
    }

    public Text getActiontarget() {
        return actiontarget;
    }

    public TextField getUserWord() {
        return userWordInput;
    }

    public TextField getGivenWord() {
        return givenWordInput;
    }
}
