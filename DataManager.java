
import java.io.*;
import java.util.ArrayList;

public class DataManager {

    private static final String FILE_NAME = "trading_data.txt";

    public static void saveData(
            int balance,
            int tcsQty,
            int infosysQty,
            int wiproQty,
            double tcsPrice,
            double infosysPrice,
            double wiproPrice,
            ArrayList<Transaction> history) {

        try (PrintWriter writer = new PrintWriter(
                new FileWriter(FILE_NAME))) {

            writer.println(balance);
            writer.println(tcsQty);
            writer.println(infosysQty);
            writer.println(wiproQty);
            writer.println(tcsPrice);
            writer.println(infosysPrice);
            writer.println(wiproPrice);

            writer.println(history.size());

            for (Transaction t : history) {
                writer.println(t.toFileString());
            }

            System.out.println("Data saved successfully!");

        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }
}
