import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

public class BankImpl extends UnicastRemoteObject implements Bank {
    private Connection connection;

    public BankImpl() throws RemoteException {
        super();
        try {
            Properties props = new Properties();
            props.load(new FileInputStream("db.properties"));
            String url = props.getProperty("db.url");
            String username = props.getProperty("db.username");
            String password = props.getProperty("db.password");
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException | IOException e) {
            throw new RemoteException("Database connection failed", e);
        }
    }

    public boolean authenticate(String accountNumber, String password) throws RemoteException {
        try {
            String query = "SELECT * FROM accounts WHERE account_number = ? AND password = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, accountNumber);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RemoteException("Authentication failed", e);
        }
    }

    public double checkBalance(String accountNumber) throws RemoteException {
        try {
            String query = "SELECT balance FROM accounts WHERE account_number = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("balance");
            } else {
                throw new RemoteException("Account not found.");
            }
        } catch (SQLException e) {
            throw new RemoteException("Failed to check balance", e);
        }
    }

    public String transferFunds(String fromAccount, String toAccount, double amount) throws RemoteException {
        try {
            connection.setAutoCommit(false);

            String checkBalanceQuery = "SELECT balance FROM accounts WHERE account_number = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkBalanceQuery);
            checkStmt.setString(1, fromAccount);
            ResultSet rs = checkStmt.executeQuery();
            if (!rs.next() || rs.getDouble("balance") < amount) {
                connection.rollback();
                return "Saldo tidak mencukupi atau akun tidak ditemukan.";
            }

            String updateFromAccount = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
            PreparedStatement updateFromStmt = connection.prepareStatement(updateFromAccount);
            updateFromStmt.setDouble(1, amount);
            updateFromStmt.setString(2, fromAccount);
            updateFromStmt.executeUpdate();

            String updateToAccount = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
            PreparedStatement updateToStmt = connection.prepareStatement(updateToAccount);
            updateToStmt.setDouble(1, amount);
            updateToStmt.setString(2, toAccount);
            updateToStmt.executeUpdate();

            String insertTransaction = "INSERT INTO transactions (account_number, description) VALUES (?, ?)";
            PreparedStatement insertStmt = connection.prepareStatement(insertTransaction);
            insertStmt.setString(1, fromAccount);
            insertStmt.setString(2, "Transfer sebesar $" + amount + " ke " + toAccount);
            insertStmt.executeUpdate();

            insertStmt.setString(1, toAccount);
            insertStmt.setString(2, "Menerima transfer sebesar $" + amount + " dari " + fromAccount);
            insertStmt.executeUpdate();

            connection.commit();
            return "Transfer berhasil.";
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RemoteException("Gagal melakukan rollback transaksi", ex);
            }
            throw new RemoteException("Transfer gagal", e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RemoteException("Gagal mengatur ulang auto-commit", e);
            }
        }
    }

    public String payBill(String accountNumber, double amount) throws RemoteException {
        try {
            String checkBalanceQuery = "SELECT balance FROM accounts WHERE account_number = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkBalanceQuery);
            checkStmt.setString(1, accountNumber);
            ResultSet rs = checkStmt.executeQuery();
            if (!rs.next() || rs.getDouble("balance") < amount) {
                return "Saldo tidak mencukupi atau akun tidak ditemukan.";
            }

            String updateAccount = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
            PreparedStatement updateStmt = connection.prepareStatement(updateAccount);
            updateStmt.setDouble(1, amount);
            updateStmt.setString(2, accountNumber);
            updateStmt.executeUpdate();

            String insertTransaction = "INSERT INTO transactions (account_number, description) VALUES (?, ?)";
            PreparedStatement insertStmt = connection.prepareStatement(insertTransaction);
            insertStmt.setString(1, accountNumber);
            insertStmt.setString(2, "Pembayaran tagihan sebesar $" + amount);
            insertStmt.executeUpdate();

            return "Pembayaran tagihan berhasil.";
        } catch (SQLException e) {
            throw new RemoteException("Pembayaran tagihan gagal", e);
        }
    }

    public List<String> getTransactionHistory(String accountNumber) throws RemoteException {
        try {
            String query = "SELECT description FROM transactions WHERE account_number = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();
            List<String> history = new ArrayList<>();
            while (rs.next()) {
                history.add(rs.getString("description"));
            }
            return history;
        } catch (SQLException e) {
            throw new RemoteException("Gagal mendapatkan riwayat transaksi", e);
        }
    }
}