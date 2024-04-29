package zzc.discord.shio.commands;

import java.util.*;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public abstract class Command {
	protected String commandName;
	protected String commandDescription;
	protected List<OptionData> commandOption;
	protected boolean guildOnly;
	protected SlashCommandData commandData;
	
	protected Command() {
		this.init();
		this.commandData = Commands.slash(this.commandName, this.commandDescription);
		
		if (this.guildOnly)
			this.commandData.setGuildOnly(true);
		
		if (this.commandOption != null && this.commandOption.size() > 0)
			this.commandData.addOptions(this.commandOption);
	}
	
	public CommandData getCommandData() {
		return this.commandData;
	}
	
	public boolean matchingName(String commandName) {
		return this.commandName.equals(commandName);
	}
	
	protected abstract void init();
	
	public abstract void exeuteCommand(@NotNull SlashCommandInteractionEvent event);
}
