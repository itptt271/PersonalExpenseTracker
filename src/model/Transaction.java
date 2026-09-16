package model;
import java.time.LocalDate;
public class Transaction {
    private static int nextId = 1;
    private int id;
    private LocalDate date;
    private TransactionType type;
    private Category category;
    private int amount;
    // 新規取引作成用（IDは自動採番）id tự đánh số
    public Transaction(LocalDate date, TransactionType type, Category category, int amount){
        this.id = nextId;
        nextId = nextId + 1;
        this.date = date;
        this.type = type;
        this.category = category;
        this.amount = amount;
    }
    // ファイルから読み込む用（既存のIDを保持）dùng khi đọc file giữ nguyên id 
    public Transaction (int id, LocalDate date, TransactionType type, Category category, int amount){
        this.id = id;
        this.date = date;
        this.type = type;
        this.category = category;
        this.amount = amount;
    }
    public int getId(){
        return id;
    }
    public LocalDate getDate(){
        return date;
    }
    public TransactionType getType(){
        return type;
    }
    public Category getCategory(){
        return category;
    }
    public int getAmount(){
        return amount;
    }

    public void setDate(LocalDate date){
        this.date = date;
    }
    public void setType(TransactionType type){
        this.type = type;
    }
    public void setCategory(Category category){
        this.category = category;
    }
    public void setAmount(int amount){
        this.amount = amount;
    }
    public static void setNextId(int id){
        nextId = id;
    }
}
