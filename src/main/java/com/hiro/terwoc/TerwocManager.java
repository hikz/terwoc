package com.hiro.terwoc;

public class TerwocManager {

    private Barang[] gudang;
    private int totalBarang;
    private int kapasitasMaksimal;

    public TerwocManager(int kapasitas){
        this.kapasitasMaksimal = kapasitas;
        this.gudang = new Barang[kapasitasMaksimal];
        this.totalBarang = 0;

    }

    // Method Entry Record
    public void tambahEntryBarang(Barang barangBaru) {
        // Validasi ID tidak boleh duplikat
        for (int i = 0; i < totalBarang; i++) {
            if (gudang[i].getIdBarang().equals(barangBaru.getIdBarang())) {
                System.out.println("Gagal! Barang dengan ID '" + barangBaru.getIdBarang() + "' sudah terdaftar di gudang.");
                return;
            }
        }

        if (totalBarang < kapasitasMaksimal) {
            gudang[totalBarang] = barangBaru;
            totalBarang++;
            System.out.println(barangBaru.getNamaBarang() + " berhasil masuk ke gudang.");
        } else {
            System.out.println("Gagal! Kapasitas gudang sudah penuh.");
        }
    }

    // Method Tracking
    public void tampilkanSemuaTracking() {
        System.out.println("\n===============================================================================");
        System.out.printf("| %-10s | %-18s | %-12s | %-4s | %-17s |%n", "ID BARANG", "NAMA BARANG", "HARGA", "STOK", "INFO KHUSUS");
        System.out.println("===============================================================================");

        if (totalBarang == 0) {
            System.out.printf("| %-73s |%n", "Gudang masih kosong. Silakan entry data terlebih dahulu.");
        } else {
            // Memanggil method hasil OVERRIDING
            for (int i = 0; i < totalBarang; i++) {
                gudang[i].tampilkanInfo();
            }
        }
        System.out.println("===============================================================================");
    }

    // Method Order Control
    public void prosesOrderKeluar(String id, int jumlahOrder){
        for (int i = 0; i < totalBarang; i++){
            if (gudang[i].getIdBarang().equals(id)){
               if (gudang[i].kurangiStok(jumlahOrder)){
                   System.out.println("Order sukses! " + gudang[i].getNamaBarang() + " dikeluarkan.");
               } else {
                   System.out.println("Order gagal! Stok " + gudang[i].getNamaBarang() + " tidak mencukupi.");
               }
               return;
            }
        }
        System.out.println("Barang dengan ID " + id + " tidak ditemukan!");
    }

    // Method untuk fitur Pengaturan Kapasitas
    public boolean ubahKapasitas(int kapasitasBaru){
        if (kapasitasBaru < totalBarang){
            System.out.println("Gagal! Kapasitas baru (" + kapasitasBaru + ") lebih kecil dari jumlah barang saat ini (" + totalBarang + ").");
            return false;
        }

        Barang[] gudangBaru = new Barang[kapasitasBaru];

        for (int i = 0; i < totalBarang; i++){
            gudangBaru[i] = this.gudang[i];
        }

        this.gudang = gudangBaru;
        this.kapasitasMaksimal = kapasitasBaru;

        System.out.println("Sukses! Kapasitas gudang diperbarui menjadi " + kapasitasMaksimal + " slot.");
        return true;
    }

    // Method Pencarian Data
    public Barang cariBarang(String id) {
        for (int i = 0; i < totalBarang; i++) {
            if (gudang[i].getIdBarang().equals(id)) {
                return gudang[i]; // Mengembalikan objek jika ditemukan
            }
        }
        return null; // Mengembalikan null jika tidak ada
    }

    public int getKapasitasMaksimal() {
        return kapasitasMaksimal;
    }

    public int getTotalBarang(){
        return totalBarang;
    }
}
