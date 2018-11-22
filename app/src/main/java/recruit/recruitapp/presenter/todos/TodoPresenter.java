package recruit.recruitapp.presenter.todos;

import android.util.Log;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import recruit.recruitapp.contract.todos.TodoListInterface;
import recruit.recruitapp.model.Todo;
import recruit.recruitapp.model.TodoList;
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

    private Realm realm;
    private TodoList todoList;

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
                    if (response.body() != null) {
                        realm = Realm.getDefaultInstance();
                        createTodoListObject();

                        for (int i = 0; i < response.body().size(); i++) {
                            JsonObject todoJsonObject = response.body().get(i);

                            createTodoObject(todoJsonObject);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {

            }
        });

    }

    @Override
    public List<Todo> getTodoList() {
        List<Todo> todoArrayList = new ArrayList<>();
        realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<TodoList> listOfTodoList = realm.where(TodoList.class).findAll();
                TodoList todoList = listOfTodoList.get(0);

                for (int i = 0; i < todoList.getTodoRealmList().size(); i++) {
                    todoArrayList.add(todoList.getTodoRealmList().get(i));
                }
            }
        });

        return todoArrayList;
    }

    private void createTodoObject(JsonObject todoJsonObject) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Todo todo = realm.createObject(Todo.class);
                todo.setUserId(todoJsonObject.get("userId").getAsInt());
                todo.setId(todoJsonObject.get("id").getAsInt());
                todo.setTitle(todoJsonObject.get("title").getAsString());
                todo.setCompleted(todoJsonObject.get("completed").getAsBoolean());

                addToTodoList(todo);
            }
        });
    }

    private void createTodoListObject() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                todoList = realm.createObject(TodoList.class);
            }
        });
    }

    private void addToTodoList(Todo todo) {
        todoList.getTodoRealmList().add(todo);
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
