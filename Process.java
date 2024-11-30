import java.util.ArrayList;
import java.util.List;

public class Process {
    private int ProcessNum ;
    private List<Integer> Allocation = new ArrayList<>();
    private List<Integer> Max;
    private List<Integer> Need;
    
    public Process(int ProcessNum, List<Integer> Allocation, List<Integer> Max) {
        this.ProcessNum = ProcessNum;
        this.Allocation = new ArrayList<>(Allocation);
        this.Max = new ArrayList<>(Max);
        calculateNeed();
    }

    public void calculateNeed(){
        Need = new ArrayList<>();
        for(int i = 0; i < Max.size(); i++){
            Need.add(Max.get(i) - Allocation.get(i));
        }
    }

    public int getProcessNum(){
        return ProcessNum;
    }

    public List<Integer> getAllocation(){
        return Allocation;
    }

    public List<Integer> getMax(){
        return Max;
    }

    public List<Integer> getNeed(){
        return Need;
    }

    public void setNeed(List<Integer> need){
        Need = need;
    }

    public void setAllocation(List<Integer> allocation){
        Allocation = allocation;
    }
}
