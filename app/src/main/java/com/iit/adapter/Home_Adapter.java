package com.iit.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import edu.iit.com.instant.R;
import edu.iit.com.instant.UserInfo;

/**
 * Created by Sachin on 14-11-2015.
 */
public class Home_Adapter extends BaseAdapter
{
    public int valInterator=0;
    private Activity activity;
    private List<UserInfo> all_userInfo;
    private static LayoutInflater inflater = null;
    static int colorid=0;

    ImageView imgView;
    Drawable image;
    int counter=0;
    public Home_Adapter(Activity activity, List<UserInfo> all_userInfo)
    {
        this.activity = activity;
        this.all_userInfo=all_userInfo;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount(){
        return all_userInfo.size();
    }

    public Object getItem(int position){
        return position;
    }

    public long getItemId(int position){
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        final View v;
        if(convertView == null)
            v = inflater.inflate(R.layout.home, null);
        else
            v= convertView;

        imgView=(ImageView)v.findViewById(R.id.imageView);
        TextView username = (TextView)v.findViewById(R.id.tv_username);
        TextView cityname = (TextView)v.findViewById(R.id.tv_cityname);
        TextView distance=(TextView)v.findViewById(R.id.tv_distance);


        UserInfo userInfo;
        userInfo = all_userInfo.get(position);
        distance.setText("Dis: "+(Math.round(1000*userInfo.getDistance())/1000)+"Miles");
        username.setText("Usr: "+userInfo.getUsername());
        cityname.setText("City: "+userInfo.getCityname());
        String url_img =userInfo.getUrl();
        new DownloadImageTask(imgView).execute(url_img);
//        new loadImage().execute();
        return v;
    }



    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


    /////////////////////

/*

    private Drawable ImageOperations(Context ctx, String url, String saveFilename) {
        try {
            InputStream is = (InputStream) this.fetch(url);
            Drawable d = Drawable.createFromStream(is, "src");
            return d;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Object fetch(String address) throws MalformedURLException,IOException {
        URL url = new URL(address);
        Object content = url.getContent();
        return content;
    }


    private class loadImage extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... params) {
            image = ImageOperations(activity,params[0],"abc"+counter+".jpg");
            counter++;
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(activity, "done....", Toast.LENGTH_SHORT).show();
            imgView.setImageDrawable(image);
        }
    }


*/

}
