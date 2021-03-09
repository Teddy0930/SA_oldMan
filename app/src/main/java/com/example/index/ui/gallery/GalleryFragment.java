package com.example.index.ui.gallery;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.index.R;
import com.example.index.UserProductActivity;

public class GalleryFragment extends Fragment {

    private GalleryViewModel GalleryViewModel;

    public static GalleryFragment newInstance() {
        return new  GalleryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.logout_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        GalleryViewModel = ViewModelProviders.of(this).get( GalleryViewModel.class);
        // TODO: Use the ViewModel
        Intent intent = new Intent(getActivity(), UserProductActivity.class);
        startActivity(intent);
    }

}
