package com.example.minhacarteira;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Ver fragmento 1
        getSupportFragmentManager().beginTransaction().add(R.id.container_newSpending, new NewSpending()).commit();
        //Ver fragmento 2
        getSupportFragmentManager().beginTransaction().add(R.id.container_listBudget, new ListSpending()).commit();
    }
}