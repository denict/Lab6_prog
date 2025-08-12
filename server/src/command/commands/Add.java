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
import java.util.Date;

/**
 * Команда "add".
 * Описание команды: добавление нового элемента в коллекцию.
 */
public class Add extends Command implements Serializable {

    @Serial
    private static final long serialVersionUID = 53L;

    private CollectionManager collectionManager;
    private ConsoleOutput consoleOutput;

    public Add(CollectionManager collectionManager, ConsoleInput consoleInput, ConsoleOutput consoleOutput) {
        super("add", "добавить новый элемент в коллекцию", 0, "");
        this.collectionManager = collectionManager;
        this.consoleOutput = consoleOutput;
    }

    /**
     * Выполнение команды.
     *
     * @param request аргументы
     */
    @Override
    public Response execute(Request request) {
        try {
            Organization org = request.getOrganization();
            if (request.getOrganization().validate()) {
                org.setId(CollectionManager.generateFreeId());
                org.setCreationDate(new Date());
                collectionManager.add(request.getOrganization());
                return new Response(true, "Объект успешно создан и добавлен в коллекцию");
            } else {
                return new Response(false, "Объект не прошел валидацию, проверьте корректность данных");
            }
        } catch (Exception e) {
            consoleOutput.printError(e.getMessage());
        }
        return null;
    }
}
