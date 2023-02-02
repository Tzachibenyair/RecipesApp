package com.example.ex7;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

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
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onRecipeLongClick.onRecipeLongClick(position, selectedPosition);
                    notifyDataSetChanged();
                    return false;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRecipeLongClick.onClick(position);
                    notifyDataSetChanged();

                }
            });
//TODO: UNDERSTAND WHAT IS THE ISSUE WITH THIS
//                itemView.setOnTouchListener(new OnSwipeTouchListener(context) {
//                    public void onSwipeRight() {
//                        itemView.setBackgroundColor(Color.GRAY);
//                        Toast.makeText(context, "right", Toast.LENGTH_SHORT).show();
//                        onRecipeListener.onSwipe(position);
//                        notifyDataSetChanged();
//                    }
//                    public void onSwipeLeft() {
//                        Toast.makeText(context, "left", Toast.LENGTH_SHORT).show();
//                    }
//
//                });
        }


    }

    public interface OnRecipeListener {
        void onRecipeLongClick(int position, int currentPosition);
        void onClick(int position);
        void onSwipe(int position);
        int getPosition();
    }



    private List<Recipe> recipeList;
    private OnRecipeListener onRecipeListener;
    int selectedPosition=-1;
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


    public void setInterface(OnRecipeListener onRecipeListener){
        this.onRecipeListener = onRecipeListener;
    }

}
