import java.util.*;

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
