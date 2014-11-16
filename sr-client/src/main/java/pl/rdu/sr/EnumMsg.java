package pl.rdu.sr;
import static pl.rdu.sr.EnumSpringCtx.CTX_MSG;

import java.util.Locale;

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
    
    
    public String getMsg(Object ...params) {
        return getMsg(Locale.getDefault(), params);
    }
    
    public String getMsg(Locale locale, Object... params) {
        return CTX_MSG.get().getMessage(super.toString(), params, locale);
    }
}
