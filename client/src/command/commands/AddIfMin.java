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
 * Команда "add_if_min".
 * Описание команды: добавить новый элемент, если его значение меньше, чем у наименьшего элемента коллекции.
 */
public class AddIfMin extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 54L;
    transient private ConsoleInput consoleInput;
    transient private ConsoleOutput consoleOutput;
    transient private Client client;



    public AddIfMin(ConsoleInput consoleInput, ConsoleOutput consoleOutput,Client client) {
        super("add_if_min", "добавить элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции", 0, "");
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
        Organization organization = new OrganizationBuilder(consoleInput, consoleOutput).build();
        consoleOutput.println(client.sendRequest(new Request(this, organization)).getMessage());
    }
}
