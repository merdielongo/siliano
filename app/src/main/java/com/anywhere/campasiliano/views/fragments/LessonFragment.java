package com.anywhere.campasiliano.views.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anywhere.campasiliano.R;
import com.anywhere.campasiliano.databinding.FragmentLessonBinding;


public class LessonFragment extends Fragment {

    private FragmentLessonBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLessonBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}