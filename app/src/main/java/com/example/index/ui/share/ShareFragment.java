package com.example.index.ui.share;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.index.DeliveryRedEnvelopeActivity;
import com.example.index.R;

public class ShareFragment extends Fragment {

    private ShareViewModel ShareViewModel;

    public static ShareFragment newInstance() {
        return new  ShareFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.logout_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ShareViewModel = ViewModelProviders.of(this).get( ShareViewModel.class);
        // TODO: Use the ViewModel
        Intent intent = new Intent(getActivity(), DeliveryRedEnvelopeActivity.class);
        startActivity(intent);
    }
}