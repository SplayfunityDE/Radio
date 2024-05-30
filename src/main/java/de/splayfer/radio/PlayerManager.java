package de.splayfer.radio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import de.splayfer.Radio;
import net.dv8tion.jda.api.entities.Guild;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class PlayerManager {

    public ConcurrentHashMap<Long, AudioPlayer> controller = new ConcurrentHashMap<>();

    public AudioPlayer getController(long guildid) {
        AudioPlayer player = null;

        if(this.controller.containsKey(guildid)) {
            player = this.controller.get(guildid);
        }
        else {
            Guild guild = Radio.jda.getGuildById(guildid);
            player = Radio.audioPlayerManager.createPlayer();
            guild.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(player));
            player.addListener(new TrackScheduler());
            player.setVolume(5);
            this.controller.put(guildid, player);
        }
        return player;
    }

    public long getGuildByPlayerHash(int hash) {
        for(AudioPlayer audioController : controller.values()) {
            if(audioController.hashCode() == hash) {
                AtomicLong guildId = new AtomicLong();
                controller.forEach((l, pl) -> {
                    if (pl == audioController) {
                        guildId.set(l);
                    }
                });
                return guildId.get();
            }
        }

        return -1;
    }

}
