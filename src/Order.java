import java.util.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


public class Order implements Comparable<Order> {
    private static int orderCounter = 1;
    private int orderId;
    private String customerId;
    private boolean isVIP;
    private Map<FoodItem, Integer> items;
    private String status; // 'order received', 'preparing', 'out for delivery', 'delivered', 'denied', 'canceled'
    private String specialRequest;
    private double totalPrice;
    private Date orderDate;

    // Constructor
    public Order(String customerId, boolean isVIP) {
        this.orderId = orderCounter++;
        this.customerId = customerId;
        this.isVIP = isVIP;
        this.items = new HashMap<>();
        this.status = "order received";
        this.totalPrice = 0.0;
        this.orderDate = new Date();
    }



    // Methods
    public void addItem(FoodItem item, int quantity) {
        items.put(item, items.getOrDefault(item, 0) + quantity);
        totalPrice += item.getPrice() * quantity;
    }

    public boolean containsItem(String itemName) {
        for (FoodItem item : items.keySet()) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return true;
            }
        }
        return false;
    }

    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }

    public boolean isVIP() {
        return isVIP;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<FoodItem, Integer> getItems() {
        return items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setSpecialRequest(String request) {
        this.specialRequest = request;
    }

    public String getSpecialRequest() {
        return specialRequest;
    }

    // Override methods
    @Override
    public int compareTo(Order other) {
        if (this.isVIP && !other.isVIP) {
            return -1;
        } else if (!this.isVIP && other.isVIP) {
            return 1;
        } else {
            return this.orderDate.compareTo(other.orderDate);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !(obj instanceof Order)) return false;
        Order other = (Order) obj;
        return this.orderId == other.orderId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }

    public void setOrderId(int i) {
        if (i >= orderCounter) {
            orderCounter = i + 1;
        }
    }

    public void setTotalPrice(double v) {
        totalPrice = v;
    }

    public void setFoodItems(List<String> list) {
        for (String s : list) {
            String[] parts = s.split(",");
            FoodItem item = new FoodItem(parts[0], Double.parseDouble(parts[1]));
            items.put(item, Integer.parseInt(parts[2]));
        }
    }

    public Map<Object, Object> getFoodItems() {
        Map<Object, Object> map = new HashMap<>();
        for (FoodItem item : items.keySet()) {
            map.put(item, items.get(item));
        }
        return map;
    }
}


class OrderTest {
    private Order order;
    private FoodItem pizza;
    private FoodItem burger;

    @BeforeEach
    void setUp() {
        order = new Order("CUST123", true);
        pizza = new FoodItem("Pizza", 12.99, "Main Course", true);
        burger = new FoodItem("Burger", 8.99, "Main Course", true);
    }

    @Test
    void testOrderConstructor() {
        assertNotNull(order);
        assertEquals("CUST123", order.getCustomerId());
        assertTrue(order.isVIP());
        assertEquals("order received", order.getStatus());
        assertEquals(0.0, order.getTotalPrice());
    }

    @Test
    void testAddItem() {
        order.addItem(pizza, 2);
        assertEquals(25.98, order.getTotalPrice(), 0.01);
        assertTrue(order.containsItem("Pizza"));

        order.addItem(burger, 1);
        assertEquals(34.97, order.getTotalPrice(), 0.01);
        assertTrue(order.containsItem("Burger"));
    }


    @Test
    void testSetStatus() {
        order.setStatus("preparing");
        assertEquals("preparing", order.getStatus());

        order.setStatus("delivered");
        assertEquals("delivered", order.getStatus());
    }

    @Test
    void testSpecialRequest() {
        order.setSpecialRequest("Extra cheese please");
        assertEquals("Extra cheese please", order.getSpecialRequest());
    }

    @Test
    void testCompareToVIP() {
        Order nonVipOrder = new Order("CUST456", false);
        assertEquals(-1, order.compareTo(nonVipOrder)); // VIP order should come first
        assertEquals(1, nonVipOrder.compareTo(order)); // Non-VIP order should come last
    }


    @Test
    void testSetFoodItems() {
        List<String> foodItems = Arrays.asList(
                "Pizza,12.99,2",
                "Burger,8.99,1"
        );
        order.setFoodItems(foodItems);

        Map<Object, Object> items = order.getFoodItems();
        assertEquals(2, items.size());

        boolean foundPizza = false;
        boolean foundBurger = false;

        for (Map.Entry<Object, Object> entry : items.entrySet()) {
            FoodItem item = (FoodItem) entry.getKey();
            if (item.getName().equals("Pizza")) {
                assertEquals(2, entry.getValue());
                foundPizza = true;
            }
            if (item.getName().equals("Burger")) {
                assertEquals(1, entry.getValue());
                foundBurger = true;
            }
        }

        assertTrue(foundPizza && foundBurger);
    }

    @Test
    void testGetItems() {
        order.addItem(pizza, 2);
        order.addItem(burger, 1);

        Map<FoodItem, Integer> items = order.getItems();
        assertEquals(2, items.size());
        assertEquals(Integer.valueOf(2), items.get(pizza));
        assertEquals(Integer.valueOf(1), items.get(burger));
    }
}