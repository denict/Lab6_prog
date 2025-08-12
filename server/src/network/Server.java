//У меня до этого были наработки по поводу класса Server (ServerApp), который принимает запросы от клиента. В условии сказан однопоточный режим, нужно ли вообще использовать Selector? Какая от них польза? И что будет, если использовать без них
//
// Класс Server
//        package network;
//
//import utility.ConsoleInput;
//import utility.InputHandler;
//
//import java.io.*;
//import java.net.InetSocketAddress;
//import java.nio.channels.ServerSocketChannel;
//import java.nio.channels.SocketChannel;
//import java.util.logging.Logger;
//
//
//**
// * Класс сервера, позволяет принимать запросы от клиента
// */
//public class Server {
//    private final String host;
//    private final int port;
//    private ServerSocketChannel serverSocketChannel;
//    final Logger serverLogger = Logger.getLogger("ServerLogger");
//
//    public Server(String host, int port) {
//        this.host = host;
//        this.port = port;
//    }
//
//    /**
//     * Запускает сервер на прослушку на заданном хосте и порту.
//     * @throws IOException если возникла ошибка при открытии канала для сервера
//     */
//    public void start() {
//        try {
//            serverSocketChannel = ServerSocketChannel.open();
//            serverSocketChannel.configureBlocking(false);
//            serverSocketChannel.bind(new InetSocketAddress(host, port));
//        } catch (IOException e) {
//            serverLogger.warning("Произошла ошибка при попытке использовать порт" + port + " для сервера.");
//        }
//    }
//
//    /**
//     * Метод для получения канала сокета.
//     *
//     * @return новый канал сокета, если клиент подключился, иначе null.
//     * @throws IOException если произошла ошибка ввода-вывода.
//     */
//    public SocketChannel getSocketChannel() {
//        try {
//            return serverSocketChannel.accept();
//        } catch (IOException e) {
//            serverLogger.warning("Не удалось создать сокет");
//        }
//        return null;
//    }
//
//    public Object getObject(SocketChannel socketChannel)  {
//        try {
//            ObjectInputStream ois = new ObjectInputStream(socketChannel.socket().getInputStream());
//            return ois.readObject();
//        } catch (IOException | ClassNotFoundException e) {
//            serverLogger.warning("Произошла ошибка при чтении объекта");
//        }
//        return null;
//    }
//
//    public void writeObject(SocketChannel socketChannel, Object obj)  {
//        try {
//            ObjectOutputStream oos = new ObjectOutputStream(socketChannel.socket().getOutputStream());
//            oos.writeObject(obj);
//        } catch (IOException e) {
//            serverLogger.warning("Произошла ошибка при записи объекта");
//        }
//
//    }
//
//    public void close() {
//        try {
//            serverSocketChannel.close();
//        } catch (IOException e) {
//            serverLogger.warning("Произошла ошибка при закрытии сокета");
//        }
//    }
//}

//package network;
//import managers.CollectionManager;
//import managers.CommandManager;
//import java.io.*;
//import java.net.InetSocketAddress;
//import java.nio.channels.*;
//import java.util.logging.Logger;
///**
// * Класс сервера, позволяет принимать запросы от клиента
// */
//public class Server {
//    private String host;
//    private int port;
//    private int timeout;
//    private int maxReconnectionAttempts;
//    private int reconnectionAttempts;
//    private CollectionManager collectionManager;
//    ServerSocketChannel serverSocket;
//    private static final Logger logger = Logger.getLogger("Serverlogger");
//
//
//    BufferedInputStream input = new BufferedInputStream(System.in);
//    BufferedReader scanner = new BufferedReader(new InputStreamReader(input));
//
//    public Server(String host, int port, CollectionManager collectionManager) {
//        this.host = host;
//        this.port = port;
//        this.collectionManager = collectionManager;
//
//    }
//
//
//    public void openServer() {
//        try {
//            serverSocket = ServerSocketChannel.open();
//            serverSocket.bind(new InetSocketAddress(host,port));
//            serverSocket.configureBlocking(false);
//        } catch (IOException e) {
//            logger.warning("Ошибка при попытке подключиться к порту");
//        }
//
//    }
//
//    private void processClientRequest(SocketChannel clientSocket) {
//        Request userRequest;
//        Response responseToUser;
//        try (ObjectInputStream clientReader = new ObjectInputStream(clientSocket.socket().getInputStream());
//             ObjectOutputStream clientWriter = new ObjectOutputStream(clientSocket.socket().getOutputStream())) {
//            userRequest = (Request) clientReader.readObject();
//            logger.info("Запрос с командой " + userRequest.getCommand().getName());
//            //  GOTO
//            responseToUser = CommandManager.getCommand(userRequest.getCommand().getName()).execute(userRequest); //GOTO
//            clientWriter.writeObject(responseToUser);
//            logger.info("Отправлен ответ " + responseToUser.getResult());
//            clientWriter.flush();
//
//        } catch (ClassNotFoundException | InvalidClassException | NotSerializableException exception) {
//            exception.printStackTrace();
//            logger.warning("Ошибка при взаимодействии с клиентом!");
//        } catch (IOException exception) {
//            exception.printStackTrace();
//            logger.warning("Ошибка ввода-вывода");
//        } finally {
//            try {
//                clientSocket.close();
//            } catch (IOException e) {
//                logger.warning("Ошибка при закрытии клиентского сокета");
//            }
//        }
//    }
//
//    public void run() {
//        try {
//            openServer();
//            logger.info("Соединение с сервером найдено");
//            Thread inputThread = new Thread(() -> {
//                try {
//                    while (true) {
//                        String line = scanner.readLine();
//                        if (line.equals("save")) {
//                            collectionManager.saveCollection(); // Сохраняем в файл
//                            logger.info("Объекты коллекции Organization сохранены");
//                        }
//                    }
//                } catch (IOException e) {
//                    logger.warning("Ошибка ввода");
//                }
//            });
//            inputThread.setDaemon(true);
//            inputThread.start();
//
//            while (true) {
//                SocketChannel socketClient = serverSocket.accept();
//                if (socketClient != null) {
//                    processClientRequest(socketClient);
//                }
//            }
//        }catch (IOException exception) {
//            logger.warning("Ошибка при запуске");
//        }catch(NotYetBoundException exception) {
//            logger.warning("Порт уже занят");
//        }
//    }
//}

package network;

import utility.ConsoleOutput;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.logging.Logger;

public class Server {
    private String host;
    private final int port;
    private ServerSocketChannel serverSocketChannel;
    private static final Logger logger = Logger.getLogger(Server.class.getName());
    private final ConsoleOutput consoleOutput;

    public Server(String host,int port, ConsoleOutput consoleOutput) {
        this.host = host;
        this.port = port;
        this.consoleOutput = consoleOutput;
    }

    public void start()  {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(host, port));
            serverSocketChannel.configureBlocking(false);
            logger.info("Сервер запущен на хосте " + host + " по порту " + port);
        } catch (IOException e) {
            logger.warning("Произошла ошибка при запуске сервера: " + e.getMessage());
        }
    }

    public void stop() {
        try {
            if (serverSocketChannel != null) {
                serverSocketChannel.close();
                logger.info("serverSocketChannel закрыт");
            }
        } catch (IOException e) {
            logger.warning("Ошибка при остановке сервера: " + e.getMessage());
        }
    }

    public ServerSocketChannel getServerSocketChannel() {
        return serverSocketChannel;
    }
    public String getHost() {
        return host;
    }
    public int getPort() {
        return port;
    }
}