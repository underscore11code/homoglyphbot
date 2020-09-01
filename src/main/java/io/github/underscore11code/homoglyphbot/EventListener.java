package io.github.underscore11code.homoglyphbot;

import net.dv8tion.jda.api.audit.ActionType;
import net.dv8tion.jda.api.audit.AuditLogEntry;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class EventListener extends ListenerAdapter {
    Logger logger = LoggerFactory.getLogger("EventListener");
    @Override
    public void onGuildMemberUpdateNickname(@NotNull GuildMemberUpdateNicknameEvent event) {
        logger.info(String.format("%#s had their nickname updated to %s in %s", event.getUser(), event.getNewNickname(), event.getGuild()));
        String cleanedNick = HomoglyphBot.getInstance().handleName(event.getNewNickname());
        if (cleanedNick.equals(event.getNewNickname())) return;
        event.getMember().modifyNickname(cleanedNick).reason("Cleaned due to nick update").queue();
        logger.info(String.format("%#s had their nickname cleaned to %s in %s", event.getUser(), cleanedNick, event.getGuild()));
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        logger.info(String.format("%#s joined  %s", event.getUser(), event.getGuild()));
        String cleanedNick = HomoglyphBot.getInstance().handleName(event.getUser().getName());
        if (cleanedNick.equals(event.getUser().getName())) return;
        event.getMember().modifyNickname(cleanedNick).reason("Cleaned due to guild join").queue();
        logger.info(String.format("%#s had their nickname cleaned to %s in %s", event.getUser(), cleanedNick, event.getGuild()));
    }
}
