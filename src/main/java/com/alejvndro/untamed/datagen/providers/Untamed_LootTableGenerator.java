package com.alejvndro.untamed.datagen.providers;

import com.alejvndro.untamed.registry.Untamed_Entities;
import com.alejvndro.untamed.registry.Untamed_Items;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.List;
import java.util.Set;

public class Untamed_LootTableGenerator extends LootTableProvider {

    public Untamed_LootTableGenerator(PackOutput output) {
        super(output, Set.of(), List.of(
                new SubProviderEntry(EntityLoot::new, LootContextParamSets.ENTITY)
        ));
    }

    private static class EntityLoot extends EntityLootSubProvider {

        EntityLoot() {
            super(FeatureFlags.REGISTRY.allFlags());
        }

        @Override
        public void generate() {
            // Mariposa: 10% de probabilidad de soltar Escama de Ala de Morpho al morir
            add(Untamed_Entities.BUTTERFLY.get(), LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(Untamed_Items.MORPHO_WING_SCALE.get())
                                    .when(LootItemRandomChanceCondition.randomChance(0.10f)))));
        }

        @Override
        protected Iterable<EntityType<?>> getKnownEntityTypes() {
            return List.of(Untamed_Entities.BUTTERFLY.get());
        }
    }
}
