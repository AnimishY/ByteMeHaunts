import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class AdminManageMenu extends JFrame{
    private JLabel H1;
    private JButton logOutButton;
    private JButton manageMenuButton;
    private JButton backToDashButton;
    private JTable table1;
    private JComboBox comboBox1;
    private JButton toggleAvailabilityButton;
    private JButton removeFromMenuButton;
    private JPanel AdminManageMenuPanel;
    private JButton AddNewItem;

    public AdminManageMenu() {
        setContentPane(AdminManageMenuPanel);
        setTitle("Admin Manage Menu");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);

        //displays all menu items in the table
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

        //populate the combobox with all menu items
        for (FoodItem item : items) {
            comboBox1.addItem(item.getName());
        }

        toggleAvailabilityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String itemName = (String) comboBox1.getSelectedItem();
                FoodItem item = Main.menu.getItem(itemName);
                item.setAvailability(!item.isAvailability());
                JOptionPane.showMessageDialog(null, "Availability of " + itemName + " is now " + (item.isAvailability() ? "Available" : "Unavailable"));
                new AdminManageMenu();
                dispose();
            }
        });

        removeFromMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String itemName = (String) comboBox1.getSelectedItem();
                Main.menu.removeItem(itemName);
                JOptionPane.showMessageDialog(null, itemName + " removed from menu.");
                new AdminManageMenu();
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
        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new Main();
                dispose();
            }
        });
        backToDashButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new AdminDashboard();
                dispose();
            }
        });
        AddNewItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new AdminAddNewItem();
                dispose();
            }
        });
    }
}
