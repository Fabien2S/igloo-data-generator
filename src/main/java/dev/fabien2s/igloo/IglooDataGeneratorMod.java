package dev.fabien2s.igloo;

import dev.fabien2s.igloo.providers.EntityTypeDataProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.data.info.BlockListReport;

public class IglooDataGeneratorMod implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator dataGenerator) {
        FabricDataGenerator.Pack pack = dataGenerator.createPack();

        // default providers
        pack.addProvider((FabricDataGenerator.Pack.Factory<BlockListReport>) BlockListReport::new);

        // custom providers
        pack.addProvider(EntityTypeDataProvider::new);
    }

}
