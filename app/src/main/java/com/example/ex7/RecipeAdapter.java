package com.example.ex7;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {



    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView recipe_image;
        public TextView recipe_name;
        public TextView recipe_cookingTime;
        public ImageView star;
        OnRecipeListener onRecipeLongClick;



        public ViewHolder(@NonNull View itemView, OnRecipeListener onRecipeLongClick) {
            super(itemView);
            recipe_image = itemView.findViewById(R.id.recipe_image);
            recipe_name = itemView.findViewById(R.id.recipe_name);
            recipe_cookingTime = itemView.findViewById(R.id.recipe_cookingTime);
            star = itemView.findViewById(R.id.star);
            this.onRecipeLongClick = onRecipeLongClick;
        }

        public void fillData(int position){
            Recipe recipe = recipeList.get(position);

            recipe_name.setText(recipe.getName());
            recipe_cookingTime.setText(recipe.getCooking_time());
            int id = context.getResources().getIdentifier(recipeList.get(position).getName(), "drawable", context.getPackageName());
            recipe_image.setImageResource(id);
            id = context.getResources().getIdentifier("star", "drawable", context.getPackageName());
            star.setImageResource(id);
            star.setVisibility(View.INVISIBLE);

            itemView.setOnTouchListener(new OnSwipeTouchListener(context) {

                public void onSwipeRight() {
                    favoritePositions.add(position);
                    onRecipeListener.onSwipe(position,true, favoritePositions);
                    notifyDataSetChanged();
                }
                public void onSwipeLeft() {
                    favoritePositions.remove(position);
                    onRecipeListener.onSwipe(position,false, favoritePositions);
                    notifyDataSetChanged();
                }

                public void onClick()
                {
                    onRecipeLongClick.onClick(position);
                    notifyDataSetChanged();
                }

                public void onLongClick()
                {
                    updatePositions(position);
                    onRecipeLongClick.onRecipeLongClick(position, selectedPosition, favoritePositions);
                    notifyDataSetChanged();
                }

            });



            if (favoritePositions.contains(position))
            {
                star.setVisibility(View.VISIBLE);
            }
            else
            {
                star.setVisibility(View.INVISIBLE);
            }

        }


    }

    private void updatePositions(int position)
    {
        List<Integer> positionToChange = new ArrayList<>();
        if (favoritePositions.contains(position))
        {
            favoritePositions.remove(position);
        }
        for (int pos : favoritePositions)
        {
            if (position < pos)
            {
                positionToChange.add(pos);
            }
        }
        for (int pos : positionToChange)
        {
            favoritePositions.remove(pos);
            favoritePositions.add(pos-1);
        }

    }

    public interface OnRecipeListener {
        void onRecipeLongClick(int position, int currentPosition, Set<Integer> favorites);
        void onClick(int position);
        boolean onSwipe(int position,boolean toAdd, Set<Integer> favorites);
        int getPosition();
    }



    private List<Recipe> recipeList;
    private OnRecipeListener onRecipeListener;
    int selectedPosition=-1;
    Set<Integer> favoritePositions = new HashSet<>();
    private Context context;

    public RecipeAdapter(Context context)
    {
        this.context = context;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.tuple, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView , onRecipeListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.fillData(position);
        selectedPosition = onRecipeListener.getPosition();
        holder.itemView.setBackgroundColor(selectedPosition == position ? Color.WHITE : Color.TRANSPARENT);
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public void setRecipeList(List<Recipe> recipeList){
        this.recipeList = recipeList;
        notifyDataSetChanged();
    }

    public void setFavoritePosition(Set<Integer> favoritePosition)
    {
        this.favoritePositions = favoritePosition;
        notifyDataSetChanged();
    }


    public void setInterface(OnRecipeListener onRecipeListener){
        this.onRecipeListener = onRecipeListener;
    }

}
