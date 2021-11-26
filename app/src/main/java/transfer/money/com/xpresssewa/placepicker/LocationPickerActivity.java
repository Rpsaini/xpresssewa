package transfer.money.com.xpresssewa.placepicker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

//public class LocationPickerActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_location_picker);
//    }
//}



import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.LocationServices;
import com.google.android.libraries.places.api.Places;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import transfer.money.com.xpresssewa.MyApplication;
import transfer.money.com.xpresssewa.R;

public class LocationPickerActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    double latitude;
    double longitude;
    private ListView mAutoCompleteList;
    private EditText Address;
    private String GETPLACESHIT = "places_hit";
    private PlacePredictions predictions;
    private Location mLastLocation;
    private AutoCompleteAdapter mAutoCompleteAdapter;
    public static int CUSTOM_AUTOCOMPLETE_REQUEST_CODE = 20;
    private static final int MY_PERMISSIONS_REQUEST_LOC = 30;
    //private ImageView searchBtn;
    private FragmentManager fragmentManager;
    private String preFilledText;
    private Handler handler;
    private VolleyJSONRequest request;
    private GoogleApiClient mGoogleApiClient;
    private int postion = 0;
    private LinearLayout contentmainpickerlinear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_picker);
        if(getIntent().hasExtra("Search Text"))
        {
            preFilledText = getIntent().getStringExtra("Search Text");
        }

        fragmentManager = getSupportFragmentManager();
        Address = (EditText) findViewById(R.id.adressText);
        mAutoCompleteList = (ListView) findViewById(R.id.searchResultLV);
        contentmainpickerlinear = findViewById(R.id.contentmainpickerlinear);
        Address.requestFocus();



        findViewById(R.id.clearText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Address.setText("");
            }
        });


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            fetchLocation();
        } else {

            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOC);


            } else {
                fetchLocation();
            }
        }


        //Add a text change listener to implement autocomplete functionality
        Address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // optimised way is to start searching for laction after user has typed minimum 3 chars
                if (Address.getText().length() > 2) {

                    //  searchBtn.setVisibility(View.GONE);

                    Runnable run = new Runnable() {


                        @Override
                        public void run() {
                            MyApplication.volleyQueueInstance.cancelRequestInQueue(GETPLACESHIT);
                            request = new VolleyJSONRequest(Request.Method.GET, getPlaceAutoCompleteUrl(Address.getText().toString()), null, null, LocationPickerActivity.this, LocationPickerActivity.this);
                            request.setTag(GETPLACESHIT);
                            MyApplication.volleyQueueInstance.addToRequestQueue(request);

                        }

                    };

                    // only canceling the network calls will not help, you need to remove all callbacks as well
                    // otherwise the pending callbacks and messages will again invoke the handler and will send the request
                    if (handler != null) {
                        handler.removeCallbacksAndMessages(null);
                    } else {
                        handler = new Handler();
                    }
                    handler.postDelayed(run, 500);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

        Address.setText(preFilledText);
        Address.setSelection(Address.getText().length());

        Address.setText(getIntent().getStringExtra("text"));
        Address.setSelection(Address.getText().length());
        Address.requestFocus();


        mAutoCompleteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                hidekeyboard();
                postion = position;
                final String placeId = String.valueOf(predictions.getPlaces().get(position).getPlace_id());
                getData("https://maps.googleapis.com/maps/api/place/details/json?placeid=" + placeId + "&key=" + getResources().getString(R.string.google_key));

                System.out.println("inside on item clicked====>" + placeId);
            }
        });

        if (getIntent().hasExtra("type")) {
            if (getIntent().getStringExtra("type").equalsIgnoreCase("saveloc") || getIntent().getStringExtra("type").equalsIgnoreCase("home") || getIntent().getStringExtra("type").equalsIgnoreCase("work")) {
                findViewById(R.id.linearSave).setVisibility(View.GONE);
            }
        }


        findViewById(R.id.back_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

    }

    public String getPlaceAutoCompleteUrl(String input) {
        StringBuilder urlString = new StringBuilder();
        urlString.append("https://maps.googleapis.com/maps/api/place/autocomplete/json");
        urlString.append("?input=");

        try {
            urlString.append(URLEncoder.encode(input, "utf8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        urlString.append("&location=");
        urlString.append(latitude + "," + longitude); // append lat long of current location to show nearby results.
        urlString.append("&radius=500&language=en");
        //urlString.append("&key=" + "AIzaSyCh8iJgNF3h-edODUQBvPBPq2TaNYyIWsQ");
        urlString.append("&key=" + ""+getResources().getString(R.string.google_key));


        if (getIntent().hasExtra("filterby")) {
            urlString.append("&types=(cities)");
        } else {
            urlString.append("&components=country:NP");
        }


        System.out.println("url===" + urlString.toString());


        return urlString.toString();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        //searchBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResponse(String response)
    {
        Gson gson = new Gson();
        predictions = gson.fromJson(response, PlacePredictions.class);
        if (mAutoCompleteAdapter == null) {
            mAutoCompleteAdapter = new AutoCompleteAdapter(this, predictions.getPlaces(), LocationPickerActivity.this);
            mAutoCompleteList.setAdapter(mAutoCompleteAdapter);
        } else {
            mAutoCompleteAdapter.clear();
            mAutoCompleteAdapter.addAll(predictions.getPlaces());
            mAutoCompleteAdapter.notifyDataSetChanged();
            mAutoCompleteList.invalidate();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
              //  .addApi(Places.GEO_DATA_API)
                .build();

    }

    @Override
    public void onConnected(Bundle bundle) {
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);

            if (mLastLocation != null) {
                latitude = mLastLocation.getLatitude();
                longitude = mLastLocation.getLongitude();
            }

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void fetchLocation() {
        //Build google API client to use fused location
        buildGoogleApiClient();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOC: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission granted!
                    fetchLocation();

                } else {
                    // permission denied!

                    Toast.makeText(this, "Please grant permission for using this app!", Toast.LENGTH_LONG).show();
                }
                return;
            }


        }
    }



    public void getSavedDataBack(final JSONObject obj) {
        hideKeyboard(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    Intent intent = new Intent();
                    intent.putExtra("sourcename", obj.getString("location"));
                    intent.putExtra("lat", obj.getString("lat"));
                    intent.putExtra("lng", obj.getString("lng"));
                    if(getIntent().hasExtra("current_location"))
                    {
                        intent.putExtra("current_location", getIntent().getStringExtra("current_location"));
                        intent.putExtra("pickup_location", getIntent().getStringExtra("pickup_location"));
                    }

                    setResult(CUSTOM_AUTOCOMPLETE_REQUEST_CODE, intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 100);


    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if(getIntent().hasExtra("type")) {
//            if(getIntent().getStringExtra("type").equalsIgnoreCase("saveloc")) {
//               // getSavedLocationData();
//            }
//        }
//        else
//        {
//            //getSavedLocationData();
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (data != null) {
                getSavedDataBack(new JSONObject(data.getStringExtra("data")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hidekeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    private void getData(final String url) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject objData = new JSONObject(response);
                            if (objData.has("result")) {
                                JSONObject resultObj = objData.getJSONObject("result");

                                if (resultObj.has("geometry")) {
                                    JSONObject geometryData = resultObj.getJSONObject("geometry");
                                    JSONObject locationData = geometryData.getJSONObject("location");
                                    String lat = locationData.getString("lat");
                                    String lng = locationData.getString("lng");


                                    Intent intent = new Intent();
                                    intent.putExtra("sourcename", predictions.getPlaces().get(postion).getPlaceDesc());
                                    intent.putExtra("lat", lat + "");
                                    intent.putExtra("lng", lng + "");

//                                    if (getIntent().hasExtra("current_location"))
//                                    {
//                                        intent.putExtra("current_location", getIntent()
//                                                .getStringExtra("current_location"));
//
//                                        intent.putExtra("pickup_location", getIntent().getStringExtra("pickup_location"));
//                                    }

                                    setResult(CUSTOM_AUTOCOMPLETE_REQUEST_CODE, intent);
                                    finish();
                                }
                            }
                        } catch (Exception e) {
                            progressDialog.dismiss();
                            System.out.println("inside exception-===" + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        // showToast.showToast(PickLocationActivity.this,"Some error occurred in our side");

                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        stringRequest.setShouldCache(true);


    }
}
