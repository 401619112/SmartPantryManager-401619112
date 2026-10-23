package za.co.smartpantry;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.Holder> {
    public interface Listener { void onSelect(Recipe recipe); }
    private final List<Recipe> recipes; private final Listener listener;
    public RecipeAdapter(List<Recipe> recipes, Listener listener) { this.recipes=recipes; this.listener=listener; }
    @NonNull @Override public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recipe_item, parent, false));
    }
    @Override public void onBindViewHolder(@NonNull Holder h, int position) {
        Recipe recipe=recipes.get(position); h.name.setText(recipe.name);
        h.info.setText(recipe.ingredients.size() + " ingredients • All available in your pantry");
        h.itemView.setOnClickListener(v -> listener.onSelect(recipe));
    }
    @Override public int getItemCount() { return recipes.size(); }
    static class Holder extends RecyclerView.ViewHolder {
        TextView name, info;
        Holder(View v) { super(v); name=v.findViewById(R.id.txtRecipeRowName); info=v.findViewById(R.id.txtRecipeRowInfo); }
    }
}
