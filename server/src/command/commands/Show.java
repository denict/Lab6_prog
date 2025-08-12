package command.commands;

import command.Command;
import entity.Organization;
import managers.CollectionManager;
import network.Request;
import network.Response;
import utility.ConsoleOutput;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Команда "show".
 * Описание команды: выводит в стандартный поток вывода все элементы коллекции в строковом представлении
 */
public class Show extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 67L;
    private CollectionManager collectionManager;
    private ConsoleOutput consoleOutput;

    public Show(CollectionManager collectionManager, ConsoleOutput consoleOutput) {
        super("show", "выводит в стандартный поток вывода все элементы коллекции в строковом представлении", 0, "");
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
        if (collectionManager.getCollectionSize() == 0) {
            return new Response(false, "Коллекция пустая, добавьте элементы");
        }
        StringBuilder sb = new StringBuilder("");
        sb.append("Количество элементов: " + collectionManager.getCollectionSize() + "\n");
        int cou = 1;
        // StreamAPI
        Collection <Organization> collection = collectionManager.getCollection().stream().sorted(Comparator.comparing(Organization::getName)).collect(Collectors.toList());
        for (Organization org : collection) {
            sb.append(cou++ + ": " + org.toString() + "\n");
        }
        return new Response(true, sb.toString());
    }
}
