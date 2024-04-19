package zzc.discord.shio.events;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import zzc.discord.shio.Bot;
import zzc.discord.shio.commands.CommandManager;
public class EventSetAnswers extends ListenerAdapter {
	private Guild guild;
	
	public EventSetAnswers setGuild(Guild guild) {
		this.guild = guild;
		return this;
	}
	
	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		if (!event.isFromType(ChannelType.PRIVATE) || event.getAuthor() == event.getJDA().getSelfUser()) {
			return;
		} else {
			List<List<String>> answerList = manageAnswers(event.getMessage().getContentRaw());
			Bot.games.get(this.guild).setAnswers(answerList);
			
			event.getChannel().sendMessage("The answers were correctly registered ! There were " + answerList.size() + " answers registered.").queue();
			
			CommandManager.waitAnswer = false;
			event.getJDA().removeEventListener(this);
		}
	}
	
	private List<List<String>> manageAnswers(String answers) {
		List<List<String>> answerList = Arrays.asList(answers.split("\n")).stream().map(s -> Arrays.asList(s.split("\\$"))).toList();
		
		//AtomicInteger i = new AtomicInteger(1);
		
		//answerList.forEach(answer -> System.out.println("Answer " + i.getAndIncrement() + ": " + answer.get(0) + (answer.size() > 1 ? "(" + answer.subList(1, answer.size()).toString() + ")" : "")));
		
		return answerList;
	}
}
