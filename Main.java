
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class Main {

    static Scanner sc = new Scanner(System.in);
    static ArrayList<Transaction> history = new ArrayList<>();

    static Stock tcs = new Stock("TCS", 3500);
    static Stock infosys = new Stock("INFOSYS", 1800);
    static Stock wipro = new Stock("WIPRO", 500);

    static Portfolio portfolio = new Portfolio(100000);

    public static void main(String[] args) {
        loadData();

        int choice = 0;

        do {
            System.out.println("\n===== ADVANCED STOCK TRADING PLATFORM =====");
            System.out.println("1. View Stocks");
            System.out.println("2. Buy Stocks");
            System.out.println("3. Sell Stocks");
            System.out.println("4. View Portfolio");
            System.out.println("5. Transaction History");
            System.out.println("6. Update Stock Price (Simulation)");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            String input = sc.next();

            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(
                    "Invalid input! Please enter a number from 1 to 7."
                );
                continue;
            }

            switch (choice) {
                case 1:
                    viewStocks();
                    break;
                case 2:
                    tradeStock(true);
                    break;
                case 3:
                    tradeStock(false);
                    break;
                case 4:
                    portfolio.displayPortfolio(tcs, infosys, wipro);
                    showProfitLoss();
                    break;
                case 5:
                    viewHistory();
                    break;
                case 6:
                    updateStockPrice();
                    break;
                case 7:
                    saveData();
                    System.out.println(
                        "Thank you for using Stock Trading Platform!"
                    );
                    break;
                default:
                    System.out.println("Invalid choice! Select 1 to 7.");
            }

        } while (choice != 7);

        sc.close();
    }

    static void viewStocks() {
        System.out.println("\n===== AVAILABLE STOCKS =====");
        tcs.displayStock();
        infosys.displayStock();
        wipro.displayStock();
    }

    static Stock getStock(int number) {
        switch (number) {
            case 1:
                return tcs;
            case 2:
                return infosys;
            case 3:
                return wipro;
            default:
                return null;
        }
    }

    static Integer readInteger(String message) {
        System.out.print(message);
        String input = sc.next();

        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a whole number.");
            return null;
        }
    }

    static void tradeStock(boolean buying) {
        viewStocks();

        Integer number = readInteger("Enter stock number (1-3): ");

        if (number == null) {
            return;
        }

        Stock stock = getStock(number);

        if (stock == null) {
            System.out.println("Invalid stock number! Select 1 to 3.");
            return;
        }

        Integer quantity = readInteger("Enter quantity: ");

        if (quantity == null) {
            return;
        }

        if (quantity <= 0) {
            System.out.println("Quantity must be greater than zero!");
            return;
        }

        int price = (int) stock.getPrice();
        String name = stock.getName();

        if (buying) {
            if (portfolio.buyStock(name, quantity, price)) {
                history.add(new Transaction(name, "BUY", quantity, price));

                System.out.println("Stock purchased successfully!");
                System.out.println("Total amount: Rs. " + (quantity * price));
                System.out.println(
                    "Remaining balance: Rs. " + portfolio.getBalance()
                );

                saveData();
            } else {
                System.out.println(
                    "Purchase failed! Check balance or quantity."
                );
            }
        } else {
            if (portfolio.sellStock(name, quantity, price)) {
                history.add(new Transaction(name, "SELL", quantity, price));

                System.out.println("Stock sold successfully!");
                System.out.println(
                    "Total amount received: Rs. " + (quantity * price)
                );
                System.out.println(
                    "Available balance: Rs. " + portfolio.getBalance()
                );

                saveData();
            } else {
                System.out.println(
                    "Sale failed! You do not have enough stocks."
                );
            }
        }
    }

    static void viewHistory() {
        System.out.println("\n===== TRANSACTION HISTORY =====");

        if (history.isEmpty()) {
            System.out.println("No transactions yet.");
            return;
        }

        for (Transaction transaction : history) {
            transaction.displayTransaction();
        }
    }

    static void updateStockPrice() {
        viewStocks();

        Integer number = readInteger(
            "Enter stock number to update (1-3): "
        );

        if (number == null) {
            return;
        }

        Stock stock = getStock(number);

        if (stock == null) {
            System.out.println("Invalid stock number! Select 1 to 3.");
            return;
        }

        System.out.print("Enter new stock price: ");
        String priceInput = sc.next();

        double newPrice;

        try {
            newPrice = Double.parseDouble(priceInput);
        } catch (NumberFormatException e) {
            System.out.println("Invalid price! Enter a numeric value.");
            return;
        }

        if (Double.isFinite(newPrice) && newPrice > 0) {
            stock.setPrice(newPrice);
            System.out.println("Stock price updated successfully!");
            saveData();
        } else {
            System.out.println("Price must be a positive number!");
        }
    }

    static void showProfitLoss() {
        System.out.println("\n===== PROFIT / LOSS =====");
        showStockProfitLoss(tcs);
        showStockProfitLoss(infosys);
        showStockProfitLoss(wipro);
    }

    static void showStockProfitLoss(Stock stock) {
        int quantity = portfolio.getQuantity(stock.getName());

        double currentValue = quantity * stock.getPrice();
        double investedValue =
            quantity * getOriginalPrice(stock.getName());

        double difference = currentValue - investedValue;

        System.out.println(
            stock.getName() + ": Rs. " + difference
            + (difference >= 0 ? " Profit" : " Loss")
        );
    }

    static int getOriginalPrice(String name) {
        switch (name) {
            case "TCS":
                return 3500;
            case "INFOSYS":
                return 1800;
            case "WIPRO":
                return 500;
            default:
                return 0;
        }
    }

    static void saveData() {
        DataManager.saveData(
            portfolio.getBalance(),
            portfolio.getQuantity("TCS"),
            portfolio.getQuantity("INFOSYS"),
            portfolio.getQuantity("WIPRO"),
            tcs.getPrice(),
            infosys.getPrice(),
            wipro.getPrice(),
            history
        );
    }

    static void loadData() {
        File file = new File("trading_data.txt");

        if (!file.exists()) {
            System.out.println(
                "No saved data found. Starting a new account."
            );
            return;
        }

        try (BufferedReader reader =
                 new BufferedReader(new FileReader(file))) {

            int balance = Integer.parseInt(reader.readLine());
            int tcsQty = Integer.parseInt(reader.readLine());
            int infosysQty = Integer.parseInt(reader.readLine());
            int wiproQty = Integer.parseInt(reader.readLine());

            double tcsPrice = Double.parseDouble(reader.readLine());
            double infosysPrice = Double.parseDouble(reader.readLine());
            double wiproPrice = Double.parseDouble(reader.readLine());

            int count = Integer.parseInt(reader.readLine());

            if (count < 0) {
                throw new IOException("Invalid transaction count.");
            }

            portfolio.loadPortfolio(
                balance, tcsQty, infosysQty, wiproQty
            );

            tcs.setPrice(tcsPrice);
            infosys.setPrice(infosysPrice);
            wipro.setPrice(wiproPrice);

            history.clear();

            for (int i = 0; i < count; i++) {
                String line = reader.readLine();

                if (line != null) {
                    history.add(Transaction.fromFileString(line));
                }
            }

            System.out.println("Saved data loaded successfully!");

        } catch (IOException | RuntimeException e) {
            System.out.println(
                "Could not load saved data: " + e.getMessage()
            );
        }
    }
}
