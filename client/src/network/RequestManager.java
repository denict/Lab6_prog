package network;
import command.Command;
import managers.CommandManager;
import network.Client;
import utility.ConsoleInput;
import utility.ConsoleOutput;

import java.util.Arrays;
import java.util.NoSuchElementException;

public class RequestManager implements Runnable {
    private final ConsoleInput consoleInput;
    private final ConsoleOutput consoleOutput;
    private final CommandManager commandManager;
    private final Client client;

    public RequestManager(ConsoleInput consoleInput, ConsoleOutput consoleOutput, CommandManager commandManager, Client client) {
        this.consoleInput = consoleInput;
        this.consoleOutput = consoleOutput;
        this.commandManager = commandManager;
        this.client = client;
    }

    private void runCommand(String request, CommandManager commandManager, ConsoleOutput consoleOutput) throws InterruptedException {

            String[] requestArray = request.split(" ");
            String nameCommand = requestArray[0].toLowerCase();
        if (nameCommand.isBlank()) {
            consoleOutput.println("Пустая строка. Воспользуйтесь командой \"help\" для просморта доступных команд");
            return;
        }

        if (nameCommand.equals("exit")) {
            System.exit(0);
            client.disconnect();
        }

        if (!commandManager.getCommands().containsKey(nameCommand)) {
            consoleOutput.println("Команда \"" + nameCommand + "\" не найдена. Воспользуйтесь командой \"help\" для просморта доступных команд");
            return;
        }
        try {
            Command command = commandManager.getCommands().get(nameCommand);
            String[] args = Arrays.copyOfRange(requestArray, 1, requestArray.length);
//            if (command.getArgsCount() != args.length) {
//                consoleOutput.printError("Команда " + nameCommand + " принимает " + command.getArgsCount() + " аргумент(а)." + " Правильное использование: " + nameCommand + " " + command.getUsageArg());
//                return;
//            }

            command.execute(args); // sendRequest отправляется на сервер
            commandManager.addToHistory(commandManager.getCommands().get(nameCommand));
        } catch (NoSuchElementException e) {
            consoleOutput.println("^D");
        }

    }


    public void run() {

        // При завершении программы срабатывает addShutdownHook;
        // Создаётся новый поток, который будет выполнен.
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            consoleOutput.println("Завершение работы программы. До свидания!");
            client.disconnect();
        }));
//        consoleOutput.println("Здравствуйте! Для справки по доступным командам, нажмите \"help\".");
        while (true) {
            try {
                consoleOutput.print("> ");
                String request = consoleInput.readLine().trim();
                runCommand(request, commandManager, consoleOutput);
            } catch (NoSuchElementException e) {
                consoleOutput.println("Конец ввода."); // Ctrl+D
                break;
            } catch (Exception e) {
                consoleOutput.printError("Ошибка во время выполнения: " + e.getMessage());
                e.printStackTrace();
                break;
            }
        }
    }
}