//package managers;
//
//
//import network.Request;
//import network.Response;
//import network.Server;
//import utility.InputHandler;
//import utility.OutputHandler;
//
//import java.io.IOException;
//import java.nio.channels.SocketChannel;
//import java.util.logging.Logger;
//

//import java.io.IOException;
//с

//**
// * Обрабатывает подключение клиента. Обрабатывает запрос клиента. Отправляет ответ на запрос клиенту.
// */
//public class ServerManager {
//    private final InputHandler consoleInput;
//    private final OutputHandler consoleOutput;
//    private final Server server;
//    private final CommandManager commandManager;
//    private final Logger serverLogger = Logger.getLogger("ServerLogger");
//
//    public ServerManager(InputHandler consoleInput, OutputHandler outputHandler, Server server, CommandManager commandManager, CollectionManager collectionManager) {
//        this.consoleInput = consoleInput;
//        this.consoleOutput = outputHandler;
//        this.server = server;
//        this.commandManager = commandManager;
//    }
//
//    public void start() {
//            server.start();
//    }
//
//    public void writeResponse(SocketChannel socketChannel, Response response) {
//            server.writeObject(socketChannel, response);
//    }
//
//    public void handlerSocketChannel(SocketChannel socketChannel)  {
//        Request request;
//        try {
//            request = (Request) server.getObject(socketChannel); //получаем запрос от клиента
//
//            //на основе запроса формируем ответ
//            Response response = new RequestHandler(commandManager).requestHandler(request);
//
//
//        } catch (IOException | ClassNotFoundException e) {
//            console.write(e.toString());
//            console.write("Принять данные не получилось");
//            socketChannel.close();
//        }
//        catch (ClassCastException e) {
//            console.write(e.toString());
//            console.write("Передан неправильный тип данных");
//        }
//        finally {
//            socketChannel.close();
//        }
//    }
//
//    public void run() {
//        SocketChannel socketChannel;
//        while (true) {
//            try {
//                socketChannel = server.getSocketChannel();
//                if (socketChannel == null) continue;
//                handlerSocketChannel(socketChannel);
//            } catch (IOException e) {
//                console.write(e.toString());
//            }
//        }
//    }
// }

//    if (filePath == null || filePath.split(";").length == 0) {
//        consoleOutput.println("Введите имя загружаемого файла в переменную среды. Пример: MY_FILE_PATH=\"C:\\Users\\user\\Desktop\\input.txt\"");
//        return;
//    } else if (filePath.split(";").length > 1) {
//        consoleOutput.printError("В переменной среды должен храниться 1 аргумент.");
//        return;
//    }




//public class ServerManager {
//    private String filePath = System.getenv("MY_FILE_PATH");
//    ConsoleInput consoleInput = new ConsoleInput();
//    public ServerManager(String filePath) {
//        this.filePath = filePath;
//
//    }
//
//
//    ConsoleOutput consoleOutput = new ConsoleOutput();
//
//    public void start() {
//        final Logger logger = Logger.getLogger("logger");
//        DumpManager dumpManager = new DumpManager(new File(filePath), consoleOutput);
//        if (!dumpManager.validate()) return;
//        CollectionManager collectionManager = new CollectionManager(dumpManager);
//        CommandManager commandManager = new CommandManager();
//        Server server = new Server("localhost", 5252, collectionManager);
//        server.run();
//    }
//}

package network;

import command.Command;
import managers.CollectionManager;
import managers.CommandManager;
import managers.DumpManager;
import utility.ConsoleInput;
import utility.ConsoleOutput;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Logger;


public class ServerRunnerManager implements Runnable{
    private final Server server;
    private final CommandManager commandManager;
    private final CollectionManager collectionManager;
    private final ConsoleInput consoleInput;
    private final ConsoleOutput consoleOutput;
    private final DumpManager dumpManager;
    private ServerSocketChannel serverSocketChannel;
    private static final Logger logger = Logger.getLogger(ServerRunnerManager.class.getName());
    private volatile boolean isRunning = true;

    BufferedInputStream input = new BufferedInputStream(System.in);
    BufferedReader scanner = new BufferedReader(new InputStreamReader(input));

    public ServerRunnerManager(Server server, CommandManager commandManager, DumpManager dumpManager, CollectionManager collectionManager, ConsoleInput consoleInput,ConsoleOutput consoleOutput) {
        this.server = server;
        this.commandManager = commandManager;
        this.dumpManager = dumpManager;
        this.collectionManager = collectionManager;
        this.consoleInput = consoleInput;
        this.consoleOutput = consoleOutput;
    }



    private void processClientRequest(SocketChannel clientSocket) {
        Request userRequest;
        Response responseToUser;
        try (ObjectInputStream clientReader = new ObjectInputStream(clientSocket.socket().getInputStream());
             ObjectOutputStream clientWriter = new ObjectOutputStream(clientSocket.socket().getOutputStream())) {
            userRequest = (Request) clientReader.readObject();

            if (!commandManager.getCommands().containsKey(userRequest.getCommand().getName())) {
                logger.warning("Команда " + userRequest.getCommand().getName() + " не найдена");
                clientWriter.writeObject(new Response(false, "Команда " + userRequest.getCommand().getName() + " не найдена"));
                return;
            } else {
                if (userRequest.getArgs() instanceof String[]) {
                    String[] args = (String[]) userRequest.getArgs();
                    if (args.length != commandManager.getCommand(userRequest.getCommand().getName()).getArgsCount()) {
                        logger.warning("Был запрос на команду " + userRequest.getCommand().getName() + " с количеством аргументов="+args.length+"\n" +
                                "ожидается " + userRequest.getCommand().getArgsCount() + " аргумент(ов)");
                        clientWriter.writeObject(new Response(false, "Ошибка: Неверное количество аргументов\n" +
                                "ожидается " + userRequest.getCommand().getArgsCount() + " аргумент(ов)"));
                        return;
                    }
                } else if (userRequest.getArgs() instanceof String) {
                    String arg = (String) userRequest.getArgs();
                    if (1 == commandManager.getCommand(userRequest.getCommand().getName()).getArgsCount()) {
                        logger.warning("Был запрос на команду " + userRequest.getCommand().getName() + " с количеством аргументов=1"+"\n" +
                                "ожидается " + userRequest.getCommand().getArgsCount() + " аргумент(ов)");
                        clientWriter.writeObject(new Response(false, "Ошибка: неверное количество аргументов=1\n" +
                                "ожидается " + userRequest.getCommand().getArgsCount() + " аргумент(ов)"));
                        return;
                    }
                }
            }
            logger.info("Запрос с командой " + userRequest.getCommand().getName());
            if (CommandManager.getCommand(userRequest.getCommand().getName()) == null) {
                consoleOutput.printError("Команда " + userRequest.getCommand().getName() + " не найдена");
                logger.warning("Команда " + userRequest.getCommand().getName() + " не найдена");
                clientWriter.writeObject(new Response(false, "Команда " + userRequest.getCommand().getName() + " не найдена"));
                return;
            }
            responseToUser = CommandManager.getCommand(userRequest.getCommand().getName()).execute(userRequest); // Вызов команды
            commandManager.addToHistory(userRequest.getCommand());
            clientWriter.writeObject(responseToUser); // Отправка Response
            logger.info("Отправлен ответ " + responseToUser.getMessage());
            clientWriter.flush();

        } catch (ClassNotFoundException | InvalidClassException | NotSerializableException e) {
            consoleOutput.printError("Ошибка при взаимодействии с клиентом!\n" + e.getMessage());
            e.printStackTrace();
            logger.warning("Ошибка при взаимодействии с клиентом!\n" + e.getMessage());
        } catch (IOException e) {
            consoleOutput.printError("Ошибка ввода вывода+\n" + e.getMessage());
            e.printStackTrace();
            logger.warning("Ошибка ввода вывода");
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                logger.warning("Ошибка при закрытии клиентского сокета");
            }
        }
    }


    public void run() {
        server.start();
        consoleOutput.println("Здравствуйте! Для справки по доступным командам, нажмите \"help\".");
        // При завершении программы срабатывает addShutdownHook;
        // Создаётся новый поток, который будет выполнен.
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                dumpManager.writeCollection(collectionManager.getCollection());
                consoleOutput.println("Завершение работы программы. До свидания!");
                logger.info("Завершение работы программы. До свидания!");
                server.stop();
                logger.info("Сервер остановлен.");
        }));

        Thread inputThread = new Thread(() -> {
            try {
                // Сделать в отдельном потоке
                while (isRunning) {
                    String line = scanner.readLine();
                    if (line.equals("save")) {
                        dumpManager.writeCollection(collectionManager.getCollection());
                        logger.info("Объекты коллекции Organization сохранены в файл " + dumpManager.getFile().getName());
                    } else if (line.equals("exit")) {
                        logger.info("Была введена команда exit.");
                        isRunning = false;
                    }
                }
            } catch (IOException e) {
                logger.warning("Ошибка ввода-вывода");
                e.printStackTrace();
            }
        });
        inputThread.setDaemon(true);
        inputThread.start();

        while (isRunning) {
            try {
                SocketChannel clientSocketChannel = server.getServerSocketChannel().accept(); // соединение устойчивое

                if (clientSocketChannel != null && clientSocketChannel.isOpen()) {
                    logger.info("Соединение с клиентом установлено.");
                    processClientRequest(clientSocketChannel);
                }

            } catch (IOException e) {
                logger.severe("Ошибка в основном цикле сервера: " + e.getMessage());
            } catch (NoSuchElementException e) {
                logger.info("Конец ввода."); //Ctrl + D
                break;
            } catch (NotYetBoundException e) {
                logger.warning("Порт уже занят");
            }
        }
    }


//    private void closeChannel(SocketChannel clientChannel) {
//        try {
//            clientChannel.close();
//            logger.info("Соединение с клиентом закрыто");
//        } catch (IOException e) {
//            logger.warning("Ошибка при закрытии соединения: " + e.getMessage());
//        }
//    }

}
