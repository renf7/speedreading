package pl.rdu.sr.model.ds;

/**
 * Entity to hold information about snapshots.
 * 
 * @author renf7_000
 *
 */
public class DsWordSnapshot extends DsAbstract {
    /** Snapshot duration in minisecounds. */
    private long duration;

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
