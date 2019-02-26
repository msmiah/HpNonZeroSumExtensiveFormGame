//import util.ml.LpSolveUtil;
import gurobi.GRBEnv;
import gurobi.GRBModel;
import lpsolve.LpSolve;
import lpsolve.LpSolveException;

/**
 * Earth Mover's Distance
 * input: two distributions, p_m and q_n, and a unit distance d_{ij} = d(p_i, q_j)
 * 
 *
 */
public class EMD {
	/**
	 * 
	 * @param d : unit distance matrix
	 * @return
	 */
	public static double distance(double[][] d, double[] wp, double[] wq){
		assert (d != null);
		assert(wp != null);
		assert (wq != null);
		int m = d.length, n = d[0].length;
		assert(wp.length == m);
		assert(wq.length == n);
//		double[][] f = new double[m][n];
		double res = 0;
		/*
		 * use lpsolve to solve the linear program
		 * obj: \sum_i \sum_j f_ij d_ij
		 * s.t.: f_ij >= 0
		 * 	\sum_j f_ij <= wp_i
		 * 	\sum_i f_ij >= wq_j
		 * 	\sum_i \sum_j f_ij = min(\sum_i wp_i, \sum_j wq_j);
		 */



		// 2*(m+n)+1 constraints in total
		try {
			LpSolve solver = LpSolve.makeLp((m*n+m+n)+1, m*n);
			// add constraints 
			// f_ij >= 0
			// TODO: find an easier way to enforce non-negative constraints
			for (int i = 0; i < m ; i++){
				for (int j = 0; j < n; j++){
					double[] coef = new double[m*n+1];
					coef[i*n + j+1] = 1;
					solver.addConstraint(coef, LpSolve.GE, 0);
				}
			}
			// \sum_j f_ij <= wp_i
			for (int i = 0; i < m; i++){
				double[] coef = new double[m*n+1];
				for (int j = 0; j < n; j++){
					coef[i*n + j+1] = 1;
				}
				solver.addConstraint(coef, LpSolve.LE, wp[i]);
			}
			// 	\sum_i f_ij >= wq_j
			for (int j = 0; j < n; j++){			
				double[] coef = new double[m*n+1];
				for (int i = 0; i < m; i++){
					coef[i*n + j+1] = 1;
				}
				solver.addConstraint(coef, LpSolve.LE, wq[j]);
			}
			// 	\sum_i \sum_j f_ij = min(\sum_i wp_i, \sum_j wq_j);
			double val = Double.MAX_VALUE, sum = 0;
			for (int i = 0; i < m; i++){
				sum += wp[i];
			}
			val = sum;
			sum = 0;
			for (int j = 0; j < n; j++){	
				sum += wq [j];
			}
			if (val > sum){
				val = sum;
			}
			double[] coef = new double[m*n+1];
			for (int i = 0; i < m; i++){
				for (int j = 0; j < n; j++){
					coef[i*n + j+1] = 1;
				}
			}
			solver.addConstraint(coef, LpSolve.EQ, val);
			
			// set objective function
			double[] objParams = new double[m*n+1];
			for (int i = 0; i < m ; i++){
				System.arraycopy(d[i], 0, objParams, i*n+1, n);
			}
			solver.addConstraint(objParams, LpSolve.LE, 1);
			solver.setObjFn(objParams);
			

			//solver.printLp();
			solver.setSolutionlimit(12);
			solver.solve();
		
			//LpSolveUtil.print(solver);
			res = computeDistance(d, solver.getPtrVariables());
			
		} catch (LpSolveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return res;
	}

	public static double distance(double[][] d){
		double[] wp = new double[d.length], wq = new double[d[0].length];
		for (int i = 0; i < wp.length; i++) wp[i] = 1;
		for (int i = 0; i < wq.length; i++) wq[i] = 1;
		return distance(d, wp, wq);
	}

	/**
	 * 
	 * @param d
	 * @param f : needs to be positive in dimension m*n
	 * @return
	 */
	private static double computeDistance(double[][] d, double[] f){
		double r = 0, q = 0;
		int n = d[0].length;
		for (int i = 0; i < d.length; i++){
			for (int j = 0; j < d[0].length; j++){
				r += d[i][j] * f[i*n+j];
				q += f[i*n+j];
			}
		}
		System.out.println("EMD distance = "+r/q);
		return r/q;
	}
}