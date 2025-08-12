package command.commands;

import command.Command;
import network.Client;
import network.Request;
import utility.ConsoleOutput;

import java.io.Serial;
import java.io.Serializable;

/**
 * Команда "remove_by_id".
 * Описание команды: удалить элемент из коллекции по его id
 */
public class RemoveByID extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 63L;
    transient private ConsoleOutput consoleOutput;
    transient private Client client;

    public RemoveByID(ConsoleOutput consoleOutput, Client client) {
        super("remove_by_id", "удалить элемент из коллекции по его id", 1, "\"id\"");
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
        consoleOutput.println(client.sendRequest(new Request(this, args)).getMessage());
    }
}
