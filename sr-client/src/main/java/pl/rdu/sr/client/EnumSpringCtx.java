package pl.rdu.sr.client;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Reprezentacja kontekstu springowego w aplikacji.
 * 
 * @author rafalszymonduda@gmail.com
 */
public enum EnumSpringCtx {
    CTX_MSG(null, "classpath:config/sr-msg.xml"),
    CTX_BC(CTX_MSG, "classpath:config/sr-bc.xml"),
    CTX(CTX_BC, "classpath:config/sr-client.xml");

    private ApplicationContext ctx;

    private EnumSpringCtx(EnumSpringCtx parent, String ... config) {
        ctx = new ClassPathXmlApplicationContext(config, parent == null ? null : parent.get());
    }

    /**
     * Zwraca kontekst springowy. Jesli kontekst nie został zainicjowany, to
     * inicuje. Metoda jest 'thread safty'.
     * 
     * @return
     */
    public ApplicationContext get() {
        return ctx;
    }

}
