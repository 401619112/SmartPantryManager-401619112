package za.co.smartpantry;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    private static final String PREFS = "smart_pantry_settings";
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); setContentView(R.layout.activity_settings);
        CheckBox alerts=findViewById(R.id.checkExpiryAlerts); SharedPreferences prefs=getSharedPreferences(PREFS, MODE_PRIVATE);
        alerts.setChecked(prefs.getBoolean("expiry_alerts", true));
        findViewById(R.id.btnSaveSettings).setOnClickListener(v -> {
            prefs.edit().putBoolean("expiry_alerts", alerts.isChecked()).apply();
            Toast.makeText(this, "Settings saved.", Toast.LENGTH_SHORT).show();
        });
        findViewById(R.id.navHome).setOnClickListener(v -> finish());
        findViewById(R.id.navPantry).setOnClickListener(v -> startActivity(new android.content.Intent(this, PantryActivity.class)));
        findViewById(R.id.navRecipes).setOnClickListener(v -> startActivity(new android.content.Intent(this, SuggestedRecipesActivity.class)));
    }
}
