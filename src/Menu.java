import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static java.awt.SystemColor.menu;

public class Menu {
    private static Map<String, FoodItem> foodItems;

    public Menu() {
        this.foodItems = new TreeMap<>();
    }


    public void loadFromCSV(String filePath) throws IOException {
        List<String[]> data = CSVUtils.readCSV(filePath);
        for (String[] row : data) {
            addItem(new FoodItem(row[0], Double.parseDouble(row[1]), row[2], Boolean.parseBoolean(row[3])));
        }
    }

    public void saveToCSV(String filePath) throws IOException {
        List<String[]> data = new ArrayList<>();
        for (FoodItem item : foodItems.values()) {
            data.add(new String[]{item.getName(), String.valueOf(item.getPrice()), item.getCategory(), String.valueOf(item.isAvailability())});
        }
        CSVUtils.writeCSV(filePath, data);
    }

    public void addItem(FoodItem item) {
        foodItems.put(item.getName().toLowerCase(), item);
    }

    public void updateItem(String name, double price, boolean availability, String category) {
        FoodItem item = foodItems.get(name.toLowerCase());
        if (item != null) {
            item.setPrice(price);
            item.setAvailability(availability);
            item.setCategory(category);
        } else {
            System.out.println("Item not found.");
        }
    }

    public void removeItem(String name) {
        if (foodItems.containsKey(name.toLowerCase())) {
            foodItems.remove(name.toLowerCase());
        } else {
            System.out.println("Item not found.");
        }
    }

    public Collection<FoodItem> getAllItems() {
        return foodItems.values();
    }

    public static FoodItem getItem(String name) {
        return foodItems.get(name.toLowerCase());
    }

    public List<FoodItem> searchItems(String keyword) {
        List<FoodItem> results = new ArrayList<>();
        for (FoodItem item : foodItems.values()) {
            if (item.getName().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(item);
            }
        }
        return results;
    }

    public List<FoodItem> filterByCategory(String category) {
        List<FoodItem> results = new ArrayList<>();
        for (FoodItem item : foodItems.values()) {
            if (item.getCategory().equalsIgnoreCase(category)) {
                results.add(item);
            }
        }
        return results;
    }

    public FoodItem getItemByName(String itemName) {
        return foodItems.get(itemName);
    }

    public void readFromCSV(String s) {
        // read from csv
        try {
            List<String[]> data = CSVUtils.readCSV(s);
            for (String[] row : data) {
                addItem(new FoodItem(row[0], Double.parseDouble(row[1]), row[2], Boolean.parseBoolean(row[3])));
                //get ratings from csv
                if (row.length > 4) {
                    FoodItem item = getItem(row[0]);
                    String[] ratings = row[4].split(",");
                    for (String rating : ratings) {
                        item.addRating(Integer.parseInt(rating));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
