package com.example.gamestore;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.gamestore.adapters.GameAdapter;
import com.example.gamestore.models.CartItem;
import com.example.gamestore.models.Game;
import com.example.gamestore.repository.CartRepository;
import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity {
    private ListView listView;
    private List<Game> gameList;
    private int selectedPosition = -1;
    private CartRepository cartRepository;

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

        // Initialize cart repository
        cartRepository = new CartRepository(getApplication());

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

                // Add to cart instead of returning to MainActivity
                addToCart(selected);
            } else {
                Toast.makeText(this, "Please select a game", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addToCart(Game game) {
        // Create CartItem from Game
        CartItem cartItem = new CartItem(
                game.getName(),
                game.getDescription(),
                game.getPrice(),
                1, // Default quantity
                "game",
                game.getImageResource()
        );

        // Add to cart using multi-threading
        cartRepository.addToCart(cartItem, new CartRepository.DataCallback<Boolean>() {
            @Override
            public void onComplete(Boolean result) {
                Toast.makeText(GameActivity.this,
                        game.getName() + " added to cart!", Toast.LENGTH_SHORT).show();

                // Return to MainActivity
                Intent intent = new Intent();
                intent.putExtra("name", game.getName());
                intent.putExtra("price", game.getPrice());
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(GameActivity.this,
                        "Error adding to cart: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}