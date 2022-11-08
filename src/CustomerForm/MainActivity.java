/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package CustomerForm;

import java.awt.Component;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.MessageFormat;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.JFrame;
import net.proteanit.sql.DbUtils;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author sparl
 */
public class MainActivity extends javax.swing.JFrame {

    Connection conn = null;
    PreparedStatement ps = null;
    Boolean flag = false;
    ResultSet rs = null;
    private JFrame frame;
    DefaultTableModel model = new DefaultTableModel();
    
    
    
    public MainActivity() {
        initComponents();
        conn = MainActivity.connectDB();
        
        try{
            String sql = "SELECT ID as 'ID', Name as 'Όνομα', Lname as 'Επίθετο', AFM as 'ΑΦΜ', Address as 'Διεύθυνση', Telephone as 'Τηλέφωνο', Mobile as 'Κινητό', City as 'Πόλη', Model as 'Μοντέλο', Benefit as 'Συνεργάτης', Seller as 'Προμηθευτής', CarCode as 'Αρ. Αυτοκιν.', KeyCode as 'Κωδ. Κλειδιού', Comments as 'Σχόλια' FROM customers";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            tableCustomers.setModel(DbUtils.resultSetToTableModel(rs));
        }
        catch(SQLException e){
        }
        
        emptyTextArea();
    }
    
    ///////////////////////////////////////////////////////////////////////////////// DB connection
    public static Connection connectDB(){
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:customersDB.db");
            return conn;
        }
        catch(ClassNotFoundException | SQLException e){
            JOptionPane.showMessageDialog(null,e);
            return null;
        }
    }
    //////////////////////////////////////////////////////////////////////////////////

    public void insertData(){
        if (labelID.getText().equals("")){
        
            String query = "INSERT INTO customers (Name, Lname, AFM, Address, Telephone, Mobile, City, Model, Benefit, Seller, CarCode, KeyCode, Comments) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            String labelname = txtName.getText(); 
            String labellname = txtLname.getText();
            int chbx1 = 0, chbx2 = 0;
            if (checkBenefit.isSelected()){
                chbx1 = 1;
            }
            if (checkSeller.isSelected()){
                chbx2 = 1;
            }

            labelCustomer.setText(labelname + " " + labellname);

            try{
                ps = conn.prepareStatement(query);

                ps.setString(1, txtName.getText());
                ps.setString(2, txtLname.getText());
                ps.setString(3, txtAFM.getText());
                ps.setString(4, txtAddress.getText());
                ps.setString(5, txtTelephone.getText());
                ps.setString(6, txtMobile.getText());
                ps.setString(7, txtCity.getText());
                ps.setString(8, txtModel.getText());
                ps.setInt(9, chbx1);
                ps.setInt(10, chbx2);
                ps.setString(11, txtCar.getText());
                ps.setString(12, txtKey.getText());
                ps.setString(13, txtComments.getText());

                ps.execute();
                try{
                    rs = ps.getGeneratedKeys();
                    String string = String.valueOf(rs.getLong(1));
                    labelID.setText(string);
                    
                }
                catch(SQLException e){
                    System.out.println(e);
                }
                
                JOptionPane.showMessageDialog(null, "Ο πελάτης καταχωρήθηκε");
               
                rs.close();
                ps.close();
               
            }catch(Exception e){
                System.out.println(e);
            }
        }
        else{
            String query = "UPDATE customers SET Name =?, Lname = ?, AFM = ?, Address = ?, Telephone = ?, Mobile = ?, City = ?, Model = ?, Benefit = ?, Seller = ?, CarCode = ?, KeyCode = ?, Comments = ? WHERE ID = ?";
            int chbx1 = 0, chbx2 = 0;
            if (checkBenefit.isSelected()){
                chbx1 = 1;
            }
            if (checkSeller.isSelected()){
                chbx2 = 1;
            }
            try{
                ps = conn.prepareStatement(query);

                ps.setString(1, txtName.getText());
                ps.setString(2, txtLname.getText());
                ps.setString(3, txtAFM.getText());
                ps.setString(4, txtAddress.getText());
                ps.setString(5, txtTelephone.getText());
                ps.setString(6, txtMobile.getText());
                ps.setString(7, txtCity.getText());
                ps.setString(8, txtModel.getText());
                ps.setInt(9, chbx1);
                ps.setInt(10, chbx2);
                ps.setString(11, txtCar.getText());
                ps.setString(12, txtKey.getText());
                ps.setString(13, txtComments.getText());
                ps.setString(14, labelID.getText());

                ps.execute();
                
                JOptionPane.showMessageDialog(null, "Οι αλλαγές αποθηκεύτηκαν");
                rs.close();
                ps.close();
            }catch(Exception e){
                System.out.println(e);
            }
        }
    }
    
    /////////////////////////////////////////////////////////////////////////////
    
    public void emptyTextArea(){
        txtArea.setText("Παρακαλώ επιλέξτε πελάτη για την προβολή στοιχείων...");
    }
    
    /////////////////////////////////////////////////////////////////////////////
    
    public void fillTextArea(){
        
        String purchaseText = "";
        String sql = "SELECT * FROM purchases WHERE CustomerID = ?";
        try{
            ps = conn.prepareStatement(sql);
            ps.setString(1, labelID.getText());
            //ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
       
            int counter = 0;
            while(rs.next()){
                counter++;
                purchaseText = purchaseText + "#" + counter
                        + "\nΌνομα Προιόντος - Υπηρεσίας:\t\t" + rs.getString("PName") 
                        + "\nΚωδικός:\t\t\t" + rs.getString("PCode")
                        + "\nΤιμή:\t\t\t" + rs.getString("PPrice")
                        + "\nΗμερομηνία Αγοράς:\t\t" + rs.getString("PDate")
                        + "\n\n";
            }
            if (counter == 0){
                 purchaseText = purchaseText + "\nΔεν υπάρχουν καταχωρημένες αγορές...";
            }
            
        }catch(SQLException e){
            System.out.println(e);
        }
        
        String yesNo1, yesNo2;
        if (checkBenefit.isSelected()){
            yesNo1 = "Ναι";
        }
        else{
            yesNo1 = "Όχι";
        }
        if (checkSeller.isSelected()){
            yesNo2 = "Ναι";
        }
        else{
            yesNo2 = "Όχι";
        }
        txtArea.setText(
                "Lock Center Hellas - Λίστα στοιχείων πελατών" +
                "\n\n-----------------------------------------------------------------------------------\n" +
                "Πληροφορίες Πελάτη:" +
                "\n-----------------------------------------------------------------------------------\n" +
                "\nΌνομα:\t\t" + txtName.getText() +
                "\nΕπίθετο:\t\t" + txtLname.getText() +
                "\nΑΦΜ:\t\t" + txtAFM.getText() +
                "\nΤηλέφωνο:\t\t" + txtTelephone.getText() +
                "\nΚινητό:\t\t" + txtMobile.getText() +
                "\nΔιεύθυνση:\t\t" + txtAddress.getText() +
                "\nΠόλη:\t\t" + txtCity.getText() +
                "\n\nΜάρκα Οχήματος:\t" + txtModel.getText() +
                "\nΑριθμός Οχήματος:\t" + txtCar.getText() +
                "\nΚωδικός Κλειδιού:\t" + txtKey.getText() +
                "\n\nΣυνεργάτης:\t\t" + yesNo1 +
                "\nΠρομηθευτής:\t\t" + yesNo2 +
                "\n\nΕπιπλέον Σχόλια:\t" + txtComments.getText() +
                "\n\n-----------------------------------------------------------------------------------\n" +
                "Αγορές Πελάτη:" +
                "\n-----------------------------------------------------------------------------------\n" +
                purchaseText
               
        );
      
    }
    
    public void printCustomerCard(){
        MessageFormat header = new MessageFormat("Κάρτα Πελατών\n");
        MessageFormat footer = new MessageFormat("Σελίδα {0, number, integer}");
        try{
            txtArea.print(header, footer);
        }catch(java.awt.print.PrinterException e){
            System.err.format("Δεν βρέθηκε εκτυπωτής", e.getMessage());
        }
        
    }
    
    /////////////////////////////////////////////////////////////////////////////
    
    public void updateTable(){
        try{
            String sql = "SELECT ID as 'ID', Name as 'Όνομα', Lname as 'Επίθετο', AFM as 'ΑΦΜ', Address as 'Διεύθυνση', Telephone as 'Τηλέφωνο', Mobile as 'Κινητό', City as 'Πόλη', Model as 'Μοντέλο', Benefit as 'Συνεργάτης', Seller as 'Προμηθευτής', CarCode as 'Αρ. Αυτοκιν.', KeyCode as 'Κωδ. Κλειδιού', Comments as 'Σχόλια' FROM customers";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            tableCustomers.setModel(DbUtils.resultSetToTableModel(rs));
        }
        catch(SQLException e){
        }
    }
    
    /////////////////////////////////////////////////////////////////////////////
    
    public void emptyForm(){
        labelCustomer.setText("(Νέος Πελάτης)");
        labelID.setText("");
        txtName.setText(null);
        txtLname.setText(null);
        txtAFM.setText(null);
        txtAddress.setText(null);
        txtCity.setText(null);
        txtMobile.setText(null);
        txtTelephone.setText(null);
        txtModel.setText(null);
        checkBenefit.setSelected(false);
        checkSeller.setSelected(false);
        txtCar.setText(null);
        txtKey.setText(null);
        txtComments.setText(null);
        txtProductName.setText(null);
        txtProductCode.setText(null);
        txtProductPrice.setText(null);
        txtProductDate.setText(null);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ScrollContainer = new javax.swing.JScrollPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        txtAFM = new javax.swing.JTextField();
        txtTelephone = new javax.swing.JTextField();
        txtCity = new javax.swing.JTextField();
        txtModel = new javax.swing.JTextField();
        txtLname = new javax.swing.JTextField();
        txtAddress = new javax.swing.JTextField();
        txtMobile = new javax.swing.JTextField();
        checkBenefit = new javax.swing.JCheckBox();
        btnSave = new javax.swing.JButton();
        btnNew = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        labelCustomer = new javax.swing.JLabel();
        labelID = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        checkSeller = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        btnRefreshProduct = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        txtProductName = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtProductPrice = new javax.swing.JTextField();
        txtProductDate = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        btnSaveProduct = new javax.swing.JButton();
        txtProductCode = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtCar = new javax.swing.JTextField();
        txtKey = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtComments = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtArea = new javax.swing.JTextArea();
        printButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableCustomers = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        findSeller = new javax.swing.JCheckBox();
        jLabel23 = new javax.swing.JLabel();
        findPartner = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBounds(new java.awt.Rectangle(0, 0, 0, 0));
        setPreferredSize(new java.awt.Dimension(1230, 800));

        jPanel4.setPreferredSize(new java.awt.Dimension(1300, 900));

        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jPanel1.setAutoscrolls(true);
        jPanel1.setPreferredSize(new java.awt.Dimension(600, 600));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel1.setText("Κάρτα Πελάτη ");

        jLabel3.setText("Επίθετο (*):");

        jLabel4.setText("ΑΦΜ:");

        jLabel5.setText("Διεύθυνση:");

        jLabel6.setText("Τηλέφωνο:");

        jLabel7.setText("Κινητό (*):");

        jLabel8.setText("Πόλη:");

        jLabel9.setText("Μοντέλο:");

        jLabel10.setText("Συνεργάτης:");

        jLabel11.setText("Όνομα (*):");

        btnSave.setBackground(new java.awt.Color(204, 204, 255));
        btnSave.setText("Αποθήκευση Στοιχείων");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnNew.setBackground(new java.awt.Color(204, 204, 255));
        btnNew.setText("Προσθήκη Νέου Πελάτη");
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });

        btnDelete.setBackground(new java.awt.Color(255, 204, 204));
        btnDelete.setText("Διαγραφή Πελάτη");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        labelCustomer.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        labelCustomer.setText("(Νέος Πελάτης)");

        labelID.setEnabled(false);

        jLabel17.setText("Προμηθευτής:");

        jLabel15.setText("Κωδικός Αγοράς (*):");

        btnRefreshProduct.setBackground(new java.awt.Color(204, 204, 255));
        btnRefreshProduct.setText("Εκκαθάριση Φόρμας");
        btnRefreshProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshProductActionPerformed(evt);
            }
        });

        jLabel14.setText("Τιμή Αγοράς:");

        jLabel16.setText("Ημερομηνία Αγοράς:");

        jLabel13.setText("Όνομα Αγοράς (*):");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel12.setText("Προσθήκη Εργασίας - Αγοράς");

        btnSaveProduct.setBackground(new java.awt.Color(204, 204, 255));
        btnSaveProduct.setText("Αποθήκευση Νέας Αγοράς");
        btnSaveProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveProductActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 3, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(txtProductPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                                .addComponent(txtProductDate, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(txtProductName, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtProductCode, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btnSaveProduct)
                        .addGap(18, 18, 18)
                        .addComponent(btnRefreshProduct)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 18, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addGap(27, 27, 27)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtProductName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(txtProductCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(txtProductDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(txtProductPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSaveProduct)
                    .addComponent(btnRefreshProduct)))
        );

        jLabel18.setText("Αρ. Αυτοκιν:");

        jLabel19.setText("Κωδ. Κλειδιού:");

        txtCar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCarActionPerformed(evt);
            }
        });

        txtKey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKeyActionPerformed(evt);
            }
        });

        jLabel20.setText("Παρατηρήσεις:");

        txtComments.setColumns(20);
        txtComments.setLineWrap(true);
        txtComments.setRows(5);
        jScrollPane3.setViewportView(txtComments);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelCustomer)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnSave)
                        .addGap(27, 27, 27)
                        .addComponent(btnNew)
                        .addGap(83, 83, 83)
                        .addComponent(btnDelete))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel20)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jScrollPane3))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel9)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtModel, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel8)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtCity, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel6)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtTelephone, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel4)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtAFM, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel11)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel18)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                                    .addComponent(txtCar, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel3)
                                .addComponent(jLabel5)
                                .addComponent(jLabel7)
                                .addComponent(jLabel10)
                                .addComponent(jLabel19)
                                .addComponent(jLabel17))
                            .addGap(28, 28, 28)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(checkSeller)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(labelID, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtLname, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                                        .addComponent(txtAddress)
                                        .addComponent(txtMobile)
                                        .addComponent(txtKey))
                                    .addComponent(checkBenefit, javax.swing.GroupLayout.Alignment.LEADING))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(labelID))
                .addGap(18, 18, 18)
                .addComponent(labelCustomer)
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(jLabel3)
                            .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtLname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(txtAFM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel6)
                            .addComponent(txtTelephone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMobile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(jLabel18)
                            .addComponent(txtCar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtKey, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txtCity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17)))
                    .addComponent(checkSeller))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(checkBenefit, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9)
                        .addComponent(jLabel10)
                        .addComponent(txtModel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave)
                    .addComponent(btnNew)
                    .addComponent(btnDelete))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(42, Short.MAX_VALUE))
        );

        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jPanel2.setPreferredSize(new java.awt.Dimension(600, 580));

        txtArea.setColumns(20);
        txtArea.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtArea.setLineWrap(true);
        txtArea.setRows(5);
        jScrollPane2.setViewportView(txtArea);

        printButton.setText("Εκτύπωση...");
        printButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(printButton)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 499, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(printButton)
                .addContainerGap(54, Short.MAX_VALUE))
        );

        tableCustomers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Όνομα", "Επώνυμο", "ΑΦΜ", "Διεύθυνση", "Τηλέφωνο", "Κινητό", "Πόλη", "Μοντέλο", "Χονδρική"
            }
        ));
        tableCustomers.getTableHeader().setReorderingAllowed(false);
        tableCustomers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableCustomersMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableCustomers);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Αναζήτηση:");

        txtSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchActionPerformed(evt);
            }
        });
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
        });

        jLabel22.setText("Κάρτα Προμηθευτών");

        findSeller.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                findSellerStateChanged(evt);
            }
        });

        jLabel23.setText("Κάρτα Συνεργατών");

        findPartner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                findPartnerStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(88, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel2)
                                .addGap(13, 13, 13)
                                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(jLabel22)
                                .addGap(10, 10, 10)
                                .addComponent(findSeller)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel23)
                                .addGap(10, 10, 10)
                                .addComponent(findPartner)))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel22)
                        .addComponent(findSeller)
                        .addComponent(jLabel23)
                        .addComponent(findPartner)))
                .addContainerGap(91, Short.MAX_VALUE))
        );

        ScrollContainer.setViewportView(jPanel4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ScrollContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ScrollContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
        if (txtSearch.getText().equals("")){
            if ((!findPartner.isSelected())&&(!findSeller.isSelected())){
                this.updateTable();
            }
            else if ((findPartner.isSelected())&&(findSeller.isSelected())){
                try{
                    String query = "SELECT ID as 'ID', Name as 'Όνομα', Lname as 'Επίθετο', AFM as 'ΑΦΜ', Address as 'Διεύθυνση', Telephone as 'Τηλέφωνο', Mobile as 'Κινητό', City as 'Πόλη', Model as 'Μοντέλο', Benefit as 'Συνεργάτης', Seller as 'Προμηθευτής', CarCode as 'Αρ. Αυτοκιν.', KeyCode as 'Κωδ. Κλειδιού', Comments as 'Σχόλια' FROM customers WHERE Benefit LIKE ? AND Seller LIKE ?";
                    ps = conn.prepareStatement(query);
                   
                    ps.setInt(1, 1);
                    ps.setInt(2, 1);
                    
                    rs = ps.executeQuery();
                    tableCustomers.setModel(DbUtils.resultSetToTableModel(rs));
                }catch(SQLException e){
                    System.out.println(e);
                }finally{
                    try{
                        rs.close();
                        ps.close();
                    }catch(SQLException e){
                        System.out.println(e);
                    }
                }
            }
            else if (findPartner.isSelected()){
                try{
                    String query = "SELECT ID as 'ID', Name as 'Όνομα', Lname as 'Επίθετο', AFM as 'ΑΦΜ', Address as 'Διεύθυνση', Telephone as 'Τηλέφωνο', Mobile as 'Κινητό', City as 'Πόλη', Model as 'Μοντέλο', Benefit as 'Συνεργάτης', Seller as 'Προμηθευτής', CarCode as 'Αρ. Αυτοκιν.', KeyCode as 'Κωδ. Κλειδιού', Comments as 'Σχόλια' FROM customers WHERE Benefit LIKE ?";
                    ps = conn.prepareStatement(query);
                    
                    ps.setInt(1, 1);
                    
                    rs = ps.executeQuery();
                    tableCustomers.setModel(DbUtils.resultSetToTableModel(rs));
                }catch(SQLException e){
                    System.out.println(e);
                }finally{
                    try{
                        rs.close();
                        ps.close();
                    }catch(SQLException e){
                        System.out.println(e);
                    }
                }
            }
            else if (findSeller.isSelected()){
                try{
                    String query = "SELECT ID as 'ID', Name as 'Όνομα', Lname as 'Επίθετο', AFM as 'ΑΦΜ', Address as 'Διεύθυνση', Telephone as 'Τηλέφωνο', Mobile as 'Κινητό', City as 'Πόλη', Model as 'Μοντέλο', Benefit as 'Συνεργάτης', Seller as 'Προμηθευτής', CarCode as 'Αρ. Αυτοκιν.', KeyCode as 'Κωδ. Κλειδιού', Comments as 'Σχόλια' FROM customers WHERE Seller LIKE ?";
                    ps = conn.prepareStatement(query);
                    
                    ps.setInt(1, 1);
                    
                    rs = ps.executeQuery();
                    tableCustomers.setModel(DbUtils.resultSetToTableModel(rs));
                }catch(SQLException e){
                    System.out.println(e);
                }finally{
                    try{
                        rs.close();
                        ps.close();
                    }catch(SQLException e){
                        System.out.println(e);
                    }
                }
            }
        }
        else{
            if ((!findPartner.isSelected())&&(!findSeller.isSelected())){
                try{
                    String query = "SELECT ID as 'ID', Name as 'Όνομα', Lname as 'Επίθετο', AFM as 'ΑΦΜ', Address as 'Διεύθυνση', Telephone as 'Τηλέφωνο', Mobile as 'Κινητό', City as 'Πόλη', Model as 'Μοντέλο', Benefit as 'Συνεργάτης', Seller as 'Προμηθευτής', CarCode as 'Αρ. Αυτοκιν.', KeyCode as 'Κωδ. Κλειδιού', Comments as 'Σχόλια' FROM customers WHERE (Name LIKE ? OR Lname LIKE ? OR AFM LIKE ? OR Mobile LIKE ? OR Telephone LIKE ? OR CarCode LIKE ? OR KeyCode LIKE ?)";
                    ps = conn.prepareStatement(query);
                    String myString = "%" + txtSearch.getText() + "%";
                    ps.setString(1, myString);
                    ps.setString(2, myString);
                    ps.setString(3, myString);
                    ps.setString(4, myString);
                    ps.setString(5, myString);
                    ps.setString(6, myString);
                    ps.setString(7, myString);
                    
                    rs = ps.executeQuery();
                    tableCustomers.setModel(DbUtils.resultSetToTableModel(rs));
                }catch(SQLException e){
                    System.out.println(e);
                }finally{
                    try{
                        rs.close();
                        ps.close();
                    }catch(SQLException e){
                        System.out.println(e);
                    }
                }
            }
            else if((findPartner.isSelected())&&(findSeller.isSelected())){
                try{
                    String query = "SELECT ID as 'ID', Name as 'Όνομα', Lname as 'Επίθετο', AFM as 'ΑΦΜ', Address as 'Διεύθυνση', Telephone as 'Τηλέφωνο', Mobile as 'Κινητό', City as 'Πόλη', Model as 'Μοντέλο', Benefit as 'Συνεργάτης', Seller as 'Προμηθευτής', CarCode as 'Αρ. Αυτοκιν.', KeyCode as 'Κωδ. Κλειδιού', Comments as 'Σχόλια' FROM customers WHERE (Name LIKE ? OR Lname LIKE ? OR AFM LIKE ? OR Mobile LIKE ? OR Telephone LIKE ? OR CarCode LIKE ? OR KeyCode LIKE ?) AND Benefit LIKE ? AND Seller LIKE ?";
                    ps = conn.prepareStatement(query);
                    String myString = "%" + txtSearch.getText() + "%";
                    ps.setString(1, myString);
                    ps.setString(2, myString);
                    ps.setString(3, myString);
                    ps.setString(4, myString);
                    ps.setString(5, myString);
                    ps.setString(6, myString);
                    ps.setString(7, myString);
                    ps.setInt(8, 1);
                    ps.setInt(9, 1);

                    rs = ps.executeQuery();
                    tableCustomers.setModel(DbUtils.resultSetToTableModel(rs));

                }catch(SQLException e){
                    System.out.println(e);
                }finally{
                    try{
                        rs.close();
                        ps.close();
                    }catch(SQLException e){
                        System.out.println(e);
                    }
                }
            }
            else if((findPartner.isSelected())){
                try{
                    String query = "SELECT ID as 'ID', Name as 'Όνομα', Lname as 'Επίθετο', AFM as 'ΑΦΜ', Address as 'Διεύθυνση', Telephone as 'Τηλέφωνο', Mobile as 'Κινητό', City as 'Πόλη', Model as 'Μοντέλο', Benefit as 'Συνεργάτης', Seller as 'Προμηθευτής', CarCode as 'Αρ. Αυτοκιν.', KeyCode as 'Κωδ. Κλειδιού', Comments as 'Σχόλια' FROM customers WHERE (Name LIKE ? OR Lname LIKE ? OR AFM LIKE ? OR Mobile LIKE ? OR Telephone LIKE ? OR CarCode LIKE ? OR KeyCode LIKE ?) AND Benefit LIKE ?";
                    ps = conn.prepareStatement(query);
                    String myString = "%" + txtSearch.getText() + "%";
                    ps.setString(1, myString);
                    ps.setString(2, myString);
                    ps.setString(3, myString);
                    ps.setString(4, myString);
                    ps.setString(5, myString);
                    ps.setString(6, myString);
                    ps.setString(7, myString);
                    ps.setInt(8, 1);

                    rs = ps.executeQuery();
                    tableCustomers.setModel(DbUtils.resultSetToTableModel(rs));

                }catch(SQLException e){
                    System.out.println(e);
                }finally{
                    try{
                        rs.close();
                        ps.close();
                    }catch(SQLException e){
                        System.out.println(e);
                    }
                }
            }
            else if((findSeller.isSelected())){
                try{
                    String query = "SELECT ID as 'ID', Name as 'Όνομα', Lname as 'Επίθετο', AFM as 'ΑΦΜ', Address as 'Διεύθυνση', Telephone as 'Τηλέφωνο', Mobile as 'Κινητό', City as 'Πόλη', Model as 'Μοντέλο', Benefit as 'Συνεργάτης', Seller as 'Προμηθευτής', CarCode as 'Αρ. Αυτοκιν.', KeyCode as 'Κωδ. Κλειδιού', Comments as 'Σχόλια' FROM customers WHERE (Name LIKE ? OR Lname LIKE ? OR AFM LIKE ? OR Mobile LIKE ? OR Telephone LIKE ? OR CarCode LIKE ? OR KeyCode LIKE ?) AND Seller LIKE ?";
                    ps = conn.prepareStatement(query);
                    String myString = "%" + txtSearch.getText() + "%";
                    ps.setString(1, myString);
                    ps.setString(2, myString);
                    ps.setString(3, myString);
                    ps.setString(4, myString);
                    ps.setString(5, myString);
                    ps.setString(6, myString);
                    ps.setString(7, myString);
                    ps.setInt(8, 1);

                    rs = ps.executeQuery();
                    tableCustomers.setModel(DbUtils.resultSetToTableModel(rs));

                }catch(SQLException e){
                    System.out.println(e);
                }finally{
                    try{
                        rs.close();
                        ps.close();
                    }catch(SQLException e){
                        System.out.println(e);
                    }
                }
            }
        }
    }//GEN-LAST:event_txtSearchKeyReleased

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchActionPerformed

    private void tableCustomersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableCustomersMouseClicked
        DefaultTableModel model = (DefaultTableModel) tableCustomers.getModel();
        int selectedRowIndex = tableCustomers.getSelectedRow();

        labelCustomer.setText(model.getValueAt(selectedRowIndex, 1).toString() + " " + model.getValueAt(selectedRowIndex, 2).toString());
        labelID.setText(model.getValueAt(selectedRowIndex, 0).toString());
        //txtID.setText(model.getValueAt(selectedRowIndex, 0).toString());
        txtName.setText(model.getValueAt(selectedRowIndex, 1).toString());
        txtLname.setText(model.getValueAt(selectedRowIndex, 2).toString());
        txtAFM.setText(model.getValueAt(selectedRowIndex, 3).toString());
        txtAddress.setText(model.getValueAt(selectedRowIndex, 4).toString());
        txtTelephone.setText(model.getValueAt(selectedRowIndex, 5).toString());
        txtMobile.setText(model.getValueAt(selectedRowIndex, 6).toString());
        txtCity.setText(model.getValueAt(selectedRowIndex, 7).toString());
        txtModel.setText(model.getValueAt(selectedRowIndex, 8).toString());

        if (model.getValueAt(selectedRowIndex, 9) != null){
            String tblchk = model.getValueAt(selectedRowIndex, 9).toString();
            int number = Integer.parseInt(tblchk);
            if (number == 1){
                checkBenefit.setSelected(true);
            }
            else{
                checkBenefit.setSelected(false);
            }
        }
        if (model.getValueAt(selectedRowIndex, 10) != null){
            String tblchk = model.getValueAt(selectedRowIndex, 10).toString();
            int number = Integer.parseInt(tblchk);
            if (number == 1){
                checkSeller.setSelected(true);
            }
            else{
                checkSeller.setSelected(false);
            }
        }  
        txtCar.setText(model.getValueAt(selectedRowIndex, 11).toString());
        txtKey.setText(model.getValueAt(selectedRowIndex, 12).toString());
        txtComments.setText(model.getValueAt(selectedRowIndex, 13).toString());
        
        fillTextArea();
    }//GEN-LAST:event_tableCustomersMouseClicked

    private void printButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printButtonActionPerformed
        printCustomerCard();
    }//GEN-LAST:event_printButtonActionPerformed

    private void btnSaveProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveProductActionPerformed
        if (txtProductName.getText().equals("") || txtProductCode.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Συμπληρώστε τα απαραίτητα πεδία");
        }
        else if (labelID.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Πρέπει να επιλέξετε πελάτη για προσθήκη αγοράς");
        }
        else{
            frame = new JFrame("Προσθήκη Αγοράς");

            if(JOptionPane.showConfirmDialog(frame, "Θέλετε σίγουρα να προσθέσετε αυτή την αγορά?", "Επιβεβαίωση Αγοράς", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION){

                String query = "INSERT INTO purchases (CustomerID, PName, PCode, PPrice, PDate) VALUES (?, ?, ?, ?, ?)";

                try{
                    ps = conn.prepareStatement(query);

                    ps.setString(1, labelID.getText());
                    ps.setString(2, txtProductName.getText());
                    ps.setString(3, txtProductCode.getText());
                    ps.setString(4, txtProductPrice.getText());
                    ps.setString(5, txtProductDate.getText());

                    ps.execute();
                    JOptionPane.showMessageDialog(null, "Η αγορά προστέθηκε!");
                    rs.close();
                    ps.close();

                    fillTextArea();

                }catch(Exception e){
                    System.out.println(e);
                }
            }
        }
    }//GEN-LAST:event_btnSaveProductActionPerformed

    private void btnRefreshProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshProductActionPerformed
        txtProductName.setText(null);
        txtProductCode.setText(null);
        txtProductPrice.setText(null);
        txtProductDate.setText(null);
    }//GEN-LAST:event_btnRefreshProductActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        frame = new JFrame("Διαγραφή");

        int row = tableCustomers.getSelectedRow();
        DefaultTableModel model = (DefaultTableModel) tableCustomers.getModel();

        if(tableCustomers.getSelectedRow() == -1){
            if (tableCustomers.getRowCount() == 0){
                JOptionPane.showMessageDialog(null, "Δεν υπάρχει κάτι προς διαγραφή", "Ουψ!", JOptionPane.OK_OPTION);

            }
            else{
                JOptionPane.showMessageDialog(null, "Επιλέξτε έναν πελάτη προς διαγραφή", "Σφάλμα!", JOptionPane.OK_OPTION);
            }
        }
        else{
            if(JOptionPane.showConfirmDialog(frame, "Σίγουρα θέλετε να διαγράψετε τον συγκεκριμένο πελάτη μαζί με τις αγορές του;", "Επιβεβαίωση", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION){
                String sel = model.getValueAt(row, 0).toString();
                model.removeRow(tableCustomers.getSelectedRow());

                String sql = "DELETE FROM customers WHERE ID = ?";
                String sqlb = "DELETE FROM purchases WHERE CustomerID = ?";

                try{
                    ps = conn.prepareStatement(sql);
                    ps.setString(1, sel);
                    ps.executeUpdate();

                    ps = conn.prepareStatement(sqlb);
                    ps.setString(1, sel);
                    ps.executeUpdate();

                    emptyTextArea();
                    emptyForm();

                }

                catch(SQLException e){
                    System.out.println(e);
                }
            }

        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        emptyForm();
        updateTable();
        emptyTextArea();
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed

        if (txtName.getText().equals("") || txtLname.getText().equals("") || txtMobile.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Πρέπει να συμπληρώσετε τα απαραίτητα πεδία");
        }
        else{
            fillTextArea();
            insertData();
            updateTable();

        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void txtCarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCarActionPerformed

    private void txtKeyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKeyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKeyActionPerformed

    private void findSellerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_findSellerStateChanged
        if (txtSearch.getText().equals("")){
            if ((!findPartner.isSelected())&&(!findSeller.isSelected())){
                this.updateTable();
            }
            else if ((findPartner.isSelected())&&(findSeller.isSelected())){
                try{
                    String query = "SELECT ID as 'ID', Name as 'Όνομα', Lname as 'Επίθετο', AFM as 'ΑΦΜ', Address as 'Διεύθυνση', Telephone as 'Τηλέφωνο', Mobile as 'Κινητό', City as 'Πόλη', Model as 'Μοντέλο', Benefit as 'Συνεργάτης', Seller as 'Προμηθευτής', CarCode as 'Αρ. Αυτοκιν.', KeyCode as 'Κωδ. Κλειδιού', Comments as 'Σχόλια' FROM customers WHERE Benefit LIKE ? AND Seller LIKE ?";
                    ps = conn.prepareStatement(query);
                   
                    ps.setInt(1, 1);
                    ps.setInt(2, 1);
                    
                    rs = ps.executeQuery();
                    tableCustomers.setModel(DbUtils.resultSetToTableModel(rs));
                }catch(SQLException e){
                    System.out.println(e);
                }finally{
                    try{
                        rs.close();
                        ps.close();
                    }catch(SQLException e){
                        System.out.println(e);
                    }
                }
            }
            else if (findPartner.isSelected()){
                try{
                    String query = "SELECT ID as 'ID', Name as 'Όνομα', Lname as 'Επίθετο', AFM as 'ΑΦΜ', Address as 'Διεύθυνση', Telephone as 'Τηλέφωνο', Mobile as 'Κινητό', City as 'Πόλη', Model as 'Μοντέλο', Benefit as 'Συνεργάτης', Seller as 'Προμηθευτής', CarCode as 'Αρ. Αυτοκιν.', KeyCode as 'Κωδ. Κλειδιού', Comments as 'Σχόλια' FROM customers WHERE Benefit LIKE ?";
                    ps = conn.prepareStatement(query);
                    
                    ps.setInt(1, 1);
                    
                    rs = ps.executeQuery();
                    tableCustomers.setModel(DbUtils.resultSetToTableModel(rs));
                }catch(SQLException e){
                    System.out.println(e);
                }finally{
                    try{
                        rs.close();
                        ps.close();
                    }catch(SQLException e){
                        System.out.println(e);
                    }
                }
            }
            else if (findSeller.isSelected()){
                try{
                    String query = "SELECT ID as 'ID', Name as 'Όνομα', Lname as 'Επίθετο', AFM as 'ΑΦΜ', Address as 'Διεύθυνση', Telephone as 'Τηλέφωνο', Mobile as 'Κινητό', City as 'Πόλη', Model as 'Μοντέλο', Benefit as 'Συνεργάτης', Seller as 'Προμηθευτής', CarCode as 'Αρ. Αυτοκιν.', KeyCode as 'Κωδ. Κλειδιού', Comments as 'Σχόλια' FROM customers WHERE Seller LIKE ?";
                    ps = conn.prepareStatement(query);
                    
                    ps.setInt(1, 1);
                    
                    rs = ps.executeQuery();
                    tableCustomers.setModel(DbUtils.resultSetToTableModel(rs));
                }catch(SQLException e){
                    System.out.println(e);
                }finally{
                    try{
                        rs.close();
                        ps.close();
                    }catch(SQLException e){
                        System.out.println(e);
                    }
                }
            }
        }
        else{
            if ((!findPartner.isSelected())&&(!findSeller.isSelected())){
                try{
                    String query = "SELECT ID as 'ID', Name as 'Όνομα', Lname as 'Επίθετο', AFM as 'ΑΦΜ', Address as 'Διεύθυνση', Telephone as 'Τηλέφωνο', Mobile as 'Κινητό', City as 'Πόλη', Model as 'Μοντέλο', Benefit as 'Συνεργάτης', Seller as 'Προμηθευτής', CarCode as 'Αρ. Αυτοκιν.', KeyCode as 'Κωδ. Κλειδιού', Comments as 'Σχόλια' FROM customers WHERE (Name LIKE ? OR Lname LIKE ? OR AFM LIKE ? OR Mobile LIKE ? OR Telephone LIKE ? OR CarCode LIKE ? OR KeyCode LIKE ?)";
                    ps = conn.prepareStatement(query);
                    String myString = "%" + txtSearch.getText() + "%";
                    ps.setString(1, myString);
                    ps.setString(2, myString);
                    ps.setString(3, myString);
                    ps.setString(4, myString);
                    ps.setString(5, myString);
                    ps.setString(6, myString);
                    ps.setString(7, myString);
                    
                    rs = ps.executeQuery();
                    tableCustomers.setModel(DbUtils.resultSetToTableModel(rs));
                }catch(SQLException e){
                    System.out.println(e);
                }finally{
                    try{
                        rs.close();
                        ps.close();
                    }catch(SQLException e){
                        System.out.println(e);
                    }
                }
            }
            else if((findPartner.isSelected())&&(findSeller.isSelected())){
                try{
                    String query = "SELECT ID as 'ID', Name as 'Όνομα', Lname as 'Επίθετο', AFM as 'ΑΦΜ', Address as 'Διεύθυνση', Telephone as 'Τηλέφωνο', Mobile as 'Κινητό', City as 'Πόλη', Model as 'Μοντέλο', Benefit as 'Συνεργάτης', Seller as 'Προμηθευτής', CarCode as 'Αρ. Αυτοκιν.', KeyCode as 'Κωδ. Κλειδιού', Comments as 'Σχόλια' FROM customers WHERE (Name LIKE ? OR Lname LIKE ? OR AFM LIKE ? OR Mobile LIKE ? OR Telephone LIKE ? OR CarCode LIKE ? OR KeyCode LIKE ?) AND Benefit LIKE ? AND Seller LIKE ?";
                    ps = conn.prepareStatement(query);
                    String myString = "%" + txtSearch.getText() + "%";
                    ps.setString(1, myString);
                    ps.setString(2, myString);
                    ps.setString(3, myString);
                    ps.setString(4, myString);
                    ps.setString(5, myString);
                    ps.setString(6, myString);
                    ps.setString(7, myString);
                    ps.setInt(8, 1);
                    ps.setInt(9, 1);

                    rs = ps.executeQuery();
                    tableCustomers.setModel(DbUtils.resultSetToTableModel(rs));

                }catch(SQLException e){
                    System.out.println(e);
                }finally{
                    try{
                        rs.close();
                        ps.close();
                    }catch(SQLException e){
                        System.out.println(e);
                    }
                }
            }
            else if((findPartner.isSelected())){
                try{
                    String query = "SELECT ID as 'ID', Name as 'Όνομα', Lname as 'Επίθετο', AFM as 'ΑΦΜ', Address as 'Διεύθυνση', Telephone as 'Τηλέφωνο', Mobile as 'Κινητό', City as 'Πόλη', Model as 'Μοντέλο', Benefit as 'Συνεργάτης', Seller as 'Προμηθευτής', CarCode as 'Αρ. Αυτοκιν.', KeyCode as 'Κωδ. Κλειδιού', Comments as 'Σχόλια' FROM customers WHERE (Name LIKE ? OR Lname LIKE ? OR AFM LIKE ? OR Mobile LIKE ? OR Telephone LIKE ? OR CarCode LIKE ? OR KeyCode LIKE ?) AND Benefit LIKE ?";
                    ps = conn.prepareStatement(query);
                    String myString = "%" + txtSearch.getText() + "%";
                    ps.setString(1, myString);
                    ps.setString(2, myString);
                    ps.setString(3, myString);
                    ps.setString(4, myString);
                    ps.setString(5, myString);
                    ps.setString(6, myString);
                    ps.setString(7, myString);
                    ps.setInt(8, 1);

                    rs = ps.executeQuery();
                    tableCustomers.setModel(DbUtils.resultSetToTableModel(rs));

                }catch(SQLException e){
                    System.out.println(e);
                }finally{
                    try{
                        rs.close();
                        ps.close();
                    }catch(SQLException e){
                        System.out.println(e);
                    }
                }
            }
            else if((findSeller.isSelected())){
                try{
                    String query = "SELECT ID as 'ID', Name as 'Όνομα', Lname as 'Επίθετο', AFM as 'ΑΦΜ', Address as 'Διεύθυνση', Telephone as 'Τηλέφωνο', Mobile as 'Κινητό', City as 'Πόλη', Model as 'Μοντέλο', Benefit as 'Συνεργάτης', Seller as 'Προμηθευτής', CarCode as 'Αρ. Αυτοκιν.', KeyCode as 'Κωδ. Κλειδιού', Comments as 'Σχόλια' FROM customers WHERE (Name LIKE ? OR Lname LIKE ? OR AFM LIKE ? OR Mobile LIKE ? OR Telephone LIKE ? OR CarCode LIKE ? OR KeyCode LIKE ?) AND Seller LIKE ?";
                    ps = conn.prepareStatement(query);
                    String myString = "%" + txtSearch.getText() + "%";
                    ps.setString(1, myString);
                    ps.setString(2, myString);
                    ps.setString(3, myString);
                    ps.setString(4, myString);
                    ps.setString(5, myString);
                    ps.setString(6, myString);
                    ps.setString(7, myString);
                    ps.setInt(8, 1);

                    rs = ps.executeQuery();
                    tableCustomers.setModel(DbUtils.resultSetToTableModel(rs));

                }catch(SQLException e){
                    System.out.println(e);
                }finally{
                    try{
                        rs.close();
                        ps.close();
                    }catch(SQLException e){
                        System.out.println(e);
                    }
                }
            }
        }
    }//GEN-LAST:event_findSellerStateChanged

    private void findPartnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_findPartnerStateChanged
        if (txtSearch.getText().equals("")){
            if ((!findPartner.isSelected())&&(!findSeller.isSelected())){
                this.updateTable();
            }
            else if ((findPartner.isSelected())&&(findSeller.isSelected())){
                try{
                    String query = "SELECT ID as 'ID', Name as 'Όνομα', Lname as 'Επίθετο', AFM as 'ΑΦΜ', Address as 'Διεύθυνση', Telephone as 'Τηλέφωνο', Mobile as 'Κινητό', City as 'Πόλη', Model as 'Μοντέλο', Benefit as 'Συνεργάτης', Seller as 'Προμηθευτής', CarCode as 'Αρ. Αυτοκιν.', KeyCode as 'Κωδ. Κλειδιού', Comments as 'Σχόλια' FROM customers WHERE Benefit LIKE ? AND Seller LIKE ?";
                    ps = conn.prepareStatement(query);
                   
                    ps.setInt(1, 1);
                    ps.setInt(2, 1);
                    
                    rs = ps.executeQuery();
                    tableCustomers.setModel(DbUtils.resultSetToTableModel(rs));
                }catch(SQLException e){
                    System.out.println(e);
                }finally{
                    try{
                        rs.close();
                        ps.close();
                    }catch(SQLException e){
                        System.out.println(e);
                    }
                }
            }
            else if (findPartner.isSelected()){
                try{
                    String query = "SELECT ID as 'ID', Name as 'Όνομα', Lname as 'Επίθετο', AFM as 'ΑΦΜ', Address as 'Διεύθυνση', Telephone as 'Τηλέφωνο', Mobile as 'Κινητό', City as 'Πόλη', Model as 'Μοντέλο', Benefit as 'Συνεργάτης', Seller as 'Προμηθευτής', CarCode as 'Αρ. Αυτοκιν.', KeyCode as 'Κωδ. Κλειδιού', Comments as 'Σχόλια' FROM customers WHERE Benefit LIKE ?";
                    ps = conn.prepareStatement(query);
                    
                    ps.setInt(1, 1);
                    
                    rs = ps.executeQuery();
                    tableCustomers.setModel(DbUtils.resultSetToTableModel(rs));
                }catch(SQLException e){
                    System.out.println(e);
                }finally{
                    try{
                        rs.close();
                        ps.close();
                    }catch(SQLException e){
                        System.out.println(e);
                    }
                }
            }
            else if (findSeller.isSelected()){
                try{
                    String query = "SELECT ID as 'ID', Name as 'Όνομα', Lname as 'Επίθετο', AFM as 'ΑΦΜ', Address as 'Διεύθυνση', Telephone as 'Τηλέφωνο', Mobile as 'Κινητό', City as 'Πόλη', Model as 'Μοντέλο', Benefit as 'Συνεργάτης', Seller as 'Προμηθευτής', CarCode as 'Αρ. Αυτοκιν.', KeyCode as 'Κωδ. Κλειδιού', Comments as 'Σχόλια' FROM customers WHERE Seller LIKE ?";
                    ps = conn.prepareStatement(query);
                    
                    ps.setInt(1, 1);
                    
                    rs = ps.executeQuery();
                    tableCustomers.setModel(DbUtils.resultSetToTableModel(rs));
                }catch(SQLException e){
                    System.out.println(e);
                }finally{
                    try{
                        rs.close();
                        ps.close();
                    }catch(SQLException e){
                        System.out.println(e);
                    }
                }
            }
        }
        else{
            if ((!findPartner.isSelected())&&(!findSeller.isSelected())){
                try{
                    String query = "SELECT ID as 'ID', Name as 'Όνομα', Lname as 'Επίθετο', AFM as 'ΑΦΜ', Address as 'Διεύθυνση', Telephone as 'Τηλέφωνο', Mobile as 'Κινητό', City as 'Πόλη', Model as 'Μοντέλο', Benefit as 'Συνεργάτης', Seller as 'Προμηθευτής', CarCode as 'Αρ. Αυτοκιν.', KeyCode as 'Κωδ. Κλειδιού', Comments as 'Σχόλια' FROM customers WHERE (Name LIKE ? OR Lname LIKE ? OR AFM LIKE ? OR Mobile LIKE ? OR Telephone LIKE ? OR CarCode LIKE ? OR KeyCode LIKE ?)";
                    ps = conn.prepareStatement(query);
                    String myString = "%" + txtSearch.getText() + "%";
                    ps.setString(1, myString);
                    ps.setString(2, myString);
                    ps.setString(3, myString);
                    ps.setString(4, myString);
                    ps.setString(5, myString);
                    ps.setString(6, myString);
                    ps.setString(7, myString);
                    
                    rs = ps.executeQuery();
                    tableCustomers.setModel(DbUtils.resultSetToTableModel(rs));
                }catch(SQLException e){
                    System.out.println(e);
                }finally{
                    try{
                        rs.close();
                        ps.close();
                    }catch(SQLException e){
                        System.out.println(e);
                    }
                }
            }
            else if((findPartner.isSelected())&&(findSeller.isSelected())){
                try{
                    String query = "SELECT ID as 'ID', Name as 'Όνομα', Lname as 'Επίθετο', AFM as 'ΑΦΜ', Address as 'Διεύθυνση', Telephone as 'Τηλέφωνο', Mobile as 'Κινητό', City as 'Πόλη', Model as 'Μοντέλο', Benefit as 'Συνεργάτης', Seller as 'Προμηθευτής', CarCode as 'Αρ. Αυτοκιν.', KeyCode as 'Κωδ. Κλειδιού', Comments as 'Σχόλια' FROM customers WHERE (Name LIKE ? OR Lname LIKE ? OR AFM LIKE ? OR Mobile LIKE ? OR Telephone LIKE ? OR CarCode LIKE ? OR KeyCode LIKE ?) AND Benefit LIKE ? AND Seller LIKE ?";
                    ps = conn.prepareStatement(query);
                    String myString = "%" + txtSearch.getText() + "%";
                    ps.setString(1, myString);
                    ps.setString(2, myString);
                    ps.setString(3, myString);
                    ps.setString(4, myString);
                    ps.setString(5, myString);
                    ps.setString(6, myString);
                    ps.setString(7, myString);
                    ps.setInt(8, 1);
                    ps.setInt(9, 1);

                    rs = ps.executeQuery();
                    tableCustomers.setModel(DbUtils.resultSetToTableModel(rs));

                }catch(SQLException e){
                    System.out.println(e);
                }finally{
                    try{
                        rs.close();
                        ps.close();
                    }catch(SQLException e){
                        System.out.println(e);
                    }
                }
            }
            else if((findPartner.isSelected())){
                try{
                    String query = "SELECT ID as 'ID', Name as 'Όνομα', Lname as 'Επίθετο', AFM as 'ΑΦΜ', Address as 'Διεύθυνση', Telephone as 'Τηλέφωνο', Mobile as 'Κινητό', City as 'Πόλη', Model as 'Μοντέλο', Benefit as 'Συνεργάτης', Seller as 'Προμηθευτής', CarCode as 'Αρ. Αυτοκιν.', KeyCode as 'Κωδ. Κλειδιού', Comments as 'Σχόλια' FROM customers WHERE (Name LIKE ? OR Lname LIKE ? OR AFM LIKE ? OR Mobile LIKE ? OR Telephone LIKE ? OR CarCode LIKE ? OR KeyCode LIKE ?) AND Benefit LIKE ?";
                    ps = conn.prepareStatement(query);
                    String myString = "%" + txtSearch.getText() + "%";
                    ps.setString(1, myString);
                    ps.setString(2, myString);
                    ps.setString(3, myString);
                    ps.setString(4, myString);
                    ps.setString(5, myString);
                    ps.setString(6, myString);
                    ps.setString(7, myString);
                    ps.setInt(8, 1);

                    rs = ps.executeQuery();
                    tableCustomers.setModel(DbUtils.resultSetToTableModel(rs));

                }catch(SQLException e){
                    System.out.println(e);
                }finally{
                    try{
                        rs.close();
                        ps.close();
                    }catch(SQLException e){
                        System.out.println(e);
                    }
                }
            }
            else if((findSeller.isSelected())){
                try{
                    String query = "SELECT ID as 'ID', Name as 'Όνομα', Lname as 'Επίθετο', AFM as 'ΑΦΜ', Address as 'Διεύθυνση', Telephone as 'Τηλέφωνο', Mobile as 'Κινητό', City as 'Πόλη', Model as 'Μοντέλο', Benefit as 'Συνεργάτης', Seller as 'Προμηθευτής', CarCode as 'Αρ. Αυτοκιν.', KeyCode as 'Κωδ. Κλειδιού', Comments as 'Σχόλια' FROM customers WHERE (Name LIKE ? OR Lname LIKE ? OR AFM LIKE ? OR Mobile LIKE ? OR Telephone LIKE ? OR CarCode LIKE ? OR KeyCode LIKE ?) AND Seller LIKE ?";
                    ps = conn.prepareStatement(query);
                    String myString = "%" + txtSearch.getText() + "%";
                    ps.setString(1, myString);
                    ps.setString(2, myString);
                    ps.setString(3, myString);
                    ps.setString(4, myString);
                    ps.setString(5, myString);
                    ps.setString(6, myString);
                    ps.setString(7, myString);
                    ps.setInt(8, 1);

                    rs = ps.executeQuery();
                    tableCustomers.setModel(DbUtils.resultSetToTableModel(rs));

                }catch(SQLException e){
                    System.out.println(e);
                }finally{
                    try{
                        rs.close();
                        ps.close();
                    }catch(SQLException e){
                        System.out.println(e);
                    }
                }
            }
        }
    }//GEN-LAST:event_findPartnerStateChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainActivity.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainActivity.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainActivity.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainActivity.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainActivity().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane ScrollContainer;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnRefreshProduct;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSaveProduct;
    private javax.swing.JCheckBox checkBenefit;
    private javax.swing.JCheckBox checkSeller;
    private javax.swing.JCheckBox findPartner;
    private javax.swing.JCheckBox findSeller;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel labelCustomer;
    private javax.swing.JLabel labelID;
    private javax.swing.JButton printButton;
    private javax.swing.JTable tableCustomers;
    private javax.swing.JTextField txtAFM;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextArea txtArea;
    private javax.swing.JTextField txtCar;
    private javax.swing.JTextField txtCity;
    private javax.swing.JTextArea txtComments;
    private javax.swing.JTextField txtKey;
    private javax.swing.JTextField txtLname;
    private javax.swing.JTextField txtMobile;
    private javax.swing.JTextField txtModel;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtProductCode;
    private javax.swing.JTextField txtProductDate;
    private javax.swing.JTextField txtProductName;
    private javax.swing.JTextField txtProductPrice;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtTelephone;
    // End of variables declaration//GEN-END:variables
}
