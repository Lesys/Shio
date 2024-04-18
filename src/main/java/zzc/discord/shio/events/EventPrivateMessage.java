package zzc.discord.shio.events;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import zzc.discord.shio.Bot;
import zzc.discord.shio.Game;

public class EventPrivateMessage extends ListenerAdapter {
	@SuppressWarnings("unlikely-arg-type")
	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		if (!event.isFromType(ChannelType.PRIVATE) || event.getAuthor() == event.getJDA().getSelfUser()) {
			return;
		} else {
			User user = event.getAuthor();
			// Check if author is in a guild with game started
//			if (Bot.players != null && Bot.players.keySet().contains(
//					event.getAuthor().getMutualGuilds().stream().filter(guild -> {
//						return Bot.players.keySet().stream().anyMatch(game -> game.getGuild() == guild) && 
//								Bot.players.get(Bot.players.keySet().stream().filter(game -> game.getGuild() == guild).findFirst().get()).contains(event.getAuthor().getName()); }))) {
			Game game = null;
			String answer = event.getMessage().getContentRaw();
			try {
				game = Bot.games.get(Bot.games.keySet().stream().filter(guild -> event.getJDA().getMutualGuilds(user).contains(guild)).filter(guild -> Bot.games.get(guild).alreadyRegistered(user)).findFirst().get());
			} catch (NoSuchElementException e) {
				game = null;
			}
			if (game != null) {
				if (game.getPlayer(user) != null && !sameAnswer(game.getPlayer(user).getAnswer(), answer)) {
					// Register answer
					game.changeAnswer(user, answer);
					event.getMessage().addReaction(Emoji.fromUnicode("U+2705")).queue();
				}
				else {
					event.getMessage().addReaction(Emoji.fromUnicode("U+274C")).queue();
				}
			}
			
//			if (Bot.games.keySet().stream().anyMatch(game -> { return Bot.players.get(game).contains(user);})) {
//				// Check if answer isn't already same as the one registered
//				if (Bot.answers.get(user) == null || (Bot.answers.get(user) != null && !sameAnswer(Bot.answers.get(user), answer))) {
//					// Register answer
//					Bot.answers.put(user, answer);
//					event.getMessage().addReaction(Emoji.fromUnicode("U+2705")).queue();
//				}
//				else {
//					event.getMessage().addReaction(Emoji.fromUnicode("U+274C")).queue();
//				}
//			}
//			else {
//				event.getChannel().sendMessage("I am sorry, but you do not belond to any current game. Please type \"__*/registerGame*__\" in the server where the game is played to participate.").queue();
//			}
		}
	}
	
	public boolean sameAnswer(String previous, String answer) {
		return previous.equalsIgnoreCase(answer);
	}
}
