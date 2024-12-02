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
        /*
         * ================
         * Perform bankers algorithm
         * ================
         */
        List<Integer> FinishedProcess = new ArrayList<>();
        int j = 0;
        int requestedProcess = -1;
        boolean rollback = false;

        for (int index = 0; index < processCount + 1; index++) { //processCount + 1 to give a chance to rollback
            if (FinishedProcess.size() == processCount) {
                break; //if nakahanap na safe sequence end na
            }
            for (int i = 0; i < processCount; i++) {
                if (FinishedProcess.contains(i)) {
                    continue; //skip finished processes
                }
                Process currentProcess = processes.get(i);
                System.out.println("\nNow Processing Process " + currentProcess.getProcessNum());
                List<Integer> CurrentNeed = currentProcess.getNeed();
                List<Integer> CurrentAllocation = currentProcess.getAllocation();
                if (processes.get(i).getRequest()) {
                    requestedProcess = i; //get the number of the requested process (if there is)
                }

                //compare instance of available to every instance of need
                int k = 0; 
                while (k < resourceCount) {
                    if (CurrentNeed.get(k) > available[j][k]) {
                        System.out.println("Process " + currentProcess.getProcessNum() + " Can't be processed");
                        break; //break if a need is less than available
                    }
                    k++;
                }

                if (k == resourceCount) { //if all available is > need of current process
                    System.out.println("Process " + currentProcess.getProcessNum() + " Can be processed\n");
                    System.out.println("Updating Available");
                    for (int k2 = 0; k2 < resourceCount; k2++) {
                        System.out.println((char) ('A' + k2) + ": " + available[j][k2] + " -> " + (CurrentAllocation.get(k2) + available[j][k2]));                        available[j + 1][k2] = CurrentAllocation.get(k2) + available[j][k2]; //populate available

                    }
                    FinishedProcess.add(processes.get(i).getProcessNum()); //add to finished process
                    System.out.println("Finished Processes: " + Arrays.toString(FinishedProcess.toArray()));
                    j++;
                }
            }

            //for request rollback if unsafe state
            boolean isSafe = FinishedProcess.size() == processCount;
            if (!isSafe && requestedProcess >= 0 && !rollback) {
                // rollback
                processes.get(requestedProcess).setAllocation(request.originalAllocation);
                processes.get(requestedProcess).setNeed(request.originalNeed);
                clearArray(available); //clear available
                available = request.getOriginalAvailable(); //repopulate available
                FinishedProcess.clear();
                index = 0; //reset loop
                rollback = true; //set rollback to true to ensure rollback only happens once
                j = 0;
                System.out.println("Due to the request, there is no safe sequence");
                System.out.println("Now Rolling Back");
                continue;
            }
            System.out.println(index);
            if (index == processCount && !isSafe && rollback){
                System.out.println("No Safe Sequence found");
                break; //if not safe and rollback happened, then there is no safe sequence
            }
        }

        System.out.println("\n\nFinal Matrix: ");
        DisplayProcesses(processes, available);
        System.out.println("Finished Processes: " + Arrays.toString(FinishedProcess.toArray()));

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
    private static int[][] clearArray(int[][] array){
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                array[i][j] = 0;
            }
        }
        return array;
    }
}