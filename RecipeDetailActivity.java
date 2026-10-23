package za.co.smartpantry;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); setContentView(R.layout.activity_recipe_detail);
        long id=getIntent().getLongExtra("recipeId", -1); DatabaseHelper db=new DatabaseHelper(this);
        Recipe selected=null; for (Recipe r: db.getRecipes()) if (r.id==id) { selected=r; break; }
        if (selected == null) { finish(); return; }
        ((TextView)findViewById(R.id.txtRecipeName)).setText(selected.name);
        StringBuilder ingredients=new StringBuilder();
        for (RecipeIngredient i: selected.ingredients) ingredients.append("• ").append(i.quantity).append(" ").append(i.unit).append(" ").append(i.name).append("\n");
        ((TextView)findViewById(R.id.txtRecipeIngredients)).setText(ingredients.toString());
        ((TextView)findViewById(R.id.txtRecipeMethod)).setText(selected.method);
        findViewById(R.id.btnBackRecipes).setOnClickListener(v -> finish());
    }
}
