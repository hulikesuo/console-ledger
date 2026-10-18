import java.time.LocalDate;
public class Expense {
    private final LocalDate date;
    private final String category;
    private final double amount;
    private final String note;

    public Expense(LocalDate date, String category, double amount, String note) {
        if (amount < 0) {
            throw new IllegalArgumentException("金额必须大于0");
        }
        this.date = date;
        this.category = category;
        this.amount = amount;
        this.note = note;

    }
    public LocalDate getDate() { return date; }
    public String getCategory()  { return category; }
    public double getAmount() { return amount; }
    public String getNote(){ return note;}
    @Override
    public String toString(){
        return date + "  " + category + "  ¥" + amount + "  " + note;
    }
}
