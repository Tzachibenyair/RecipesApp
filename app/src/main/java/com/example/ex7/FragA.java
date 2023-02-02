package com.example.ex7;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragA#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragA extends Fragment implements RecipeAdapter.OnRecipeListener {
    private MainViewModel model;
    FragAListener listener;

    public static FragA newInstance(String param1, String param2) {
        FragA fragment = new FragA();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.listener = (FragAListener)context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_frag_a, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        RecyclerView rvRecipe = (RecyclerView) view.findViewById(R.id.rvRecipe);
        RecipeAdapter adapter = new RecipeAdapter(getContext());
        adapter.setInterface(this);
        rvRecipe.setAdapter(adapter);
        rvRecipe.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        model = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        model.getRecipeMutableLiveData().observe(getActivity(), new Observer<ArrayList<Recipe>>() {
            @Override
            public void onChanged(ArrayList<Recipe> recipes) {
                adapter.setRecipeList(recipes);
            }
        });
    }
//fagA responsible to update the livedata with his modelView
    @Override
    public void onRecipeLongClick(int position, int currentPosition) {
        model.remove(position);
        if(position == currentPosition)
            model.setPosition(-1);
        if(position < currentPosition)
            model.setPosition(currentPosition - 1);
    }

    @Override
    public void onClick(int position) {
        model.setPosition(position);
        listener.OnClickEvent();
    }

    @Override
    public void onSwipe(int position) {
        model.addFavoriteRecipe(position);
    }

    @Override
    public int getPosition() {
        return model.getPosition();
    }

    public interface FragAListener{
        public void OnClickEvent();
    }



}