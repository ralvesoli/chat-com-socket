/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sd.app.frame;

import com.sd.app.bean.ChatMessage;
import com.sd.app.bean.ChatMessage.Action;
import com.sd.app.service.ClienteService;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;

/**
 *
 * @author ralves
 */
public class ClienteFrame extends javax.swing.JFrame {

    private Socket socket;
    private ChatMessage message;
    private ClienteService service;

    /**
     * Creates new form ClienteFrame
     */
    public ClienteFrame() {
        initComponents();
    }

    public class ListennerSocket implements Runnable {

        private ObjectInputStream input;

        public ListennerSocket(Socket socket) throws IOException {
            this.input = new ObjectInputStream(socket.getInputStream());
        }

        @Override
        public void run() {
            try {
                ChatMessage message = null;

                while ((message = (ChatMessage) input.readObject()) != null) {
                    Action action = message.getAction();

                    if (action.equals(Action.CONNECT)) {
                        connected(message);
                    } else if (action.equals(Action.DISCONNECT)) {
                        disconnected();
                        socket.close();
                    } else if (action.equals(Action.SEND_ONE)) {
                        receive(message);
                    } else if (action.equals(Action.USERS_ONLINE)) {
                        refreshListOnlines(message);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(ClienteFrame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ClienteFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void connected(ChatMessage message) {
        if (message.getText().equals("FALHOU!")) {
            this.textName.setText("");
            JOptionPane.showMessageDialog(this, "Falha ao conectar: Nome já existente!");
            return;
        }

        this.message = message;
        this.btnConectar.setEnabled(false);
        this.textName.setEnabled(false);
        this.textAreaReceive.setEnabled(true);
        this.btnExit.setEnabled(true);
        this.textAreaSend.setEnabled(true);
        this.btnSend.setEnabled(true);
        this.btnReset.setEnabled(true);

        JOptionPane.showMessageDialog(this, "Conectado com sucesso!");

    }

    private void disconnected() {

        this.message = message;
        this.btnConectar.setEnabled(true);
        this.textName.setEnabled(true);
        this.textAreaReceive.setEnabled(false);
        this.btnExit.setEnabled(false);
        this.textAreaSend.setEnabled(false);
        this.btnSend.setEnabled(false);
        this.btnReset.setEnabled(false);
        this.textAreaReceive.setText("");
        this.textAreaSend.setText("");

        JOptionPane.showMessageDialog(this, "Desconectado com sucesso!");

    }

    private void receive(ChatMessage message) {
        this.textAreaReceive.append(message.getName() + " diz: " + message.getText() + "\n");
    }

    private void refreshListOnlines(ChatMessage message) {
        System.out.println(message.getSetOnlines().toString());
        Set<String> names = message.getSetOnlines();

        names.remove(message.getName());

        String[] array = (String[]) names.toArray(new String[names.size()]);

        this.listOnlines.setListData(array);
        this.listOnlines.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.listOnlines.setLayoutOrientation(JList.VERTICAL);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        textName = new javax.swing.JTextField();
        btnConectar = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        listOnlines = new javax.swing.JList<>();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textAreaReceive = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        textAreaSend = new javax.swing.JTextArea();
        btnSend = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(119, 242, 112));

        jPanel1.setBackground(new java.awt.Color(174, 178, 253));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Conectar"));
        jPanel1.setToolTipText("");

        btnConectar.setText("Conectar");
        btnConectar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConectarActionPerformed(evt);
            }
        });

        btnExit.setText("Sair");
        btnExit.setEnabled(false);
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(textName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnConectar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnExit)
                .addGap(8, 8, 8))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(textName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnConectar)
                .addComponent(btnExit))
        );

        jPanel2.setBackground(new java.awt.Color(174, 178, 253));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Onlines"));

        jScrollPane3.setViewportView(listOnlines);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3)
        );

        jPanel3.setBackground(new java.awt.Color(174, 178, 253));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        textAreaReceive.setEditable(false);
        textAreaReceive.setColumns(20);
        textAreaReceive.setRows(5);
        textAreaReceive.setEnabled(false);
        jScrollPane1.setViewportView(textAreaReceive);

        textAreaSend.setColumns(20);
        textAreaSend.setRows(5);
        textAreaSend.setEnabled(false);
        jScrollPane2.setViewportView(textAreaSend);

        btnSend.setText("Enviar");
        btnSend.setEnabled(false);
        btnSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendActionPerformed(evt);
            }
        });

        btnReset.setText("Limpar");
        btnReset.setEnabled(false);
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btnSend, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)))
                        .addContainerGap())
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnReset)
                    .addComponent(btnSend))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnConectarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConectarActionPerformed
        String name = textName.getText();

        if (!name.isEmpty()) {
            this.message = new ChatMessage();
            this.message.setAction(Action.CONNECT);
            this.message.setName(name);

            try {
                this.service = new ClienteService();
                this.socket = this.service.connect();

                new Thread(new ListennerSocket(this.socket)).start();

                this.service.send(message);

            } catch (IOException ex) {
                Logger.getLogger(ClienteFrame.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_btnConectarActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        ChatMessage message = new ChatMessage();
        message.setName(this.message.getName());
        message.setAction(Action.DISCONNECT);
        try {
            this.service.send(message);
        } catch (IOException ex) {
            Logger.getLogger(ClienteFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        disconnected();
    }//GEN-LAST:event_btnExitActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        this.textAreaSend.setText("");
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendActionPerformed
        String text = this.textAreaSend.getText();
        String name = this.message.getName();
        if (!text.isEmpty()) {
            this.message = new ChatMessage();

            if (this.listOnlines.getSelectedIndex() > -1) {
                this.message.setNameReserved(this.listOnlines.getSelectedValue());
                this.message.setAction(Action.SEND_ONE);
                this.listOnlines.clearSelection();
            } else {
                this.message.setAction(Action.SEND_ALL);
            }

            this.message.setName(name);
            this.message.setText(text);
            this.textAreaReceive.append("Você disse: " + text + "\n");

            try {
                this.service.send(this.message);
            } catch (IOException ex) {
                Logger.getLogger(ClienteFrame.class.getName()).log(Level.SEVERE, null, ex);
            }

            this.textAreaSend.setText("");
        }
    }//GEN-LAST:event_btnSendActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConectar;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSend;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JList<String> listOnlines;
    private javax.swing.JTextArea textAreaReceive;
    private javax.swing.JTextArea textAreaSend;
    private javax.swing.JTextField textName;
    // End of variables declaration//GEN-END:variables
}
