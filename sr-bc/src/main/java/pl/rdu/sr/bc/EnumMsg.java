package pl.rdu.sr.bc;
import java.util.Locale;

import org.springframework.context.MessageSource;

public enum EnumMsg {
    MSG_APPLICATION_WINDOW_TITLE,
    
    MSG_WORDCHECK_SCENETITLE,
    MSG_WORDCHECK_LABEL_SNAPSHOT_WORD,
    MSG_WORDCHECK_LABEL_USER_WORD,
    MSG_WORDCHECK_LABEL_CORRECT_WORD,
    MSG_WORDCHECK_BUTTON_CHECK,
    MSG_WORDCHECK_BUTTON_START,
    
    ERR_WORDCHECKER_WRONGGIVENWORLD,
    ;
    
    private static MessageSource ms;
    
    public static void init(MessageSource ms) {
        EnumMsg.ms = ms;
    }
    
    public String getMsg(Object ...params) {
        return getMsg(Locale.getDefault(), params);
    }
    
    public String getMsg(Locale locale, Object... params) {
        return ms.getMessage(super.toString(), params, locale);
    }
}
