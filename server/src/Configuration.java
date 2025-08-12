package server;

/**
 * Класс конфигураций сервера (порт, хост,
 * название файла со стартовым состоянием коллекции, название файла с историей состояний)
 */
public class Configuration {
    private static String host;
    private static int port;
    private static String startFileName;

    public static String getHost() {
        return host;
    }

    public static void setHost(String host) {
        Configuration.host = host;
    }

    public static int getPort() {
        return port;
    }

    public static void setPort(int port) {
        Configuration.port = port;
    }

    public static String getStartFileName() {
        return startFileName;
    }

    public static void setStartFileName(String startFileName) {
        Configuration.startFileName = startFileName;
    }


}
