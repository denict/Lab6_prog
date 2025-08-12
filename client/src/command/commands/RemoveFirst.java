package command.commands;

import command.Command;
import network.Client;
import network.Request;
import utility.ConsoleOutput;

import java.io.Serial;
import java.io.Serializable;

/**
 * Команда "remove_first".
 * Описание команды: удалить первый элемент из коллекции
 */
public class RemoveFirst extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 64L;
    transient private final ConsoleOutput consoleOutput;
    transient private Client client;

    public RemoveFirst(ConsoleOutput consoleOutput, Client client) {
        super("remove_first", "удалить первый элемент из коллекции", 0, "");
        this.consoleOutput = consoleOutput;
        this.client = client;
    }

    /**
     * Выполнение команды.
     *
     * @param args аргументы
     */
    @Override
    public void execute(String[] args) throws InterruptedException, NullPointerException {
        consoleOutput.println(client.sendRequest(new Request(this)).getMessage());
    }
}
