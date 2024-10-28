# Proyek Bank RMI

Proyek ini menyediakan aplikasi perbankan sederhana yang diimplementasikan menggunakan Java RMI (Remote Method Invocation). Aplikasi ini memungkinkan klien untuk melakukan autentikasi, memeriksa saldo, mentransfer dana, dan mengakses riwayat transaksi melalui metode jarak jauh. README ini mencakup detail pengaturan, deskripsi file Java utama, dan instruksi untuk menjalankan proyek.

## Struktur Proyek

### File Java Utama

1. **Bank.java**Mendefinisikan antarmuka `Bank`, yang mendeklarasikan metode-metode yang dapat dipanggil secara jarak jauh, termasuk:

   - `authenticate(String accountNumber, String password)`
   - `checkBalance(String accountNumber)`
   - `transferFunds(String fromAccount, String toAccount, double amount)`
   - `payBill(String accountNumber, double amount)`
   - `getTransactionHistory(String accountNumber)`
2. **BankClient.java**Aplikasi klien yang berinteraksi dengan layanan bank jarak jauh. Menghubungkan ke server dan melakukan hal-hal berikut:

   - Meminta informasi kredensial akun pengguna.
   - Menggunakan `rmi://localhost:1098/BankService` untuk menemukan dan memanggil metode pada layanan bank jarak jauh.
3. **BankImpl.java**Mengimplementasikan antarmuka `Bank`. Fitur utama:

   - Mengelola koneksi basis data untuk mengautentikasi pengguna, memeriksa saldo, dan menangani transaksi.
   - Memuat konfigurasi basis data dari file properti dan memulai koneksi.
4. **BankServer.java**Aplikasi server utama yang menjalankan layanan `Bank`. Langkah-langkahnya termasuk:

   - Memulai registry RMI pada port 1098.
   - Mendaftarkan instance `BankImpl` dengan nama `BankService`.

## Instruksi Pengaturan

### Prasyarat

1. **Java Development Kit (JDK) 8+**Pastikan JDK terinstal dan variabel lingkungan `JAVA_HOME` sudah diatur.
2. **MySQL Connector/J**Tambahkan driver JDBC MySQL ke classpath proyek untuk konektivitas basis data.
3. **RMI Registry**
   Server harus memiliki port yang terbuka (default adalah `1098`) untuk registry RMI.

### Menjalankan Proyek

1. **Mulai Server RMI**

   ```bash
   javac Bank.java BankImpl.java BankServer.java
   java BankServer
   ```
2. **Mulai Klien**

   ```bash
   javac BankClient.java
   java BankClient
   ```

## Konfigurasi Tambahan

Pastikan konfigurasi basis data (misalnya, kredensial pengguna, URL basis data) sudah diatur dengan benar di file properti yang dibaca oleh `BankImpl.java`. File ini harus ditempatkan di direktori yang sama dengan file server atau di lokasi yang ditentukan dalam kode.
