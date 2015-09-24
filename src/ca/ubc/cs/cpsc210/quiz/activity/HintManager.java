package ca.ubc.cs.cpsc210.quiz.activity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.*;

/**
 * Created by dkuang on 11/18/14.
 * Manager for hints
 */
public class HintManager {
    private static final float ARROW_ROTATION_OFFSET = -45;
    private final int TOTAL_HINTS;
    private GoogleMap map;
    private int hintsRemaining;
    private Marker arrow;
    private LatLng currentHintLocation;

    /**
     * Constructor initializes hint manager with map on which to overlay the arrow
     * and the number of hints given
     * @param map the map on which to place the overlay
     * @param totalHints the number of hints to start with
     */
    public HintManager(GoogleMap map, int totalHints) {
        this.map = map;
        TOTAL_HINTS = totalHints;
        hintsRemaining = TOTAL_HINTS;
        restart();
    }

    /**
     * Get the number of remaining hints
     * @return the number of hints remaining
     */
    public int getHintsRemaining() {
        return hintsRemaining;
    }

    /**
     * Get the location of where the hint was last used
     * @return the location of the current hint
     */
    public LatLng getCurrentHintLocation() {
        return currentHintLocation;
    }

    /**
     * Adds an arrow overlay pointing in the direction of the target restaurant
     *
     * @param marker the marker where the arrow should be placed
     * @param restaurantLatLng the destination restaurant location
     */
    public void addArrow(Marker marker, LatLng restaurantLatLng) {
        MarkerOptions goo = new MarkerOptions()
                .position(marker.getPosition())
                .anchor(0, 1)
                .rotation(getBearing(marker.getPosition(), restaurantLatLng) + ARROW_ROTATION_OFFSET)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow))
                .flat(true);
        arrow = map.addMarker(goo);
        currentHintLocation = marker.getPosition();

    }

    /**
     * Use up a hint if any hints remain
     *
     * @return true if a hint is used; false otherwise
     */
    public boolean useHint() {
        if (hintsRemaining > 0) {
            hintsRemaining--;
            return true;
        }
        return false;
    }

    /**
     * Remove any old overlays
     */
    public void removeArrow() {
        if (arrow != null) {
            arrow.remove();
            arrow = null;
        }
    }

    /**
     * Reset the hint manager
     */
    public void restart() {
        arrow = null;
        hintsRemaining = TOTAL_HINTS;
    }

    /**
     * NOTE TO CPSC 210 STUDENTS: this method is needed only if you attempt the bonus exercise.
     * <p/>
     * Get bearing from origin to destination on map
     *
     * @param origin location of origin
     * @param dest   location of destination
     * @return bearing from origin to destination measured clockwise in degrees from due North
     */
    private float getBearing(LatLng origin, LatLng dest) {
        double originLongRad = Math.toRadians(origin.longitude);
        double originLatRad = Math.toRadians(origin.latitude);
        double destLongRad = Math.toRadians(dest.longitude);
        double destLatRad = Math.toRadians(dest.latitude);

        double deltaLong = destLongRad - originLongRad;
        double y = Math.sin(deltaLong) * Math.cos(destLatRad);
        double x = Math.cos(originLatRad) * Math.sin(destLatRad)
                - Math.sin(originLatRad) * Math.cos(destLatRad) * Math.cos(deltaLong);
        double bearing = Math.atan2(y, x);
        return (float) ((int) Math.toDegrees(bearing) + 360) % 360;
    }
}
