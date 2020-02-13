package org.fooshtech.recipeslist.util;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import org.fooshtech.recipeslist.AppExecutors;
import org.fooshtech.recipeslist.requests.responses.ApiResponse;

// CacheObject: Type for the Resource data.   (database cache)
// RequestObject: Type for the API response. (network request)
public abstract class NetworkBoundResource<CacheObject, RequestObject> {

    private AppExecutors appExecutors;
    // the results object will be observed in the UI
    private MediatorLiveData<Resource<CacheObject>> results = new MediatorLiveData<>();

    public NetworkBoundResource(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
        init();
    }

    // will setup the First view from the cache
    private void init(){
        // The First Job is to looking at the DB Cache
        //update LiveData for loading status
        results.setValue((Resource<CacheObject>) Resource.loading(null));

        // observe LiveData source from local db
        final LiveData<CacheObject> dbSource = loadFromDb();

        results.addSource(dbSource, new Observer<CacheObject>() {
            @Override
            public void onChanged(@Nullable CacheObject cacheObject) {

                results.removeSource(dbSource);

                if(shouldFetch(cacheObject)){
                    // get data from the network

                }
                else{
                    results.addSource(dbSource, new Observer<CacheObject>() {
                        @Override
                        public void onChanged(@Nullable CacheObject cacheObject) {
                            setValue(Resource.success(cacheObject));
                        }
                    });
                }
            }
        });

    }

    private void setValue(Resource<CacheObject> newValue){
        if(results.getValue() != newValue){
            results.setValue(newValue);
        }
    }

    // Called to save the result of the API response into the database.
    // this is the last step in our work
    @WorkerThread
    protected abstract void saveCallResult(@NonNull RequestObject item);


    // Called with the data in the database to decide whether to fetch
    // potentially updated data from the network.
    @MainThread
    protected abstract boolean shouldFetch(@Nullable CacheObject data);



    // Called to get the cached data from the database.
    @NonNull @MainThread
    protected abstract LiveData<CacheObject> loadFromDb();



    // Called to create the API call.
    @NonNull @MainThread
    protected abstract LiveData<ApiResponse<RequestObject>> createCall();



    // Returns a LiveData object that represents the resource that's implemented
    // in the base class.
    // and we will use this method to return the Live Data (list) to UI that is getting from Cache DB
    public final LiveData<Resource<CacheObject>> getAsLiveData(){
        return results;
    }
}
