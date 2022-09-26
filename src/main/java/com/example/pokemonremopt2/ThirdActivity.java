package com.example.pokemonremopt2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pokemonremopt2.databinding.ActivityThirdBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
public class ThirdActivity extends AppCompatActivity {

    ActivityThirdBinding binding;
    ArrayList<String> pokemonList;
    Handler handler = new Handler();
    ArrayAdapter<String> listAdapter;
    ProgressDialog progressDialog;
    Button rBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageView test = findViewById(R.id.pokemonImg);
        binding = ActivityThirdBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initializeUserList();
        new FecthData().start();

    }

    private void initializeUserList() {
        pokemonList = new ArrayList<>();
        listAdapter = new ArrayAdapter<String>(this, R.layout.custom_textview, pokemonList);
        binding.pokemonList.setAdapter(listAdapter);
    }

    class FecthData extends Thread {
        private String data = "";
        private TextView text = findViewById(R.id.text);
        private String pokemonId = getIntent().getStringExtra("keyId");
        private ImageView pokemonImg = findViewById(R.id.pokemonImg);
        private String pokemonUrlImage;



        @Override
        public void run() {
            rBtn = findViewById(R.id.rBtn);
            rBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ThirdActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });
            text.setText(text.getText() + pokemonId);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog = new ProgressDialog(ThirdActivity.this);
                    progressDialog.setMessage("Fetching Data");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            });

            try {
                URL urlPokemon = new URL(  "https://pokeapi.co/api/v2/pokemon/" + pokemonId);
                HttpURLConnection conexao = (HttpURLConnection) urlPokemon.openConnection();
                InputStream inputStream = conexao.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    data = data + line;
                }

                if (!data.isEmpty()) {
                    pokemonList.clear();
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray typesArray = jsonObject.getJSONArray("types");
                    String typeName = "Type: ";
                    String name = "Name: ";
                    JSONObject sprites = jsonObject.getJSONObject("sprites");
                    pokemonUrlImage = sprites.getString("front_default");

                    name = name + jsonObject.getString("name").toUpperCase();
                    pokemonList.add(name);
                    for (int i = 0; i < typesArray.length(); i++) {
                        JSONObject slotObject = typesArray.getJSONObject(i);
                        JSONObject typeObject = slotObject.getJSONObject("type");
                        if ((i + 1) == typesArray.length()) {
                            typeName = typeName + typeObject.getString("name").toUpperCase();
                        }
                        else {
                            typeName = typeName + typeObject.getString("name").toUpperCase() + ", ";
                        }

                    }
                    pokemonList.add(typeName);




                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                text.setText("ERROR1");
            } catch (IOException e) {
                e.printStackTrace();
                text.setText("ERROR2");
            } catch (JSONException e) {
                e.printStackTrace();
                text.setText("ERROR3");
            }



            handler.post(new Runnable() {
                @Override
                public void run() {
                    Picasso.get()
                            .load(pokemonUrlImage)
                            .into(pokemonImg);
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        listAdapter.notifyDataSetChanged();
                    }
                }
            });
        }


    }
}