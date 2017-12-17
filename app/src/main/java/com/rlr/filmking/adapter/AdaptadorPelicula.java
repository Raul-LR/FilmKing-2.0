package com.rlr.filmking.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.rlr.filmking.R;
import com.rlr.filmking.model.Pelicula;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.*;

public class AdaptadorPelicula
        extends RecyclerView.Adapter<AdaptadorPelicula.ViewHolder>{

    private ArrayList<Pelicula> datos;
    private FragmentManager fragmentManager;

    public class ViewHolder
            extends RecyclerView.ViewHolder {

        private Context context;
        private FragmentManager fragmentManager;

        @BindView(R.id.valoracion)
        protected TextView Valoracion;

        @BindView(R.id.caratula)
        protected ImageView Caratula;

        @BindView(R.id.titulo)
        protected TextView Titulo;

        @BindView(R.id.star)
        protected ImageView Star;

        @BindView(R.id.gradiente)
        protected ImageView Gradiente;

        protected Pelicula movie;

        public ViewHolder(View itemView, Context context, FragmentManager fragmentManager) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.context = context;
            this.fragmentManager = fragmentManager;
        }

        public void update(Pelicula t) {
            this.movie = t;

            Titulo.setVisibility(View.INVISIBLE);
            Valoracion.setVisibility(View.INVISIBLE);
            Star.setVisibility(View.INVISIBLE);
            Gradiente.setVisibility(View.INVISIBLE);

            Titulo.setText(movie.getTitle());
            Valoracion.setText("" + movie.getVoteAverage());
            Picasso.with(context).load("https://image.tmdb.org/t/p/w500" + movie.getPosterPath()).into(Caratula);
        }

        public void startInfoAnimation(){
            Animation translation = AnimationUtils.loadAnimation(this.context, R.anim.translation);
            Titulo.setVisibility(View.VISIBLE);
            Valoracion.setVisibility(View.VISIBLE);
            Star.setVisibility(View.VISIBLE);
            Gradiente.setVisibility(View.VISIBLE);
            Gradiente.startAnimation(translation);
            Titulo.startAnimation(translation);
            Valoracion.startAnimation(translation);
            Star.startAnimation(translation);
        }

        public void hideInfo(){
            Animation inverse_translation = AnimationUtils.loadAnimation(this.context, R.anim.inverse_translation);
            Titulo.startAnimation(inverse_translation);
            Valoracion.startAnimation(inverse_translation);
            Star.startAnimation(inverse_translation);
            Gradiente.startAnimation(inverse_translation);
            Titulo.setVisibility(View.INVISIBLE);
            Valoracion.setVisibility(View.INVISIBLE);
            Star.setVisibility(View.INVISIBLE);
            Gradiente.setVisibility(View.INVISIBLE);
        }

        @OnClick(R.id.list_item)
        public void OnClick(){
            Fragment fragment = new DetallesAdapter();
            Bundle bundle = new Bundle();
            bundle.putInt("movie_id", this.movie.getId());
            fragment.setArguments(bundle);
            this.fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack("Fragment_Detalles").commit();
        }

        @OnLongClick(R.id.list_item)
        public boolean OnLongClick(){
            startInfoAnimation();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    hideInfo();
                }
            }, 2000);

            return true;
        }
    }

    public AdaptadorPelicula(ArrayList<Pelicula> datos, FragmentManager fragmentManager) {this.datos = datos; this.fragmentManager = fragmentManager;}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.listitem_peliculas, viewGroup, false);

        ViewHolder tvh = new ViewHolder(itemView, viewGroup.getContext(), this.fragmentManager);

        return tvh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int pos) {
        Pelicula item = datos.get(pos);

        viewHolder.update(item);
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

}
