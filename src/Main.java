import java.util.Scanner;
import manager.DatabaseManager;
import manager.DebtManager;
import manager.TransactionManager;
public class Main {
    public static void main(String[] args) {
        DatabaseManager.createTransactionsTable();
        DatabaseManager.createDebtsTable();
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
                    TransactionManager.addTransaction(scanner);
                    break;
                case 2:
                    TransactionManager.displayTransactions();
                    break;
                case 3:
                    TransactionManager.displaySummary();
                    break;
                case 4:
                    TransactionManager.searchMenu(scanner);
                    break;
                case 5:
                    TransactionManager.editTransaction(scanner);
                    break;
                case 6:
                    TransactionManager.deleteTransaction(scanner);
                    break;
                case 7:
                    TransactionManager.displayCategoryStatistics();
                    break;
                case 8:
                    DebtManager.debtMenu(scanner);
                    break;
                case 9:
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