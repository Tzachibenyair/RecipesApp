package com.example.ex7;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainViewModel extends AndroidViewModel {
    MutableLiveData<ArrayList<Recipe>> recipeLiveData;
    MutableLiveData<Integer> positionRecipeLiveData;
    MutableLiveData<ArrayList<Recipe>> favoriteRecipesLiveData;
    MutableLiveData<Set<Integer>> favoritePositionsLiveData;
    Set<Integer> favoritePositionSet = new HashSet<>();
    ArrayList<Recipe> recipeList;
    ArrayList<Recipe> favoriteList;
    Integer position = new Integer(-1);
    boolean removeStatus; //add false here!!!!
    private static final String FILE_NAME = "recipes.txt";
    Context context;
    ArrayList<Recipe> originRecipe;

    public MainViewModel(@NonNull Application application) {
        super(application);
        recipeLiveData = new MutableLiveData<>();
        positionRecipeLiveData = new MutableLiveData<>();
        favoriteRecipesLiveData = new MutableLiveData<>();
        favoritePositionsLiveData = new MutableLiveData<>();
        context = application.getBaseContext();
        init(application.getBaseContext());
    }

    public MutableLiveData<ArrayList<Recipe>> getRecipeMutableLiveData(){
        return recipeLiveData;
    }

    public MutableLiveData<Integer> getPositionMutableLiveData(){
        return positionRecipeLiveData;
    }

    public int getCookingTimeForCurrentRecipe()
    {
        if (position == -1)
            return -1;
        String str = recipeList.get(position).getCooking_time();
        str = str.replaceAll("\\D+","");
        return Integer.parseInt(str);
    }

    public void init(Context context){
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getApplication());
        this.removeStatus =  getPrefs.getBoolean("checkboxPref",false);
        recipeList = RecipeXMLParser.parseRecipes(context);
        originRecipe = RecipeXMLParser.parseRecipes(context);
        favoriteList = new ArrayList<>();
        if(removeStatus == false) {
            recipeLiveData.setValue(recipeList);
            positionRecipeLiveData.setValue(position);
            saveRecipes(null);
        }else{
            loadData();
        }
    }

    public void addFavoriteRecipe(int position)
    {
        favoriteList.add(recipeList.get(position));
        saveRecipes(null);
        favoriteRecipesLiveData.setValue(favoriteList);
    }

    public void removeFavoriteRecipe(int position)
    {
        String name = recipeList.get(position).getName();
        favoriteList.remove(recipeList.get(position));
        saveRecipes(name);
        favoriteRecipesLiveData.setValue(favoriteList);
    }

    public boolean  isFavoriteRecipe(int position)
    {
        if (recipeList.size()-1 < position)
            return false;
        return favoriteList.contains(recipeList.get(position));
    }

    public void setFavoritePosition(Set<Integer> favoritePosition)
    {
        favoritePositionSet = favoritePosition;
        favoritePositionsLiveData.setValue(favoritePositionSet);
    }

    public MutableLiveData<Set<Integer>> getFavoritePositionsLiveData()
    {
        return favoritePositionsLiveData;
    }


    public int getPosition(){
        return position;
    }
    public void setPosition(int position){
        this.position = position;
        positionRecipeLiveData.setValue(position);
    }

    public void remove(int position) {
        recipeList.remove(position);
        saveRecipes(null);
        recipeLiveData.setValue(recipeList);
    }


    public void setCheckBox(boolean removeStatus) {
        this.removeStatus = removeStatus;
    }

    private void loadData() {
//load the modified recipes list
        FileInputStream fis = null;
        ArrayList<Recipe> tempRecipe = new ArrayList<>();
        try {
            fis = getApplication().openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String recipe_name;
            while((recipe_name = br.readLine()) != null){
                for(Recipe recipe : recipeList){
                    if(recipe.getName().equals(recipe_name)){
                        tempRecipe.add(recipe);
                        break;
                    }
                }
            }
            recipeList = tempRecipe;
            recipeLiveData.setValue(recipeList);
            positionRecipeLiveData.setValue(position);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



//load the favorite recipes
        ArrayList<Recipe> tempFavorite = new ArrayList<>();
        Set<Integer> tempFavoritePositions = new HashSet<>();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        for(int i=0; i < recipeList.size(); i++)
        {
            String recipe_name = sharedPref.getString(recipeList.get(i).getName(),null);
            if(recipe_name != null){
                tempFavorite.add(recipeList.get(i));
                tempFavoritePositions.add(i);
            }
        }
        favoriteList = tempFavorite;
        favoritePositionSet = tempFavoritePositions;
        favoriteRecipesLiveData.setValue(favoriteList);
        favoritePositionsLiveData.setValue(favoritePositionSet);
    }

    private void saveRecipes(String name) {

        FileOutputStream fos = null;
        try {
            fos = getApplication().openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            for (Recipe recipe : recipeList) {
                String recipe_name = recipe.getName() + "\n";
                fos.write(recipe_name.getBytes());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        if(name != null)
        {
            editor.remove(name).commit();
        }
        boolean search;
        for (Recipe recipe : recipeList)
        {
            search = false;
            for (Recipe recipe1 : favoriteList)
            {
                if (recipe.getName().equals(recipe1.getName()))
                {
                    search = true;
                }
            }
            if(search == true)
                editor.putString(recipe.getName(), recipe.getName());
            else
                editor.putString(recipe.getName(), null);
        }
        editor.apply();
    }


//     for (Recipe recipe : favoriteList) {
//        editor.putString(recipe.getName(), recipe.getName());
//    }
//        editor.apply();

//        boolean search;
//        for(Recipe recipe : originRecipe) {
//            search = false;
//            for(Recipe recipe1 : recipeList){
//                if(country.getName().equals(country1.getName())){
//                    search = true;
//                }
//            }
//            if(search == true)
//                editor.putString(country.getName(), country.getName());
//            else
//                editor.putString(country.getName(), null);
//        }
//        editor.apply();






}
