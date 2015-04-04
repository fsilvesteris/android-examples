package android.examples.org.gmapview;

/**
 This code is based on code that is described at the following location
 http://javazerosign.blogspot.co.uk/2013/11/google-map-v3-for-android-devices.html
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import static android.location.LocationManager.NETWORK_PROVIDER;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

@SuppressLint("SetJavaScriptEnabled")
public class GMapview extends Activity implements LocationListener {

    private Location mostRecentLocation;

    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 0; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 0; // in Milliseconds

    private int zoomLevel = 16;

    protected LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmapview);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(NETWORK_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES, MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, this);

        showCurrentLocation();

        setupWebView();
        //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    /**
     * Sets up the WebView object and loads the URL of the page *
     */
    // @SuppressLint(R.string.setjavascriptenabled)
    private void setupWebView() {

        WebView webView = (WebView) findViewById(R.id.webview);
        webView.addJavascriptInterface(this, "android");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().getBuiltInZoomControls();
        webView.getSettings().setBuiltInZoomControls(true);

        webView.loadUrl("file:///android_asset/mapload.html");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.gmapview, menu);
        return true;
    }


    protected void showCurrentLocation() {
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        mostRecentLocation = location;
        showLocation("Current", location);
    }


    /**
     * @param location the location
     */
    @Override
    public void onLocationChanged(Location location) {
        showLocation("New", location);
    }

    private void showLocation(String locationName, Location location) {
        makeText(this, formatLocation(locationName, location), LENGTH_LONG).show();
    }

    private String formatLocation(String locationName, Location location) {
        return location != null ? formatLocation(locationName, location.getLatitude(), location.getLongitude()) : formatLocation(locationName, null, null);
    }


    /**
     * @param provider the name of the location provider associated with this update
     * @param status   the provider status
     * @param extras   an optional Bundle which will contain provider specific status variables.
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // makeText(this, "Provider status changed", LENGTH_LONG).show();
    }

    /**
     * @param provider the name of the location provider associated with this update
     */
    @Override
    public void onProviderDisabled(String provider) {
        makeText(this, "Provider disabled by the user. GPS turned off", LENGTH_LONG).show();
    }


    /**
     * @param provider the name of the location provider associated with this update
     */
    @Override
    public void onProviderEnabled(String provider) {
        makeText(this, "Provider enabled by the user. GPS turned on", LENGTH_LONG).show();
    }


    /**
     * call this method to initialise the zoom level - note this is just a setter - does not cause the map to update
     * https://developers.google.com/maps/documentation/staticmaps/#Zoomlevels
     *
     * @param value - zoomlevel, between 0 and 21
     */
    protected void setZoomLevel(int value) {
        zoomLevel = Math.min(22, Math.max(0, value));
    }

    @JavascriptInterface
    public double getLatitude() {

        return mostRecentLocation.getLatitude();
    }

    @JavascriptInterface
    public double getLongitude() {

        return mostRecentLocation.getLongitude();
    }

    @JavascriptInterface
    public int getZoomLevel() {
        return zoomLevel;
    }

    @JavascriptInterface
    public void showToast(String toast) {
        makeText(this, toast, LENGTH_SHORT).show();
    }


    /**
     * for details on  String.format options, refer to http://docs.oracle.com/javase/1.5.0/docs/api/java/util/Formatter.html#syntax
     *
     * @param locationName - location prefix
     * @param latitude     - latitude
     * @param longitude    - longitude
     * @return - formatted text
     */
    private String formatLocation(String locationName, Double latitude, Double longitude) {
        if ((latitude != null && longitude != null)) {
            return String.format("%1s Location %2$s, %3$s", locationName, latitude, longitude);
        }
        return locationName + " Location - Unknown";
    }

    /**
    https://developers.google.com/maps/documentation/javascript/examples/event-simple
    https://developers.google.com/maps/documentation/javascript/reference?csw=1#event
    */
    @JavascriptInterface
    public void mapEvent(String event, double latitude, double longitude, int zoom) {
        String message = formatLocation(event, latitude, longitude) + " zoom: " + zoom;
        Log.i("MAP_EVENT", message);
        setZoomLevel(zoom);
    }

}