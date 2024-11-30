import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class BankersAlgorithm {

    public static void main(String[] args) {

        /*
         * ================
         * Set Processes
         * ================
         */

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter the number of processes: ");
        int processCount = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter the number of resources: ");
        int resourceCount = sc.nextInt();
        sc.nextLine();

        StringBuilder letters = new StringBuilder();
        for (int j = 0; j < resourceCount; j++) {
            letters.append((char) ('A' + j));
            if (j < resourceCount - 1) {
                letters.append(", ");
            }
        }

        List<Integer> available = new ArrayList<>();
        System.out.print("Enter the initial amount for the resources: " + "(" + letters + "): ");
        String InitialResource = sc.nextLine();
        for (String str : Arrays.asList(InitialResource.split(",\\s*"))) {
            available.add(Integer.parseInt(str));
        }

        List<Process> processes = new ArrayList<>();
        for (int i = 0; i < processCount; i++) {
            System.out.println("\nEnter data for process " + (i) + ": ");

            List<Integer> max = new ArrayList<>();
            List<Integer> allocation = new ArrayList<>();

            System.out.print("Maximum resource demands of process " + i + " (" + letters + "): ");
            String MaxResource = sc.nextLine();
            for (String str : Arrays.asList(MaxResource.split(",\\s*"))) {
                max.add(Integer.parseInt(str));
            }

            System.out.print("Allocated resource for process " + i + " (" + letters + "): ");
            String AllocatedResource = sc.nextLine();
            for (String str : Arrays.asList(AllocatedResource.split(",\\s*"))) {
                allocation.add(Integer.parseInt(str));
            }

            processes.add(new Process(i, allocation, max));
        }
        /*
         * ================
         * Request Resource
         * ================
         */
        System.out.print("Do you want to make a request resource? Yes or No: ");
        if (sc.nextLine().equalsIgnoreCase("yes")) {
            System.out.print("What process will make a request?: ");
            int ProcessNumberRequest = sc.nextInt();
            sc.nextLine();

            List<Integer> RequestResourceList = new ArrayList<Integer>();

            System.out.print("Maximum resource demands of process " + ProcessNumberRequest + " (" + letters + "): ");
            String RequestResource = sc.nextLine();
            for (String str : Arrays.asList(RequestResource.split(",\\s*"))) {
                RequestResourceList.add(Integer.parseInt(str));
            }

            Process process = processes.get(ProcessNumberRequest);
            Request request = new Request(process, available, RequestResourceList);

            processes.get(ProcessNumberRequest).setAllocation(request.ComputeAllocation());
            processes.get(ProcessNumberRequest).setNeed(request.ComputeNeed());
            available = request.ComputeAvailable();
        } else {
            System.out.println("Proceeding");
        }

        // Validate if deadlock
        // Validate if safe sequence

        // Output table matrix
        DisplayProcesses(processes, resourceCount);

        sc.close();
    }

    public static void DisplayProcesses(List<Process> processes, int resourceCount) {
        // Print the header
        System.out.printf("%-10s %-20s %-20s %-20s %-20s%n",
                "Process", "Allocation", "Max", "Need", "Available");

        List<String> lettersList = new ArrayList<>();
        for (int i = 0; i < resourceCount; i++) {
            lettersList.add("" + (char)('A' + i));
        }
        String letters = listToString(lettersList);

        System.out.printf("%-10s %-20s %-20s %-20s %-20s%n",
                "",
                letters,
                letters,
                letters,
                letters);

        for (int i = 0; i < processes.size(); i++) {
            Process process = processes.get(i);

            String allocation = listToString(process.getAllocation());
            String max = listToString(process.getMax());
            String need = listToString(process.getNeed());

            System.out.printf("%-10s %-20s %-20s %-20s %-20s%n",
                    process.getProcessNum(),
                    allocation,
                    max,
                    need,
                    "N/A");
        }
        System.out.println();
    }

    private static <T> String listToString(List<T> list) {
        StringBuilder sb = new StringBuilder();
        for (T element : list) {
            sb.append(element).append(" ");
        }
        return sb.toString().trim();
    }
}