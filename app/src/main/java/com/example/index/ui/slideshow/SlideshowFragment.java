package com.example.index.ui.slideshow;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.index.R;
import com.example.index.SettingsActivity;
import com.example.index.UserRecordsActivity;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;

    public static SlideshowFragment newInstance() {
        return new  SlideshowFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.logout_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        slideshowViewModel = ViewModelProviders.of(this).get( SlideshowViewModel.class);
        // TODO: Use the ViewModel
        Intent intent = new Intent(getActivity(), UserRecordsActivity.class);
        startActivity(intent);
    }
}