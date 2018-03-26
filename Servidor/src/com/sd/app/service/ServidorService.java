/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sd.app.service;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sd.app.bean.ChatMessage;
import com.sd.app.bean.ChatMessage.Action;

/**
 *
 * @author ralves
 */
public class ServidorService {

    private ServerSocket serverSocket;
    private Socket socket;
    private Map<String, ObjectOutputStream> mapOnlines = new HashMap<String, ObjectOutputStream>();
    private static final Integer PORT = 2000;
    
    public ServidorService() throws IOException {
        serverSocket = new ServerSocket(PORT);

        System.out.println("Servidor online");

        while (true) {
            socket = serverSocket.accept();

            new Thread(new ListenerSocket(socket)).start();
        }
    }

    private class ListenerSocket implements Runnable {

        private ObjectOutputStream output;
        private ObjectInputStream input;

        public ListenerSocket(Socket socket) {
            try {
                this.output = new ObjectOutputStream(socket.getOutputStream());
                this.input = new ObjectInputStream(socket.getInputStream());
            } catch (IOException ex) {
                Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void run() {

            ChatMessage message = null;

            try {
                while ((message = (ChatMessage) input.readObject()) != null) {
                    Action action = message.getAction();

                    if (action.equals(Action.CONNECT)) {
                        boolean isConnect = connect(message, output);
                        if (isConnect) {
                            mapOnlines.put(message.getName(), output);
                            sendListOnlines();
                        }
                    } else if (action.equals(Action.DISCONNECT)) {
                        disconnect(message, output);
                        sendListOnlines();
                        return;
                    } else if (action.equals(Action.SEND_ONE)) {
                        sendOne(message);
                    } else if (action.equals(Action.SEND_ALL)) {
                        sendAll(message);
                    }
                }
            } catch (IOException ex) {
                ChatMessage cm = new ChatMessage();
                cm.setName(message.getName());
                disconnect(cm, output);
                sendListOnlines();
                System.out.println(message.getName() + " deixou o chat!");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(
                        ServidorService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private boolean connect(ChatMessage message, ObjectOutputStream output) throws IOException {
        if (mapOnlines.size() == 0) {
            message.setText("CONECTADO!");
            send(message, output);
            return true;
        }

        if (mapOnlines.containsKey(message.getName())) {
            message.setText("FALHOU!");
            send(message, output);
            return false;
        } else {
            message.setText("CONECTADO");
            send(message, output);
            return true;
        }
    }

    private void disconnect(ChatMessage message, ObjectOutputStream output) {
        mapOnlines.remove(message.getName());

        message.setText(message.getName() + " SAIU!");

        message.setAction(Action.SEND_ONE);

        sendAll(message);

        System.out.println(message.getName() + " saiu.");

    }

    private void sendOne(ChatMessage message) {
        for (Map.Entry<String, ObjectOutputStream> ky : mapOnlines.entrySet()) {
            if (ky.getKey().equals(message.getNameReserved())) {
                try {
                    ky.getValue().writeObject(message);
                    this.saveHistory(message);
                } catch (IOException ex) {
                    Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void send(ChatMessage message, ObjectOutputStream output) throws IOException {
        output.writeObject(message);

    }

    private void sendAll(ChatMessage message) {
        for (Map.Entry<String, ObjectOutputStream> ky : mapOnlines.entrySet()) {
            if (!ky.getKey().equals(message.getName())) {
                message.setAction(Action.SEND_ONE);
                try {
                    ky.getValue().writeObject(message);
                    this.saveHistory(message);
                } catch (IOException ex) {
                    Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void sendListOnlines() {
        Set<String> setNames = new HashSet<String>();

        for (Map.Entry<String, ObjectOutputStream> ky : mapOnlines.entrySet()) {
            setNames.add(ky.getKey());
        }

        ChatMessage message = new ChatMessage();
        message.setAction(Action.USERS_ONLINE);
        message.setSetOnlines(setNames);

        for (Map.Entry<String, ObjectOutputStream> ky : mapOnlines.entrySet()) {
            message.setName(ky.getKey());
            try {

                ky.getValue().writeObject(message);
            } catch (IOException ex) {
                Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private boolean writeFile(String host, String ipAddress, Integer port, String text) {
    	Writer writer = null;
    	String Log = "<"+host+">@<"+ipAddress+">@<"+PORT+">#<"+text+">";

    	try {
    	    writer = new BufferedWriter(new OutputStreamWriter(
    	          new FileOutputStream("history.txt"), "utf-8"));
    	    
    	    writer.write(Log + "\n");
    	    return true;
    	} catch (IOException ex) {
    	    return false;
    	} finally {
    	   try {writer.close();} catch (Exception ex) {return true;}
    	}
    }
    
    private void saveHistory(ChatMessage message) {
    	System.out.println("Salvando arquivo de histórico...");
    	String host = message.getHost();
    	String ipAddress = message.getIpAddress();
    	String text = message.getText();
    	
    	if(writeFile(host, ipAddress, PORT, text)) System.out.println("Salvo com sucesso!");
    	else System.out.println("Algo de errado aconteceu ao salvar o histórico");
    }
}
