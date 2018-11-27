package com.tannerquesenberry.rentallot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by tannerquesenberry on 3/13/18.
 */

public class Patron {
    private String fname;
    private String lname;
    private int age;
    private String carRentalID;
    private String patronID;

    public Patron(String fname, String lname, int age, String carRentalID, String patronID){
        this.fname = fname;
        this.lname = lname;
        this.age = age;
        this.carRentalID = carRentalID;
        this.patronID = patronID;
    }

    // Constructor to convert JSON object into a Java class instance
    public Patron(JSONObject object){
        try {
            this.fname = object.getString("fname");
            this.lname = object.getString("lname");
            this.age = object.getInt("age");
            this.carRentalID = object.getString("rental_id");
            this.patronID = object.getString("id");
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    // Factory method to convert array of JSON objects in list of objects
    // Car.fromJSON(jsonArray);
    public static ArrayList<Patron> fromJSON(JSONArray jsonObjects){
        ArrayList<Patron> patrons = new ArrayList<Patron>();
        for (int i = 0; i < jsonObjects.length(); i++){
            try {
                patrons.add(new Patron(jsonObjects.getJSONObject(i)));
            } catch(JSONException e){
                e.printStackTrace();
            }
        }
        return patrons;
    }

    public String getFname(){
        return fname;
    }

    public String getLname(){
        return lname;
    }

    public String getCarRentalID(){
        return carRentalID;
    }

    public int getAge(){
        return age;
    }

    public String getStringAge(){
        return Integer.toString(age);
    }

    public String getPatronID(){return patronID;}

}
