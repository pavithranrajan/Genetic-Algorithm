import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm implements Algorithm{
    static Random rand = new Random();
    private static List<TestEntity> testEntities = new ArrayList<>();
    private int sampleSize;

    @Override
    public Result runAlgorithm(int sampleSize, int populationSize, String filePath) throws IOException {
        Instant startTime = Instant.now();
        this.sampleSize = sampleSize;
        String line;
        File f = new File(filePath);
        //converting the file from txt to csv only if csv file doesn't exist in src path
        if(!f.exists()){
            FileConverter.convertTxtToCsv(filePath);
        }
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        while ((line = br.readLine()) != null)
        {
            String[] testData = line.split(",");
            TestEntity testEntity = new TestEntity();
            testEntity.setTestCaseId(testData[0]);
            List<Integer> faultMatrixList = new ArrayList<>();
            for(int i=1;i<testData.length;i++){
                faultMatrixList.add(Integer.parseInt(testData[i]));
            }
            testEntity.setFaultMatrixData(faultMatrixList);
            //testentities has list of TestEntity - > testcaseId along with fault matrix
            testEntities.add(testEntity);
        }
        Population myPop = new Population(populationSize, sampleSize,testEntities,true);
        myPop.getFitness();
        int generationCount = 1;
        //Iterating the evolution for 1000 times because there is no definite goal to achieve
        while(generationCount < AlgorithmConstants.ITERATIONCOUNT){
            //evolving the population
            myPop = evolvePopulation(myPop);
            myPop.getFitness();
            generationCount++;
        }
        System.out.println("Solution found from Genetic Algorithm after 1000 iteration");
        System.out.println("Generation: " + generationCount);
        System.out.println("Fittest Entity with fitness value: ");
        EntitiesWithFitness fittestEntity = myPop.getFittest();
        System.out.println(fittestEntity.getFitnessValue());
        for(TestEntity entity : fittestEntity.getEntitiesList()){
            System.out.println("Test case id: "+entity.getTestCaseId());
            StringBuffer stringBuffer = new StringBuffer();
            for(Integer faultMatrix : entity.getFaultMatrixData()){
                stringBuffer.append(faultMatrix);
                stringBuffer.append(",");
            }
            stringBuffer.deleteCharAt(stringBuffer.length()-1);
            System.out.println("Fault Matrix: "+stringBuffer.toString());
        }
        Duration timeElapsed = Duration.between(startTime, Instant.now());
        System.out.println("Time taken: "+ timeElapsed.toMillis()+" milliseconds");
        System.out.println("-----------------------------------------------------------------------------------------------------------------");
        Result evaluation = new Result();
        evaluation.setDuration(timeElapsed.toMillis());
        evaluation.setIterationCount(generationCount);

        Result evaluation1 = new Result();
        evaluation1.setDuration(timeElapsed.toMillis());
        evaluation1.setIterationCount(generationCount);

        evaluation1.getDuration();
        return evaluation;
    }

    public Population evolvePopulation(Population pop) {
        int elitismOffset;
        //retaining the best EntitiesWithFitness which has maximum fitness value - Elitism concept
        Population newPopulation = new Population(pop.getTestEntities().size(),0, testEntities, false);
        if (AlgorithmConstants.ELITISM) {
            newPopulation.getTestEntities().add(0, pop.getFittest());
            elitismOffset = 1;
        } else {
            elitismOffset = 0;
        }

        for (int i = elitismOffset; i < pop.getTestEntities().size(); i++) {
            // selecting 2 EntitiesWithFitness which has maximum fitness from random sets of tournament (10)
            EntitiesWithFitness testEntityWithFitness1 = tournamentSelection(pop);
            EntitiesWithFitness testEntityWithFitness2 = tournamentSelection(pop);
            //Crossbreeding 2 fittest to get one EntitiesWithFitness
            EntitiesWithFitness newEntityWithFitness = crossover(testEntityWithFitness1, testEntityWithFitness2);
            newPopulation.getTestEntities().add(i, newEntityWithFitness);
        }

        for (int i = elitismOffset; i < newPopulation.getTestEntities().size(); i++) {
            //mutation on new population
            mutate(newPopulation.getTestEntities().get(i));
        }

        return newPopulation;
    }

    private void mutate(EntitiesWithFitness entitiesWithFitness) {
        List<TestEntity> testEntities = entitiesWithFitness.getEntitiesList();
        for (int i = 0; i < testEntities.size(); i++) {
            //mutate based on mutation rate condition
            if (Math.random() <= AlgorithmConstants.MUTATIONRATE) {
                int index1 = rand.nextInt(0,entitiesWithFitness.getEntitiesList().size()-1);
                int index2 = rand.nextInt(0,entitiesWithFitness.getEntitiesList().size()-1);
                if(index1 == index2){
                    index2 = rand.nextInt(0,entitiesWithFitness.getEntitiesList().size());
                }
                // randomly selecting 2 testEntities (testcaseId and faultMatrix from EntitiesWithFitness and swap the places)
                TestEntity temp1 = testEntities.get(index1);
                TestEntity temp2 = testEntities.get(index2);
                testEntities.remove(index1);
                testEntities.add(index1,temp2);
                testEntities.remove(index2);
                testEntities.add(index2,temp1);
            }
        }
    }

    private EntitiesWithFitness crossover(EntitiesWithFitness testEntityWithFitness1, EntitiesWithFitness testEntityWithFitness2) {
        EntitiesWithFitness newEntityWithFitness = new EntitiesWithFitness();
        List<TestEntity> testEntities = new ArrayList<>();
        for (int i = 0; i < sampleSize; i++) {
            //picking the testEntities from sample 1 or 2 based on crossOver rate condition
            if (Math.random() <= AlgorithmConstants.CROSSOVERRATE) {
                //this if condition to avoid duplicates
                if(!testEntities.contains(testEntityWithFitness1.getEntitiesList().get(i))) {
                    testEntities.add(testEntityWithFitness1.getEntitiesList().get(i));
                } else{
                    testEntities.add(testEntityWithFitness2.getEntitiesList().get(i));
                }
            } else {
                //this if condition to avoid duplicates
                if(!testEntities.contains(testEntityWithFitness2.getEntitiesList().get(i))) {
                    testEntities.add(testEntityWithFitness2.getEntitiesList().get(i));
                } else{
                    testEntities.add(testEntityWithFitness1.getEntitiesList().get(i));
                }
            }
        }
        newEntityWithFitness.setEntitiesList(testEntities);
        return newEntityWithFitness;
    }

    private EntitiesWithFitness tournamentSelection(Population pop) {
        Population tournament = new Population(AlgorithmConstants.TOURNAMENTSIZE, 0,testEntities, false);
        for (int i = 0; i < AlgorithmConstants.TOURNAMENTSIZE; i++) {
            int randomId = (int) (Math.random() * pop.getTestEntities().size());
            tournament.getTestEntities().add(pop.getTestEntities().get(randomId));
        }
        EntitiesWithFitness fittest = tournament.getFittest();
        return fittest;
    }
}
