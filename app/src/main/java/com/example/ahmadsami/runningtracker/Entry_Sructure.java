package com.example.ahmadsami.runningtracker;

public class Entry_Sructure {

    // declaring variables

    public  int tracker_ID;
    public  String tracker_time;
    public  String tracker_distance;
    public  String tracker_date ;

    // default constructor when we don't want to pass anything when creating and object of this class
    public Entry_Sructure() {

    }

    //  main constructor used to set  all the contents
    public Entry_Sructure(String tracker_time, String tracker_distance,String tracker_date,int tracker_ID) {

        this.tracker_ID = tracker_ID;
        this.tracker_time = tracker_time;
        this.tracker_distance = tracker_distance;
        this.tracker_date = tracker_date;
    }


    public void setTracker_id(int id){

        this.tracker_ID = id;
    }


    public void setTracker_distance(String distance){
        this.tracker_distance = distance;
    }


    public void setTracker_time(String time){
        this.tracker_time = time;
    }

    public void setTracker_date(String date){
        this.tracker_date = date;
    }

    public int getTracker_ID(){
        return  tracker_ID;
    }

    public String getTracker_time() {
        return tracker_time;
    }

    public String getTracker_distance(){
        return tracker_distance;
    }

    public String getTracker_date(){

        return tracker_date;
    }
}
