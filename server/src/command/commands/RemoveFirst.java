package command.commands;

import command.Command;
import managers.CollectionManager;
import network.Request;
import network.Response;
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
    private final CollectionManager collectionManager;
    private final ConsoleOutput consoleOutput;

    public RemoveFirst(CollectionManager collectionManager, ConsoleOutput consoleOutput) {
        super("remove_first", "удалить первый элемент из коллекции", 0, "");
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
        if (collectionManager.getCollection() == null || collectionManager.getCollection().isEmpty()) {
            return new Response(false, "Коллекция пуста. Нельзя удалить первый элемент.");
        }
        collectionManager.removeFirstElementOfCollection();
        return new Response(true, "Первый элемент коллекции был успешно удален!");
    }
}
