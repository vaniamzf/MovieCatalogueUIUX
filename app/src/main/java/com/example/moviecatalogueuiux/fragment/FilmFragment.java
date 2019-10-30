package com.example.moviecatalogueuiux.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moviecatalogueuiux.BuildConfig;
import com.example.moviecatalogueuiux.R;
import com.example.moviecatalogueuiux.adapter.MovieAdapter;
import com.example.moviecatalogueuiux.apihelper.MovieAPI;
import com.example.moviecatalogueuiux.apihelper.RetrofitAPI;
import com.example.moviecatalogueuiux.apihelper.Scraper;
import com.example.moviecatalogueuiux.model.Movies;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class FilmFragment extends Fragment {
    private static final String API_KEY = BuildConfig.APIKEY;

    private MovieAdapter mMovieAdapter;
    private final List<Movies> mMoviesList = new ArrayList<>();

    public FilmFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rvMain = view.findViewById(R.id.rv_nowplaying);

        fetchData();
        mMovieAdapter = new MovieAdapter(getContext(), mMoviesList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvMain.setLayoutManager(layoutManager);
        rvMain.setAdapter(mMovieAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_film, container, false);
    }

    private void fetchData() {
        MovieAPI movieAPI = RetrofitAPI.getClient().create(MovieAPI.class);
        movieAPI.findNowPlayingMovies(API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Scraper<Movies>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Scraper<Movies> moviesResponse) {
                        initializeData(moviesResponse.getResultMovies());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void initializeData(List<Movies> movies) {
        mMoviesList.clear();
        mMoviesList.addAll(movies);
        mMovieAdapter.notifyDataSetChanged();
    }
}
