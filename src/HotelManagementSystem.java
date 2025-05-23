import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class HotelManagementSystem {
    private JFrame frame;
    private JTextArea textArea;
    private RoomManager roomManager;

    public HotelManagementSystem() {
        roomManager = new RoomManager();
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Rose View Hotel");
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Title label
        JLabel titleLabel = new JLabel("ROSE VIEW HOTEL", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(109, 207, 190)); // Indigo
        titleLabel.setPreferredSize(new Dimension(frame.getWidth(), 80));
        frame.add(titleLabel, BorderLayout.NORTH);

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(214, 225, 225)); // Lavender
        buttonPanel.setLayout(new GridLayout(4, 1, 10, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 150, 30, 150));

        JButton btnAddCustomer = new JButton("Add Customer");
        JButton btnViewRooms = new JButton("View Available Rooms");
        JButton btnCheckOut = new JButton("Check Out");
        JButton btnViewCustomers = new JButton("View Customers");

        // Colorful buttons
        btnAddCustomer.setBackground(new Color(119, 176, 223)); // Steel Blue
        btnAddCustomer.setForeground(Color.WHITE);
        btnViewRooms.setBackground(new Color(60, 179, 113)); // Medium Sea Green
        btnViewRooms.setForeground(Color.WHITE);
        btnCheckOut.setBackground(new Color(225, 97, 123)); // Crimson
        btnCheckOut.setForeground(Color.WHITE);
        btnViewCustomers.setBackground(new Color(197, 189, 49)); // Dark Orange
        btnViewCustomers.setForeground(Color.WHITE);

        buttonPanel.add(btnAddCustomer);
        buttonPanel.add(btnViewRooms);
        buttonPanel.add(btnCheckOut);
        buttonPanel.add(btnViewCustomers);

        frame.add(buttonPanel, BorderLayout.CENTER);

        // Text area for showing info
        textArea = new JTextArea();
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        textArea.setEditable(false);
        textArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(580, 200));
        frame.add(scrollPane, BorderLayout.SOUTH);

        // Button actions
        btnAddCustomer.addActionListener(e -> addCustomer());
        btnViewRooms.addActionListener(e -> viewAvailableRooms());
        btnCheckOut.addActionListener(e -> checkOut());
        btnViewCustomers.addActionListener(e -> viewCustomers());

        frame.setLocationRelativeTo(null); // center screen
        frame.setVisible(true);
    }

    private void addCustomer() {
        try {
            String name = JOptionPane.showInputDialog(frame, "Enter Customer Name:");
            if (name == null || name.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Name cannot be empty.");
                return;
            }

            String inputRoom = JOptionPane.showInputDialog(frame, "Enter Room Number to Book:");
            if (inputRoom == null) return;
            int roomNumber = Integer.parseInt(inputRoom);

            Room room = roomManager.getRoomByNumber(roomNumber);
            if (room == null) {
                JOptionPane.showMessageDialog(frame, "Room does not exist.");
                return;
            }
            if (room.isOccupied()) {
                JOptionPane.showMessageDialog(frame, "Room already booked.");
                return;
            }

            String contact = getValidatedMobileNumber();
            if (contact == null) return; // user cancelled

            Customer customer = new Customer(name.trim(), contact, room.getType(), roomNumber, room.getPrice());
            boolean success = roomManager.bookRoom(roomNumber, customer);

            if (success) {
                JOptionPane.showMessageDialog(frame, "Room " + roomNumber + " successfully booked for " + name + ".");
                textArea.setText(""); // clear any previous text
            } else {
                JOptionPane.showMessageDialog(frame, "Booking failed.");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid room number.");
        }
    }

    private String getValidatedMobileNumber() {
        while (true) {
            String contact = JOptionPane.showInputDialog(frame, "Enter 10-digit Mobile Number:");
            if (contact == null) return null; // user cancelled
            if (contact.matches("\\d{10}")) {
                return contact;
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid mobile number. Please enter exactly 10 digits.");
            }
        }
    }

    private void viewAvailableRooms() {
        List<Room> rooms = roomManager.getAvailableRooms();
        if (rooms.isEmpty()) {
            textArea.setText("No rooms available right now.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-10s %-15s %-10s%n", "Room No.", "Room Type", "Price"));
        sb.append("=====================================\n");
        for (Room r : rooms) {
            sb.append(String.format("%-10d %-15s ₹%-10.2f%n", r.getRoomNumber(), r.getType(), r.getPrice()));
        }
        textArea.setText(sb.toString());
    }

    private void checkOut() {
        try {
            String inputRoom = JOptionPane.showInputDialog(frame, "Enter Room Number to Check Out:");
            if (inputRoom == null) return;
            int roomNumber = Integer.parseInt(inputRoom);

            Room room = roomManager.getRoomByNumber(roomNumber);
            if (room == null || !room.isOccupied()) {
                JOptionPane.showMessageDialog(frame, "Room is not currently occupied.");
                return;
            }

            double totalPrice = roomManager.getTotalPrice(roomNumber);
            int confirm = JOptionPane.showConfirmDialog(frame,
                    "Total price: ₹" + totalPrice + "\nConfirm Check Out?",
                    "Confirm Check Out",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                roomManager.checkOut(roomNumber);
                JOptionPane.showMessageDialog(frame, "Room " + roomNumber + " successfully checked out.");
                textArea.setText("");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid room number.");
        }
    }

    private void viewCustomers() {
        List<Customer> customers = roomManager.getAllCustomers();
        if (customers.isEmpty()) {
            textArea.setText("No customers currently checked in.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-20s %-15s %-10s %-10s %-10s%n", "Name", "Mobile", "Room No.", "Type", "Price"));
        sb.append("==============================================================\n");
        for (Customer c : customers) {
            sb.append(String.format("%-20s %-15s %-10d %-10s ₹%-10.2f%n",
                    c.getName(), c.getContactNumber(), c.getRoomNumber(), c.getRoomType(), c.getRoomPrice()));
        }
        textArea.setText(sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HotelManagementSystem::new);
    }
}
