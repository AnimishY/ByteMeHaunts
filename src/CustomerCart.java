import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class CustomerCart extends JFrame {
    private JLabel H1;
    private JButton exploreMenuButton;
    private JButton ManageCart;
    private JButton viewPreviousOrdersButton;
    private JPanel CustomerDashboardPanel;
    private JComboBox MenuItemsList;
    private JTextField QtyInput;
    private JButton addToCartButton;
    private JTable table1;
    private JLabel CartTotal;
    private JComboBox CartItemList;
    private JButton removeItemFromCartButton;
    private JButton placeOrderButton;
    private JButton logOutButton;
    private JButton placeOrderAsVIPButton;

    public CustomerCart(Customer customer) {
        setContentPane(CustomerDashboardPanel);
        setTitle("Customer Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);

        // Populate the combo box with food item names
        for (FoodItem item : Main.menu.getAllItems()) {
            // Only show available items
            if (item.isAvailability()) {
                MenuItemsList.addItem(item.getName());
            }
        }

        // Populate the combo box with cart item names
        for (FoodItem item : customer.getCart().keySet()) {
            CartItemList.addItem(item.getName());
        }

        // display cart in table
        String[] columnNames = {"Name", "Price", "Quantity"};
        Map<FoodItem, Integer> cart = customer.getCart();
        Object[][] data = new Object[cart.size() + 1][3];
        data[0] = columnNames;
        int i = 1;
        for (FoodItem item : cart.keySet()) {
            data[i][0] = item.getName();
            data[i][1] = item.getPrice();
            data[i][2] = cart.get(item);
            i++;
        }
        table1.setModel(new javax.swing.table.DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        // display cart total
        CartTotal.setText("Total: " + customer.getCartTotal() + " INR");


        ManageCart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new CustomerDashboard(customer);
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
        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String itemName = (String) MenuItemsList.getSelectedItem();
                int quantity = Integer.parseInt(QtyInput.getText());
                FoodItem item = Main.menu.getItem(itemName);
                if (item != null && quantity > 0 && item.isAvailability()) {
                    customer.addToCart(item, quantity);
                    JOptionPane.showMessageDialog(null, "Item added to cart.");
                    new CustomerCart(customer);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Item not found.");
                }
            }
        });
        removeItemFromCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String itemName = (String) CartItemList.getSelectedItem();
                FoodItem item = Main.menu.getItem(itemName);
                if (item != null) {
                    customer.getCart().remove(item);
                    JOptionPane.showMessageDialog(null, "Item removed from cart.");
                    new CustomerCart(customer);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Item not found.");
                }
            }
        });
        placeOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Order order = new Order(customer.getCustomerId(), customer.isVIP());
                for (Map.Entry<FoodItem, Integer> entry : customer.getCart().entrySet()) {
                    order.addItem(entry.getKey(), entry.getValue());
                }
                Main.orderManager.placeOrder(order);
                customer.addOrderToHistory(order); // Add order to customer's order history
                customer.clearCart();
                JOptionPane.showMessageDialog(null, "Order placed successfully. Order ID: " + order.getOrderId());
                new CustomerDashboard(customer);
                dispose();
            }
        });
        viewPreviousOrdersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new CustomerOrderHistory(customer);
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
        placeOrderAsVIPButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // place order as VIP
                Order order = new Order(customer.getCustomerId(), true);
                for (Map.Entry<FoodItem, Integer> entry : customer.getCart().entrySet()) {
                    order.addItem(entry.getKey(), entry.getValue());
                }
                Main.orderManager.placeOrder(order);
                customer.addOrderToHistory(order); // Add order to customer's order history
                customer.clearCart();
                JOptionPane.showMessageDialog(null, "Order placed successfully as VIP. Order ID: " + order.getOrderId());
                new CustomerDashboard(customer);
                dispose();
            }
        });
    }
}