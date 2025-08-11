package de.splayfer.radio.general;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import de.splayfer.radio.Radio;
import net.dv8tion.jda.api.entities.Guild;

public class AudioLoadResult implements AudioLoadResultHandler {

    private String uri;
    private Guild guild;

    public AudioLoadResult(String uri, Guild guild) {
        this.uri = uri;
        this.guild = guild;
    }
    @Override
    public void trackLoaded(AudioTrack track) {
        System.out.println("track loaded");
        Radio.playerManager.getController(this.guild.getIdLong()).playTrack(track);
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {

    }

    @Override
    public void noMatches() {

    }

    @Override
    public void loadFailed(FriendlyException exception) {

    }

}
