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
import com.example.moviecatalogueuiux.adapter.TVAdapter;
import com.example.moviecatalogueuiux.apihelper.MovieAPI;
import com.example.moviecatalogueuiux.apihelper.RetrofitAPI;
import com.example.moviecatalogueuiux.apihelper.Scraper;
import com.example.moviecatalogueuiux.model.TV;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class TVFragment extends Fragment {
    private static final String API_KEY = BuildConfig.APIKEY;

    private TVAdapter tvAdapter;
    private final List<TV> tvList = new ArrayList<>();

    public TVFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rvMain = view.findViewById(R.id.rv_tvonair);

        fetchData();
        tvAdapter = new TVAdapter(getContext(), tvList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvMain.setLayoutManager(layoutManager);
        rvMain.setAdapter(tvAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv, container, false);
    }

    private void fetchData() {
        MovieAPI movieAPI = RetrofitAPI.getClient().create(MovieAPI.class);
        movieAPI.findOnTheAirTv(API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Scraper<TV>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Scraper<TV> tvResponse) {
                        initializeData(tvResponse.getResultTv());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void initializeData(List<TV> tvs) {
        tvList.clear();
        tvList.addAll(tvs);
        tvAdapter.notifyDataSetChanged();
    }
}
