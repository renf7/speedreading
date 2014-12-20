package pl.rdu.sr.bc;

import java.nio.charset.Charset;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Zbiór wszystkich stałych aplikacji.
 * 
 * @author rafalszymonduda@gmail.com
 *
 */
@SuppressWarnings("restriction")
public interface Constant {

    /**
     * Strona kodowa w aplikacji.
     */
    String ENCODING = "UTF-8";

    /**
     * Charset w apliacji.
     */
    Charset CHARSET = Charset.forName(ENCODING);

    /**
     * Ścieżka do pliku ze słowami angielskimi.
     */
    String ENG_WORDS_FILE = "corncob_lowercase.txt";
    
    /**
     * Czciąka nagłówka.
     */
    Font HEADER_FONT = Font.font("Tahoma", FontWeight.NORMAL, 20);
    
    /**
     * Ułamek jaki nanosakundy stanowią milisekundy.
     */
    int MILIS_NANOS_FRACTION = 1000000;
}
