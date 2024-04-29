package zzc.discord.shio.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import org.jetbrains.annotations.NotNull;
import java.util.*;
public class CommandManager extends ListenerAdapter {	
	public static boolean waitAnswer = false;
	public List<Command> commands = new ArrayList<Command>();
	
	@Override
	public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
		String command = event.getName();
		
		Command slashCommand = this.commands.stream().filter(comm -> comm.matchingName(command)).findFirst().orElseGet(null);
		
		if (slashCommand != null) {
			slashCommand.exeuteCommand(event);
		}
//		
//		if (command.equals("startgame")) {
//			event.deferReply().queue();
//
//			if (Bot.games.keySet().stream().noneMatch(guild -> guild == event.getGuild())) {
//				//Bot.games.put(event.getGuild(), new Game(event.getGuild(), event.getUser()));
//				Bot.games.put(event.getGuild(), new Game(event));
//				
//				String userMention = event.getUser().getAsMention();
//				event.getHook().sendMessage(userMention + " created a game ! Use \"__*registergame*__\" to register in the game ! Server Name: " + event.getGuild().getName()).queue();
//			}
//			else {
//				event.getHook().sendMessage("A game already exists for this server. Use \"__*/registergame*__\" to register in the game, or \"__*/stopgame*__\" to stop the current game.").queue();
//			}
//		} else if (command.equals("stopgame")) {
//			event.deferReply().queue();
//
//			if (Bot.games.keySet().stream().anyMatch(guild -> guild == event.getGuild()) && Bot.games.get(event.getGuild()).getGameMaster() == event.getUser()) {
//				//Bot.games.put(event.getGuild(), new Game(event.getGuild(), event.getUser()));
//				Bot.games.remove(event.getGuild());
//				
//				String userMention = event.getUser().getAsMention();
//				event.getHook().sendMessage(userMention + " stopped the game. Thanks for playing !").queue();
//			}
//			else {
//				event.getHook().sendMessage("There is no game currently started in this server. Please use the command \"__*/startgame*__\" to start a game.").queue();
//			}
//		} else if (command.equals("registergame")) {
//			event.deferReply().queue();
//			User user = event.getUser();
//			String userMention = event.getUser().getAsMention();
//			// Check if user isn't already registered in a game
//			if (!Bot.games.keySet().stream().filter(guild -> event.getUser().getMutualGuilds().contains(guild)).anyMatch(guild -> Bot.games.get(guild).alreadyRegistered(user))) {			
//				// Check if guild has started a game
//				if (Bot.games.keySet().stream().anyMatch(guild -> guild == event.getGuild())) {
//					// Add user with the guild he wrote the message
//					Bot.games.get(event.getGuild()).addPlayer(user);
//					//Bot.answers.put(user, "");
//
//					event.getHook().sendMessage(userMention + " has been registered to the current game.").queue();
//					
//				} else { // No game started
//					event.getHook().sendMessage("There is no game currently started in this server. Please use the command \"__*/startgame*__\" to start a game.").queue();
//				}
//			} else { // Already registered
//				event.getHook().sendMessage(userMention + " is already registered for a game.").queue();
//			}			
//		} else if (command.equals("getpoints")) {
//			event.deferReply().queue();
//
//			if (Bot.games.keySet().stream().anyMatch(guild -> guild == event.getGuild())) {
//				event.getHook().sendMessage("Here is the scoreboard: \n" + Bot.games.get(event.getGuild()).getPoints()).queue();
//			}
//			else {
//				event.getHook().sendMessage("There is no game currently started in this server. Please use the command \"__*/startgame*__\" to start a game.").queue();
//			}
//		} else if (command.equals("allmyanswers")) {
//			event.deferReply().queue();
//
//			if (!event.getChannelType().equals(ChannelType.PRIVATE)) {
//				event.getHook().sendMessage("Please use this command in Private Channel.").queue();
//			} else {
//				Guild guild = Bot.games.keySet().stream().filter(g -> Bot.games.get(g).alreadyRegistered(event.getUser())).findFirst().orElseGet(null);
//				if (guild != null) {
//			        final StringBuilder builder = new StringBuilder();
//			        builder.append("Server " + guild.getName() + ":\n");
//
//					AtomicInteger i = new AtomicInteger(1);
//					Game game = Bot.games.get(guild);
//					game.getPlayer(event.getUser()).getAllAnswers().stream().map(answer -> i.get() + " - " + answer + " " + Emoji.fromUnicode(game.getAnswer(i.getAndIncrement() - 1).stream().anyMatch(a -> answer != "" ? Game.correctAnswer(answer, a) : false) ? "U+2705" : "U+274C").getFormatted()).forEach(answer -> builder.append(answer + "\n"));
//					event.getHook().sendMessage("Here are your answers " + event.getUser().getAsMention() + ": \n" + builder.toString()).queue();
//				} else {
//					event.getHook().sendMessage("There is no game currently started in this server. Please use the command \"__*/startgame*__\" to start a game.").queue();
//				}
//			}
//		} else if (command.equals("answers")) {
//			event.deferReply().queue();
//			// run command "answers"
//			String answers = "Server " + event.getGuild().getName() + ": ";
//	        final StringBuilder builder = new StringBuilder();
//	        builder.append(answers);
//	        
//	        if (Bot.games.get(event.getGuild()) != null) {
//	        	Game game = Bot.games.get(event.getGuild());
//	        	if (game.getGameMaster() == event.getUser()) {
//	        		try {
//		        		if (game.getAnswer().size() >= 1) {
//			        		builder.append(game.getPlayers().stream().map(player-> player.getUser().getGlobalName()).toList().toString());
//		        			game.getPlayers().forEach(player -> builder.append("\n" + player.getUser().getAsMention() + ": " + player.getAnswer()  + " " + Emoji.fromUnicode(game.getAnswer().stream().anyMatch(a -> Game.correctAnswer(player.getAnswer(), a)) ? "U+2705" : "U+274C").getFormatted()));
//	
//							event.getHook().sendMessage("Answers: " + builder.toString() + "\nThe correct answer was " + game.getAnswer() + " !").queue();
//							
//							game.nextAnswer(); // Always last
//			        		
//				        	//builder.append(Bot.players.get(Bot.players.keySet().stream().filter(game -> game.getGuild() == event.getGuild()).findFirst().get()).stream().map(user -> user.getGlobalName()).toList().toString());
//				        	//Bot.players.get(Bot.players.keySet().stream().filter(game -> game.getGuild() == event.getGuild()).findFirst().get()).forEach(player -> builder.append("\n" + player.getAsMention() + ": " + Bot.answers.get(player)  + " " + Emoji.fromUnicode("U+2705").getFormatted()));
//		        		} else {
//							event.getHook().sendMessage("There is currently no answers registered. Please make sure you have done the \"__*/setanswers*__\" command and added the answers in the correct format.").queue();
//				        }
//	        		} catch (IndexOutOfBoundsException e) {
//	        			event.getHook().sendMessage("The answer you asked is out of bound.").queue();
//	        		}
//	        	} else {
//					event.getHook().sendMessage("Only the Game Master " + Bot.games.get(event.getGuild()).getGameMaster() + " can use this command.").queue();
//		        }
//	        } else {
//				event.getHook().sendMessage("There is no game currently started in this server. Please use the command \"__*/startgame*__\" to start a game.").queue();
//	        }
//		} else if (command.equals("setanswers")) {
//			event.deferReply().queue();
//
//			Guild guild = null;
//			event.getJDA().getMutualGuilds(event.getUser()).forEach(g -> System.err.println("Guild: " + g));
//			if (event.getGuild() == null) {
//				if (event.getOption("guild") != null && Bot.games.keySet().stream().filter(g -> event.getJDA().getMutualGuilds(event.getUser()).contains(g)).anyMatch(g -> g.getName().equals(event.getOption("guild").getAsString()))) {
//					guild = Bot.games.keySet().stream().filter(g -> event.getJDA().getMutualGuilds(event.getUser()).contains(g)).filter(g -> g.getName().equals(event.getOption("guild").getAsString())).findFirst().get();
//				} else {
//					guild = null;
//				}
//			} else {
//				guild = event.getGuild();
//			}
//			if (guild != null) {
//		        if (Bot.games.get(guild) != null) {
//		        	Game game = Bot.games.get(guild);
//		        	if (game.getGameMaster() == event.getUser()) {
//		        		if (event.getOption("format") != null) {
//		        			if (event.getOption("format").getAsString().equalsIgnoreCase("text")) {
//			        			event.getHook().sendMessage("You choose the \"text\" format. Please send your answers in private message. The correct format is 1 answer per row, each row can contain multiple choices split by the $ mark. For example:\nRe: Zero Kara Hajimeru Isekai Seikatsu$re:zero\nSword Art Online$SAO").queue();
//			        			
//			        			event.getJDA().addEventListener(new EventSetAnswers().setGuild(guild));
//			        			
//			        			CommandManager.waitAnswer = true;
//			        		} else if (event.getOption("format").getAsString().equalsIgnoreCase("file")) {
//			        			event.getHook().sendMessage("You choose the \"file\" format. Please send your file in private message. The correct format inside the file is 1 answer per row, each row can contain multiple choices split by the $ mark. For example:\nRe: Zero Kara Hajimeru Isekai Seikatsu$re:zero\nSword Art Online$SAO").queue();
//
//			        			event.getJDA().addEventListener(new EventSetAnswersFile().setGuild(guild));
//			        			
//			        			CommandManager.waitAnswer = true;
//			        		} else {
//			        			event.getHook().sendMessage("Option \"format\" has not been correctly filled. Please use one of the auto-completed choice (current: " + event.getOption("format").getAsString() + ").").queue();
//			        		}
//	        			}  else {
//		        			event.getHook().sendMessage("Option \"format\" need 1 parameter (current: " + event.getOptions().size() + ").").queue();
//		        		}
//	        		} else {
//						event.getHook().sendMessage("Only the Game Master " + Bot.games.get(guild).getGameMaster() + " can use this command.").queue();
//			        }
//	        	} else {
//					event.getHook().sendMessage("There is no game currently started in this server. Please use the command \"__*/startgame*__\" to start a game.").queue();
//		        }
//	        } else {
//    			event.getHook().sendMessage("Option \"guild\" isn't matching with a guild name registered. Please be sure to copy/paste the name displayed when you use the \"__*/startgame*__\" command.").queue();
//			}
//		}
		else {
			event.reply("Command not recognized :/").queue();
		}
	}
	/*
	public void onGuildReady(@NotNull ReadyEvent event) {
		List<CommandData> commandData = new ArrayList<>();
		event.getJDA().updateCommands().addCommands(commandData).queue();
	}*/
	
	@Override
	public void onReady(@NotNull ReadyEvent event) {
//		List<CommandData> commandData = new ArrayList<>();

		this.commands.add(new CommandAllMyAnswers());
		this.commands.add(new CommandAnswers());
		this.commands.add(new CommandGetPoints());
		this.commands.add(new CommandRegisterGame());
		this.commands.add(new CommandSetAnswers());
		this.commands.add(new CommandStartGame());
		this.commands.add(new CommandStopGame());
		
		event.getJDA().updateCommands().addCommands(this.commands.stream().map(comm -> comm.getCommandData()).toList()).queue();
		
//		commandData.add(Commands.slash("answers", "Get the answers from the players for the current question.").setGuildOnly(true));
//		commandData.add(Commands.slash("startgame", "Starts a game in current server (if none existing). Use \"__*/registergame*__\" to register.").setGuildOnly(true));
//		commandData.add(Commands.slash("stopgame", "Stops the current game started on this server.").setGuildOnly(true));
//		commandData.add(Commands.slash("registergame", "Registers user for game in server. Users can't register for multiple games, even on other server.").setGuildOnly(true));
//		commandData.add(Commands.slash("getpoints", "Displays all the registered players and their current points.").setGuildOnly(true));
//		commandData.add(Commands.slash("allmyanswers", "Displays all the answers of the player that did the command. (Only works on Private Message)"));
//		commandData.add(Commands.slash("setanswers", "Set the answers for the game you are the GM. Can also be done in private with the \"guild\" option.").addOptions(
//				new OptionData(OptionType.STRING, "format", "The format of the answers you want to send").addChoice("text", "text").addChoice("file", "file"),
//				new OptionData(OptionType.STRING, "guild", "The name of the guild were you want to add the answers")));
//		event.getJDA().updateCommands().addCommands(commandData).queue();
	}
}