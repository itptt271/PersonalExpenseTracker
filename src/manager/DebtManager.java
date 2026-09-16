package manager;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import model.Debt;

public class DebtManager {
    // ================================================
    // 借金 CRUD（登録・表示・返済）
    // ================================================
    // 借金登録
    public static void addDebt(Scanner scanner, List<Debt> debts){
        System.out.println("借金を追加します。");
        System.out.println();
        String name = selectCreditorName(scanner);
        int amount = selectDebtAmount(scanner);
        Debt newDebt = new Debt(name, amount);
        debts.add(newDebt);
    }
    // 借金一覧表示
    public static void displayDebts(List<Debt> debts){
        System.out.println("==============================");
        System.out.println("          借金一覧");
        System.out.println("==============================");
        System.out.println();
        printDebtList(debts);
    }
    // 返済する
    public static void addPayment(Scanner scanner, List<Debt> debts){
        System.out.println("==============================");
        System.out.println("          返済");
        System.out.println("==============================");
        System.out.println();
        printDebtList(debts);
        int debtId = inputDebtId(scanner);
        Debt targetDebt = findDebtById(debts, debtId);
        if(targetDebt == null){
            System.out.println("該当する借金がありません。");
        }else{
            printDebt(targetDebt);
            System.out.println();
            int remaining = targetDebt.getRemainingAmount();
            int payment = selectPaymentAmount(scanner, remaining);
            targetDebt.addPayment(payment);
            System.out.println("返済を記録しました。");
        }
    }
    // 借金合計を計算
    public static int calculateTotalRemainingDebt(List<Debt> debts){
        int total = 0;
        for(Debt t : debts){
            total += t.getRemainingAmount();
        }
        return total;
    }
    // 借金メニュー
    public static void debtMenu(Scanner scanner, List<Debt> debts){
        boolean back = false;
        while(!back){
            System.out.println("==============================");
            System.out.println("          借金管理");
            System.out.println("==============================");
            System.out.println();
            System.out.println("1. 借金を追加");
            System.out.println("2. 借金一覧を表示");
            System.out.println("3. 返済する");
            System.out.println("4. 借金合計を表示");
            System.out.println("5. 戻る");
            System.out.println();
            System.out.print("番号を選択してください：");
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        addDebt(scanner, debts);
                        break;
                    case 2:
                        displayDebts(debts);
                        break;
                    case 3:
                        addPayment(scanner, debts);
                        break;
                    case 4:
                        int totalRemaining = calculateTotalRemainingDebt(debts);
                        System.out.println();
                        System.out.println("借金合計: " + totalRemaining + "円");
                        break;
                    case 5:
                        back = true;
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
    }
    // ================================================
    // 共通処理（private - 複数箇所で利用）
    // ================================================
    // 借入先の名前を入力（借金登録で使用）
    private static String selectCreditorName(Scanner scanner){
        String creditorName = null;
        boolean validName = false;
        while(!validName){
            System.out.print("借入先を入力してください:");
            String name = scanner.nextLine();
            if(!name.isEmpty()){
                creditorName = name;
                validName = true;
            }else{
                System.out.println("名前を入力してください。");
            }
        }
        return creditorName;
    }
    // 返済額を入力（返済で専用・残高を超えないよう制限）
    private static int selectPaymentAmount(Scanner scanner, int remaining){
        int amount = 0;
        boolean validAmount = false;
        while(!validAmount){
            System.out.print("借金額を入力してください：");
            try{
                amount = scanner.nextInt();
                if(amount <= 0){
                    System.out.println("金額は0より大きい値を入力してください。");
                }else if(amount > remaining){
                    System.out.println("返済額が残りの借金額（" + remaining + "円）を超えています。");
                }else{
                    validAmount = true;
                }
            }catch (InputMismatchException e){
                System.out.println("数字を入力してください。");
                scanner.nextLine();
            }
        }
        return amount;
    }
    // 借金額を入力（借金登録で使用）
    private static int selectDebtAmount(Scanner scanner){

        int amount = 0;
        boolean validAmount = false;
        while(!validAmount){
            System.out.print("借金額を入力してください：");
            try{
                amount = scanner.nextInt();
                if(amount > 0){
                    validAmount = true;
                }else{
                    System.out.println("金額は0より大きい値を入力してください。");
            }
            }catch (InputMismatchException e){
                System.out.println("数字を入力してください。");
                scanner.nextLine();
            }
        }
        return amount;
    }
    // 借金情報を1件表示（一覧・返済で共通利用）
    private static void printDebt(Debt debt){
        String status;
        if(debt.isPaid()){
            status = "返済済み"; 
        }else{
            status = "返済中";
        }
        System.out.println("ID:" + debt.getId() 
        + " | " + debt.getCreditorName() 
        + " | " + debt.getAmount() + "円 | 残り:" + debt.getRemainingAmount()
        + "円 | " + String.format(Locale.US, "%.0f",debt.getProgressPercentage()) 
        + "% | " + status);
    }
    // 借金検索（IDで検索・返済で共通利用）
    private static Debt findDebtById(List<Debt> debts, int debtId){
        for(Debt t : debts){
            if(t.getId() == debtId){
                return t;
            }
        }
        return null;
    }
    // 借金ID入力（返済で使用）
    private static int inputDebtId(Scanner scanner){
        int debtId = 0;
        boolean validId = false;
        while(!validId){
            System.out.print("借金IDを入力してください:");
            try {
                debtId = scanner.nextInt();
                if(debtId > 0){
                    validId = true;
                }else{
                    System.out.println("IDは0より大きい値を入力してください。");
                }
            } catch (InputMismatchException e) {
                System.out.println("数字を入力してください。");
                scanner.nextLine();
            }
        }
        return debtId;
    }
    // 借金一覧を出力（ヘッダーなし・複数箇所で共通利用）
    private static void printDebtList(List<Debt> debts){
        if(debts.isEmpty()){
            System.out.println("借金履歴がありません。");
        }else{
            int stt = 1;
            for(Debt t : debts){
                System.out.print("[" + stt + "] ");
                printDebt(t);
                stt++;
            }
        }
    }
}
