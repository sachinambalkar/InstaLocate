package edu.iit.com.instant;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class MapsActivity extends Activity
{
    private GoogleMap mMap;
    LocationManager sys_locationManager;
    ImageView imgView;
    Drawable image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        getFragmentManager().beginTransaction().replace(R.id.container,new item_Fragment()).commit();
    }

    public void search(View view){
        EditText etv_radius=(EditText)findViewById(R.id.etv_radius);

        Integer radius=Integer.parseInt(etv_radius.getText().toString());
        item_Fragment intf=new item_Fragment();
        intf.setRadius(radius);
        getFragmentManager().beginTransaction().replace(R.id.container,intf).commit();
    }
}
