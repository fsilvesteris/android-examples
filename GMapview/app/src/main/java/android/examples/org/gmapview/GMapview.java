package android.examples.org.gmapview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.webkit.WebView;
import android.widget.Toast;

@SuppressLint("SetJavaScriptEnabled")
public class GMapview extends Activity {

    private WebView webView;
    private Location mostRecentLocation;
    double[] lats = new double[20];
    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 0; // in
    // Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 0; // in
// Milliseconds

    protected LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmapview);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES,
                MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, new MyLocationListener());

        showCurrentLocation();
        setupWebView();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    /** Sets up the WebView object and loads the URL of the page **/
// @SuppressLint(R.string.setjavascriptenabled)
    private void setupWebView() {

        webView = (WebView) findViewById(R.id.webview);
        webView.addJavascriptInterface(new ScriptINTF(this), "android");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().getBuiltInZoomControls();
        webView.getSettings().setBuiltInZoomControls(true);

        webView.loadUrl("file:///android_asset/mapload.html");
// webView.loadUrl(MAP_URL);
    }

    public class ScriptINTF {
        Context mContext;

        /** Instantiate the interface and set the context */
        ScriptINTF(Context c) {
            mContext = c;
        }

        public double getLatitude() {

            return mostRecentLocation.getLatitude();
        }

        public void showToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }

        public double getLongitude() {
            return mostRecentLocation.getLongitude();
        }

        public void showloc(double lat, double lng) {

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
// getMenuInflater().inflate(R.menu.gmapview, menu);
        return true;
    }


    protected void showCurrentLocation() {

        Location location = locationManager
                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        mostRecentLocation = location;
        if (location != null) {
            String message = String.format(
                    "Current Location \n Longitude: %1$s \n Latitude: %2$s",
                    location.getLongitude(), location.getLatitude());
            Toast.makeText(GMapview.this, message, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(GMapview.this, "null", Toast.LENGTH_LONG).show();
        }

    }

    private class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location location) {
            String message = String.format(
                    "New Location \n Longitude: %1$s \n Latitude: %2$s",
                    location.getLongitude(), location.getLatitude());
            Toast.makeText(GMapview.this, message, Toast.LENGTH_LONG).show();
        }

        public void onStatusChanged(String s, int i, Bundle b) {
// Toast.makeText(GMapview.this, "Provider status changed",
// Toast.LENGTH_LONG).show();
        }

        public void onProviderDisabled(String s) {
            Toast.makeText(GMapview.this,
                    "Provider disabled by the user. GPS turned off",
                    Toast.LENGTH_LONG).show();
        }

        public void onProviderEnabled(String s) {
            Toast.makeText(GMapview.this,
                    "Provider enabled by the user. GPS turned on",
                    Toast.LENGTH_LONG).show();
        }

    }

}