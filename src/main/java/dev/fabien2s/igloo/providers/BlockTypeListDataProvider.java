package dev.fabien2s.igloo.providers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.fabien2s.igloo.mixin.BlockBehaviourAccessor;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class BlockTypeListDataProvider implements DataProvider {

    private final FabricDataOutput output;

    public BlockTypeListDataProvider(FabricDataOutput output) {
        this.output = output;
    }

    @Override
    public @NotNull CompletableFuture<?> run(CachedOutput cachedOutput) {
        JsonObject rootObject = new JsonObject();
        for (Block block : BuiltInRegistries.BLOCK) {
            StateDefinition<Block, BlockState> stateDefinition = block.getStateDefinition();

            JsonObject blockObject = new JsonObject();
            {
                blockObject.addProperty("id", BuiltInRegistries.BLOCK.getId(block));

                {
                    BlockState defaultBlockState = block.defaultBlockState();
                    blockObject.addProperty("default_state_id", Block.getId(defaultBlockState));
                }

                blockObject.addProperty("has_collision", ((BlockBehaviourAccessor) block).hasCollision());

                blockObject.addProperty("explosion_resistance", block.getExplosionResistance());

                blockObject.addProperty("friction", block.getFriction());
                blockObject.addProperty("speed_factor", block.getSpeedFactor());
                blockObject.addProperty("jump_factor", block.getJumpFactor());

                JsonArray propertyContainerObject = new JsonArray();
                {
                    for (Property<?> property : stateDefinition.getProperties()) {
                        String propertyName = property.getName();
                        propertyContainerObject.add(propertyName);
                    }
                }
                blockObject.add("properties", propertyContainerObject);

                JsonArray stateContainerArray = new JsonArray();
                {
                    for (BlockState blockState : stateDefinition.getPossibleStates()) {
                        JsonObject stateObject = new JsonObject();
                        {
                            stateObject.addProperty("id", Block.getId(blockState));

                            JsonArray blockStatePropertyArray = new JsonArray();
                            {
                                for (Property<?> property : blockState.getProperties()) {
                                    Comparable<?> propertyValue = blockState.getValue(property);
                                    String propertyName = Util.getPropertyName(property, propertyValue);
                                    blockStatePropertyArray.add(propertyName);
                                }
                            }
                            stateObject.add("properties", blockStatePropertyArray);
                        }
                        stateContainerArray.add(stateObject);
                    }
                }
                blockObject.add("states", stateContainerArray);
            }

            ResourceLocation blockKey = BuiltInRegistries.BLOCK.getKey(block);
            rootObject.add(blockKey.toString(), blockObject);
        }

        Path path = this.output.getOutputFolder(PackOutput.Target.REPORTS).resolve("blocks.json");
        return DataProvider.saveStable(cachedOutput, rootObject, path);
    }

    @Override
    public @NotNull String getName() {
        return "Block Types";
    }

}
