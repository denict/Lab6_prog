//package managers;
//
//import entity.Organization;
//
//import java.util.*;
//
///**
// * Класс для управления коллекцией {@code Organization}, включая добавление, удаление,
// * обновление, сортировку элементов, а также для работы с их идентификаторами и сохранения коллекции.
// */
//public class CollectionManager {
//    /**
//     * Коллекция элементов типа {@code Organization}.
//     */
//    private static Stack<Organization> collection = new Stack<Organization>();
//    /**
//     * Хеш-карта для быстрого доступа к элементам коллекции по их идентификаторам.
//     */
//    private static Map<Integer, Organization> organizationMap = new HashMap<Integer, Organization>();
//    private Date initTime;
//    private final DumpManager dumpManager;
//    private Organization minElement = null;
//
//    /**
//     * Конструктор для создания объекта {@code CollectionManager}.
//     *
//     * @param dumpManager Менеджер для сохранения коллекции.
//     */
//    public CollectionManager(DumpManager dumpManager) {
//        this.initTime = new Date();
//        this.dumpManager = dumpManager;
//    }
//
//    /**
//     * Статический метод для генерации нового id
//     * @return минимальный несуществующий id
//     */
//    public static int generateFreeId() {
//        if (collection.isEmpty()) return 1;
//        Set<Integer> keys = new HashSet<>(organizationMap.keySet()); // Копируем ключи, чтобы избежать ConcurrentModificationException
//        if (keys.isEmpty()) return 1;
//        int maxId = Collections.max(keys); // Находим максимальный ID
//        for (int i = 1; i < maxId; i++) {
//            if (!organizationMap.containsKey(i)) return i;
//        }
//        return Collections.max(organizationMap.keySet()) + 1;
//    }
//
//    /**
//     * Очищает коллекцию {@code collection} и карту {@code organizationMap}.
//     */
//    public void clearCollection() {
//        collection.clear();
//        organizationMap.clear();
//
//    }
//
//    /**
//     * Проверяет, все ли идентификаторы уникальны в переданной коллекции.
//     *
//     * @param collection Коллекция для проверки.
//     * @return {@code true}, если все идентификаторы уникальны, иначе {@code false}.
//     */
//    public static boolean allIdAreUnique(Collection<Organization> collection) {
//        HashSet<Integer> ids = new HashSet<>();
//        for (Organization org: collection) {
//            if (ids.contains(org.getId())) return false;
//            ids.add(org.getId());
//        }
//        return true;
//    }
//
//    /**
//     * Проверяет, существует ли элемент с указанным ID в коллекции.
//     *
//     * @param id ID элемента.
//     * @return {@code true}, если элемент существует, иначе {@code false}.
//     */
//    public static boolean hasIdInCollection(Integer id) {
//        return organizationMap.containsKey(id);
//    }
//
//    /**
//     * Получает минимальный элемент коллекции.
//     *
//     * @return Минимальный элемент коллекции.
//     */
//    public Organization getMinElement() {
//        return  minElement;
//    }
//
//
//    /**
//     * Обновляет минимальный элемент коллекции.
//     *
//     * @param org Новый элемент для обновления минимального элемента.
//     */
//    public  void updateMinElement(Organization org) {
//        if (minElement == null) {
//            minElement = org;
//            return;
//        }
//        if (minElement.compareTo(org) < 0) {
//            minElement = org;
//        }
//    }
//
//    /**
//     * Получение типа коллекции
//     * @return класс объекта коллекции
//     */
//    public String getTypeOfCollection() {
//        return collection.getClass().getName();
//    }
//
//    /**
//     * Возвращает размер коллекции.
//     *
//     * @return Размер коллекции.
//     */
//    public int getCollectionSize() {
//        return collection.size();
//    }
//
//
//    /**
//     * @return  время инициализации.
//     */
//    public Date getInitTime() {
//        return initTime;
//    }
//
//    /**
//     * @return коллекция.
//     */
//    public Stack<Organization> getCollection() {
//        return collection;
//    }
//    public Map<Integer, Organization> getOrganizationMap() { return organizationMap; }
//
//    /**
//     * @param id ID organization
//     * @return Элемент коллекции по ID, если существует, иначе null.
//     */
//    public Organization byId(Integer id) {
//        return organizationMap.get(id);
//    }
//
//    /**
//     * Проверка, содержит ли коллекция дракона
//     *
//     * @param organization элемент organization для проверки принадлежности к
//     *                     коллекции
//     * @return true, если коллекция содержит organization, иначе false.
//     */
//    public boolean isContain(Organization organization) {
//        return organizationMap.containsKey(organization.getId());
//    }
//
//    /**
//     * Сортировка коллекции collection элементов Organization по умолчанию
//     */
//    public void updateSort() {
//        Collections.sort(collection);
//    }
//
//    public void removeFirstElementOfCollection() {
//        collection.removeElementAt(0);
//    }
//    /**
//     * Добавляет элемент organization в коллекцию collection
//     * Добавляет элемент в HashMap для быстрого поиска
//     * Сортирует коллекцию по умолчанию
//     *
//     * @param organization добавляемый элемент
//     *
//     * @return true, если произошло добавление, иначе false
//     */
//    public boolean add(Organization organization) {
//        if (isContain(organization))
//            return false;
//        updateMinElement(organization);
//        organizationMap.put(organization.getId(), organization);
//        collection.push(organization);
//        updateSort();
//        return true;
//    }
//
//
//
//    /**
//     * По ID organization удаляет элемент из коллекции
//     *
//     * @param id ID удаляемого organization
//     * @return true, если произошло удаление, иначе false
//     */
//    public boolean remove(Integer id) {
//        Organization organization = byId(id);
//        if (organization == null)
//            return false;
//        organizationMap.remove(id);
//        collection.remove(organization);
//        return true;
//    }
//
//
//
//
//    /**
//     * Сохраняет коллекицию в CSV-файл
//     *
//     */
//    public void saveCollection() {
//        dumpManager.writeCollection(collection);
//
//    }
//
//    @Override
//    public String toString() {
//        if (collection.isEmpty())
//            return "Коллекция пуста!";
//        StringBuilder info = new StringBuilder();
//
//        for (Organization organization : collection) {
//            info.append(organization + "\n");
//        }
//
//        return info.toString().trim();
//    }
//
//}
