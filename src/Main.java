import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    private static final Path FILE = Path.of("ledger.csv");
    private static final List<Expense> ledger = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        load();
        while (true) {
            System.out.println("\n==== 记账本 ====");
            System.out.println("1 记一笔   2 查看全部   3 统计   4 按月查询   0 退出");
            System.out.print("> ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> addExpense();
                case "2" -> ledger.forEach(System.out::println);
                case "3" -> printStats();
                case "4" -> lookMonth();
                case "0" -> { System.out.println("再见！"); save(); return; }
                default -> System.out.println("没有这个选项：" + choice);
            }

        }

    }
    private static void addExpense() {
        try {
            System.out.print("类别：");
            String category = scanner.nextLine().trim();
            System.out.print("金额：");
            double amount = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("备注：");
            String note = scanner.nextLine().trim();
            ledger.add(new Expense(LocalDate.now(), category, amount, note));
            System.out.println("已记录✅ ");
        } catch (NumberFormatException e) {
            System.out.println("金额必须是数字（比如 12.5），这条没有保存。");
        } catch (IllegalArgumentException e) {
            System.out.println("保存失败：" + e.getMessage());
        }
    }
    private static void printStats() {
        double total = ledger.stream().mapToDouble(Expense::getAmount).sum();
        Map<String,Double> byCategroy = ledger.stream().collect(
                Collectors.groupingBy(Expense::getCategory, Collectors.summingDouble(Expense::getAmount))
        );
        System.out.println("总支出：" + total);
        byCategroy.forEach((cat, sum) -> System.out.println(cat+":"+sum));
    }
    private static void save() {
        try (BufferedWriter w = Files.newBufferedWriter(FILE)) {
            for (Expense e : ledger) {
                w.write(e.getDate() + "," + e.getCategory() + ","
                        + e.getAmount() + "," + e.getNote());
                w.newLine();
            }
            System.out.println("已保存 " + ledger.size() + " 条到 " + FILE.toAbsolutePath());
        } catch (IOException e) {
            System.out.println("保存失败：" + e.getMessage());
        }
    }
    private static void lookMonth(){
            System.out.println("您想查询几年几月的流水？如：2026-08");
            String sc = scanner.nextLine().trim();
            List<Expense> inMonth = ledger.stream().
                    filter(e -> e.getDate().toString().startsWith(sc)).
                    sorted(Comparator.comparing(Expense::getDate).reversed()).
                    toList();
            System.out.println(sc + " 月，共 " + inMonth.size() + " 笔：");
            inMonth.forEach(System.out::println);


    }
    private static void load(){
        if(!Files.exists(FILE))return;
        try{
            for(String line : Files.readAllLines(FILE)){
                String[] p = line.split(",", 4);
                ledger.add(new Expense(LocalDate.parse(p[0]),p[1],
                        Double.parseDouble(p[2]),p[3]));
            }
            System.out.println("已加载" + ledger.size() + "条历史账目");
        }catch(Exception e){
            System.out.println("历史信息损坏，从空账本开始：" + e.getMessage());
        }
    }
}