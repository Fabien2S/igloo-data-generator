package dev.fabien2s.igloo.providers;

import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.DetectedVersion;
import net.minecraft.WorldVersion;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class VersionInfoDataProvider implements DataProvider {

    private final FabricDataOutput output;

    public VersionInfoDataProvider(FabricDataOutput output) {
        this.output = output;
    }

    @Override
    public @NotNull CompletableFuture<?> run(CachedOutput cachedOutput) {
        JsonObject rootObject = new JsonObject();
        WorldVersion version = DetectedVersion.tryDetectVersion();

        rootObject.addProperty("id", version.getId());
        rootObject.addProperty("name", version.getName());
        rootObject.addProperty("is_stable", version.isStable());
        rootObject.addProperty("protocol_id", version.getProtocolVersion());
        rootObject.addProperty("build_time", version.getBuildTime().toInstant().toEpochMilli());


        Path path = this.output.getOutputFolder().resolve("version.json");
        return DataProvider.saveStable(cachedOutput, rootObject, path);
    }

    @Override
    public @NotNull String getName() {
        return "Version Info";
    }

}
