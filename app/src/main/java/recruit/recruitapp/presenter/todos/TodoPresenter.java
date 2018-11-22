package recruit.recruitapp.presenter.todos;

import com.google.gson.JsonObject;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import recruit.recruitapp.contract.todos.TodoListInterface;
import recruit.recruitapp.service.TodoService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TodoPresenter implements TodoListInterface.Presenter {

    private final String baseUrl = "https://jsonplaceholder.typicode.com/";

    private TodoService todoService;

    private static TodoPresenter todoPresenter;

    public TodoPresenter() {
    }

    @Override
    public void getTodos() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient().newBuilder()
                .addInterceptor(interceptor)

                .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                .build();

        Retrofit.Builder builder = new Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder
                .client(client)
                .build();

        todoService = retrofit.create(TodoService.class);

        Call<ArrayList<JsonObject>> call = todoService.getTodos();
        call.enqueue(new Callback<ArrayList<JsonObject>>() {
            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {
                if (response.isSuccessful()) {

                }
            }

            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {

            }
        });

    }

    //ensure singleton
    public static TodoPresenter getInstance() {
        if (todoPresenter == null) {
            todoPresenter = new TodoPresenter();
            return todoPresenter;
        }
        return todoPresenter;
    }

}
