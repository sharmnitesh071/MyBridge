package com.databridge.mybridge.ui.createpost;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.databridge.mybridge.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class EveryonereplyFragment extends BottomSheetDialogFragment {






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_everyonereply, container, false);
    }
}