import java.util.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class Customer {
    private String customerId;
    private String name;
    private boolean isVIP;
    private List<Order> orderHistory;
    private Map<FoodItem, Integer> cart;

    // Constructor
    public Customer(String customerId, String name) {
        this.customerId = customerId;
        this.name = name;
        this.isVIP = false;
        this.orderHistory = new ArrayList<>();
        this.cart = new HashMap<>();
    }

    public static Map<String, Customer> getCustomers() {
        return Main.customers;
    }

    // Methods
    public void becomeVIP() {
        this.isVIP = true;
    }

    public boolean isVIP() {
        return isVIP;
    }

    public void addOrderToHistory(Order order) {
        orderHistory.add(order);
    }

    public List<Order> getOrderHistory() {
        return orderHistory;
    }

    public String getCustomerId() {
        return customerId;
    }

    public Map<FoodItem, Integer> getCart() {
        return cart;
    }

    public void clearCart() {
        cart.clear();
    }

    public void addToCart(FoodItem item, int quantity) {
        cart.put(item, quantity);
    }

    public String getCartTotal() {
        double total = 0.0;
        for (FoodItem item : cart.keySet()) {
            total += item.getPrice() * cart.get(item);
        }
        return String.format("%.2f", total);
    }

    public String getName() {
        return name;
    }

}


class CustomerTest {
    private Customer customer;
    private FoodItem pizza;
    private FoodItem burger;
    private static final double DELTA = 0.001;

    @BeforeEach
    void setUp() {
        customer = new Customer("CUST123", "John Doe");
        pizza = new FoodItem("Pizza", 12.99, "Main Course", true);
        burger = new FoodItem("Burger", 8.99, "Main Course", true);
    }

    @Test
    void testConstructor() {
        assertNotNull(customer);
        assertEquals("CUST123", customer.getCustomerId());
        assertEquals("John Doe", customer.getName());
        assertFalse(customer.isVIP());
        assertTrue(customer.getOrderHistory().isEmpty());
        assertTrue(customer.getCart().isEmpty());
    }

    @Test
    void testBecomeVIP() {
        assertFalse(customer.isVIP());
        customer.becomeVIP();
        assertTrue(customer.isVIP());
    }

    @Test
    void testAddOrderToHistory() {
        Order order1 = new Order("CUST123", false);
        Order order2 = new Order("CUST123", false);

        customer.addOrderToHistory(order1);
        assertEquals(1, customer.getOrderHistory().size());
        assertTrue(customer.getOrderHistory().contains(order1));

        customer.addOrderToHistory(order2);
        assertEquals(2, customer.getOrderHistory().size());
        assertTrue(customer.getOrderHistory().contains(order2));
    }

    @Test
    void testGetOrderHistory() {
        assertTrue(customer.getOrderHistory().isEmpty());

        Order order = new Order("CUST123", false);
        customer.addOrderToHistory(order);

        List<Order> history = customer.getOrderHistory();
        assertNotNull(history);
        assertEquals(1, history.size());
        assertEquals(order, history.get(0));
    }

    @Test
    void testCartOperations() {
        // Test adding items to cart
        customer.addToCart(pizza, 2);
        assertEquals(1, customer.getCart().size());
        assertEquals(Integer.valueOf(2), customer.getCart().get(pizza));

        // Add another item
        customer.addToCart(burger, 1);
        assertEquals(2, customer.getCart().size());
        assertEquals(Integer.valueOf(1), customer.getCart().get(burger));

        // Test clearing cart
        customer.clearCart();
        assertTrue(customer.getCart().isEmpty());
    }

    @Test
    void testGetCartTotal() {
        // Empty cart
        assertEquals("0.00", customer.getCartTotal());

        // Add single item
        customer.addToCart(pizza, 2);
        assertEquals("25.98", customer.getCartTotal());

        // Add another item
        customer.addToCart(burger, 1);
        assertEquals("34.97", customer.getCartTotal());

        // Clear cart
        customer.clearCart();
        assertEquals("0.00", customer.getCartTotal());
    }

    @Test
    void testGetCartTotalPrecision() {
        FoodItem item = new FoodItem("Test Item", 1.23456, "Test", true);
        customer.addToCart(item, 2);
        assertEquals("2.47", customer.getCartTotal());
    }

    @Test
    void testCartModification() {
        // Test updating quantity of existing item
        customer.addToCart(pizza, 2);
        customer.addToCart(pizza, 3); // Should override previous quantity
        assertEquals(Integer.valueOf(3), customer.getCart().get(pizza));
    }

    @Test
    void testMultipleOperations() {
        // Test sequence of operations
        customer.addToCart(pizza, 2);
        customer.addToCart(burger, 1);
        assertEquals("34.97", customer.getCartTotal());

        customer.clearCart();
        assertEquals("0.00", customer.getCartTotal());

        customer.addToCart(pizza, 1);
        assertEquals("12.99", customer.getCartTotal());
    }

    @Test
    void testOrderHistoryManipulation() {
        Order order1 = new Order("CUST123", false);
        Order order2 = new Order("CUST123", true);
        Order order3 = new Order("CUST123", false);

        customer.addOrderToHistory(order1);
        customer.addOrderToHistory(order2);
        customer.addOrderToHistory(order3);

        List<Order> history = customer.getOrderHistory();
        assertEquals(3, history.size());
        assertEquals(order1, history.get(0));
        assertEquals(order2, history.get(1));
        assertEquals(order3, history.get(2));
    }

}