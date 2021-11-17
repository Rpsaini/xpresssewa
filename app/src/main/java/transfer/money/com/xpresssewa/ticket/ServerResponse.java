package transfer.money.com.xpresssewa.ticket;

public class ServerResponse
{
    private String Message;
    private boolean status;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
