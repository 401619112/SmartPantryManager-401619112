package za.co.smartpantry;

public class PantryItem {
    public long id;
    public String name, unit, expiryDate;
    public double quantity;

    public PantryItem(long id, String name, double quantity, String unit, String expiryDate) {
        this.id = id; this.name = name; this.quantity = quantity;
        this.unit = unit == null ? "" : unit; this.expiryDate = expiryDate == null ? "" : expiryDate;
    }
}
