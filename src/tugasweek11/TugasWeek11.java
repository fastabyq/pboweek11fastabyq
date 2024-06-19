/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package tugasweek11;
import java.sql.*;
import java.util.Scanner;

/**
 *
 * @author User
 */
public class TugasWeek11 {

    /**
     * @param args the command line arguments
     */
      static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
      static final String DB_URL = "jdbc:mysql://127.0.0.1/week11";
      static final String USER = "root";
      static final String PASS = "";
      
      static Connection conn;
      static Statement stmt;
      static ResultSet rs;
      
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Menu:");
            System.out.println("1. Tambah Buku");
            System.out.println("2. Tampilkan Buku");
            System.out.println("3. Ubah Buku");
            System.out.println("4. Hapus Buku");
            System.out.println("5. Keluar");
            System.out.print("Pilih menu: ");
            int pilihan = scanner.nextInt();
            scanner.nextLine(); 
            
            switch (pilihan) {
                case 1:
                    tambahBuku(scanner);
                    break;
                case 2:
                    tampilkanBuku();
                    break;
                case 3:
                    System.out.print("Masukkan ID Buku yang ingin diubah: ");
                    int idUbah = scanner.nextInt();
                    scanner.nextLine(); // Konsumsi newline
                    ubahBuku(scanner, idUbah);
                    break;
                case 4:
                    System.out.print("Masukkan ID Buku yang ingin dihapus: ");
                    int idHapus = scanner.nextInt();
                    scanner.nextLine(); // Konsumsi newline
                    hapusBuku(idHapus);
                    break;
                case 5:
                    System.out.println("Keluar program...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Pilihan tidak tersedia, coba lagi.");
            }
        }
    }

    public static void tambahBuku(Scanner scanner) {
        System.out.print("Masukkan judul buku: ");
        String judul = scanner.nextLine();
        System.out.print("Masukkan tahun terbit: ");
        int tahunTerbit = scanner.nextInt();
        System.out.print("Masukkan jumlah stok: ");
        int stok = scanner.nextInt();
        System.out.print("Masukkan ID penulis: ");
        int idPenulis = scanner.nextInt();
        
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "INSERT INTO books (title, publication_year, stock, author_id) VALUES (?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, judul);
            ps.setInt(2, tahunTerbit);
            ps.setInt(3, stok);
            ps.setInt(4, idPenulis);
            ps.execute();
            ps.close();
            conn.close();
            System.out.println("Buku berhasil ditambahkan!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void tampilkanBuku() {
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM books");
            int i = 1;
            while (rs.next()) {
                System.out.println("Buku #" + i);
                System.out.println("ID           : " + rs.getInt("id"));
                System.out.println("Judul        : " + rs.getString("title"));
                System.out.println("Tahun Terbit : " + rs.getInt("publication_year"));
                System.out.println("Stok         : " + rs.getInt("stock"));
                System.out.println("ID Penulis   : " + rs.getInt("author_id"));
                System.out.println();
                i++;
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void ubahBuku(Scanner scanner, int id) {
        System.out.print("Masukkan judul buku baru: ");
        String judul = scanner.nextLine();
        System.out.print("Masukkan tahun terbit baru: ");
        int tahunTerbit = scanner.nextInt();
        System.out.print("Masukkan jumlah stok baru: ");
        int stok = scanner.nextInt();
        System.out.print("Masukkan ID penulis baru: ");
        int idPenulis = scanner.nextInt();
        
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String cekSql = "SELECT COUNT(*) FROM books WHERE id = ?";
            PreparedStatement cekPs = conn.prepareStatement(cekSql);
            cekPs.setInt(1, id);
            ResultSet cekRs = cekPs.executeQuery();
            cekRs.next();
            if (cekRs.getInt(1) == 0) {
                System.out.println("Buku dengan ID " + id + " tidak ditemukan!");
                cekRs.close();
                cekPs.close();
                conn.close();
                return;
            }
            cekRs.close();
            cekPs.close();
            
            String sql = "UPDATE books SET title = ?, publication_year = ?, stock = ?, author_id = ? WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, judul);
            ps.setInt(2, tahunTerbit);
            ps.setInt(3, stok);
            ps.setInt(4, idPenulis);
            ps.setInt(5, id);
            ps.execute();
            ps.close();
            conn.close();
            System.out.println("Buku berhasil diubah!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hapusBuku(int id) {
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String cekSql = "SELECT COUNT(*) FROM books WHERE id = ?";
            PreparedStatement cekPs = conn.prepareStatement(cekSql);
            cekPs.setInt(1, id);
            ResultSet cekRs = cekPs.executeQuery();
            cekRs.next();
            if (cekRs.getInt(1) == 0) {
                System.out.println("Buku dengan ID " + id + " tidak ditemukan!");
                cekRs.close();
                cekPs.close();
                conn.close();
                return;
            }
            cekRs.close();
            cekPs.close();
            
            String sql = "DELETE FROM books WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();
            ps.close();
            conn.close();
            System.out.println("Buku berhasil dihapus!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
