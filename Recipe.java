package za.co.smartpantry;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    public long id;
    public String name, method;
    public List<RecipeIngredient> ingredients = new ArrayList<>();

    public Recipe(long id, String name, String method) {
        this.id = id; this.name = name; this.method = method;
    }
}
