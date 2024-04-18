package zzc.discord.shio.commands;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import zzc.discord.shio.Bot;
import zzc.discord.shio.Game;

import org.jetbrains.annotations.NotNull;
import java.util.*;
public class CommandManager extends ListenerAdapter {	

	@Override
	public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
		String command = event.getName();
		
		if (command.equals("startgame")) {
			event.deferReply().queue();

			if (Bot.games.keySet().stream().noneMatch(guild -> guild == event.getGuild())) {
				//Bot.games.put(event.getGuild(), new Game(event.getGuild(), event.getUser()));
				Bot.games.put(event.getGuild(), new Game(event));
				
				String userMention = event.getUser().getAsMention();
				event.getHook().sendMessage(userMention + " created a game ! Use \"__*registergame*__\" to register in the game !").queue();
			}
			else {
				event.getHook().sendMessage("A game already exists for this server. Use \"__*/registergame*__\" to register in the game, or \"__*/stopgame*__\" (TO BE CREATED) to stop the current game.").queue();
			}
		} else if (command.equals("registergame")) {
			event.deferReply().queue();
			// CHeck if guild has started a game
			if (Bot.games.keySet().stream().anyMatch(guild -> guild == event.getGuild())) {
				User user = event.getUser();
				String userMention = event.getUser().getAsMention();
				// CHeck if user isn't already registered in a game
				if (!Bot.games.get(event.getGuild()).alreadyRegistered(user)) {
					// Add user with the guild he wrote the message
					Bot.games.get(event.getGuild()).addPlayer(user);
					//Bot.answers.put(user, "");

					event.getHook().sendMessage(userMention + " has been registered to the current game.").queue();
				} else { // Already registered
					event.getHook().sendMessage(userMention + " is already registered for the current game.").queue();
				}
			} else { // No game started
				event.getHook().sendMessage("There is no game currently started in this server. Please use the command \"__*/startgame*__\" to start a game.").queue();
			}
		} else if (command.equals("answers")) {
			event.deferReply().queue();
			// run command "answers"
			String answers = "Server " + event.getGuild().getName() + ": ";
	        final StringBuilder builder = new StringBuilder();
	        builder.append(answers);
	        
	        if (Bot.games.get(event.getGuild()) != null) {
	        	Game game = Bot.games.get(event.getGuild());
	        	if (game.getGameMaster() == event.getUser()) {
	        		builder.append(game.getPlayers().stream().map(player-> player.getUser().getGlobalName()).toList().toString());
	        		game.getPlayers().forEach(player -> builder.append("\n" + player.getUser().getAsMention() + ": " + player.getAnswer()  + " " + Emoji.fromUnicode(player.getAnswer().equalsIgnoreCase(game.getAnswer()) ? "U+2705" : "U+274C").getFormatted()));
	        		
		        	//builder.append(Bot.players.get(Bot.players.keySet().stream().filter(game -> game.getGuild() == event.getGuild()).findFirst().get()).stream().map(user -> user.getGlobalName()).toList().toString());
		        	//Bot.players.get(Bot.players.keySet().stream().filter(game -> game.getGuild() == event.getGuild()).findFirst().get()).forEach(player -> builder.append("\n" + player.getAsMention() + ": " + Bot.answers.get(player)  + " " + Emoji.fromUnicode("U+2705").getFormatted()));
			        
					event.getHook().sendMessage("Answers: " + builder.toString() + "\nThe correct answer was " + game.getAnswer() + " !").queue();
					
					game.nextAnswer();
	        	} else {
					event.getHook().sendMessage("Only the Game Master " + Bot.games.get(event.getGuild()).getGameMaster() + " can use this command.").queue();
		        }
	        } else {
				event.getHook().sendMessage("There is no game currently started in this server. Please use the command \\\"__*/startgame*__\\\" to start a game.").queue();
	        }
		}
		else {
			event.reply("Command not recognized").queue();
		}
	}
	
	public void onGuildReady(@NotNull ReadyEvent event) {
		List<CommandData> commandData = new ArrayList<>();
		commandData.add(Commands.slash("answers", "Get the answers from the players for the current question."));
		commandData.add(Commands.slash("startgame", "Starts a game in current server (if none existing). Use \"__*/registergame*__\" to register."));
		commandData.add(Commands.slash("registergame", "Registers user for game in server. Users can't register for multiple games, even on other server."));
		event.getJDA().updateCommands().addCommands(commandData).queue();
	}
	
	public void onReady(@NotNull ReadyEvent event) {
		List<CommandData> commandData = new ArrayList<>();
		commandData.add(Commands.slash("setanswers", "Set the answers for the game you are the GM. Please separate answers with $ without space.")); //TODO
		event.getJDA().updateCommands().addCommands(commandData).queue();		
	}
}