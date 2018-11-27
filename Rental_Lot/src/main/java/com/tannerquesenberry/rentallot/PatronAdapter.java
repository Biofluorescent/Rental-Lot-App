package com.tannerquesenberry.rentallot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by tannerquesenberry on 3/13/18.
 */

public class PatronAdapter extends ArrayAdapter<Patron>{
    public PatronAdapter(Context context, ArrayList<Patron> patrons){
        super(context, 0, patrons);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        // Positions data item
        Patron patron = getItem(position);

        // Check if existing view is reused, or inflate view
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_patron, parent, false);
        }

        //Look up view to populate
        TextView tvFName = (TextView) convertView.findViewById(R.id.tvFName);
        TextView tvLName = (TextView) convertView.findViewById(R.id.tvLName);
        TextView tvAge = (TextView) convertView.findViewById(R.id.tvAge);
        TextView tvCarRentalID = (TextView) convertView.findViewById(R.id.tvCarRentalID);
        TextView tvPatronID = (TextView) convertView.findViewById(R.id.tvPatronID);

        // Populate data into template
        tvFName.setText(patron.getFname());
        tvLName.setText(patron.getLname());
        tvAge.setText(Integer.toString(patron.getAge()));
        if (patron.getCarRentalID().equals("")){
            tvCarRentalID.setText("Not Renting");
        }else {
            tvCarRentalID.setText(patron.getCarRentalID());
        }
        tvPatronID.setText(patron.getPatronID());

        // Return view to render
        return convertView;
    }

}
