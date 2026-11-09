package com.hiro.terwoc;

// Layanan asosiasi: memproses interaksi antara pemasok dan barang yang sudah terdaftar
public class TransaksiPenerimaanBarang {
    public boolean prosesPenerimaan(Pemasok pemasok, Barang barang, int jumlah) {
        if (pemasok == null || barang == null) {
            throw new IllegalArgumentException("Pemasok dan barang wajib tersedia.");
        }
        if (jumlah <= 0) {
            throw new IllegalArgumentException("Jumlah penerimaan harus lebih besar dari 0.");
        }
        barang.tambahStok(jumlah, pemasok.getNamaPemasok());
        System.out.println("Penerimaan " + barang.getNamaBarang() + " dari " + pemasok.getNamaPemasok()
                + " berhasil. Stok sekarang: " + barang.getStok());
        return true;
    }
}
