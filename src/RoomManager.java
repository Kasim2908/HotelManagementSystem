import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class RoomManager {
    private Connection conn;

    public RoomManager() {
        try {
            // Change these credentials according to your MySQL setup
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/hotel_management?useSSL=false&serverTimezone=UTC",
                    "root",
                    "1234"
            );
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database connection failed. Exiting.");
            System.exit(1);
        }
    }

    public List<Room> getAvailableRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE occupied = FALSE ORDER BY room_number";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rooms.add(new Room(
                        rs.getInt("room_number"),
                        rs.getString("type"),
                        rs.getDouble("price")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    public Room getRoomByNumber(int roomNumber) {
        String sql = "SELECT * FROM rooms WHERE room_number = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Room room = new Room(
                            rs.getInt("room_number"),
                            rs.getString("type"),
                            rs.getDouble("price")
                    );
                    room.setOccupied(rs.getBoolean("occupied"));
                    return room;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean bookRoom(int roomNumber, Customer customer) {
        try {
            conn.setAutoCommit(false);

            // Check if room is free
            Room room = getRoomByNumber(roomNumber);
            if (room == null || room.isOccupied()) {
                conn.rollback();
                return false;
            }

            // Insert customer
            String insertCustomer = "INSERT INTO customers (name, contact_number, room_type, room_number, room_price) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertCustomer)) {
                ps.setString(1, customer.getName());
                ps.setString(2, customer.getContactNumber());
                ps.setString(3, customer.getRoomType());
                ps.setInt(4, customer.getRoomNumber());
                ps.setDouble(5, customer.getRoomPrice());
                ps.executeUpdate();
            }

            // Update room to occupied
            String updateRoom = "UPDATE rooms SET occupied = TRUE WHERE room_number = ?";
            try (PreparedStatement ps = conn.prepareStatement(updateRoom)) {
                ps.setInt(1, roomNumber);
                ps.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ignored) {}
            e.printStackTrace();
            return false;
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException ignored) {}
        }
    }

    public void checkOut(int roomNumber) {
        try {
            conn.setAutoCommit(false);

            // Delete customer with this room number
            String deleteCustomer = "DELETE FROM customers WHERE room_number = ?";
            try (PreparedStatement ps = conn.prepareStatement(deleteCustomer)) {
                ps.setInt(1, roomNumber);
                ps.executeUpdate();
            }

            // Mark room free
            String updateRoom = "UPDATE rooms SET occupied = FALSE WHERE room_number = ?";
            try (PreparedStatement ps = conn.prepareStatement(updateRoom)) {
                ps.setInt(1, roomNumber);
                ps.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ignored) {}
            e.printStackTrace();
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException ignored) {}
        }
    }

    public double getTotalPrice(int roomNumber) {
        String sql = "SELECT price FROM rooms WHERE room_number = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("price");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers ORDER BY room_number";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                customers.add(new Customer(
                        rs.getString("name"),
                        rs.getString("contact_number"),
                        rs.getString("room_type"),
                        rs.getInt("room_number"),
                        rs.getDouble("room_price")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }
}
