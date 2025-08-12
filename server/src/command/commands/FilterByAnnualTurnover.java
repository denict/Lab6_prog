package command.commands;

import command.Command;
import entity.Organization;
import managers.CollectionManager;
import network.Request;
import network.Response;
import utility.ConsoleOutput;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Команда "filter_by_annual_turnover".
 * Описание команды: вывести элементы, значение поля annualTurnover которых равно заданному.
 */
public class FilterByAnnualTurnover extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 58L;
    transient private CollectionManager collectionManager;
    transient private ConsoleOutput consoleOutput;

    public FilterByAnnualTurnover(CollectionManager collectionManager, ConsoleOutput consoleOutput) {
        super("filter_by_annual_turnover", "вывести элементы, значение поля annualTurnover которых равно заданному", 1, "\"annualTurnover\"");
        this.collectionManager = collectionManager;
        this.consoleOutput = consoleOutput;
    }

    /**
     * Выполнение команды.
     *
     * @param args аргументы
     */
    @Override
    public Response execute(Request request) {
        try {
        Double annualTurnover = Double.parseDouble(((String[])request.getArgs())[0]);
        if (CollectionManager.getCollection() == null) {
//            consoleOutput.println("Коллекция пустая! Элементов с фиксированным annualTurnover нет!");
            return new Response(false, "Коллекция пустая! Элементов с фиксированным annualTurnover нет!");
        }


            // StreamAPI
            List<Organization> filtered = collectionManager.getCollection().stream().filter(org -> org.getAnnualTurnover().equals(annualTurnover)).toList();
            int count = 1;
            StringBuilder sb = new StringBuilder("");
            for (Organization org : filtered) {
                sb.append(count++ + ": " + org.toString() + "\n");
            }
            if (filtered.isEmpty()) {
                return new Response(false, "Нет элементов, значение поля annualTurnover которых равно " + annualTurnover);
//                consoleOutput.println("Нет элементов, значение поля annualTurnover которых равно " + annualTurnover);
            }
            return new Response(true, sb.toString());
        } catch (NumberFormatException e) {
//            consoleOutput.printError("Введите число типа Double");
            return new Response(false, "Введите число типа Double");
        }
    }
}
