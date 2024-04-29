package zzc.discord.shio.commands;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import zzc.discord.shio.Bot;
import zzc.discord.shio.Game;

public class CommandStartGame extends Command {	
	@Override
	protected void init() {
		this.commandName = "startgame";
		this.commandDescription = "Starts a game in current server (if none existing). Use \"__*/registergame*__\" to register.";
		this.guildOnly = true;
	}
	
	@Override
	public void exeuteCommand(@NotNull SlashCommandInteractionEvent event) {
		event.deferReply().queue();

		if (Bot.games.keySet().stream().noneMatch(guild -> guild == event.getGuild())) {
			//Bot.games.put(event.getGuild(), new Game(event.getGuild(), event.getUser()));
			Bot.games.put(event.getGuild(), new Game(event));
			
			String userMention = event.getUser().getAsMention();
			event.getHook().sendMessage(userMention + " created a game ! Use \"__*registergame*__\" to register in the game ! Server Name: " + event.getGuild().getName()).queue();
		}
		else {
			event.getHook().sendMessage("A game already exists for this server. Use \"__*/registergame*__\" to register in the game, or \"__*/stopgame*__\" to stop the current game.").queue();
		}
	}

}
