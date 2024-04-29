package zzc.discord.shio.commands;

import java.util.concurrent.atomic.AtomicInteger;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import zzc.discord.shio.Bot;
import zzc.discord.shio.Game;

public class CommandAllMyAnswers extends Command {
	@Override
	protected void init() {
		this.commandName = "allmyanswers";
		this.commandDescription = "Displays all the answers of the player that did the command. (Only works on Private Message)";		
	}
	
	@Override
	public void exeuteCommand(@NotNull SlashCommandInteractionEvent event) {
		event.deferReply().queue();

		if (!event.getChannelType().equals(ChannelType.PRIVATE)) {
			event.getHook().sendMessage("Please use this command in Private Channel.").queue();
		} else {
			Guild guild = Bot.games.keySet().stream().filter(g -> Bot.games.get(g).alreadyRegistered(event.getUser())).findFirst().orElseGet(null);
			if (guild != null) {
		        final StringBuilder builder = new StringBuilder();
		        builder.append("Server " + guild.getName() + ":\n");

				AtomicInteger i = new AtomicInteger(1);
				Game game = Bot.games.get(guild);
				game.getPlayer(event.getUser()).getAllAnswers().stream().map(answer -> i.get() + " - " + answer + " " + Emoji.fromUnicode(game.getAnswer(i.getAndIncrement() - 1).stream().anyMatch(a -> answer != "" ? Game.correctAnswer(answer, a) : false) ? "U+2705" : "U+274C").getFormatted()).forEach(answer -> builder.append(answer + "\n"));
				event.getHook().sendMessage("Here are your answers " + event.getUser().getAsMention() + ": \n" + builder.toString()).queue();
			} else {
				event.getHook().sendMessage("There is no game currently started in this server. Please use the command \"__*/startgame*__\" to start a game.").queue();
			}
		}
	}

}
