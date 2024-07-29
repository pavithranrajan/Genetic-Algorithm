import java.util.ArrayList;
import java.util.List;


public class Population {

    private List<EntitiesWithFitness> testEntities;

    public List<EntitiesWithFitness> getTestEntities() {
        return testEntities;
    }

    public void setTestEntities(List<EntitiesWithFitness> testEntities) {
        this.testEntities = testEntities;
    }

    public Population(int size, int sampleSize, List<TestEntity> testEntities, boolean createNew) {
        this.testEntities = new ArrayList<>();
        //creates new random population based on size, only when the createNew is true otherwise returns initialized list
        if (createNew) {
            createNewPopulation(size,sampleSize,testEntities);
        }
    }



    private void createNewPopulation(int size, int sampleSize, List<TestEntity> dataSets) {
        for(int i=0; i < size; i++) {
            EntitiesWithFitness testEntityList = EntitiesWithFitness.getSampleTestEntities(sampleSize,dataSets);
            //This condition takes care of not adding duplicate EntitiesWithFitness to the population.
            if(!testEntities.contains(testEntityList)) {
                this.testEntities.add(testEntityList);
                }else {
                    i--;
                }
        }
    }

    public void getFitness() {
        for(EntitiesWithFitness entities : testEntities){
            entities.getFitness();
        }
    }

    //This method returns the EntitiesWithFitness from the List in population which has maximum fitness value
    protected EntitiesWithFitness getFittest() {
        EntitiesWithFitness fittest = testEntities.get(0);
        for (int i = 0; i < testEntities.size(); i++) {
            if (fittest.getFitnessValue() <= testEntities.get(i).getFitnessValue()) {
                fittest = testEntities.get(i);
            }
        }
        return fittest;
    }
}
