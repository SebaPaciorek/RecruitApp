package recruit.recruitapp.view.todos;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import recruit.recruitapp.R;
import recruit.recruitapp.contract.todos.TodoListInterface;
import recruit.recruitapp.model.Todo;
import recruit.recruitapp.model.TodoList;
import recruit.recruitapp.presenter.todos.TodoPresenter;
import recruit.recruitapp.presenter.todos.TodoRecyclerViewAdapter;

public class TodoFragment extends Fragment implements TodoListInterface.View {

    private RecyclerView todoRecyclerView;
    private List<Todo> todoList;

    private LinearLayoutManager recyclerLayoutManager;
    private DividerItemDecoration dividerItemDecoration;

    private TodoPresenter todoPresenter;


    public TodoFragment() {
        todoPresenter = TodoPresenter.getInstance();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo, container, false);

        if (getFirstLaunchSharedPreferences().contains("true")) {
            todoPresenter.getTodos();
            setFirstLaunchSharedPreferences("false");
        }

        todoList = todoPresenter.getTodoList();

        TodoRecyclerViewAdapter todoRecyclerViewAdapter = new TodoRecyclerViewAdapter(todoList);

        todoRecyclerView = view.findViewById(R.id.todoRecyclerView);

        setLayoutManager();
        setDividerItemDecoration();

        todoRecyclerView.setAdapter(todoRecyclerViewAdapter);

        return view;
    }

    public void setLayoutManager() {
        recyclerLayoutManager = new LinearLayoutManager(getContext());
        todoRecyclerView.setLayoutManager(recyclerLayoutManager);
    }

    public void setDividerItemDecoration() {
        dividerItemDecoration = new DividerItemDecoration(todoRecyclerView.getContext(), recyclerLayoutManager.getOrientation());
        todoRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    private String getFirstLaunchSharedPreferences() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.first_launch_shared_preferences), Context.MODE_PRIVATE);
        String firstLaunch = sharedPreferences.getString(getString(R.string.first_launch_shared_preferences), "true");
        return firstLaunch;
    }

    private void setFirstLaunchSharedPreferences(String firstLaunch) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.first_launch_shared_preferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.first_launch_shared_preferences), firstLaunch);
        editor.apply();
    }
}
