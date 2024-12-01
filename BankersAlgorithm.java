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

        int[][] available = new int[processCount + 1][resourceCount];
        System.out.print("Enter the maximum for all the resources: " + "(" + letters + "): ");
        String InitialResource = sc.nextLine();
        int temp = 0;
        for (String str : Arrays.asList(InitialResource.split(",\\s*"))) {
            available[0][temp] = Integer.parseInt(str);
            temp++;
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
         * Calculate Total Allocated for each resource
         * ================
         */

        int[] TotalAllocation = new int[resourceCount];
        for (Process process : processes) {
            for (int i = 0; i < resourceCount; i++) {
                TotalAllocation[i] += process.getAllocation().get(i);
            }
        }
        /*
         * ================
         * Calculate First Available for each resource
         * ================
         */

        for (int i = 0; i < resourceCount; i++) {
            available[0][i] -= TotalAllocation[i];
        }
        /*
         * ================
         * Request Resource
         * ================
         */
        Request request = null;
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
            request = new Request(process, available, RequestResourceList);

            if (request.isViable()) {
                processes.get(ProcessNumberRequest).setAllocation(request.ComputeAllocation());
                processes.get(ProcessNumberRequest).setNeed(request.ComputeNeed());
                processes.get(ProcessNumberRequest).setRequest(true);
                available = request.ComputeAvailable();
            } else {
                System.out.println("System cannot handle request...Proceeding");
            }

        } else {
            System.out.println("Proceeding");
        }
        
        List<Integer> FinishedProcess = new ArrayList<>();
        int j = 0;
        for (int index = 0; index < processCount; index++) {
            if (FinishedProcess.size() == processCount)
                break;
            for (int i = 0; i < processCount; i++) {
                if (FinishedProcess.contains(i)) {
                    continue;
                }

                Process currentProcess = processes.get(i);

                List<Integer> CurrentNeed = currentProcess.getNeed();
                List<Integer> CurrentAllocation = currentProcess.getAllocation();
                Boolean isRequest = currentProcess.getRequest();
                int k = 0;

                while (k < resourceCount) {
                    if (CurrentNeed.get(k) > available[j][k]) {
                        if (isRequest) {
                            processes.get(i).setAllocation(request.getOriginalAllocation());
                            processes.get(i).setNeed(request.getOriginalNeed());
                            processes.get(i).setRequest(false);
                            available = request.getOriginalAvailable();
                            FinishedProcess.clear();
                            i = 0;
                            index = 0;
                            continue;
                        }
                        break;
                    }
                    k++;
                }
                if (k == resourceCount) {
                    for (int k2 = 0; k2 < resourceCount; k2++) {
                        available[j + 1][k2] = CurrentAllocation.get(k2) + available[j][k2];
                    }
                    FinishedProcess.add(processes.get(i).getProcessNum());
                    j++;
                }
            }
        }


        if (FinishedProcess.size() == processCount) {
            System.out.println("Safe State");
        } else {
            System.out.println("Unsafe");
        }

        System.out.println(Arrays.toString(FinishedProcess.toArray()));

        // Validate if deadlock
        // Validate if safe sequence

        // Output table matrix
        DisplayProcesses(processes, available);

        sc.close();
    }

    public static void DisplayProcesses(List<Process> processes, int[][] available) {
        // Print the header
        int resourceCount = available.length > 0 ? available[0].length : 0;
        System.out.printf("%-10s %-20s %-20s %-20s %-20s%n",
                "Process", "Allocation", "Max", "Need", "Available");

        List<String> lettersList = new ArrayList<>();
        for (int i = 0; i < resourceCount; i++) {
            lettersList.add("" + (char) ('A' + i));
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
            String availableRow = i < available.length ? listToString(available[i]) : "N/A";

            System.out.printf("%-10s %-20s %-20s %-20s %-20s%n",
                    process.getProcessNum(),
                    allocation,
                    max,
                    need,
                    availableRow);
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

    private static String listToString(int[] array) {
        StringBuilder sb = new StringBuilder();
        for (int num : array) {
            sb.append(num).append(" ");
        }
        return sb.toString().trim();
    }

}