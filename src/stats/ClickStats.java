package stats;

/**
 * StatsPlugin tracking only LMB clicks.
 * @author Thibault Helsmoortel
 */
public class ClickStats implements StatsPlugin {

    private boolean trackingEnabled;
    private int clicks;

    public ClickStats() {
        this.trackingEnabled = false;
        clicks = 0;
    }

    public int getClicks() {
        return clicks;
    }

    public boolean isTrackingEnabled() {
        return trackingEnabled;
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
    public void track(StatsAction statsAction) {
        if (statsAction.equals(StatsAction.MOUSE_LMB_CLICK) && trackingEnabled) clicks++;
    }
}
