package com.tannerquesenberry.rentallot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Button to view cars
        Button viewCars = (Button) findViewById(R.id.button);
        viewCars.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, ViewCars.class);
                startActivity(intent);
            }
        });

        // Button to create cars
        Button createCars = (Button) findViewById(R.id.button5);
        createCars.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, PostCar.class);
                startActivity(intent);
            }
        });

        // Button to view patrons
        Button viewPatrons = (Button) findViewById(R.id.button2);
        viewPatrons.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, ViewPatrons.class);
                startActivity(intent);
            }
        });

        // Button to create patrons
        Button createPatrons = (Button) findViewById(R.id.button3);
        createPatrons.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, PostPatron.class);
                startActivity(intent);
            }
        });

        // Button to delete cars
        Button deleteCar = (Button) findViewById(R.id.button4);
        deleteCar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, DeleteCar.class);
                startActivity(intent);
            }
        });

        // Button to delete patrons
        Button deletePatron = (Button) findViewById(R.id.button6);
        deletePatron.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, DeletePatron.class);
                startActivity(intent);
            }
        });

        // Button to modify patrons
        Button modifyPatron = (Button) findViewById(R.id.button8);
        modifyPatron.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, ModifyPatron.class);
                startActivity(intent);
            }
        });

        // Button to modify cars
        Button modifyCar = (Button) findViewById(R.id.button7);
        modifyCar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, ModifyCar.class);
                startActivity(intent);
            }
        });

        // Button to rent car
        Button rentCar = (Button) findViewById(R.id.button9);
        rentCar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, RentCar.class);
                startActivity(intent);
            }
        });

        // Button to return car
        Button returnCar = (Button) findViewById(R.id.button10);
        returnCar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, ReturnCar.class);
                startActivity(intent);
            }
        });

    }
}
