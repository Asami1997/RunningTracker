package com.example.ahmadsami.runningtracker;

public class Entry_Sructure {

    // declaring variables

    public  int tracker_ID;
    public  String tracker_time;
    public  String tracker_distance;
    public  String tracker_date ;
    public  String tracker_steps;

    // default constructor when we don't want to pass anything when creating and object of this class
    public Entry_Sructure() {

    }

    // constructor for only the title and the content
    public Entry_Sructure(String tracker_time, String tracker_distance,String tracker_date,String tracker_steps) {
        this.tracker_time = tracker_time;
        this.tracker_distance = tracker_distance;
        this.tracker_date = tracker_date;
        this.tracker_steps = tracker_steps;
    }

    //  main constructor used to set  all the contents
    public Entry_Sructure(int tracker_ID,String tracker_time, String tracker_distance,String tracker_date,String tracker_steps) {

        this.tracker_ID = tracker_ID;
        this.tracker_time = tracker_time;
        this.tracker_distance = tracker_distance;
        this.tracker_date = tracker_date;
        this.tracker_steps = tracker_steps;
    }


    // set recipe id only
    public void setTracker_id(int id){

        this.tracker_ID = id;
    }


    // set recipe content only
    public void setTracker_distance(String distance){
        this.tracker_distance = distance;
    }


    // set recipe title only
    public void setTracker_time(String time){
        this.tracker_time = time;
    }

    // set recipe content only
    public void setTracker_date(String date){
        this.tracker_distance = date;
    }

    // set recipe content only
    public void setTracker_steps(String steps){
        this.tracker_steps = steps;
    }

    // retrieve recipe ID
    public int getTracker_ID(){
        return  tracker_ID;
    }

    public String getTracker_time() {
        return tracker_time;
    }

    // retrieve recipe content
    public String getTracker_distance(){
        return tracker_distance;
    }

    // retrieve recipe title
    public String getTracker_steps(){

        return tracker_steps;
    }
    public String getTracker_date(){

        return tracker_date;
    }
}
