package dev.fabien2s.igloo;

import dev.fabien2s.igloo.providers.BlockTypeListDataProvider;
import dev.fabien2s.igloo.providers.EntityTypeListDataProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class IglooDataGeneratorMod implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator dataGenerator) {
        FabricDataGenerator.Pack pack = dataGenerator.createPack();
        pack.addProvider(BlockTypeListDataProvider::new);
        pack.addProvider(EntityTypeListDataProvider::new);
    }

}
