package org.fooshtech.recipeslist.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import org.fooshtech.recipeslist.models.Recipe;


import java.util.List;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

public class RecipeListViewModel extends AndroidViewModel {

    private static final String TAG = "RecipeListViewModel";

    // in the Home Activity there is 2 View States
    public enum ViewState  {CATEGORIES,RECIPES};

    private MutableLiveData<ViewState>  viewState;

    public RecipeListViewModel(@NonNull Application application) {
        super(application);

        init();

    }

    private void init(){
        // this condition to check if viewstate is instantiated
        if(viewState==null){
                viewState = new MutableLiveData<>();
                viewState.setValue(ViewState.CATEGORIES);
        }
    }

    public  LiveData<ViewState> getViewState(){
        return viewState;
    }

}













