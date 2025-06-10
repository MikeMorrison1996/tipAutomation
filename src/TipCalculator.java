import java.util.*;

public class TipCalculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> names = new ArrayList<>();
        List<Double> hours = new ArrayList<>();

        System.out.print("Enter total tips: ");
        double totalTips = scanner.nextDouble();

        System.out.println("Enter employee names and hours worked (type 'done' to finish):");
        while (true) {
            System.out.print("Name: ");
            String name = scanner.next();
            if (name.equalsIgnoreCase("done")) break;

            System.out.print("Hours: ");
            double hour = scanner.nextDouble();

            names.add(name);
            hours.add(hour);
        }

        double totalHours = hours.stream().mapToDouble(Double::doubleValue).sum();
        System.out.println("\n--- Tip Distribution ---");
        for (int i = 0; i < names.size(); i++) {
            double share = (hours.get(i) / totalHours) * totalTips;
            System.out.printf("%s gets $%.2f\n", names.get(i), share);
        }
    }
}
