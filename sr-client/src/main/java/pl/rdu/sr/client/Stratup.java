package pl.rdu.sr.client;

import static pl.rdu.sr.bc.EnumMsg.MSG_APPLICATION_WINDOW_TITLE;
import static pl.rdu.sr.client.EnumSpringCtx.CTX;

import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.rdu.sr.client.view.WorldCheckerView;
/**
 * Klasa rozpoczynająca działanie aplikacji.
 *      
 * @author renf7_000
 *
 */
@SuppressWarnings("restriction")
public final class Stratup extends Application {

    public static void main(String[] args) throws IOException {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        WorldCheckerView wcv = CTX.get().getBean(WorldCheckerView.class);
        primaryStage.setTitle(MSG_APPLICATION_WINDOW_TITLE.getMsg());
        
        Scene scene = new Scene(wcv.getParent(), 300, 275);
        primaryStage.setScene(scene);
        primaryStage.show();
        
    }
}
