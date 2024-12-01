import java.util.ArrayList;
import java.util.List;

//in using the request, have it checked if the request <= need, and request <= available;
//if (!isViable){System.out.println("Request is not viable")}

public class Request {

    private Process process;
    private List<Integer> request;
    private int[][] available;
    public List<Integer> originalAllocation;
    public List<Integer> originalNeed;
    public int[][] originalAvailable;

    public Request(Process process, int[][] available, List<Integer> request) {
        this.process = process;
        this.request = request;
        this.available = available;
        this.originalAvailable = available;
        this.originalAllocation = new ArrayList<>(process.getAllocation());
        this.originalNeed = new ArrayList<>(process.getNeed());

    }


    //processes.get(ProcessRequested).setAllocation = Request.ComputeAllocation();
    public List<Integer> ComputeAllocation() {
        List<Integer> Allocation = this.process.getAllocation();

        for (int i = 0; i < request.size(); i++) {
            int req = request.get(i);
            Allocation.set(i, (Allocation.get(i) + req));
        }

        return Allocation;
    }

    //Available = Request.ComputeAvailable();
    public int[][] ComputeAvailable() {
        int[][] Available = this.available;

        for (int i = 0; i < request.size(); i++) {
            int req = request.get(i);
            Available[0][i] -= req;
        }

        return Available;
    }

    //processes.get(ProcessRequested).setNeed = Request.ComputeNeed();
    public List<Integer> ComputeNeed() {
        List<Integer> Need = this.process.getNeed();

        for (int i = 0; i < request.size(); i++) {
            int req = request.get(i);
            Need.set(i, (Need.get(i) - req));
        }

        return Need;
    }

    public List<Integer> getOriginalAllocation(){
        return originalAllocation;
    }

    public int[][] getOriginalAvailable(){
        return originalAvailable;
    }

    public List<Integer> getOriginalNeed(){
        return originalNeed;
    }





    // if (!isViable){
    // System.out.println("Request is not viable");
    // return;
    // }
    
    public boolean isViable(){
        for (int i = 0; i < request.size(); i++) {
            if(!(request.get(i) <= process.getNeed().get(i))){
                System.out.println("Requested Resource is less the need of the process");
                return false; 
            } 
            if(!(request.get(i) <= available[0][i])){
                System.out.println("Requested Resource is less the available");
                return false; 
            }
        }
        return true;
    }
}
