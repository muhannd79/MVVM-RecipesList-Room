package org.fooshtech.recipeslist.repositories;


import android.content.Context;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import org.fooshtech.recipeslist.AppExecutors;
import org.fooshtech.recipeslist.models.Recipe;
import org.fooshtech.recipeslist.persistence.RecipeDao;
import org.fooshtech.recipeslist.persistence.RecipeDatabase;
import org.fooshtech.recipeslist.requests.ServiceGenerator;
import org.fooshtech.recipeslist.requests.responses.ApiResponse;
import org.fooshtech.recipeslist.requests.responses.RecipeSearchResponse;
import org.fooshtech.recipeslist.util.Constants;
import org.fooshtech.recipeslist.util.NetworkBoundResource;
import org.fooshtech.recipeslist.util.Resource;

import java.util.List;

public class RecipeRepository {

    private static final String TAG = "RecipeRepository";
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
            public void saveCallResult(@NonNull RecipeSearchResponse item) {

                if(item.getRecipes() != null){ // recipe list will be null if api key is expired

                    Recipe[] recipes = new Recipe[item.getRecipes().size()];

                    int index = 0;

                    for(long rowId: recipeDao.insertRecipes((Recipe[]) (item.getRecipes().toArray(recipes)))){
                        if(rowId == -1){ // conflict detected
                            Log.d(TAG, "saveCallResult: CONFLICT... This recipe is already in cache.");
                            // if already exists, I don't want to set the ingredients or timestamp b/c they will be erased
                            recipeDao.updateRecipe(
                                    recipes[index].getRecipe_id(),
                                    recipes[index].getTitle(),
                                    recipes[index].getPublisher(),
                                    recipes[index].getImage_url(),
                                    recipes[index].getSocial_rank()
                            );
                        }
                        index++;
                    }
                }
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
                return ServiceGenerator.getRecipeApi()
                        .searchRecipe(
                                Constants.API_KEY,
                                query,
                                String.valueOf(pageNumber)
                        );
            }
        }.getAsLiveData();
    }

}


















