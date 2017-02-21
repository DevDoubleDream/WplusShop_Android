package kr.wdream.wplusshop.app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

import kr.wdream.wplusshop.R;
import kr.wdream.wplusshop.common.util.StorePointUtil;

//import kr.co.wdream.common.ButtonCommon;

/**
 * Created by SEO on 2015-12-17.
 */
public class StoreMap extends FragmentActivity implements View.OnClickListener{

    static LatLng store_point;
    static LatLng otherStore_point;
    static String stnm;
    static String stphone;
    static String[][] otherStoreResult = null;
    static int radius = 1000;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_map);

        Main.locationHistory.add(this);

        Intent intent = getIntent();
        stnm    = intent.getExtras().getString("stnm");
        stphone = intent.getExtras().getString("stphone");
        store_point =  new LatLng(Float.parseFloat(intent.getExtras().getString("point_xx")), Float.parseFloat(intent.getExtras().getString("point_yy")));

        ((TextView)findViewById(R.id.txt_head_center)).setText(stnm);
        LinearLayout before_bullet = (LinearLayout)findViewById(R.id.icon_head_left);
        before_bullet.setVisibility(View.VISIBLE);
        before_bullet.setOnClickListener(this);

        HashMap<String, String> otherStoreParams = new HashMap<String, String>();
        otherStoreParams.put("cmd", "map");
        otherStoreParams.put("xx", intent.getExtras().getString("point_xx"));
        otherStoreParams.put("yy", intent.getExtras().getString("point_yy"));
        otherStoreParams.put("dt", String.valueOf(radius));
        otherStoreResult = StorePointUtil.getOtherStoreResult(otherStoreParams);

        setUpMapIfNeeded();

//        ButtonCommon.setClick(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
//        ButtonCommon.setClickLogin(this);
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        setUpOtherMap(otherStoreResult);
        if(stphone.equals("--"))    mMap.addMarker(new MarkerOptions().position(store_point).title(stnm));
        else                        mMap.addMarker(new MarkerOptions().position(store_point).title(stnm).snippet(stphone));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(store_point, 15));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);

        CircleOptions circleOptions = new CircleOptions()
                .center(store_point)
                .radius(radius)
                .strokeWidth(2)
                .strokeColor(Color.parseColor("#0084d3"))
                .fillColor(Color.parseColor("#300084d3"));
        // Supported formats are: #RRGGBB #AARRGGBB
        //   #AA is the alpha, or amount of transparency

        mMap.addCircle(circleOptions);
    }

    private void setUpOtherMap(String[][] otherStore) {
        for(int i = 0; i < otherStore.length; i++){
            otherStore_point =  new LatLng(Float.parseFloat(otherStore[i][2]), Float.parseFloat(otherStore[i][3]));
            if(otherStore[i][1].equals("--")) mMap.addMarker(new MarkerOptions().position(otherStore_point).title(otherStore[i][0]).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)).alpha(0.7f));
            else                              mMap.addMarker(new MarkerOptions().position(otherStore_point).title(otherStore[i][0]).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)).alpha(0.7f).snippet(otherStore[i][1]));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.icon_head_left:
                finish();
                break;
        }
    }

}
