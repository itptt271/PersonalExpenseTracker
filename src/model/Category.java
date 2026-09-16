package model;
public enum Category {
    SALARY,
    FOOD,
    ELECTRICITY,
    WATER,
    GAS,
    INTERNET,
    INSURANCE,
    GYM,
    TRANSPORTATION,
    ENTERTAINMENT,
    SHOPPING,
    OTHER;
    public String getJapaneseName(){
        switch (this) {
            case SALARY:
                return "給料";
            case FOOD:
                return "食費";
            case ELECTRICITY:
                return "電気代";
            case WATER:
                return "水道代";
            case GAS:
                return "ガス代";
            case INTERNET:
                return "インターネット";
            case INSURANCE:
                return "保険";
            case GYM:
                return "ジム";
            case TRANSPORTATION:
                return "交通費";
            case ENTERTAINMENT:
                return "娯楽";
            case SHOPPING:
                return "買い物";
            case OTHER:
                return "その他";
            default:
                return "";
        }
    }
    public boolean isIncomeCategory(){
        switch (this) {
            case SALARY:
                return true;
            default:
                return false;
        }
    }
}
