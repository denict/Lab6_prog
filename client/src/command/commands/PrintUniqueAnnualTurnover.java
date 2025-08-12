package command.commands;

import command.Command;
import entity.Organization;
import network.Client;
import network.Request;
import utility.ConsoleOutput;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;

/**
 * Команда "print_unique_annual_turnover".
 * Описание команды: вывести уникальные значения поля annualTurnover всех элементов в коллекции
 */
public class PrintUniqueAnnualTurnover extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 62L;
    transient private ConsoleOutput consoleOutput;
    transient private Client client;

    public PrintUniqueAnnualTurnover(ConsoleOutput consoleOutput, Client client) {
        super("print_unique_annual_turnover", "вывести уникальные значения поля annualTurnover всех элементов в коллекции", 0, "");
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
