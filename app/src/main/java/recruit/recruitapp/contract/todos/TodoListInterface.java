package recruit.recruitapp.contract.todos;

import java.util.List;

import recruit.recruitapp.model.Todo;

public interface TodoListInterface {
    interface View {
        void showAlertDialogRemove(int userId, int id, int position);

        void showAlertDialogEdit(int userId, int idTodo, int position, String title);

        void showProgressBar(boolean show);

        void itemInserted(int position);

        void itemRemoved(int position);

        void itemEdited(int position);

        void showToast(String message);

        void setFirstLaunchSharedPreferences(String firstLaunch);

        void showTodosFirstLaunch(List<Todo> todoList);
    }

    interface Presenter {
        void getTodos();

        List<Todo> getTodoList();

        List<Todo> getTodoListFirstLaunch();

        void removeTodo(int userId, int id, int position);

        void editTodo(int userId, int id, int position, String title);
    }
}
