import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

class Employee implements Serializable {
    String id;
    String name;
    int leaveBalance;

    public Employee(String id, String name, int leaveBalance) {
        this.id = id;
        this.name = name;
        this.leaveBalance = leaveBalance;
    }
}

class LeaveApplication implements Serializable {
    String empId;
    String reason;
    String status; // Pending, Approved, Rejected

    public LeaveApplication(String empId, String reason) {
        this.empId = empId;
        this.reason = reason;
        this.status = "Pending";
    }
}

class DataStore {
    static final String EMP_FILE = "employees.dat";
    static final String LEAVE_FILE = "leaves.dat";

    static void saveEmployees(ArrayList<Employee> list) throws Exception {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(EMP_FILE));
        oos.writeObject(list);
        oos.close();
    }

    static ArrayList<Employee> loadEmployees() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(EMP_FILE));
            return (ArrayList<Employee>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    static void saveLeaves(ArrayList<LeaveApplication> list) throws Exception {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(LEAVE_FILE));
        oos.writeObject(list);
        oos.close();
    }

    static ArrayList<LeaveApplication> loadLeaves() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(LEAVE_FILE));
            return (ArrayList<LeaveApplication>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}

public class LeaveManagementSystem extends JFrame {

    ArrayList<Employee> employees;
    ArrayList<LeaveApplication> leaves;

    JTextField empIdField, nameField, reasonField;
    JTextArea outputArea;

    public LeaveManagementSystem() {
        employees = DataStore.loadEmployees();
        leaves = DataStore.loadLeaves();

        setTitle("Employee Leave Management System");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(0, 2));

        empIdField = new JTextField();
        nameField = new JTextField();
        reasonField = new JTextField();

        panel.add(new JLabel("Employee ID:"));
        panel.add(empIdField);
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Leave Reason:"));
        panel.add(reasonField);

        JButton addEmpBtn = new JButton("Add Employee");
        JButton applyLeaveBtn = new JButton("Apply Leave");
        JButton viewBalanceBtn = new JButton("View Balance");
        JButton viewLeavesBtn = new JButton("View Leaves");
        JButton approveBtn = new JButton("Approve Leave");
        JButton rejectBtn = new JButton("Reject Leave");

        panel.add(addEmpBtn);
        panel.add(applyLeaveBtn);
        panel.add(viewBalanceBtn);
        panel.add(viewLeavesBtn);
        panel.add(approveBtn);
        panel.add(rejectBtn);

        outputArea = new JTextArea();
        outputArea.setEditable(false);

        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        // Actions
        addEmpBtn.addActionListener(e -> addEmployee());
        applyLeaveBtn.addActionListener(e -> applyLeave());
        viewBalanceBtn.addActionListener(e -> viewBalance());
        viewLeavesBtn.addActionListener(e -> viewLeaves());
        approveBtn.addActionListener(e -> updateLeave("Approved"));
        rejectBtn.addActionListener(e -> updateLeave("Rejected"));

        setVisible(true);
    }

    void addEmployee() {
        String id = empIdField.getText();
        String name = nameField.getText();

        employees.add(new Employee(id, name, 10));
        saveAll();
        outputArea.setText("Employee added with 10 leaves.\n");
    }

    void applyLeave() {
        String id = empIdField.getText();
        String reason = reasonField.getText();

        leaves.add(new LeaveApplication(id, reason));
        saveAll();
        outputArea.setText("Leave applied (Pending).\n");
    }

    void viewBalance() {
        String id = empIdField.getText();
        for (Employee e : employees) {
            if (e.id.equals(id)) {
                outputArea.setText("Leave Balance: " + e.leaveBalance);
                return;
            }
        }
        outputArea.setText("Employee not found");
    }

    void viewLeaves() {
        String id = empIdField.getText();
        StringBuilder sb = new StringBuilder();

        for (LeaveApplication l : leaves) {
            if (l.empId.equals(id)) {
                sb.append("Reason: ").append(l.reason)
                  .append(", Status: ").append(l.status).append("\n");
            }
        }

        outputArea.setText(sb.toString());
    }

    void updateLeave(String status) {
        String id = empIdField.getText();

        for (LeaveApplication l : leaves) {
            if (l.empId.equals(id) && l.status.equals("Pending")) {
                l.status = status;

                if (status.equals("Approved")) {
                    for (Employee e : employees) {
                        if (e.id.equals(id)) {
                            e.leaveBalance--;
                        }
                    }
                }
                saveAll();
                outputArea.setText("Leave " + status);
                return;
            }
        }
        outputArea.setText("No pending leave found");
    }

    void saveAll() {
        try {
            DataStore.saveEmployees(employees);
            DataStore.saveLeaves(leaves);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new LeaveManagementSystem();
    }
}
