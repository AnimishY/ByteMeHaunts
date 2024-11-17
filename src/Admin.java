import java.util.*;


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
