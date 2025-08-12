package command.commands;

import command.Command;
import entity.Organization;
import entity.builders.OrganizationBuilder;
import managers.CollectionManager;
import network.Request;
import network.Response;
import utility.ConsoleInput;
import utility.ConsoleOutput;

import java.io.Serial;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.stream.Stream;

/**
 * Команда "add_if_min".
 * Описание команды: добавить новый элемент, если его значение меньше, чем у наименьшего элемента коллекции.
 */
public class AddIfMin extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 54L;
    private CollectionManager collectionManager;
    private ConsoleOutput consoleOutput;

    public AddIfMin(CollectionManager collectionManager, ConsoleOutput consoleOutput) {
        super("add_if_min", "добавить элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции", 0, "");
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
        Organization org = request.getOrganization();
        if (!org.validate()) {
            return new Response(false, "Объект не прошел валидацию, проверьте корректность данных");
        }

        org.setCreationDate(new Date());
        org.setId(CollectionManager.generateFreeId());

        if (collectionManager.getCollection() == null) {
//            consoleOutput.println("Коллекция пустая, поэтому можно добавить любой элемент");
            collectionManager.add(org);
            collectionManager.updateMinElement();
//            consoleOutput.println("Элемент успешно добавлен в коллекцию! Минимальный элемент коллекции обновлён.");
            return new Response(true, "Элемент успешно добавлен в коллекцию,коллекция и так пустая! Минимальный элемент коллекции обновлён.");
        } else {
            // Находим минимальный элемент по имени
            Organization min = collectionManager.getCollection().stream().min(Comparator.comparing(or -> or.getAnnualTurnover())).orElse(null);
            if (Stream.of(org).anyMatch(o -> o.getAnnualTurnover() < min.getAnnualTurnover())) {
                collectionManager.add(org);
                collectionManager.setMinElement(org);
                return new Response(true, "Элемент успешно добавлен в коллекцию! Минимальный элемент коллекции обновлён.");
            } else {
//                consoleOutput.println("Элемент не добавлен в коллекцию, потому он больше минимального элемента этой коллекции:" + collectionManager.getMinElement());
                return new Response(false, "Элемент не добавлен в коллекцию, потому он больше минимального элемента этой коллекции: " + collectionManager.getMinElement());
            }
        }
    }
}
