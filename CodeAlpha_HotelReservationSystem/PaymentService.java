class PaymentService {
    public boolean processPayment(double amount) {
        System.out.println("Processing payment of ₹" + amount + "...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {}
        System.out.println("Payment Successful!");
        return true;
    }
}