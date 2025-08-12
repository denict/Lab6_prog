package command.commands;

import command.Command;
import managers.CommandManager;
import network.Client;
import network.Request;
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
    transient private ConsoleOutput consoleOutput;
    transient private Client client;

    public Help(ConsoleOutput consoleOutput, Client client) {
        super("help", "вывод справки о доступных командах", 0, "");
        this.consoleOutput = consoleOutput;
        this.client = client;
    }

    @Override
    public void execute(String[] args) throws InterruptedException, NullPointerException {
        consoleOutput.println(client.sendRequest(new Request(this)).getMessage());
    }
}

