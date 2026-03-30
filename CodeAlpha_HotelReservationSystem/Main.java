public class Main {
    public static void main(String[] args) {
        HotelService service = new HotelService();
        PaymentService paymentService = new PaymentService();

        service.addRoom(new Room(1, "Standard", 1000));
        service.addRoom(new Room(2, "Deluxe", 2000));
        service.addRoom(new Room(3, "Suite", 3000));

        User user = new User(101, "Mukund");

        System.out.println("Available Deluxe Rooms:");
        for (Room r : service.searchRooms("Deluxe")) {
            System.out.println("Room ID: " + r.roomId);
        }

        Booking booking = service.bookRoom(user, "Deluxe");

        if (booking != null) {
            boolean paid = paymentService.processPayment(2000);
            booking.isPaid = paid;
        }

        service.viewBookings();

        if (booking != null) {
            service.cancelBooking(booking.bookingId);
        }
    }
}