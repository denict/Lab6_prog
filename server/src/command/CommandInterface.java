package command;

import network.Request;
import network.Response;

/**
 * Интерфейс для Command, реализующий метод execute
 */
public interface CommandInterface {


    Response execute(Request request);

}
