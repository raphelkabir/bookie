package raphel.bookie.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;

import java.util.List;

import raphel.bookie.IconCreditsActivity;
import raphel.bookie.data.Repository;
import raphel.bookie.data.room.Database;
import raphel.bookie.data.room.ReadingSession;
import raphel.bookie.databinding.ActivityMainBinding;
import raphel.bookie.R;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        Database.setApplicationContext(getApplicationContext());
        new Thread(() -> {
            Repository repository = Repository.getInstance();
            runOnUiThread(() -> {
                Observer<List<ReadingSession>> observer = new Observer<List<ReadingSession>>() {
                    @Override
                    public void onChanged(List<ReadingSession> sessions) {
                        repository.dayChange();
                        repository.getSessions().removeObserver(this);
                    }
                };
                repository.getSessions().observe(this, observer);
            });
        }).start();

        setContentView(binding.getRoot());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_open_license:
                startActivity(new Intent(this, OssLicensesMenuActivity.class));
                return true;
            case R.id.menu_icons_credits:
                startActivity(new Intent(this, IconCreditsActivity.class));
                return true;
        }

        return false;
    }
}