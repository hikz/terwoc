package com.hiro.terwoc;

import java.util.ArrayList;
import java.util.HashMap;

public class TerwocManager {

    // Menggunakan Collection Framework: ArrayList dan HashMap
    private ArrayList<Barang> daftarBarang;
    private HashMap<String, Barang> mapBarang;
    private final RuangPenyimpanan ruangPenyimpanan;
    private final TransaksiPenerimaanBarang transaksiPenerimaanBarang;
    private final HashMap<String, Pemasok> mapPemasok;

    public TerwocManager(int kapasitas) {
        this.ruangPenyimpanan = new RuangPenyimpanan(kapasitas);
        this.transaksiPenerimaanBarang = new TransaksiPenerimaanBarang();
        this.daftarBarang = new ArrayList<>();
        this.mapBarang = new HashMap<>();
        this.mapPemasok = new HashMap<>();
    }

    // Method Entry Record
    public void tambahEntryBarang(Barang barangBaru) {
        // Validasi ID tidak boleh duplikat menggunakan HashMap (O(1))
        if (mapBarang.containsKey(barangBaru.getIdBarang())) {
            System.out.println("Gagal! Barang dengan ID '" + barangBaru.getIdBarang() + "' sudah terdaftar di gudang.");
            return;
        }

        if (ruangPenyimpanan.masihTersedia(daftarBarang.size())) {
            daftarBarang.add(barangBaru);
            mapBarang.put(barangBaru.getIdBarang(), barangBaru);
            System.out.println(barangBaru.getNamaBarang() + " berhasil masuk ke gudang.");
        } else {
            System.out.println("Gagal! Kapasitas gudang sudah penuh.");
        }
    }

    // Aggregation: Pemasok dibuat di luar manager, lalu didaftarkan di sini.
    public boolean tambahPemasok(Pemasok pemasok) {
        if (pemasok == null) {
            System.out.println("Gagal! Data pemasok tidak boleh kosong.");
            return false;
        }
        if (mapPemasok.containsKey(pemasok.getIdPemasok())) {
            System.out.println("Gagal! ID pemasok '" + pemasok.getIdPemasok() + "' sudah terdaftar.");
            return false;
        }
        mapPemasok.put(pemasok.getIdPemasok(), pemasok);
        System.out.println("Pemasok " + pemasok.getNamaPemasok() + " berhasil didaftarkan.");
        return true;
    }

    public Pemasok cariPemasok(String id) {
        return mapPemasok.get(id);
    }

    public void lihatSemuaPemasok() {
        System.out.println("\n===============================================");
        System.out.printf("| %-10s | %-24s | %-24s |%n", "ID", "NAMA PEMASOK", "KONTAK");
        System.out.println("===============================================");
        if (mapPemasok.isEmpty()) {
            System.out.println("Belum ada pemasok terdaftar.");
        } else {
            for (Pemasok pemasok : mapPemasok.values()) pemasok.tampilkanInfo();
        }
        System.out.println("===============================================");
    }

    public boolean prosesPenerimaanBarang(String idPemasok, String idBarang, int jumlah) {
        Pemasok pemasok = cariPemasok(idPemasok);
        Barang barang = cariBarang(idBarang);
        if (pemasok == null) {
            System.out.println("Pemasok dengan ID '" + idPemasok + "' tidak ditemukan.");
            return false;
        }
        if (barang == null) {
            System.out.println("Barang dengan ID '" + idBarang + "' tidak ditemukan.");
            return false;
        }
        return transaksiPenerimaanBarang.prosesPenerimaan(pemasok, barang, jumlah);
    }

    // Method Tracking
    public void tampilkanSemuaTracking() {
        System.out.println("\n===============================================================================");
        System.out.printf("| %-8s | %-15s | %-12s | %-7s | %-17s |%n", "ID BARANG", "NAMA BARANG", "HARGA", "STOK", "INFO KHUSUS");
        System.out.println("===============================================================================");

        if (daftarBarang.isEmpty()) {
            System.out.printf("| %-73s |%n", "Gudang masih kosong. Silakan entry data terlebih dahulu.");
        } else {
            // Iterasi menggunakan ArrayList
            for (Barang b : daftarBarang) {
                b.tampilkanInfo();
            }
        }
        System.out.println("===============================================================================");
    }

    // Method Order Control menggunakan Exception Handling (throw & throws)
    public void prosesOrderKeluar(String id, int jumlahOrder) throws StokTidakCukupException, Exception {
        Barang b = mapBarang.get(id);

        if (b == null) {
            // Melempar checked exception umum jika ID tidak ada
            throw new Exception("Barang dengan ID '" + id + "' tidak ditemukan!");
        }

        if (jumlahOrder <= 0) {
            // Melempar unchecked exception jika input jumlah tidak valid
            throw new IllegalArgumentException("Jumlah order harus lebih besar dari 0!");
        }

        if (b.getStok() < jumlahOrder) {
            // Melempar custom checked exception jika stok kurang
            throw new StokTidakCukupException("Stok tidak mencukupi! Sisa stok " + b.getNamaBarang() + " hanya " + b.getStok() + ", diminta: " + jumlahOrder);
        }

        b.kurangiStok(jumlahOrder);
        System.out.println("Order sukses! " + b.getNamaBarang() + " sebanyak " + jumlahOrder + " berhasil dikeluarkan. Sisa stok: " + b.getStok());
    }

    // Method Pengaturan Kapasitas
    public boolean ubahKapasitas(int kapasitasBaru) {
        if (!ruangPenyimpanan.ubahKapasitas(kapasitasBaru, daftarBarang.size())) {
            System.out.println("Gagal! Kapasitas baru (" + kapasitasBaru + ") lebih kecil dari jumlah barang saat ini (" + daftarBarang.size() + ").");
            return false;
        }

        System.out.println("Sukses! Kapasitas gudang diperbarui menjadi " + ruangPenyimpanan.getKapasitasMaksimal() + " slot.");
        return true;
    }

    // Method Pencarian Data cepat dengan HashMap
    public Barang cariBarang(String id) {
        return mapBarang.get(id);
    }

    public void simulasiPengecekanKualitas(Barang b) {
        System.out.println("\n--- MEMULAI SIMULASI PENGECEKAN KUALITAS ---");
        System.out.println("Menganalisis barang dengan ID: " + b.getIdBarang());
        b.tampilkanInfo();

        System.out.println("Status: Pengecekan Selesai. Barang memenuhi standar gudang TERWOC.");
        System.out.println("--------------------------------------------");
    }

    public void simulasiHitungDiskon(String id, double persentase) {
        Barang b = cariBarang(id);
        if (b == null) {
            System.out.println("Barang dengan ID '" + id + "' tidak ditemukan!");
            return;
        }

        System.out.println("\n--- SIMULASI HITUNG DISKON (INTERFACE DISKONABLE) ---");
        System.out.println("ID Barang    : " + b.getIdBarang());
        System.out.println("Nama Barang  : " + b.getNamaBarang());
        System.out.println("Kategori     : " + b.getKategori());
        System.out.printf("Harga Normal : Rp%.0f%n", b.getHarga());

        // Memeriksa apakah barang mengimplementasikan interface Diskonable
        if (b instanceof Diskonable) {
            Diskonable barangDiskon = (Diskonable) b;
            double hargaSetelahDiskon = barangDiskon.hitungHargaDiskon(persentase);
            System.out.printf("Diskon Promo : %.1f%%%n", persentase);
            System.out.printf("Harga Promo  : Rp%.0f%n", hargaSetelahDiskon);
        } else {
            System.out.println("Status Promo : Barang kategori '" + b.getKategori() + "' TIDAK mendukung diskon promo.");
        }
        System.out.println("-----------------------------------------------------");
    }

    public int getKapasitasMaksimal() {
        return ruangPenyimpanan.getKapasitasMaksimal();
    }

    public int getTotalBarang() {
        return daftarBarang.size();
    }
}
