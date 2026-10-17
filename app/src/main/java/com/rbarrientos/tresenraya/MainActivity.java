package com.rbarrientos.tresenraya;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button iniciarButton = findViewById(R.id.iniciarButton);
        iniciarButton.setOnClickListener(oyenteIniciarButton);
    }

    private final View.OnClickListener oyenteIniciarButton = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(MainActivity.this, TresEnRayaActivity.class);
            startActivity(i);
        }
    };
}