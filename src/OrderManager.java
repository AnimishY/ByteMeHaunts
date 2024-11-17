import java.io.*;
import java.util.*;

public class OrderManager {
    private PriorityQueue<Order> pendingOrders;
    private Map<Integer, Order> allOrders; // Key: Order ID
    private Map<String, List<Order>> customerOrderHistory; // Key: Customer ID
    private double totalRevenue;
    private int totalOrdersProcessed;

    public OrderManager() {
        this.pendingOrders = new PriorityQueue<>();
        this.allOrders = new HashMap<>();
        this.customerOrderHistory = new HashMap<>();
        this.totalRevenue = 0.0;
        this.totalOrdersProcessed = 0;
    }

    public void updateOrderStatus(int orderId, String status) {
        Order order = allOrders.get(orderId);
        if (order != null) {
            // Remove from pendingOrders if the status is being updated from a pending state
            if (isPendingStatus(order.getStatus()) && !isPendingStatus(status)) {
                pendingOrders.remove(order);
            }
            order.setStatus(status);

            if (status.equals("delivered")) {
                totalRevenue += order.getTotalPrice();
                totalOrdersProcessed++;
            }
        } else {
            System.out.println("Order ID not found.");
        }
    }

    private boolean isPendingStatus(String status) {
        return status.equals("order received") || status.equals("preparing") || status.equals("out for delivery");
    }

    public void processRefund(int orderId) {
        Order order = allOrders.get(orderId);
        if (order != null && (order.getStatus().equals("canceled") || order.getStatus().equals("delivered") || order.getStatus().equals("denied"))) {
            totalRevenue -= order.getTotalPrice();
            System.out.println("Refund processed for Order ID: " + orderId);
        } else {
            System.out.println("Refund not applicable for Order ID: " + orderId);
        }
    }

    public void generateDailySalesReport() {
        System.out.println("Total Revenue: INR " + String.format("%.2f", totalRevenue));
        System.out.println("Total Orders Processed: " + totalOrdersProcessed);
        // Additional report details can be added here
    }

    public List<Order> getPendingOrders() {
        List<Order> orders = new ArrayList<>(pendingOrders);
        orders.sort(null); // Sort according to priority
        return orders;
    }

    public Map<Integer, Order> getAllOrders() {
        return allOrders;
    }

    public List<Order> getCustomerOrderHistory(String customerId) {
        return customerOrderHistory.getOrDefault(customerId, new ArrayList<>());
    }

    public void cancelOrder(int orderId) {
        Order order = allOrders.get(orderId);
        if (order != null && order.getStatus().equals("order received")) {
            order.setStatus("canceled");
            pendingOrders.remove(order);
            System.out.println("Order ID " + orderId + " has been canceled.");
        } else {
            System.out.println("Order cannot be canceled.");
        }
    }

    public void denyOrdersWithItem(String itemName) {
        List<Order> ordersToUpdate = new ArrayList<>();

        for (Order order : pendingOrders) {
            if (order.containsItem(itemName)) {
                ordersToUpdate.add(order);
            }
        }

        for (Order order : ordersToUpdate) {
            updateOrderStatus(order.getOrderId(), "denied");
            System.out.println("Order ID " + order.getOrderId() + " has been denied due to item removal.");
        }
    }

    public void loadFromCSV(String orderDetailsFile, String orderHeadersFile, Map customer) {
        // Clear existing data structures
        pendingOrders.clear();
        allOrders.clear();
        customerOrderHistory.clear();

        // Clear all customers' order histories first
        for (Customer c : Main.customers.values()) {
            c.getOrderHistory().clear();
        }

        totalRevenue = 0.0;
        totalOrdersProcessed = 0;

        Map<Integer, Order> loadedOrders = new HashMap<>();

        // First, load order headers
        try (BufferedReader reader = new BufferedReader(new FileReader(orderHeadersFile))) {
            String line = reader.readLine(); // Skip header line
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int orderId = Integer.parseInt(parts[0]);
                String customerId = parts[1];
                boolean isVIP = Boolean.parseBoolean(parts[2]);

                Order order = new Order(customerId, isVIP);
                order.setOrderId(orderId);
                order.setStatus(parts[3]);
                if (!parts[4].isEmpty()) {
                    order.setSpecialRequest(parts[4].replace(";", ","));
                }
                order.setTotalPrice(Double.parseDouble(parts[5]));

                loadedOrders.put(orderId, order);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading order headers: " + e.getMessage());
        }

        // Then, load order details
        try (BufferedReader reader = new BufferedReader(new FileReader(orderDetailsFile))) {
            String line = reader.readLine(); // Skip header line
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int orderId = Integer.parseInt(parts[0]);
                String itemName = parts[1].replace(";", ",");
                double price = Double.parseDouble(parts[2]);
                int quantity = Integer.parseInt(parts[3]);

                Order order = loadedOrders.get(orderId);
                if (order != null) {
                    FoodItem foodItem = new FoodItem(itemName, price);
                    order.getItems().put(foodItem, quantity);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading order details: " + e.getMessage());
        }

        // Add all loaded orders to OrderManager structures
        for (Order order : loadedOrders.values()) {
            // Add to OrderManager structures
            placeOrder(order);

            // Update customerOrderHistory in OrderManager
            // write a function that takes the placed order and moves it to the private List<Order> orderHistory; in customer class
            // Update Customer's order history
            Customer customers = Main.customers.get(order.getCustomerId());
            System.out.println(customer);
            if (customers != null && !customers.getOrderHistory().contains(order)) {
                customers.addOrderToHistory(order);
            }


            // Update revenue and processed orders if the order is delivered
            if (order.getStatus().equals("delivered")) {
                totalRevenue += order.getTotalPrice();
                totalOrdersProcessed++;
            }
        }

        System.out.println("Orders loaded successfully.");


        // Output the customer ID and order ID for all orders in terminal
        for (Order order : allOrders.values()) {
            System.out.println("Customer ID: " + order.getCustomerId() + ", Order ID: " + order.getOrderId());
        }
    }
    // Also modify placeOrder to ensure customer history is updated
    public void placeOrder(Order order) {
        pendingOrders.add(order);
        allOrders.put(order.getOrderId(), order);

        // Update customerOrderHistory in OrderManager
        customerOrderHistory.computeIfAbsent(order.getCustomerId(), k -> new ArrayList<>()).add(order);

        // Update Customer's order history
        Customer customer = Main.customers.get(order.getCustomerId());
        if (customer != null && !customer.getOrderHistory().contains(order)) {
            customer.addOrderToHistory(order);
        }
    }

    // Also modify saveToCSV to handle orders in proper order
    public void saveToCSV(String orderDetailsFile, String orderHeadersFile) {
        try (PrintWriter detailsWriter = new PrintWriter(new FileWriter(orderDetailsFile));
             PrintWriter headersWriter = new PrintWriter(new FileWriter(orderHeadersFile))) {

            // Write headers for both files
            headersWriter.println("OrderID,CustomerID,IsVIP,Status,SpecialRequest,TotalPrice");
            detailsWriter.println("OrderID,FoodItem,Price,Quantity");

            // Get all orders sorted by ID to ensure consistent saving
            List<Order> sortedOrders = new ArrayList<>(allOrders.values());
            sortedOrders.sort(Comparator.comparingInt(Order::getOrderId));

            // Write data for each order
            for (Order order : sortedOrders) {
                // Write to headers file
                headersWriter.printf("%d,%s,%b,%s,%s,%.2f%n",
                        order.getOrderId(),
                        order.getCustomerId(),
                        order.isVIP(),
                        order.getStatus(),
                        order.getSpecialRequest() != null ? order.getSpecialRequest().replace(",", ";") : "",
                        order.getTotalPrice()
                );

                // Write to details file
                Map<FoodItem, Integer> items = order.getItems();
                for (Map.Entry<FoodItem, Integer> entry : items.entrySet()) {
                    FoodItem item = entry.getKey();
                    Integer quantity = entry.getValue();
                    detailsWriter.printf("%d,%s,%.2f,%d%n",
                            order.getOrderId(),
                            item.getName().replace(",", ";"),
                            item.getPrice(),
                            quantity
                    );
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error saving orders to CSV: " + e.getMessage());
        }
    }
}