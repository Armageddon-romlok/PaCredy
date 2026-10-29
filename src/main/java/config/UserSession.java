package config;

public class UserSession {
    private static String idPegawai;
    private static String username;
    private static String nama;
    private static String role;

    public static void setSession(String id, String uname, String name, String r) {
        idPegawai = id;
        username = uname;
        nama = name;
        role = r;
    }

    public static String getIdPegawai() { return idPegawai; }
    public static String getUsername() { return username; }
    public static String getNama() { return nama; }
    public static String getRole() { return role; }

    public static void clearSession() {
        idPegawai = null;
        username = null;
        nama = null;
        role = null;
    }
}