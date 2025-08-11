package de.splayfer.radio.general;

import de.splayfer.radio.MongoDBDatabase;
import de.splayfer.radio.Radio;
import de.splayfer.radio.general.commands.RadioCommand;
import de.splayfer.radio.utils.CommandManager;
import de.splayfer.radio.utils.enums.Guilds;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.bson.Document;

public class RadioManager {

    static MongoDBDatabase mongoDB = MongoDBDatabase.getDatabase("splayfunity");

    public static void init() {
        Radio.builder.addEventListeners(new RadioCommand());
        CommandManager.addCommands(Guilds.MAIN,
                Commands.slash("radio", "\uD83D\uDCFB │ Verwalte den Radio Bot")
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)));
    }

    public static void updateRadioChannel(Channel channel) {
        if (mongoDB.exists("config", "identifier", "radio"))
            mongoDB.updateLine("config", "identifier", "radio", "channel", channel.getIdLong());
        else
            mongoDB.insert("config", new Document("identifier", "radio").append("channel", channel.getIdLong()).append("stream", null));
    }

    public static void enableStream(RadioStream radioStream) {
        mongoDB.updateLine("config", "identifier", "radio", "stream", radioStream.getName());
    }

    public static Channel getRadioChannel() {
        if (mongoDB.exists("config", "identifier", "radio"))
            return Radio.jda.getGuildChannelById(mongoDB.find("config", "identifier", "radio").first().getLong("channel"));
        return null;
    }

    public static void checkForRadio() {
        if (mongoDB.exists("config", "identifier", "radio")) {
            Document document = mongoDB.find("config", "identifier", "radio").first();
            AudioChannel audioChannel = Radio.jda.getChannelById(AudioChannel.class, document.getLong("channel"));
            audioChannel.getGuild().getAudioManager().openAudioConnection(audioChannel);
            if (document.get("stream") != null) {
                String url = mongoDB.find("radio", "name", document.getString("stream")).first().getString("url");
                Radio.audioPlayerManager.loadItem(url, new AudioLoadResult(url, audioChannel.getGuild()));
                System.out.println("loaded: " + url);
            }
        }
    }

}
