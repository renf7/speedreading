package pl.rdu.sr.client.presenter;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractPresenter<V,M> {
    @Autowired
    protected V v;
    
    @Autowired
    protected M m;

    public V getV() {
        return v;
    }

}
