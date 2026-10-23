package za.co.smartpantry;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class PantryActivity extends AppCompatActivity {
    private DatabaseHelper db; private final List<PantryItem> items = new ArrayList<>(); private PantryAdapter adapter;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); setContentView(R.layout.activity_pantry); db = new DatabaseHelper(this);
        RecyclerView recycler = findViewById(R.id.recyclerPantry); recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PantryAdapter(items, new PantryAdapter.Listener() {
            @Override public void onEdit(PantryItem item) { Intent i = new Intent(PantryActivity.this, AddEditIngredientActivity.class); i.putExtra("id", item.id); i.putExtra("name", item.name); i.putExtra("quantity", item.quantity); i.putExtra("unit", item.unit); i.putExtra("expiry", item.expiryDate); startActivity(i); }
            @Override public void onDelete(PantryItem item) { db.deletePantryItem(item.id); loadItems(); }
        });
        recycler.setAdapter(adapter);
        findViewById(R.id.btnAddIngredient).setOnClickListener(v -> startActivity(new Intent(this, AddEditIngredientActivity.class)));
        findViewById(R.id.navHome).setOnClickListener(v -> { finish(); });
        findViewById(R.id.navRecipes).setOnClickListener(v -> startActivity(new Intent(this, SuggestedRecipesActivity.class)));
        findViewById(R.id.navSettings).setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
    }
    private void loadItems() { items.clear(); items.addAll(db.getPantryItems()); adapter.notifyDataSetChanged(); ((TextView)findViewById(R.id.txtEmptyPantry)).setVisibility(items.isEmpty() ? android.view.View.VISIBLE : android.view.View.GONE); }
    @Override protected void onResume() { super.onResume(); if (db != null && adapter != null) loadItems(); }
}
