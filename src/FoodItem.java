import java.util.*;

public class FoodItem {
    private String name;
    private double price;
    private String category;
    private boolean availability;
    private List<Integer> ratings;

    // Constructor
    public FoodItem(String name, double price, String category, boolean availability) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.availability = availability;
        this.ratings = new ArrayList<>();
    }

    public FoodItem(String part, double price) {
        this.name = part;
        this.price = price;
        this.category = "Part";
        this.availability = true;
        this.ratings = new ArrayList<>();
    }

    // Getter methods
    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    // Rating methods
    public void addRating(int rating) {
        ratings.add(rating);
    }

    public double getAverageRating() {
        if (ratings.isEmpty()) {
            return 0.0;
        }
        int sum = 0;
        for (int r : ratings) {
            sum += r;
        }
        return (double) sum / ratings.size();
    }
}
