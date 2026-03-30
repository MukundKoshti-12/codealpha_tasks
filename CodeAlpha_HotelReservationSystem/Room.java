class Room {
    int roomId;
    String category;
    double price;
    boolean isAvailable;

    public Room(int roomId, String category, double price) {
        this.roomId = roomId;
        this.category = category;
        this.price = price;
        this.isAvailable = true;
    }
}