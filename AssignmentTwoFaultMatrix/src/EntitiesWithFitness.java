import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/* This class has list of Test Entity along with fitness values
* for example a given EntitiesWithFitness will have
* 1 - > t1083, faultmatrix and lineNum from the test file
* 1 - > t101, faultmatrix and lineNum from the test file
* fitness value has APFD calculated for all test entities in EntitiesWithFitness*/
public class EntitiesWithFitness {
    private List<TestEntity> entitiesList;
    private double fitnessValue;
    static Random rand = new Random();

    public List<TestEntity> getEntitiesList() {
        return entitiesList;
    }

    public void setEntitiesList(List<TestEntity> entitiesList) {
        this.entitiesList = entitiesList;
    }

    public double getFitnessValue() {
        return fitnessValue;
    }

    /* getSampleTestEntities creates sample size EntitiesWithFitness
    * based on the sampleSize param from the whole datasets
    * from entire fault matrix file */
    public static EntitiesWithFitness getSampleTestEntities(int size, List<TestEntity> dataSets){
        EntitiesWithFitness testEntities = new EntitiesWithFitness();
        List<TestEntity> testEntityList = new ArrayList<>();
        for(int i=0; i< size;i++){
            TestEntity testEntity = dataSets.get(rand.nextInt(dataSets.size()));
            if(!testEntityList.contains(testEntity)){
                testEntityList.add(testEntity);
            }else{
                i--;
            }
        }
        testEntities.setEntitiesList(testEntityList);
        return testEntities;
    }

    /*This fitness function calculates fitness value of a
    * collection of TestEntity (collection of test cases with fault matrix)
    * based on APFD calculation*/
    public void getFitness(){
        int totalTestCases = 0;
        List<Integer> noOfFaultsList = new ArrayList<>();

        //this list is initialised to all 0's and gets updated when a fault is found for first time
        for(int k=0;k<entitiesList.get(0).getFaultMatrixData().size();k++){
            noOfFaultsList.add(0);
        }

        for(int i=0; i< entitiesList.size();i++){
            for(int j=0; j<entitiesList.get(i).getFaultMatrixData().size();j++){
                //updating the faultList to 1 only when it's value is still 0
                if(entitiesList.get(i).getFaultMatrixData().get(j) == 1 && noOfFaultsList.get(j)==0){
                    totalTestCases += i+1;
                    noOfFaultsList.set(j,1);
                }
            }
        }
        //increasing the total test cases value when the fault lst still has 0
        for(int i=0; i<noOfFaultsList.size();i++){
            if(noOfFaultsList.get(i) == 0){
                totalTestCases += entitiesList.size()+1;
            }
        }
        //APFD calsulation to return the fitness value (double datatype)
        double intermediateValue = (double)totalTestCases / (entitiesList.size() * entitiesList.get(0).getFaultMatrixData().size());
        double secondaryValue = (double) 1.0 /  (2 * entitiesList.size());
        this.fitnessValue = 1.0 - intermediateValue + secondaryValue;
    }
}
