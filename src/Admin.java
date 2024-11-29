import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Admin {
    private final String name;
    private String adminId;

    // Constructor
    public Admin(String adminId, String name) {
        this.adminId = adminId;
        this.name = name;
    }

    // Methods
    public void addFoodItem(Menu menu, FoodItem item) {
        menu.addItem(item);
    }

    public void updateFoodItem(Menu menu, String itemName, double price, boolean availability, String category) {
        menu.updateItem(itemName, price, availability, category);
    }

    public void removeFoodItem(Menu menu, OrderManager orderManager, String itemName) {
        menu.removeItem(itemName);
        orderManager.denyOrdersWithItem(itemName);
    }

    public void updateOrderStatus(OrderManager orderManager, int orderId, String status) {
        orderManager.updateOrderStatus(orderId, status);
    }

    public void processRefund(OrderManager orderManager, int orderId) {
        orderManager.processRefund(orderId);
    }

    public void generateDailySalesReport(OrderManager orderManager) {
        orderManager.generateDailySalesReport();
    }

    public String getAdminId() {
        return adminId;
    }

}


class AdminTest {
    private Admin admin;
    private Menu menu;
    private OrderManager orderManager;
    private FoodItem pizza;
    private FoodItem burger;

    @BeforeEach
    void setUp() {
        admin = new Admin("ADMIN123", "John Admin");
        menu = new Menu();
        orderManager = new OrderManager();
        pizza = new FoodItem("Pizza", 12.99, "Main Course", true);
        burger = new FoodItem("Burger", 8.99, "Main Course", true);
    }

    @Test
    void testConstructor() {
        Admin testAdmin = new Admin("ADMIN456", "Test Admin");
        assertNotNull(testAdmin);
        assertEquals("ADMIN456", testAdmin.getAdminId());
    }

    @Test
    void testUpdateFoodItem() {
        // First add an item
        admin.addFoodItem(menu, pizza);

        // Update the item
        admin.updateFoodItem(menu, "Pizza", 14.99, false, "Specials");

        // Find the updated item
        FoodItem updatedItem = null;
        for (FoodItem item : menu.getAllItems()) {
            if (item.getName().equals("Pizza")) {
                updatedItem = item;
                break;
            }
        }

        assertNotNull(updatedItem);
        assertEquals(14.99, updatedItem.getPrice());
        assertEquals("Specials", updatedItem.getCategory());
        assertFalse(updatedItem.isAvailability());
    }

    @Test
    void testGetAdminId() {
        assertEquals("ADMIN123", admin.getAdminId());
    }
}