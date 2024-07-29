import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.AlgorithmConstraints;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class RandomSearchAlgorithm implements Algorithm{

    private static List<TestEntity> testEntities = new ArrayList<>();

    @Override
    public Result runAlgorithm(int sampleSize,int populationSize, String filePath) throws IOException {
        Result evaluation = new Result();
        int count = 0;
        Instant instant = Instant.now();
        String line;

        File f = new File(filePath);
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
            testEntities.add(testEntity);
        }
        //creating population based on population size
        Population population = new Population(populationSize,sampleSize, testEntities, true);
        population.getFitness();
        //iterating for 10000 times
        for( int i =0 ; i < AlgorithmConstants.ITERATIONCOUNTFORRANDOM ; i++) {
            //new random population is created
            Population newPopulation = new Population(populationSize,sampleSize, testEntities, true);
            newPopulation.getFitness();
            // if average fitness is greater for new, new replaces the old population
            if(findAverageFitness(newPopulation) > findAverageFitness(population)){
                population = newPopulation;
            }
            }
        System.out.println("Solution found from RandomSearch Algorithm after iterations "+AlgorithmConstants.ITERATIONCOUNTFORRANDOM);
        System.out.println("Fittest Entity with fitness value: ");
        EntitiesWithFitness fittestEntity = population.getFittest();
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
        Duration timeElapsed = Duration.between(instant,Instant.now());
        System.out.println("Time taken by Random Search Algorithm: "+timeElapsed.toMillis()+ " Milliseconds");
        System.out.println("-----------------------------------------------------------------------------------------------------------------");
        evaluation.setDuration(timeElapsed.toMillis());
        evaluation.setIterationCount(count);
        return evaluation;
    }

    private double findAverageFitness(Population population) {
        double totalFitness = 0.0;
        for(EntitiesWithFitness entitiesWithFitness : population.getTestEntities()){
            totalFitness += entitiesWithFitness.getFitnessValue();
        }
        return totalFitness/population.getTestEntities().size();
    }

}
