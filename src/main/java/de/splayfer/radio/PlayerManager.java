package de.splayfer.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import de.splayfer.Radio;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class PlayerManager {

    public ConcurrentHashMap<Long, AudioPlayer> controller = new ConcurrentHashMap<Long, AudioPlayer>();

    public AudioPlayer getController(long guildid) {
        AudioPlayer mc = null;

        if(this.controller.containsKey(guildid)) {
            mc = this.controller.get(guildid);
        }
        else {
            mc = Radio.audioPlayerManager.createPlayer();

            this.controller.put(guildid, mc);
        }
        return mc;
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
