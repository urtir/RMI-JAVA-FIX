import java.rmi.Naming;
import java.util.List;

public class BankClient {
    public static void main(String[] args) {
        try {
            // Mengakses layanan bank remote
            Bank bank = (Bank) Naming.lookup("rmi://localhost/BankService");

            // Mengecek saldo akun
            System.out.println("Checking balance for ACC123:");
            double balance = bank.checkBalance("ACC123");
            System.out.println("Balance: $" + balance);

            // Melakukan transfer dana
            System.out.println("\nTransferring $1000 from ACC123 to ACC456:");
            String transferResult = bank.transferFunds("ACC123", "ACC456", 1000.00);
            System.out.println(transferResult);

            // Melakukan pembayaran tagihan
            System.out.println("\nPaying bill of $500 from ACC123:");
            String billPaymentResult = bank.payBill("ACC123", 500.00);
            System.out.println(billPaymentResult);

            // Mengecek mutasi rekening untuk ACC123
            System.out.println("\nTransaction history for ACC123:");
            List<String> history = bank.getTransactionHistory("ACC123");
            for (String transaction : history) {
                System.out.println(transaction);
            }

        } catch (Exception e) {
            System.out.println("Bank Client failed: " + e);
        }
    }
}
