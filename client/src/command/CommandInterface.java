package command;



/**
 * Интерфейс для Command, реализующий метод execute
 */
public interface CommandInterface {

    void execute(String[] args) throws InterruptedException;

}
