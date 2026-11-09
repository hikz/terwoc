package com.hiro.terwoc;

// Entitas mandiri; dibuat di luar TerwocManager lalu didaftarkan
public class Pemasok {
    private final String idPemasok;
    private final String namaPemasok;
    private final String kontak;

    public Pemasok(String idPemasok, String namaPemasok, String kontak) {
        if (idPemasok == null || idPemasok.isBlank()) throw new IllegalArgumentException("ID pemasok wajib diisi.");
        if (namaPemasok == null || namaPemasok.isBlank()) throw new IllegalArgumentException("Nama pemasok wajib diisi.");
        this.idPemasok = idPemasok;
        this.namaPemasok = namaPemasok;
        this.kontak = kontak == null ? "-" : kontak;
    }

    public String getIdPemasok() { return idPemasok; }
    public String getNamaPemasok() { return namaPemasok; }
    public String getKontak() { return kontak; }

    public void tampilkanInfo() {
        System.out.printf("| %-10s | %-24s | %-24s |%n", idPemasok, namaPemasok, kontak);
    }
}
