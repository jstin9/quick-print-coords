package com.jstn9;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import me.shedaniel.clothconfig2.gui.entries.StringListEntry;
import net.minecraft.text.Text;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setTitle(Text.translatable("config.coordinateoutput.title"))
                    .setSavingRunnable(KeyInputHandler::saveConfig)
                    .setParentScreen(parent);

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            StringListEntry coordinatesTemplateEntry = entryBuilder.startTextField(
                            Text.translatable("config.coordinateoutput.option.coordinates_template"),
                            KeyInputHandler.getCoordinatesTemplate())
                    .setDefaultValue("My cords: {X}, {Y}, {Z}, {DIMENSION}")
                    .setTooltip(Text.translatable("config.coordinateoutput.option.coordinates_template.tooltip"))
                    .setSaveConsumer(KeyInputHandler::setCoordinatesTemplate)
                    .build();

            BooleanListEntry showCoordinatesEntry = entryBuilder.startBooleanToggle(
                            Text.translatable("config.coordinateoutput.option.show_coordinates"),
                            KeyInputHandler.isShowCoordinates())
                    .setDefaultValue(true)
                    .setTooltip(Text.translatable("config.coordinateoutput.option.show_coordinates.tooltip"))
                    .setSaveConsumer(KeyInputHandler::setShowCoordinates)
                    .build();

            StringListEntry overworldTextEntry = entryBuilder.startTextField(
                            Text.translatable("config.coordinateoutput.option.overworld_text"),
                            KeyInputHandler.getOverworldText())
                    .setDefaultValue("Overworld")
                    .setTooltip(Text.translatable("config.coordinateoutput.option.overworld_text.tooltip"))
                    .setSaveConsumer(KeyInputHandler::setOverworldText)
                    .build();

            StringListEntry netherTextEntry = entryBuilder.startTextField(
                            Text.translatable("config.coordinateoutput.option.nether_text"),
                            KeyInputHandler.getNetherText())
                    .setDefaultValue("Nether")
                    .setTooltip(Text.translatable("config.coordinateoutput.option.nether_text.tooltip"))
                    .setSaveConsumer(KeyInputHandler::setNetherText)
                    .build();

            StringListEntry endTextEntry = entryBuilder.startTextField(
                            Text.translatable("config.coordinateoutput.option.end_text"),
                            KeyInputHandler.getEndText())
                    .setDefaultValue("End")
                    .setTooltip(Text.translatable("config.coordinateoutput.option.end_text.tooltip"))
                    .setSaveConsumer(KeyInputHandler::setEndText)
                    .build();

            BooleanListEntry showDeathMessageEntry = entryBuilder.startBooleanToggle(
                            Text.translatable("config.coordinateoutput.option.show_death_message"),
                            KeyInputHandler.isShowDeathMessage())
                    .setDefaultValue(true)
                    .setTooltip(Text.translatable("config.coordinateoutput.option.show_death_message.tooltip"))
                    .setSaveConsumer(KeyInputHandler::setShowDeathMessage)
                    .build();

            StringListEntry deathMessageTemplateEntry = entryBuilder.startTextField(
                            Text.translatable("config.coordinateoutput.option.death_message_template"),
                            KeyInputHandler.getDeathMessageTemplate())
                    .setDefaultValue("You died at: {X}, {Y}, {Z}, {DIMENSION}")
                    .setTooltip(Text.translatable("config.coordinateoutput.option.death_message_template.tooltip"))
                    .setSaveConsumer(KeyInputHandler::setDeathMessageTemplate)
                    .build();

            builder.getOrCreateCategory(Text.translatable("config.coordinateoutput.category.general"))
                    .addEntry(coordinatesTemplateEntry)
                    .addEntry(showCoordinatesEntry)
                    .addEntry(overworldTextEntry)
                    .addEntry(netherTextEntry)
                    .addEntry(endTextEntry)
                    .addEntry(showDeathMessageEntry)
                    .addEntry(deathMessageTemplateEntry);

            return builder.build();
        };
    }
}
