public class Customer {
    private String name;
    private String contactNumber;
    private String roomType;
    private int roomNumber;   // New field to store booked room number
    private double roomPrice; // New field to store booked room price

    public Customer(String name, String contactNumber, String roomType, int roomNumber, double roomPrice) {
        this.name = name;
        this.contactNumber = contactNumber;
        this.roomType = roomType;
        this.roomNumber = roomNumber;
        this.roomPrice = roomPrice;
    }

    public String getName() {
        return name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public double getRoomPrice() {
        return roomPrice;
    }
}
