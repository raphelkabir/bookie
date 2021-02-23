package raphel.bookie.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import raphel.bookie.data.Repository;
import raphel.bookie.data.room.Database;
import raphel.bookie.databinding.ActivityMainBinding;
import raphel.bookie.ui.controls.HostPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        Database.setApplicationContext(getApplicationContext());
        new Thread(() -> {
            Repository repository = Repository.getInstance();
        }).start();

        setContentView(binding.getRoot());
    }
}