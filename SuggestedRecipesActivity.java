package za.co.smartpantry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class SuggestedRecipesActivity extends AppCompatActivity {
    private DatabaseHelper db; private final List<Recipe> matches = new ArrayList<>(); private RecipeAdapter adapter;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); setContentView(R.layout.activity_recipes); db = new DatabaseHelper(this);
        RecyclerView recycler=findViewById(R.id.recyclerRecipes); recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter=new RecipeAdapter(matches, recipe -> { Intent i=new Intent(this, RecipeDetailActivity.class); i.putExtra("recipeId", recipe.id); startActivity(i); }); recycler.setAdapter(adapter);
        findViewById(R.id.navHome).setOnClickListener(v -> finish());
        findViewById(R.id.navPantry).setOnClickListener(v -> startActivity(new Intent(this, PantryActivity.class)));
        findViewById(R.id.navSettings).setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
    }
    private void refresh() {
        matches.clear(); List<PantryItem> pantry=db.getPantryItems();
        for (Recipe recipe: db.getRecipes()) if (IngredientMatcher.canMake(recipe, pantry)) matches.add(recipe);
        adapter.notifyDataSetChanged(); findViewById(R.id.txtNoRecipes).setVisibility(matches.isEmpty() ? View.VISIBLE : View.GONE);
    }
    @Override protected void onResume() { super.onResume(); if (db != null && adapter != null) refresh(); }
}
