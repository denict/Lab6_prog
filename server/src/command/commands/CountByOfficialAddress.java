package command.commands;

import command.Command;
import entity.Organization;
import managers.CollectionManager;
import network.Request;
import network.Response;
import utility.ConsoleOutput;

import java.io.Serial;
import java.io.Serializable;

/**
 * Команда "count_by_official_address".
 * Описание команды: вывести количество элементов, у которых значение поля officialAddress равно заданному.
 */
public class CountByOfficialAddress extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 56L;
    transient private CollectionManager collectionManager;
    transient private ConsoleOutput consoleOutput;

    public CountByOfficialAddress(CollectionManager collectionManager, ConsoleOutput consoleOutput) {
        super("count_by_official_address", "вывести количество элементов, у которых значение поля officialAddress равно заданному", 2, "\"street\" \"zipCode\"");
    }

    /**
     * Выполнение команды.
     *
     * @param args аргументы
     */
    @Override
    public Response execute(Request request) {
        String street = ((String[])request.getArgs())[0];
        String zipCode = ((String[])request.getArgs())[1];
        if (CollectionManager.getCollection() == null) {
            return new Response(false, "Коллекция пустая, количество элементов с фиксированным \"street\" " + street + " и \"zipCode\" " + zipCode +  "равно 0");
        }
        // Использование StreamAPI и lambda exspression
        long count = CollectionManager.getCollection().stream().filter(org -> org.getOfficialAddress().getStreet().equals(street) && org.getOfficialAddress().getZipCode().equals(zipCode)).count();
        return new Response(true, "Количество элементов, у которых значение поля officialAddress (\"street\" = " + street + "; \"zipCode\" = " + zipCode + ") равно " + count);


    }


}
