package za.co.smartpantry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "smart_pantry.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) { super(context, DB_NAME, null, DB_VERSION); }

    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE pantry_items (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, quantity REAL NOT NULL, unit TEXT NOT NULL, expiry_date TEXT)");
        db.execSQL("CREATE TABLE recipes (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, method TEXT NOT NULL)");
        db.execSQL("CREATE TABLE recipe_ingredients (id INTEGER PRIMARY KEY AUTOINCREMENT, recipe_id INTEGER NOT NULL, name TEXT NOT NULL, quantity REAL NOT NULL, unit TEXT NOT NULL, FOREIGN KEY(recipe_id) REFERENCES recipes(id) ON DELETE CASCADE)");
        seedRecipes(db);
    }
    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS recipe_ingredients"); db.execSQL("DROP TABLE IF EXISTS recipes"); db.execSQL("DROP TABLE IF EXISTS pantry_items"); onCreate(db);
    }

    public long addPantryItem(PantryItem item) { return getWritableDatabase().insert("pantry_items", null, pantryValues(item)); }
    public int updatePantryItem(PantryItem item) { return getWritableDatabase().update("pantry_items", pantryValues(item), "id=?", new String[]{String.valueOf(item.id)}); }
    public int deletePantryItem(long id) { return getWritableDatabase().delete("pantry_items", "id=?", new String[]{String.valueOf(id)}); }
    private ContentValues pantryValues(PantryItem i) {
        ContentValues v = new ContentValues(); v.put("name", i.name); v.put("quantity", i.quantity); v.put("unit", i.unit); v.put("expiry_date", i.expiryDate); return v;
    }
    public List<PantryItem> getPantryItems() {
        List<PantryItem> list = new ArrayList<>();
        try (Cursor c = getReadableDatabase().query("pantry_items", null, null, null, null, null, "name COLLATE NOCASE")) {
            while (c.moveToNext()) list.add(new PantryItem(c.getLong(c.getColumnIndexOrThrow("id")), c.getString(c.getColumnIndexOrThrow("name")), c.getDouble(c.getColumnIndexOrThrow("quantity")), c.getString(c.getColumnIndexOrThrow("unit")), c.getString(c.getColumnIndexOrThrow("expiry_date"))));
        } return list;
    }
    public List<Recipe> getRecipes() {
        List<Recipe> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor c = db.query("recipes", null, null, null, null, null, "name COLLATE NOCASE")) {
            while (c.moveToNext()) {
                Recipe r = new Recipe(c.getLong(c.getColumnIndexOrThrow("id")), c.getString(c.getColumnIndexOrThrow("name")), c.getString(c.getColumnIndexOrThrow("method")));
                try (Cursor ic = db.query("recipe_ingredients", null, "recipe_id=?", new String[]{String.valueOf(r.id)}, null, null, "id")) {
                    while (ic.moveToNext()) r.ingredients.add(new RecipeIngredient(ic.getString(ic.getColumnIndexOrThrow("name")), ic.getDouble(ic.getColumnIndexOrThrow("quantity")), ic.getString(ic.getColumnIndexOrThrow("unit"))));
                }
                list.add(r);
            }
        } return list;
    }
    private void seedRecipes(SQLiteDatabase db) {
        String[][] data = {
            {"Tomato Egg Scramble","Eggs, tomatoes and onion make a quick pan meal.","egg|2|piece;tomato|2|piece;onion|1|piece"},
            {"Simple Tomato Pasta","A simple pasta tossed with tomatoes and garlic.","pasta|200|g;tomato|2|piece;garlic|2|piece;olive oil|1|tbsp"},
            {"Cheesy Omelette","A fluffy omelette with cheese.","egg|2|piece;cheese|40|g;milk|30|ml"},
            {"Garlic Fried Rice","Use cooked rice with garlic and egg for a quick meal.","rice|250|g;garlic|2|piece;egg|1|piece;oil|1|tbsp"},
            {"Potato Hash","Crisp potatoes with onion.","potato|300|g;onion|1|piece;oil|1|tbsp"},
            {"Banana Pancakes","Mash banana into a basic pancake batter.","banana|2|piece;egg|1|piece;flour|100|g;milk|100|ml"},
            {"Apple Oat Porridge","Warm oats with apple and milk.","oats|60|g;apple|1|piece;milk|200|ml"},
            {"Carrot Soup","A smooth soup made with carrot and onion.","carrot|3|piece;onion|1|piece;stock|500|ml"},
            {"Tuna Pasta","Pasta with tuna and tomato.","pasta|200|g;tuna|1|can;tomato|2|piece"},
            {"Bean Salad","A quick bean salad with tomato and onion.","beans|200|g;tomato|2|piece;onion|1|piece;lemon|1|piece"},
            {"Peanut Butter Toast","Toast bread and spread peanut butter.","bread|2|piece;peanut butter|2|tbsp"},
            {"Spinach Egg Wrap","Eggs and spinach wrapped in a tortilla.","egg|2|piece;spinach|80|g;tortilla|1|piece"},
            {"Cheese Tomato Toast","Toast topped with tomato and cheese.","bread|2|piece;tomato|1|piece;cheese|40|g"},
            {"Lentil Curry","Simmer lentils with tomato and curry powder.","lentils|200|g;tomato|2|piece;curry powder|1|tsp;onion|1|piece"},
            {"Chickpea Lemon Bowl","Chickpeas with lemon and olive oil.","chickpeas|200|g;lemon|1|piece;olive oil|1|tbsp"},
            {"Yoghurt Banana Bowl","A simple fruit and yoghurt bowl.","yoghurt|150|g;banana|1|piece;oats|30|g"},
            {"Flour Flatbread","A quick flatbread with flour, water and oil.","flour|200|g;water|120|ml;oil|1|tbsp"},
            {"Corn Fritters","Corn and egg fritters with flour.","corn|150|g;egg|1|piece;flour|60|g"},
            {"Mushroom Garlic Pasta","Garlic mushrooms tossed through pasta.","pasta|200|g;mushroom|150|g;garlic|2|piece;olive oil|1|tbsp"},
            {"Roasted Carrots and Potatoes","Roast carrots and potatoes with oil.","carrot|2|piece;potato|250|g;oil|1|tbsp"}
        };
        for (String[] row : data) {
            String[] ingredients = row[2].split(";");
            ContentValues rv = new ContentValues(); rv.put("name", row[0]); rv.put("method", row[1] + " Prepare the ingredients, cook until tender and season to taste."); long recipeId = db.insert("recipes", null, rv);
            for (String ingredient : ingredients) {
                String[] p = ingredient.split("\\|");
                ContentValues iv = new ContentValues(); iv.put("recipe_id", recipeId); iv.put("name", p[0]); iv.put("quantity", Double.parseDouble(p[1])); iv.put("unit", p[2]); db.insert("recipe_ingredients", null, iv);
            }
        }
    }
}
