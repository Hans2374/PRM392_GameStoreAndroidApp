package com.example.gamestore;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.gamestore.adapters.AccessoryAdapter;
import com.example.gamestore.models.Accessory;
import java.util.ArrayList;
import java.util.List;

public class AccessoryActivity extends AppCompatActivity {
    private ListView listView;
    private List<Accessory> accessoryList;
    private int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_accessory);

        listView = findViewById(R.id.listViewAccessories);
        Button btnOrder = findViewById(R.id.btnOrderAccessory);

        accessoryList = new ArrayList<>();
        accessoryList.add(new Accessory("Gaming Headset", "7.1 Surround Sound", 120, R.drawable.accessory1));
        accessoryList.add(new Accessory("Mechanical Keyboard", "RGB Backlit, Cherry MX", 150, R.drawable.accessory2));
        accessoryList.add(new Accessory("Gaming Mouse", "16000 DPI, Wireless", 80, R.drawable.accessory3));
        accessoryList.add(new Accessory("Controller", "Wireless Xbox Controller", 60, R.drawable.accessory4));

        AccessoryAdapter adapter = new AccessoryAdapter(this, accessoryList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            selectedPosition = position;
            // Highlight selected item
            for (int i = 0; i < parent.getChildCount(); i++) {
                parent.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
            }
            view.setBackgroundColor(Color.LTGRAY);
        });

        btnOrder.setOnClickListener(v -> {
            if (selectedPosition != -1) {
                Accessory selected = accessoryList.get(selectedPosition);
                Intent intent = new Intent();
                intent.putExtra("name", selected.getName());
                intent.putExtra("price", selected.getPrice());
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(this, "Please select an accessory", Toast.LENGTH_SHORT).show();
            }
        });
    }
}