import java.util.*;

// -------------------- Room Classes --------------------

abstract class Room {

    protected String roomType;
    protected int beds;
    protected int size;
    protected double price;

    public Room(String roomType, int beds, int size, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    public String getRoomType() {
        return roomType;
    }

    public void displayRoom() {
        System.out.println(roomType + ":");
        System.out.println("Beds: " + beds);
        System.out.println("Size: " + size + " sqft");
        System.out.println("Price per night: " + price);
    }
}

class SingleRoom extends Room {
    public SingleRoom() {
        super("Single", 1, 250, 1500.0);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double", 2, 400, 2500.0);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite", 3, 750, 5000.0);
    }
}

// -------------------- Room Inventory --------------------

class RoomInventory {

    private Map<String, Integer> roomAvailability;

    public RoomInventory() {
        roomAvailability = new HashMap<>();
        initializeInventory();
    }

    private void initializeInventory() {
        roomAvailability.put("Single", 5);
        roomAvailability.put("Double", 3);
        roomAvailability.put("Suite", 2);
    }

    public Map<String, Integer> getRoomAvailability() {
        return roomAvailability;
    }

    public void updateAvailability(String roomType, int count) {
        roomAvailability.put(roomType, count);
    }
}

// -------------------- Reservation --------------------

class Reservation {

    /** Name of the guest making the booking */
    private String guestName;

    /** Requested room type */
    private String roomType;

    /**
     * Creates a new booking request
     */
    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// -------------------- Booking Queue --------------------

class BookingRequestQueue {

    /** Queue storing booking requests */
    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    /** Add booking request */
    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
    }

    /** Get next booking request */
    public Reservation getNextRequest() {
        return requestQueue.poll();
    }

    /** Check if queue has requests */
    public boolean hasPendingRequests() {
        return !requestQueue.isEmpty();
    }
}

// -------------------- Main Application --------------------

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Booking Request Queue\n");

        // Initialize booking queue
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Create booking requests
        Reservation r1 = new Reservation("Abhi", "Single");
        Reservation r2 = new Reservation("Subha", "Double");
        Reservation r3 = new Reservation("Vanmathi", "Suite");

        // Add requests to queue
        bookingQueue.addRequest(r1);
        bookingQueue.addRequest(r2);
        bookingQueue.addRequest(r3);

        // Process booking requests in FIFO order
        while (bookingQueue.hasPendingRequests()) {

            Reservation request = bookingQueue.getNextRequest();

            System.out.println("Processing Booking:");
            System.out.println("Guest Name: " + request.getGuestName());
            System.out.println("Requested Room: " + request.getRoomType());
            System.out.println("---------------------------");
        }
    }
}

class RoomAllocationService {

    private Set<String> allocatedRoomIds;

    private Map<String, Set<String>> assignedRoomsByType;

    public RoomAllocationService() {
        allocatedRoomIds = new HashSet<>();
        assignedRoomsByType = new HashMap<>();
    }

    public void allocateRoom(Reservation reservation, RoomInventory inventory) {

        String roomType = reservation.getRoomType();
        Map<String, Integer> availability = inventory.getRoomAvailability();

        if (availability.getOrDefault(roomType, 0) > 0) {

            String roomId = generateRoomId(roomType);

            allocatedRoomIds.add(roomId);

            assignedRoomsByType
                    .computeIfAbsent(roomType, k -> new HashSet<>())
                    .add(roomId);

            inventory.updateAvailability(roomType,
                    availability.get(roomType) - 1);

            System.out.println("Booking Confirmed!");
            System.out.println("Guest: " + reservation.getGuestName());
            System.out.println("Room Type: " + roomType);
            System.out.println("Assigned Room ID: " + roomId);
            System.out.println("-----------------------------");

        } else {
            System.out.println("No available rooms for " + roomType +
                    " for guest " + reservation.getGuestName());
        }
    }

    private String generateRoomId(String roomType) {

        String prefix = roomType.substring(0, 1).toUpperCase();

        String roomId;

        do {
            int number = new Random().nextInt(900) + 100;
            roomId = prefix + number;
        } while (allocatedRoomIds.contains(roomId));

        return roomId;
    }
}
