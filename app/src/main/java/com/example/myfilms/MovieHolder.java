package com.example.myfilms;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MovieHolder extends RecyclerView.ViewHolder {

    private TextView title;
    private ImageView poster;

    public MovieHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.textItemId);
        poster = itemView.findViewById(R.id.imageItemId);
    }

    public void bind(Search item) {
        title.setText(item.getTitle());
        Bitmap imagePoster = BitmapFactory.decodeByteArray(
                item.getImage(),
                0,
                item.getImage().length
        );
        poster.setImageBitmap(imagePoster);
    }


}
