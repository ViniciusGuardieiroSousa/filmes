package com.example.myfilms;

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
import com.example.myfilms.database.DatabaseFactory;
import com.example.myfilms.database.MovieDatabase;
import com.example.myfilms.exceptions.DatabaseException;
import com.example.myfilms.retrofit.APIFactory;
import com.example.myfilms.retrofit.NetworkAPI;
import com.example.myfilms.retrofit.SearchFilmes;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.myfilms.constants.BundlesKeyConstants.IMAGE_MOVIE_DESCRIPTION_ACTIVITY_KEY;
import static com.example.myfilms.constants.BundlesKeyConstants.TITLE_MOVIE_DESCRIPTION_ACTIVITY_KEY;
import static com.example.myfilms.constants.BundlesKeyConstants.TYPE_MOVIE_DESCRIPTION_ACTIVITY_KEY;
import static com.example.myfilms.constants.BundlesKeyConstants.YEAR_MOVIE_DESCRIPTION_ACTIVITY_KEY;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "erro";
    private Button searchButton;
    private EditText searchEditText;
    private RecyclerView recyclerView;
    private SearchList searchList;
    private ArrayList<Search> moviesExisting = new ArrayList<>(0);
    private Context context;
    private MovieAdapter movieAdapter;
    private MovieDatabase database;
    private NetworkAPI networkAPI;
    private SearchFilmes searchFilmes;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        findViewsById();
        configDatabaseAndGetMoviesSaved();
        configNetworkAPI();
        configRecycleView();
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textoDigitado = searchEditText.getText().toString();
                ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                        searchButton.getWindowToken(), 0);
                if (textoDigitado == null)
                    Toast.makeText(getApplicationContext(), "Texto não pode ser vazio", Toast.LENGTH_LONG).show();
                else {
                    textoDigitado = treatBlankSpaceOnSearchText(textoDigitado);
                    searchMovies(textoDigitado);
                }
            }
        });
    }

    private void findViewsById() {
        searchButton = findViewById(R.id.botaoBuscaId);
        searchEditText = findViewById(R.id.editTextBuscaID);
        recyclerView = findViewById(R.id.recyclerViewBuscaID);
    }

    private void configDatabaseAndGetMoviesSaved() {
        try {
            database = DatabaseFactory.getMovieDataBase(this, "Movies", MODE_PRIVATE);
            moviesExisting = database.getMovies();
        } catch (DatabaseException exception) {
            Log.e("DatabaseError", exception.getMessage());
        }
    }

    private void configNetworkAPI(){
        networkAPI = APIFactory.getAPI();
        searchFilmes = networkAPI.getSearchMovies();
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
                Intent intent = new Intent(
                        MainActivity.this, MovieDescriptionActivity.class
                );
                intent.putExtra(
                        TITLE_MOVIE_DESCRIPTION_ACTIVITY_KEY,
                        moviesExisting.get(recyclerView.getChildAdapterPosition(v)).getTitle()
                );
                intent.putExtra(
                        YEAR_MOVIE_DESCRIPTION_ACTIVITY_KEY,
                        moviesExisting.get(recyclerView.getChildAdapterPosition(v)).getYear()
                );
                intent.putExtra(
                        IMAGE_MOVIE_DESCRIPTION_ACTIVITY_KEY,
                        moviesExisting.get(recyclerView.getChildAdapterPosition(v)).getImage()
                );
                intent.putExtra(
                        TYPE_MOVIE_DESCRIPTION_ACTIVITY_KEY,
                        moviesExisting.get(recyclerView.getChildAdapterPosition(v)).getType()
                );
                startActivity(intent);
            }
        };
    }

    private String treatBlankSpaceOnSearchText(String texto) {
        StringBuilder retorno = new StringBuilder();
        for (int i = 0; i < texto.length(); i++) {
            if (texto.charAt(i) == ' ') {
                retorno.append("+");
            } else {
                retorno.append(texto.charAt(i));
            }
        }
        return retorno.toString();
    }

    private void searchMovies(String textoDigitado) {
        Call<SearchList> moviesSearched = searchFilmes.getSearch(textoDigitado);
        moviesSearched.enqueue(new Callback<SearchList>() {
            @Override
            public void onResponse(Call<SearchList> call, Response<SearchList> response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "erro: " + response.code());
                } else {
                    searchList = response.body();
                    int cnt = 0;
                    if (searchList.filmes != null) {
                        for (Search c : searchList.filmes) {
                            if (moviesExisting.size() == 0 || !moviesExisting.contains(c)) {
                                downloadImage(c);
                                cnt++;
                            }
                        }
                        if (cnt == 0)
                            Toast.makeText(getApplicationContext(), "Filme já existente", Toast.LENGTH_LONG).show();
                        else if (cnt == 1)
                            Toast.makeText(getApplicationContext(), "Filme cadastrado", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getApplicationContext(), "Filmes cadastrados", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Filme não encontrado", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchList> call, Throwable t) {
                Log.e(TAG, "erro: " + t.getMessage());
            }
        });
    }

    private void downloadImage(final Search movie) {
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

    private byte[] compressImageFromBitmap(Bitmap image){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private void configMovieOnDatabaseAndAdapter(Search movie){
        movieAdapter.insertItem(movie);
        saveMovieOnDatabase(movie);
    }

    private void saveMovieOnDatabase(Search movie){
        try {
            database.insertMovie(movie);
        } catch (DatabaseException exception) {
            Log.e("DatabaseError", "Error to add a movie on a database");
        }
    }


}
