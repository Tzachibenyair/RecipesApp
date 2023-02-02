package com.example.ex7;

public class Recipe {
	String name;
	String ingredients;
	String cooking_time;
	String dish_type;
	String cooking_steps;

	public String getIngredients() {
		return ingredients;
	}

	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCooking_steps() {
		return cooking_steps;
	}

	public void setCooking_steps(String cooking_steps) {
		this.cooking_steps = cooking_steps;
	}

	public Recipe(String name, String ingredients, String cooking_steps, String cooking_time){
		this.name = name;
		this.ingredients = ingredients;
		this.cooking_steps =cooking_steps;
		this.cooking_time = cooking_time;

	}

	public String getDish_type() {
		return dish_type;
	}

	public void setDish_type(String dish_type) {
		this.dish_type = dish_type;
	}

	public Recipe() {
		// TODO Auto-generated constructor stub
	}

	public void setCooking_time(String cooking_time){
		this.cooking_time = cooking_time;
	}
	public String getCooking_time(){
		return this.cooking_time;
	}

	public int compare(Recipe other) {
		return  this.name.compareTo(other.name);
	}

	@Override
	public String toString() {
		return name;
	}

}
