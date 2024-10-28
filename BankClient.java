import java.rmi.Naming;
import java.util.List;
import java.util.Scanner;

public class BankClient {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Masukkan nomor akun: ");
            String accountNumber = scanner.nextLine();

            System.out.print("Masukkan password: ");
            String password = scanner.nextLine();

            Bank bank = (Bank) Naming.lookup("rmi://localhost:1098/BankService");

            if (!bank.authenticate(accountNumber, password)) {
                System.out.println("Autentikasi gagal.");
                return;
            }

            System.out.println("Autentikasi berhasil.");
            boolean running = true;

            while (running) {
                System.out.println("\nSelamat datang di Bank RIDU, silakan pilih akses Anda:");
                System.out.println("1. Transfer Dana");
                System.out.println("2. Bayar Tagihan");
                System.out.println("3. Cek Mutasi");
                System.out.println("4. Cek Saldo");
                System.out.println("0. Keluar");
                System.out.print("Pilihan Anda: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (choice) {
                    case 1:
                        System.out.print("Masukkan jumlah transfer: ");
                        double transferAmount = scanner.nextDouble();
                        scanner.nextLine(); // consume newline
                        System.out.print("Masukkan nomor akun tujuan: ");
                        String toAccount = scanner.nextLine();
                        String transferResult = bank.transferFunds(accountNumber, toAccount, transferAmount);
                        System.out.println(transferResult);
                        break;
                    case 2:
                        System.out.print("Masukkan jumlah tagihan: ");
                        double billAmount = scanner.nextDouble();
                        scanner.nextLine(); // consume newline
                        String billPaymentResult = bank.payBill(accountNumber, billAmount);
                        System.out.println(billPaymentResult);
                        break;
                    case 3:
                        System.out.println("\nRiwayat transaksi untuk " + accountNumber + ":");
                        List<String> history = bank.getTransactionHistory(accountNumber);
                        for (String transaction : history) {
                            System.out.println(transaction);
                        }
                        break;
                    case 4:
                        double balance = bank.checkBalance(accountNumber);
                        System.out.println("Saldo Anda: $" + balance);
                        break;
                    case 0:
                        running = false;
                        System.out.println("Terima kasih telah menggunakan layanan Bank RIDU.");
                        break;
                    default:
                        System.out.println("Pilihan tidak valid. Silakan coba lagi.");
                }
            }

        } catch (Exception e) {
            System.out.println("Bank Client gagal: " + e);
        }
    }
}