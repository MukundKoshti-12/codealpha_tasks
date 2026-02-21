
import java.util.*;
import java.io.*;

class Student implements Serializable {
    String name;
    ArrayList<Integer> grades;

    Student(String name) {
        this.name = name;
        this.grades = new ArrayList<>();
    }

    void addGrade(int grade) {
        grades.add(grade);
    }

    double getAverage() {
        if (grades.isEmpty()) return 0;
        int sum = 0;
        for (int g : grades) sum += g;
        return (double) sum / grades.size();
    }

    int getHighest() {
        return grades.isEmpty() ? 0 : Collections.max(grades);
    }

    int getLowest() {
        return grades.isEmpty() ? 0 : Collections.min(grades);
    }

    void displayReport() {
        System.out.println("Name: " + name);
        System.out.println("Grades: " + grades);
        System.out.printf("Average: %.2f\n", getAverage());
        System.out.println("Highest: " + getHighest());
        System.out.println("Lowest: " + getLowest());
        System.out.println("----------------------");
    }
}

public class StudentGradeManager {
    static Scanner sc = new Scanner(System.in);
    static ArrayList<Student> students = new ArrayList<>();
    static final String FILE_NAME = "students.dat";

    public static void main(String[] args) {
        loadFromFile();
        int choice;

        do {
            showMenu();
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> addStudent();
                case 2 -> addGrades();
                case 3 -> deleteStudent();
                case 4 -> displayAllReports();
                case 5 -> displayClassStatistics();
                case 6 -> saveToFile();
                case 7 -> System.out.println("Exiting program...");
                default -> System.out.println("Invalid choice!");
            }
        } while (choice != 7);
    }

    static void showMenu() {
        System.out.println("\n===== Student Grade Manager =====");
        System.out.println("1. Add Student");
        System.out.println("2. Add Grades");
        System.out.println("3. Delete Student");
        System.out.println("4. Display Summary Report");
        System.out.println("5. Overall Class Statistics");
        System.out.println("6. Save Data");
        System.out.println("7. Exit");
        System.out.print("Enter choice: ");
    }

    static void addStudent() {
        System.out.print("Enter student name: ");
        String name = sc.nextLine();
        students.add(new Student(name));
        System.out.println("Student added successfully!");
    }

    static void addGrades() {
        if (students.isEmpty()) {
            System.out.println("No students available!");
            return;
        }

        Student s = selectStudent();
        if (s == null) return;

        System.out.print("How many grades to add? ");
        int count = sc.nextInt();

        for (int i = 0; i < count; i++) {
            int grade;
            while (true) {
                System.out.print("Enter grade (0–100): ");
                grade = sc.nextInt();

                if (grade >= 0 && grade <= 100) break;
                System.out.println("Invalid grade. Must be between 0 and 100.");
            }
            s.addGrade(grade);
        }
        sc.nextLine();

        System.out.println("Grades added successfully!");
    }

    static void deleteStudent() {
        if (students.isEmpty()) {
            System.out.println("No students to delete!");
            return;
        }

        Student s = selectStudent();
        if (s == null) return;

        students.remove(s);
        System.out.println("Student deleted successfully!");
    }

    static Student selectStudent() {
        System.out.println("Select Student:");
        for (int i = 0; i < students.size(); i++) {
            System.out.println((i + 1) + ". " + students.get(i).name);
        }

        System.out.print("Enter choice: ");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice < 1 || choice > students.size()) {
            System.out.println("Invalid selection!");
            return null;
        }

        return students.get(choice - 1);
    }

    static void displayAllReports() {
        if (students.isEmpty()) {
            System.out.println("No students available!");
            return;
        }

        System.out.println("\n===== Summary Report =====");
        for (Student s : students) {
            s.displayReport();
        }
    }

    static void displayClassStatistics() {
        if (students.isEmpty()) {
            System.out.println("No students available!");
            return;
        }

        int totalGrades = 0, sum = 0;
        int highest = Integer.MIN_VALUE;
        int lowest = Integer.MAX_VALUE;

        for (Student s : students) {
            for (int g : s.grades) {
                sum += g;
                totalGrades++;
                highest = Math.max(highest, g);
                lowest = Math.min(lowest, g);
            }
        }

        if (totalGrades == 0) {
            System.out.println("No grades available!");
            return;
        }

        System.out.println("\n===== Class Statistics =====");
        System.out.printf("Class Average: %.2f\n", (double) sum / totalGrades);
        System.out.println("Highest Grade: " + highest);
        System.out.println("Lowest Grade: " + lowest);
    }

    static void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(students);
            System.out.println("Data saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    static void loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            students = (ArrayList<Student>) ois.readObject();
            System.out.println("Data loaded successfully!");
        } catch (Exception e) {
            System.out.println("No previous data found. Starting fresh.");
        }
    }
}
