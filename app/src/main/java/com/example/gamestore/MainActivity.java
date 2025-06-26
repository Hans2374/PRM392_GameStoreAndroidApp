package com.example.gamestore;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_GAME = 1;
    private static final int REQUEST_ACCESSORY = 2;

    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = findViewById(R.id.tvResult);
        Button btnSelectGame = findViewById(R.id.btnSelectGame);
        Button btnSelectAccessory = findViewById(R.id.btnSelectAccessory);
        Button btnExit = findViewById(R.id.btnExit);

        btnSelectGame.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            startActivityForResult(intent, REQUEST_GAME);
        });

        btnSelectAccessory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AccessoryActivity.class);
            startActivityForResult(intent, REQUEST_ACCESSORY);
        });

        btnExit.setOnClickListener(v -> finish());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            String name = data.getStringExtra("name");
            int price = data.getIntExtra("price", 0);

            if (requestCode == REQUEST_GAME) {
                tvResult.setText("Game: " + name + " - $" + price);
            } else if (requestCode == REQUEST_ACCESSORY) {
                tvResult.setText("Accessory: " + name + " - $" + price);
            }
        }
    }
}