import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class HillClimbingAlgorithm implements Algorithm{

    static Random rand = new Random();
    private static List<TestEntity> testEntities = new ArrayList<>();
    int lineNum = 1;

    @Override
    public Result runAlgorithm(int sampleSize,int populationSize, String filePath) throws IOException {
        Instant startTime = Instant.now();
        String line;

        File f = new File(filePath);
        if(!f.exists()){
            FileConverter.convertTxtToCsv(filePath);
        }
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        while ((line = br.readLine()) != null)
        {
            TestEntity testEntity = createTestEntitiesList(line, lineNum);
            testEntities.add(testEntity);
            lineNum++;
        }
        Population initialState = new Population(populationSize, sampleSize,testEntities, true);
        initialState.getFitness();
        int iterationCount = 1;
        while (iterationCount < AlgorithmConstants.ITERATIONCOUNT) {
            //Evaluating the neighbourhood of the initial population
            Population nextNeighbour = evaluateNeighbourhood(initialState,filePath);
            nextNeighbour.getFitness();
            initialState = nextNeighbour;
            iterationCount++;
        }
        System.out.println("Solution found from HillClimbingAlgorithm Algorithm after 1000 iteration");
        System.out.println("Generation: " + iterationCount);
        System.out.println("Fittest Entity with fitness value: ");
        EntitiesWithFitness fittestEntity = initialState.getFittest();
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
        evaluation.setIterationCount(iterationCount);
        return evaluation;
    }

    private TestEntity createTestEntitiesList(String line, int n) {
        String[] testData = line.split(",");
        TestEntity testEntity = new TestEntity();
        testEntity.setTestCaseId(testData[0]);
        List<Integer> faultMatrixList = new ArrayList<>();
        for(int i=1;i<testData.length;i++){
            faultMatrixList.add(Integer.parseInt(testData[i]));
        }
        testEntity.setFaultMatrixData(faultMatrixList);
        testEntity.setLineNumber(n);
        return testEntity;
    }

    /* first reading the next neighbour (List of EntitiesWithFitness) of the old population and
    * calculates the fitness value, if greater than current fitness then replace the new neighbour
    * into the existing population, if not find previous neighbour and looping the same*/
    private Population evaluateNeighbourhood(Population initialState, String filePath) throws IOException {
        List<EntitiesWithFitness> initialEntitiesWithFitnesses = initialState.getTestEntities();
        List<EntitiesWithFitness> neighbourhoodEntities = new ArrayList<>();
        for(int i=0; i< initialEntitiesWithFitnesses.size();i++){
            double currentFitness = initialEntitiesWithFitnesses.get(i).getFitnessValue();
            List<TestEntity> neighbourEntities = new ArrayList<>();
            for(TestEntity entity:initialEntitiesWithFitnesses.get(i).getEntitiesList()){
                //reading next entity from file
                int n = entity.getLineNumber()+1;
                if(n >= lineNum-1){
                    n = rand.nextInt(1,lineNum-1);
                }
                String line = "";
                line = Files.readAllLines(Paths.get(filePath)).get(n-1);
                neighbourEntities.add(createTestEntitiesList(line,n));
            }
            //calculating fitness for neighbour
            double nextNeighbourFitness = getNextNeighbourFitness(neighbourEntities);
            //if lesser than population fitness
            if(nextNeighbourFitness <= currentFitness){
                neighbourEntities.clear();
                for(TestEntity entity:initialEntitiesWithFitnesses.get(i).getEntitiesList()){
                    //read the previous neighbour
                    int n = entity.getLineNumber()-1;
                    if(n >= lineNum-1 || n==0){
                        n = rand.nextInt(1,lineNum-1);
                    }
                    String line = "";
                    line = Files.readAllLines(Paths.get(filePath)).get(n-1);
                    neighbourEntities.add(createTestEntitiesList(line,n));
                }
                nextNeighbourFitness = getNextNeighbourFitness(neighbourEntities);
            }

            //adding the new neighbour to poplation list based on fitness condition
            if(nextNeighbourFitness > currentFitness){
                EntitiesWithFitness entities = new EntitiesWithFitness();
                entities.setEntitiesList(neighbourEntities);
                neighbourhoodEntities.add(entities);
            } else{
                neighbourhoodEntities.add(initialEntitiesWithFitnesses.get(i));
            }
        }
        initialState.getTestEntities().clear();
        initialState.setTestEntities(neighbourhoodEntities);
        return initialState;
    }

    private double getNextNeighbourFitness(List<TestEntity> neighbourEntities) {
        EntitiesWithFitness entities = new EntitiesWithFitness();
        entities.setEntitiesList(neighbourEntities);
        entities.getFitness();
        return entities.getFitnessValue();
    }
}

