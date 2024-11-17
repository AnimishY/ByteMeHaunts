import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

// library for csv
import java.io.FileWriter;
import java.io.IOException;

public class Main extends JFrame {
    private JRadioButton customerRadioButton;
    private JRadioButton adminRadioButton;
    private JButton enterButton;
    private JPanel MainPanel;
    private JLabel H1;
    private JLabel H2;
    private JButton exitButton;

    static Menu menu = new Menu();
    static OrderManager orderManager = new OrderManager();
    static Map<String, Customer> customers = new HashMap<>();
    private static Admin admin = new Admin("admin001", "Admin");

    public Main() {
        setContentPane(MainPanel);
        setTitle("Food Delivery System");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);

        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (customerRadioButton.isSelected()) {
                    new CustomerLogin();
                    dispose();
                } else if (adminRadioButton.isSelected()) {
                    new AdminLogin();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Please select an option.");
                }
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                saveDataToCSV();
                dispose();
            }
        });
    }

    public static void main(String[] args) {
        seedData();
        readDataFromCSV();
        new Main();
    }

    private static void seedData() {
        menu.addItem(new FoodItem("Burger", 55, "Snacks", true));
        menu.addItem(new FoodItem("Fries", 60, "Sides", true));
        menu.addItem(new FoodItem("Coke", 75, "Beverages", true));
        menu.addItem(new FoodItem("Pizza", 175, "Meals", true));
        menu.addItem(new FoodItem("Salad", 80, "Sides", true));
        menu.addItem(new FoodItem("Coffee", 120, "Beverages", true));
    }

    @Override
    public void dispose() {
        saveDataToCSV();
        super.dispose();
    }

    private static void readDataFromCSV() {
        try {
            menu.readFromCSV("menu.csv");
            customers = new HashMap<>();
            orderManager.loadFromCSV("ordersdetails.csv", "ordersheaders.csv", customers);

            System.out.println("Reading customers from CSV...");
            for (String[] row : CSVUtils.readCSV("customers.csv")) {
                customers.put(row[0], new Customer(row[0], row[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveDataToCSV() {
        try {
            menu.saveToCSV("menu.csv");
            orderManager.saveToCSV("ordersdetails.csv", "ordersheaders.csv");
            FileWriter writer = new FileWriter("customers.csv");
            for (Customer customer : customers.values()) {
                writer.write(customer.getCustomerId() + "," + customer.getName() + "\n");
            }
            writer.close();
            System.out.println("Data saved to CSV.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
