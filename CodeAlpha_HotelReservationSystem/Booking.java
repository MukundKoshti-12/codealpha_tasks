class Booking {
    int bookingId;
    int userId;
    int roomId;
    String category;
    boolean isPaid;

    public Booking(int bookingId, int userId, int roomId, String category) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.roomId = roomId;
        this.category = category;
        this.isPaid = false;
    }
}