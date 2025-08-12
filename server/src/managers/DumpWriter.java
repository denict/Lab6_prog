package managers;

import entity.Organization;

import java.util.Collection;

/**
 * Интерфейс, определяющий методы для записи коллекции в файл.
 */
public interface DumpWriter {

    String fromCollectionToCSV(Collection<Organization> collection);

    void writeCollection(Collection<Organization> collection);


}
