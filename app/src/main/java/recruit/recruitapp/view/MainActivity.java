package recruit.recruitapp.view;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.todoFragmentContainer, todoFragment).commit();
    }

    public static MainActivity getInstance(){
        return mainActivity;
    }
}
