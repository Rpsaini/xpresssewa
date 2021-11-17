package transfer.money.com.xpresssewa.Bean;

public class DynamicFormBean
{
    private String id;
    private String hint;
    private String inputtype;
    private String maxLength;

    public DynamicFormBean(String id, String hint, String inputtype, String maxLength) {
        this.id = id;
        this.hint = hint;
        this.inputtype = inputtype;
        this.maxLength = maxLength;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint)
    {
        this.hint = hint;
    }








}
