package com.example.myfilms;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MovieHolder extends RecyclerView.ViewHolder {

    private TextView movieTitle;
    private ImageView moviePoster;

    public MovieHolder(@NonNull View itemView) {
        super(itemView);
        movieTitle = itemView.findViewById(R.id.textItemId);
        moviePoster = itemView.findViewById(R.id.imageItemId);
    }

    public void bind(Search item) {
        movieTitle.setText(item.getTitle());
        Bitmap imagePoster = BitmapFactory.decodeByteArray(
                item.getImage(),
                0,
                item.getImage().length
        );
        moviePoster.setImageBitmap(imagePoster);
    }


}
