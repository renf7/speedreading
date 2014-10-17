package pl.rdu.sr.client.presenter;

import static javafx.scene.paint.Color.FIREBRICK;
import static javafx.scene.paint.Color.GREEN;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static pl.rdu.sr.EnumMsg.ERR_WORDCHECKER_WRONGGIVENWORLD;
import static pl.rdu.sr.EnumMsg.MSG_WORDCHECK_BUTTON_CHECK;
import static pl.rdu.sr.EnumMsg.MSG_WORDCHECK_BUTTON_START;
import static pl.rdu.sr.EnumMsg.MSG_WORDCHECK_LABEL_CORRECT_WORD;
import static pl.rdu.sr.client.view.WorldCheckerView.FX_CSS_TEXTFIELD_COLOR_BLACK;
import static pl.rdu.sr.client.view.WorldCheckerView.FX_CSS_TEXTFIELD_COLOR_WHITE;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.rdu.sr.bo.impl.EnglishWorldBOImpl;
import pl.rdu.sr.client.model.WorldCheckerModel;
import pl.rdu.sr.client.view.WorldCheckerView;

@Component
@SuppressWarnings("restriction")
public class WorldCheckerPresenter extends AbstractPresenter<WorldCheckerView, WorldCheckerModel> {
    private static final Logger LOG = LoggerFactory.getLogger(WorldCheckerPresenter.class);
    private static final float WAIT_FACTOR_SUCCES = 1.1f;
    private static final float WAIT_FACTOR_FAIL = 0.9f;
            
    @Autowired
    private EnglishWorldBOImpl englishWorldBOImpl;
    
    private long snapshotPerCharTime;
    
    @PostConstruct
    public void init() {
        v.getBtn().setOnAction(checkWorldAction);
        v.getUserWord().setOnAction(checkWorldAction);
    }
    
    EventHandler<ActionEvent> checkWorldAction = new EventHandler<ActionEvent>() {
        @Override
        public void handle(javafx.event.ActionEvent event) {
            String btnMsgExpected = MSG_WORDCHECK_BUTTON_CHECK.getMsg();
            String btnMsgActual = v.getBtn().getText();
            
            boolean success = true;
            if (!btnMsgExpected.equals(btnMsgActual)) {
                changeButtonTextToExpected(btnMsgExpected, btnMsgActual);
            } else {
                success = checkWord();
            }
            
            String givenNewWord;
            long snapshotTime;
            if (success) {
                givenNewWord = englishWorldBOImpl.getRandomWord();
                v.getGivenWord().setText(givenNewWord);
                snapshotTime = calculateWaitTime(WAIT_FACTOR_SUCCES, givenNewWord);
            } else {
                givenNewWord = v.getGivenWord().getText();
                snapshotTime = calculateWaitTime(WAIT_FACTOR_FAIL, givenNewWord);
            }
            
            doWordSnapshot(snapshotTime);
            
            v.getUserWord().requestFocus();
        }

        private void changeButtonTextToExpected(String btnMsgExpected, String btnMsgActual) {
            String btnMsgStart = MSG_WORDCHECK_BUTTON_START.getMsg();
            if (!btnMsgStart.equals(btnMsgActual)) {
                LOG.warn("Expected btn text: " + btnMsgStart, new IllegalStateException());
            }
            v.getBtn().setText(btnMsgExpected);
        }

        private boolean checkWord() {
            String userWord = v.getUserWord().getText();
            
            if (userWord.equals(v.getGivenWord().getText())) {
                v.getUserWord().setText(EMPTY);
                v.getActiontarget().setFill(GREEN);
                v.getActiontarget().setText(MSG_WORDCHECK_LABEL_CORRECT_WORD.getMsg());
                
                return true;
            } else {
                v.getActiontarget().setFill(FIREBRICK);
                v.getActiontarget().setText(ERR_WORDCHECKER_WRONGGIVENWORLD.getMsg(userWord));
                
                return false;
            }
        }

        private long calculateWaitTime(float factor, String givenNewWord) {
            long ret = snapshotPerCharTime;
            
            if (snapshotPerCharTime <= 0) {
                snapshotPerCharTime = 100L;
            } else {
                snapshotPerCharTime *= factor;
            }
            
            ret = snapshotPerCharTime * givenNewWord.length();
            return ret;
        }

        private void doWordSnapshot(final long waitTimeMs) {
            new Thread(new Task<Void>() {
                
                @Override 
                public Void call() {
                    Platform.runLater(() ->  v.getGivenWord().setStyle(FX_CSS_TEXTFIELD_COLOR_BLACK));
                    
                    try {
                        Thread.sleep(waitTimeMs);
                    } catch (InterruptedException ex) {
                        LOG.error("wait interruption", ex);
                    }
                    
                    Platform.runLater(() ->  v.getGivenWord().setStyle(FX_CSS_TEXTFIELD_COLOR_WHITE));
                    
                    return null;
                }
            }).start();
        }
    };

}
