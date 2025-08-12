package command.commands;

import command.Command;
import managers.CommandManager;
import network.Request;
import network.Response;
import utility.ConsoleOutput;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * Команда "help".
 * Описание команды: вывод справки о доступных командах.
 */
public class Help extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 59L;
    private CommandManager commandManager;
    private ConsoleOutput consoleOutput;

    public Help(CommandManager commandManager, ConsoleOutput consoleOutput) {
        super("help", "вывод справки о доступных командах", 0, "");
        this.commandManager = commandManager;
        this.consoleOutput = consoleOutput;
    }

    /**
     * Выполнение команды.
     *
     * @param args аргументы
     */
    public Response execute(Request request) {
        StringBuilder sb = new StringBuilder("");
        sb.append("Краткая справка по всем доступным командам.\n");

        // Stream API
        commandManager.getCommands().entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry ->
                        sb.append(entry.getKey() + " - " + entry.getValue().getDescription() + "\n")
                );
        return new Response(true, sb.toString());
//        for (Map.Entry<String, Command> entry : commandManager.getCommands().entrySet()) {
//            String commandName = entry.getKey();
//            Command command = entry.getValue();
//            consoleOutput.println(commandName + " - " + command.getDescription());
//        }
    }
}

