package com.hiro.terwoc;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MainApp {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        TerwocManager sistemTerwoc = new TerwocManager(15); // Kapasitas awal 15
        boolean aplikasiJalan = true;

        System.out.println("========================================");
        System.out.println("   SELAMAT DATANG DI SISTEM TERWOC");
        System.out.println("========================================");

        // DEMO DESERIALIZATION: Memuat data secara otomatis dari file biner .dat di awal program berjalan
        boolean berhasilMuat = sistemTerwoc.muatDataDariFile();

        // Jika file belum ada atau data masih kosong, inisialisasi minimal 3-5 data objek & 2 pemasok default
        if (!berhasilMuat || sistemTerwoc.getTotalBarang() == 0) {
            System.out.println("-> [INISIALISASI DATA AWAL] Menambahkan 5 data objek barang utama & 2 pemasok...");
            sistemTerwoc.tambahEntryBarang(new BarangElektronik("E01", "Laptop ASUS", 12000000, 5, 24));
            sistemTerwoc.tambahEntryBarang(new BarangSembako("S01", "Beras Mentik", 65000, 50, "Kg", "12-2025"));
            sistemTerwoc.tambahEntryBarang(new BarangPakaian("P01", "Kemeja Flanel", 150000, 20, "XL", "Katun"));
            sistemTerwoc.tambahEntryBarang(new BarangElektronik("E02", "Mouse Wireless", 250000, 15, 12));
            sistemTerwoc.tambahEntryBarang(new BarangPakaian("P02", "Jaket Parka", 320000, 10, "L", "Parasut"));

            sistemTerwoc.tambahPemasok(new Pemasok("SUP01", "CV Sumber Niaga", "0812-0000-0000"));
            sistemTerwoc.tambahPemasok(new Pemasok("SUP02", "PT Pangan Nusantara", "0856-1111-2222"));

            // Otomatis simpan data awal ke file biner
            sistemTerwoc.simpanDataKeFile();
        }

        while (aplikasiJalan) {
            System.out.println("\n--- MAIN MENU TERWOC ---");
            System.out.println("1. Entry Data Baru (Elektronik/Sembako/Pakaian)");
            System.out.println("2. Tampilkan Seluruh Data (Runtime Polymorphism & ArrayList)");
            System.out.println("3. Pencarian & Tambah Stok (HashMap Fast Lookup)");
            System.out.println("4. Order Keluar (Exception Handling: try-catch-finally)");
            System.out.println("5. Pengaturan Sistem (Ubah Kuota Kapasitas)");
            System.out.println("6. Simulasi Pengecekan Kualitas (Dynamic Binding via Parameter)");
            System.out.println("7. Simulasi Diskon (Interface & Implements)");
            System.out.println("8. Daftarkan Pemasok (Aggregation)");
            System.out.println("9. Tampilkan Semua Pemasok");
            System.out.println("10. Terima Stok dari Pemasok (Association)");
            System.out.println("11. Baca File Log Riwayat/Transaksi (File I/O Teks)");
            System.out.println("12. Simpan Data Objek ke File (Object Serialization)");
            System.out.println("13. Muat Data Objek dari File (Object Deserialization)");
            System.out.println("0. Simpan & Keluar");
            System.out.print("Pilih menu: ");

            int pilihan = -1;
            try {
                pilihan = input.nextInt();
                input.nextLine(); // Membersihkan buffer enter
            } catch (InputMismatchException e) {
                System.out.println("Error: Pilihan menu harus berupa angka bulat!");
                input.nextLine(); // Bersihkan input yang salah
                continue;
            }

            switch (pilihan) {
                case 1:
                    System.out.println("\n[ ENTRY BARANG BARU ]");
                    if (sistemTerwoc.getTotalBarang() >= sistemTerwoc.getKapasitasMaksimal()) {
                        System.out.println("Gudang Penuh! Silakan tambah kapasitas di menu Pengaturan.");
                        break;
                    }

                    try {
                        System.out.println("Pilih Jenis Barang:");
                        System.out.println("1. Barang Elektronik");
                        System.out.println("2. Barang Sembako");
                        System.out.println("3. Barang Pakaian");
                        System.out.print("Pilihan (1/2/3): ");
                        int jenis = input.nextInt();
                        input.nextLine();

                        System.out.print("Masukkan ID Barang : ");
                        String id = input.nextLine();
                        System.out.print("Masukkan Nama      : ");
                        String nama = input.nextLine();
                        System.out.print("Masukkan Harga     : ");
                        double harga = input.nextDouble();
                        System.out.print("Masukkan Stok      : ");
                        int stok = input.nextInt();
                        input.nextLine();

                        if (jenis == 1) {
                            System.out.print("Masukkan Garansi (Bulan): ");
                            int garansi = input.nextInt();
                            input.nextLine();
                            sistemTerwoc.tambahEntryBarang(new BarangElektronik(id, nama, harga, stok, garansi));
                        } else if (jenis == 2) {
                            System.out.print("Masukkan Satuan (Kg/L/Pcs): ");
                            String satuan = input.nextLine();
                            System.out.print("Masukkan Tgl Expired      : ");
                            String tglExpired = input.nextLine();
                            sistemTerwoc.tambahEntryBarang(new BarangSembako(id, nama, harga, stok, satuan, tglExpired));
                        } else if (jenis == 3) {
                            System.out.print("Masukkan Ukuran (S/M/L/XL): ");
                            String ukuran = input.nextLine();
                            System.out.print("Masukkan Bahan Kain       : ");
                            String bahan = input.nextLine();
                            sistemTerwoc.tambahEntryBarang(new BarangPakaian(id, nama, harga, stok, ukuran, bahan));
                        } else {
                            System.out.println("Jenis barang tidak valid.");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Error Input: Format harga/stok/garansi harus angka!");
                        input.nextLine();
                    }
                    break;

                case 2:
                    System.out.println("\n[ TRACKING GUDANG ]");
                    sistemTerwoc.tampilkanSemuaTracking();
                    break;

                case 3:
                    System.out.println("\n[ PENCARIAN & TAMBAH STOK KHUSUS ]");
                    System.out.print("Masukkan ID Barang yang dicari: ");
                    String cariId = input.nextLine();
                    Barang barangDitemukan = sistemTerwoc.cariBarang(cariId);

                    if (barangDitemukan != null) {
                        System.out.println("Barang ditemukan: " + barangDitemukan.getNamaBarang() + " (Kategori: " + barangDitemukan.getKategori() + ")");
                        try {
                            System.out.print("Jumlah stok yang ditambahkan: ");
                            int jmlStok = input.nextInt();
                            input.nextLine();

                            System.out.print("Apakah dari Supplier baru? (Y/N): ");
                            String jwb = input.nextLine();

                            // Compile-Time Polymorphism (Method Overloading)
                            if (jwb.equalsIgnoreCase("Y")) {
                                System.out.print("Masukkan Nama Supplier: ");
                                String supp = input.nextLine();
                                barangDitemukan.tambahStok(jmlStok, supp);
                                sistemTerwoc.catatLogTransaksi("TAMBAH STOK KHUSUS: " + barangDitemukan.getNamaBarang()
                                        + " (+" + jmlStok + " dari Supplier " + supp + "). Stok: " + barangDitemukan.getStok());
                            } else {
                                barangDitemukan.tambahStok(jmlStok);
                                System.out.println("Stok berhasil ditambah secara reguler.");
                                sistemTerwoc.catatLogTransaksi("TAMBAH STOK REGULER: " + barangDitemukan.getNamaBarang()
                                        + " (+" + jmlStok + "). Stok: " + barangDitemukan.getStok());
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Error: Jumlah stok harus berupa angka bulat!");
                            input.nextLine();
                        }
                    } else {
                        System.out.println("Barang tidak ditemukan.");
                    }
                    break;

                case 4:
                    // Penerapan Exception Handling: try, catch, finally
                    System.out.println("\n[ PROSES ORDER KELUAR ]");
                    System.out.print("Masukkan ID Barang : ");
                    String idOrder = input.nextLine();
                    System.out.print("Jumlah Dikeluarkan : ");

                    try {
                        int jumlah = input.nextInt();
                        input.nextLine();
                        // Memanggil method yang mendeklarasikan throws
                        sistemTerwoc.prosesOrderKeluar(idOrder, jumlah);
                    } catch (StokTidakCukupException e) {
                        // Menangkap Custom Exception saat stok tidak cukup
                        System.out.println("[PERINGATAN BISNIS] " + e.getMessage());
                    } catch (IllegalArgumentException e) {
                        // Menangkap kesalahan argumen (misal jumlah <= 0)
                        System.out.println("[ERROR INPUT] " + e.getMessage());
                    } catch (InputMismatchException e) {
                        // Menangkap kesalahan tipe data input scanner
                        System.out.println("[ERROR INPUT] Jumlah yang dikeluarkan harus berupa angka bulat!");
                        input.nextLine();
                    } catch (Exception e) {
                        // Menangkap exception umum (misal barang tidak ditemukan)
                        System.out.println("[ERROR SISTEM] " + e.getMessage());
                    } finally {
                        // Blok finally yang selalu dieksekusi dalam kondisi apapun
                        System.out.println("[LOG] Pemeriksaan order keluar selesai dijalankan.");
                    }
                    break;

                case 5:
                    try {
                        System.out.print("Masukkan kuota kapasitas baru: ");
                        int kapasitasBaru = input.nextInt();
                        input.nextLine();
                        sistemTerwoc.ubahKapasitas(kapasitasBaru);
                    } catch (InputMismatchException e) {
                        System.out.println("Error: Kapasitas harus berupa angka bulat!");
                        input.nextLine();
                    }
                    break;

                case 6:
                    System.out.println("\n[ SIMULASI PENGECEKAN KUALITAS ]");
                    System.out.print("Masukkan ID Barang untuk dicek: ");
                    String idCek = input.nextLine();
                    Barang barangDiCek = sistemTerwoc.cariBarang(idCek);

                    if (barangDiCek != null) {
                        sistemTerwoc.simulasiPengecekanKualitas(barangDiCek);
                    } else {
                        System.out.println("Barang dengan ID tersebut tidak ditemukan.");
                    }
                    break;

                case 7:
                    System.out.println("\n[ SIMULASI DISKON (INTERFACE & IMPLEMENTS) ]");
                    System.out.print("Masukkan ID Barang : ");
                    String idDiskon = input.nextLine();
                    try {
                        System.out.print("Masukkan Persentase Diskon (%): ");
                        double diskon = input.nextDouble();
                        input.nextLine();
                        sistemTerwoc.simulasiHitungDiskon(idDiskon, diskon);
                    } catch (InputMismatchException e) {
                        System.out.println("Error: Persentase diskon harus berupa angka!");
                        input.nextLine();
                    }
                    break;

                case 8:
                    System.out.println("\n[ PENDAFTARAN PEMASOK (AGGREGATION) ]");
                    System.out.print("Masukkan ID Pemasok : ");
                    String idPemasok = input.nextLine();
                    System.out.print("Masukkan Nama       : ");
                    String namaPemasok = input.nextLine();
                    System.out.print("Masukkan Kontak     : ");
                    String kontakPemasok = input.nextLine();
                    try {
                        sistemTerwoc.tambahPemasok(new Pemasok(idPemasok, namaPemasok, kontakPemasok));
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 9:
                    sistemTerwoc.lihatSemuaPemasok();
                    break;

                case 10:
                    System.out.println("\n[ PENERIMAAN STOK DARI PEMASOK (ASSOCIATION) ]");
                    System.out.print("Masukkan ID Pemasok : ");
                    String idPemasokTerima = input.nextLine();
                    System.out.print("Masukkan ID Barang  : ");
                    String idBarangTerima = input.nextLine();
                    try {
                        System.out.print("Jumlah diterima     : ");
                        int jumlahTerima = input.nextInt();
                        input.nextLine();
                        sistemTerwoc.prosesPenerimaanBarang(idPemasokTerima, idBarangTerima, jumlahTerima);
                    } catch (InputMismatchException e) {
                        System.out.println("Error: Jumlah penerimaan harus berupa angka bulat!");
                        input.nextLine();
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 11:
                    // DEMO FILE I/O TEKS (Membaca file log dengan File & Scanner)
                    sistemTerwoc.bacaLogTransaksi();
                    break;

                case 12:
                    // DEMO SERIALIZATION: Membekukan seluruh data objek ke file biner
                    sistemTerwoc.simpanDataKeFile();
                    break;

                case 13:
                    // DEMO DESERIALIZATION: Membangkitkan kembali data objek dari file biner
                    sistemTerwoc.muatDataDariFile();
                    break;

                case 0:
                    // DEMO SERIALIZATION SEBELUM KELUAR
                    System.out.println("\n[ MENYIMPAN SESI KE DATABASE INTERNAL... ]");
                    sistemTerwoc.simpanDataKeFile();
                    aplikasiJalan = false;
                    System.out.println("Terima kasih telah menggunakan sistem TERWOC. Program ditutup.");
                    break;

                default:
                    System.out.println("Menu tidak valid.");
            }
        }
        input.close();
    }
}
