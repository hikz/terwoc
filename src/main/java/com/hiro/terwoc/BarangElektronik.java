package com.hiro.terwoc;

public class BarangElektronik extends Barang {
    private int lamaGaransiBulan;

    public BarangElektronik(String id, String nama, double harga, int stok, int garansi) {
        super(id, nama, harga, stok);
        this.lamaGaransiBulan = garansi;
    }

    public int getLamaGaransiBulan() { return this.lamaGaransiBulan; }

    public void setLamaGaransiBulan(int garansi) {
        if(garansi >= 0) {
            this.lamaGaransiBulan = garansi;
        }
    }

    @Override
    public void tampilkanInfo() {
        System.out.printf("| %-8s | %-15s | Rp%-10.0f | Stok: %-4d | Garansi: %d Bulan |\n",
                super.getIdBarang(), super.getNamaBarang(), super.getHarga(), super.getStok(), this.lamaGaransiBulan);
    }
}