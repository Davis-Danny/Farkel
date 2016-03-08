package citbyui.farkel.stats;

import java.util.ArrayList;

public class StatBean {
	double farkelChance;
	int avgScore;
	double rerollChance;
	double rollWorth;
	ArrayList<Double> leftChance;

	public double getRollWorth() {
		return rollWorth;
	}

	public void setRollWorth(double rollWorth) {
		this.rollWorth = rollWorth;
	}
	
	public StatBean(double farkelChance,int avgScore,double rerollChance,double rollWorth) {
		setFarkelChance(farkelChance);
		setAvgScore(avgScore);
		setRerollChance(rerollChance);
		setRollWorth(rollWorth);
	}

	public StatBean(double farkelChance,int avgScore,double rerollChance,ArrayList<Double>  leftChance) {
		setFarkelChance(farkelChance);
		setAvgScore(avgScore);
		setRerollChance(rerollChance);
		setLeftChance(leftChance);
	}
	
	public double getFarkelChance() {
		return farkelChance;
	}
	public void setFarkelChance(double farkelChance) {
		this.farkelChance = farkelChance;
	}
	public int getAvgScore() {
		return avgScore;
	}
	public void setAvgScore(int avgScore) {
		this.avgScore = avgScore;
	}
	public double getRerollChance() {
		return rerollChance;
	}
	public void setRerollChance(double rerollChance) {
		this.rerollChance = rerollChance;
	}
	public ArrayList<Double> getLeftChance() {
		return leftChance;
	}
	public void setLeftChance(ArrayList<Double> leftCount) {
		this.leftChance = leftCount;
	}

}
