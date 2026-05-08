package com.alejvndro.untamed.entity.ai.sensors.generic;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class NearestSpecificBlockSensor<T extends LivingEntity> extends Sensor<T> {
    private final Predicate<T> shouldScan;
    private final BiPredicate<T, BlockPos> isValidBlock;
    private final int xzRange;
    private final int yRange;

    // Constructor para buscar por un TAG de bloques (como shark_attractors)
    public NearestSpecificBlockSensor(TagKey<Block> blockTag, int xzRange, int yRange) {
        this((entity) -> true, (entity, pos) -> entity.level().getBlockState(pos).is(blockTag), xzRange, yRange);
    }

    // Constructor para buscar un BLOQUE específico
    public NearestSpecificBlockSensor(Block block, int xzRange, int yRange) {
        this((entity) -> true, (entity, pos) -> entity.level().getBlockState(pos).is(block), xzRange, yRange);
    }

    // Constructor maestro con predicados personalizados (estilo Primal)
    public NearestSpecificBlockSensor(Predicate<T> shouldScan, BiPredicate<T, BlockPos> isValidBlock, int xzRange, int yRange) {
        super(20); // Se ejecuta cada 1 segundo (20 ticks)
        this.shouldScan = shouldScan;
        this.isValidBlock = isValidBlock;
        this.xzRange = xzRange;
        this.yRange = yRange;
    }

    @Override
    public Set<MemoryModuleType<?>> requires() {
        // Usamos NEAREST_REPELLENT como memoria temporal para guardar el bloque encontrado
        return ImmutableSet.of(MemoryModuleType.NEAREST_REPELLENT);
    }

    @Override
    protected void doTick(ServerLevel level, T entity) {
        if (this.shouldScan.test(entity)) {
            BlockPos entityPos = entity.blockPosition();
            Optional<BlockPos> foundBlock = BlockPos.findClosestMatch(entityPos, xzRange, yRange, 
                (pos) -> this.isValidBlock.test(entity, pos));

            if (foundBlock.isPresent()) {
                // Guardamos la posición del bloque en la memoria del cerebro
                entity.getBrain().setMemory(MemoryModuleType.NEAREST_REPELLENT, foundBlock.get());
            } else {
                entity.getBrain().eraseMemory(MemoryModuleType.NEAREST_REPELLENT);
            }
        }
    }
}