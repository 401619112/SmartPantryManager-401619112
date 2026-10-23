package za.co.smartpantry;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class IngredientMatcher {
    private IngredientMatcher() {}

    public static String normalize(String value) {
        if (value == null) return "";
        String s = value.trim().toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9 ]", "").replaceAll("\\s+", " ");
        // Small, predictable normalization for common English plurals.
        if (s.endsWith("ies") && s.length() > 4) s = s.substring(0, s.length() - 3) + "y";
        else if (s.endsWith("oes") && s.length() > 4) s = s.substring(0, s.length() - 2);
        else if (s.endsWith("s") && !s.endsWith("ss") && s.length() > 3) s = s.substring(0, s.length() - 1);
        return s;
    }

    private static String unit(String raw) {
        String u = raw == null ? "" : raw.trim().toLowerCase(Locale.ROOT);
        if (u.equals("pieces") || u.equals("pcs")) return "piece";
        if (u.equals("grams")) return "g";
        if (u.equals("kilograms") || u.equals("kg")) return "kg";
        if (u.equals("millilitres") || u.equals("milliliters") || u.equals("ml")) return "ml";
        if (u.equals("litres") || u.equals("liters") || u.equals("l")) return "l";
        if (u.equals("cups")) return "cup";
        if (u.equals("tablespoons") || u.equals("tbsp")) return "tbsp";
        if (u.equals("teaspoons") || u.equals("tsp")) return "tsp";
        return u;
    }

    // Conservative conversions only. Unknown or incompatible units do not match.
    private static double inBase(double quantity, String rawUnit) {
        String u = unit(rawUnit);
        if (u.equals("kg")) return quantity * 1000.0;
        if (u.equals("l")) return quantity * 1000.0;
        if (u.equals("g") || u.equals("ml") || u.equals("piece") || u.equals("cup") || u.equals("tbsp") || u.equals("tsp") || u.isEmpty()) return quantity;
        return Double.NaN;
    }
    private static String dimension(String rawUnit) {
        String u = unit(rawUnit);
        if (u.equals("g") || u.equals("kg")) return "mass";
        if (u.equals("ml") || u.equals("l")) return "volume";
        if (u.equals("piece") || u.isEmpty()) return "count";
        if (u.equals("cup") || u.equals("tbsp") || u.equals("tsp")) return "kitchen-volume";
        return "unknown:" + u;
    }

    public static boolean canMake(Recipe recipe, List<PantryItem> pantry) {
        Map<String, PantryItem> available = new HashMap<>();
        for (PantryItem item : pantry) {
            String key = normalize(item.name);
            PantryItem previous = available.get(key);
            if (previous == null || compatibleAndEnough(item, previous)) available.put(key, item);
        }
        for (RecipeIngredient required : recipe.ingredients) {
            PantryItem found = available.get(normalize(required.name));
            if (found == null) return false;
            if (!dimension(found.unit).equals(dimension(required.unit))) return false;
            double have = inBase(found.quantity, found.unit);
            double need = inBase(required.quantity, required.unit);
            if (Double.isNaN(have) || Double.isNaN(need) || have + 0.000001 < need) return false;
        }
        return true;
    }
    private static boolean compatibleAndEnough(PantryItem candidate, PantryItem previous) {
        if (!dimension(candidate.unit).equals(dimension(previous.unit))) return false;
        return inBase(candidate.quantity, candidate.unit) > inBase(previous.quantity, previous.unit);
    }
}
