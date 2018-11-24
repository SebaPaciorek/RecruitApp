package recruit.recruitapp.view;

import android.app.SearchManager;
import android.content.Context;
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
import android.widget.SearchView;

import recruit.recruitapp.R;
import recruit.recruitapp.view.todos.TodoFragment;

public class MainActivity extends AppCompatActivity {

    private TodoFragment todoFragment = new TodoFragment();

    private static MainActivity mainActivity;

    private  SearchView searchView;

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

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                TodoFragment.getInstance().showFilteredTodos(s);
                return false;
            }
        };

        searchView.setOnQueryTextListener(onQueryTextListener);

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
