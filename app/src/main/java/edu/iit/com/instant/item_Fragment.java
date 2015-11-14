package edu.iit.com.instant;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.ListFragment;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.iit.adapter.Home_Adapter;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import edu.iit.com.instant.dummy.DummyContent;

public class item_Fragment extends ListFragment {
    //List<Alert> globalAlerts;
    // AlertAdapter adapter;
    // List<Alert> alerts;
    double userLatitude = 0;
    double userLongitude = 0;
    private GoogleMap mMap;
    LocationManager sys_locationManager;

    Home_Adapter adapter;
    List<UserInfo> all_usUserInfo = new ArrayList<>();
    // public View rootView;
    int radius=0;
    public void setRadius(int radius){
        this.radius=radius;
    }
    public item_Fragment() {

    }

    private static View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle("View all users");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.show_all_users, container, false);
        } catch (InflateException e) {
            e.printStackTrace();
        }
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        try {
            InitializeMap();
            getCurrentAddress();
            loadData();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        adapter = new Home_Adapter(getActivity(), all_usUserInfo);
        setListAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
//        UserInfo user1= new UserInfo("329052488","Fairfield","aditya","https://scontent.cdninstagram.com/hphotos-frc/t51.2885-15/s320x320/e15/10475138_818919314807450_634602356_n.jpg","38.2493581","-122.0399663");
//        UserInfo user2= new UserInfo("834559220","Fairfield","adrielangel","https://scontent.cdninstagram.com/hphotos-xaf1/t51.2885-15/s320x320/e15/11377412_833245026759030_737387628_n.jpg","38.2493581","-122.0399663");
//        all_usUserInfo.add(user1);
//        all_usUserInfo.add(user2);
        adapter = new Home_Adapter(getActivity(), all_usUserInfo);
        setListAdapter(adapter);
    }


    public void loadData() throws IOException {
//        C:\Users\Sachin\AndroidStudioProjects\Instant\app\src\main\res\dataset.txt
        String complete_data_file_path = "/InstaLocate/dataset.txt";
        String entered_filename = Environment.getExternalStorageDirectory().getPath() + complete_data_file_path;
        File input_file = new File(entered_filename);
        Toast.makeText(getActivity(), "" + input_file.getPath(), Toast.LENGTH_LONG).show();
        String delimiter_file = "\\|";
        if (input_file.exists()) {
            FileInputStream fstream;
            try {
                fstream = new FileInputStream(entered_filename);
                Toast.makeText(getActivity(), "File found", Toast.LENGTH_LONG).show();
                DataInputStream in = new DataInputStream(fstream);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String strLine;
                System.out.println();
                int counter = 0;
                while ((strLine = br.readLine()) != null) {
                    String splited_data[] = strLine.split(delimiter_file);
                    if (splited_data.length == 5) {

                        double lat = Double.parseDouble((splited_data[4].split(":")[1]).split(";")[0]);
                        double lon = Double.parseDouble((splited_data[4].split(":")[1]).split(";")[1]);
                        UserInfo user = new UserInfo(splited_data[0].split(":")[1],//ID
                                splited_data[1].split(":")[1],//CITY
                                splited_data[2].split(":")[1],//USERNAME
                                "https:" + splited_data[3].split(":")[2],//URL
                                lat + "",//LAT
                                lon + "");//LONG

                        double distance = distFrom(lat, lon, userLatitude,userLongitude);
                        if (distance <= radius)
                        {
                            user.setDistance(distance);
                            all_usUserInfo.add(user);
                            LatLng currentPosition = new LatLng(Double.parseDouble((splited_data[4].split(":")[1]).split(";")[0]),
                                    Double.parseDouble((splited_data[4].split(":")[1]).split(";")[1]));
//                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 6));
                            mMap.addMarker(new MarkerOptions().position(currentPosition).snippet("").title(splited_data[2].split(":")[1]));
                        }
                    }
/*
                    counter++;
                        if(counter>100)
                          break;
*/
                }
                in.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                //System.out.println("File not found exception");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), "File not found", Toast.LENGTH_SHORT).show();
        }
    }


    public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 3958.75; // miles (or 6371.0 kilometers)
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }


    public void InitializeMap() {
        Criteria criteria = new Criteria();
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        sys_locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(true);
        criteria.setBearingRequired(true);
        criteria.setSpeedRequired(true);
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setVerticalAccuracy(Criteria.ACCURACY_MEDIUM);
        criteria.setBearingAccuracy(Criteria.ACCURACY_LOW);
        criteria.setSpeedAccuracy(Criteria.ACCURACY_HIGH);
//        ResetCoOrdinates();
    }


    public void getCurrentAddress() throws Exception {
        LocationManager locationManager;
        String svcName = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) getActivity().getSystemService(svcName);
        String provider;
        Location location;

        provider = LocationManager.GPS_PROVIDER;
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        location = locationManager.getLastKnownLocation(provider);
        if (location == null) {
            provider = LocationManager.NETWORK_PROVIDER;
            location = locationManager.getLastKnownLocation(provider);
        }
        if (location == null) {
            provider = LocationManager.PASSIVE_PROVIDER;
            location = locationManager.getLastKnownLocation(provider);
        }


        Context context = getActivity().getApplicationContext();
        if (location != null) {
            userLatitude = location.getLatitude();
            userLongitude = location.getLongitude();
        }
        if (userLatitude == 0 && userLongitude == 0) {
            Toast.makeText(context, "Please make sure that the location service is ON", Toast.LENGTH_LONG).show();
        }
    }

    private int checkSelfPermission(String accessFineLocation) {
        return 0;
    }

}



