package com.hiro.terwoc;

public abstract class Barang {
    private String idBarang;
    private String namaBarang;
    private double harga;
    private int stok;

    private static int totalBarangDibuat = 0;

    public Barang(String idBarang, String namaBarang, double harga, int stok) {
        this.idBarang = idBarang;
        this.namaBarang = namaBarang;
        this.setHarga(harga);
        this.setStok(stok);

        totalBarangDibuat++;
    }

    public String getIdBarang() { return this.idBarang; }
    public String getNamaBarang() { return this.namaBarang; }
    public double getHarga() { return this.harga; }
    public int getStok() { return this.stok; }

    public void setHarga(double harga) {
        if (harga >= 0) {
            this.harga = harga;
        } else {
            System.out.println("Error: Harga tidak boleh bernilai negatif!");
        }
    }

    public void setStok(int stok) {
        if (stok >= 0) {
            this.stok = stok;
        } else {
            System.out.println("Error: Stok tidak boleh bernilai negatif!");
        }
    }

    public static int getTotalBarangDibuat() {
        return totalBarangDibuat;
    }

    public void tambahStok(int jumlah) {
        if (jumlah > 0) this.stok += jumlah;
    }

    public void tambahStok(int jumlah, String namaSupplier) {
        if (jumlah > 0) {
            this.stok += jumlah;
            System.out.println("Stok bertambah. (Barang dari Supplier: " + namaSupplier + ")");
        }
    }

    public boolean kurangiStok(int jumlah){
        if(this.stok >= jumlah){
            this.stok -= jumlah;
            return true; // Berhasil
        } else {
            return false; // Gagal;
        }
    }

    // Method abstract untuk dioverride oleh subclass
    public abstract void tampilkanInfo();
}





