import java.util.*;
import java.io.*;

class HotelService {
    List<Room> rooms = new ArrayList<>();
    List<Booking> bookings = new ArrayList<>();
    int bookingCounter = 1;

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public List<Room> searchRooms(String category) {
        List<Room> result = new ArrayList<>();
        for (Room r : rooms) {
            if (r.category.equalsIgnoreCase(category) && r.isAvailable) {
                result.add(r);
            }
        }
        return result;
    }

    public Booking bookRoom(User user, String category) {
        for (Room r : rooms) {
            if (r.category.equalsIgnoreCase(category) && r.isAvailable) {
                r.isAvailable = false;

                Booking booking = new Booking(
                    bookingCounter++, user.userId, r.roomId, category
                );

                bookings.add(booking);
                saveBookingToFile(booking);
                return booking;
            }
        }
        return null;
    }

    public void cancelBooking(int bookingId) {
        Iterator<Booking> iterator = bookings.iterator();
        while (iterator.hasNext()) {
            Booking b = iterator.next();
            if (b.bookingId == bookingId) {
                for (Room r : rooms) {
                    if (r.roomId == b.roomId) {
                        r.isAvailable = true;
                        break;
                    }
                }
                iterator.remove();
                System.out.println("Booking cancelled!");
                return;
            }
        }
        System.out.println("Booking not found!");
    }

    private void saveBookingToFile(Booking b) {
        try (FileWriter fw = new FileWriter("bookings.txt", true)) {
            fw.write(b.bookingId + "," + b.userId + "," + b.roomId + "," + b.category + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void viewBookings() {
        for (Booking b : bookings) {
            System.out.println("BookingID: " + b.bookingId +
                               " UserID: " + b.userId +
                               " RoomID: " + b.roomId +
                               " Category: " + b.category +
                               " Paid: " + b.isPaid);
        }
    }
}