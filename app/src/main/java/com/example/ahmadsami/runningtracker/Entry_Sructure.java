package com.example.ahmadsami.runningtracker;
// structure of each entry in the database
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

        // save data to appropriate variables
        this.tracker_ID = tracker_ID;
        this.tracker_time = tracker_time;
        this.tracker_distance = tracker_distance;
        this.tracker_date = tracker_date;
    }

    // set the session's ID
    public void setTracker_id(int id){

        this.tracker_ID = id;
    }


    // sets the session's total distance
    public void setTracker_distance(String distance){
        this.tracker_distance = distance;
    }

    // set the session's total time
    public void setTracker_time(String time){
        this.tracker_time = time;
    }

    // set the session's date
    public void setTracker_date(String date){
        this.tracker_date = date;
    }

    // get the session's id
    public int getTracker_ID(){
        return  tracker_ID;
    }

    // get the session's total time
    public String getTracker_time() {
        return tracker_time;
    }

    // get the session total distance
    public String getTracker_distance(){
        return tracker_distance;
    }

    // get the session's date
    public String getTracker_date(){

        return tracker_date;
    }
}
