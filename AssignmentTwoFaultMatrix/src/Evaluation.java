import java.io.IOException;

public class Evaluation {
    public static void main(String[] args) throws IOException {
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();
        Result geneticAlgorithmResult= geneticAlgorithm.runAlgorithm(AlgorithmConstants.SAMPLESIZEFORBIGMATRIX,300,"src/bigfaultmatrix.csv");
        //Result geneticAlgorithmResult= geneticAlgorithm.runAlgorithm(AlgorithmConstants.SAMPLESIZEFORSMALLMATRIX,300,"src/smallfaultmatrix.csv");

        HillClimbingAlgorithm hillClimbingAlgorithm = new HillClimbingAlgorithm();
        Result hillclimbingAlgoResult = hillClimbingAlgorithm.runAlgorithm(AlgorithmConstants.SAMPLESIZEFORBIGMATRIX,9,"src/bigfaultmatrix.csv");
        //Result hillclimbingAlgoResult= hillClimbingAlgorithm.runAlgorithm(AlgorithmConstants.SAMPLESIZEFORSMALLMATRIX,9,"src/smallfaultmatrix.csv");

        RandomSearchAlgorithm randomSearchAlgorithm = new RandomSearchAlgorithm();
        Result rsAlgoResult = randomSearchAlgorithm.runAlgorithm(AlgorithmConstants.SAMPLESIZEFORBIGMATRIX,9,"src/bigfaultmatrix.csv");
        //Result rsAlgoResult= randomSearchAlgorithm.runAlgorithm(AlgorithmConstants.SAMPLESIZEFORSMALLMATRIX,9,"src/smallfaultmatrix.csv");

        System.out.println("Comparison of algorithms results:");
        System.out.println("Difference in iteration between HillClimbing and GA is " +(Math.abs(hillclimbingAlgoResult.getIterationCount()-geneticAlgorithmResult.getIterationCount())));
        System.out.println("Difference in duration between HillClimbing and GA is " +(Math.abs(hillclimbingAlgoResult.getDuration()-geneticAlgorithmResult.getDuration()))+ " milliseconds");

        System.out.println("Difference in iteration between Random Search and GA is " +(Math.abs(rsAlgoResult.getIterationCount()-geneticAlgorithmResult.getIterationCount())));
        System.out.println("Difference in duration between Random Search and GA is " +(Math.abs(rsAlgoResult.getDuration()-geneticAlgorithmResult.getDuration()))+ " milliseconds");
    }
}
