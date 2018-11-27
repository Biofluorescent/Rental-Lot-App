package com.tannerquesenberry.rentallot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by tannerquesenberry on 3/12/18.
 */

public class CarsAdapter extends ArrayAdapter<Car> {
    public CarsAdapter(Context context, ArrayList<Car> cars){
        super(context, 0, cars);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        // Positions data item
        Car car = getItem(position);

        // Check if existing view is reused, or inflate view
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_car, parent, false);
        }

        //Look up view to populate
        TextView tvMake = (TextView) convertView.findViewById(R.id.tvMake);
        TextView tvModel = (TextView) convertView.findViewById(R.id.tvModel);
        TextView tvYear = (TextView) convertView.findViewById(R.id.tvYear);
        TextView tvColor = (TextView) convertView.findViewById(R.id.tvColor);
        TextView tvRented = (TextView) convertView.findViewById(R.id.tvRented);
        TextView tvRentalID = (TextView) convertView.findViewById(R.id.tvRentalID);

        // Populate data into template
        tvMake.setText(car.getMake());
        tvModel.setText(car.getModel());
        tvYear.setText(Integer.toString(car.getYear()));
        tvColor.setText(car.getColor());
        if (car.getRented()) {
            tvRented.setText(R.string.rented);
        }else{
            tvRented.setText(R.string.available);
        }
        tvRentalID.setText(car.getRentalID());

        // Return view to render
        return convertView;
    }

}
