package command.commands;

import command.Command;
import managers.CollectionManager;
import managers.DumpManager;
import utility.ConsoleOutput;

/**
 * Команда "save".
 * Описание команды: сохранить коллекцию в файл
 */
public class Save extends Command {
    private final CollectionManager collectionManager;
    private final DumpManager dumpManager;
    private final ConsoleOutput consoleOutput;

    public Save(CollectionManager collectionManager, DumpManager dumpManager, ConsoleOutput consoleOutput) {
        super("save", "сохранить коллекцию в файл", 0, "");
        this.collectionManager = collectionManager;
        this.dumpManager = dumpManager;
        this.consoleOutput = consoleOutput;
    }

    /**
     * Выполнение команды.
     *
     * @param args аргументы
     */
    @Override
    public void execute(String[] args) {
//        if (args.length != 0) {
//            consoleOutput.println("Команда не принимает аргументов!");
//            return;
//        }
        // FileNotFoundException обработан в DumpManager
        dumpManager.writeCollection(collectionManager.getCollection());
        if (collectionManager.getCollection().isEmpty()) {
            consoleOutput.println("Вы сохранили пустую коллекцию в " + dumpManager.getFile().getName());
        } else {
            consoleOutput.println("Коллекция успешно сохранена в " + dumpManager.getFile().getName());
        }
    }
}
