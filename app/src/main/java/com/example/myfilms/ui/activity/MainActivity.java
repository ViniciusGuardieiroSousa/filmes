package com.example.myfilms.ui.activity;

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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.myfilms.R;
import com.example.myfilms.exceptions.DatabaseException;
import com.example.myfilms.factory.APIFactory;
import com.example.myfilms.mapper.ListMapper;
import com.example.myfilms.mapper.ListMapperImpl;
import com.example.myfilms.mapper.Mapper;
import com.example.myfilms.mapper.NetworkMovieToMovieMapper;
import com.example.myfilms.repository.MovieRepository;
import com.example.myfilms.repository.Repository;
import com.example.myfilms.repository.database.DatabaseFactory;
import com.example.myfilms.repository.dtos.NetworkMovie;
import com.example.myfilms.repository.dtos.NetworkResult;
import com.example.myfilms.repository.retrofit.MoviesAPI;
import com.example.myfilms.ui.domainModel.Movie;
import com.example.myfilms.ui.recycler.MovieAdapter;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static
        com.example.myfilms.constants.BundlesKeyConstants.IMAGE_MOVIE_DESCRIPTION_ACTIVITY_KEY;
import static
        com.example.myfilms.constants.BundlesKeyConstants.TITLE_MOVIE_DESCRIPTION_ACTIVITY_KEY;
import static com.example.myfilms.constants.BundlesKeyConstants.TYPE_MOVIE_DESCRIPTION_ACTIVITY_KEY;
import static com.example.myfilms.constants.BundlesKeyConstants.YEAR_MOVIE_DESCRIPTION_ACTIVITY_KEY;

public class MainActivity extends AppCompatActivity {

    private Button searchButton;
    private EditText searchEditText;
    private RecyclerView recyclerView;
    private List<Movie> moviesExisting = new ArrayList<>(0);
    private Context context;
    private MovieAdapter movieAdapter;
    private Repository<Movie> movieRepository;
    private MoviesAPI moviesAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        findViewsById();
        configRepository();
        configSearchButtonListener();
        getSavedMovies();
        configRecycleView();
    }

    private void configRepository() {
        try {
            movieRepository = new MovieRepository(
                    DatabaseFactory.getMovieDataBase(
                            context,
                            "Movies",
                            MODE_PRIVATE
                    )
            );
            this.moviesAPI = APIFactory.getMoviesAPI();
        } catch (Exception exception) {
            Log.e("DatabaseError", Objects.requireNonNull(exception.getMessage()));
        }

    }

    private void findViewsById() {
        searchButton = findViewById(R.id.botaoBuscaId);
        searchEditText = findViewById(R.id.editTextBuscaID);
        recyclerView = findViewById(R.id.recyclerViewBuscaID);
    }

    private void configSearchButtonListener() {
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String typedText = searchEditText.getText().toString();
                ((InputMethodManager) Objects.requireNonNull(context.getSystemService(Context.INPUT_METHOD_SERVICE))).hideSoftInputFromWindow(
                        searchButton.getWindowToken(), 0);
                if (typedText.isEmpty())
                    Toast.makeText(getApplicationContext(), "Texto não pode ser vazio", Toast.LENGTH_LONG).show();
                else {
                    searchMovies(typedText);
                }
            }
        });
    }

    private void getSavedMovies() {
        try {
            moviesExisting = movieRepository.getSavedItems();
        } catch (DatabaseException exception) {
            Log.e("DatabaseError", Objects.requireNonNull(exception.getMessage()));
        }
    }

    private void configRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        movieAdapter = new MovieAdapter(moviesExisting);
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

    private void searchMovies(String text) {
        Call<NetworkResult> moviesSearched = moviesAPI.getSearch(text);
        moviesSearched.enqueue(new Callback<NetworkResult>() {
            @Override
            public void onResponse(Call<NetworkResult> call, Response<NetworkResult> response) {
                treatRetrofitResponse(response);
            }

            @Override
            public void onFailure(Call<NetworkResult> call, Throwable t) {
                noMoviesFounded();
            }
        });
    }

    private void treatRetrofitResponse(Response<NetworkResult> response){
        if (response.isSuccessful()) {
            NetworkResult networkResult = response.body();
            treatNetworkResult(networkResult);
        }
        else{
            noMoviesFounded();
        }
    }

    private void treatNetworkResult(NetworkResult networkResult){
        if (networkResultHaveMovies(networkResult)) {
            List<Movie> result = convertNetworkMoviesOnMovies(networkResult.movies);
            addMoviesOnMoviesExisting(result);
        } else {
            noMoviesFounded();
        }
    }

    private boolean networkResultHaveMovies(NetworkResult networkResult) {
        return networkResult != null && networkResult.movies != null;
    }

    private List<Movie> convertNetworkMoviesOnMovies(List<NetworkMovie> networkMovies){
        Mapper<NetworkMovie, Movie> networkMovieToMovieMapper = new NetworkMovieToMovieMapper();
        ListMapper<NetworkMovie, Movie> networkMovieListToMovieListMapper = new ListMapperImpl<>(networkMovieToMovieMapper);
        return networkMovieListToMovieListMapper.map(networkMovies);
    }

    private void noMoviesFounded() {
        showToastMessage("No movies founded");
    }

    private void addMoviesOnMoviesExisting(List<Movie> moviesResult) {
        int numberOfMoviesAdded = addDifferentMoviesOnMoviesExisting(moviesResult);
        displayMessageWhenMoviesWasAdded(numberOfMoviesAdded);
    }


    private int addDifferentMoviesOnMoviesExisting(List<Movie> moviesResult) {
        int numberOfMoviesAdded = 0;
        for (Movie movie : moviesResult) {
            if (movieIsntOnMoviesExistingVariable(movie)) {
                downloadImage(movie);
                numberOfMoviesAdded++;
            }
        }
        return numberOfMoviesAdded;
    }

    private Boolean movieIsntOnMoviesExistingVariable(Movie movie) {
        return moviesExisting.size() == 0 || !moviesExisting.contains(movie);
    }

    private void displayMessageWhenMoviesWasAdded(int numberOfMoviesAdded) {
        if (noMoviesAdded(numberOfMoviesAdded))
            showToastMessage("Filme já existente");
        else if (oneMovieAdded(numberOfMoviesAdded))
            showToastMessage("Filme cadastrado");
        else
            showToastMessage("Filmes cadastrados");
    }

    private Boolean noMoviesAdded(int numberOfMoviesAdded) {
        return numberOfMoviesAdded == 0;
    }

    private Boolean oneMovieAdded(int numberOfMoviesAdded) {
        return numberOfMoviesAdded == 1;
    }

    private void showToastMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private void downloadImage(final Movie movie) {
        Glide.with(context)
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
        saveMovieOnDatabase(movie);
    }

    private void saveMovieOnDatabase(Movie movie) {
        try {
            movieRepository.insertItem(movie);
        } catch (DatabaseException exception) {
            Log.e("DatabaseError", "Error to add a movie on a database");
        }
    }
}
