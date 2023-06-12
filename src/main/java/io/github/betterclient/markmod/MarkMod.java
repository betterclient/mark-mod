package io.github.betterclient.markmod;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class MarkMod implements ModInitializer {
	public static List<String> marked = new ArrayList<>();

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			register(dispatcher);
		});
	}

	public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(
				LiteralArgumentBuilder
						.<ServerCommandSource>literal("mark")
						.then(
								RequiredArgumentBuilder
										.<ServerCommandSource, String>argument("player", StringArgumentType.string())
										.executes(context -> {
											String playerName = StringArgumentType.getString(context, "player");

											return this.execute(playerName);
										})
						)
		);
	}

	private int execute(String playerName) {
		if(MarkMod.marked.contains(playerName)) {
			MarkMod.marked.remove(playerName);
			MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("§cRemoved mark of player: §6" + playerName));
		} else {
			MarkMod.marked.add(playerName);
			MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("§9Marked §6" + playerName));
		}

		return 1;
	}
}
