package org.fooshtech.recipeslist.util;

import android.util.Log;

import org.fooshtech.recipeslist.models.Recipe;

import java.util.List;

public class Testing {

    public static void printRecipes(List<Recipe>list, String tag){
        for(Recipe recipe: list){
            Log.d(tag, "onChanged: " + recipe.getTitle());
        }
    }
}
