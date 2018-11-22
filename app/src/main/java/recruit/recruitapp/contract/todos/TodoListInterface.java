package recruit.recruitapp.contract.todos;

import java.util.List;

import recruit.recruitapp.model.Todo;
import recruit.recruitapp.model.TodoList;

public interface TodoListInterface {
    interface View {

    }

    interface Presenter {
        void getTodos();

        List<Todo> getTodoList();
    }
}
