package com.example.gamestore.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.gamestore.R;
import com.example.gamestore.models.Accessory;
import java.util.List;

public class AccessoryAdapter extends ArrayAdapter<Accessory> {
    private Context context;
    private List<Accessory> accessories;

    public AccessoryAdapter(Context context, List<Accessory> accessories) {
        super(context, 0, accessories);
        this.context = context;
        this.accessories = accessories;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_accessory, parent, false);
        }

        Accessory accessory = accessories.get(position);

        ImageView imgAccessory = convertView.findViewById(R.id.imgAccessory);
        TextView tvName = convertView.findViewById(R.id.tvAccessoryName);
        TextView tvDescription = convertView.findViewById(R.id.tvAccessoryDescription);
        TextView tvPrice = convertView.findViewById(R.id.tvAccessoryPrice);

        imgAccessory.setImageResource(accessory.getImageResource());
        tvName.setText(accessory.getName());
        tvDescription.setText(accessory.getDescription());
        tvPrice.setText("$" + accessory.getPrice());

        return convertView;
    }
}