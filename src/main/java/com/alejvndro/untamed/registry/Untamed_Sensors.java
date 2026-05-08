package com.alejvndro.untamed.registry;

import com.alejvndro.untamed.Untamed_Main;
import com.alejvndro.untamed.entity.ai.sensors.TigerSharkConduitSensor;
import com.alejvndro.untamed.entity.ai.sensors.generic.NearestSpecificBlockSensor;
import com.alejvndro.untamed.entity.animal.TigerSharkEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Untamed_Sensors {
    public static final DeferredRegister<SensorType<?>> SENSOR_TYPES = 
            DeferredRegister.create(ForgeRegistries.SENSOR_TYPES, Untamed_Main.MOD_ID);

    // 1. Sensor para detectar al jugador con el efecto de Canalizador (Conduit Power)
    public static final RegistryObject<SensorType<TigerSharkConduitSensor>> TIGER_SHARK_CONDUIT_PLAYER = 
            SENSOR_TYPES.register("shark_near_conduit_player", () -> new SensorType<>(TigerSharkConduitSensor::new));

    // 2. Sensor para detectar bloques de interés (Conduit u otros atractores marinos)
    // Usamos la clase genérica NearestSpecificBlockSensor que creamos
    public static final RegistryObject<SensorType<NearestSpecificBlockSensor<TigerSharkEntity>>> TIGER_SHARK_NEAREST_CONDUIT = 
            SENSOR_TYPES.register("shark_near_conduit", () -> new SensorType<>(() -> 
                    new NearestSpecificBlockSensor<>(
                            // Buscamos el bloque de Conduit directamente (estilo Primal)
                            Blocks.CONDUIT, 
                            24, // Radio horizontal (XZ)
                            24  // Radio vertical (Y)
                    )));

    // 3. Opcional: Sensor por TAGS (Si quieres que el tiburón busque varios tipos de bloques como en Primal)
    public static final RegistryObject<SensorType<NearestSpecificBlockSensor<TigerSharkEntity>>> TIGER_SHARK_ATTRACTORS = 
            SENSOR_TYPES.register("shark_attractors", () -> new SensorType<>(() -> 
                    new NearestSpecificBlockSensor<>(
                            TagKey.create(Registries.BLOCK, new ResourceLocation("untamed", "shark_attractors")), 
                            24, 24
                    )));
}