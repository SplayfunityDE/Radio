package de.splayfer.radio.commands;

import de.splayfer.radio.RadioManager;
import de.splayfer.radio.RadioStream;
import de.splayfer.utils.DefaultMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RadioCommand extends ListenerAdapter {

    private static HashMap<InteractionHook, Integer> hooks = new HashMap<>();

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("radio")) {
            /*
            AudioManager manager = event.getGuild().getAudioManager();
            manager.openAudioConnection(event.getOption("kanal").getAsChannel().asAudioChannel());
            Radio.audioPlayerManager.loadItem("", new AudioLoadResult("", event.getGuild(), event));
             */
            EmbedBuilder banner = new EmbedBuilder();
            banner.setImage("https://cdn.discordapp.com/attachments/985551183479463998/1228072680205586503/banner_radio.png?ex=662ab6cf&is=661841cf&hm=bcbf0e1f0e7873e3e7ed62476b85058a8a1fc4c73aa9b1f1284e83cf4e34a1ae&");
            banner.setColor(0xffa600);
            EmbedBuilder channelEmbed = new EmbedBuilder();
            channelEmbed.setColor(0xffa600);
            channelEmbed.setTitle(":radio: Verwaltung der Radio Funktion");
            channelEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");
            StringBuilder stringBuilder = new StringBuilder();
            StringSelectMenu.Builder selectMenu = StringSelectMenu.create("radio.channel.select");
            selectMenu.setPlaceholder("\uD83D\uDCE1 Wähle einen Sender");
            List<RadioStream> radioStreamList = RadioStream.getAllRadioStreams();
            int count = 1;
            if (radioStreamList.isEmpty())
                stringBuilder.append("**`KEINE SENDER VORHANDEN`**");
            else {
                for (RadioStream radioStream : radioStreamList) {
                     if (radioStreamList.size() <= 24) {
                        stringBuilder.append(radioStream.getName()).append("\n");
                        selectMenu.addOption(radioStream.getName(), radioStream.getName());
                    } else {
                        while (count <= 24) {
                            stringBuilder.append(radioStream.getName()).append("\n");
                            selectMenu.addOption(radioStream.getName(), radioStream.getName());
                            count++;
                        }
                    }
                }
            }
            selectMenu.addOption("Neu hinzufügen", "radio.channel.addchannel", "Klicke, um einen weiteren Kanal hinzuzufügen", Emoji.fromFormatted("➕"));
            selectMenu.setMaxValues(1);
            channelEmbed.addField("Verfügbare Sender", stringBuilder.toString(), false);
            if (radioStreamList.size() > 24)
                hooks.put(event.replyEmbeds(banner.build(), channelEmbed.build()).addComponents(ActionRow.of(selectMenu.build()), ActionRow.of(Button.secondary("radio.channel.nextpage", "Nächste Seite").withEmoji(Emoji.fromFormatted("➡\uFE0F")))).setEphemeral(true).complete(), 1);
            else
                event.replyEmbeds(banner.build(), channelEmbed.build()).addActionRow(selectMenu.build()).setEphemeral(true).queue();

            EmbedBuilder configEmbed = new EmbedBuilder();
            configEmbed.setColor(0xffa600);
            configEmbed.setTitle("Kanal für Radio");
            configEmbed.addField("", "Kanal", true);
            if (RadioManager.getRadioChannel() != null)
                configEmbed.addField("", RadioManager.getRadioChannel().getAsMention(), true);
            else
                configEmbed.addField("", "**`NICHT FESTGELEGT`**", true);
            configEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");
            event.getHook().sendMessageEmbeds(configEmbed.build()).addActionRow(EntitySelectMenu.create("radio.config.select", EntitySelectMenu.SelectTarget.CHANNEL).setChannelTypes(ChannelType.VOICE, ChannelType.STAGE).setPlaceholder("\uD83D\uDCC2 Lege einen Sprachkanal fest").build()).setEphemeral(true).queue();
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getButton().getId().startsWith("radio")) {
            List<RadioStream> radioStreamList = RadioStream.getAllRadioStreams();
            int page;
            switch (event.getButton().getId().substring(6)) {
                case "channel.nextpage":
                    page = hooks.get(getHookWithId(event.getMessageIdLong()));
                    if (radioStreamList.size() > 24 * page) {
                        StringBuilder stringBuilder = new StringBuilder();
                        StringSelectMenu.Builder selectMenu = StringSelectMenu.create("radio.channel.select");
                        selectMenu.setPlaceholder("\uD83D\uDCE1 Wähle einen Sender");
                        int count = 1;
                        while (count <= 24) {
                            if (radioStreamList.get((24 * page + count) - 1) == null) break;
                            stringBuilder.append(radioStreamList.get((24 * page + count) - 1).getName()).append("\n");
                            selectMenu.addOption(radioStreamList.get((24 * page + count) - 1).getName(), radioStreamList.get((24 * page + count) - 1).getName());
                            count++;
                        }
                        selectMenu.addOption("Neu hinzufügen", "radio.channel.addchannel", "Klicke, um einen weiteren Kanal hinzuzufügen", Emoji.fromFormatted("➕"));
                        List<Button> buttons = List.of(Button.secondary("radio.channel.previospage", "Vorherige Seite").withEmoji(Emoji.fromFormatted("⬅\uFE0F")));
                        if (radioStreamList.size() > 24 * (page + 1))
                            buttons.add(Button.secondary("radio.channel.nextpage", "Nächste Seite").withEmoji(Emoji.fromFormatted("➡\uFE0F")));
                        event.editComponents(ActionRow.of(selectMenu.build()), ActionRow.of(buttons)).queue();
                        MessageEditBuilder builder = MessageEditBuilder.fromMessage(event.getHook().retrieveOriginal().complete());
                        event.getHook().editOriginalEmbeds(builder.getEmbeds().get(0), EmbedBuilder.fromData(builder.getEmbeds().get(1).toData()).clearFields().addField("Verfügbare Sender", stringBuilder.toString(), false).build()).queue();
                        hooks.remove(getHookWithId(event.getMessageIdLong()));
                        hooks.put(event.getHook(), page + 1);
                    } else {
                        event.editButton(null).queue();
                        event.getHook().sendMessageEmbeds(DefaultMessage.error("Nicht verfügbar", "Es scheint, als sind keine weiteren Seiten vorhanden...")).setEphemeral(true).queue();
                    }
                    break;
                case "channel.previospage":
                    page = hooks.get(getHookWithId(event.getMessageIdLong())) - 1;
                    if (radioStreamList.size() > 24 * page) {
                        StringBuilder stringBuilder = new StringBuilder();
                        StringSelectMenu.Builder selectMenu = StringSelectMenu.create("radio.channel.select");
                        selectMenu.setPlaceholder("\uD83D\uDCE1 Wähle einen Sender");
                        int count = 1;
                        while (count <= 24) {
                            if (radioStreamList.get((24 * page + count) - 1) == null) break;
                            stringBuilder.append(radioStreamList.get((24 * page + count) - 1).getName()).append("\n");
                            selectMenu.addOption(radioStreamList.get((24 * page + count) - 1).getName(), radioStreamList.get((24 * page + count) - 1).getName());
                            count++;
                        }
                        selectMenu.addOption("Neu hinzufügen", "radio.channel.addchannel", "Klicke, um einen weiteren Kanal hinzuzufügen", Emoji.fromFormatted("➕"));
                        List<Button> buttons = new ArrayList<>();
                        if (radioStreamList.size() > 24 * (page + 1))
                            buttons.add(Button.secondary("radio.channel.nextpage", "Nächste Seite").withEmoji(Emoji.fromFormatted("➡\uFE0F")));
                        if (page > 1)
                            buttons.add(Button.secondary("radio.channel.previospage", "Vorherige Seite").withEmoji(Emoji.fromFormatted("⬅\uFE0F")));
                        event.editComponents(ActionRow.of(selectMenu.build()), ActionRow.of(buttons)).queue();
                        MessageEditBuilder builder = MessageEditBuilder.fromMessage(event.getHook().retrieveOriginal().complete());
                        event.getHook().editOriginalEmbeds(builder.getEmbeds().get(0), EmbedBuilder.fromData(builder.getEmbeds().get(1).toData()).clearFields().addField("Verfügbare Sender", stringBuilder.toString(), false).build()).queue();
                        hooks.remove(getHookWithId(event.getMessageIdLong()));
                        hooks.put(event.getHook(), page + 1);
                    } else {
                        event.editButton(null).queue();
                        event.getHook().sendMessageEmbeds(DefaultMessage.error("Nicht verfügbar", "Es scheint, als sind keine weiteren Seiten vorhanden...")).setEphemeral(true).queue();
                    }
                    break;
            }
        }
    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        if (event.getSelectMenu().getId().startsWith("radio")) {
            switch (event.getSelectMenu().getId().substring(6)) {
                case "channel.select":
                    if (event.getSelectedOptions().get(0).getValue().equals("radio.channel.addchannel")) {
                        event.replyModal(Modal.create("radio.channel.addchannel", "Radiokanal hinzufügen")
                                .addComponents(
                                        ActionRow.of(TextInput.create("name", "Name", TextInputStyle.SHORT)
                                                .setPlaceholder("Name des Senders")
                                                .setRequired(true).build()),
                                        ActionRow.of(TextInput.create("url", "Url", TextInputStyle.PARAGRAPH)
                                                .setPlaceholder("Link zum Stream")
                                                .setRequired(true).build()))
                                .build()).queue();
                    } else {
                        RadioManager.enableStream(RadioStream.getRadioStream(event.getValues().get(0)));
                        RadioManager.checkForRadio();
                        event.deferEdit().queue();
                    }
                    break;
            }
        }
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if (event.getModalId().startsWith("radio")) {
            switch (event.getModalId().substring(6)) {
                case "channel.addchannel":
                    new RadioStream(event.getValue("name").getAsString(), event.getValue("url").getAsString()).insertToMongoDB();
                    event.reply("Sender " + event.getValue("name").getAsString() + " erfolgreich hinzugefügt").setEphemeral(true).queue();
                    break;
            }
        }
    }

    @Override
    public void onEntitySelectInteraction(EntitySelectInteractionEvent event) {
        if (event.getSelectMenu().getId().startsWith("radio")) {
            switch (event.getSelectMenu().getId().substring(6)) {
                case "config.select":
                    RadioManager.updateRadioChannel(event.getMentions().getChannels().get(0));
                    event.editMessageEmbeds(EmbedBuilder.fromData(event.getMessage().getEmbeds().get(0).toData()).clearFields().addField("", "Kanal", true).addField("", event.getMentions().getChannels().get(0).getAsMention(), true).build()).queue();
                    RadioManager.checkForRadio();
                    break;
            }
        }
    }

    public static InteractionHook getHookWithId(long id) {
        for (InteractionHook hook : hooks.keySet())
            if (hook.retrieveOriginal().complete().getIdLong() == id)
                return hook;
        return null;
    }
}
