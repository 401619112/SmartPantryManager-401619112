package za.co.smartpantry;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper db;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); setContentView(R.layout.activity_main); db = new DatabaseHelper(this);
        findViewById(R.id.btnPantry).setOnClickListener(v -> startActivity(new Intent(this, PantryActivity.class)));
        findViewById(R.id.btnRecipes).setOnClickListener(v -> startActivity(new Intent(this, SuggestedRecipesActivity.class)));
        findViewById(R.id.btnSettings).setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
    }
    @Override protected void onResume() {
        super.onResume();
        TextView summary = findViewById(R.id.txtSummary);
        if (summary != null && db != null) summary.setText("You have " + db.getPantryItems().size() + " pantry ingredients saved. Let's make them count.");
    }
}
