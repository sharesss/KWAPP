package http.data;

import org.json.JSONObject;

/**
 * Created by A1 on 2017/8/18.
 */

public class InvestMoneyEntity {
    private int id;
    private String  amount;//金额
    private double stockRate;//股权

    public InvestMoneyEntity(JSONObject jsonObject) {
        this.id = jsonObject.optInt("id");
        this.amount = jsonObject.optString("amount");
        this.stockRate = jsonObject.optDouble("stockRate");

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public double getStockRate() {
        return stockRate;
    }

    public void setStockRate(double stockRate) {
        this.stockRate = stockRate;
    }
}
