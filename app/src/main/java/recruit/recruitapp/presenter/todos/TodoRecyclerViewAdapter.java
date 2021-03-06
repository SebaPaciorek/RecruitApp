package recruit.recruitapp.presenter.todos;

import android.content.DialogInterface;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import recruit.recruitapp.R;
import recruit.recruitapp.model.Todo;
import recruit.recruitapp.view.MainActivity;
import recruit.recruitapp.view.todos.TodoFragment;

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
        if (todoList != null) {
            return todoList.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView userId;
        public TextView id;
        public CheckBox todoCheckBox;
        public ImageView editImageView;
        public ImageView removeImageView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userId = itemView.findViewById(R.id.userIdTodoList);
            id = itemView.findViewById(R.id.idTodoList);
            todoCheckBox = itemView.findViewById(R.id.todoCheckBoxTodoList);
            editImageView = itemView.findViewById(R.id.editTodoImageView);
            removeImageView = itemView.findViewById(R.id.removeTodoImageView);

            setOnCheckBoxListener();
            setOnEditListener();
            setOnRemoveListener();
        }

        void setOnCheckBoxListener() {
            todoCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TodoPresenter.getInstance().updateTodoCompleted(Integer.valueOf(userId.getText().toString()), Integer.valueOf(id.getText().toString()), todoCheckBox.isChecked());
                }
            });
        }

        void setOnEditListener() {
            editImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TodoFragment.getInstance().showAlertDialogEdit(Integer.valueOf(userId.getText().toString()), Integer.valueOf(id.getText().toString()), getItemViewType(), todoCheckBox.getText().toString());
                }
            });
        }

        void setOnRemoveListener() {
            removeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TodoFragment.getInstance().showAlertDialogRemove(Integer.valueOf(userId.getText().toString()), Integer.valueOf(id.getText().toString()), getItemViewType());
                }
            });
        }

        @Override
        public void onClick(View view) {

        }
    }
}
