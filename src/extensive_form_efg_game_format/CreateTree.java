package extensive_form_efg_game_format;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.*;
import java.lang.*;

import javax.rmi.CORBA.Util;

import extensive_form_filemanager.CreateGambitEFGFile;
import utils.Utils;


public class CreateTree {
	private ArrayList<String> mChnaceNodeActionList;
	private ArrayList<String> realHostConfigList;
	private ArrayList<String> honeypotConfigLIst;
	private ArrayList<String> featuresVector;
	private ArrayList<Double> mChanceNodeProbablityList;
	private CreateGambitEFGFile createGambitFile;
	private Hashtable<String, Integer> mBinarytoIntNumbers; 
	public Hashtable<String, Double> realSystemValues;
	public Hashtable<String, Double> honeypotValues;
	public Hashtable<String, Double> realSystemProbabilities;
	public Hashtable<String, Double> honeypotProbabilites;
	public Hashtable<String,String> p2InformationSet;
	private Node mChanceNode;
	private int mChaceInfoSetNo = 1;
	private int mTotalFeatures = Utils.TOTAL_FEATUES_NUMBER_IN_GAME;
	private int mOutcomeCnt;
	private ArrayList<Double> modificationCost;

	private boolean isModifyRealSystem = false;
	private boolean isModifyBothSystem = true;

	public void init() { 
		mOutcomeCnt = 0;
		mChnaceNodeActionList = new ArrayList<String>();
		realHostConfigList = new ArrayList<String>();
		honeypotConfigLIst = new ArrayList<String>();
		mChanceNodeProbablityList = new ArrayList();
		featuresVector = new ArrayList();
		modificationCost = new ArrayList();
		createGambitFile = new CreateGambitEFGFile("hsg_4_features");
		mBinarytoIntNumbers = new Hashtable<>(); 
		realSystemValues = new Hashtable<>();
		realSystemProbabilities = new Hashtable<>();
		honeypotValues = new Hashtable<>();
		honeypotProbabilites = new Hashtable<>();
		p2InformationSet = new Hashtable<>();
		setSystemValues();
		int numChanceNode = (int)Math.pow(2.0,(double) mTotalFeatures)*2;
		double[] prob = generateRandomProbability(numChanceNode);
		for (int i = 0; i < prob.length; i++) {
			//double tmp = (Math.round(prob[i] * 100.0)) / 100.0; //Random nature probability generation
			double tmp = ((1.0/prob.length) * 100.0) / 100.0;
			//System.out.println(tmp);
			//mChanceNodeProbablityList.add(tmp);
		}
		//setP2InformationSet();
		setModificationCost();
		setProbability();
		generateBinaryRepresentation(0, Utils.REAL_HOST_FEATURES_NUM,realHostConfigList);
		generateBinaryRepresentation(0, Utils.TOTAL_FEATUES_NUMBER_IN_GAME, featuresVector);
		
		generateNatureActions(Utils.TOTAL_FEATUES_NUMBER_IN_GAME);
		//generateNatureActions();
		
		mChanceNode = new Node(Utils.CHANCE_NODE_NAME, mChaceInfoSetNo, mChnaceNodeActionList,
				mChanceNodeProbablityList, 0);
		createGambitFile.createChanceNode(mChanceNode.getNodeName(), mChanceNode.getInfoSetNumber(),
				mChanceNode.getActionsList(), mChanceNode.getProbabilitiees(), 0);

		/*
		 * mChnaceNodeActionList.add("A"); mChnaceNodeActionList.add("B");
		 * mChanceNodeProbablityList.add(0.5); mChanceNodeProbablityList.add(0.5);
		 * createGambitFile.createChanceNode("c", 1, mChnaceNodeActionList,
		 * mChanceNodeProbablityList, 0);
		 */
		movePlayerOne();
	}
	
	/* index 0 used for the cost rihtmost bit of network and so on*/
	public void setModificationCost() {

		
			modificationCost.add(1.0); // HP system modification cost
			modificationCost.add(5.0); // real modification cost
		}
	 
	public void setProbability() {   
		
		if (Utils.REAL_HOST_FEATURES_NUM == 1) {
			realSystemProbabilities.put("0", 1.0);
			realSystemProbabilities.put("1", 1.0); 

			honeypotProbabilites.put("0", 1.0);
			honeypotProbabilites.put("1", 1.0);
		} else if (Utils.REAL_HOST_FEATURES_NUM == 2) {

			realSystemProbabilities.put("00", 0.1);
			realSystemProbabilities.put("01", 0.7);
			realSystemProbabilities.put("10", 0.2);
			realSystemProbabilities.put("11", 0.1);

			honeypotProbabilites.put("00", 0.1);
			honeypotProbabilites.put("01", 0.1);
			honeypotProbabilites.put("10", 0.7);
			honeypotProbabilites.put("11", 0.1);
		} else if (Utils.REAL_HOST_FEATURES_NUM == 3) {

			realSystemProbabilities.put("000", 0.1); 
			realSystemProbabilities.put("001", 0.1);
			realSystemProbabilities.put("010", 0.1);
			realSystemProbabilities.put("011", 0.1);
			realSystemProbabilities.put("100", 0.2);
			realSystemProbabilities.put("101", 0.2);
			realSystemProbabilities.put("110", 0.1);
			realSystemProbabilities.put("111", 0.1);

			honeypotProbabilites.put("000", 0.1);
			honeypotProbabilites.put("001", 0.1);
			honeypotProbabilites.put("010", 0.1);
			honeypotProbabilites.put("011", 0.1);
			honeypotProbabilites.put("100", 0.1);
			honeypotProbabilites.put("101", 0.1);
			honeypotProbabilites.put("110", 0.2);
			honeypotProbabilites.put("111", 0.2);
		} else if (Utils.REAL_HOST_FEATURES_NUM == 4) {

			realSystemProbabilities.put("0000", 0.1);
			realSystemProbabilities.put("0001", 0.1); 
			realSystemProbabilities.put("0010", 0.1);
			realSystemProbabilities.put("0011", 0.1);
			realSystemProbabilities.put("0100", 0.1);
			realSystemProbabilities.put("0101", 0.1);
			realSystemProbabilities.put("0110", 0.1);
			realSystemProbabilities.put("0111", 0.1);
			realSystemProbabilities.put("1000", 0.1);
			realSystemProbabilities.put("1001", 0.1);
			realSystemProbabilities.put("1010", 0.0);
			realSystemProbabilities.put("1011", 0.0);
			realSystemProbabilities.put("1100", 0.0);
			realSystemProbabilities.put("1101", 0.0);
			realSystemProbabilities.put("1110", 0.0);
			realSystemProbabilities.put("1111", 0.0);

			honeypotProbabilites.put("0000", 0.0);
			honeypotProbabilites.put("0001", 0.0);
			honeypotProbabilites.put("0010", 0.0);
			honeypotProbabilites.put("0011", 0.0);
			honeypotProbabilites.put("0100", 0.0);
			honeypotProbabilites.put("0101", 0.0);
			honeypotProbabilites.put("0110", 0.0);
			honeypotProbabilites.put("0111", 0.0);
			honeypotProbabilites.put("1000", 0.0);
			honeypotProbabilites.put("1001", 0.0);
			honeypotProbabilites.put("1010", 1.0);
			honeypotProbabilites.put("1011", 0.0);
			honeypotProbabilites.put("1100", 0.0);
			honeypotProbabilites.put("1101", 0.0);
			honeypotProbabilites.put("1110", 0.0);
			honeypotProbabilites.put("1111", 0.0);
		}
		
	}
	
	public void setSystemValues() {

		if (Utils.REAL_HOST_FEATURES_NUM == 1) {
			realSystemValues.put("0", 5.0);
			realSystemValues.put("1", 5.0);

			honeypotValues.put("0", 2.0);
			honeypotValues.put("1", 2.0
					);
		} else if (Utils.REAL_HOST_FEATURES_NUM == 2) {

			realSystemValues.put("00", 10.0);
			realSystemValues.put("01", 10.0); 
			realSystemValues.put("10", 10.0);
			realSystemValues.put("11", 10.0);

			honeypotValues.put("00", 10.0);
			honeypotValues.put("01", 10.0);
			honeypotValues.put("10", 10.0);
			honeypotValues.put("11", 10.0);
		} else if (Utils.REAL_HOST_FEATURES_NUM == 3) {
			realSystemValues.put("000", 500.);
			realSystemValues.put("001", 500.);
			realSystemValues.put("010", 500.); 
			realSystemValues.put("011", 500.);
			realSystemValues.put("100", 500.);
			realSystemValues.put("101", 500.);
			realSystemValues.put("110", 500.);
			realSystemValues.put("111", 500.);

			honeypotValues.put("000", 100.);
			honeypotValues.put("001", 100.);
			honeypotValues.put("010", 100.);
			honeypotValues.put("011", 100.);
			honeypotValues.put("100", 100.);
			honeypotValues.put("101", 100.);
			honeypotValues.put("110", 100.);
			honeypotValues.put("111", 100.);
		} else if (Utils.REAL_HOST_FEATURES_NUM == 4) {

			realSystemValues.put("0000", 500.);
			realSystemValues.put("0001", 500.);
			realSystemValues.put("0010", 500.);
			realSystemValues.put("0011", 500.);
			realSystemValues.put("0100", 500.);
			realSystemValues.put("0101", 500.);
			realSystemValues.put("0110", 500.);
			realSystemValues.put("0111", 500.);
			realSystemValues.put("1000", 500.);
			realSystemValues.put("1001", 500.);
			realSystemValues.put("1010", 500.);
			realSystemValues.put("1011", 500.);
			realSystemValues.put("1100", 500.);
			realSystemValues.put("1101", 500.);
			realSystemValues.put("1110", 500.);
			realSystemValues.put("1111", 500.);

			honeypotValues.put("0000", 100.);
			honeypotValues.put("0001", 100.);
			honeypotValues.put("0010", 100.);
			honeypotValues.put("0011", 100.);
			honeypotValues.put("0100", 100.);
			honeypotValues.put("0101", 100.);
			honeypotValues.put("0110", 100.);
			honeypotValues.put("0111", 100.);
			honeypotValues.put("1000", 100.);
			honeypotValues.put("1001", 100.);
			honeypotValues.put("1010", 100.);
			honeypotValues.put("1011", 100.);
			honeypotValues.put("1100", 100.);
			honeypotValues.put("1101", 100.);
			honeypotValues.put("1110", 100.);
			honeypotValues.put("1111", 100.);
		}
	}
	
	
	public static String reverseStr(String str) {
	    if ( str == null ) {
	          return null;
	    }
	    int len = str.length();
	    if (len <= 0) {
	        return "";
	    }
	    char[] strArr = new char[len];
	    int count = 0;
	    for (int i = len - 1; i >= 0; i--) {
	        strArr[count] = str.charAt(i);
	        count++;
	    }
	    return new String(strArr);
	}
	
	public void generateNatureActions(int numOfFeature) {
		for (int i = 0; i < featuresVector.size(); i++)

		{
			String networkString = featuresVector.get(i);
			double probability=1;
			ArrayList<String> tmpFeatureList = new ArrayList();
			for (int j = 0; j < numOfFeature; j+= Utils.REAL_HOST_FEATURES_NUM) {
				String featuresInEachHost = networkString.substring(j, j + Utils.REAL_HOST_FEATURES_NUM);
				tmpFeatureList.add(featuresInEachHost);
				if(j >= (Utils.REAL_HOST_FEATURES_NUM * Utils.TOTAL_NUM_OF_REAL_HOST))
					probability *= honeypotProbabilites.get(featuresInEachHost);
				else
					probability *= realSystemProbabilities.get(featuresInEachHost);
			}
			int n = tmpFeatureList.size(); 
			permute(tmpFeatureList, 0, n-1, networkString); 
			//System.out.println("Feature vec : " + networkString + " : " +probability);
			mChanceNodeProbablityList.add(probability);
			mBinarytoIntNumbers.put(networkString, Integer.parseInt(networkString, 2));
			mChnaceNodeActionList.add(networkString);
		}

	}
	public void generateNatureActions() {
		double sum = 0;
		for (int i = 0; i < 1; i++) {
			for (int j = 0; j < realHostConfigList.size(); j++) {
				for (int k = 0; k < realHostConfigList.size(); k++) {
					int realFlag = 0;
					double probability=0;
					if(i == 0) {
						realFlag = 1;
						probability = realSystemProbabilities.get(realHostConfigList.get(j)) * honeypotProbabilites.get(realHostConfigList.get(k));
						//System.out.println("Prob : " + probability);
						//sum += probability;
					}
					//probability = realSystemProbabilities.get(realHostConfigList.get(j)) * honeypotProbabilites.get(realHostConfigList.get(k));
					//String actionStr= realFlag +realHostConfigList.get(j)+ i + realHostConfigList.get(k);
					String actionStr= realHostConfigList.get(j)+ realHostConfigList.get(k);
					//System.out.println(actionStr);
					//setP2InformationSet(realHostConfigList.get(j), realHostConfigList.get(k));
					mChanceNodeProbablityList.add(probability);
					mBinarytoIntNumbers.put(actionStr, Integer.parseInt(actionStr, 2));
					mChnaceNodeActionList.add(actionStr);
					
				}
			}
		}
		//System.out.println("Sum : " + sum);
	}

	private void movePlayerOne() {
		for (int i = 0; i < mChnaceNodeActionList.size(); i++) {
			ArrayList<String> actions = new ArrayList<>();
			ArrayList<Integer> flipPositions = new ArrayList<>();
			actions.add(mChnaceNodeActionList.get(i));
			flipPositions.add(-1);
			int infoNo = Integer.parseInt(mChnaceNodeActionList.get(i),2);
			int startIndex =0;
			int numberOfFeatures =0;
			if(isModifyBothSystem) {
				numberOfFeatures = Utils.TOTAL_FEATUES_NUMBER_IN_GAME;
				Utils.numOfDefenderActions = Utils.TOTAL_FEATUES_NUMBER_IN_GAME + 1;
			}else if(isModifyRealSystem) {
				numberOfFeatures = Utils.TOTAL_FEATUES_NUMBER_IN_GAME;
				startIndex = Utils.REAL_HOST_FEATURES_NUM*Utils.TOTAL_NUM_OF_REAL_HOST;
			}else {
				numberOfFeatures =Utils.REAL_HOST_FEATURES_NUM;
			}
			
			for (int j = startIndex; j < numberOfFeatures; j++) {
				//if (j == Utils.REAL_HOST_FEATURES_NUM)
					//continue;
				int flipFeature = flipBits(infoNo, j);
				String strFormat = "%"+ (Utils.TOTAL_FEATUES_NUMBER_IN_GAME ) +"s";
				String flippedStr = String.format(strFormat, Integer.toBinaryString(flipFeature)).replace(' ', '0');
				actions.add(flippedStr);
				if( j < Utils.REAL_HOST_FEATURES_NUM*Utils.TOTAL_NUM_OF_REAL_HOST)
					flipPositions.add(0);
				else
					flipPositions.add(1);
			}

			createGambitFile.createPlayerNode(Utils.PLAYER_NODE_NAME, Utils.PLAYER_ONE, infoNo,
					mChnaceNodeActionList.get(i), actions, 0);
			for (int k = 0; k < actions.size(); k++) {
				movePlayerTwo(actions.get(k),flipPositions.get(k), infoNo, mChnaceNodeActionList.get(i));
			}
			if (null != actions) {
				actions.clear();
				actions = null;
			}

		}
	}

	private void movePlayerTwo(String playerOneAction,int flipPos, int palyerOneInfoSet, String natureAction) {

		ArrayList<String> actions = new ArrayList<>(); 
		for (int j = 0; j < playerOneAction.length(); j+= Utils.REAL_HOST_FEATURES_NUM) {
			String featuresInEachHost = playerOneAction.substring(j, j + Utils.REAL_HOST_FEATURES_NUM);
			actions.add(featuresInEachHost);
			
		}
		String p2InfoSet = getP2InofrmationSet(playerOneAction); // Making same infoset for both real and HP 
		int infosetNo = mBinarytoIntNumbers.get(p2InfoSet); // playerOne action was used previously. For creating uncertainity for player2 new information set is used.
		createGambitFile.createPlayerNode(Utils.PLAYER_NODE_NAME, Utils.PLAYER_TWO, infosetNo, playerOneAction, actions,
				0);
		setTerminalNode(actions,flipPos, playerOneAction, palyerOneInfoSet,natureAction);
	}	

	private double getUtility(int index) {
		if(index == -1)
			return 0;
		//System.out.println("index" + index);
		return modificationCost.get(index);
		
	}
	private boolean isActionsEqual(List actions) {
		for (int i = 0; i < actions.size() - 1; i++) {
			if (!actions.get(i).equals(actions.get(i + 1)))
				return false;
		}
		return true;
	}

	private void setTerminalNode(List actions,int flipPos, String playerOneAction, int playerOneInfoSet, String natureAction) {


		double cost = getUtility(flipPos);
		//boolean isEqual = isActionsEqual(actions);
		ArrayList<Double> payoffs = new ArrayList<>();
		for (int k = 0; k < actions.size(); k++) {

			if (payoffs.size() != 0)
				payoffs.clear();
			if(k < Utils.TOTAL_NUM_OF_REAL_HOST) {
				double payoff = realSystemValues.get(actions.get(k));
				payoffs.add(-(cost + payoff));
				payoffs.add(payoff);
			}else {
				double payoff = honeypotValues.get(actions.get(k));
				payoffs.add((payoff-cost));
				payoffs.add(-payoff);
			}
			
			createGambitFile.createTerminalNode(Utils.TERMINAL_NODE_NAME, ++mOutcomeCnt, "Outcome " + mOutcomeCnt,
					payoffs);
		}
 
	}
	
	public String getP2InofrmationSet(String action) {
		return p2InformationSet.get(action);
	}
	
	
public void setP2InformationSet(String networkConfig, String infoset , int val) {
		
		if(!p2InformationSet.containsKey(networkConfig)) {
			//System.out.println(networkConfig + " real conf :" + infoset);
			p2InformationSet.put(networkConfig, infoset);
		}
		
	}
	
	public void setP2InformationSet(String realHost, String hp) {
		
		String realNetworkConfig = realHost+ hp;
		String imperfectNetwrokConfig  = hp + realHost;
		String infoSet = realHost+hp;
		if(!p2InformationSet.contains(realNetworkConfig) && !p2InformationSet.contains(imperfectNetwrokConfig)) {
			System.out.println(realNetworkConfig + " : " + infoSet);
			p2InformationSet.put(realNetworkConfig, infoSet);
		}
		if(!realHost.equals(hp) && !p2InformationSet.contains(imperfectNetwrokConfig)) {
			p2InformationSet.put(imperfectNetwrokConfig,infoSet);
			System.out.println(imperfectNetwrokConfig + " : " + infoSet);
		}

	}

	public int flipBits(int n, int k) {
		int mask = 1 << k;
				
		return n ^ mask;
	}

	public void closeFile() {
		createGambitFile.closeFile();
	}
	private void permute(ArrayList<String> allFeatures, int l, int r, String realConfig) 
	{ 
		if (l == r) { 
			String str =  new String();
			for(int i =0 ; i <allFeatures.size(); i++)
				str += allFeatures.get(i);
				
			setP2InformationSet(str, realConfig, 0);
		}
		else
		{ 
			for (int i = l; i <= r; i++)  
			{ 
				Collections.swap(allFeatures,l,i); 
				permute(allFeatures, l+1, r, realConfig); 
				Collections.swap(allFeatures,l,i); 
			} 
		} 
	} 

	private double[] generateRandomProbability(int n) {
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

	public void generateBinaryRepresentation(int i, int n, ArrayList<String> featureVec) {
		if (i == (1 << n))
			return;
		else {
			String temp = Long.toBinaryString(i);
			while (temp.length() < n) {
				temp = '0' + temp;
			}
			featureVec.add(temp);
			generateBinaryRepresentation(i + 1, n,featureVec);
		}
	}

}
