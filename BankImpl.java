import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class BankImpl extends UnicastRemoteObject implements Bank {

    // Simulasi data akun dan saldo
    private HashMap<String, Double> accounts;
    // Simulasi catatan mutasi rekening
    private HashMap<String, List<String>> transactionHistory;

    public BankImpl() throws RemoteException {
        super();
        accounts = new HashMap<>();
        transactionHistory = new HashMap<>();

        // Menambahkan beberapa akun untuk simulasi
        accounts.put("ACC123", 5000.00);
        accounts.put("ACC456", 3000.00);
        accounts.put("ACC789", 7000.00);

        // Menambahkan catatan mutasi untuk setiap akun
        transactionHistory.put("ACC123", new ArrayList<>());
        transactionHistory.put("ACC456", new ArrayList<>());
        transactionHistory.put("ACC789", new ArrayList<>());
    }

    // Mengecek saldo
    public double checkBalance(String accountNumber) throws RemoteException {
        if (accounts.containsKey(accountNumber)) {
            return accounts.get(accountNumber);
        } else {
            throw new RemoteException("Account not found.");
        }
    }

    // Transfer dana antar akun
    public String transferFunds(String fromAccount, String toAccount, double amount) throws RemoteException {
        if (!accounts.containsKey(fromAccount) || !accounts.containsKey(toAccount)) {
            return "One or both accounts not found.";
        }

        if (accounts.get(fromAccount) < amount) {
            return "Insufficient funds in the account.";
        }

        // Proses transfer
        accounts.put(fromAccount, accounts.get(fromAccount) - amount);
        accounts.put(toAccount, accounts.get(toAccount) + amount);

        // Mencatat mutasi transaksi
        transactionHistory.get(fromAccount).add("Transferred $" + amount + " to " + toAccount);
        transactionHistory.get(toAccount).add("Received $" + amount + " from " + fromAccount);

        return "Transfer of $" + amount + " from " + fromAccount + " to " + toAccount + " successful.";
    }

    // Pembayaran tagihan
    public String payBill(String accountNumber, double amount) throws RemoteException {
        if (!accounts.containsKey(accountNumber)) {
            return "Account not found.";
        }

        if (accounts.get(accountNumber) < amount) {
            return "Insufficient funds to pay the bill.";
        }

        // Proses pembayaran
        accounts.put(accountNumber, accounts.get(accountNumber) - amount);

        // Mencatat mutasi pembayaran
        transactionHistory.get(accountNumber).add("Paid bill of $" + amount);

        return "Bill payment of $" + amount + " from account " + accountNumber + " successful.";
    }

    // Mendapatkan mutasi rekening
    public List<String> getTransactionHistory(String accountNumber) throws RemoteException {
        if (!transactionHistory.containsKey(accountNumber)) {
            throw new RemoteException("Account not found.");
        }

        return transactionHistory.get(accountNumber);
    }
}
