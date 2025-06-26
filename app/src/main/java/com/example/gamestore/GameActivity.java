package com.example.gamestore;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.gamestore.adapters.GameAdapter;
import com.example.gamestore.models.Game;
import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity {
    private ListView listView;
    private List<Game> gameList;
    private int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_game);

        listView = findViewById(R.id.listViewGames);
        Button btnOrder = findViewById(R.id.btnOrderGame);

        gameList = new ArrayList<>();
        gameList.add(new Game("Cyberpunk 2077", "Open world RPG", 60, R.drawable.game1));
        gameList.add(new Game("FIFA 24", "Football simulation", 70, R.drawable.game2));
        gameList.add(new Game("Call of Duty MW3", "First person shooter", 80, R.drawable.game3));
        gameList.add(new Game("Minecraft", "Sandbox game", 30, R.drawable.game4));

        GameAdapter adapter = new GameAdapter(this, gameList);
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
                Game selected = gameList.get(selectedPosition);
                Intent intent = new Intent();
                intent.putExtra("name", selected.getName());
                intent.putExtra("price", selected.getPrice());
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(this, "Please select a game", Toast.LENGTH_SHORT).show();
            }
        });
    }
}