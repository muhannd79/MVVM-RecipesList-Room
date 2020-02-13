package org.fooshtech.recipeslist.requests.responses;

import android.util.Log;

import java.io.IOException;

import retrofit2.Response;

public class ApiResponse<T> {

    private static final String TAG = "ApiResponse";

    //if we pass the same name for the method it just will override

    public ApiResponse<T> create(Throwable error){
        return new ApiErrorResponse<>(error.getMessage().equals("") ? error.getMessage() : "Unknown error\nCheck network connection");
    }


    public ApiResponse<T> create(Response<T> response) {

        if(response.isSuccessful()){
            T body =  response.body();

            if(body == null || response.code() ==204){ //204 is empty response code
                return new ApiEmptyResponse<>();
            } else {
                return new ApiSuccessResponse<>(body);
            }
        } else {
            String errorMessage = "";
            try {
                errorMessage = response.errorBody().string();
            } catch (IOException e){
                Log.d(TAG,"");
                errorMessage = e.getMessage();

            }
            return new ApiErrorResponse<>(errorMessage);
        }
    }


    public class  ApiSuccessResponse<T> extends  ApiResponse<T> {

        private T body; //this will be body of the response

        ApiSuccessResponse(T body){
            this.body = body;
        }

        public T getBody() {
            return body;
        }
    }



    public class ApiErrorResponse<T> extends ApiResponse<T> {

        private String errorMessage;

        ApiErrorResponse(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

    }



    public class ApiEmptyResponse<T> extends ApiResponse<T> {
        // we need to write any code because it will not do anything
    }

}
