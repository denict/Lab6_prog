
package network;
import command.Command;
import entity.Organization;

import java.io.Serial;
import java.io.Serializable;

public class Request implements Serializable {
    @Serial
    private static final long serialVersionUID = 5L;
    private Organization organization;
    private Command command;
    private Object args;

    public Request(Command command) {
        this.command = command;
    }
    public Request(Organization organization, Command command, Object args) {
        this.organization = organization;
        this.command = command;
        this.args = args;
    }
    public Request(Organization organization, Command command) {
        this.organization = organization;
        this.command = command;
    }

    public Request(Command command, Object args) {
        this.command = command;
        this.args = args;
    }

    public Request(Organization organization) {
        this.organization = organization;
    }

    public Request (String args) {
        this.args = args;
    }
    public Command getCommand() {
        return command;
    }
    public Organization getOrganization() {
        return organization;
    }
    public Object getArgs() {
        return args;
    }
}