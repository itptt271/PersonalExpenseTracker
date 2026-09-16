import java.util.List;
import java.util.Scanner;
import manager.DatabaseManager;
import manager.DebtManager;
import manager.FileManager;
import manager.TransactionManager;
import model.Transaction;
import model.Debt;
public class Main {
    public static void main(String[] args) {
        DatabaseManager.createTransactionsTable();
        DatabaseManager.createDebtsTable();
        DatabaseManager.insertTransaction("2026-09-14", "EXPENSE", "FOOD", 1000);
        DatabaseManager.insertDebt("Test", 10000, 0);
        List<Transaction> transactions = FileManager.loadTransactions();
        List<Debt> debts = FileManager.loadDebts();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while(running){
            System.out.println("================================");
            System.out.println("          個人支出管理");
            System.out.println("================================");
            System.out.println();
            System.out.println("1. 取引を追加");
            System.out.println("2. 取引履歴を表示");
            System.out.println("3. サマリーを表示");
            System.out.println("4. 取引を探索");
            System.out.println("5. 取引編集");
            System.out.println("6. 取引削除");
            System.out.println("7. カテゴリー別統計");
            System.out.println("8. 借金管理");
            System.out.println("9. 終了");
            System.out.println();
            System.out.print("番号を選択してください：");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    TransactionManager.addTransaction(scanner, transactions);
                    break;
                case 2:
                    TransactionManager.displayTransactions(transactions);
                    break;
                case 3:
                    TransactionManager.displaySummary(transactions);
                    break;
                case 4:
                    TransactionManager.searchMenu(scanner, transactions);
                    break;
                case 5:
                    TransactionManager.editTransaction(scanner, transactions);
                    break;
                case 6:
                    TransactionManager.deleteTransaction(scanner, transactions);
                    break;
                case 7:
                    TransactionManager.displayCategoryStatistics(transactions);
                    break;
                case 8:
                    DebtManager.debtMenu(scanner, debts);
                    break;
                case 9:
                    FileManager.saveTransactions(transactions);
                    FileManager.saveDebts(debts);
                    System.out.println("終了します。");
                    running = false;
                    break;
                default:
                    System.out.println("無効な番号です。");
                    break;
            }
        }
    }
}