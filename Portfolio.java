
public class Portfolio {
    private int balance;

    private int tcsQuantity;
    private int infosysQuantity;
    private int wiproQuantity;

    public Portfolio(int balance) {
        this.balance = balance;
        this.tcsQuantity = 0;
        this.infosysQuantity = 0;
        this.wiproQuantity = 0;
    }

    public int getBalance() {
        return balance;
    }

    public void loadPortfolio(int balance, int tcsQty,
                              int infosysQty, int wiproQty) {
        this.balance = balance;
        this.tcsQuantity = tcsQty;
        this.infosysQuantity = infosysQty;
        this.wiproQuantity = wiproQty;
    }

    public int getQuantity(String stockName) {
        switch (stockName.toUpperCase()) {
            case "TCS":
                return tcsQuantity;
            case "INFOSYS":
                return infosysQuantity;
            case "WIPRO":
                return wiproQuantity;
            default:
                return 0;
        }
    }

    public boolean buyStock(String stockName, int quantity, int price) {
        if (quantity <= 0 || price <= 0) return false;

        long total = (long) quantity * price;
        if (total > balance) return false;

        switch (stockName.toUpperCase()) {
            case "TCS":
                tcsQuantity += quantity;
                break;
            case "INFOSYS":
                infosysQuantity += quantity;
                break;
            case "WIPRO":
                wiproQuantity += quantity;
                break;
            default:
                return false;
        }

        balance -= (int) total;
        return true;
    }

    public boolean sellStock(String stockName, int quantity, int price) {
        if (quantity <= 0 || price <= 0) return false;
        if (quantity > getQuantity(stockName)) return false;

        switch (stockName.toUpperCase()) {
            case "TCS":
                tcsQuantity -= quantity;
                break;
            case "INFOSYS":
                infosysQuantity -= quantity;
                break;
            case "WIPRO":
                wiproQuantity -= quantity;
                break;
            default:
                return false;
        }

        balance += quantity * price;
        return true;
    }

    public void displayPortfolio(Stock tcs, Stock infosys, Stock wipro) {
        System.out.println("\n===== MY PORTFOLIO =====");
        System.out.println("Available balance: Rs. " + balance);
        System.out.println("TCS quantity: " + tcsQuantity);
        System.out.println("INFOSYS quantity: " + infosysQuantity);
        System.out.println("WIPRO quantity: " + wiproQuantity);

        double value =
                tcsQuantity * tcs.getPrice()
              + infosysQuantity * infosys.getPrice()
              + wiproQuantity * wipro.getPrice();

        System.out.println("Total portfolio value: Rs. " + value);
        System.out.println("Total account value: Rs. " + (balance + value));
    }
}
