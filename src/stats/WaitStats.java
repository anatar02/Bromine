package stats;

/**
 * StatsPlugin tracking waits.
 * @author Thibault Helsmoortel
 */
public class WaitStats implements StatsPlugin {

    private boolean trackingEnabled;
    private int timesWaited;

    public WaitStats() {
        this.trackingEnabled = false;
        this.timesWaited = 0;
    }

    @Override
    public void enableTracking() {
        this.trackingEnabled = true;
    }

    @Override
    public void disableTracking() {
        this.trackingEnabled = false;
    }

    @Override
    public boolean isTrackingEnabled() {
        return trackingEnabled;
    }

    @Override
    public void track(StatsAction statsAction) {
        if (statsAction.equals(StatsAction.WAIT) && trackingEnabled) timesWaited++;
    }

    @Override
    public void reset() {
        this.timesWaited = 0;
    }

    @Override
    public String represent() {
        return "Times waited: " + timesWaited;
    }
}
