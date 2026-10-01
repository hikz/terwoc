package com.hiro.terwoc;

import java.util.Scanner;

public class MainApp {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        TerwocManager sistemTerwoc = new TerwocManager(10); // Kapasitas 10
        boolean aplikasiJalan = true;

        sistemTerwoc.tambahEntryBarang(new BarangElektronik("E01", "Laptop ASUS", 12000000, 5, 24));
        sistemTerwoc.tambahEntryBarang(new BarangSembako("S01", "Beras Mentik", 65000, 50, "Kg", "12-2025"));
        sistemTerwoc.tambahEntryBarang(new BarangPakaian("P01", "Kemeja Flanel", 150000, 20, "XL", "Katun"));

        System.out.println("========================================");
        System.out.println("   SELAMAT DATANG DI SISTEM TERWOC");
        System.out.println("========================================");

        while (aplikasiJalan) {
            System.out.println("\n--- MAIN MENU TERWOC ---");
            System.out.println("1. Entry Data Baru (Elektronik/Sembako/Pakaian)");
            System.out.println("2. Tampilkan Seluruh Data (Runtime Polymorphism)");
            System.out.println("3. Pencarian & Tambah Stok (Compile-Time Polymorphism)");
            System.out.println("4. Order Keluar (Kurangi Stok)");
            System.out.println("5. Pengaturan Sistem (Ubah Kapasitas Array)");
            System.out.println("6. Simulasi Pengecekan Kualitas (Dynamic Binding via Parameter)");
            System.out.println("0. Keluar");
            System.out.print("Pilih menu: ");

            int pilihan = input.nextInt();
            input.nextLine(); // Membersihkan sisa enter

            switch (pilihan) {
                case 1:
                    System.out.println("\n[ ENTRY BARANG BARU ]");
                    if (sistemTerwoc.getTotalBarang() >= sistemTerwoc.getKapasitasMaksimal()) {
                        System.out.println("Gudang Penuh! Silakan tambah kapasitas di menu Pengaturan.");
                        break;
                    }

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
                        System.out.println("Barang ditemukan: " + barangDitemukan.getNamaBarang());
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
                        } else {
                            barangDitemukan.tambahStok(jmlStok);
                            System.out.println("Stok berhasil ditambah secara reguler.");
                        }
                    } else {
                        System.out.println("Barang tidak ditemukan.");
                    }
                    break;

                case 4:
                    System.out.print("Masukkan ID Barang : ");
                    String idOrder = input.nextLine();
                    System.out.print("Jumlah Dikeluarkan : ");
                    int jumlah = input.nextInt();
                    sistemTerwoc.prosesOrderKeluar(idOrder, jumlah);
                    break;

                case 5:
                    System.out.print("Masukkan kapasitas array baru: ");
                    int kapasitasBaru = input.nextInt();
                    sistemTerwoc.ubahKapasitas(kapasitasBaru);
                    break;

                case 6:
                    // FITUR BARU: Mendemonstrasikan Dynamic Binding lewat parameter method
                    System.out.println("\n[ SIMULASI PENGECEKAN KUALITAS ]");
                    System.out.print("Masukkan ID Barang untuk dicek: ");
                    String idCek = input.nextLine();
                    Barang barangDiCek = sistemTerwoc.cariBarang(idCek);

                    if (barangDiCek != null) {
                        // Mengirim objek dari berbagai subclass ke method berparameter Superclass
                        sistemTerwoc.simulasiPengecekanKualitas(barangDiCek);
                    } else {
                        System.out.println("Barang dengan ID tersebut tidak ditemukan.");
                    }
                    break;

                case 0:
                    aplikasiJalan = false;
                    System.out.println("Menutup sistem TERWOC.");
                    break;

                default:
                    System.out.println("Menu tidak valid.");
            }
        }
        input.close();
    }
}