package manager;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import model.Category;
import model.TotalSummary;
import model.Transaction;
import model.TransactionType;
import java.util.Map;
import java.util.HashMap;
public class TransactionManager {
    // ================================================
    // 取引 CRUD（登録・表示・編集・削除）
    // ================================================
    // 取引履歴表示
    public static void displayTransactions(){
        System.out.println("==============================");
        System.out.println("          取引履歴");
        System.out.println("==============================");
        System.out.println();
        List<Transaction> transactions = manager.DatabaseManager.getAllTransactions();
        printTransactionList(transactions);
    }
    // サマリー計算
    public static TotalSummary calculateTotals(List<Transaction> transactions){
        int totalExpense = 0;
        int totalIncome = 0;
        for(Transaction t : transactions){
            if(t.getType() == TransactionType.EXPENSE){
                totalExpense += t.getAmount();
            }else if(t.getType() == TransactionType.INCOME){
                totalIncome += t.getAmount();
            }
        }
        return new TotalSummary(totalExpense, totalIncome);
    }
    // 取引登録
    public static void addTransaction(Scanner scanner){
        System.out.println("取引を追加します。");
        System.out.println();
        LocalDate date = selectDate(scanner);
        System.out.println();
        TransactionType type = selectType(scanner);
        Category category = selectCategory(scanner, type);
        int amount = inputAmount(scanner);
        DatabaseManager.insertTransaction(date.toString(), type.toString(), category.toString(), amount);
    }
    // サマリー表示
    public static void displaySummary(){
        System.out.println("==============================");
        System.out.println("          サマリー");
        System.out.println("==============================");
        System.out.println();
        List <Transaction> transactions = manager.DatabaseManager.getAllTransactions();
        TotalSummary summary = calculateTotals(transactions);
        System.out.println("支出合計: " + summary.getTotalExpense() + "円");
        System.out.println("収入合計: " + summary.getTotalIncome() + "円");
        System.out.println("残高: " + summary.getBalance() + "円");
        if(summary.getTotalIncome() == 0){
            System.out.println("貯金額: データがありません");
        }else{
            double savingsRate = (double) summary.getBalance() / summary.getTotalIncome() * 100;
            System.out.println("貯金額: " + String.format(Locale.US, "%.0f", savingsRate) + "%");
        }
    } 
    // 取引編集
    public static void editTransaction(Scanner scanner){

        System.out.println("==============================");
        System.out.println("          取引編集");
        System.out.println("==============================");
        System.out.println();
        List<Transaction> transactions = DatabaseManager.getAllTransactions();
        printTransactionList(transactions);
        if(transactions.isEmpty()){
            return;
            }
            System.out.print("編集する番号を選んでください（一覧の番号）：");
            int number = scanner.nextInt();
            scanner.nextLine();
            int index = number - 1;
            if(index < 0 || index >= transactions.size()){
                System.out.println("該当する番号がありません。");
            }
            else{
                Transaction targeTransaction = transactions.get(index);
                int id = targeTransaction.getId();
                LocalDate date = selectDate(scanner);
                TransactionType type = selectType(scanner);
                Category category = selectCategory(scanner, type);
                int amount = inputAmount(scanner);
                DatabaseManager.updateTransaction(id, date.toString(), type.toString(), category.toString(), amount);
                System.out.println("取引を編集しました。");
            }
    }
    // 取引削除
    public static void deleteTransaction(Scanner scanner){

        System.out.println("==============================");
        System.out.println("          取引削除");
        System.out.println("==============================");
        System.out.println();
        List<Transaction> transactions = DatabaseManager.getAllTransactions();
        printTransactionList(transactions);
        if(transactions.isEmpty()){
            return;
        }
        System.out.print("削除する番号を選んでください（一覧の番号）：");
        int number = scanner.nextInt();
        scanner.nextLine();
        int index = number - 1;
        if(index < 0 || index >= transactions.size()){
            System.out.println("該当する取引がありません。");
        }else{
            Transaction targetTransaction = transactions.get(index);
            boolean validAnswer = false;
            while(!validAnswer){
                printTransaction(targetTransaction);
                System.out.print("この取引を削除しますか？ (y/n): ");
                String answer = scanner.nextLine();
                if(answer.equalsIgnoreCase("y")){
                    validAnswer = true;
                    DatabaseManager.deleteTransaction(targetTransaction.getId());
                }else if(answer.equalsIgnoreCase("n")){
                    validAnswer = true;
                    System.out.println("削除をキャンセルしました。");
                }else{
                    System.out.println("無効な入力です。");
                }
            }
        }
    }
    // ================================================
    // 取引検索
    // ================================================
    //取引検索(category)
    public static void searchByCategory(Scanner scanner){
        System.out.println("==============================");
        System.out.println("          カテゴリーで検索");
        System.out.println("==============================");
        System.out.println();
        List<Transaction> transactions = DatabaseManager.getAllTransactions();
        Category category = selectAnyCategory(scanner);
        boolean found = false;
        int resultCount = 0;
        int totalAmount = 0;
        for(Transaction t : transactions){
            if(t.getCategory() == category){
                found = true;
                resultCount++;
                totalAmount += t.getAmount();
                printTransaction(t);
            }
        }
        if(!found){
            System.out.println("該当する取引がありません。");
        }else{
            System.out.println();
            System.out.println("検索結果: " + resultCount + "件");
            System.out.println("合計金額: " + totalAmount + "円");
        }
    }
    //取引検索(date)
    public static void searchByDate(Scanner scanner){
        System.out.println("==============================");
        System.out.println("          日付で検索");
        System.out.println("==============================");
        System.out.println();
        LocalDate searchDate = selectDate(scanner);
        boolean found = false;
        int resultCount = 0;
        int totalAmount = 0;
        List<Transaction> transactions = DatabaseManager.getAllTransactions();
        for(Transaction t: transactions){
            if(t.getDate().equals(searchDate)){
                found = true;
                resultCount++;
                totalAmount += t.getAmount();
                printTransaction(t);
            }
        }
        if(!found){
            System.out.println("該当する取引がありません。");
        }else{
            System.out.println();
            System.out.println("検索結果: " + resultCount + "件");
            System.out.println("合計金額: " + totalAmount + "円");
        }
    }
    //取引検索(menu)
    public static void searchMenu(Scanner scanner){
        System.out.println("==============================");
        System.out.println("          取引検索");
        System.out.println("==============================");
        System.out.println();
        boolean validsearchChoice = false;
        while(!validsearchChoice){
            System.out.println("検索方法を選択してください");
            System.out.println();
            System.out.println("1. カテゴリーで検索");
            System.out.println("2. 日付で検索");
            System.out.println();
            System.out.print("番号を入力してください：");
            try {
                int searchChoice = scanner.nextInt();
                scanner.nextLine();
                if(searchChoice == 1){
                    searchByCategory(scanner);
                    validsearchChoice = true;
                }else if(searchChoice == 2){
                    searchByDate(scanner);
                    validsearchChoice = true;
                }else{
                    System.out.println("無効な番号です。");
                }
            } catch (InputMismatchException e) {
                System.out.println("数字を入力してください。");
                scanner.nextLine();
            }
        }
    }
    // ================================================
    // 統計 (Statistics)
    // ================================================
    // カテゴリー別支出
    public static Map<Category, Integer> calculateCategoryTotals(List<Transaction> transactions){
        Map<Category, Integer> categoryTotals = new HashMap<>();
        for(Transaction t : transactions){
            if(t.getType() == TransactionType.EXPENSE){

                Category category = t.getCategory();
                int amount = t.getAmount();
                if(categoryTotals.containsKey(category)){
                    int currentTotal = categoryTotals.get(category);
                    categoryTotals.put(category, currentTotal + amount);
                }else{
                    categoryTotals.put(category, amount);
                }
            }
        }
        return categoryTotals;
    }
    // カテゴリー別支出表示
    public static void displayCategoryStatistics(){
        System.out.println("==============================");
        System.out.println("       カテゴリー別支出");
        System.out.println("==============================");
        System.out.println();
        List <Transaction> transactions = manager.DatabaseManager.getAllTransactions();
        Map<Category, Integer> categoryTotals = calculateCategoryTotals(transactions);
        TotalSummary summary = calculateTotals(transactions);
        int totalExpense = summary.getTotalExpense();
        if(totalExpense == 0){
            System.out.println("データがありません");
        }else{
            for(Category category : categoryTotals.keySet()){
                int amount = categoryTotals.get(category);
                double percentage = (double) amount / totalExpense * 100;
                System.out.println(category.getJapaneseName() + ": " + amount + "円 (" + String.format(Locale.US, "%.0f", percentage) + "%)");
            }
        }
        System.out.println();
        Category mostUsendCategory = findMostUsedCategory(transactions);
       if(mostUsendCategory != null){
        System.out.println("一番使ったカテゴリー: " + mostUsendCategory.getJapaneseName());
       }
    }
    // 一番使ったカテゴリー
    public static Category findMostUsedCategory(List<Transaction> transactions){
        Map<Category, Integer> categoryTotals = calculateCategoryTotals(transactions);
        Category mostUsedCategory = null;
        int maxAmount = 0;
        for(Category category : categoryTotals.keySet()){
            int amount = categoryTotals.get(category);
            if(amount > maxAmount){
                maxAmount = amount;
                mostUsedCategory = category;
            }
        }
        return mostUsedCategory;
    }
    // ================================================
    // 共通処理（private - 複数箇所で利用）
    // ================================================
    // 取引情報を1件表示（表示・検索で共通利用）
    private static void printTransaction(Transaction t){
        System.out.println("ID:" + t.getId() + 
        " | " + t.getDate() + 
        " | " + t.getType().getJapaneseName() + 
        " | " + String.format("%-4s", t.getCategory().getJapaneseName()) + 
        " | " + t.getAmount() + "円"
    );
    }
    // カテゴリー選択（登録・検索で共通利用）
    private static Category selectCategory(Scanner scanner, TransactionType type){
        boolean validCategory = false;
        Category category = null;
        while(!validCategory){
            System.out.println("カテゴリーを選択してください");
            System.out.println();

            if(type == TransactionType.INCOME){
                System.out.println("1. 給料");
            }else{
                System.out.println("2. 食費");
                System.out.println("3. 電気代");
                System.out.println("4. 水道代");
                System.out.println("5. ガス代");
                System.out.println("6. インターネット");
                System.out.println("7. 保険");
                System.out.println("8. ジム");
                System.out.println("9. 交通費");
                System.out.println("10. 娯楽");
                System.out.println("11. 買い物");
                System.out.println("12. その他");
            }
            System.out.println();
            System.out.print("番号を入力してください：");
            try {
                int categoryChoice = scanner.nextInt();
                switch (categoryChoice) {
                    case 1:
                        category = Category.SALARY;
                        validCategory = true;
                        break;
                    case 2:
                        category = Category.FOOD;
                        validCategory = true;
                        break;
                    case 3:
                        category = Category.ELECTRICITY;
                        validCategory = true;
                        break;
                    case 4:
                        category = Category.WATER;
                        validCategory = true;
                        break;
                    case 5:
                        category = Category.GAS;
                        validCategory = true;
                        break;
                    case 6:
                        category = Category.INTERNET;
                        validCategory = true;
                        break;
                    case 7:
                        category = Category.INSURANCE;
                        validCategory = true;
                        break;
                    case 8:
                        category = Category.GYM;
                        validCategory = true;
                        break;
                    case 9:
                        category = Category.TRANSPORTATION;
                        validCategory = true;
                        break;
                    case 10:
                        category = Category.ENTERTAINMENT;
                        validCategory = true;
                        break;
                    case 11:
                        category = Category.SHOPPING;
                        validCategory = true;
                        break;
                    case 12:
                        category = Category.OTHER;
                        validCategory = true;
                        break;
                    default:
                        System.out.println("無効な番号です。");
                        break;
                }
                
            } catch (InputMismatchException e) {
                System.out.println("数字を入力してください。");
                scanner.nextLine();
            }
        }
        return category;
    }
    // 日付選択（登録・検索で共通利用）
    private static LocalDate selectDate(Scanner scanner){
       LocalDate date = null;
       boolean validDate = false;
       while(!validDate){
           System.out.print("日付を入力してください（例: 2026-08-15): ");
           String dateInput = scanner.nextLine();
           try {
               date = LocalDate.parse(dateInput);
               validDate = true;
           } catch (DateTimeParseException e) {
               System.out.println("日付の形式が正しくありません。");
               System.out.println("例: 2026-08-15");
           }
       }
       return date;
   }
    // 取引タイプ選択（登録・編集で共通利用）
    private static TransactionType selectType(Scanner scanner){
       TransactionType type = null;
       boolean validType = false;
       while(!validType){
           System.out.println("取引タイプを選択してください");
           System.out.println();
           System.out.println("1. 収入");
           System.out.println("2. 支出");
           System.out.println();
           System.out.print("番号を入力してください：");
           try {
               int typeChoice = scanner.nextInt();
               if(typeChoice == 1){
                   type = TransactionType.INCOME;
                   validType = true;
               }else if(typeChoice == 2){
                   type = TransactionType.EXPENSE;
                   validType = true;
               }else{
                   System.out.println("無効な番号です。");
               }
           } catch (InputMismatchException e) {
               System.out.println("数字を入力してください。");
               scanner.nextLine();
           }
       }
       return type;
   }
    // 金額（登録・編集で共通利用）
    private static int inputAmount(Scanner scanner){
       int amount = 0;
       boolean validAmount = false;
       while(!validAmount){
           System.out.print("金額を入力してください：");
           try {
               amount = scanner.nextInt();
               if(amount > 0){
                   validAmount = true;
               }else{
                   System.out.println("金額は0より大きい値を入力してください。");
               }
           } catch (InputMismatchException e) {
               System.out.println("数字を入力してください。");
               scanner.nextLine();
           }
       }
       return amount;
   }
    // カテゴリー選択（検索専用・全カテゴリー表示
    private static Category selectAnyCategory(Scanner scanner){
        boolean validCategory = false;
        Category category = null;
        while(!validCategory){
            System.out.println("カテゴリーを選択してください");
            System.out.println();
            System.out.println("1. 給料");
            System.out.println("2. 食費");
            System.out.println("3. 電気代");
            System.out.println("4. 水道代");
            System.out.println("5. ガス代");
            System.out.println("6. インターネット");
            System.out.println("7. 保険");
            System.out.println("8. ジム");
            System.out.println("9. 交通費");
            System.out.println("10. 娯楽");
            System.out.println("11. 買い物");
            System.out.println("12. その他");
            System.out.println();
            System.out.print("番号を入力してください：");
            try {
                int categoryChoice = scanner.nextInt();
                switch (categoryChoice) {
                    case 1:
                        category = Category.SALARY;
                        validCategory = true;
                        break;
                    case 2:
                        category = Category.FOOD;
                        validCategory = true;
                        break;
                    case 3:
                        category = Category.ELECTRICITY;
                        validCategory = true;
                        break;
                    case 4:
                        category = Category.WATER;
                        validCategory = true;
                        break;
                    case 5:
                        category = Category.GAS;
                        validCategory = true;
                        break;
                    case 6:
                        category = Category.INTERNET;
                        validCategory = true;
                        break;
                    case 7:
                        category = Category.INSURANCE;
                        validCategory = true;
                        break;
                    case 8:
                        category = Category.GYM;
                        validCategory = true;
                        break;
                    case 9:
                        category = Category.TRANSPORTATION;
                        validCategory = true;
                        break;
                    case 10:
                        category = Category.ENTERTAINMENT;
                        validCategory = true;
                        break;
                    case 11:
                        category = Category.SHOPPING;
                        validCategory = true;
                        break;
                    case 12:
                        category = Category.OTHER;
                        validCategory = true;
                        break;
                    default:
                        System.out.println("無効な番号です。");
                        break;
                }
                
            } catch (InputMismatchException e) {
                System.out.println("数字を入力してください。");
                scanner.nextLine();
            }
        }
        return category;
    }
    // 取引一覧を出力（ヘッダーなし・複数箇所で共通利用）
    private static void printTransactionList(List<Transaction> transactions){
        if(transactions.isEmpty()){
            System.out.println("取引履歴がありません。");
        }else{
            int stt = 1;
            for (Transaction t : transactions) {
                System.out.print("[" + stt + "] ");
                printTransaction(t);
                stt++;
            }
        }
    }
}