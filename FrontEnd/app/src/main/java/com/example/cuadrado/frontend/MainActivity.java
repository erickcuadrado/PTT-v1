package com.example.cuadrado.frontend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button siguiente;
    EditText et1;
    TextView tv1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et1=(EditText)findViewById(R.id.usuario);
        siguiente= (Button)findViewById(R.id.btnHome);
        siguiente.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent siguiente = new Intent(MainActivity.this, Main2Activity.class);
                siguiente.putExtra("Hola", et1.getText().toString());
                startActivity(siguiente);
            }
        });
    }

}
