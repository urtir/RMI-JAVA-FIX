import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Bank extends Remote {
    double checkBalance(String accountNumber) throws RemoteException;
    String transferFunds(String fromAccount, String toAccount, double amount) throws RemoteException;
    String payBill(String accountNumber, double amount) throws RemoteException;
    List<String> getTransactionHistory(String accountNumber) throws RemoteException;
}
