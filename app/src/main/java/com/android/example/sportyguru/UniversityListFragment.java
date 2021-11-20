package com.android.example.sportyguru;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.example.sportyguru.data.UniversitydbHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UniversityListFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private static final String TAG = UniversityListFragment.class.toString();
    fetchData mFetchData;
    String country = "";
    private View loadingIndicator ;
    private RecyclerView mRecyclerView;
    private List<University> mUniversityList;
    private UniversityAdapter mUniversityAdapter;
    private UniversityAPIQueryUtils mAPIQueryUtils;
    private String url = "http://universities.hipolabs.com/search?country=";
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager locationManager;
    private LocationRequest mLocationRequest;
    private boolean isConnected;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_university_list, container, false);
        init(view);
        //Getting User Location
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        return view;
    }

    private void init(View view) {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

         isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if(mUniversityList == null){
            mUniversityList = new ArrayList<>();}
        loadingIndicator=view.findViewById(R.id.progress);
        loadingIndicator.setVisibility(View.VISIBLE);

        mRecyclerView = view.findViewById(R.id.university_list_item_root);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAPIQueryUtils = new UniversityAPIQueryUtils();
        mFetchData = new fetchData();
        mUniversityAdapter = new UniversityAdapter(getContext(), mUniversityList, university -> {

            Bundle args = new Bundle();
            args.putSerializable("key", university);
            Navigation.findNavController(getView()).navigate(R.id.action_universityListFragment_to_university_detail_fragment, args);
        });

        mRecyclerView.setAdapter(mUniversityAdapter);
        if(!isConnected){
            mUniversityList.clear();
            mUniversityList.addAll(readFromDB());
            mUniversityAdapter.notifyDataSetChanged();
        }else{
            if(mFetchData.getStatus() != AsyncTask.Status.RUNNING &&!country.equals("")){
                mFetchData.execute(url);
            }
        }

        if(!country.equals("")&&mUniversityList.size()!=0){
            loadingIndicator.setVisibility(View.GONE);
        }

    }

    @Override
    public void onConnected(Bundle bundle) {
        if(!country.equals("")){
            return;
        }
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        } startLocationUpdates();
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLocation == null){
            startLocationUpdates();
        }
        if (mLocation != null) {
            Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = gcd.getFromLocation(mLocation.getLatitude(), mLocation.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (addresses.size() > 0) {
                country = addresses.get(0).getCountryName();
                Toast.makeText(getContext(), "Your Current Location is " + country, Toast.LENGTH_SHORT).show();
                url = url + country;

                if(mFetchData.getStatus() != AsyncTask.Status.RUNNING && isConnected){
                        mFetchData.execute(url);
                }
                Log.e("Country", country);

            }
        } else {
             Toast.makeText(getContext(), "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_LOW_POWER)
                .setInterval(1);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        Log.d("reque", "--->>>>");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
    @Override
    public void onLocationChanged(Location location) {

    }

    private void saveToDB(University university) {
        SQLiteDatabase database = new UniversitydbHelper(getContext()).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UniversitydbHelper.UNIVERSITY_NAME,university.getUniversity_Name());
        values.put(UniversitydbHelper.UNIVERSITY_STATE_PROVINCE,university.getUniversity_State_Province());
        values.put(UniversitydbHelper.UNIVERSITY_COUNTRY,university.getUniversity_country());
        values.put(UniversitydbHelper.UNIVERSITY_WEB_PAGE,university.getUniversity_Web_Page());
        values.put(UniversitydbHelper.UNIVERSITY_DOMAIN_NAME,university.getUniversityDomainName());
        values.put(UniversitydbHelper.UNIVERSITY_CODE,university.getUniversityCode());
        values.put(UniversitydbHelper.UNIVERSITY_ADDRESS,university.getUniversity_Address());

        database.insert(UniversitydbHelper.UNIVERSITY_TABLE_NAME, null, values);

    }

    private  ArrayList <University> readFromDB() {
        ArrayList<University> result = new ArrayList<>();
        SQLiteDatabase database = new UniversitydbHelper(getContext()).getReadableDatabase();

        String[] projection = {
                UniversitydbHelper.UNIVERSITY_COLUMN_ID,
                UniversitydbHelper.UNIVERSITY_NAME,
                UniversitydbHelper.UNIVERSITY_STATE_PROVINCE,
                UniversitydbHelper.UNIVERSITY_COUNTRY,
                UniversitydbHelper.UNIVERSITY_WEB_PAGE,
                UniversitydbHelper.UNIVERSITY_DOMAIN_NAME,
                UniversitydbHelper.UNIVERSITY_CODE,
                UniversitydbHelper.UNIVERSITY_ADDRESS
        };




        Cursor cursor = database.query(
                UniversitydbHelper.UNIVERSITY_TABLE_NAME,   // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // don't sort
        );
        final int NAME_COLOUM_INDEX = cursor.getColumnIndex(UniversitydbHelper.UNIVERSITY_NAME);
        final int STATEPROVINCE_COLOUM_INDEX = cursor.getColumnIndex(UniversitydbHelper.UNIVERSITY_STATE_PROVINCE);
        final int COUNTRY_COLOUM_INDEX = cursor.getColumnIndex(UniversitydbHelper.UNIVERSITY_COUNTRY);
        final int WEBPAGE_COLOUM_INDEX = cursor.getColumnIndex(UniversitydbHelper.UNIVERSITY_WEB_PAGE);
        final int DOMAIN_NAME_INDEX = cursor.getColumnIndex(UniversitydbHelper.UNIVERSITY_DOMAIN_NAME);
        final int CODE_COLOUM_INDEX = cursor.getColumnIndex(UniversitydbHelper.UNIVERSITY_CODE);
        final int ADDRESS_COLOUM_INDEX = cursor.getColumnIndex(UniversitydbHelper.UNIVERSITY_ADDRESS);
        if(cursor.moveToFirst()){
            do{
                University university = new University(
                        cursor.getString(NAME_COLOUM_INDEX),
                        cursor.getString(STATEPROVINCE_COLOUM_INDEX),
                        cursor.getString(COUNTRY_COLOUM_INDEX),
                        cursor.getString(WEBPAGE_COLOUM_INDEX),
                        cursor.getString(DOMAIN_NAME_INDEX),
                        cursor.getString(CODE_COLOUM_INDEX),
                        cursor.getString(ADDRESS_COLOUM_INDEX)
                );
                result.add(university);
            }while (cursor.moveToNext());
        }
        return result;


    }

    public class fetchData extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

                mUniversityList.clear();
                mUniversityList.addAll((ArrayList<University>) mAPIQueryUtils.extractUniversity(strings[0]));
                int lastI = mUniversityList.size();
                if(mUniversityList.size()>20){
                    lastI = 20;
                }
                for(int i =0 ; i < lastI ; i++){
                    saveToDB(mUniversityList.get(i));
                }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            loadingIndicator.setVisibility(View.GONE);
            mUniversityAdapter.notifyDataSetChanged();
        }
    }
}


