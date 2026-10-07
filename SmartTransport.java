import java.util.*;

public class SmartTransport {

    // ================= STUDENT CLASS =================
    static class Student {
        private int studentId;
        private String name;
        private int stopId;
        private boolean present;
        private boolean authorized;
        private boolean feePaid;
        private boolean boarded;

        Student(int studentId, String name, int stopId,
                boolean present, boolean authorized, boolean feePaid) {

            this.studentId = studentId;
            this.name = name;
            this.stopId = stopId;
            this.present = present;
            this.authorized = authorized;
            this.feePaid = feePaid;
            this.boarded = false;
        }

        int getStudentId() {
            return studentId;
        }

        String getName() {
            return name;
        }

        int getStopId() {
            return stopId;
        }

        boolean isPresent() {
            return present;
        }

        boolean isAuthorized() {
            return authorized;
        }

        boolean isFeePaid() {
            return feePaid;
        }

        boolean isBoarded() {
            return boarded;
        }

        void setBoarded(boolean boarded) {
            this.boarded = boarded;
        }

        void display() {
            System.out.println(
                studentId + " | " +
                name + " | Stop " +
                stopId + " | Present: " +
                present + " | Authorized: " +
                authorized + " | Fee Paid: " +
                feePaid + " | Boarded: " +
                boarded
            );
        }
    }

    // ================= BUS CLASS =================
    static class Bus {
        protected String busId;
        protected String busType;
        protected int capacity;
        protected boolean available;

        protected ArrayList<Student> boardedStudents;

        Bus(String busId, String busType, int capacity) {
            this.busId = busId;
            this.busType = busType;
            this.capacity = capacity;
            this.available = true;
            this.boardedStudents = new ArrayList<>();
        }

        boolean hasSpace() {
            return boardedStudents.size() < capacity;
        }

        void boardStudent(Student student) {
            if (hasSpace()) {
                boardedStudents.add(student);
                student.setBoarded(true);
            }
        }

        void breakdown() {
            available = false;
            System.out.println(busId + " has broken down.");
        }

        void displayBus() {
            System.out.println(
                busId + " | " +
                busType + " | Capacity: " +
                capacity + " | Students: " +
                boardedStudents.size() + " | Available: " +
                available
            );
        }
    }

    // ================= INHERITANCE =================
    static class MiniBus extends Bus {

        MiniBus(String busId) {
            super(busId, "Mini Bus", 15);
        }
    }

    static class StandardBus extends Bus {

        StandardBus(String busId) {
            super(busId, "Standard Bus", 25);
        }
    }

    // ================= STOP CLASS =================
    static class Stop {
        private int stopId;
        private String stopName;

        ArrayDeque<Student> waitingQueue;

        Stop(int stopId, String stopName) {
            this.stopId = stopId;
            this.stopName = stopName;
            waitingQueue = new ArrayDeque<>();
        }

        void addStudent(Student student) {
            waitingQueue.add(student);
        }

        void displayStop() {
            System.out.println(
                "Stop " + stopId +
                " - " + stopName +
                " | Waiting Students: " +
                waitingQueue.size()
            );
        }
    }

    // ================= GRAPH =================
    static class Edge {
        int destination;
        int weight;

        Edge(int destination, int weight) {
            this.destination = destination;
            this.weight = weight;
        }
    }

    static class Node implements Comparable<Node> {
        int vertex;
        int distance;

        Node(int vertex, int distance) {
            this.vertex = vertex;
            this.distance = distance;
        }

        public int compareTo(Node other) {
            return Integer.compare(this.distance, other.distance);
        }
    }

    static class Graph {
        ArrayList<ArrayList<Edge>> adjacencyList;

        Graph(int vertices) {
            adjacencyList = new ArrayList<>();

            for (int i = 0; i < vertices; i++) {
                adjacencyList.add(new ArrayList<>());
            }
        }

        void addEdge(int source, int destination, int weight) {
            adjacencyList.get(source)
                         .add(new Edge(destination, weight));

            adjacencyList.get(destination)
                         .add(new Edge(source, weight));
        }

        void dijkstra(int source) {

            int vertices = adjacencyList.size();

            int[] distance = new int[vertices];

            Arrays.fill(distance, Integer.MAX_VALUE);

            PriorityQueue<Node> priorityQueue =
                new PriorityQueue<>();

            distance[source] = 0;

            priorityQueue.add(
                new Node(source, 0)
            );

            while (!priorityQueue.isEmpty()) {

                Node current = priorityQueue.poll();

                int currentVertex = current.vertex;

                for (Edge edge :
                     adjacencyList.get(currentVertex)) {

                    int newDistance =
                        distance[currentVertex] + edge.weight;

                    if (newDistance <
                        distance[edge.destination]) {

                        distance[edge.destination] =
                            newDistance;

                        priorityQueue.add(
                            new Node(
                                edge.destination,
                                newDistance
                            )
                        );
                    }
                }
            }

            System.out.println(
                "\nShortest Distance from Campus:"
            );

            for (int i = 0; i < vertices; i++) {

                if (distance[i] == Integer.MAX_VALUE) {
                    System.out.println(
                        "Stop " + i + " : Not reachable"
                    );
                } else {
                    System.out.println(
                        "Stop " + i +
                        " : " + distance[i] +
                        " km"
                    );
                }
            }
        }
    }

    // ================= DATA STRUCTURES =================

    static ArrayList<Student> students =
        new ArrayList<>();

    static HashMap<Integer, Stop> stops =
        new HashMap<>();

    static ArrayList<Bus> buses =
        new ArrayList<>();

    static HashSet<Integer> allocatedStudents =
        new HashSet<>();

    static ArrayList<String> events =
        new ArrayList<>();

    static Graph graph;

    // ================= CREATE STUDENTS =================

    static void createStudents() {

        for (int i = 1; i <= 40; i++) {

            int stopId = ((i - 1) % 10) + 1;

            boolean present = (i % 5 != 0);

            boolean authorized = (i % 7 != 0);

            boolean feePaid = (i % 6 != 0);

            Student student = new Student(
                i,
                "Student" + i,
                stopId,
                present,
                authorized,
                feePaid
            );

            students.add(student);
        }

        System.out.println("40 students created.");
    }

    // ================= CREATE STOPS =================

    static void createStops() {

        for (int i = 1; i <= 10; i++) {

            Stop stop =
                new Stop(i, "Pickup Point " + i);

            stops.put(i, stop);
        }

        System.out.println("10 pickup stops created.");
    }

    // ================= CREATE BUSES =================

    static void createBuses() {

        buses.add(new MiniBus("B1"));
        buses.add(new MiniBus("B2"));
        buses.add(new StandardBus("B3"));

        System.out.println("3 buses created.");
    }

    // ================= WAITING QUEUE =================

    static void prepareWaitingQueues() {

        for (Student student : students) {

            if (student.isPresent()
                && student.isAuthorized()) {

                Stop stop =
                    stops.get(student.getStopId());

                stop.addStudent(student);
            }
        }

        System.out.println(
            "Waiting queues prepared."
        );
    }

    // ================= DISPLAY STUDENTS =================

    static void showStudents() {

        System.out.println(
            "\n========== STUDENTS =========="
        );

        for (Student student : students) {
            student.display();
        }
    }

    // ================= DISPLAY STOPS =================

    static void showStops() {

        System.out.println(
            "\n========== PICKUP STOPS =========="
        );

        for (Stop stop : stops.values()) {
            stop.displayStop();
        }
    }

    // ================= DISPLAY BUSES =================

    static void showBuses() {

        System.out.println(
            "\n========== BUSES =========="
        );

        for (Bus bus : buses) {
            bus.displayBus();
        }
    }

    // ================= FEE REPORT =================

    static void showFeeReport() {

        System.out.println(
            "\n========== FEE REPORT =========="
        );

        for (Student student : students) {

            if (!student.isFeePaid()) {

                System.out.println(
                    "Student " +
                    student.getStudentId() +
                    " - " +
                    student.getName() +
                    " : FEE PENDING"
                );
            }
        }
    }

    // ================= DAILY TRIP =================

    static void buildDailyTrip() {

        System.out.println(
            "\n========== DAILY TRIP =========="
        );

        events.add("Daily trip started.");

        Bus bus = buses.get(0);

        for (int stopId = 1; stopId <= 10; stopId++) {

            Stop stop = stops.get(stopId);

            if (stop.waitingQueue.isEmpty()) {

                System.out.println(
                    "Stop " + stopId +
                    " : Empty - Bus skipped."
                );

                events.add(
                    "Stop " + stopId +
                    " skipped because it is empty."
                );

                continue;
            }

            System.out.println(
                "\nVisiting Stop " + stopId
            );

            while (!stop.waitingQueue.isEmpty()
                   && bus.hasSpace()) {

                Student student =
                    stop.waitingQueue.poll();

                bus.boardStudent(student);

                allocatedStudents.add(
                    student.getStudentId()
                );

                System.out.println(
                    student.getName() +
                    " boarded " +
                    bus.busId
                );
            }

            if (!bus.hasSpace()) {

                System.out.println(
                    bus.busId +
                    " reached full capacity."
                );

                events.add(
                    bus.busId +
                    " reached capacity."
                );

                break;
            }
        }

        System.out.println(
            "\nDaily trip completed."
        );
    }

    // ================= UNALLOCATED STUDENTS =================

    static void showUnallocatedStudents() {

        System.out.println(
            "\n========== UNALLOCATED STUDENTS =========="
        );

        boolean found = false;

        for (Student student : students) {

            if (student.isPresent()
                && student.isAuthorized()
                && !student.isBoarded()) {

                System.out.println(
                    student.getStudentId() +
                    " - " +
                    student.getName() +
                    " - Stop " +
                    student.getStopId()
                );

                found = true;
            }
        }

        if (!found) {
            System.out.println(
                "No unallocated students."
            );
        }
    }

    // ================= BREAKDOWN =================

    static void simulateBreakdown() {

        System.out.println(
            "\n========== BUS BREAKDOWN =========="
        );

        Bus bus = buses.get(0);

        bus.breakdown();

        events.add(
            bus.busId +
            " breakdown occurred."
        );

        System.out.println(
            "Students already boarded remain recorded."
        );

        System.out.println(
            "Waiting students can be reassigned."
        );
    }

    // ================= REASSIGN =================

    static void reassignWaitingStudents() {

        System.out.println(
            "\n========== REASSIGN WAITING STUDENTS =========="
        );

        for (Bus bus : buses) {

            if (!bus.available) {
                continue;
            }

            while (bus.hasSpace()) {

                Student waitingStudent = null;
                Stop studentStop = null;

                for (Stop stop : stops.values()) {

                    if (!stop.waitingQueue.isEmpty()) {

                        waitingStudent =
                            stop.waitingQueue.poll();

                        studentStop = stop;

                        break;
                    }
                }

                if (waitingStudent == null) {
                    break;
                }

                bus.boardStudent(waitingStudent);

                allocatedStudents.add(
                    waitingStudent.getStudentId()
                );

                System.out.println(
                    waitingStudent.getName() +
                    " reassigned to " +
                    bus.busId
                );
            }
        }

        events.add(
            "Waiting students reassigned."
        );
    }

    // ================= SHORTEST ROUTE =================

    static void showShortestRoutes() {

        System.out.println(
            "\n========== SHORTEST ROUTES =========="
        );

        graph.dijkstra(0);
    }

    // ================= TEST EVENTS =================

    static void showEvents() {

        System.out.println(
            "\n========== TEST EVENTS =========="
        );

        if (events.isEmpty()) {

            System.out.println(
                "No events recorded yet."
            );

        } else {

            for (String event : events) {
                System.out.println(
                    "- " + event
                );
            }
        }
    }

    // ================= GRAPH CREATION =================

    static void createGraph() {

        graph = new Graph(11);

        graph.addEdge(0, 1, 5);
        graph.addEdge(0, 2, 8);
        graph.addEdge(1, 3, 6);
        graph.addEdge(2, 4, 4);
        graph.addEdge(3, 5, 7);
        graph.addEdge(4, 6, 5);
        graph.addEdge(5, 7, 3);
        graph.addEdge(6, 8, 6);
        graph.addEdge(7, 9, 4);
        graph.addEdge(8, 10, 5);

        System.out.println(
            "Transport route graph created."
        );
    }

    // ================= MENU =================

    static void menu() {

        Scanner scanner =
            new Scanner(System.in);

        while (true) {

            System.out.println(
                "\n======================================"
            );

            System.out.println(
                " SMART TRANSPORT MANAGEMENT SYSTEM"
            );

            System.out.println(
                "======================================"
            );

            System.out.println(
                "1. Show Students"
            );

            System.out.println(
                "2. Show Pickup Stops"
            );

            System.out.println(
                "3. Show Buses"
            );

            System.out.println(
                "4. Show Fee Report"
            );

            System.out.println(
                "5. Build Daily Trip"
            );

            System.out.println(
                "6. Show Shortest Routes"
            );

            System.out.println(
                "7. Simulate Bus Breakdown"
            );

            System.out.println(
                "8. Reassign Waiting Students"
            );

            System.out.println(
                "9. Show Unallocated Students"
            );

            System.out.println(
                "10. Show Test Events"
            );

            System.out.println(
                "0. Exit"
            );

            System.out.print(
                "Enter your choice: "
            );

            int choice = scanner.nextInt();

            switch (choice) {

                case 1:
                    showStudents();
                    break;

                case 2:
                    showStops();
                    break;

                case 3:
                    showBuses();
                    break;

                case 4:
                    showFeeReport();
                    break;

                case 5:
                    buildDailyTrip();
                    break;

                case 6:
                    showShortestRoutes();
                    break;

                case 7:
                    simulateBreakdown();
                    break;

                case 8:
                    reassignWaitingStudents();
                    break;

                case 9:
                    showUnallocatedStudents();
                    break;

                case 10:
                    showEvents();
                    break;

                case 0:
                    System.out.println(
                        "Project ended."
                    );

                    scanner.close();
                    return;

                default:
                    System.out.println(
                        "Invalid choice."
                    );
            }
        }
    }

    // ================= MAIN =================

    public static void main(String[] args) {

        System.out.println(
            "======================================"
        );

        System.out.println(
            " SMART TRANSPORT MANAGEMENT SYSTEM"
        );

        System.out.println(
            "======================================"
        );

        createStudents();
        createStops();
        createBuses();
        prepareWaitingQueues();
        createGraph();

        System.out.println(
            "\nSystem initialized successfully."
        );

        menu();
    }
}