package zzc.discord.shio;

import net.dv8tion.jda.api.entities.User;

public class Player {
	private User user;
	private int points;
	private String currentAnswer;
	
	public Player(User user) {
		this.user = user;
		this.points = 0;
		this.currentAnswer = "";
	}
	
	public User getUser() {
		return this.user;
	}
	
	public void changeAnswer(String newAnswer) {
		this.currentAnswer = newAnswer;
	}
	
	public String getAnswer() {
		return this.currentAnswer;
	}
	
	public void resetAnswer() {
		this.currentAnswer = "";
	}
	
	public int getPoints() {
		return this.points;
	}
	
	public void addPoint() {
		this.points++;
	}
	
	public void addPoints(int points) {
		this.points += points;
	}
}
