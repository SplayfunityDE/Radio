package de.splayfer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import de.splayfer.radio.RadioManager;
import de.splayfer.radio.PlayerManager;
import de.splayfer.utils.CommandManager;
import dev.lavalink.youtube.YoutubeAudioSourceManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class  Radio {

    public static JDA jda;
    public static JDABuilder builder;
    public static PlayerManager playerManager;
    public static Guild mainGuild;
    public static Guild emojiServerGuild;
    public static Guild emojiServerGuild2;

    public static AudioPlayerManager audioPlayerManager = new DefaultAudioPlayerManager();

    public static void main(String[] args) throws InterruptedException {
        builder = JDABuilder.createDefault("OTQxNzUwODMwMTc0OTg2MzMw.GtfVZH.mksuDQ_ZrQEaqIGuntfmXv6ha2nUrMN7FZvgUM")
                .setStatus(OnlineStatus.ONLINE)
                .setActivity(Activity.streaming("🌀SPLΛYFUNITY🌀", "https://twitch.tv/splayfer"))
                .addEventListeners(new ReadyEventClass());

        audioPlayerManager = new DefaultAudioPlayerManager();
        audioPlayerManager.registerSourceManager(new YoutubeAudioSourceManager());
        AudioSourceManagers.registerRemoteSources(audioPlayerManager, YoutubeAudioSourceManager.class);
        playerManager = new PlayerManager();

        MongoDBDatabase.connect();
        RadioManager.init();

        jda = builder.build();

        CommandManager.initCommands(jda.awaitReady());
        RadioManager.checkForRadio();
    }
}

class ReadyEventClass extends ListenerAdapter {
    @Override
    public void onReady(ReadyEvent event) {
        Radio.mainGuild = event.getJDA().getGuildById("873506353551925308");
        Radio.emojiServerGuild = event.getJDA().getGuildById("877158057988202496");
        Radio.emojiServerGuild2 = event.getJDA().getGuildById("879786460667052083");
    }
}