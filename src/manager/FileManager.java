package manager;
import java.util.ArrayList;
import java.util.List;
import model.Category;
import model.Debt;
import model.Transaction;
import model.TransactionType;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
public class FileManager {
    private static final String TRANSACTION_FILE = "transactions.txt";
    private static final String DEBTS_FILE = "debts.txt";
    // ================================================
    // Transaction File Management
    // ================================================
    // Transaction保存
    public static void saveTransactions(List <Transaction> transactions){
        try (BufferedWriter writer = new BufferedWriter(
            new FileWriter (TRANSACTION_FILE))){
                for(Transaction t : transactions){
                    writer.write(
                        t.getId() + "," +
                        t.getDate() + "," +
                        t.getType() + "," +
                        t.getCategory() + "," +
                        t.getAmount()
                    );
                    writer.newLine();
                }
            }catch (IOException e) {
                System.out.println("ファイルの保存に失敗しました。");
        }

    }
    // Transaction読み込み
    public static List<Transaction> loadTransactions(){
        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new FileReader(TRANSACTION_FILE))){
            String line;
            while((line = reader.readLine()) != null){
                try {
                    String[] data = line.split(",");
                    int id = Integer.parseInt(data[0]);
                    LocalDate date = LocalDate.parse(data[1]);
                    TransactionType type = TransactionType.valueOf(data[2]);
                    Category category = Category.valueOf(data[3]);
                    int amount = Integer.parseInt(data[4]);
                    Transaction loadedTransaction = new Transaction(id, date, type, category, amount);
                    transactions.add(loadedTransaction);
                } catch (Exception e) {
                    System.out.println("データの読み込みに失敗した行をスキップしました: " + line);
                }
            }
            int maxId = 0;
            for(Transaction t : transactions){
                if(t.getId() > maxId){
                    maxId = t.getId();
                }
            }
            Transaction.setNextId(maxId + 1);
        } catch (IOException e) {
            System.out.println("ファイルの読み込みに失敗しました。");
        }
        return transactions;
    }
    // ================================================
    // Debt File Management
    // ================================================
    public static void saveDebts(List<Debt> debts){
        try(BufferedWriter writer = new BufferedWriter(
            new FileWriter(DEBTS_FILE))){
                for(Debt debt : debts){
                    writer.write(
                        debt.getId() + "," +
                        debt.getCreditorName() + "," +
                        debt.getAmount() + "," +
                        debt.getPaidAmount()
                );
                    writer.newLine();
            }
        }catch (IOException e) {
            System.out.println("借金情報の保存に失敗しました。");
        }
    } 
    // Debt読み込み
    public static List<Debt> loadDebts(){
        List<Debt> debts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new FileReader(DEBTS_FILE))){
            String line;
            while((line = reader.readLine()) != null){
                try {
                    String[] data = line.split(",");
                    int id = Integer.parseInt(data[0]);
                    String creditorName = data[1];
                    int amount = Integer.parseInt(data[2]);
                    int paidAmount = Integer.parseInt(data[3]);
                    Debt debt = new Debt(
                        id,
                        creditorName,
                        amount, 
                        paidAmount
                );
                debts.add(debt);
                } catch (Exception e) {
                    System.out.println("データの読み込みに失敗した行をスキップしました: " + line);
                }
            }
            int maxId = 0;
            for(Debt debt : debts){
                if(debt.getId() > maxId){
                    maxId = debt.getId();
                }
            }
            Debt.setNextId(maxId + 1);
        } catch (IOException e) {
            System.out.println("借金情報の読み込みに失敗しました。");
        }
        return debts;
    }
}
