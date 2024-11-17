import java.util.*;

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
