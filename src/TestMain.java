import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import au.com.bytecode.opencsv.CSVReader;
import extensive_form_efg_game_format.CreateTree;
import extensive_form_game.Game;
import extensive_form_game_solver.DefenderSequenceFormLPApproximationSolver;
import utils.Utils;



/**
 * @author IASRLUser
 *
 */
public class TestMain {

	public static void main(String[] args) throws IOException {
		int numOfSimulation = 100;
		int possibleCombination = (int) Math.pow(2.0, (double) Utils.REAL_HOST_FEATURES_NUM);
		//writeFeatureDistribution(possibleCombination,numOfSimulation);
		double[][][] distributions = readDistributions("featuredristibution.txt",numOfSimulation,possibleCombination);
		double gameValue = 0.0;
		FileWriter fw = new FileWriter("resultsboth.csv");
		//FileWriter fw = new FileWriter("resultshp.csv");
			for (int i = 0; i < numOfSimulation; i++) {
				Game experimentGame = generateGameTree(true, distributions[i]);
				DefenderSequenceFormLPApproximationSolver equilibriumSolver = new DefenderSequenceFormLPApproximationSolver(
						experimentGame, 1);
				// StackleBergNaiveSolver stSolver = new StackleBergNaiveSolver<>(drpGame, 1);
				equilibriumSolver.solveGame();
				gameValue = equilibriumSolver.getGameValue();
				// stSolver.solveGame();
				try {
					equilibriumSolver.writeStrategyToFile("defenderStrategy_hp_only.txt");
					// stSolver.writeStrategyToFile("defenderStrategyStSolver.txt");
				} catch (Exception e) {
					e.printStackTrace();
				}
				String resultRes =calculateDistance(distributions[i][0], distributions[i][1]) + "," + gameValue;
				try {
					fw.write(resultRes + "\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			fw.close();
		
		
		/*
		double[][] p1Strategy = equilibriumSolver.getStrategyProfile()[1];
		BestResponseLPSolver brSolver = new BestResponseLPSolver(drpGame, 2,
				p1Strategy);
		brSolver.solveGame();
		try {
			brSolver.writeStrategyToFile("attacker_br.txt");
		} catch (IloException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DoubleOracleLPSolver equilibriumSolver = new DoubleOracleLPSolver(drpGame, 1);
		double prevP1Payoff = 0.0;
		double prevP2Payoff = 0.0;
		boolean isOnlyDefaultStrategy = true;

		int iter = 2;

		while (--iter > 0) {
			if (!isOnlyDefaultStrategy) {
				equilibriumSolver.solveGame();

				try {
					equilibriumSolver.writeStrategyToFile("defenderStrategy.txt");
				} catch (IloException e) {
					e.printStackTrace();
				}
			}

			isOnlyDefaultStrategy = false;
			TObjectDoubleMap<String>[] brSequencesMap = new TObjectDoubleMap[3];
			for (int player = 1; player < 3; player++) {

				double[][] RestricatedEquilibriumStrategy = player == 1 ? equilibriumSolver.getStrategyProfile()[2]
						: equilibriumSolver.getStrategyProfile()[1];
				BestResponseLPSolver brSolver = new BestResponseLPSolver(drpGame, player,
						RestricatedEquilibriumStrategy);
				brSolver.solveGame();
				if (player == 1)
					prevP1Payoff = brSolver.getValueOfGame();
				else
					prevP2Payoff = brSolver.getValueOfGame();
				//System.out.println("Game value" + brSolver.getValueOfGame());
				brSequencesMap[player] = brSolver.getStrategyVarMap();
				TObjectDoubleIterator<String> it = brSequencesMap[player].iterator();
				for (int i = brSequencesMap[player].size(); i-- > 0;) {
					it.advance();
					System.out.println("player : " + player + " Key : " + it.key() + " Value :" + it.value());
				}
				try {
					brSolver.writeStrategyToFile("Player-" + player + "-bestResponseStrategy.txt");

				} catch (IloException e) {
					e.printStackTrace();
				}

			}
			try {
				equilibriumSolver.updateRestrictedGame(0, brSequencesMap, new TIntHashSet(), new TIntHashSet());
				equilibriumSolver.solveRestrictedGame();
			} catch (IloException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
*/
		
	}
	
	public static double[][][] readDistributions(String filename, int numSimulation, int numCombination) {
		double[][][] featureDistributions = new double[numSimulation][2][numCombination];
		CSVReader in = null;
		int simIndex = 0;
		try {
			in = new CSVReader(new FileReader(filename), ',');
			// in = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			System.out.println("filename: " + filename);
			System.exit(0);
		}

		String[] splitLine;
		try {
			while ((splitLine = in.readNext()) != null) {
				int indexCnt = 0;
				for (String s : splitLine) {
					if (!s.equals("")) {
						double d = Double.parseDouble(s);
						//System.out.print(d+" ");
						if(indexCnt <numCombination)
							featureDistributions[simIndex][0][indexCnt%numCombination] = d;
						else
							featureDistributions[simIndex][1][indexCnt%numCombination] = d;
						++indexCnt;
					}
				}
				//System.out.println();
				simIndex++;
			}
		} catch (IOException e) {
			System.out.println("Game::CreateGameFromFile: Read exception");
		}
		return featureDistributions;
	}

	public static void writeFeatureDistribution(int numFeature, int totSimulation) throws IOException {
		FileWriter fw = new FileWriter("featuredristibution.txt");
		for (int i = 0; i < totSimulation; i++) {
			double[] probReal = generateRandomProbability(numFeature);
			double[] probHp = generateRandomProbability(numFeature);
			String probString = "";
			for (int j = 0; j < numFeature; j++) {
				probReal[j] = (Math.round(probReal[j] * 100.0)) / 100.0;
				probString = probString + probReal[j] + ",";
			}
			for (int j = 0; j < numFeature; j++) {
				probHp[j] = (Math.round(probHp[j] * 100.0)) / 100.0;
				probString = probString + probHp[j] + ",";
			}
			fw.write(probString+"\n");
		}
		fw.close();
	}

	public static Game generateGameTree(boolean isBothHost, double[][] fDistribution) {
		CreateTree treeOnlyHp = new CreateTree("games/hsg_game", isBothHost, fDistribution[0],
				fDistribution[1]);
		treeOnlyHp.init();
		treeOnlyHp.closeFile();
		Game hfsGame = new Game();
		hfsGame.createGameFromFileZerosumPackageFormat("games/hsg_game.efg");
		return hfsGame;
	}
	
	public static double[] generateRandomProbability(int n) {
		double a[] = new double[n];
		double s = 0.0d;
		Random random = new Random();
		for (int i = 0; i < n; i++) {
			a[i] = 1.0d - random.nextDouble();
			a[i] = -1 * Math.log(a[i]);
			s += a[i];
		}
		for (int i = 0; i < n; i++) {
			a[i] /= s;
		}
		return a;
	}
	
	public static double calculateDistance(double[] array1, double[] array2)
    {
        double Sum = 0.0;
        for(int i=0;i<array1.length;i++) {
           Sum = Sum + Math.pow((array1[i]-array2[i]),2.0);
        }
        return Math.sqrt(Sum);
    }

}