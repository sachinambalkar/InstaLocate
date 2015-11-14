package edu.iit.com.instant;


public class UserInfo {

    String username;
    String locationId;
    String url;
    String latitude;
    String longitude;
    String cityname;
    Double distance=0.0;
    public UserInfo(String locationId,String cityname,String username,String url,String latitude,String longitude){
        this.username=username;
        this.locationId=locationId;
        this.url=url;
        this.latitude=latitude;
        this.longitude=longitude;
        this.cityname=cityname;
    }

    public void setDistance(Double distance){
        this.distance=distance;
    }
    public Double getDistance(){
        return distance;
    }
    public String getCityname(){
        return cityname;
    }

    public String getUsername(){
        return username;
    }

    public String getlocationId(){
        return locationId;
    }

    public String getUrl(){
        return url;
    }

    public String getLatitude(){
        return latitude;
    }

    public String getLongitude(){
        return longitude;
    }
}
