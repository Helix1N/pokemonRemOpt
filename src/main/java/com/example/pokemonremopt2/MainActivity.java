package com.example.pokemonremopt2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {


    //vars
    public static final int CAMERA_PERMISSION_CODE = 100;

    //widgets
    private Button camera;
    /*private Button generate;
    private Button scan;
    private TextView textViewTest;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        camera = findViewById(R.id.cameraPermission);

        /*generate = findViewById(R.id.generate);
        scan = findViewById(R.id.scan);
        textViewTest = findViewById(R.id.texttest);*/

        /*scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, ScanActivity.class);
                startActivity(intent);
            }
        });*/

        /*generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewTest.setText("wabalublub");
            }
        });*/

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionAndRun(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
            }
        });
        /*generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GenerateCode.class);
                startActivity(intent);
            }
        });*/
    }
    public void checkPermissionAndRun(String permission, int requestCode){
        if(ContextCompat.checkSelfPermission(MainActivity.this, permission)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {permission},
                    requestCode);
        }
        else{
            Intent intent = new Intent(MainActivity.this, ScanActivity.class);
            startActivity(intent);
            //Toast.makeText(this, "Permission Already Granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CAMERA_PERMISSION_CODE){
            if(grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}



