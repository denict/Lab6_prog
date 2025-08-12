package command.commands;

import command.Command;
import managers.CollectionManager;
import network.Request;
import network.Response;
import utility.ConsoleOutput;

import java.io.Serial;
import java.io.Serializable;

/**
 * Команда "clear".
 * Описание команды: очистить коллекцию.
 */
public class Clear extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 55L;
    private final CollectionManager collectionManager;
    private final ConsoleOutput consoleOutput;

    public Clear(CollectionManager collectionManager, ConsoleOutput consoleOutput) {
        super("clear", "очистить коллекцию", 0, "");
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
            if (collectionManager.getCollectionSize() == 0) {
                return new Response(false, "Коллекция пустая");
            }
            collectionManager.clearCollection();
            return new Response(true, "Коллекция успешно очищена.");
        } catch (Exception e) {
            consoleOutput.printError(e.getMessage());
            return new Response(false, "Произошла ошибка во время выполнения команды \"" + getName() + "\": " + e.getMessage());
        }
    }
}
