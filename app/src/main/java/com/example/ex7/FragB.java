package com.example.ex7;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragB extends Fragment {
    private MainViewModel model;
    TextView recipe_data;

    public FragB() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FragB newInstance(String param1, String param2) {
        FragB fragment = new FragB();
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_frag_b, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recipe_data = view.findViewById(R.id.recipe_data);
        model = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        model.getPositionMutableLiveData().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer >= 0) {
                    Recipe recipe = model.recipeList.get(integer);
                    if (recipe != null) {
                        recipe_data.setText(recipe.getCooking_steps());
                    }
                }else {
                    recipe_data.setText(R.string.s1);
                }
            }
        });
    }
}