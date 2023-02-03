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
import java.util.Set;

public class MainViewModel extends AndroidViewModel {
    MutableLiveData<ArrayList<Recipe>> recipeLiveData;
    MutableLiveData<Integer> positionRecipe;
    MutableLiveData<ArrayList<Recipe>> favoriteRecipes;
    MutableLiveData<Set<Integer>> favoritePositions;
    Set<Integer> favoritePositionSet;
    ArrayList<Recipe> recipeList;
    ArrayList<Recipe> favoriteList;
    Integer position = new Integer(-1);
    boolean removeStatus = false; //add false here!!!!
    private static final String FILE_NAME = "recipes.txt";
    Context context;
    ArrayList<Recipe> originRecipe;

    public MainViewModel(@NonNull Application application) {
        super(application);
        recipeLiveData = new MutableLiveData<>();
        positionRecipe = new MutableLiveData<>();
        favoriteRecipes = new MutableLiveData<>();
        favoritePositions = new MutableLiveData<>();
        context = application.getBaseContext();
        init(application.getBaseContext());
    }

    public MutableLiveData<ArrayList<Recipe>> getRecipeMutableLiveData(){
        return recipeLiveData;
    }

    public MutableLiveData<Integer> getPositionMutableLiveData(){
        return positionRecipe;
    }

    public void init(Context context){
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getApplication());
//        this.removeStatus =  getPrefs.getBoolean("checkboxPref",false);
        recipeList = RecipeXMLParser.parseRecipes(context);
        originRecipe = RecipeXMLParser.parseRecipes(context);
        favoriteList = new ArrayList<>();
        if(removeStatus == false) {
            recipeLiveData.setValue(recipeList);
            positionRecipe.setValue(position);
            saveRecipes();
        }else{
            loadRecipes();
        }
    }

    public void addFavoriteRecipe(int position)
    {
        favoriteList.add(originRecipe.get(position));
        //TODO: SAVE DATA
        favoriteRecipes.setValue(favoriteList);
    }

    public void removeFavoriteRecipe(int position)
    {
        favoriteList.remove(originRecipe.get(position));
        //TODO: SAVE DATA
        favoriteRecipes.setValue(favoriteList);
    }

    public boolean  isFavoriteRecipe(int position)
    {
        return favoriteList.contains(originRecipe.get(position));
    }

    public void setFavoritePosition(Set<Integer> favoritePosition)
    {
        favoritePositionSet = favoritePosition;
        favoritePositions.setValue(favoritePositionSet);
    }

    public MutableLiveData<Set<Integer>> getFavoritePositionsLiveData()
    {
        return favoritePositions;
    }


    public int getPosition(){
        return position;
    }
    public void setPosition(int position){
        this.position = position;
        positionRecipe.setValue(position);
    }

    public void remove(int position) {
        recipeList.remove(position);
        saveRecipes();
        recipeLiveData.setValue(recipeList);
    }

    public void setCheckBox(boolean removeStatus) {
        this.removeStatus = removeStatus;
    }

    private void loadRecipes() {

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
            positionRecipe.setValue(position);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void saveRecipes() {

        FileOutputStream fos = null;
        try {
            fos = getApplication().openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            for(Recipe recipe : recipeList){
                String recipe_name = recipe.getName() + "\n";
                fos.write(recipe_name.getBytes());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }




//        boolean search;
//        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        for(Country country : originCountry) {
//            search = false;
//            for(Country country1 : countryList){
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



}
