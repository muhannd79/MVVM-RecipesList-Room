package org.fooshtech.recipeslist.repositories;


import android.content.Context;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import org.fooshtech.recipeslist.AppExecutors;
import org.fooshtech.recipeslist.models.Recipe;
import org.fooshtech.recipeslist.persistence.RecipeDao;
import org.fooshtech.recipeslist.persistence.RecipeDatabase;
import org.fooshtech.recipeslist.requests.responses.ApiResponse;
import org.fooshtech.recipeslist.requests.responses.RecipeSearchResponse;
import org.fooshtech.recipeslist.util.NetworkBoundResource;
import org.fooshtech.recipeslist.util.Resource;

import java.util.List;

public class RecipeRepository {

    private static RecipeRepository instance;
    private RecipeDao recipeDao;

    public static RecipeRepository getInstance(Context context){
        if(instance == null){
            instance = new RecipeRepository(context);
        }
        return instance;
    }

    private RecipeRepository(Context context) {
        recipeDao = RecipeDatabase.getInstance(context).getRecipeDao();
    }

    public LiveData<Resource<List<Recipe>>> searchRecipesApi(final String  query,  final  int pageNumber){
        return new NetworkBoundResource<List<Recipe>,RecipeSearchResponse>(AppExecutors.getInstance()){
            @Override
            protected void saveCallResult(@NonNull RecipeSearchResponse item) {

            }

            @Override
            protected boolean shouldFetch(@Nullable List<Recipe> data) {
                return false;
            }

            @NonNull
            @Override
            protected LiveData<List<Recipe>> loadFromDb() {
                return recipeDao.searchRecipes(query,pageNumber);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<RecipeSearchResponse>> createCall() {
                return null;
            }
        }.getAsLiveData();
    }

}


















