package zzc.discord.shio.commands;

import java.util.Arrays;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import zzc.discord.shio.Bot;
import zzc.discord.shio.Game;
import zzc.discord.shio.events.EventSetAnswers;
import zzc.discord.shio.events.EventSetAnswersFile;

public class CommandSetAnswers extends Command {
	@Override
	protected void init() {
		this.commandName = "setanswers";
		this.commandDescription = "Set the answers for the game you are the GM. Can also be done in private with the \"guild\" option.";
		this.commandOption = Arrays.asList(new OptionData(OptionType.STRING, "format", "The format of the answers you want to send").addChoice("text", "text").addChoice("file", "file"),
				new OptionData(OptionType.STRING, "guild", "The name of the guild were you want to add the answers"));
	}
	
	@Override
	public void exeuteCommand(@NotNull SlashCommandInteractionEvent event) {
		event.deferReply().queue();

		Guild guild = null;
		event.getJDA().getMutualGuilds(event.getUser()).forEach(g -> System.err.println("Guild: " + g));
		if (event.getGuild() == null) {
			if (event.getOption("guild") != null && Bot.games.keySet().stream().filter(g -> event.getJDA().getMutualGuilds(event.getUser()).contains(g)).anyMatch(g -> g.getName().equals(event.getOption("guild").getAsString()))) {
				guild = Bot.games.keySet().stream().filter(g -> event.getJDA().getMutualGuilds(event.getUser()).contains(g)).filter(g -> g.getName().equals(event.getOption("guild").getAsString())).findFirst().get();
			} else {
				guild = null;
			}
		} else {
			guild = event.getGuild();
		}
		if (guild != null) {
	        if (Bot.games.get(guild) != null) {
	        	Game game = Bot.games.get(guild);
	        	if (game.getGameMaster() == event.getUser()) {
	        		if (event.getOption("format") != null) {
	        			if (event.getOption("format").getAsString().equalsIgnoreCase("text")) {
		        			event.getHook().sendMessage("You choose the \"text\" format. Please send your answers in private message. The correct format is 1 answer per row, each row can contain multiple choices split by the $ mark. For example:\nRe: Zero Kara Hajimeru Isekai Seikatsu$re:zero\nSword Art Online$SAO").queue();
		        			
		        			event.getJDA().addEventListener(new EventSetAnswers().setGuild(guild));
		        			
		        			CommandManager.waitAnswer = true;
		        		} else if (event.getOption("format").getAsString().equalsIgnoreCase("file")) {
		        			event.getHook().sendMessage("You choose the \"file\" format. Please send your file in private message. The correct format inside the file is 1 answer per row, each row can contain multiple choices split by the $ mark. For example:\nRe: Zero Kara Hajimeru Isekai Seikatsu$re:zero\nSword Art Online$SAO").queue();

		        			event.getJDA().addEventListener(new EventSetAnswersFile().setGuild(guild));
		        			
		        			CommandManager.waitAnswer = true;
		        		} else {
		        			event.getHook().sendMessage("Option \"format\" has not been correctly filled. Please use one of the auto-completed choice (current: " + event.getOption("format").getAsString() + ").").queue();
		        		}
        			}  else {
	        			event.getHook().sendMessage("Option \"format\" need 1 parameter (current: " + event.getOptions().size() + ").").queue();
	        		}
        		} else {
					event.getHook().sendMessage("Only the Game Master " + Bot.games.get(guild).getGameMaster() + " can use this command.").queue();
		        }
        	} else {
				event.getHook().sendMessage("There is no game currently started in this server. Please use the command \"__*/startgame*__\" to start a game.").queue();
	        }
        } else {
			event.getHook().sendMessage("Option \"guild\" isn't matching with a guild name registered. Please be sure to copy/paste the name displayed when you use the \"__*/startgame*__\" command.").queue();
		}
	}

}
