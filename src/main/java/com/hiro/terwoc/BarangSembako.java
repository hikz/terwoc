package com.hiro.terwoc;

public class BarangSembako extends Barang {
    private String satuan;
    private String tglKedaluwarsa;

    public BarangSembako(String idBarang, String namaBarang, double harga, int stok, String satuan, String tglKedaluwarsa) {
        super(idBarang, namaBarang, harga, stok);

        this.setSatuan(satuan);
        this.setTglKedaluwarsa(tglKedaluwarsa);
    }

    public String getSatuan() {
        return this.satuan;
    }

    public void setSatuan(String satuan) {
        if (satuan != null && !satuan.trim().isEmpty()) {
            this.satuan = satuan;
        } else {
            System.out.println("Error: Satuan barang tidak boleh kosong!");
            this.satuan = "Tidak diketahui"; // Nilai default jika gagal validasi
        }
    }

    public String getTglKedaluwarsa() {
        return this.tglKedaluwarsa;
    }

    public void setTglKedaluwarsa(String tglKedaluwarsa) {
        if (tglKedaluwarsa != null && !tglKedaluwarsa.trim().isEmpty()) {
            this.tglKedaluwarsa = tglKedaluwarsa;
        } else {
            System.out.println("Error: Tanggal kedaluwarsa tidak boleh kosong!");
            this.tglKedaluwarsa = "Belum Diatur";
        }
    }

    @Override
    public void tampilkanInfo() {
        String stokSatuan = super.getStok() + " " + this.satuan;
        System.out.printf("| %-8s | %-15s | Rp%-10.0f | Stok: %-4s | Expired: %-8s |\n",
                super.getIdBarang(), super.getNamaBarang(), super.getHarga(), stokSatuan, this.tglKedaluwarsa);
    }
}