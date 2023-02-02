package com.example.ex7;


import android.content.Context;
import android.content.res.AssetManager;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class RecipeXMLParser {
	final static String KEY_RECIPE ="recipe";
	final static String KEY_NAME="name";
	final static String KEY_INGREDIENTS ="ingredients";
	final static String KEY_COOKING_TIME ="cooking_time";
	final static String KEY_DISH_TYPE ="dish_type";
	final static String KEY_COOKING_STEPS ="cooking_steps";


	public static ArrayList<Recipe> parseRecipes(Context context){
		ArrayList<Recipe> data = null;
		InputStream in = openRecipesFile(context);
		XmlPullParserFactory xmlFactoryObject;
		try {
				xmlFactoryObject = XmlPullParserFactory.newInstance();
				XmlPullParser parser = xmlFactoryObject.newPullParser();
		
				parser.setInput(in, null);
		        int eventType = parser.getEventType();
		        Recipe currentRecipe = null;
		        String inTag = "";
		        String strTagText = null;
		
		        while (eventType != XmlPullParser.END_DOCUMENT){
		        	inTag = parser.getName();
		            switch (eventType){
		                case XmlPullParser.START_DOCUMENT:
		                	data = new ArrayList<Recipe>();
		                    break;
		                case XmlPullParser.START_TAG:
		                	if (inTag.equalsIgnoreCase(KEY_RECIPE))
		                    	currentRecipe = new Recipe();
		                    break;
		                case XmlPullParser.TEXT:
		                	strTagText = parser.getText();
		                	break;
		                case XmlPullParser.END_TAG:
		                	if (inTag.equalsIgnoreCase(KEY_RECIPE))
		                    	data.add(currentRecipe);
		                	else if (inTag.equalsIgnoreCase(KEY_NAME))
		                    	currentRecipe.name = strTagText;
		                	else if (inTag.equalsIgnoreCase(KEY_DISH_TYPE))
		                    	currentRecipe.dish_type =strTagText;
		                	else if (inTag.equalsIgnoreCase(KEY_INGREDIENTS))
		                    	currentRecipe.ingredients =strTagText;
		                	else if (inTag.equalsIgnoreCase(KEY_COOKING_TIME))
		                    	currentRecipe.cooking_time =strTagText;
		                	else if (inTag.equalsIgnoreCase(KEY_COOKING_STEPS))
		                    	currentRecipe.setCooking_steps(strTagText);
		            		inTag ="";
		                	break;	                    
		            }//switch
		            eventType = parser.next();
		        }//while
			} catch (Exception e) {e.printStackTrace();}
		return data;
	}

	private static InputStream openRecipesFile(Context context){
		AssetManager assetManager = context.getAssets();
		InputStream in =null;
		try {
			in = assetManager.open("recipes.xml");
		} catch (IOException e) {e.printStackTrace();}
		return in;
	}
}
