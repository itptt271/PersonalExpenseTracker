package manager;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:finance.db";
    // データベースへの接続を取得
    public static Connection connect(){
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            System.out.println("データベース接続に失敗しました: " + e.getMessage());
        }
        return conn;
    }
    // transactionsテーブルを作成（存在しない場合のみ）
    public static void createTransactionsTable(){
        String sql ="CREATE TABLE IF NOT EXISTS transactions (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "date TEXT NOT NULL," +
                    "type TEXT NOT NULL," +
                    "category TEXT NOT NULL," +
                    "amount INTEGER NOT NULL" +
                    ")";
        try (Connection conn = connect();
            Statement stmt = conn.createStatement()){
            stmt.execute(sql);
            System.out.println("transactionsテーブルを作成しました。");
        } catch (SQLException e) {
            System.out.println("テーブル作成に失敗しました: " + e.getMessage());
        }
    }
    // debtsテーブルを作成（存在しない場合のみ）
    public static void createDebtsTable(){
        String sql = "CREATE TABLE IF NOT EXISTS debts (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "creditorName TEXT NOT NULL," +
                     "amount INTEGER NOT NULL," +
                     "paidAmount INTEGER NOT NULL" +
                     ")";
        try (Connection conn = connect();
            Statement stmt = conn.createStatement()){
                stmt.execute(sql);
                System.out.println("debtsテーブルを作成しました。");
        } catch (SQLException e) {
            System.out.println("テーブル作成に失敗しました: " + e.getMessage());
        }
    }
    // 取引をDBに追加
    public static void insertTransaction(String date, String type, String category, int amount){
        String sql = "INSERT INTO transactions (date, type, category, amount) VALUES (?, ?, ?, ?)";
        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
                pstmt.setString(1, date);
                pstmt.setString(2, type);
                pstmt.setString(3, category);
                pstmt.setInt(4, amount);
                pstmt.executeUpdate();
                System.out.println("取引をDBに追加しました。");
        } catch (SQLException e) {
            System.out.println("追加に失敗しました: " + e.getMessage());
        }
    }
    // 借金をDBに追加
    public static void insertDebt(String creditorName, int amount, int paidAmount){
        String sql = "INSERT INTO debts (creditorName, amount, paidAmount) VALUES (?, ?, ?)";
        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
                pstmt.setString(1, creditorName);
                pstmt.setInt(2, amount);
                pstmt.setInt(3, paidAmount);
                pstmt.executeUpdate();
                System.out.println("借金をDBに追加しました。");
        } catch (SQLException e) {
            System.out.println("追加に失敗しました: " + e.getMessage());
        }
    }
    // 全ての取引をDBから取得
    public static List<model.Transaction>getAllTransactions(){
        List<model.Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions";
        try (Connection conn = connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
                while(rs.next()){
                    int id = rs.getInt("id");
                    java.time.LocalDate date = java.time.LocalDate.parse(rs.getString("date"));
                    model.TransactionType type = model.TransactionType.valueOf(rs.getString("type"));
                    model.Category category = model.Category.valueOf(rs.getString("category"));
                    int amount = rs.getInt("amount");
                    model.Transaction t = new model.Transaction(id, date, type, category, amount);
                    transactions.add(t);
                }
        } catch (SQLException e) {
            System.out.println("取得に失敗しました: " + e.getMessage());
        }
        return transactions;
    }
    // 全ての借金をDBから取得
    public static List<model.Debt> getAllDebts(){
        List<model.Debt> debts = new ArrayList<>();
        String sql = "SELECT * FROM debts";
        try (Connection conn = connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
                while(rs.next()){
                    int id = rs.getInt("id");
                    String creditorName = rs.getString("creditorName");
                    int amount = rs.getInt("amount");
                    int paidAmount = rs.getInt("paidAmount");
                    model.Debt debt = new model.Debt(id, creditorName, amount, paidAmount);
                    debts.add(debt);
                }
        } catch (SQLException e) {
            System.out.println("取得に失敗しました: " + e.getMessage());
        }
        return debts;
    }
    // 指定したIDの取引を削除
    public static void deleteTransaction(int id){
        String sql = "DELETE FROM transactions WHERE id = ?";
        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
                System.out.println("取引を削除しました。");
        } catch (SQLException e) {
            System.out.println("削除に失敗しました: " + e.getMessage());
        }
    }
    // テスト用：DB内の全取引を表示（デバッグ確認用）
    public static void printAllTransactions(){
        for(model.Transaction t : getAllTransactions()){
            System.out.println("ID:" + t.getId() + " | " + t.getDate() + " | " + t.getType() + " | " + t.getCategory() + " | " + t.getAmount() + "円");
        }
    }
    // 指定したIDの取引を更新
    public static void updateTransaction(int id, String date, String type, String category, int amount){
        String sql = "UPDATE transactions SET date = ?, type = ?, category = ?, amount = ? WHERE id = ? ";
        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
                pstmt.setString(1, date);
                pstmt.setString(2, type);
                pstmt.setString(3, category);
                pstmt.setInt(4, amount);
                pstmt.setInt(5, id);
                pstmt.executeUpdate();
                System.out.println("取引を更新しました。");
        } catch (SQLException e) {
            System.out.println("更新に失敗しました: " + e.getMessage());
        }
    }
    // 指定したIDの借金の返済額を更新
    public static void updateDebtPayment(int id, int newPaidAmount){
        String sql = "UPDATE debts SET paidAmount = ? WHERE id = ?";
        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
                pstmt.setInt(1, newPaidAmount);
                pstmt.setInt(2, id);
                pstmt.executeUpdate();
                System.out.println("返済を記録しました。");
        }catch (SQLException e) {
            System.out.println("更新に失敗しました: " + e.getMessage());
        }
    }
}
