package com.example.index.ui.send;

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
import com.example.index.ReceiveRedEnvelopeActivity;

public class SendFragment extends Fragment {

    private SendViewModel sendViewModel;

    public static SendFragment newInstance() {
        return new  SendFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.logout_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sendViewModel = ViewModelProviders.of(this).get( SendViewModel.class);
        // TODO: Use the ViewModel
        Intent intent = new Intent(getActivity(), ReceiveRedEnvelopeActivity.class);
        startActivity(intent);
    }

}