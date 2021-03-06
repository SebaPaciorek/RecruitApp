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

        void showMessageRecyclerViewEmpty(boolean isEmpty);

        void setFirstLaunchSharedPreferences(String firstLaunch);

        void showAllTodos();

        void showFinishedTodos();

        void showUnfinishedTodos();

        void showFilteredTodos(String filter);
    }

    interface Presenter {
        void getTodos();

        List<Todo> getTodoList();

        List<Todo> getTodoListFirstLaunch();

        void removeTodo(int userId, int id, int position);

        void editTodo(int userId, int id, int position, String title);

        void updateTodoCompleted(int userId, int id, boolean completed);

        List<Todo> getAll();

        List<Todo> getFinished();

        List<Todo> getUnfinished();

        List<Todo> getFiltered(String filter);
    }
}
