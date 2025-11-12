/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crud;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import java.sql.ResultSetMetaData;

/**
 *
 * @author ASUS
 */
public class crud {
    private Connection Koneksidb;
    private String username="root";
    private String password="";
    private String dbname="db_perikanan"; 
    private String urlKoneksi="jdbc:mysql://localhost/"+dbname;
    public boolean duplikasi=false;
    public String CEK_ID_UKM_PRODUK, CEK_NAMA_PRODUK = null;
    public String CEK_NAMA_UKM, CEK_ALAMAT_UKM, CEK_JENIS_USAHA, CEK_TAHUN_BERDIRI, CEK_KECAMATAN, CEK_NOMOR, CEK_INFO, CEK_LOKASI = null;
    public String CEK_NAMA_KEGIATAN, CEK_TANGGAL_KEGIATAN, CEK_TEMPAT_KEGIATAN = null;
    public String CEK_ID_UKM_KOMEN, CEK_REPLY_FROM, CEK_NAMA_KOMEN, CEK_EMAIL_KOMEN, CEK_ISI_KOMEN, CEK_TANGGAL_KOMEN = null;
    public String CEK_NAMA_PENGURUS, CEK_JABATAN = null;

    
    public crud(){
        try {
            Driver dbdriver = new com.mysql.jdbc.Driver();
            DriverManager.registerDriver(dbdriver);
            Koneksidb=DriverManager.getConnection(urlKoneksi,username,password);
            System.out.print("Database Terkoneksi");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,e.toString());
        }
    }

    public void simpanDaftarProduk01(String id_produk, String id_ukm, String nama_produk){
        try {
            // Kolom 'foto_produk' DIHILANGKAN
            String sqlsimpan="insert into daftar_produk(id_produk, id_ukm, nama_produk) value"
                    + " ('"+id_produk+"', '"+id_ukm+"', '"+nama_produk+"')";
            String sqlcari="select*from daftar_produk where id_produk='"+id_produk+"'";
            
            Statement cari=Koneksidb.createStatement();
            ResultSet data=cari.executeQuery(sqlcari);
            
            if (data.next()){
                JOptionPane.showMessageDialog(null, "ID Produk sudah terdaftar");
            } else {
                Statement perintah=Koneksidb.createStatement();
                perintah.execute(sqlsimpan);
                JOptionPane.showMessageDialog(null, "Data Produk berhasil disimpan");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public void simpanDaftarProduk02(String id_produk, String id_ukm, String nama_produk){
        try {
            // Kolom 'foto_produk' DIHILANGKAN
            String sqlsimpan="INSERT INTO daftar_produk (id_produk, id_ukm, nama_produk) VALUES (?, ?, ?)";
            String sqlcari= "SELECT*FROM daftar_produk WHERE id_produk = ?";
            
            PreparedStatement cari = Koneksidb.prepareStatement(sqlcari);
            cari.setString(1, id_produk);
            ResultSet data = cari.executeQuery();
            
            if (data.next()){
                JOptionPane.showMessageDialog(null, "ID Produk sudah terdaftar");
                this.duplikasi = true;
                this.CEK_ID_UKM_PRODUK = data.getString("id_ukm");
                this.CEK_NAMA_PRODUK = data.getString("nama_produk");
                // CEK_FOTO_PRODUK dihilangkan
            } else {
                this.duplikasi = false;
                this.CEK_ID_UKM_PRODUK = null;
                this.CEK_NAMA_PRODUK = null;
                
                PreparedStatement perintah = Koneksidb.prepareStatement(sqlsimpan);
                perintah.setString(1, id_produk);
                perintah.setString(2, id_ukm);
                perintah.setString(3, nama_produk);
                perintah.executeUpdate();
                JOptionPane.showMessageDialog(null, "Data Produk berhasil disimpan");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void ubahDaftarProduk(String id_produk, String id_ukm, String nama_produk){
        try {
            // Kolom 'foto_produk' DIHILANGKAN
            String sqlubah="UPDATE daftar_produk SET id_ukm = ?, nama_produk = ? WHERE id_produk = ?";
            PreparedStatement perintah = Koneksidb.prepareStatement(sqlubah);
            perintah.setString(1, id_ukm);
            perintah.setString(2, nama_produk);
            perintah.setString(3, id_produk); // ID sebagai parameter ke-3
            perintah.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Produk berhasil diubah");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void hapusDaftarProduk(String id_produk){
        try {
            String sqlhapus="DELETE FROM daftar_produk WHERE id_produk = ?";
            PreparedStatement perintah = Koneksidb.prepareStatement(sqlhapus);
            perintah.setString(1, id_produk);
            perintah.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Produk berhasil dihapus");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void tampilDataDaftarProduk(JTable komponentabel, String SQL){
        try {
            PreparedStatement perintah = Koneksidb.prepareStatement(SQL);
            ResultSet data = perintah.executeQuery();
            ResultSetMetaData meta = data.getMetaData();
            int jumlahkolom = meta.getColumnCount();
            DefaultTableModel modeltabel = new DefaultTableModel();
            
            modeltabel.addColumn("ID Produk");
            modeltabel.addColumn("ID UKM");
            modeltabel.addColumn("Nama Produk");
            // Kolom 'Foto Produk' DIHILANGKAN
            
            while(data.next()){
                Object[] row = new Object[jumlahkolom];
                for(int i=1; i<=jumlahkolom; i++){
                    row[i-1]=data.getObject(i);
                }
                modeltabel.addRow(row);
            }
            komponentabel.setModel(modeltabel);
        } catch (Exception e) {
            // e.printStackTrace(); 
        }
    }

    public void simpanDaftarUkm01(String id_ukm, String nama_ukm, String alamat, String jenis_usaha, String tahun_berdiri, String kecamatan, String nomor, String info, String lokasi){
        try {
            // Kolom 'gambar' DIHILANGKAN
            String sqlsimpan="insert into daftar_ukm(id_ukm, nama_ukm, alamat, jenis_usaha, tahun_berdiri, kecamatan, nomor, info, lokasi) value"
                    + " ('"+id_ukm+"', '"+nama_ukm+"', '"+alamat+"', '"+jenis_usaha+"', '"+tahun_berdiri+"', '"+kecamatan+"', '"+nomor+"', '"+info+"', '"+lokasi+"')";
            String sqlcari="select*from daftar_ukm where id_ukm='"+id_ukm+"'";
            
            Statement cari=Koneksidb.createStatement();
            ResultSet data=cari.executeQuery(sqlcari);
            
            if (data.next()){
                JOptionPane.showMessageDialog(null, "ID UKM sudah terdaftar");
            } else {
                Statement perintah=Koneksidb.createStatement();
                perintah.execute(sqlsimpan);
                JOptionPane.showMessageDialog(null, "Data UKM berhasil disimpan");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public void simpanDaftarUkm02(String id_ukm, String nama_ukm, String alamat, String jenis_usaha, String tahun_berdiri, String kecamatan, String nomor, String info, String lokasi){
        try {
            // Kolom 'gambar' DIHILANGKAN
            String sqlsimpan="INSERT INTO daftar_ukm (id_ukm, nama_ukm, alamat, jenis_usaha, tahun_berdiri, kecamatan, nomor, info, lokasi) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            String sqlcari= "SELECT*FROM daftar_ukm WHERE id_ukm = ?";
            
            PreparedStatement cari = Koneksidb.prepareStatement(sqlcari);
            cari.setString(1, id_ukm);
            ResultSet data = cari.executeQuery();
            
            if (data.next()){
                JOptionPane.showMessageDialog(null, "ID UKM sudah terdaftar");
                this.duplikasi = true;
                this.CEK_NAMA_UKM = data.getString("nama_ukm");
                this.CEK_ALAMAT_UKM = data.getString("alamat");
                this.CEK_JENIS_USAHA = data.getString("jenis_usaha");
                this.CEK_TAHUN_BERDIRI = data.getString("tahun_berdiri");
                this.CEK_KECAMATAN = data.getString("kecamatan");
                this.CEK_NOMOR = data.getString("nomor");
                this.CEK_INFO = data.getString("info");
                this.CEK_LOKASI = data.getString("lokasi");
                // CEK_GAMBAR dihilangkan
            } else {
                this.duplikasi = false;
                this.CEK_NAMA_UKM = null;
                this.CEK_ALAMAT_UKM = null;
                this.CEK_JENIS_USAHA = null;
                this.CEK_TAHUN_BERDIRI = null;
                this.CEK_KECAMATAN = null;
                this.CEK_NOMOR = null;
                this.CEK_INFO = null;
                this.CEK_LOKASI = null;
                
                PreparedStatement perintah = Koneksidb.prepareStatement(sqlsimpan);
                perintah.setString(1, id_ukm);
                perintah.setString(2, nama_ukm);
                perintah.setString(3, alamat);
                perintah.setString(4, jenis_usaha);
                perintah.setString(5, tahun_berdiri);
                perintah.setString(6, kecamatan);
                perintah.setString(7, nomor);
                perintah.setString(8, info);
                perintah.setString(9, lokasi);
                perintah.executeUpdate();
                JOptionPane.showMessageDialog(null, "Data UKM berhasil disimpan");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void ubahDaftarUkm(String id_ukm, String nama_ukm, String alamat, String jenis_usaha, String tahun_berdiri, String kecamatan, String nomor, String info, String lokasi){
        try {
            // Kolom 'gambar' DIHILANGKAN
            String sqlubah="UPDATE daftar_ukm SET nama_ukm = ?, alamat = ?, jenis_usaha = ?, tahun_berdiri = ?, kecamatan = ?, nomor = ?, info = ?, lokasi = ? WHERE id_ukm = ?";
            PreparedStatement perintah = Koneksidb.prepareStatement(sqlubah);
            perintah.setString(1, nama_ukm);
            perintah.setString(2, alamat);
            perintah.setString(3, jenis_usaha);
            perintah.setString(4, tahun_berdiri);
            perintah.setString(5, kecamatan);
            perintah.setString(6, nomor);
            perintah.setString(7, info);
            perintah.setString(8, lokasi);
            perintah.setString(9, id_ukm); 
            perintah.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data UKM berhasil diubah");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void hapusDaftarUkm(String id_ukm){
        try {
            String sqlhapus="DELETE FROM daftar_ukm WHERE id_ukm = ?";
            PreparedStatement perintah = Koneksidb.prepareStatement(sqlhapus);
            perintah.setString(1, id_ukm);
            perintah.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data UKM berhasil dihapus");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void tampilDataDaftarUkm(JTable komponentabel, String SQL){
        try {
            PreparedStatement perintah = Koneksidb.prepareStatement(SQL);
            ResultSet data = perintah.executeQuery();
            ResultSetMetaData meta = data.getMetaData();
            int jumlahkolom = meta.getColumnCount();
            DefaultTableModel modeltabel = new DefaultTableModel();
            
            modeltabel.addColumn("ID UKM");
            modeltabel.addColumn("Nama UKM");
            modeltabel.addColumn("Alamat");
            modeltabel.addColumn("Jenis Usaha");
            modeltabel.addColumn("Tahun Berdiri");
            modeltabel.addColumn("Kecamatan");
            modeltabel.addColumn("Nomor");
            modeltabel.addColumn("Info");
            modeltabel.addColumn("Lokasi");
            
            while(data.next()){
                Object[] row = new Object[jumlahkolom];
                for(int i=1; i<=jumlahkolom; i++){
                    row[i-1]=data.getObject(i);
                }
                modeltabel.addRow(row);
            }
            komponentabel.setModel(modeltabel);
        } catch (Exception e) {
        }
    }

    public void simpanKegiatan01(String id_kegiatan, String nama_kegiatan, String tanggal, String tempat){
        try {
            String sqlsimpan="insert into kegiatan(id_kegiatan, nama_kegiatan, tanggal, tempat) value"
                    + " ('"+id_kegiatan+"', '"+nama_kegiatan+"', '"+tanggal+"', '"+tempat+"')";
            String sqlcari="select*from kegiatan where id_kegiatan='"+id_kegiatan+"'";
            
            Statement cari=Koneksidb.createStatement();
            ResultSet data=cari.executeQuery(sqlcari);
            
            if (data.next()){
                JOptionPane.showMessageDialog(null, "ID Kegiatan sudah terdaftar");
            } else {
                Statement perintah=Koneksidb.createStatement();
                perintah.execute(sqlsimpan);
                JOptionPane.showMessageDialog(null, "Data Kegiatan berhasil disimpan");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public void simpanKegiatan02(String id_kegiatan, String nama_kegiatan, String tanggal, String tempat){
        try {
            String sqlsimpan="INSERT INTO kegiatan (id_kegiatan, nama_kegiatan, tanggal, tempat) VALUES (?, ?, ?, ?)";
            String sqlcari= "SELECT*FROM kegiatan WHERE id_kegiatan = ?";
            
            PreparedStatement cari = Koneksidb.prepareStatement(sqlcari);
            cari.setString(1, id_kegiatan);
            ResultSet data = cari.executeQuery();
            
            if (data.next()){
                JOptionPane.showMessageDialog(null, "ID Kegiatan sudah terdaftar");
                this.duplikasi = true;
                this.CEK_NAMA_KEGIATAN = data.getString("nama_kegiatan");
                this.CEK_TANGGAL_KEGIATAN = data.getString("tanggal");
                this.CEK_TEMPAT_KEGIATAN = data.getString("tempat");
            } else {
                this.duplikasi = false;
                this.CEK_NAMA_KEGIATAN = null;
                this.CEK_TANGGAL_KEGIATAN = null;
                this.CEK_TEMPAT_KEGIATAN = null;
                
                PreparedStatement perintah = Koneksidb.prepareStatement(sqlsimpan);
                perintah.setString(1, id_kegiatan);
                perintah.setString(2, nama_kegiatan);
                perintah.setString(3, tanggal);
                perintah.setString(4, tempat);
                perintah.executeUpdate();
                JOptionPane.showMessageDialog(null, "Data Kegiatan berhasil disimpan");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void ubahKegiatan(String id_kegiatan, String nama_kegiatan, String tanggal, String tempat){
        try {
            String sqlubah="UPDATE kegiatan SET nama_kegiatan = ?, tanggal = ?, tempat = ? WHERE id_kegiatan = ?";
            PreparedStatement perintah = Koneksidb.prepareStatement(sqlubah);
            perintah.setString(1, nama_kegiatan);
            perintah.setString(2, tanggal);
            perintah.setString(3, tempat);
            perintah.setString(4, id_kegiatan); 
            perintah.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Kegiatan berhasil diubah");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void hapusKegiatan(String id_kegiatan){
        try {
            String sqlhapus="DELETE FROM kegiatan WHERE id_kegiatan = ?";
            PreparedStatement perintah = Koneksidb.prepareStatement(sqlhapus);
            perintah.setString(1, id_kegiatan);
            perintah.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Kegiatan berhasil dihapus");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void tampilDataKegiatan(JTable komponentabel, String SQL){
        try {
            PreparedStatement perintah = Koneksidb.prepareStatement(SQL);
            ResultSet data = perintah.executeQuery();
            ResultSetMetaData meta = data.getMetaData();
            int jumlahkolom = meta.getColumnCount();
            DefaultTableModel modeltabel = new DefaultTableModel();
            
            modeltabel.addColumn("ID Kegiatan");
            modeltabel.addColumn("Nama Kegiatan");
            modeltabel.addColumn("Tanggal");
            modeltabel.addColumn("Tempat");
            
            while(data.next()){
                Object[] row = new Object[jumlahkolom];
                for(int i=1; i<=jumlahkolom; i++){
                    row[i-1]=data.getObject(i);
                }
                modeltabel.addRow(row);
            }
            komponentabel.setModel(modeltabel);
        } catch (Exception e) {
        }
    }

    public void simpanKomentar01(String id_komen, String id_ukm, String reply_from, String nama_komen, String email_komen, String isi_komen, String tanggal){
        try {
            String sqlsimpan="insert into komentar(id_komen, id_ukm, reply_from, nama_komen, email_komen, isi_komen, tanggal) value"
                    + " ('"+id_komen+"', '"+id_ukm+"', '"+reply_from+"', '"+nama_komen+"', '"+email_komen+"', '"+isi_komen+"', '"+tanggal+"')";
            String sqlcari="select*from komentar where id_komen='"+id_komen+"'";
            
            Statement cari=Koneksidb.createStatement();
            ResultSet data=cari.executeQuery(sqlcari);
            
            if (data.next()){
                JOptionPane.showMessageDialog(null, "ID Komentar sudah terdaftar");
            } else {
                Statement perintah=Koneksidb.createStatement();
                perintah.execute(sqlsimpan);
                JOptionPane.showMessageDialog(null, "Data Komentar berhasil disimpan");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public void simpanKomentar02(String id_komen, String id_ukm, String reply_from, String nama_komen, String email_komen, String isi_komen, String tanggal){
        try {
            String sqlsimpan="INSERT INTO komentar (id_komen, id_ukm, reply_from, nama_komen, email_komen, isi_komen, tanggal) VALUES (?, ?, ?, ?, ?, ?, ?)";
            String sqlcari= "SELECT*FROM komentar WHERE id_komen = ?";
            
            PreparedStatement cari = Koneksidb.prepareStatement(sqlcari);
            cari.setString(1, id_komen);
            ResultSet data = cari.executeQuery();
            
            if (data.next()){
                JOptionPane.showMessageDialog(null, "ID Komentar sudah terdaftar");
                this.duplikasi = true;
                this.CEK_ID_UKM_KOMEN = data.getString("id_ukm");
                this.CEK_REPLY_FROM = data.getString("reply_from");
                this.CEK_NAMA_KOMEN = data.getString("nama_komen");
                this.CEK_EMAIL_KOMEN = data.getString("email_komen");
                this.CEK_ISI_KOMEN = data.getString("isi_komen");
                this.CEK_TANGGAL_KOMEN = data.getString("tanggal");
            } else {
                this.duplikasi = false;
                this.CEK_ID_UKM_KOMEN = null;
                this.CEK_REPLY_FROM = null;
                this.CEK_NAMA_KOMEN = null;
                this.CEK_EMAIL_KOMEN = null;
                this.CEK_ISI_KOMEN = null;
                this.CEK_TANGGAL_KOMEN = null;
                
                PreparedStatement perintah = Koneksidb.prepareStatement(sqlsimpan);
                perintah.setString(1, id_komen);
                perintah.setString(2, id_ukm);
                perintah.setString(3, reply_from);
                perintah.setString(4, nama_komen);
                perintah.setString(5, email_komen);
                perintah.setString(6, isi_komen);
                perintah.setString(7, tanggal);
                perintah.executeUpdate();
                JOptionPane.showMessageDialog(null, "Data Komentar berhasil disimpan");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void ubahKomentar(String id_komen, String id_ukm, String reply_from, String nama_komen, String email_komen, String isi_komen, String tanggal){
        try {
            String sqlubah="UPDATE komentar SET id_ukm = ?, reply_from = ?, nama_komen = ?, email_komen = ?, isi_komen = ?, tanggal = ? WHERE id_komen = ?";
            PreparedStatement perintah = Koneksidb.prepareStatement(sqlubah);
            perintah.setString(1, id_ukm);
            perintah.setString(2, reply_from);
            perintah.setString(3, nama_komen);
            perintah.setString(4, email_komen);
            perintah.setString(5, isi_komen);
            perintah.setString(6, tanggal);
            perintah.setString(7, id_komen); 
            perintah.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Komentar berhasil diubah");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void hapusKomentar(String id_komen){
        try {
            String sqlhapus="DELETE FROM komentar WHERE id_komen = ?";
            PreparedStatement perintah = Koneksidb.prepareStatement(sqlhapus);
            perintah.setString(1, id_komen);
            perintah.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Komentar berhasil dihapus");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void tampilDataKomentar(JTable komponentabel, String SQL){
        try {
            PreparedStatement perintah = Koneksidb.prepareStatement(SQL);
            ResultSet data = perintah.executeQuery();
            ResultSetMetaData meta = data.getMetaData();
            int jumlahkolom = meta.getColumnCount();
            DefaultTableModel modeltabel = new DefaultTableModel();
            
            modeltabel.addColumn("ID Komen");
            modeltabel.addColumn("ID UKM");
            modeltabel.addColumn("Reply From");
            modeltabel.addColumn("Nama");
            modeltabel.addColumn("Email");
            modeltabel.addColumn("Isi Komentar");
            modeltabel.addColumn("Tanggal");
            
            while(data.next()){
                Object[] row = new Object[jumlahkolom];
                for(int i=1; i<=jumlahkolom; i++){
                    row[i-1]=data.getObject(i);
                }
                modeltabel.addRow(row);
            }
            komponentabel.setModel(modeltabel);
        } catch (Exception e) {
        }
    }

    public void simpanPengurus01(String id_pengurus, String nama_pengurus, String jabatan){
        try {
            // Kolom 'foto_pengurus' DIHILANGKAN
            String sqlsimpan="insert into pengurus(id_pengurus, nama_pengurus, jabatan) value"
                    + " ('"+id_pengurus+"', '"+nama_pengurus+"', '"+jabatan+"')";
            String sqlcari="select*from pengurus where id_pengurus='"+id_pengurus+"'";
            
            Statement cari=Koneksidb.createStatement();
            ResultSet data=cari.executeQuery(sqlcari);
            
            if (data.next()){
                JOptionPane.showMessageDialog(null, "ID Pengurus sudah terdaftar");
            } else {
                Statement perintah=Koneksidb.createStatement();
                perintah.execute(sqlsimpan);
                JOptionPane.showMessageDialog(null, "Data Pengurus berhasil disimpan");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    // Pola 02 (Aman)
    public void simpanPengurus02(String id_pengurus, String nama_pengurus, String jabatan){
        try {
            String sqlsimpan="INSERT INTO pengurus (id_pengurus, nama_pengurus, jabatan) VALUES (?, ?, ?)";
            String sqlcari= "SELECT*FROM pengurus WHERE id_pengurus = ?";
            
            PreparedStatement cari = Koneksidb.prepareStatement(sqlcari);
            cari.setString(1, id_pengurus);
            ResultSet data = cari.executeQuery();
            
            if (data.next()){
                JOptionPane.showMessageDialog(null, "ID Pengurus sudah terdaftar");
                this.duplikasi = true;
                this.CEK_NAMA_PENGURUS = data.getString("nama_pengurus");
                this.CEK_JABATAN = data.getString("jabatan");
                // CEK_FOTO_PENGURUS dihilangkan
            } else {
                this.duplikasi = false;
                this.CEK_NAMA_PENGURUS = null;
                this.CEK_JABATAN = null;
                
                PreparedStatement perintah = Koneksidb.prepareStatement(sqlsimpan);
                perintah.setString(1, id_pengurus);
                perintah.setString(2, nama_pengurus);
                perintah.setString(3, jabatan);
                perintah.executeUpdate();
                JOptionPane.showMessageDialog(null, "Data Pengurus berhasil disimpan");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void ubahPengurus(String id_pengurus, String nama_pengurus, String jabatan){
        try {
            String sqlubah="UPDATE pengurus SET nama_pengurus = ?, jabatan = ? WHERE id_pengurus = ?";
            PreparedStatement perintah = Koneksidb.prepareStatement(sqlubah);
            perintah.setString(1, nama_pengurus);
            perintah.setString(2, jabatan);
            perintah.setString(3, id_pengurus); // ID sebagai parameter ke-3
            perintah.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Pengurus berhasil diubah");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void hapusPengurus(String id_pengurus){
        try {
            String sqlhapus="DELETE FROM pengurus WHERE id_pengurus = ?";
            PreparedStatement perintah = Koneksidb.prepareStatement(sqlhapus);
            perintah.setString(1, id_pengurus);
            perintah.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Pengurus berhasil dihapus");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void tampilDataPengurus(JTable komponentabel, String SQL){
        try {
            PreparedStatement perintah = Koneksidb.prepareStatement(SQL);
            ResultSet data = perintah.executeQuery();
            ResultSetMetaData meta = data.getMetaData();
            int jumlahkolom = meta.getColumnCount();
            DefaultTableModel modeltabel = new DefaultTableModel();
            
            modeltabel.addColumn("ID Pengurus");
            modeltabel.addColumn("Nama Pengurus");
            modeltabel.addColumn("Jabatan");
            
            while(data.next()){
                Object[] row = new Object[jumlahkolom];
                for(int i=1; i<=jumlahkolom; i++){
                    row[i-1]=data.getObject(i);
                }
                modeltabel.addRow(row);
            }
            komponentabel.setModel(modeltabel);
        } catch (Exception e) {
        }
    } 
}
    
