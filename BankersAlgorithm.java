import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BankersAlgorithm {

    public static void main(String[] args) {


        /*
        ================
        Set Processes
        ================
        */

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
        
        List<Process> processes = new ArrayList<>();
        for(int i = 0; i < processCount; i++){
            System.out.println("\nEnter data for process " + (i + 1) + ": ");

            List <Integer> max = new ArrayList<>();
            List <Integer> allocation = new ArrayList<>();

            System.out.println("\nMaximum resource demands:");
            for(int j = 0; j < resourceCount; j++){
                char resourceLetter = (char) ('A' + j);
                System.out.print("Resource " + resourceLetter + ": ");
                max.add(sc.nextInt());
            }

            System.out.println("\nAllocated resources:");
            for(int k = 0; k < resourceCount; k++){
                char resourceLetter = (char) ('A' + k);
                System.out.print("Resource " + resourceLetter + ": ");
                allocation.add(sc.nextInt());
            }

            processes.add(new Process(i, allocation, max));
        }
        /*
        ================
        Request Resource
        ================
        */
        System.out.print("Do you want to make a request resource? Yes or No: " );
        sc.nextLine();
        if(sc.nextLine() == "Yes"){
            System.out.print("What process will make a request?: " );
            int ProcessNumberRequest = sc.nextInt();
            sc.nextLine();

            List<Integer> requestResource = new ArrayList<Integer>();

            for(int i = 0; i < resourceCount; i++){
                char resourceLetter = (char) ('A' + i);
                System.out.print("Resource " + resourceLetter + ": ");
                requestResource.add(sc.nextInt());
                sc.nextLine();
            }

            Process process = processes.get(ProcessNumberRequest - 1);
            Request request = new Request(process, available, requestResource);
            
            processes.get(ProcessNumberRequest).setAllocation(request.ComputeAllocation());
            processes.get(ProcessNumberRequest).setNeed(request.ComputeNeed());   
            available = request.ComputeAvailable();     
        }else{
            System.err.println("Proceeding");
        }


        // Validate if deadlock
        // Validate if sequence is safe

        // Output table matrix

        sc.close();
    }

}