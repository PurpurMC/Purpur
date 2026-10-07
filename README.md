# Voxen Legends

**Voxen Legends** adalah native Minecraft server fork berbasis [Purpur](https://github.com/PurpurMC/Purpur), ditargetkan untuk **Minecraft 1.21** dan **Java 21**. Repository ini mempertahankan pipeline patch Purpur/Paperweight; ini bukan plugin, mock server, atau JAR Purpur yang sekadar diganti nama.

## Status source dan batasan yang dapat diverifikasi

Source fork ini menyimpan patch Purpur dan mengambil base server Paper yang dipatok pada `paperCommit` di `gradle.properties`. Remote `upstream` menunjuk ke `https://github.com/PurpurMC/Purpur.git` untuk audit/updating.

Environment build **harus** menjalankan JDK 21. Build berhenti dengan pesan eksplisit apabila Java selain 21 digunakan; tidak ada fallback diam-diam. Sebelum mengembangkan code server, hasilkan source kerja Purpur dengan `applyPatches`; direktori hasil (`Purpur-API`, `Purpur-Server`, dan generator) sengaja tidak dikomit karena dapat direproduksi dari patch dan upstream Paper.

## Build

```bash
export JAVA_HOME=/path/ke/jdk-21
export PATH="$JAVA_HOME/bin:$PATH"
java -version
./gradlew applyPatches
./gradlew clean check buildVoxen
```

`buildVoxen` bergantung langsung pada `createMojmapBundlerJar`, yaitu task Paperweight yang membangun bundler server dari source yang dipatch. Artifact yang dapat dijalankan ditempatkan oleh task tersebut di `build/libs/`; gunakan nama artifact yang benar-benar dilaporkan Gradle, bukan JAR yang disalin atau di-rename manual.

## Menjalankan server (setelah build berhasil)

```bash
java -jar build/libs/<artifact-yang-dihasilkan>.jar nogui
```

Jangan mengklaim server dapat melakukan bootstrap, memuat dunia, atau melakukan shutdown sebelum command ini benar-benar diuji pada artifact hasil build.

## Arah pengembangan Voxen

Perubahan Voxen harus dibuat sebagai patch native pada lifecycle server Purpur, bukan sebagai server kedua. Fitur harus server-authoritative, tervalidasi, dan tidak melakukan operasi database atau API Bukkit yang tidak aman dari thread asinkron. Pesan fitur Voxen ditulis dalam Bahasa Indonesia dan konfigurasi gameplay tidak boleh menggantung pada data dari client.

Prioritas implementasi adalah integritas transaksi dan persistence sebelum ekonomi, quest, faction, bounty, legend, NPC, dialogue, territory, dan world state. Security/anti-cheat harus mencatat bukti serta menggunakan violation level dan decay; satu anomali tidak boleh otomatis menjadi ban.

## Pengembangan dan troubleshooting

* Jalankan `./gradlew applyPatches` lagi apabila source generated dihapus.
* Pastikan checkout adalah repository Git; build Purpur sengaja menolak source ZIP karena patch/upstream tidak dapat diaudit dengan benar.
* Jika Gradle melaporkan versi Java yang salah, perbaiki `JAVA_HOME` ke JDK 21, lalu ulangi command. Jangan gunakan Java 17, 25, atau fallback toolchain.
* Jangan commit `build/`, `.gradle/`, world, log, plugin runtime, atau database produksi; semuanya diabaikan oleh `.gitignore`.

Lisensi patch mengikuti [MIT](LICENSE), kecuali dinyatakan lain pada header patch.
