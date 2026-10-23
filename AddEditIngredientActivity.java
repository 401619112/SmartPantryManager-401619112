package za.co.smartpantry;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddEditIngredientActivity extends AppCompatActivity {
    private DatabaseHelper db; private EditText name, quantity, unit, expiry; private long itemId = -1;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); setContentView(R.layout.activity_add_edit_ingredient); db = new DatabaseHelper(this);
        name=findViewById(R.id.editName); quantity=findViewById(R.id.editQuantity); unit=findViewById(R.id.editUnit); expiry=findViewById(R.id.editExpiry);
        itemId=getIntent().getLongExtra("id", -1);
        if (itemId != -1) {
            ((TextView)findViewById(R.id.txtFormTitle)).setText("Edit ingredient");
            name.setText(getIntent().getStringExtra("name")); quantity.setText(String.valueOf(getIntent().getDoubleExtra("quantity", 0)));
            unit.setText(getIntent().getStringExtra("unit")); expiry.setText(getIntent().getStringExtra("expiry"));
        }
        findViewById(R.id.btnSaveIngredient).setOnClickListener(v -> save());
        findViewById(R.id.btnCancel).setOnClickListener(v -> finish());
    }
    private void save() {
        String n=name.getText().toString().trim(), q=quantity.getText().toString().trim(), u=unit.getText().toString().trim(), e=expiry.getText().toString().trim();
        if (TextUtils.isEmpty(n) || TextUtils.isEmpty(q) || TextUtils.isEmpty(u)) { Toast.makeText(this, "Please enter a name, quantity and unit.", Toast.LENGTH_LONG).show(); return; }
        double amount;
        try { amount=Double.parseDouble(q); if (!Double.isFinite(amount) || amount <= 0) throw new NumberFormatException(); }
        catch (NumberFormatException ex) { quantity.setError("Enter a quantity greater than zero"); return; }
        if (!e.isEmpty() && !e.matches("\\d{4}-\\d{2}-\\d{2}")) { expiry.setError("Use YYYY-MM-DD, or leave blank"); return; }
        PantryItem item = new PantryItem(itemId, n, amount, u, e);
        if (itemId == -1) db.addPantryItem(item); else db.updatePantryItem(item);
        Toast.makeText(this, "Ingredient saved.", Toast.LENGTH_SHORT).show(); finish();
    }
}
