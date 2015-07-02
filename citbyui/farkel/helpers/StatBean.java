package citbyui.farkel.helpers;

public class StatBean {
	double farkelChance;
	int avgScore;
	double rerollChance;
	public StatBean(double farkelChance,int avgScore,double rerollChance) {
		setFarkelChance(farkelChance);
		setAvgScore(avgScore);
		setRerollChance(rerollChance);
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

}
