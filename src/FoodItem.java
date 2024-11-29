import java.util.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
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

class FoodItemTest {
    private FoodItem foodItem;
    private static final double DELTA = 0.001; // For double comparisons

    @BeforeEach
    void setUp() {
        foodItem = new FoodItem("Pizza", 12.99, "Main Course", true);
    }

    @Test
    void testMainConstructor() {
        assertNotNull(foodItem);
        assertEquals("Pizza", foodItem.getName());
        assertEquals(12.99, foodItem.getPrice(), DELTA);
        assertEquals("Main Course", foodItem.getCategory());
        assertTrue(foodItem.isAvailability());
        assertEquals(0.0, foodItem.getAverageRating(), DELTA);
    }

    @Test
    void testSimplifiedConstructor() {
        FoodItem simpleItem = new FoodItem("Burger", 8.99);
        assertNotNull(simpleItem);
        assertEquals("Burger", simpleItem.getName());
        assertEquals(8.99, simpleItem.getPrice(), DELTA);
        assertEquals("Part", simpleItem.getCategory());
        assertTrue(simpleItem.isAvailability());
        assertEquals(0.0, simpleItem.getAverageRating(), DELTA);
    }

    @Test
    void testGetters() {
        assertEquals("Pizza", foodItem.getName());
        assertEquals(12.99, foodItem.getPrice(), DELTA);
        assertEquals("Main Course", foodItem.getCategory());
        assertTrue(foodItem.isAvailability());
    }

    @Test
    void testSetPrice() {
        foodItem.setPrice(15.99);
        assertEquals(15.99, foodItem.getPrice(), DELTA);

        foodItem.setPrice(0.0);
        assertEquals(0.0, foodItem.getPrice(), DELTA);
    }

    @Test
    void testSetAvailability() {
        foodItem.setAvailability(false);
        assertFalse(foodItem.isAvailability());

        foodItem.setAvailability(true);
        assertTrue(foodItem.isAvailability());
    }

    @Test
    void testSetCategory() {
        foodItem.setCategory("Dessert");
        assertEquals("Dessert", foodItem.getCategory());

        foodItem.setCategory("");
        assertEquals("", foodItem.getCategory());
    }

    @Test
    void testAddRating() {
        foodItem.addRating(5);
        assertEquals(5.0, foodItem.getAverageRating(), DELTA);

        foodItem.addRating(4);
        assertEquals(4.5, foodItem.getAverageRating(), DELTA);

        foodItem.addRating(3);
        assertEquals(4.0, foodItem.getAverageRating(), DELTA);
    }

    @Test
    void testAverageRatingWithNoRatings() {
        assertEquals(0.0, foodItem.getAverageRating(), DELTA);
    }

    @Test
    void testAverageRatingWithMultipleRatings() {
        foodItem.addRating(5);
        foodItem.addRating(4);
        foodItem.addRating(3);
        foodItem.addRating(5);
        foodItem.addRating(4);

        // Average should be (5+4+3+5+4)/5 = 4.2
        assertEquals(4.2, foodItem.getAverageRating(), DELTA);
    }

    @Test
    void testAddRatingBoundaryValues() {
        foodItem.addRating(1); // Minimum rating
        assertEquals(1.0, foodItem.getAverageRating(), DELTA);

        foodItem.addRating(5); // Maximum rating
        assertEquals(3.0, foodItem.getAverageRating(), DELTA);

        foodItem.addRating(3); // Middle rating
        assertEquals(3.0, foodItem.getAverageRating(), DELTA);
    }

    @Test
    void testMultiplePropertyChanges() {
        // Test changing multiple properties in sequence
        foodItem.setPrice(20.99);
        foodItem.setCategory("Special");
        foodItem.setAvailability(false);

        assertEquals(20.99, foodItem.getPrice(), DELTA);
        assertEquals("Special", foodItem.getCategory());
        assertFalse(foodItem.isAvailability());
    }

    @Test
    void testRatingCalculationPrecision() {
        // Test precision of average calculation
        foodItem.addRating(1);
        foodItem.addRating(2);
        foodItem.addRating(3);

        // Average should be (1+2+3)/3 = 2.0
        assertEquals(2.0, foodItem.getAverageRating(), DELTA);
    }

    @Test
    void testLargeNumberOfRatings() {
        // Test with a larger number of ratings
        for (int i = 1; i <= 100; i++) {
            foodItem.addRating(5);
        }
        assertEquals(5.0, foodItem.getAverageRating(), DELTA);
    }
}