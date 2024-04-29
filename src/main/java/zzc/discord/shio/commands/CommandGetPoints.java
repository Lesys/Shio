package zzc.discord.shio.commands;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import zzc.discord.shio.Bot;

public class CommandGetPoints extends Command {
	@Override
	protected void init() {
		this.commandName = "getpoints";
		this.commandDescription = "Displays all the registered players and their current points.";	
		this.guildOnly = true;
	}
	
	@Override
	public void exeuteCommand(@NotNull SlashCommandInteractionEvent event) {
		event.deferReply().queue();

		if (Bot.games.keySet().stream().anyMatch(guild -> guild == event.getGuild())) {
			event.getHook().sendMessage("Here is the scoreboard: \n" + Bot.games.get(event.getGuild()).getPoints()).queue();
		}
		else {
			event.getHook().sendMessage("There is no game currently started in this server. Please use the command \"__*/startgame*__\" to start a game.").queue();
		}
	}

}
