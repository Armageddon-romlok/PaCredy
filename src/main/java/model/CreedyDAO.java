package model;

import config.Koneksi;
import java.sql.*;

public class CreedyDAO {

    // 1. Validasi Login
    public Karyawan validasiLogin(String userOrNip, String password) {
        String sql = "SELECT * FROM pegawai WHERE (username = ? OR nip = ?) AND password = ? AND status = 'AKTIF'";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userOrNip);
            ps.setString(2, userOrNip);
            ps.setString(3, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Karyawan(
                        rs.getString("id_pegawai"),
                        rs.getString("nip"),
                        rs.getString("nama"),
                        rs.getString("username"),
                        rs.getString("role"),
                        rs.getString("status")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error Login DAO: " + e.getMessage());
        }
        return null;
    }

    // 2. Tampil Semua Data Pegawai
    public ResultSet getAllPegawai() throws SQLException {
        Connection conn = Koneksi.getConnection();
        Statement st = conn.createStatement();
        return st.executeQuery("SELECT nip, nama, username, role, status FROM pegawai");
    }

    // 3. Tambah Pegawai
    public boolean tambahPegawai(String nip, String nama, String username, String password, String role) {
        String sql = "INSERT INTO pegawai (nip, nama, username, password, role, status) VALUES (?, ?, ?, ?, ?, 'AKTIF')";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nip);
            ps.setString(2, nama);
            ps.setString(3, username);
            ps.setString(4, password);
            ps.setString(5, role);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error Tambah Pegawai: " + e.getMessage());
            return false;
        }
    }

    // 4. Ubah Pegawai
    public boolean ubahPegawai(String nip, String nama, String role) {
        String sql = "UPDATE pegawai SET nama = ?, role = ? WHERE nip = ?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nama);
            ps.setString(2, role);
            ps.setString(3, nip);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error Ubah Pegawai: " + e.getMessage());
            return false;
        }
    }

    // 5. Hapus Pegawai (Permanen)
    public boolean hapusPegawai(String nip) {
        String sql = "DELETE FROM pegawai WHERE nip = ?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nip);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error Hapus Pegawai: " + e.getMessage());
            return false;
        }
    }

    // 6. Nonaktifkan Pegawai
    public boolean nonaktifkanPegawai(String nip) {
        String sql = "UPDATE pegawai SET status = 'NONAKTIF' WHERE nip = ?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nip);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error Nonaktifkan Pegawai: " + e.getMessage());
            return false;
        }
    }

    // 7. Transaksi Pegawai / Teller (Setor & Tarik)
    public boolean prosesTransaksi(String noRek, String idPegawai, String jenis, double nominal) {
        String sqlTx = "INSERT INTO transaksi (id_rekening, id_pegawai, jenis_transaksi, nominal, tanggal_transaksi) VALUES (?, ?, ?, ?, NOW())";
        String sqlSaldo = jenis.equalsIgnoreCase("SETOR") 
            ? "UPDATE rekening SET saldo = saldo + ? WHERE id_rekening = ?" 
            : "UPDATE rekening SET saldo = saldo - ? WHERE id_rekening = ?";

        Connection conn = Koneksi.getConnection();
        try {
            conn.setAutoCommit(false);

            try (PreparedStatement psTx = conn.prepareStatement(sqlTx)) {
                psTx.setString(1, noRek);
                psTx.setString(2, idPegawai);
                psTx.setString(3, jenis);
                psTx.setDouble(4, nominal);
                psTx.executeUpdate();
            }

            try (PreparedStatement psSaldo = conn.prepareStatement(sqlSaldo)) {
                psSaldo.setDouble(1, nominal);
                psSaldo.setString(2, noRek);
                psSaldo.executeUpdate();
            }

            conn.commit();
            conn.setAutoCommit(true);
            return true;
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) {}
            System.err.println("Error Transaksi: " + e.getMessage());
            return false;
        }
    }

    // 8. Histori Transaksi
    public ResultSet getHistoriTransaksi() throws SQLException {
        Connection conn = Koneksi.getConnection();
        Statement st = conn.createStatement();
        return st.executeQuery("SELECT id_transaksi, id_rekening, jenis_transaksi, nominal, tanggal_transaksi FROM transaksi ORDER BY tanggal_transaksi DESC");
    }
}