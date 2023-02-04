package com.example.ex7;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FragB extends Fragment {
    private MainViewModel model;
    private int cookingTime;
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
        View view =inflater.inflate(R.layout.fragment_frag_b, container, false);
        Button startTimerButton = view.findViewById(R.id.timer);
        startTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Start Timer")
                        .setMessage("Are you sure you want to start the timer?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int countdownTime = cookingTime*60;
                                Data data = new Data.Builder().putInt("countdown_time", countdownTime).build();
                                OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(TimerWorker.class).setInputData(data).build();
                                WorkManager.getInstance(getContext()).enqueue(workRequest);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create().show();
            }
        });

        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recipe_data = view.findViewById(R.id.recipe_data);
        model = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        cookingTime = model.getCookingTimeForCurrentRecipe();
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