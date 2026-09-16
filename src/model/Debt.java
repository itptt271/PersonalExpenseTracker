package model;

public class Debt {
    private static int nextId = 1;
    private int id;
    private String creditorName;
    private int amount;
    private int paidAmount;
    // 借金登録用
    // 新しい借金を作成するときに使用
    public Debt(String creditorName, int amount){
        this.id = nextId;
        nextId = nextId + 1;
        this.creditorName = creditorName;
        this.amount = amount;
        this.paidAmount = 0;
    }
    // ファイル読み込み用
    // 保存した借金データを復元するときに使用
    public Debt(int id, String creditorName, int amount, int paidAmount){
        this.id = id;
        this.creditorName = creditorName;
        this.amount = amount;
        this.paidAmount = paidAmount;
    }
    public static void setNextId(int id){
        nextId = id;
    }
    public int getId(){
        return id;
    }
    public String getCreditorName(){
        return creditorName;
    }
    public int getAmount(){
        return amount;
    }
    public int getPaidAmount(){
        return paidAmount;
    }
    public double getProgressPercentage(){
        return (double) paidAmount / amount * 100;
    }
    public int getRemainingAmount(){
        return amount - paidAmount;
    }
    public boolean isPaid(){
        return paidAmount >= amount;
    }
    public void addPayment(int payment){
        paidAmount = paidAmount + payment;
    }
}