package recruit.recruitapp.presenter.todos;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import recruit.recruitapp.R;
import recruit.recruitapp.contract.todos.TodoListInterface;
import recruit.recruitapp.model.Todo;
import recruit.recruitapp.model.TodoList;
import recruit.recruitapp.service.TodoService;
import recruit.recruitapp.view.todos.TodoFragment;
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
    public static TodoList todoList;
    private List<Todo> todoListArrayList;

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
                    TodoFragment.getInstance().setFirstLaunchSharedPreferences("false");
                    TodoFragment.getInstance().showAllTodos();
                } else {
                    TodoFragment.getInstance().showToast(TodoFragment.getInstance().getResources().getString(R.string.title_get_todos_api_error));
                    TodoFragment.getInstance().showMessageRecyclerViewEmpty(true);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {
                TodoFragment.getInstance().showToast(TodoFragment.getInstance().getResources().getString(R.string.title_get_todos_api_error));
                TodoFragment.getInstance().showMessageRecyclerViewEmpty(true);
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
                todoList = listOfTodoList.get(0);

                if (todoList != null) {
                    todoArrayList.addAll(todoList.getTodoRealmList());
                }
            }
        });

        return todoArrayList;
    }

    @Override
    public List<Todo> getTodoListFirstLaunch() {

        return todoListArrayList;
    }

    @Override
    public void editTodo(int userId, int id, int position, String title) {
        getTodoList();
        realm = Realm.getDefaultInstance();

        todoList.getTodoRealmList().getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Todo todoToEdit = realm.where(Todo.class).equalTo("userId", userId).equalTo("id", id).findFirst();
                if (todoToEdit != null) {
                    todoToEdit.setTitle(title);
                } else {
                    TodoFragment.getInstance().showToast(TodoFragment.getInstance().getResources().getString(R.string.message_edit_failed));
                }
            }
        });
        TodoFragment.getInstance().itemEdited(position);
    }

    /*
    by requirements userId is always 1
    new object is always added on end of list
     */
    public void insertTodo(String title) {
        realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                int userId = 1;
                int maxId = 0;
                if (realm.where(Todo.class).equalTo("userId", userId).max("id") != null) {
                    maxId = Objects.requireNonNull(realm.where(Todo.class).max("id")).intValue();
                }
                Todo todo = new Todo();
                todo.setUserId(userId);
                todo.setId(maxId + 1);
                todo.setTitle(title);
                todo.setCompleted(false);

                addToTodoList(todo);
            }
        });
        TodoFragment.getInstance().itemInserted(getTodoList().size());
    }

    @Override
    public List<Todo> getAll() {
        List<Todo> todoArrayList = new ArrayList<>();
        realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                RealmResults<TodoList> listOfTodoList = realm.where(TodoList.class).findAll();
                if (listOfTodoList != null) {
                    todoList = listOfTodoList.get(0);
                }

                if (todoList != null) {
                    todoArrayList.addAll(todoList.getTodoRealmList());
                }
            }
        });

        return todoArrayList;
    }

    @Override
    public List<Todo> getFinished() {
        List<Todo> todoArrayList = new ArrayList<>();
        List<Todo> todoArrayListCompleted = new ArrayList<>();
        realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<TodoList> listOfTodoList = realm.where(TodoList.class).findAll();
                todoList = listOfTodoList.get(0);

                if (todoList != null) {
                    todoArrayList.addAll(todoList.getTodoRealmList());
                }
            }
        });

        for (int i = 0; i < todoArrayList.size(); i++) {
            if (todoArrayList.get(i).isCompleted()) {
                todoArrayListCompleted.add(todoArrayList.get(i));
            }
        }

        return todoArrayListCompleted;
    }

    @Override
    public List<Todo> getUnfinished() {
        List<Todo> todoArrayList = new ArrayList<>();
        List<Todo> todoArrayListUncompleted = new ArrayList<>();
        realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<TodoList> listOfTodoList = realm.where(TodoList.class).findAll();
                todoList = listOfTodoList.get(0);

                if (todoList != null) {
                    for (int i = 0; i < todoList.getTodoRealmList().size(); i++) {
                        todoList.getTodoRealmList().get(i).setCompleted(false);
                    }
                    todoArrayList.addAll(todoList.getTodoRealmList());
                }
            }
        });

        for (int i = 0; i < todoArrayList.size(); i++) {
            if (!todoArrayList.get(i).isCompleted()) {
                todoArrayListUncompleted.add(todoArrayList.get(i));
            } else {
                todoArrayListUncompleted.add(todoArrayList.get(i));
            }
        }

        return todoArrayListUncompleted;
    }

    @Override
    public List<Todo> getFiltered(String filter) {
        List<Todo> todoArrayList = new ArrayList<>();
        List<Todo> todoArrayListFiltered = new ArrayList<>();
        realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                RealmResults<TodoList> listOfTodoList = realm.where(TodoList.class).findAll();
                if (listOfTodoList != null) {
                    todoList = listOfTodoList.get(0);
                }

                if (todoList != null) {
                    todoArrayList.addAll(todoList.getTodoRealmList());
                }
            }
        });

        for (int i = 0; i < todoArrayList.size(); i++) {
            if (todoArrayList.get(i).getTitle().contains(filter)) {
                todoArrayListFiltered.add(todoArrayList.get(i));
            } else if (isInteger(filter)) {
                if (todoArrayList.get(i).getUserId() == Integer.valueOf(filter)) {
                    todoArrayListFiltered.add(todoArrayList.get(i));
                }
            }
        }

        return todoArrayListFiltered;
    }

    private boolean isInteger(String s) {
        return isInteger(s, 10);
    }

    private boolean isInteger(String s, int radix) {
        if (s.isEmpty()) return false;
        for (int i = 0; i < s.length(); i++) {
            if (i == 0 && s.charAt(i) == '-') {
                if (s.length() == 1) return false;
                else continue;
            }
            if (Character.digit(s.charAt(i), radix) < 0) return false;
        }
        return true;
    }

    @Override
    public void removeTodo(int userId, int id, int position) {
        getTodoList();
        realm = Realm.getDefaultInstance();

        todoList.getTodoRealmList().getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Todo todoToRemove = realm.where(Todo.class).equalTo("userId", userId).equalTo("id", id).findFirst();
                if (todoToRemove != null) {

                    todoToRemove.deleteFromRealm();

                    TodoFragment.getInstance().showProgressBar(false);
                    TodoFragment.getInstance().showToast(TodoFragment.getInstance().getResources().getString(R.string.message_remove_success));

                } else {
                    TodoFragment.getInstance().showProgressBar(false);
                    TodoFragment.getInstance().showToast(TodoFragment.getInstance().getResources().getString(R.string.message_remove_failed));
                }
            }

        });
        TodoFragment.getInstance().itemRemoved(position);
    }

    @Override
    public void updateTodoCompleted(int userId, int id, boolean completed) {
        getTodoList();
        realm = Realm.getDefaultInstance();

        todoList.getTodoRealmList().getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Todo todoToUpdate = realm.where(Todo.class).equalTo("userId", userId).equalTo("id", id).findFirst();
                if (todoToUpdate != null) {
                    todoToUpdate.setCompleted(completed);
                }
            }
        });
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
                todoListArrayList = new ArrayList<>();
            }
        });
    }

    private void addToTodoList(Todo todo) {
        todoList.getTodoRealmList().add(todo);
        if (todoListArrayList != null) todoListArrayList.add(todo);
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
