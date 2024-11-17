import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class CustomerDashboard extends JFrame {
    private JLabel H1;
    private JButton exploreMenuButton;
    private JButton ManageCart;
    private JButton viewPreviousOrdersButton;
    private JTable table1;
    private JPanel CustomerDashboardPanel;
    private JButton logOutButton;
    private JButton sortCheapestFirstButton;
    private JButton sortExpensiveFirstButton;
    private JComboBox categoryselect;
    private JButton filterByCategoryButton;

    public CustomerDashboard(Customer customer) {
        setContentPane(CustomerDashboardPanel);
        setTitle("Customer Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);

        // Populate the category dropdown
        // Get all the categories from the menu
        List<String> categories = new ArrayList<>();
        for (FoodItem item : Main.menu.getAllItems()) {
            if (!categories.contains(item.getCategory())) {
                categories.add(item.getCategory());
            }
        }

        // Add the categories to the dropdown
        for (String category : categories) {
            categoryselect.addItem(category);
        }

        displayMenu();

        exploreMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                displayMenu();
            }
        });
        ManageCart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new CustomerCart(customer);
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
        sortCheapestFirstButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                List<FoodItem> items = new ArrayList<>(Main.menu.getAllItems());
                items.sort((a, b) -> Double.compare(a.getPrice(), b.getPrice()));
                displayItems(items);
            }
        });
        sortExpensiveFirstButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                List<FoodItem> items = new ArrayList<>(Main.menu.getAllItems());
                items.sort((a, b) -> Double.compare(b.getPrice(), a.getPrice()));
                displayItems(items);
            }
        });
        filterByCategoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String category = (String) categoryselect.getSelectedItem();
                List<FoodItem> items = Main.menu.filterByCategory(category);
                displayItems(items);
            }
        });
    }

    private void displayItems(List<FoodItem> items) {
        // Display the items in the table
        String[] columnNames = {"Name", "Price", "Category", "Availability", "Rating"};
        Object[][] data = new Object[items.size() + 1][5];
        data[0] = columnNames;

        // Display the items in the table
        for (int i = 0; i < items.size(); i++) {
            FoodItem item = items.get(i);
            data[i + 1][0] = item.getName();
            data[i + 1][1] = item.getPrice();
            data[i + 1][2] = item.getCategory();
            data[i + 1][3] = item.isAvailability() ? "Available" : "Unavailable";
            data[i + 1][4] = String.format("%.1f", item.getAverageRating());
        }

        table1.setModel(new javax.swing.table.DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
    }

    public void displayMenu() {
        // Display the menu in the table
        String[] columnNames = {"Name", "Price", "Category", "Availability", "Rating"};
        List<FoodItem> items = new ArrayList<>(Main.menu.getAllItems());
        Object[][] data = new Object[items.size() + 1][5];
        data[0] = columnNames;

        for (int i = 0; i < items.size(); i++) {
            FoodItem item = items.get(i);
            data[i + 1][0] = item.getName();
            data[i + 1][1] = item.getPrice();
            data[i + 1][2] = item.getCategory();
            data[i + 1][3] = item.isAvailability() ? "Available" : "Unavailable";
            data[i + 1][4] = String.format("%.1f", item.getAverageRating());
        }

        table1.setModel(new javax.swing.table.DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
    }
}