import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.stream.Collectors;

public class CustomerOrderHistory extends JFrame {
    private JLabel H1;
    private JButton exploreMenuButton;
    private JButton ManageCart;
    private JButton viewPreviousOrdersButton;
    private JTable table1;
    private JComboBox comboBox1;
    private JButton cancelOrderButton;
    private JPanel CustomerOrderHistory;
    private JButton logOutButton;

    public CustomerOrderHistory(Customer customer) {
        setContentPane(CustomerOrderHistory);
        setTitle("Customer Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);

        // iterate over all orders and populate the combo box with order IDs related to customerid
        for (Order order : Main.orderManager.getAllOrders().values().stream().filter(order -> order.getCustomerId().equals(customer.getCustomerId())).collect(Collectors.toList())) {
            comboBox1.addItem(order.getOrderId());
        }

        // populate the table with order details
        String[] columnNames = {"Order ID", "Order Items", "Total", "Status"};
        //first create an array of orders whose order id is in the combo box
        Object[][] data = new Object[comboBox1.getItemCount()][4];
        for (int i = 0; i < comboBox1.getItemCount(); i++) {
            Order order = Main.orderManager.getAllOrders().get(comboBox1.getItemAt(i));
            data[i][0] = order.getOrderId();
            // show order details like Burger: 5; Pizza: 2
            StringBuilder orderItems = new StringBuilder();
            for (Object item : order.getFoodItems().keySet()) {
                orderItems.append(((FoodItem) item).getName()).append(": ").append(order.getFoodItems().get(item)).append("; ");
            }
            data[i][1] = orderItems.toString();
            data[i][2] = order.getTotalPrice();
            data[i][3] = order.getStatus();
        }

        System.out.println("Order History: ");
        for (Order order : customer.getOrderHistory()) {
            System.out.println("Order ID: " + order.getOrderId() + ", Total: " + order.getTotalPrice() + ", Status: " + order.getStatus());
        }

        if (data.length == 0) {
            JOptionPane.showMessageDialog(null, "No orders found.");
        } else {
            table1.setModel(new javax.swing.table.DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            });

        }

        // Display the order details in the table
        viewPreviousOrdersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new CustomerOrderHistory(customer);
                dispose();
            }
        });

        // Cancel order button
        cancelOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int orderId = (int) comboBox1.getSelectedItem();
                Order order = Main.orderManager.getAllOrders().get(orderId);
                if (order != null && order.getCustomerId().equals(customer.getCustomerId())) {
                    if (order.getStatus().equals("order received")) {
                        Main.orderManager.cancelOrder(orderId);
                        System.out.println("Order canceled successfully.");
                        JOptionPane.showMessageDialog(null, "Order " + orderId + " has been cancelled.");
                    } else {
                        System.out.println("Order cannot be canceled at this stage.");
                        JOptionPane.showMessageDialog(null, "Order cannot be canceled at this stage.");
                    }
                } else {
                    System.out.println("Order not found or does not belong to you.");
                }
                dispose();
                new CustomerOrderHistory(customer);
            }
        });
        viewPreviousOrdersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new CustomerOrderHistory(customer);
                dispose();
            }
        });
        exploreMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new CustomerDashboard(customer);
                dispose();
            }
        });
        ManageCart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new CustomerCart(customer);
                dispose();
            }
        });
        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new Main();
                dispose();
            }
        });
    }
}