import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdminDashboard extends JFrame {
    private JLabel H1;
    private JButton manageMenuButton;
    private JTable table1;
    private JPanel AdminDashboardPanel;
    private JButton logOutButton;
    private JComboBox OrderIDselect;
    private JComboBox orderstatusselect;
    private JButton updateOrderButton;
    private JButton viewOrderDetailsButton;
    private JComboBox orderIDSelectAgain;
    private JLabel AllOrdersText;

    public AdminDashboard() {
        setContentPane(AdminDashboardPanel);
        setTitle("Admin Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);

        // Populate the combo box with order IDs
        for (Order order : Main.orderManager.getPendingOrders()) {
            OrderIDselect.addItem(String.valueOf(order.getOrderId()));
        }

        // Populate the combo box with order IDs again
        for (Order order : Main.orderManager.getAllOrders().values()) {
            orderIDSelectAgain.addItem(String.valueOf(order.getOrderId()));
        }

        // Populate the combo box with order statuses
        orderstatusselect.addItem("order received");
        orderstatusselect.addItem("preparing");
        orderstatusselect.addItem("out for delivery");
        orderstatusselect.addItem("delivered");

        //displays all orders in the table with VIP orders highlighted

        List<Order> pendingOrders = Main.orderManager.getPendingOrders();
        if (pendingOrders.isEmpty()) {
            System.out.println("No pending orders.");
        } else {
            // Separate VIP and regular orders
            List<Order> vipOrders = new ArrayList<>();
            List<Order> regularOrders = new ArrayList<>();
            for (Order order : pendingOrders) {
                if (order.isVIP()) {
                    vipOrders.add(order);
                } else {
                    regularOrders.add(order);
                }
            }

            // Create table model
            String[] columnNames = {"Order ID", "Customer ID", "Total Price", "Items", "Special Request"};

            List<Object[]> rowData = new ArrayList<>();

            //show headers as row1
            rowData.add(new Object[]{"Order ID", "Customer ID", "Total Price", "Items", "Special Request"});

            // add row to highlight vip orders
            rowData.add(new Object[]{"VIP Orders", "", "", "", ""});

            // Add VIP orders to table data
            for (Order order : vipOrders) {
                rowData.add(new Object[]{
                        order.getOrderId(),
                        order.getCustomerId(),
                        "INR " + String.format("%.2f", order.getTotalPrice()),
                        order.getItems().entrySet().stream()
                                .map(entry -> entry.getKey().getName() + " x" + entry.getValue())
                                .collect(Collectors.joining(", ")),
                        order.getSpecialRequest()
                });
            }

            rowData.add(new Object[]{"Regular Orders", "", "", "", ""});

            // Add regular orders to table data
            for (Order order : regularOrders) {
                rowData.add(new Object[]{
                        order.getOrderId(),
                        order.getCustomerId(),
                        "INR " + String.format("%.2f", order.getTotalPrice()),
                        order.getItems().entrySet().stream()
                                .map(entry -> entry.getKey().getName() + " x" + entry.getValue())
                                .collect(Collectors.joining("\n")),
                        order.getSpecialRequest()
                });
            }

            // Convert list to array
            Object[][] data = rowData.toArray(new Object[0][]);

            // Create table
            table1.setModel(new javax.swing.table.DefaultTableModel(
                    data,
                    columnNames
            ));

        }
        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new Main();
                dispose();
            }
        });
        manageMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new AdminManageMenu();
                dispose();
            }
        });
        updateOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int orderId = Integer.parseInt((String) OrderIDselect.getSelectedItem());
                String status = (String) orderstatusselect.getSelectedItem();
                Main.orderManager.updateOrderStatus(orderId, status);
                new AdminDashboard();
                dispose();
            }
        });
        viewOrderDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // display order details in a dialog box
                int orderId = Integer.parseInt((String) orderIDSelectAgain.getSelectedItem());
                Order order = Main.orderManager.getAllOrders().get(orderId);
                if (order != null) {
                    StringBuilder orderDetails = new StringBuilder();
                    orderDetails.append("Order ID: ").append(order.getOrderId()).append("\n");
                    orderDetails.append("Customer ID: ").append(order.getCustomerId()).append("\n");
                    orderDetails.append("Total Price: INR ").append(String.format("%.2f", order.getTotalPrice())).append("\n");
                    orderDetails.append("Items: ").append("\n");
                    for (FoodItem item : order.getItems().keySet()) {
                        orderDetails.append(item.getName()).append(" x").append(order.getItems().get(item)).append("\n");
                    }
                    orderDetails.append("Special Request: ").append(order.getSpecialRequest() != null ? order.getSpecialRequest() : "None").append("\n");
                    orderDetails.append("Status: ").append(order.getStatus()).append("\n");
                    JOptionPane.showMessageDialog(null, orderDetails.toString());
                } else {
                    JOptionPane.showMessageDialog(null, "Order not found.");
                }
            }
        });
    }
}