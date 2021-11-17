package transfer.money.com.xpresssewa.Model;

public class PriceComparisonResponse {
    private String id;
    private String img_name;
    private String price;
    private String difference;
    private String info;

    public String getDifference() {
        return difference;
    }

    public void setDifference(String difference) {
        this.difference = difference;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImg_name(String img_name) {
        this.img_name = img_name;
    }

    public String getImg_name() {
        return img_name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
