import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BankersAlgorithm {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter the number of processes: ");
        int processCount = sc.nextInt();
        sc.nextLine();
        
        System.out.print("Enter the number of resources: ");
        int resourceCount = sc.nextInt();
        sc.nextLine();

        List<Integer> available = new ArrayList<>();
        System.out.println("Enter the initial amount for the resources:");
        for(int i = 0; i < resourceCount; i++){
            char resourceLetter = (char) ('A' + i);
            System.out.print("Resource " + resourceLetter + ": ");
            available.add(sc.nextInt());
        }
        
        // Validate if deadlock
        // Validate if sequence is safe

        // Output table matrix
        
        sc.close();
    }

}