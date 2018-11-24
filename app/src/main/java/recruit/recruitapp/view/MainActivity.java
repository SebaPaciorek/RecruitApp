package recruit.recruitapp.view;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import recruit.recruitapp.R;
import recruit.recruitapp.view.todos.TodoFragment;

public class MainActivity extends AppCompatActivity {

    private TodoFragment todoFragment = new TodoFragment();

    private static MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.todoFragmentContainer, todoFragment).commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.app_toolbar_navigation, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.filter_all:
                item.setChecked(true);
                TodoFragment.getInstance().showAllTodos();
                return true;

            case R.id.filter_finished:
                item.setChecked(true);
                TodoFragment.getInstance().showFinishedTodos();
                return true;

            case R.id.filter_unfinished:
                item.setChecked(true);
                TodoFragment.getInstance().showUnfinishedTodos();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static MainActivity getInstance() {
        return mainActivity;
    }
}
