package recruit.recruitapp.presenter.todos;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import recruit.recruitapp.R;
import recruit.recruitapp.model.Todo;

public class TodoRecyclerViewAdapter extends RecyclerView.Adapter<TodoRecyclerViewAdapter.ViewHolder> {

    private List<Todo> todoList;
    private TodoRecyclerViewAdapter.ViewHolder holder;

    public TodoRecyclerViewAdapter(List<Todo> todoList) {
        this.todoList = todoList;
    }

    @NonNull
    @Override
    public TodoRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item_recycler_view, parent, false);
        TodoRecyclerViewAdapter.ViewHolder viewHolder = new TodoRecyclerViewAdapter.ViewHolder(view);
        this.holder = viewHolder;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TodoRecyclerViewAdapter.ViewHolder viewHolder, int i) {
        final Todo todo = todoList.get(i);

        holder.userId.setText(String.valueOf(todo.getUserId()));
        holder.id.setText(String.valueOf(todo.getId()));
        holder.todoCheckBox.setChecked(todo.isCompleted());
        holder.todoCheckBox.setText(todo.getTitle());
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView userId;
        public TextView id;
        public CheckBox todoCheckBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userId = itemView.findViewById(R.id.userIdTodoList);
            id = itemView.findViewById(R.id.idTodoList);
            todoCheckBox = itemView.findViewById(R.id.todoCheckBoxTodoList);
        }
    }
}
