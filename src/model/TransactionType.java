package model;
public enum TransactionType {
    INCOME,
    EXPENSE;
    public String getJapaneseName(){
        switch (this) {
            case INCOME:
                return "収入";
            case EXPENSE:
                return "支出";
            default:
                return "";
        }
    }
}
