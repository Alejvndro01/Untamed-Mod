package com.alejvndro.untamed.registry;

import com.alejvndro.untamed.Untamed_Main;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Untamed_Items {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Untamed_Main.MOD_ID);

    // Registro del Huevo de Tiburón Tigre
    // Parámetros: Entidad, Color de fondo, Color de manchas, Propiedades
    public static final RegistryObject<Item> TIGER_SHARK_SPAWN_EGG = ITEMS.register("tiger_shark_spawn_egg",
            () -> new ForgeSpawnEggItem(Untamed_Entities.TIGER_SHARK, 0x4E5C5F, 0x1A1A1A, new Item.Properties()));

    // Ítem opcional para el icono de la pestaña (puedes usar el mismo huevo si quieres)
    public static final RegistryObject<Item> LOGO_TAB = ITEMS.register("logo_tab",
            () -> new Item(new Item.Properties()));
}