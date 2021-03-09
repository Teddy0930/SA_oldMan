package com.example.index.ui.tools;

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
import com.example.index.UserProductActivity;

public class ToolsFragment extends Fragment {

    private ToolsViewModel ToolsViewModel;

    public static ToolsFragment newInstance() {
        return new  ToolsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.logout_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ToolsViewModel = ViewModelProviders.of(this).get( ToolsViewModel.class);
        // TODO: Use the ViewModel
        Intent intent = new Intent(getActivity(), SettingsActivity.class);
        startActivity(intent);
    }
}