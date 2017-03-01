package com.company;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Common {
    private int PORT_NUMBER;
    private static final String DONE = "DONE";
    private static Socket sock;
    private static ObjectOutputStream oos;
    private static ObjectInputStream ois;
    private static ServerSocket servsock;
    private static String baseDir;

    public static void sendFile(File dir) throws Exception {
        byte[] buff = new byte[sock.getSendBufferSize()];
        int bytesRead = 0;

        InputStream in = new FileInputStream(dir);

        while((bytesRead = in.read(buff))>0) {
            oos.write(buff,0,bytesRead);
        }
        in.close();
        // after sending a file you need to close the socket and reopen one.
        oos.flush();
        reinitConn();
    }

    public static void receiveFile(File dir) throws Exception {
        FileOutputStream wr = new FileOutputStream(dir);
        byte[] outBuffer = new byte[sock.getReceiveBufferSize()];
        int bytesReceived = 0;
        while((bytesReceived = ois.read(outBuffer))>0) {
            wr.write(outBuffer,0,bytesReceived);
        }
        wr.flush();
        wr.close();

        reinitConn();
    }
    public static void reinitConn() throws Exception {
        oos.close();
        ois.close();
        sock.close();
        sock = servsock.accept();
        oos = new ObjectOutputStream(sock.getOutputStream());
        ois = new ObjectInputStream(sock.getInputStream());
    }
}
