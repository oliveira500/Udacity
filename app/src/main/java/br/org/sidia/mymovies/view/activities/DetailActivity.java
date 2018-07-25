package br.org.sidia.mymovies.view.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import br.org.sidia.mymovies.R;
import br.org.sidia.mymovies.view.fragments.DetailFragment;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
    }

}
