package com.hiro.terwoc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

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

    // =========================================================================
    // FITUR FILE I/O TEKS DASAR (FileWriter, File, Scanner)
    // =========================================================================

    // Mencatat riwayat log ke file teks log_transaksi.txt menggunakan FileWriter (append mode)
    public void catatLogTransaksi(String pesan) {
        try {
            // Parameter 'true' pada FileWriter berarti mode append (menambah tanpa menimpa)
            FileWriter pipaTulis = new FileWriter("log_transaksi.txt", true);
            String waktu = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            pipaTulis.write("[" + waktu + "] " + pesan + "\n");
            pipaTulis.close(); // Wajib ditutup agar tersimpan
        } catch (IOException e) {
            System.out.println("Peringatan: Gagal menulis log transaksi ke file. " + e.getMessage());
        }
    }

    // Membaca dan menampilkan kembali isi file log teks menggunakan File dan Scanner
    public void bacaLogTransaksi() {
        System.out.println("\n--- RIWAYAT LOG TRANSAKSI / AKTIVITAS (FILE I/O TEKS: log_transaksi.txt) ---");
        try {
            File fileLog = new File("log_transaksi.txt");
            if (!fileLog.exists()) {
                System.out.println("-> Belum ada catatan riwayat transaksi (file 'log_transaksi.txt' belum ada).");
                return;
            }

            Scanner mesinBaca = new Scanner(fileLog);
            int barisKe = 1;
            boolean adaData = false;
            while (mesinBaca.hasNextLine()) {
                String barisTeks = mesinBaca.nextLine();
                System.out.println(barisKe + ". " + barisTeks);
                barisKe++;
                adaData = true;
            }
            if (!adaData) {
                System.out.println("-> File 'log_transaksi.txt' masih kosong.");
            }
            mesinBaca.close();
            System.out.println("-----------------------------------------------------------------------------");
        } catch (FileNotFoundException e) {
            System.out.println("Gagal: File log tidak ditemukan di komputer!");
        }
    }

    // =========================================================================
    // FITUR OBJECT SERIALIZATION & DESERIALIZATION (ObjectOutputStream & ObjectInputStream)
    // =========================================================================

    // Serialization: Membekukan seluruh kumpulan data objek ke dalam file biner data_terwoc.dat
    public void simpanDataKeFile() {
        System.out.println("\n[ SERIALIZATION: MENYIMPAN DATA OBJEK KE FILE ]");
        try {
            FileOutputStream fileKeluar = new FileOutputStream("data_terwoc.dat");
            ObjectOutputStream mesinPembeku = new ObjectOutputStream(fileKeluar);

            // Menyimpan struktur ArrayList dan HashMap sekaligus
            mesinPembeku.writeObject(this.daftarBarang);
            mesinPembeku.writeObject(this.mapPemasok);
            mesinPembeku.writeObject(this.ruangPenyimpanan);

            mesinPembeku.close();
            fileKeluar.close();
            System.out.println("-> [SYSTEM LOG] Seluruh data (" + this.daftarBarang.size() + " barang, "
                    + this.mapPemasok.size() + " pemasok) BERHASIL DISIMPAN (Serialization) ke 'data_terwoc.dat'!");

            catatLogTransaksi("SERIALIZATION: Menyimpan " + this.daftarBarang.size() + " barang dan "
                    + this.mapPemasok.size() + " pemasok ke file 'data_terwoc.dat'.");
        } catch (Exception e) {
            System.out.println("-> [SYSTEM ERROR] Gagal menyimpan objek: " + e.getMessage());
        }
    }

    // Deserialization: Membaca urutan biner dan merakit kembali menjadi objek utuh di memori
    @SuppressWarnings("unchecked")
    public boolean muatDataDariFile() {
        System.out.println("\n[ DESERIALIZATION: MEMUAT DATA OBJEK DARI FILE ]");
        try {
            File cekFile = new File("data_terwoc.dat");
            if (!cekFile.exists()) {
                System.out.println("-> [SYSTEM LOG] File 'data_terwoc.dat' belum ada (baru pertama kali dibuka).");
                return false;
            }

            FileInputStream fileMasuk = new FileInputStream(cekFile);
            ObjectInputStream mesinPembangkit = new ObjectInputStream(fileMasuk);

            // Membangkitkan (Deserialize) kembali objek ArrayList dan HashMap
            this.daftarBarang = (ArrayList<Barang>) mesinPembangkit.readObject();

            HashMap<String, Pemasok> loadedPemasok = (HashMap<String, Pemasok>) mesinPembangkit.readObject();
            this.mapPemasok.clear();
            if (loadedPemasok != null) {
                this.mapPemasok.putAll(loadedPemasok);
            }

            RuangPenyimpanan loadedRuang = (RuangPenyimpanan) mesinPembangkit.readObject();
            if (loadedRuang != null) {
                this.ruangPenyimpanan.ubahKapasitas(loadedRuang.getKapasitasMaksimal(), this.daftarBarang.size());
            }

            // Sinkronkan HashMap mapBarang untuk pencarian instan O(1)
            this.mapBarang.clear();
            for (Barang b : this.daftarBarang) {
                this.mapBarang.put(b.getIdBarang(), b);
            }

            // Kembalikan nilai static counter agar sinkron dengan total barang yang dimuat
            Barang.setTotalBarangDibuat(this.daftarBarang.size());

            mesinPembangkit.close();
            fileMasuk.close();
            System.out.println("-> [SYSTEM LOG] Data sistem BERHASIL DIMUAT (Deserialization)! Total: "
                    + this.daftarBarang.size() + " barang & " + this.mapPemasok.size() + " pemasok.");

            catatLogTransaksi("DESERIALIZATION: Berhasil memuat " + this.daftarBarang.size() + " barang dan "
                    + this.mapPemasok.size() + " pemasok dari 'data_terwoc.dat'.");
            return true;
        } catch (Exception e) {
            System.out.println("-> [SYSTEM ERROR] Gagal memuat data objek: " + e.getMessage());
            return false;
        }
    }

    // =========================================================================
    // LOGIKA OPERASIONAL GUDANG
    // =========================================================================

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

            // Catat log ke file teks
            catatLogTransaksi("ENTRY BARANG: " + barangBaru.getNamaBarang() + " (ID: "
                    + barangBaru.getIdBarang() + ", Kategori: " + barangBaru.getKategori()
                    + ", Stok Awal: " + barangBaru.getStok() + ", Harga: Rp" + String.format("%.0f", barangBaru.getHarga()) + ")");
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
        catatLogTransaksi("DAFTAR PEMASOK: " + pemasok.getNamaPemasok() + " (ID: " + pemasok.getIdPemasok() + ")");
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
        boolean berhasil = transaksiPenerimaanBarang.prosesPenerimaan(pemasok, barang, jumlah);
        if (berhasil) {
            catatLogTransaksi("PENERIMAAN STOK: " + barang.getNamaBarang() + " (ID: " + barang.getIdBarang()
                    + ") dari Pemasok '" + pemasok.getNamaPemasok() + "' sejumlah " + jumlah
                    + " pcs. Stok sekarang: " + barang.getStok());
        }
        return berhasil;
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
            throw new Exception("Barang dengan ID '" + id + "' tidak ditemukan!");
        }

        if (jumlahOrder <= 0) {
            throw new IllegalArgumentException("Jumlah order harus lebih besar dari 0!");
        }

        if (b.getStok() < jumlahOrder) {
            throw new StokTidakCukupException("Stok tidak mencukupi! Sisa stok " + b.getNamaBarang() + " hanya " + b.getStok() + ", diminta: " + jumlahOrder);
        }

        b.kurangiStok(jumlahOrder);
        System.out.println("Order sukses! " + b.getNamaBarang() + " sebanyak " + jumlahOrder + " berhasil dikeluarkan. Sisa stok: " + b.getStok());

        // Catat riwayat order keluar ke log teks
        catatLogTransaksi("ORDER KELUAR: " + b.getNamaBarang() + " (ID: " + b.getIdBarang() + ") sebanyak "
                + jumlahOrder + " pcs. Sisa stok: " + b.getStok());
    }

    // Method Pengaturan Kapasitas
    public boolean ubahKapasitas(int kapasitasBaru) {
        if (!ruangPenyimpanan.ubahKapasitas(kapasitasBaru, daftarBarang.size())) {
            System.out.println("Gagal! Kapasitas baru (" + kapasitasBaru + ") lebih kecil dari jumlah barang saat ini (" + daftarBarang.size() + ").");
            return false;
        }

        System.out.println("Sukses! Kapasitas gudang diperbarui menjadi " + ruangPenyimpanan.getKapasitasMaksimal() + " slot.");
        catatLogTransaksi("PENGATURAN KAPASITAS: Kuota diperbarui menjadi " + ruangPenyimpanan.getKapasitasMaksimal() + " slot.");
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
