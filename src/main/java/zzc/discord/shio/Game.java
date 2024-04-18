package zzc.discord.shio;

import java.util.*;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Game {
	private JDA jda;
	private Guild guild;
	private User gamemaster;
	private List<String> answers;
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
		this.answers = new ArrayList<String>();
		this.answerPosition = 0;
		this.answers.add("Testos");
		this.answers.add("Testas");
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
		if (!this.alreadyRegistered(user))
			this.players.add(new Player(user));
	}
	
	public boolean alreadyRegistered(User user) {
		return this.players.stream().anyMatch(player -> player.getUser() == user);
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
	
	public void changeAnswer(User user, String newAnswer) {
		this.players.stream().filter(player -> player.getUser() == user).findFirst().get().changeAnswer(newAnswer);
	}
	
	public void nextAnswer() {
		if (this.answerPosition + 1 >= this.answers.size()) {
	        final StringBuilder builder = new StringBuilder();
    		builder.append(this.getPlayers().stream().map(player-> player.getUser().getAsMention() + " - " + player.getPoints()).toList().toString());

			this.channel.sendMessage("The answer n°" + (this.answerPosition + 1) + " was the last one. Game is over !\nHere are the results: \n" + builder.toString()).queue();
		} else {
			this.players.forEach(player -> {if (player.getAnswer().equalsIgnoreCase(this.getAnswer())) player.addPoint(); player.resetAnswer(); });
			this.answerPosition++;
		}
	}
	
	public String getAnswer() {
		return this.answers.get(this.answerPosition);
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
