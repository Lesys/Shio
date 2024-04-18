package zzc.discord.shio.events;

import java.util.Arrays;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EventJoin extends ListenerAdapter {
	@Override
	public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
		List<String> channelNames = Arrays.asList("welcome", "bienvenue", "general", "général"); 
		String channelName = "";
		int i = 0;
		do {
			channelName = channelNames.get(i);
			System.err.println("Chan name: " + channelName);
		} while (event.getGuild().getTextChannelsByName(channelName, true).isEmpty() && ++i < channelNames.size());
		
		if (!event.getGuild().getTextChannelsByName(channelName, true).isEmpty())
			event.getGuild().getTextChannelsByName(channelName, true).get(0).sendMessage("Say hi to " + event.getMember().getAsMention() + " ! \\o").queue();
		else
			return;
	}
}
