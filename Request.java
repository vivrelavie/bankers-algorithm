import java.util.List;

//in using the request, have it checked if the request <= need, and request <= available;
//if (!isViable){System.out.println("Request is not viable")}

public class Request {

    private Process process;
    private List<Integer> request;
    private List<Integer> available;
    public List<Integer> originalAllocation;
    public List<Integer> originalNeed;
    public List<Integer> originalAvailable;

    public Request(Process process, List<Integer> available, List<Integer> request) {
        this.process = process;
        this.request = request;
        this.available = available;
        this.originalAvailable = available;
        this.originalAllocation = process.getAllocation();
        this.originalNeed = process.getNeed();

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
    public List<Integer> ComputeAvailable() {
        List<Integer> Available = this.available;

        for (int i = 0; i < request.size(); i++) {
            int req = request.get(i);
            available.set(i, (available.get(i) - req));
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
            if(!(request.get(i) <= available.get(i))){
                System.out.println("Requested Resource is less the available");
                return false; 
            }
        }
        return true;
    }

}
