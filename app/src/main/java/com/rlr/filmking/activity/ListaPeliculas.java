package com.rlr.filmking.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rlr.filmking.R;
import com.rlr.filmking.adapter.AdaptadorPelicula;
import com.rlr.filmking.model.Pelicula;
import com.rlr.filmking.model.PeliculaResponse;
import com.rlr.filmking.rest.ApiClient;
import com.rlr.filmking.rest.ApiInterface;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Raúl on 17/02/2017.
 */

public class ListaPeliculas extends Fragment {

    private final static String API_KEY = "cd9b31ae50149fdf24f9642d2d0c08c4";
    private String Language = "es-ES";
    private ArrayList<Pelicula> datos;

    @BindView(R.id.peliculas)
    protected RecyclerView recView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View content = LayoutInflater.from(getContext()).inflate(R.layout.lista_peliculas, container, false);
        ButterKnife.bind(this, content);

        final FragmentManager fragmentManager = getFragmentManager();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<PeliculaResponse> call = apiService.getMoviesNowPlaying(API_KEY, Language);
        call.enqueue(new Callback<PeliculaResponse>() {
            @Override
            public void onResponse(Call<PeliculaResponse> call, Response<PeliculaResponse> response) {
                int statusCode = response.code();

                datos = response.body().getResults();

                recView.setHasFixedSize(true);
                recView.setItemAnimator(new DefaultItemAnimator());
                recView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                recView.setAdapter(new AdaptadorPelicula(datos, fragmentManager));

            }

            @Override
            public void onFailure(Call<PeliculaResponse> call, Throwable t) {

            }
        });
        return content;
    }
}
