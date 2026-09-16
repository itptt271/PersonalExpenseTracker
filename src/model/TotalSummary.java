package model;
public class TotalSummary {
    private int totalExpense;
    private int totalIncome;

    public TotalSummary(int totalExpense, int totalIncome){
        this.totalExpense = totalExpense;
        this.totalIncome = totalIncome;
    }

    public int getTotalExpense(){
        return totalExpense;
    }

    public int getTotalIncome(){
        return totalIncome;
    }

    public int getBalance(){
        return totalIncome - totalExpense;
    }
}
