package pl.rdu.sr.client.presenter;

import static javafx.scene.paint.Color.FIREBRICK;
import static javafx.scene.paint.Color.GREEN;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static pl.rdu.sr.bc.Constant.MILIS_NANOS_FRACTION;
import static pl.rdu.sr.bc.EnumMsg.ERR_WORDCHECKER_WRONGGIVENWORLD;
import static pl.rdu.sr.bc.EnumMsg.MSG_WORDCHECK_BUTTON_CHECK;
import static pl.rdu.sr.bc.EnumMsg.MSG_WORDCHECK_BUTTON_START;
import static pl.rdu.sr.bc.EnumMsg.MSG_WORDCHECK_LABEL_CORRECT_WORD;
import static pl.rdu.sr.client.view.WorldCheckerView.FX_CSS_TEXTFIELD_COLOR_BLACK;
import static pl.rdu.sr.client.view.WorldCheckerView.FX_CSS_TEXTFIELD_COLOR_WHITE;

import java.util.concurrent.Semaphore;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import pl.rdu.sr.bc.bo.impl.EnglishWorldBOImpl;
import pl.rdu.sr.client.model.WorldCheckerModel;
import pl.rdu.sr.client.view.WorldCheckerView;

@Component
@SuppressWarnings("restriction")
public class WorldCheckerPresenter extends AbstractPresenter<WorldCheckerView, WorldCheckerModel> {
    private static final Logger LOG = LoggerFactory.getLogger(WorldCheckerPresenter.class);
            
    @Autowired
    private EnglishWorldBOImpl englishWorldBOImpl;

    @Value("${WorldCheckerPresenter.snapshotFactorSuccess.default}")
    private float snapshotFactorSuccess;
    
    @Value("${WorldCheckerPresenter.snapshotFactorFail.default}")
    private float snapshotFactorFail;
    
    @Value("${WorldCheckerPresenter.snapshotPerCharTime.default}")
    private long snapshotPerCharTime;
    
    @Value("${WorldCheckerPresenter.betweenSnaphotWaitTime.default}")
    private long betweenSnaphotWaitTime;
    
    private Semaphore sem = new Semaphore(1);
    
    private State state = State.FIRST_RUN;
    private enum State {
        FIRST_RUN, CHECK;
    }
    
    @PostConstruct
    public void init() {
        v.getBtn().setOnAction(checkWorldAction);
        v.getUserWord().setOnAction(checkWorldAction);
        
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                v.getUserWord().requestFocus();
            }
        });
    }
    
    private EventHandler<ActionEvent> checkWorldAction = new EventHandler<ActionEvent>() {
        public void handle(javafx.event.ActionEvent event) {
            String btnMsgExpected = MSG_WORDCHECK_BUTTON_CHECK.getMsg();
            String btnMsgActual = v.getBtn().getText();
            
            if (!btnMsgExpected.equals(btnMsgActual)) {
                changeButtonTextToExpected(btnMsgExpected, btnMsgActual);
            } else {
                state = State.CHECK;
            }
            
            String givenNewWord;
            long snapshotTime;
            if (checkWord()) {
                sleep(betweenSnaphotWaitTime, 0);
                givenNewWord = englishWorldBOImpl.getRandomWord();
                v.getGivenWord().setText(givenNewWord);
                snapshotTime = calculateWaitTime(snapshotFactorSuccess, givenNewWord);
                
                if (state == State.CHECK) {
                    v.getUserWord().setText(EMPTY);
                    v.getActiontarget().setFill(GREEN);
                    v.getActiontarget().setText(MSG_WORDCHECK_LABEL_CORRECT_WORD.getMsg(snapshotPerCharTime / MILIS_NANOS_FRACTION));
                }
            } else {
                givenNewWord = v.getGivenWord().getText();
                snapshotTime = calculateWaitTime(snapshotFactorFail, givenNewWord);
                
                v.getActiontarget().setFill(FIREBRICK);
                v.getActiontarget().setText(ERR_WORDCHECKER_WRONGGIVENWORLD.getMsg(snapshotPerCharTime / MILIS_NANOS_FRACTION));
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
            
            return userWord.equals(v.getGivenWord().getText());
        }

        private long calculateWaitTime(float factor, String givenNewWord) {
            long ret = snapshotPerCharTime;
            
            snapshotPerCharTime *= factor;
            
            if (LOG.isDebugEnabled()) {
                LOG.debug("snapshotPerCharTime " + ret / MILIS_NANOS_FRACTION + " ms word length: "
                        + givenNewWord.length());
            }
            
            ret = snapshotPerCharTime * givenNewWord.length();
            return ret;
        }

        private void doWordSnapshot(final long waitTimeNs) {
            final int nanos = (int) (waitTimeNs % MILIS_NANOS_FRACTION);
            final long millis = waitTimeNs / MILIS_NANOS_FRACTION;

            if (LOG.isDebugEnabled()) {
                LOG.debug("waitTime: " + millis + " ms");
            }
            
            try {
                sem.acquire();
            } catch (InterruptedException e) {
                LOG.debug("sem acquire interrupted", e);
            }
            
            new Thread(new Task<Void>() {
                
                @Override 
                public Void call() {
                    Platform.runLater(() ->  v.getGivenWord().setStyle(FX_CSS_TEXTFIELD_COLOR_BLACK));
                    
                    sleep(millis, nanos);
                    
                    Platform.runLater(() ->  v.getGivenWord().setStyle(FX_CSS_TEXTFIELD_COLOR_WHITE));
                    
                    sem.release();
                    
                    return null;
                }
            }).start();
        }
    };


    private void sleep(long millis, int nanos) {
        try {
            Thread.sleep(millis, nanos);
        } catch (InterruptedException ex) {
            LOG.error("wait interruption", ex);
        }
    }
}
