

public class Transaction {
    private String stockName;
    private String type;
    private int quantity;
    private double price;
    private double total;

    public Transaction(String stockName, String type,
                       int quantity, double price) {
        this.stockName = stockName;
        this.type = type;
        this.quantity = quantity;
        this.price = price;
        this.total = quantity * price;
    }

    public String toFileString() {
        return stockName + "," + type + "," + quantity
                + "," + price;
    }

    public static Transaction fromFileString(String line) {
        String[] data = line.split(",");

        return new Transaction(
                data[0],
                data[1],
                Integer.parseInt(data[2]),
                Double.parseDouble(data[3])
        );
    }

    public void displayTransaction() {
        System.out.println(
                type + " | " + stockName
                + " | Quantity: " + quantity
                + " | Price: Rs. " + price
                + " | Total: Rs. " + total
        );
    }
}
