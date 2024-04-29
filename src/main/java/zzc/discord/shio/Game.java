package zzc.discord.shio;

import java.util.*;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Game {
	private JDA jda;
	private Guild guild;
	private User gamemaster;
	private List<List<String>> answers;
	private int answerPosition;
	private List<Player> players;
	private MessageChannelUnion channel;
	
	public Game(SlashCommandInteractionEvent event) {
		this(event.getGuild(), event.getUser());
		this.jda = event.getJDA();
		this.channel = event.getChannel();
	}
	
	private Game(Guild guild, User gamemaster) {
		this.guild = guild;
		this.gamemaster = gamemaster;
		this.players = new ArrayList<Player>();
		this.answers = new ArrayList<List<String>>();
		this.answerPosition = 0;
	}
	
	public Guild getGuild() {
		return this.guild;
	}
	
	public User getGameMaster() {
		return this.gamemaster;
	}
	
	public List<Player> getPlayers() {
		return this.players;
	}
	
	public void addPlayer(User user) {
		if (!this.alreadyRegistered(user)) {
			Player player = new Player(user);
			this.players.add(player);
			
			for (int i = 0; i < this.answerPosition; i++)
				player.resetAnswer();
		}
	}
	
	public Player getPlayer(User user) {
		Player p = null;
		
		try {
			p = this.players.stream().filter(player -> player.getUser() == user).findFirst().get();
		} catch (NoSuchElementException e) {
			p = null;
		}
		return p;
	}
	
	public List<String> getAnswer() throws IndexOutOfBoundsException {
		return this.answers.get(this.answerPosition);
	}
	
	public List<String> getAnswer(int answerPosition) {
		try {
			return answerPosition < this.answerPosition || this.answerPosition + 1 >= this.answers.size() ? this.answers.get(answerPosition) : Arrays.asList("");
		} catch (IndexOutOfBoundsException e) {
			return Arrays.asList("");
		}
	}
	
	public void setAnswers(List<List<String>> answers) {
		this.answers.addAll(answers);
	}
	
	public boolean alreadyRegistered(User user) {
		return this.players.stream().anyMatch(player -> player.getUser() == user);
	}
	
	public void changeAnswer(User user, String newAnswer) {
		this.players.stream().filter(player -> player.getUser() == user).findFirst().get().changeAnswer(newAnswer);
	}
	
	public void nextAnswer() {
		this.players.forEach(player -> {if (this.getAnswer().stream().anyMatch(a -> Game.correctAnswer(player.getAnswer(), a))) player.addPoint(); player.resetAnswer(); });
		if (this.answerPosition + 1 >= this.answers.size()) {
	        final StringBuilder builder = new StringBuilder();
    		builder.append(this.getPlayers().stream().map(player-> player.getUser().getAsMention() + " - " + player.getPoints()).toList().toString());

			this.channel.sendMessage("The answer n°" + (this.answerPosition + 1) + " was the last one. Game is over !\nHere are the results: \n" + builder.toString()).queue();
			//Bot.games.remove(this.guild);
		} else {
			this.answerPosition++;
		}
	}
	
	public String getPoints() {
		return this.getPlayers().stream().map(player-> player.getUser().getAsMention() + " - " + player.getPoints()).toList().toString();
	}
	
	public static boolean correctAnswer(String playerAnswer, String awaitedAnswer) {
		return playerAnswer.equalsIgnoreCase(awaitedAnswer);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass().isAssignableFrom(Guild.class)) {
			Guild g = (Guild)obj;
			
			return this.guild.equals(g);
		} else if (obj.getClass().isAssignableFrom(Game.class)) {
			Game g = (Game)obj;
			
			return this.guild.equals(g.getGuild());
		}
		
		return false;
	}
}
