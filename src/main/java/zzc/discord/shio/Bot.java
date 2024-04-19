package zzc.discord.shio;

import java.util.*;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.*;
import net.dv8tion.jda.api.utils.cache.*;
import zzc.discord.shio.events.EventKick;
import zzc.discord.shio.events.EventPrivateMessage;
import zzc.discord.shio.commands.CommandManager;
import zzc.discord.shio.events.EventJoin;

public class Bot {
	public static GatewayIntent[] INTENTS = {GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES};
	//public static Map<User, String> answers = new HashMap<User, String>(); //<UserID, Answer>
	public static Map<Guild, Game> games = new HashMap<Guild, Game>(); //<Guild, Game>


	public static void main(String[] args) throws LoginException {
		//CommandManager commandManager = new CommandManager("$");
		//commandManager.register(new CommandAnswers());
		
		//old token: MTIyODUyMzM5MjEwMjMwNTk0Mw.G6MGZb.S94j6ZabrfBZ1Krma3Ru0MLKsIMwaYjkW-QkZo
		String jdaToken = "MTIyODUyMzM5MjEwMjMwNTk0Mw.G6MGZb.S94j6ZabrfBZ1Krma3Ru0MLKsIMwaYjkW-QkZo";
		JDA jda = JDABuilder.create(jdaToken, Arrays.asList(INTENTS))
				.enableCache(CacheFlag.VOICE_STATE)
				.setStatus(OnlineStatus.ONLINE)
				.setActivity(Activity.customStatus("Use \"/startgame\" to start a game \\o/"))
				.addEventListeners(new EventJoin())
				.addEventListeners(new EventKick())
				.addEventListeners(new EventPrivateMessage())
				.addEventListeners(new CommandManager())
				/*.addEventListeners(new CommandAnswers())
				.addEventListeners(new CommandStartGame())
				.addEventListeners(new CommandRegisterGame())*/
				/*.addEventListeners(new ListenerAdapter() {

		            @Override
		               public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		                // Don't execute commands from bots
		                if (event.getAuthor().isBot()) {
		                    return;
		                }

		                commandManager.executeCommand(event.getMessage());
		            }
		        })*/
				.build();
	}
}
