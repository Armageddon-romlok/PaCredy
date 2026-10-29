package model;

public abstract class Pengguna {
    protected String idPegawai;
    protected String nip;
    protected String nama;
    protected String username;
    protected String role;
    protected String status;

    public Pengguna(String idPegawai, String nip, String nama, String username, String role, String status) {
        this.idPegawai = idPegawai;
        this.nip = nip;
        this.nama = nama;
        this.username = username;
        this.role = role;
        this.status = status;
    }

    public String getIdPegawai() { return idPegawai; }
    public String getNip() { return nip; }
    public String getNama() { return nama; }
    public String getUsername() { return username; }
    public String getRole() { return role; }
    public String getStatus() { return status; }
}