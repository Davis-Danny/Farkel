package citbyui.farkel.stats;

import java.util.ArrayList;

import citbyui.farkel.players.Player;

public class PlayerStats {

	private Player player;
	private int farkels;
	private ArrayList<Integer> scores;
	private int totalRounds;
	public PlayerStats(Player player) {
		setPlayer(player);
		setFarkels(0);
		scores = new ArrayList<Integer>();
		totalRounds = 0;
	}
	
	public void addTurn(int points){
		if(scores.size()<=(points/50)){
			fillArray(points/50,scores);
		}
		scores.set(points/50, scores.get(points/50)+1);
		totalRounds++;
	}
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}

	public int getFarkels() {
		return farkels;
	}

	public void setFarkels(int farkels) {
		this.farkels = farkels;
	}
	
	public ArrayList<Integer> fillArray(int num, ArrayList<Integer> list){
		for(int i= list.size();i<=num;i++){
			list.add(i,0);
		}
		return list;
	}
	public ArrayList<Integer> getScores(){
		return scores;
	}
	public int getTotalRounds(){
		return totalRounds;
	}
}
