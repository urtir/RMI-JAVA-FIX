import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class BankServer {
    public static void main(String[] args) {
        try {
            // Membuat instance BankImpl
            BankImpl bank = new BankImpl();

            // Membuat registry pada port 1098
            LocateRegistry.createRegistry(1098);

            // Mendaftarkan objek remote ke RMI registry
            Naming.rebind("rmi://localhost:1098/BankService", bank);

            System.out.println("Bank Server is ready.");
        } catch (Exception e) {
            System.out.println("Bank Server failed: " + e);
        }
    }
}