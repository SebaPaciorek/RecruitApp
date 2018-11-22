package recruit.recruitapp.model;

import io.realm.RealmList;
import io.realm.RealmObject;

public class TodoList extends RealmObject {

    private RealmList<Todo> todoRealmList;

    public TodoList() {
    }

    public TodoList(RealmList<Todo> todoRealmList) {
        this.todoRealmList = todoRealmList;
    }

    public RealmList<Todo> getTodoRealmList() {
        return todoRealmList;
    }

    public void setTodoRealmList(RealmList<Todo> todoRealmList) {
        this.todoRealmList = todoRealmList;
    }
}
