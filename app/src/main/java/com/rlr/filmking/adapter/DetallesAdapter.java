package com.rlr.filmking.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rlr.filmking.R;
import com.rlr.filmking.model.Pelicula;
import com.rlr.filmking.model.PeliculaResponse;
import com.rlr.filmking.rest.ApiClient;
import com.rlr.filmking.rest.ApiInterface;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Raúl on 23/01/2017.
 */

public class DetallesAdapter extends Fragment {

    private static int movie_id;
    private final static String API_KEY = "cd9b31ae50149fdf24f9642d2d0c08c4";
    private String Language = "es-ES";

    @BindView(R.id.fondo)
    protected ImageView Fondo;

    @BindView(R.id.caratula_detalles)
    protected ImageView Caratula;

    @BindView(R.id.titulo_detalles)
    protected TextView Titulo;

    @BindView(R.id.tituloOriginal_detalles)
    protected TextView Titulo_original;

    @BindView(R.id.valoracion_detalles)
    protected TextView Valoracion;

    @BindView(R.id.idiomaOriginal_detalles)
    protected TextView Idioma_original;

    @BindView(R.id.estreno_detalles)
    protected TextView Estreno;

    @BindView(R.id.popularidad_detalles)
    protected TextView Popularidad;

    @BindView(R.id.descripcion_detalles)
    protected TextView Descripcion;

    @BindView(R.id.lista_peliculasSimilares)
    protected RecyclerView peliculas_similares;

    public DetallesAdapter(){}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View content = LayoutInflater.from(getContext()).inflate(R.layout.detalles_pelicula, container, false);
        ButterKnife.bind(this, content);
        Bundle bundle = this.getArguments();
        this.movie_id = bundle.getInt("movie_id");

        final FragmentManager fragmentManager = getFragmentManager();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<Pelicula> call = apiService.getMovieDetails(this.movie_id, API_KEY, Language);
        call.enqueue(new Callback<Pelicula>() {
            @Override
            public void onResponse(Call<Pelicula> call, Response<Pelicula> pelicula) {
                int statusCode = pelicula.code();

                Picasso.with(getContext()).load("https://image.tmdb.org/t/p/w500" + pelicula.body().getBackdropPath()).into(Fondo);
                Picasso.with(getContext()).load("https://image.tmdb.org/t/p/w500" + pelicula.body().getPosterPath()).into(Caratula);
                Titulo.setText(pelicula.body().getTitle());
                Titulo_original.setText(pelicula.body().getOriginalTitle());
                Valoracion.setText(""+pelicula.body().getVoteAverage());
                Idioma_original.setText("Idioma original: " + pelicula.body().getOriginalLanguage());
                Estreno.setText("Fecha de estreno: " + pelicula.body().getReleaseDate());
                Popularidad.setText("Popularidad: " + pelicula.body().getPopularity());
                Descripcion.setText(pelicula.body().getOverview());
            }

            @Override
            public void onFailure(Call<Pelicula> call, Throwable t) {

            }
        });

        ApiInterface apiService2 =
                ApiClient.getClient().create(ApiInterface.class);

        Call<PeliculaResponse> call2 = apiService2.getSimilarMovies(this.movie_id, API_KEY, Language);
        call2.enqueue(new Callback<PeliculaResponse>() {
            @Override
            public void onResponse(Call<PeliculaResponse> call2, Response<PeliculaResponse> response2) {
                ArrayList<Pelicula> peliculas = response2.body().getResults();
                int statusCode = response2.code();

                peliculas_similares.setHasFixedSize(true);
                peliculas_similares.setItemAnimator(new DefaultItemAnimator());
                peliculas_similares.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                peliculas_similares.setAdapter(new AdaptadorPelicula(peliculas, fragmentManager));
            }

            @Override
            public void onFailure(Call<PeliculaResponse> call2, Throwable t) {

            }
        });

        return content;
    }

    public static int getMovie_id(){return movie_id;}
}
