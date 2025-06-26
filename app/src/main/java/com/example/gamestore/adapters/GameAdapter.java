package com.example.gamestore.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.gamestore.R;
import com.example.gamestore.models.Game;
import java.util.List;

public class GameAdapter extends ArrayAdapter<Game> {
    private Context context;
    private List<Game> games;

    public GameAdapter(Context context, List<Game> games) {
        super(context, 0, games);
        this.context = context;
        this.games = games;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_game, parent, false);
        }

        Game game = games.get(position);

        ImageView imgGame = convertView.findViewById(R.id.imgGame);
        TextView tvName = convertView.findViewById(R.id.tvGameName);
        TextView tvDescription = convertView.findViewById(R.id.tvGameDescription);
        TextView tvPrice = convertView.findViewById(R.id.tvGamePrice);

        imgGame.setImageResource(game.getImageResource());
        tvName.setText(game.getName());
        tvDescription.setText(game.getDescription());
        tvPrice.setText("$" + game.getPrice());

        return convertView;
    }
}