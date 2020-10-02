package com.example.myfilms.feature.movies.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.myfilms.R;
import com.example.myfilms.feature.movies.domain.entity.Movie;
import com.example.myfilms.feature.movies.presentation.ViewModel.MainActivityViewModel;
import com.example.myfilms.feature.movies.presentation.recycler.MovieAdapter;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.myfilms.feature.movies.presentation.constants.BundlesKeyConstants.IMAGE_MOVIE_DESCRIPTION_ACTIVITY_KEY;
import static com.example.myfilms.feature.movies.presentation.constants.BundlesKeyConstants.TITLE_MOVIE_DESCRIPTION_ACTIVITY_KEY;
import static com.example.myfilms.feature.movies.presentation.constants.BundlesKeyConstants.TYPE_MOVIE_DESCRIPTION_ACTIVITY_KEY;
import static com.example.myfilms.feature.movies.presentation.constants.BundlesKeyConstants.YEAR_MOVIE_DESCRIPTION_ACTIVITY_KEY;

public class MainActivity extends AppCompatActivity {

    private static String SEARCH_TAG_ERROR = "SearchError";
    private static String NO_MOVIES_FOUNDED_MESSAGE_CONSTANTS = "Filmes não encontrados";
    private static String DATABASE_TAG_ERROR_CONSTANTS = "DatabaseError";
    private static String ERROR_TO_ADD_MOVIES_ON_DATABASE_MESSAGE_CONSTANTS =
            "Error to add a movie on a database";
    private static String MOVIES_ALREADY_EXISTING_MESSAGE_CONSTANT = "Filme já existente";
    private static String ONE_MOVIE_REGISTERED_EXISTING_MESSAGE_CONSTANT = "Filme cadastrado";
    private static String MOVIES_REGISTERED_EXISTING_MESSAGE_CONSTANT = "Filmes cadastrados";
    private static String SEARCH_ERROR_MESSAGE_CONSTANT = "Texto não pode ser vazio";

    private Button searchButton;
    private EditText searchEditText;
    private RecyclerView recyclerView;
    private List<Movie> moviesExisting = new ArrayList<>(0);
    private MovieAdapter movieAdapter;
    private MainActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        findViewsById();
        getSavedMovies();
        configSearchButtonListener();
        configRecycleView();
        configObservers();

    }

    private void findViewsById() {
        searchButton = findViewById(R.id.botaoBuscaId);
        searchEditText = findViewById(R.id.editTextBuscaID);
        recyclerView = findViewById(R.id.recyclerViewBuscaID);
    }

    private void getSavedMovies() {
        try {
            viewModel.getMovies();
        } catch (Exception exception) {
            Log.e(DATABASE_TAG_ERROR_CONSTANTS, Objects.requireNonNull(exception.getMessage()));
        }
    }

    private void configSearchButtonListener() {
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String typedText = searchEditText.getText().toString();
                hideKeyboardWhenSearchButtonWasClicked();
                if (typedText.isEmpty())
                    showToastMessage(SEARCH_ERROR_MESSAGE_CONSTANT);
                else {
                    searchMovies(typedText);
                }
            }
        });
    }

    private void hideKeyboardWhenSearchButtonWasClicked(){
        ((InputMethodManager) Objects.requireNonNull(
                this.getSystemService(Context.INPUT_METHOD_SERVICE))
        ).hideSoftInputFromWindow(
                searchButton.getWindowToken(), 0
        );
    }

    private void searchMovies(String text) {
        try{
            viewModel.searchMovies(text);
        } catch (Exception exception){
            Log.e(SEARCH_TAG_ERROR, Objects.requireNonNull(exception.getMessage()));
        }
    }

    private void configRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        movieAdapter = new MovieAdapter();
        recyclerView.setAdapter(movieAdapter);
        movieAdapter.setOnClickListener(createRecyclerListener());
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        );
    }

    private View.OnClickListener createRecyclerListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Movie movieCLiked = moviesExisting.get(recyclerView.getChildAdapterPosition(v));
                Intent descriptionIntent = createDescriptionIntent(movieCLiked);
                startActivity(descriptionIntent);
            }
        };
    }

    private Intent createDescriptionIntent(Movie movie) {
        Intent intent = new Intent(
                MainActivity.this, MovieDescriptionActivity.class
        );
        intent.putExtra(
                TITLE_MOVIE_DESCRIPTION_ACTIVITY_KEY,
                movie.getTitle()
        );
        intent.putExtra(
                YEAR_MOVIE_DESCRIPTION_ACTIVITY_KEY,
                movie.getYear()
        );
        intent.putExtra(
                IMAGE_MOVIE_DESCRIPTION_ACTIVITY_KEY,
                movie.getImage()
        );
        intent.putExtra(
                TYPE_MOVIE_DESCRIPTION_ACTIVITY_KEY,
                movie.getType()
        );
        return intent;
    }

    private void configObservers(){
        configExceptionObserver();
        configSearchMoviesObserver();
        configMoviesLiveData();
    }

    private void configExceptionObserver(){
        viewModel.getExceptionLiveData().observe(this, new Observer<Exception>() {
            @Override
            public void onChanged(Exception e) {
                showToastMessage(e.getMessage());
            }
        });
    }

    private void configMoviesLiveData(){
        viewModel.getMoviesLiveData().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                moviesExisting = movies;
                movieAdapter.insertItems(movies);
            }
        });
    }

    private void configSearchMoviesObserver(){
        viewModel.getSearchMoviesLiveData().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                if(moviesIsNotNull(movies)){
                    addMoviesOnMoviesExisting(movies);
                } else {
                    noMoviesFounded();
                }
            }
        });
    }

    private Boolean moviesIsNotNull(List<Movie> movies) {
        return movies!=null;
    }

    private void noMoviesFounded() {
        showToastMessage(NO_MOVIES_FOUNDED_MESSAGE_CONSTANTS);
    }

    private void addMoviesOnMoviesExisting(List<Movie> moviesResult) {
        int numberOfMoviesAdded = addDifferentMoviesOnMoviesExisting(moviesResult);
        displayMessageWhenMoviesWasAdded(numberOfMoviesAdded);
    }

    private int addDifferentMoviesOnMoviesExisting(List<Movie> moviesResult) {
        int numberOfMoviesAdded = 0;
        for (Movie movie : moviesResult) {
            if (movieIsNotOnMoviesExistingVariable(movie)) {
                downloadImage(movie);
                numberOfMoviesAdded++;
            }
        }
        return numberOfMoviesAdded;
    }

    private Boolean movieIsNotOnMoviesExistingVariable(Movie movie) {
        return moviesExisting.size() == 0 || !moviesExisting.contains(movie);
    }

    private void displayMessageWhenMoviesWasAdded(int numberOfMoviesAdded) {
        if (noMoviesAdded(numberOfMoviesAdded))
            showToastMessage(MOVIES_ALREADY_EXISTING_MESSAGE_CONSTANT);
        else if (oneMovieAdded(numberOfMoviesAdded))
            showToastMessage(ONE_MOVIE_REGISTERED_EXISTING_MESSAGE_CONSTANT);
        else
            showToastMessage(MOVIES_REGISTERED_EXISTING_MESSAGE_CONSTANT);
    }

    private Boolean noMoviesAdded(int numberOfMoviesAdded) {
        return numberOfMoviesAdded == 0;
    }

    private Boolean oneMovieAdded(int numberOfMoviesAdded) {
        return numberOfMoviesAdded == 1;
    }

    private void downloadImage(final Movie movie) {
        Glide.with(this)
                .asBitmap()
                .load(movie.getPoster())
                .into(new CustomTarget<Bitmap>(100, 100) {
                    @Override
                    public void onResourceReady(
                            @NonNull Bitmap resource,
                            @Nullable Transition<? super Bitmap> transition
                    ) {
                        movie.setImage(compressImageFromBitmap(resource));
                        configMovieOnDatabaseAndAdapter(movie);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }

    private byte[] compressImageFromBitmap(Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private void configMovieOnDatabaseAndAdapter(Movie movie) {
        movieAdapter.insertItem(movie);
        moviesExisting.add(movie);
        saveMovieOnDatabase(movie);
    }

    private void saveMovieOnDatabase(Movie movie) {
        try {
            viewModel.insertMovie(movie);
        } catch (Exception exception) {
            Log.e(DATABASE_TAG_ERROR_CONSTANTS,ERROR_TO_ADD_MOVIES_ON_DATABASE_MESSAGE_CONSTANTS);
        }
    }

    private void showToastMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
