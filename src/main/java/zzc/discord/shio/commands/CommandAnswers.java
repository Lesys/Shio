package zzc.discord.shio.commands;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import zzc.discord.shio.Bot;
import zzc.discord.shio.Game;

public class CommandAnswers extends Command {
	@Override
	protected void init() {
		this.commandName = "answers";
		this.commandDescription = "Get the answers of the players for the current question shows the correct answer with it.";		
		this.guildOnly = true;
	}
	
	@Override
	public void exeuteCommand(@NotNull SlashCommandInteractionEvent event) {
		event.deferReply().queue();
		// run command "answers"
		String answers = "Server " + event.getGuild().getName() + ": ";
        final StringBuilder builder = new StringBuilder();
        builder.append(answers);
        
        if (Bot.games.get(event.getGuild()) != null) {
        	Game game = Bot.games.get(event.getGuild());
        	if (game.getGameMaster() == event.getUser()) {
        		try {
	        		if (game.getAnswer().size() >= 1) {
		        		builder.append(game.getPlayers().stream().map(player-> player.getUser().getGlobalName()).toList().toString());
	        			game.getPlayers().forEach(player -> builder.append("\n" + player.getUser().getAsMention() + ": " + player.getAnswer()  + " " + Emoji.fromUnicode(game.getAnswer().stream().anyMatch(a -> Game.correctAnswer(player.getAnswer(), a)) ? "U+2705" : "U+274C").getFormatted()));

						event.getHook().sendMessage("Answers: " + builder.toString() + "\nThe correct answer was " + game.getAnswer() + " !").queue();
						
						game.nextAnswer(); // Always last
		        		
			        	//builder.append(Bot.players.get(Bot.players.keySet().stream().filter(game -> game.getGuild() == event.getGuild()).findFirst().get()).stream().map(user -> user.getGlobalName()).toList().toString());
			        	//Bot.players.get(Bot.players.keySet().stream().filter(game -> game.getGuild() == event.getGuild()).findFirst().get()).forEach(player -> builder.append("\n" + player.getAsMention() + ": " + Bot.answers.get(player)  + " " + Emoji.fromUnicode("U+2705").getFormatted()));
	        		} else {
						event.getHook().sendMessage("There is currently no answers registered. Please make sure you have done the \"__*/setanswers*__\" command and added the answers in the correct format.").queue();
			        }
        		} catch (IndexOutOfBoundsException e) {
        			event.getHook().sendMessage("The answer you asked is out of bound.").queue();
        		}
        	} else {
				event.getHook().sendMessage("Only the Game Master " + Bot.games.get(event.getGuild()).getGameMaster() + " can use this command.").queue();
	        }
        } else {
			event.getHook().sendMessage("There is no game currently started in this server. Please use the command \"__*/startgame*__\" to start a game.").queue();
        }
	}

}
