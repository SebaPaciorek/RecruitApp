package recruit.recruitapp.contract.todos;

import java.util.List;

import recruit.recruitapp.model.Todo;
import recruit.recruitapp.model.TodoList;

public interface TodoListInterface {
    interface View {
        void showAlertDialogRemove(int userId, int id);

        void showProgressBar(boolean show);

        void itemRemoved(int position);

        void showToast(String message);

        void setFirstLaunchSharedPreferences(String firstLaunch);

        void showTodosFirstLaunch(List<Todo> todoList);
    }

    interface Presenter {
        void getTodos();

        List<Todo> getTodoList();

        List<Todo> getTodoListFirstLaunch();

        void removeTodo(int userId, int id);
    }
}
