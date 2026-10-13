package com.hiro.terwoc;

public class BarangPakaian extends Barang {
    private String ukuran;
    private String bahan;

    public BarangPakaian(String idBarang, String namaBarang, double harga, int stok, String ukuran, String bahan) {
        super(idBarang, namaBarang, harga, stok); // Memanggil constructor Superclass
        this.ukuran = ukuran;
        this.bahan = bahan;
    }

    // Method Overriding untuk Runtime Polymorphism
    @Override
    public void tampilkanInfo() {
        System.out.printf("| %-9s | %-15s | Rp%-10.0f | %-7d | Size: %-4s, %-4s |%n",
                super.getIdBarang(), super.getNamaBarang(), super.getHarga(), super.getStok(), this.ukuran, this.bahan);
    }
}