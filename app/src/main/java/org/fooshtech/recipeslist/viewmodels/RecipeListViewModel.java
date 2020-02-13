package org.fooshtech.recipeslist.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.annotation.NonNull;

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













