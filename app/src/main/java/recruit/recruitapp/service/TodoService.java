package recruit.recruitapp.service;

import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface TodoService {

    @GET("/todos")
    @Headers("Content-Type: application/json")
    Call<ArrayList<JsonObject>> getTodos();
}
