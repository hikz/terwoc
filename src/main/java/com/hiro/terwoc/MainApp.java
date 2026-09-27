package com.hiro.terwoc;

import java.util.Scanner;

public class MainApp {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // Inisialisasi awal TERWOC dengan kapasitas array 5
        TerwocManager sistemTerwoc = new TerwocManager(5);
        boolean aplikasiJalan = true;

        System.out.println("========================================");
        System.out.println("   SELAMAT DATANG DI SISTEM TERWOC");
        System.out.println("========================================");

        while (aplikasiJalan) {
            System.out.println("\n--- MAIN MENU TERWOC ---");
            System.out.println("1. Entry Data Baru ");
            System.out.println("2. Tampilkan Seluruh Data ");
            System.out.println("3. Pencarian & Tambah Stok ");
            System.out.println("4. Order Keluar ");
            System.out.println("5. Pengaturan Sistem ");
            System.out.println("0. Keluar");
            System.out.print("Pilih menu: ");

            int pilihan = input.nextInt();
            input.nextLine(); // Membersihkan sisa enter/newline

            switch (pilihan) {
                case 1:
                    System.out.println("\n[ ENTRY BARANG BARU ]");
                    // Validasi kapasitas array
                    if (sistemTerwoc.getTotalBarang() >= sistemTerwoc.getKapasitasMaksimal()) {
                        System.out.println("Gudang Penuh! Silakan tambah kapasitas di menu Pengaturan (Menu 5).");
                        break;
                    }

                    System.out.println("Pilih Jenis Barang:");
                    System.out.println("1. Barang Elektronik");
                    System.out.println("2. Barang Sembako");
                    System.out.print("Pilihan (1/2): ");
                    int jenis = input.nextInt();
                    input.nextLine(); // Membersihkan sisa enter

                    System.out.print("Masukkan ID Barang : ");
                    String id = input.nextLine();
                    System.out.print("Masukkan Nama      : ");
                    String nama = input.nextLine();
                    System.out.print("Masukkan Harga     : ");
                    double harga = input.nextDouble();
                    System.out.print("Masukkan Stok      : ");
                    int stok = input.nextInt();
                    input.nextLine(); // Membersihkan sisa enter

                    if (jenis == 1) {
                        System.out.print("Masukkan Garansi (Bulan): ");
                        int garansi = input.nextInt();
                        input.nextLine();

                        BarangElektronik barangElektronik = new BarangElektronik(id, nama, harga, stok, garansi);
                        sistemTerwoc.tambahEntryBarang(barangElektronik);

                    } else if (jenis == 2) {
                        System.out.print("Masukkan Satuan (Kg/L/Pcs) : ");
                        String satuan = input.nextLine();
                        System.out.print("Masukkan Tanggal Expired   : ");
                        String tglExpired = input.nextLine();

                        BarangSembako barangSembako = new BarangSembako(id, nama, harga, stok, satuan, tglExpired);
                        sistemTerwoc.tambahEntryBarang(barangSembako);

                    } else {
                        System.out.println("Jenis barang tidak valid. Batal menambahkan barang.");
                    }
                    break;

                case 2:
                    System.out.println("\n[ TRACKING GUDANG ]");
                    sistemTerwoc.tampilkanSemuaTracking();
                    System.out.println("Total Objek Barang yang pernah dibuat: " + Barang.getTotalBarangDibuat());
                    break;

                case 3:
                    System.out.println("\n[ PENCARIAN & TAMBAH STOK KHUSUS ]");
                    System.out.print("Masukkan ID Barang yang dicari: ");
                    String cariId = input.nextLine();

                    Barang barangDitemukan = sistemTerwoc.cariBarang(cariId);

                    if (barangDitemukan != null) {
                        System.out.println("Barang ditemukan: " + barangDitemukan.getNamaBarang());
                        System.out.print("Masukkan jumlah stok yang ditambahkan: ");
                        int jmlStok = input.nextInt();
                        input.nextLine(); // Bersihkan enter

                        System.out.print("Apakah barang ini dari Supplier baru? (Y/N): ");
                        String jwb = input.nextLine();

                        if (jwb.equalsIgnoreCase("Y")) {
                            System.out.print("Masukkan Nama Supplier: ");
                            String supp = input.nextLine();
                            // Memanggil Method OVERLOADING (2 Parameter)
                            barangDitemukan.tambahStok(jmlStok, supp);
                        } else {
                            // Memanggil Method OVERLOADING (1 Parameter)
                            barangDitemukan.tambahStok(jmlStok);
                            System.out.println("Stok berhasil ditambah secara reguler.");
                        }
                    } else {
                        System.out.println("Barang dengan ID '" + cariId + "' tidak ditemukan di gudang.");
                    }
                    break;

                case 4:
                    System.out.println("\n[ ORDER BARANG KELUAR ]");
                    System.out.print("Masukkan ID Barang : ");
                    String idOrder = input.nextLine();
                    System.out.print("Jumlah Dikeluarkan : ");
                    int jumlah = input.nextInt();
                    sistemTerwoc.prosesOrderKeluar(idOrder, jumlah);
                    break;

                case 5:
                    System.out.println("\n[ PENGATURAN SISTEM TERWOC ]");
                    System.out.println("Kapasitas Array saat ini : " + sistemTerwoc.getKapasitasMaksimal() + " slot");
                    System.out.println("Terpakai                 : " + sistemTerwoc.getTotalBarang() + " slot");
                    System.out.print("Masukkan kapasitas array baru: ");
                    int kapasitasBaru = input.nextInt();
                    sistemTerwoc.ubahKapasitas(kapasitasBaru);
                    break;

                case 0:
                    aplikasiJalan = false;
                    System.out.println("Menutup sistem TERWOC. Sampai jumpa!");
                    break;

                default:
                    System.out.println("Menu tidak valid. Silakan pilih 0-5.");
            }
        }

        input.close();
    }
}