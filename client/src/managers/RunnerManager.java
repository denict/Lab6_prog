//package managers;
//import command.Command;
//import utility.InputHandler;
//import utility.OutputHandler;
//
//import java.util.Arrays;
//import java.util.NoSuchElementException;
//
//public class RunnerManager implements Runnable{
//    private final InputHandler consoleInput;
//    private final OutputHandler consoleOutput;
//    private final CommandManager commandManager;
//    private final DumpManager dumpManager;
//
//    public RunnerManager(InputHandler consoleInput, OutputHandler consoleOutput, CommandManager commandManager, DumpManager dumpManager) {
//        this.consoleInput = consoleInput;
//        this.consoleOutput = consoleOutput;
//        this.commandManager = commandManager;
//        this.dumpManager = dumpManager;
//    }
//
//    /**
//     * Проверяет корректность введенной команды и выполняет ее.
//     *
//     * @param requestParts     Разделенная на части введенная строка команды.
//     * @param commandManager Менеджер команд.
//     * @param consoleOutput  Обработчик вывода для отображения сообщений.
//     */
//    public static void runCommand(String[] requestParts, CommandManager commandManager, OutputHandler consoleOutput) {
//        String requestCommandName = requestParts[0].toLowerCase();
//        if (requestCommandName.isBlank()) {
//            consoleOutput.println("Вы ничего не ввели. Воспользуйтесь командой \"help\" для просморта доступных команд");
//            return;
//
//        }
//        String[] queryCommandArgs = Arrays.copyOfRange(requestParts, 1, requestParts.length);
//        if (!commandManager.getCommands().containsKey(requestCommandName)) {
//            consoleOutput.printError("Команда \"" + requestCommandName + "\" не найдена. Воспользуйтесь командой \"help\" для просморта доступных команд");
//            return;
//        }
//
//        try {
//            Command command = commandManager.getCommands().get(requestCommandName);
//            if (command.getArgsCount() != queryCommandArgs.length) {
//                consoleOutput.printError("Команда " + requestCommandName + " принимает " + command.getArgsCount() + " аргумент(а)." + " Правильное использование: " + requestCommandName + " " + command.getUsageArg());
//                return;
//            }
////            command.execute(queryCommandArgs);
//            commandManager.addToHistory(commandManager.getCommands().get(requestCommandName));
//        } catch (NoSuchElementException e) {
//            consoleOutput.println("^D");
//        } catch (Exception e) {
//            consoleOutput.printError("Произошла ошибка во время выполнения команды \"" + requestCommandName + "\": " + e.getMessage());
//        }
//    }
//
//
//    /**
//     * Запускает главный цикл программы, обрабатывая пользовательский ввод и выполняя команды.
//     * Включает обработку выхода из программы, добавляя {@code ShutdownHook}, который выполняется при завершении работы.
//     */
//    @Override
//    public void run() {
//
//        // При завершении программы срабатывает addShutdownHook;
//        // Создаётся новый поток, который будет выполнен.
//        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//            consoleOutput.println("Завершение работы программы. До свидания!");
//        }));
//
//        consoleOutput.println("Здравствуйте! Для справки по доступным командам, нажмите \"help\".");
//
//        consoleOutput.println("Работа с файлом: " + dumpManager.getFile().getName());
//
//        while (true) {
//            try {
//                String query = consoleInput.readLine().trim();
//                String[] queryParts = query.split(" ");
//                runCommand(queryParts, commandManager, consoleOutput);
//            } catch (NoSuchElementException e) {
//                consoleOutput.println("Конец ввода."); // Ctrl+D
//                break;
//            } catch (Exception e) {
//                consoleOutput.printError("Ошибка во время выполнения: " + e.getMessage());
//                break;
//            }
//        }
//    }
//
//    @Override
//    public int hashCode() {
//            return super.hashCode();
//    }
//}
