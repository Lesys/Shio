package zzc.discord.shio.commands;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import zzc.discord.shio.Bot;

public class CommandRegisterGame extends Command {
	@Override
	protected void init() {
		this.commandName = "registergame";
		this.commandDescription = "Registers user for game in server. Users can't register for multiple games, even on other server.";	
		this.guildOnly = true;
	}
	
	@Override
	public void exeuteCommand(@NotNull SlashCommandInteractionEvent event) {
		event.deferReply().queue();
		
		User user = event.getUser();
		String userMention = event.getUser().getAsMention();
		// Check if user isn't already registered in a game
		if (!Bot.games.keySet().stream().filter(guild -> event.getUser().getMutualGuilds().contains(guild)).anyMatch(guild -> Bot.games.get(guild).alreadyRegistered(user))) {			
			// Check if guild has started a game
			if (Bot.games.keySet().stream().anyMatch(guild -> guild == event.getGuild())) {
				// Add user with the guild he wrote the message
				Bot.games.get(event.getGuild()).addPlayer(user);
				//Bot.answers.put(user, "");

				event.getHook().sendMessage(userMention + " has been registered to the current game.").queue();
				
			} else { // No game started
				event.getHook().sendMessage("There is no game currently started in this server. Please use the command \"__*/startgame*__\" to start a game.").queue();
			}
		} else { // Already registered
			event.getHook().sendMessage(userMention + " is already registered for a game.").queue();
		}	
	}

}
