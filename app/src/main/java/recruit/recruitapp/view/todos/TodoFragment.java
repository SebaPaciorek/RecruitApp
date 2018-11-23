package recruit.recruitapp.view.todos;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import recruit.recruitapp.R;
import recruit.recruitapp.contract.todos.TodoListInterface;
import recruit.recruitapp.model.Todo;
import recruit.recruitapp.presenter.todos.TodoPresenter;
import recruit.recruitapp.presenter.todos.TodoRecyclerViewAdapter;
import recruit.recruitapp.view.MainActivity;

public class TodoFragment extends Fragment implements TodoListInterface.View, View.OnClickListener {

    private RecyclerView todoRecyclerView;
    private List<Todo> todoList;

    private LinearLayoutManager recyclerLayoutManager;
    private DividerItemDecoration dividerItemDecoration;

    private TodoPresenter todoPresenter;

    private TodoRecyclerViewAdapter todoRecyclerViewAdapter;
    private ProgressBar progressBar;

    private Button addNewTodoButton;

    private static TodoFragment todoFragment;

    public TodoFragment() {
        todoPresenter = TodoPresenter.getInstance();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        todoFragment = this;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo, container, false);

        progressBar = view.findViewById(R.id.progressBar);

        todoRecyclerView = view.findViewById(R.id.todoRecyclerView);

        addNewTodoButton = MainActivity.getInstance().findViewById(R.id.newTodoButton);

        setOnAddNewTodoButton();

        setLayoutManager();
        setDividerItemDecoration();

        if (getFirstLaunchSharedPreferences().contains("true")) {
            todoPresenter.getTodos();
            todoList = todoPresenter.getTodoListFirstLaunch();
        } else {
            todoList = todoPresenter.getTodoList();
            todoRecyclerViewAdapter = new TodoRecyclerViewAdapter(todoList);
            todoRecyclerView.setAdapter(todoRecyclerViewAdapter);
        }

        return view;
    }

    @Override
    public void showTodosFirstLaunch(List<Todo> todoList) {
        todoRecyclerViewAdapter = new TodoRecyclerViewAdapter(todoList);
        todoRecyclerView.setAdapter(todoRecyclerViewAdapter);
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

    @Override
    public void setFirstLaunchSharedPreferences(String firstLaunch) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.first_launch_shared_preferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.first_launch_shared_preferences), firstLaunch);
        editor.apply();
    }

    private void setOnAddNewTodoButton() {
        addNewTodoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialogInsert();
            }
        });
    }

    public void showAlertDialogInsert() {
        EditText editText = new EditText(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.getInstance());
        builder.setMessage(R.string.alert_dialog_insert);
        builder.setCancelable(true);
        builder.setView(editText);

        builder.setPositiveButton(
                R.string.alert_dialog_insert_positive_button,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        todoPresenter.insertTodo(editText.getText().toString());
                    }
                });

        builder.setNegativeButton(
                R.string.alert_dialog_insert_negative_button,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void showAlertDialogEdit(int userId, int idTodo, int position, String title) {
        EditText editText = new EditText(getActivity());
        editText.setText(title);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.getInstance());
        builder.setMessage(R.string.alert_dialog_edit);
        builder.setCancelable(true);
        builder.setView(editText);

        builder.setPositiveButton(
                R.string.alert_dialog_edit_positive_button,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        todoPresenter.editTodo(userId, idTodo, position, editText.getText().toString());
                    }
                });

        builder.setNegativeButton(
                R.string.alert_dialog_edit_negative_button,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void showAlertDialogRemove(int userId, int idTodo, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.getInstance());
        builder.setMessage(R.string.alert_dialog_remove);
        builder.setCancelable(true);

        builder.setPositiveButton(
                R.string.alert_dialog_remove_positive_button,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        todoPresenter.removeTodo(userId, idTodo, position);
                        showProgressBar(true);
                    }
                });

        builder.setNegativeButton(
                R.string.alert_dialog_remove_negative_button,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void itemInserted(int position) {
        todoList = todoPresenter.getTodoList();
        todoRecyclerViewAdapter = new TodoRecyclerViewAdapter(todoList);
        todoRecyclerView.setAdapter(todoRecyclerViewAdapter);
        todoRecyclerView.scrollToPosition(position - 1);
        todoRecyclerViewAdapter.notifyItemInserted(position);
    }

    @Override
    public void itemEdited(int position) {
        todoList = todoPresenter.getTodoList();
        todoRecyclerViewAdapter = new TodoRecyclerViewAdapter(todoList);
        todoRecyclerView.setAdapter(todoRecyclerViewAdapter);
        todoRecyclerViewAdapter.notifyItemChanged(position);
    }

    @Override
    public void itemRemoved(int position) {
        todoList = todoPresenter.getTodoList();
        todoRecyclerViewAdapter = new TodoRecyclerViewAdapter(todoList);
        todoRecyclerView.setAdapter(todoRecyclerViewAdapter);
        todoRecyclerViewAdapter.notifyItemRemoved(position);
    }

    @Override
    public void showProgressBar(boolean show) {
        if (progressBar != null) {
            if (show) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    public static TodoFragment getInstance() {
        return todoFragment;
    }

    @Override
    public void onClick(View view) {

    }
}
