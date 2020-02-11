package org.fooshtech.recipeslist.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import org.fooshtech.recipeslist.models.Recipe;
import org.fooshtech.recipeslist.repositories.RecipeRepository;

import java.util.List;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

public class RecipeListViewModel extends AndroidViewModel {

    private static final String TAG = "RecipeListViewModel";

    public RecipeListViewModel(@NonNull Application application) {
        super(application);

    }


}













