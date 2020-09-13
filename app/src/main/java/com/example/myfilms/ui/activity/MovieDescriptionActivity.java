package com.example.myfilms.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myfilms.R;

import static com.example.myfilms.constants.BundlesKeyConstants.IMAGE_MOVIE_DESCRIPTION_ACTIVITY_KEY;
import static com.example.myfilms.constants.BundlesKeyConstants.TITLE_MOVIE_DESCRIPTION_ACTIVITY_KEY;
import static com.example.myfilms.constants.BundlesKeyConstants.TYPE_MOVIE_DESCRIPTION_ACTIVITY_KEY;
import static com.example.myfilms.constants.BundlesKeyConstants.YEAR_MOVIE_DESCRIPTION_ACTIVITY_KEY;

public class MovieDescriptionActivity extends AppCompatActivity {
    private static final int IMAGE_OFFSET = 0;
    private TextView movieTitle, movieYear, movieType;
    private ImageView moviePoster;
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
        movieTitle = findViewById(R.id.movieTitleDescriptionActivityID);
        movieYear = findViewById(R.id.movieYearDescriptionActivityId);
        moviePoster = findViewById(R.id.moviePosterImageDescriptionActivityId);
        movieType = findViewById(R.id.movieTypeDescriptionActivityId);
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
        setTextWithBundleInformation(movieTitle, TITLE_MOVIE_DESCRIPTION_ACTIVITY_KEY);
        setTextWithBundleInformation(movieYear, YEAR_MOVIE_DESCRIPTION_ACTIVITY_KEY);
        setTextWithBundleInformation(movieType, TYPE_MOVIE_DESCRIPTION_ACTIVITY_KEY);
        setImageWithBundleInformation();

    }

    private void setTextWithBundleInformation(TextView textView, String key) {
        String extra = extras.getString(key);
        if (extra != null)
            textView.setText(extra);
    }

    private void setImageWithBundleInformation() {
        byte[] imageByteArray = extras.getByteArray(IMAGE_MOVIE_DESCRIPTION_ACTIVITY_KEY);
        if(imageByteArray != null){
            Bitmap imageDecoded = BitmapFactory
                    .decodeByteArray(imageByteArray, IMAGE_OFFSET, imageByteArray.length);
            moviePoster.setImageBitmap(imageDecoded);
        }
    }


}
