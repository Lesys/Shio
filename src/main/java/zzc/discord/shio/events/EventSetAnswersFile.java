package zzc.discord.shio.events;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import zzc.discord.shio.Bot;
import zzc.discord.shio.commands.CommandManager;
public class EventSetAnswersFile extends EventSetAnswers {
	
	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		if (!event.isFromType(ChannelType.PRIVATE) || event.getAuthor() == event.getJDA().getSelfUser()) {
			return;
		} else if (event.getMessage().getAttachments().size() <= 0) {
			event.getMessage().reply("Please put a file attached to your message so I can manage your answers. Be sure to respect the format asked.\nPlease reuse the previous command once more.").queue();
			
			CommandManager.waitAnswer = false;
			event.getJDA().removeEventListener(this);
		} else {
			Attachment attach = event.getMessage().getAttachments().get(0);
			try {
				InputStream inputStream = attach.getProxy().download().get();
				byte[] bytes = inputStream.readAllBytes();
				
				String answers = new String(bytes, StandardCharsets.UTF_8);
				
				inputStream.close();

				List<List<String>> answerList = this.manageAnswers(answers);
				Bot.games.get(this.guild).setAnswers(answerList);
				
				event.getChannel().sendMessage("The answers were correctly registered ! There were " + answerList.size() + " answers registered.").queue();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			CommandManager.waitAnswer = false;
			event.getJDA().removeEventListener(this);
		}
	}
}
