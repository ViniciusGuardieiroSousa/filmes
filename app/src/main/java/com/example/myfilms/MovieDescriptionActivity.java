package com.example.myfilms;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.myfilms.BundlesKeyConstants.IMAGE_MOVIE_DESCRIPTION_ACTIVITY_KEY;
import static com.example.myfilms.BundlesKeyConstants.TITLE_MOVIE_DESCRIPTION_ACTIVITY_KEY;
import static com.example.myfilms.BundlesKeyConstants.TYPE_MOVIE_DESCRIPTION_ACTIVITY_KEY;
import static com.example.myfilms.BundlesKeyConstants.YEAR_MOVIE_DESCRIPTION_ACTIVITY_KEY;

public class MovieDescriptionActivity extends AppCompatActivity {
    private static final int IMAGE_OFFSET = 0;
    private TextView movieTitleTextView, movieYearTextView, movieTypeTextView;
    private ImageView moviePosterImage;
    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_description);
        findViewsById();
        extras = getIntent().getExtras();
        setViewBundleInformationIfExtraIsNotNull();
    }

    private void findViewsById() {
        movieTitleTextView = findViewById(R.id.movieTitleDescriptionActivityID);
        movieYearTextView = findViewById(R.id.movieYearDescriptionActivityId);
        moviePosterImage = findViewById(R.id.moviePosterImageDescriptionActivityId);
        movieTypeTextView = findViewById(R.id.movieTypeDescriptionActivityId);
    }

    private void setViewBundleInformationIfExtraIsNotNull() {
        if (extrasIsNotNull()) {
            setViewBundleInformation();
        }
    }

    private boolean extrasIsNotNull() {
        return extras != null;
    }

    private void setViewBundleInformation() {
        setTextWithBundleInformation(movieTitleTextView, TITLE_MOVIE_DESCRIPTION_ACTIVITY_KEY);
        setTextWithBundleInformation(movieYearTextView, YEAR_MOVIE_DESCRIPTION_ACTIVITY_KEY);
        setTextWithBundleInformation(movieTypeTextView, TYPE_MOVIE_DESCRIPTION_ACTIVITY_KEY);
        setImageWithBundleInformation(
                moviePosterImage,
                IMAGE_MOVIE_DESCRIPTION_ACTIVITY_KEY,
                IMAGE_OFFSET
        );

    }

    private void setTextWithBundleInformation(TextView textView, String key) {
        String extraString = extras.getString(key);
        if (extraString != null)
            textView.setText(extraString);
    }

    private void setImageWithBundleInformation(ImageView imageView, String key, int imageOffset) {
        byte[] imageByteArray = extras.getByteArray(key);
        if(imageByteArray != null){
            Bitmap imageDecoded = BitmapFactory
                    .decodeByteArray(imageByteArray, imageOffset, imageByteArray.length);
            imageView.setImageBitmap(imageDecoded);
        }

    }


}
