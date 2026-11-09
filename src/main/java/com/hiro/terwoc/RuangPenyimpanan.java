package com.hiro.terwoc;

// Komponen internal gudang yg kapasitasnya dimiliki oleh TerwocManager.
public class RuangPenyimpanan {
    private int kapasitasMaksimal;

    public RuangPenyimpanan(int kapasitasMaksimal) {
        if (kapasitasMaksimal < 0) {
            throw new IllegalArgumentException("Kapasitas tidak boleh negatif.");
        }
        this.kapasitasMaksimal = kapasitasMaksimal;
    }

    public int getKapasitasMaksimal() {
        return kapasitasMaksimal;
    }

    public boolean ubahKapasitas(int kapasitasBaru, int jumlahBarangSaatIni) {
        if (kapasitasBaru < jumlahBarangSaatIni) return false;
        kapasitasMaksimal = kapasitasBaru;
        return true;
    }

    public boolean masihTersedia(int jumlahBarangSaatIni) {
        return jumlahBarangSaatIni < kapasitasMaksimal;
    }
}
