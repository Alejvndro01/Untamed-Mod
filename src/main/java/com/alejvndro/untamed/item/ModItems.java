package com.alejvndro.untamed.item;

import com.alejvndro.untamed.Untamed;
import com.alejvndro.untamed.entity.ModEntities;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.HoneyBottleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = 
            DeferredRegister.create(ForgeRegistries.ITEMS, Untamed.MOD_ID);

    // Logo del mod
    public static final RegistryObject<Item> LOGO_TAB = ITEMS.register("logo_tab", 
        () -> new Item(new Item.Properties()));
        
    // Néctar de Mariposa (Comportamiento de Botella de Miel)
    public static final RegistryObject<Item> BUTTERFLY_NECTAR = ITEMS.register("butterfly_nectar", 
    () -> new HoneyBottleItem(new Item.Properties()
        .stacksTo(16) // Se apila como las botellas de miel
        .food(new FoodProperties.Builder()
            .nutrition(2) 
            .saturationMod(0.2f) 
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 200, 1), 1.0f) // 10 seg
            .effect(() -> new MobEffectInstance(MobEffects.JUMP, 400, 1), 1.0f) // 20 seg
            .effect(() -> new MobEffectInstance(MobEffects.SLOW_FALLING, 600, 0), 1.0f) // 30 seg
            .alwaysEat() 
            .build())
    ) {
        // Clase anónima para forzar la animación de beber sin crear archivos .java extra
        @Override
        public UseAnim getUseAnimation(ItemStack pStack) {
            return UseAnim.DRINK;
        }
    });

    // Polen (Nombre corto actualizado)
    public static final RegistryObject<Item> POLLEN = ITEMS.register("pollen", 
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> MORPHO_WING_SCALE = ITEMS.register("morpho_wing_scale", 
            () -> new Item(new Item.Properties()));

   // Huevo de Spawn (Azul Morpho y Negro)
        public static final RegistryObject<Item> BUTTERFLY_SPAWN_EGG = ITEMS.register("butterfly_spawn_egg", 
                () -> new ForgeSpawnEggItem(ModEntities.BUTTERFLY, 0x00b7ff, 0x000000, new Item.Properties()));

    // Huevo de Spawn del Tiburón Tigre (azul-gris oscuro y blanco)
    public static final RegistryObject<Item> TIGER_SHARK_SPAWN_EGG = ITEMS.register("tiger_shark_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.TIGER_SHARK, 0x5a6e82, 0xd8dce0, new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}