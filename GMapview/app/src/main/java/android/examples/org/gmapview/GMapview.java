package android.examples.org.gmapview;

/**
 This code is based on the code that is described at the following location
 http://javazerosign.blogspot.co.uk/2013/11/google-map-v3-for-android-devices.html
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
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
        webView.addJavascriptInterface(new ScriptINTF(this), "android");
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
        String message = (location != null) ? String.format("%1s Location \n Longitude: %2$s \n Latitude: %3$s", locationName, location.getLongitude(), location.getLatitude()) : "null";
        makeText(this, message, LENGTH_LONG).show();
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


    public class ScriptINTF {
        private final Context mContext;

        /**
         * Instantiate the interface and set the context
         */
        private ScriptINTF(Context c) {

            mContext = c;
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
        public void showToast(String toast) {
            makeText(mContext, toast, LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public void showloc(double lat, double lng) {
            //whilst this method is empty right now, it is referenced in the javascript
        }

    }
}