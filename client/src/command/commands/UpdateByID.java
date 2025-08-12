package command.commands;

import command.Command;
import entity.Organization;
import entity.builders.OrganizationBuilder;
import network.Client;
import network.Request;
import utility.ConsoleInput;
import utility.ConsoleOutput;

import java.io.Serial;
import java.io.Serializable;

/**
 * Команда "update_by_id".
 * Описание команды: обновить значение элемента коллекции, id которого равен заданному
 */
public class UpdateByID extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 65L;
    transient private ConsoleInput consoleInput;
    transient private ConsoleOutput consoleOutput;
    transient private Client client;


    public UpdateByID(ConsoleInput consoleInput, ConsoleOutput consoleOutput, Client client) {
        super("update_by_id", "обновить значение элемента коллекции, id которого равен заданному", 1, "\"id\"");
        this.consoleInput = consoleInput;
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
        var organization = new OrganizationBuilder(consoleInput, consoleOutput).build();
        consoleOutput.println(client.sendRequest(new Request(this, organization, args)).getMessage());
    }
}
