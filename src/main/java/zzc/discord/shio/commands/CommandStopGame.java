package zzc.discord.shio.commands;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import zzc.discord.shio.Bot;

public class CommandStopGame extends Command {	
	@Override
	protected void init() {
		this.commandName = "stopgame";
		this.commandDescription = "Stops the current game started on this server.";	
		this.guildOnly = true;
	}
	
	@Override
	public void exeuteCommand(@NotNull SlashCommandInteractionEvent event) {
		event.deferReply().queue();

		if (Bot.games.keySet().stream().anyMatch(guild -> guild == event.getGuild()) && Bot.games.get(event.getGuild()).getGameMaster() == event.getUser()) {
			//Bot.games.put(event.getGuild(), new Game(event.getGuild(), event.getUser()));
			Bot.games.remove(event.getGuild());
			
			String userMention = event.getUser().getAsMention();
			event.getHook().sendMessage(userMention + " stopped the game. Thanks for playing !").queue();
		}
		else {
			event.getHook().sendMessage("There is no game currently started in this server. Please use the command \"__*/startgame*__\" to start a game.").queue();
		}
	}

}
