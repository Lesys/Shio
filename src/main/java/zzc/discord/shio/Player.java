package zzc.discord.shio;

import java.util.*;

import net.dv8tion.jda.api.entities.User;

public class Player {
	protected User user;
	protected int points;
	protected String currentAnswer;
	protected List<String> allAnswers;
	
	public Player(User user) {
		this.user = user;
		this.points = 0;
		this.currentAnswer = "";
		this.allAnswers = new ArrayList<String>();
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
	
	public List<String> getAllAnswers() {
		return this.allAnswers;
	}
	
	public void resetAnswer() {
		this.allAnswers.add(this.currentAnswer);
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
