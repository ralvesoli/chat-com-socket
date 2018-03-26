/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sd.app.service;

import com.sd.app.bean.ChatMessage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author ralves
 */
public class ClienteService {
    
    private Socket socket;
    private ObjectOutputStream output;
    
    public Socket connect() throws IOException {
        this.socket = new Socket("localhost", 2000);
        this.output = new ObjectOutputStream(socket.getOutputStream());
        return socket;
    }
    
    public void send(ChatMessage message) throws IOException{
        output.writeObject(message);
    }
}
