package com.tannerquesenberry.rentallot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by tannerquesenberry on 3/12/18.
 */

public class Car {
    private String make;
    private String model;
    private int year;
    private String color;
    private Boolean rented;
    private String rentalID;

    public Car(String make, String model, int year, String color, Boolean rented, String rentalID){
        this.make = make;
        this.model = model;
        this.year = year;
        this.color = color;
        this.rented = rented;
        this.rentalID = rentalID;
    }

    // Constructor to convert JSON object into a Java class instance
    public Car(JSONObject object){
        try {
            this.make = object.getString("make");
            this.model = object.getString("model");
            this.year = object.getInt("year");
            this.color = object.getString("color");
            this.rented = object.getBoolean("rented");
            this.rentalID = object.getString("id");
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    // Factory method to convert array of JSON objects in list of objects
    // Car.fromJSON(jsonArray);
    public static ArrayList<Car> fromJSON(JSONArray jsonObjects){
        ArrayList<Car> cars = new ArrayList<Car>();
        for (int i = 0; i < jsonObjects.length(); i++){
            try {
                cars.add(new Car(jsonObjects.getJSONObject(i)));
            } catch(JSONException e){
                e.printStackTrace();
            }
        }
        return cars;
    }

    public String getMake(){
        return make;
    }

    public String getModel(){
        return model;
    }

    public String getColor(){
        return color;
    }

    public int getYear(){
        return year;
    }

    public String getStringYear(){
        return Integer.toString(year);
    }

    public Boolean getRented(){
        return rented;
    }

    public String getRentalID(){
        return rentalID;
    }
}
