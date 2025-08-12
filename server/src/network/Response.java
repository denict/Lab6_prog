//package network;
//
//public class Response {
//    private String result;
//
//
//    public Response(String result) {
//        this.result = result;
//    }
//
//    public String getResult() {
//        return result;
//    }
//}

package network;
import entity.Organization;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;

public class Response implements Serializable {

    @Serial
    private static final long serialVersionUID = 4L;

    private boolean success;
    private String message;

    public Response(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }


}