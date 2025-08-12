package network;

import utility.ConsoleOutput;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Objects;

public class Client implements Serializable {

    private final String host;
    private final int port;
    private final int timeout;
    private final int maxReconnectionAttempts;
    private SocketChannel socketChannel;
    private ObjectOutputStream writer;
    private ObjectInputStream reader;
    private int reconnectionAttempts;
    private ConsoleOutput consoleOutput;

    public Client(String host, int port, int timeout, int maxReconnectionAttempts, ConsoleOutput consoleOutput) {
        this.host = host;
        this.port = port;
        this.timeout = timeout;
        this.maxReconnectionAttempts = maxReconnectionAttempts;
        this.consoleOutput = consoleOutput;
    }


    public void connect() {
        try {
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(true);
            socketChannel.connect(new InetSocketAddress(host,port));
            // Ожидание завершения подключения
//            while (!socketChannel.finishConnect()) {
//                Thread.sleep(100); // Небольшая задержка для проверки подключения
//            }
            writer = new ObjectOutputStream(socketChannel.socket().getOutputStream());
            reader = new ObjectInputStream(socketChannel.socket().getInputStream());
        }catch (IOException e) {
            System.err.println("Ошибка подключения к серверу");
        }
    }


    public void disconnect() {
        try {
            if (socketChannel != null && socketChannel.isOpen()) {
                socketChannel.close();
            }
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
        }catch (IOException e) {
            System.err.println("Ошибка при закрытии соединения" + e.getMessage());
        }
    }


    public Response sendRequest(Request request) throws InterruptedException {
        this.connect();
        for (int reconnectionAttempts = 0; reconnectionAttempts <= maxReconnectionAttempts; reconnectionAttempts++) {
            try {
                if (Objects.isNull(writer) || Objects.isNull(reader)) throw new IOException();
                writer.writeObject(request);
                writer.flush();
                Response response = (Response) reader.readObject();
                return response;

            }catch (IOException e) {
                if (reconnectionAttempts >= maxReconnectionAttempts) {
                    System.out.println("Не удалось подключиться к серверу за " + maxReconnectionAttempts + " попыток.");
                    System.exit(0);
                }
                System.err.println("Переподключение через: " +timeout/1000 + " секунд");
                Thread.sleep(timeout);
                this.connect();
            }
            catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (NullPointerException e) {
                return new Response(false, "Ошибка: null pointer exception");
            }
        }
        return null;
    }
}