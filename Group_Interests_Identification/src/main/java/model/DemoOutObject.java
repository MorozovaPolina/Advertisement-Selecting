package model;

public class DemoOutObject {
    String request_id;
    String transaction_type;
    String time;
    String source;
    String target;
    String order_number;

    public DemoOutObject(String request_id, String transaction_type, String time, String source, String target, String order_number) {
        this.request_id = request_id;
        this.transaction_type = transaction_type;
        this.time = time;
        this.source = source;
        this.target = target;
        this.order_number = order_number;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }
}
