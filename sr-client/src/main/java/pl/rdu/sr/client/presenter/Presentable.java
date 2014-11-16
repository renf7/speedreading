package pl.rdu.sr.client.presenter;

public interface Presentable {
    @SuppressWarnings("restriction")
    <T extends javafx.scene.Parent> T show();
}
