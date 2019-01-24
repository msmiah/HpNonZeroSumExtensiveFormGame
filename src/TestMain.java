import extensive_form_efg_game_format.CreateTree;
import extensive_form_game.Game;
import extensive_form_game_solver.DefenderSequenceFormLPApproximationSolver;



/**
 * @author IASRLUser
 *
 */
public class TestMain {

	public static void main(String[] args) {

		CreateTree tree = new CreateTree();
		tree.init();
		tree.closeFile();

		Game drpGame = new Game();
		drpGame.createGameFromFileZerosumPackageFormat("hsg_4_features.efg");
		DefenderSequenceFormLPApproximationSolver equilibriumSolver = new DefenderSequenceFormLPApproximationSolver(drpGame, 1);
		//StackleBergNaiveSolver stSolver = new StackleBergNaiveSolver<>(drpGame, 1);
		equilibriumSolver.solveGame();
		//stSolver.solveGame();
		try {
			equilibriumSolver.writeStrategyToFile("defenderStrategy.txt");
			//stSolver.writeStrategyToFile("defenderStrategyStSolver.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
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

}