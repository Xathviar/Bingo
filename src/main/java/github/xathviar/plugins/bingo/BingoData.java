package github.xathviar.plugins.bingo;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static github.xathviar.plugins.bingo.HelperClass.broadcastMessage;

public class BingoData {

    private static HashMap<Player, List<Material>> entityBingoMap;

    private static List<Material> bingoItems;

    private Startup startup;

    public BingoData(Startup startup) {
        entityBingoMap = new HashMap<>();
        bingoItems = new ArrayList<>();
        this.startup = startup;
        genItems();
    }
    

    public void checkItem(HumanEntity entity, ItemStack item) {
        checkItem((Player) entity, item);
    }

    public void checkItems(HumanEntity entity, Inventory inventory) {
        checkItems((Player) entity, inventory);
    }

    public void checkItem(Player entity, ItemStack item) {
        if (startup.hasStarted()) {
            if (bingoItems.contains(item.getType())
                    && !entityBingoMap.getOrDefault(entity, new ArrayList<Material>() {{
                add(Material.BARRIER);
            }}).contains(item.getType())) {
                if (entityBingoMap.get(entity) != null) {
                    List<Material> itemstacks = entityBingoMap.get(entity);
                    itemstacks.add(item.getType());
                    entityBingoMap.put(entity, itemstacks);
                } else {
                    List<Material> items = new ArrayList<>();
                    items.add(item.getType());
                    entityBingoMap.put(entity, items);
                }
                broadcastMessage(ChatColor.YELLOW + item.getType().toString() + ChatColor.RESET + " has been registered by " + entity.getDisplayName() + " (" + entityBingoMap.get(entity).size() + "/9)");
            }
            checkWin(entity);
        }
    }

    public void checkItems(Player entity, Inventory inventory) {
        if (startup.hasStarted()) {
            for (Material bingoItem : bingoItems) {
                if (inventory.contains(bingoItem) && !entityBingoMap.getOrDefault(entity, new ArrayList<Material>() {{
                    add(Material.BARRIER);
                }}).contains(bingoItem)) {
                    if (entityBingoMap.get(entity) != null) {
                        List<Material> itemStacks = entityBingoMap.get(entity);
                        itemStacks.add(bingoItem);
                        entityBingoMap.put(entity, itemStacks);
                    } else {
                        List<Material> itemstacks = new ArrayList<>();
                        itemstacks.add(bingoItem);
                        entityBingoMap.put(entity, itemstacks);
                    }
                    broadcastMessage(ChatColor.YELLOW + bingoItem.toString() + ChatColor.RESET + " has been registered by " + entity.getDisplayName() + " (" + entityBingoMap.get(entity).size() + "/9)");
                }
            }
            checkWin(entity);
        }
    }

    private void checkWin(Player entity) {
        if (entityBingoMap.get(entity) != null && entityBingoMap.get(entity).size() == 9) {
            genItems();
            entityBingoMap.clear();
            int[] time = startup.stopScheduler();
            if (time[0] != 0)
                broadcastMessage(String.format("%s won the Bingo after %02d Hours, %02d Minutes, %02d Seconds.", entity.getDisplayName(), time[0], time[1], time[2]));
            else if (time[1] != 0)
                broadcastMessage(String.format("%s won the Bingo after %02d Minutes, %02d Seconds.", entity.getDisplayName(), time[1], time[2]));
            else
                broadcastMessage(String.format("%s won the Bingo after %02d Seconds.", entity.getDisplayName(), time[2]));
        }
    }

    public void resetEntity(Player entity) {
        entityBingoMap.remove(entity);
    }

    public void displayBoard(Player entity) {
        Inventory inventory = Bukkit.createInventory(entity, 5 * 9, "Bingo Board");
        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, new ItemStack(Material.LIME_STAINED_GLASS_PANE));
        }
        for (int i = 0; i < 9; i++) {
            inventory.setItem(i + 9 * 4, new ItemStack(Material.LIME_STAINED_GLASS_PANE));
        }

        int counter = 0;
        for (int i = 1; i < 4; i++) {
            for (int j = 3; j < 6; j++) {
                ItemStack itemStack = new ItemStack(bingoItems.get(counter));
                itemStack.setLore(Collections.singletonList(ChatColor.RED + "Item not found" + ChatColor.RESET));
                if (entityBingoMap.get(entity) != null && entityBingoMap.get(entity).contains(bingoItems.get(counter))) {
                    itemStack.setLore(Collections.singletonList(ChatColor.GREEN + "Item found" + ChatColor.RESET));
                    itemStack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
                    itemStack.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                }
                inventory.setItem(9 * i + j, itemStack);
                counter++;
            }
        }
        entity.openInventory(inventory);
    }

    public void genItems() {
        Set<Material> items = new HashSet<>();
        List<Material> materials = new ArrayList<>(Arrays.asList(Material.class.getEnumConstants()));
        materials.remove(Material.AIR);
        materials.remove(Material.ATTACHED_MELON_STEM);
        materials.remove(Material.ATTACHED_PUMPKIN_STEM);
        materials.remove(Material.BARRIER);
        materials.remove(Material.BAT_SPAWN_EGG);
        materials.remove(Material.BEDROCK);
        materials.remove(Material.BEEHIVE);
        materials.remove(Material.BEETROOTS);
        materials.remove(Material.BEE_NEST);
        materials.remove(Material.BEE_SPAWN_EGG);
        materials.remove(Material.BLACK_SHULKER_BOX);
        materials.remove(Material.BLACK_BANNER);
        materials.remove(Material.BLACK_WALL_BANNER);
        materials.remove(Material.BLAZE_POWDER);
        materials.remove(Material.BLAZE_ROD);
        materials.remove(Material.BLAZE_SPAWN_EGG);
        materials.remove(Material.BLUE_BANNER);
        materials.remove(Material.BLUE_ICE);
        materials.remove(Material.BLUE_SHULKER_BOX);
        materials.remove(Material.BRAIN_CORAL);
        materials.remove(Material.BRAIN_CORAL_BLOCK);
        materials.remove(Material.BRAIN_CORAL_FAN);
        materials.remove(Material.BRAIN_CORAL_WALL_FAN);
        materials.remove(Material.BRICKS);
        materials.remove(Material.BROWN_BANNER);
        materials.remove(Material.BROWN_SHULKER_BOX);
        materials.remove(Material.BROWN_WALL_BANNER);
        materials.remove(Material.BUBBLE_COLUMN);
        materials.remove(Material.BUBBLE_CORAL_BLOCK);
        materials.remove(Material.BUBBLE_CORAL);
        materials.remove(Material.BUBBLE_CORAL_FAN);
        materials.remove(Material.BUBBLE_CORAL_WALL_FAN);
        materials.remove(Material.CARROTS);
        materials.remove(Material.CAT_SPAWN_EGG);
        materials.remove(Material.CAVE_AIR);
        materials.remove(Material.CAVE_SPIDER_SPAWN_EGG);
        materials.remove(Material.CHAIN_COMMAND_BLOCK);
        materials.remove(Material.CHAINMAIL_BOOTS);
        materials.remove(Material.CHAINMAIL_LEGGINGS);
        materials.remove(Material.CHAINMAIL_CHESTPLATE);
        materials.remove(Material.CHAINMAIL_HELMET);
        materials.remove(Material.CHICKEN_SPAWN_EGG);
        materials.remove(Material.CHIPPED_ANVIL);
        materials.remove(Material.CHORUS_FLOWER);
        materials.remove(Material.CHORUS_FRUIT);
        materials.remove(Material.CHORUS_PLANT);
        materials.remove(Material.COAL_ORE);
        materials.remove(Material.COARSE_DIRT);
        materials.remove(Material.COD_SPAWN_EGG);
        materials.remove(Material.COMMAND_BLOCK);
        materials.remove(Material.COMMAND_BLOCK_MINECART);
        materials.remove(Material.CONDUIT);
        materials.remove(Material.COW_SPAWN_EGG);
        materials.remove(Material.CREEPER_BANNER_PATTERN);
        materials.remove(Material.CREEPER_HEAD);
        materials.remove(Material.CREEPER_SPAWN_EGG);
        materials.remove(Material.CREEPER_WALL_HEAD);
        materials.remove(Material.CYAN_BANNER);
        materials.remove(Material.CYAN_SHULKER_BOX);
        materials.remove(Material.CYAN_WALL_BANNER);
        materials.remove(Material.DAMAGED_ANVIL);
        materials.remove(Material.DARK_OAK_WALL_SIGN);
        materials.remove(Material.DEAD_BRAIN_CORAL);
        materials.remove(Material.DEAD_BRAIN_CORAL_BLOCK);
        materials.remove(Material.DEAD_BRAIN_CORAL_FAN);
        materials.remove(Material.DEAD_BRAIN_CORAL_WALL_FAN);
        materials.remove(Material.DEAD_BUBBLE_CORAL);
        materials.remove(Material.DEAD_BUBBLE_CORAL_BLOCK);
        materials.remove(Material.DEAD_BUBBLE_CORAL_FAN);
        materials.remove(Material.DEAD_BUBBLE_CORAL_WALL_FAN);
        materials.remove(Material.DEAD_FIRE_CORAL);
        materials.remove(Material.DEAD_FIRE_CORAL_BLOCK);
        materials.remove(Material.DEAD_FIRE_CORAL_FAN);
        materials.remove(Material.DEAD_FIRE_CORAL_WALL_FAN);
        materials.remove(Material.DEAD_HORN_CORAL);
        materials.remove(Material.DEAD_HORN_CORAL_BLOCK);
        materials.remove(Material.DEAD_HORN_CORAL_FAN);
        materials.remove(Material.DEAD_HORN_CORAL_WALL_FAN);
        materials.remove(Material.DEAD_TUBE_CORAL);
        materials.remove(Material.DEAD_TUBE_CORAL_BLOCK);
        materials.remove(Material.DEAD_TUBE_CORAL_FAN);
        materials.remove(Material.DEAD_TUBE_CORAL_WALL_FAN);
        materials.remove(Material.DEBUG_STICK);
        materials.remove(Material.DIAMOND_HORSE_ARMOR);
        materials.remove(Material.DOLPHIN_SPAWN_EGG);
        materials.remove(Material.DONKEY_SPAWN_EGG);
        materials.remove(Material.DRAGON_BREATH);
        materials.remove(Material.DRAGON_EGG);
        materials.remove(Material.DRAGON_HEAD);
        materials.remove(Material.DRAGON_WALL_HEAD);
        materials.remove(Material.DROWNED_SPAWN_EGG);
        materials.remove(Material.ELDER_GUARDIAN_SPAWN_EGG);
        materials.remove(Material.ELYTRA);
        materials.remove(Material.EMERALD_ORE);
        materials.remove(Material.ENCHANTED_BOOK);
        materials.remove(Material.ENCHANTED_GOLDEN_APPLE);
        materials.remove(Material.ENDERMAN_SPAWN_EGG);
        materials.remove(Material.ENDERMITE_SPAWN_EGG);
        materials.remove(Material.ENDER_CHEST);
        materials.remove(Material.ENDER_EYE);
        materials.remove(Material.END_CRYSTAL);
        materials.remove(Material.END_GATEWAY);
        materials.remove(Material.END_PORTAL);
        materials.remove(Material.END_PORTAL_FRAME);
        materials.remove(Material.END_ROD);
        materials.remove(Material.END_STONE);
        materials.remove(Material.END_STONE_BRICK_SLAB);
        materials.remove(Material.END_STONE_BRICK_STAIRS);
        materials.remove(Material.END_STONE_BRICK_WALL);
        materials.remove(Material.EVOKER_SPAWN_EGG);
        materials.remove(Material.EXPERIENCE_BOTTLE);
        materials.remove(Material.FARMLAND);
        materials.remove(Material.FERN);
        materials.remove(Material.FILLED_MAP);
        materials.remove(Material.FIRE);
        materials.remove(Material.FIREWORK_ROCKET);
        materials.remove(Material.FIREWORK_STAR);
        materials.remove(Material.FIRE_CHARGE);
        materials.remove(Material.FIRE_CORAL);
        materials.remove(Material.FIRE_CORAL_BLOCK);
        materials.remove(Material.FIRE_CORAL_FAN);
        materials.remove(Material.FIRE_CORAL_WALL_FAN);
        materials.remove(Material.FLOWER_BANNER_PATTERN);
        materials.remove(Material.FOX_SPAWN_EGG);
        materials.remove(Material.FROSTED_ICE);
        materials.remove(Material.GHAST_SPAWN_EGG);
        materials.remove(Material.GHAST_TEAR);
        materials.remove(Material.GLOBE_BANNER_PATTERN);
        materials.remove(Material.GLOWSTONE);
        materials.remove(Material.GLOWSTONE_DUST);
        materials.remove(Material.GOLDEN_HORSE_ARMOR);
        materials.remove(Material.GRASS_PATH);
        materials.remove(Material.GRAY_BANNER);
        materials.remove(Material.GRAY_SHULKER_BOX);
        materials.remove(Material.GRAY_WALL_BANNER);
        materials.remove(Material.GREEN_BANNER);
        materials.remove(Material.GREEN_SHULKER_BOX);
        materials.remove(Material.GREEN_WALL_BANNER);
        materials.remove(Material.GUARDIAN_SPAWN_EGG);
        materials.remove(Material.HEART_OF_THE_SEA);
        materials.remove(Material.HONEY_BLOCK);
        materials.remove(Material.HONEY_BOTTLE);
        materials.remove(Material.HONEYCOMB);
        materials.remove(Material.HONEYCOMB_BLOCK);
        materials.remove(Material.HORN_CORAL);
        materials.remove(Material.HORN_CORAL_BLOCK);
        materials.remove(Material.HORN_CORAL_FAN);
        materials.remove(Material.HORN_CORAL_WALL_FAN);
        materials.remove(Material.HORSE_SPAWN_EGG);
        materials.remove(Material.HUSK_SPAWN_EGG);
        materials.remove(Material.ICE);
        materials.remove(Material.INFESTED_CHISELED_STONE_BRICKS);
        materials.remove(Material.INFESTED_COBBLESTONE);
        materials.remove(Material.INFESTED_CRACKED_STONE_BRICKS);
        materials.remove(Material.INFESTED_MOSSY_STONE_BRICKS);
        materials.remove(Material.INFESTED_STONE);
        materials.remove(Material.INFESTED_STONE_BRICKS);
        materials.remove(Material.IRON_HORSE_ARMOR);
        materials.remove(Material.JUNGLE_WALL_SIGN);
        materials.remove(Material.KNOWLEDGE_BOOK);
        materials.remove(Material.LAPIS_ORE);
        materials.remove(Material.LARGE_FERN);
        materials.remove(Material.LAVA);
        materials.remove(Material.LEAD);
        materials.remove(Material.LEATHER_HORSE_ARMOR);
        materials.remove(Material.LIGHT_BLUE_BANNER);
        materials.remove(Material.LIGHT_BLUE_WALL_BANNER);
        materials.remove(Material.LIGHT_BLUE_SHULKER_BOX);
        materials.remove(Material.LIGHT_GRAY_BANNER);
        materials.remove(Material.LIGHT_GRAY_WALL_BANNER);
        materials.remove(Material.LIGHT_GRAY_SHULKER_BOX);
        materials.remove(Material.LIME_BANNER);
        materials.remove(Material.LIME_WALL_BANNER);
        materials.remove(Material.LINGERING_POTION);
        materials.remove(Material.LLAMA_SPAWN_EGG);
        materials.remove(Material.MAGENTA_BANNER);
        materials.remove(Material.MAGENTA_SHULKER_BOX);
        materials.remove(Material.MAGENTA_WALL_BANNER);
        materials.remove(Material.MAP);
        materials.remove(Material.MAGMA_BLOCK);
        materials.remove(Material.MAGMA_CREAM);
        materials.remove(Material.MAGMA_CUBE_SPAWN_EGG);
        materials.remove(Material.MOJANG_BANNER_PATTERN);
        materials.remove(Material.MOOSHROOM_SPAWN_EGG);
        materials.remove(Material.MOSSY_COBBLESTONE);
        materials.remove(Material.MOSSY_COBBLESTONE_SLAB);
        materials.remove(Material.MOSSY_COBBLESTONE_STAIRS);
        materials.remove(Material.MOSSY_COBBLESTONE_WALL);
        materials.remove(Material.MOSSY_STONE_BRICKS);
        materials.remove(Material.MOSSY_STONE_BRICK_SLAB);
        materials.remove(Material.MOSSY_STONE_BRICK_STAIRS);
        materials.remove(Material.MOSSY_STONE_BRICK_WALL);
        materials.remove(Material.MOVING_PISTON);
        materials.remove(Material.MULE_SPAWN_EGG);
        materials.remove(Material.MUSIC_DISC_11);
        materials.remove(Material.MUSIC_DISC_13);
        materials.remove(Material.MUSIC_DISC_BLOCKS);
        materials.remove(Material.MUSIC_DISC_CAT);
        materials.remove(Material.MUSIC_DISC_CHIRP);
        materials.remove(Material.MUSIC_DISC_FAR);
        materials.remove(Material.MUSIC_DISC_MALL);
        materials.remove(Material.MUSIC_DISC_MELLOHI);
        materials.remove(Material.MUSIC_DISC_STAL);
        materials.remove(Material.MUSIC_DISC_STRAD);
        materials.remove(Material.MUSIC_DISC_WAIT);
        materials.remove(Material.MUSIC_DISC_WARD);
        materials.remove(Material.NAME_TAG);
        materials.remove(Material.NAUTILUS_SHELL);
        materials.remove(Material.NETHERRACK);
        materials.remove(Material.NETHER_BRICK);
        materials.remove(Material.NETHER_BRICK_FENCE);
        materials.remove(Material.NETHER_BRICK_SLAB);
        materials.remove(Material.NETHER_BRICK_STAIRS);
        materials.remove(Material.NETHER_BRICK_WALL);
        materials.remove(Material.NETHER_BRICKS);
        materials.remove(Material.NETHER_PORTAL);
        materials.remove(Material.NETHER_QUARTZ_ORE);
        materials.remove(Material.NETHER_STAR);
        materials.remove(Material.NETHER_WART);
        materials.remove(Material.NETHER_WART_BLOCK);
        materials.remove(Material.OBSERVER);
        materials.remove(Material.OAK_WALL_SIGN);
        materials.remove(Material.OCELOT_SPAWN_EGG);
        materials.remove(Material.ORANGE_BANNER);
        materials.remove(Material.ORANGE_WALL_BANNER);
        materials.remove(Material.ORANGE_SHULKER_BOX);
        materials.remove(Material.ZOMBIE_HEAD);
        materials.remove(Material.ZOMBIE_HORSE_SPAWN_EGG);
        materials.remove(Material.ZOMBIE_PIGMAN_SPAWN_EGG);
        materials.remove(Material.ZOMBIE_VILLAGER_SPAWN_EGG);
        materials.remove(Material.ZOMBIE_WALL_HEAD);
        materials.remove(Material.YELLOW_BANNER);
        materials.remove(Material.YELLOW_BED);
        materials.remove(Material.YELLOW_SHULKER_BOX);
        materials.remove(Material.YELLOW_WALL_BANNER);
        materials.remove(Material.WATER);
        materials.remove(Material.WALL_TORCH);
        materials.remove(Material.WANDERING_TRADER_SPAWN_EGG);
        materials.remove(Material.WET_SPONGE);
        materials.remove(Material.WHITE_BANNER);
        materials.remove(Material.WHITE_SHULKER_BOX);
        materials.remove(Material.WHITE_WALL_BANNER);
        materials.remove(Material.WITCH_SPAWN_EGG);
        materials.remove(Material.WITHER_ROSE);
        materials.remove(Material.WITHER_SKELETON_SKULL);
        materials.remove(Material.WITHER_SKELETON_SPAWN_EGG);
        materials.remove(Material.WITHER_SKELETON_WALL_SKULL);
        materials.remove(Material.WOLF_SPAWN_EGG);
        materials.remove(Material.WRITABLE_BOOK);
        materials.remove(Material.WRITTEN_BOOK);
        materials.remove(Material.VEX_SPAWN_EGG);
        materials.remove(Material.VILLAGER_SPAWN_EGG);
        materials.remove(Material.VINDICATOR_SPAWN_EGG);
        materials.remove(Material.VOID_AIR);
        materials.remove(Material.TALL_GRASS);
        materials.remove(Material.TALL_SEAGRASS);
        materials.remove(Material.TIPPED_ARROW);
        materials.remove(Material.TOTEM_OF_UNDYING);
        materials.remove(Material.TRADER_LLAMA_SPAWN_EGG);
        materials.remove(Material.TRIDENT);
        materials.remove(Material.TRIPWIRE);
        materials.remove(Material.TROPICAL_FISH_SPAWN_EGG);
        materials.remove(Material.TUBE_CORAL);
        materials.remove(Material.TUBE_CORAL_BLOCK);
        materials.remove(Material.TUBE_CORAL_FAN);
        materials.remove(Material.TUBE_CORAL_WALL_FAN);
        materials.remove(Material.TURTLE_EGG);
        materials.remove(Material.TURTLE_HELMET);
        materials.remove(Material.TURTLE_SPAWN_EGG);
        materials.remove(Material.SHULKER_SPAWN_EGG);
        materials.remove(Material.SHULKER_SHELL);
        materials.remove(Material.SHULKER_BOX);
        materials.remove(Material.SADDLE);
        materials.remove(Material.SALMON_SPAWN_EGG);
        materials.remove(Material.SCUTE);
        materials.remove(Material.SEA_LANTERN);
        materials.remove(Material.SEA_PICKLE);
        materials.remove(Material.SEAGRASS);
        materials.remove(Material.SHEEP_SPAWN_EGG);
        materials.remove(Material.SILVERFISH_SPAWN_EGG);
        materials.remove(Material.SKELETON_HORSE_SPAWN_EGG);
        materials.remove(Material.SKELETON_SKULL);
        materials.remove(Material.SKELETON_SPAWN_EGG);
        materials.remove(Material.SKELETON_WALL_SKULL);
        materials.remove(Material.SKULL_BANNER_PATTERN);
        materials.remove(Material.SLIME_BALL);
        materials.remove(Material.SLIME_BLOCK);
        materials.remove(Material.SLIME_SPAWN_EGG);
        materials.remove(Material.SMOOTH_RED_SANDSTONE);
        materials.remove(Material.SMOOTH_RED_SANDSTONE_SLAB);
        materials.remove(Material.SMOOTH_RED_SANDSTONE_STAIRS);
        materials.remove(Material.SNOW);
        materials.remove(Material.SNOW_BLOCK);
        materials.remove(Material.SPAWNER);
        materials.remove(Material.SPECTRAL_ARROW);
        materials.remove(Material.SPIDER_SPAWN_EGG);
        materials.remove(Material.SPLASH_POTION);
        materials.remove(Material.SPONGE);
        materials.remove(Material.SPRUCE_WALL_SIGN);
        materials.remove(Material.SQUID_SPAWN_EGG);
        materials.remove(Material.STICKY_PISTON);
        materials.remove(Material.STRAY_SPAWN_EGG);
        materials.remove(Material.STRUCTURE_BLOCK);
        materials.remove(Material.STRUCTURE_VOID);
        materials.remove(Material.SWEET_BERRY_BUSH);
        materials.remove(Material.RABBIT);
        materials.remove(Material.RABBIT_FOOT);
        materials.remove(Material.RABBIT_SPAWN_EGG);
        materials.remove(Material.RABBIT_STEW);
        materials.remove(Material.RAVAGER_SPAWN_EGG);
        materials.remove(Material.RED_BANNER);
        materials.remove(Material.RED_MUSHROOM_BLOCK);
        materials.remove(Material.RED_NETHER_BRICK_SLAB);
        materials.remove(Material.RED_NETHER_BRICK_STAIRS);
        materials.remove(Material.RED_NETHER_BRICK_WALL);
        materials.remove(Material.RED_NETHER_BRICKS);
        materials.remove(Material.RED_SAND);
        materials.remove(Material.RED_SANDSTONE_SLAB);
        materials.remove(Material.RED_SANDSTONE_STAIRS);
        materials.remove(Material.RED_SANDSTONE_WALL);
        materials.remove(Material.RED_WALL_BANNER);
        materials.remove(Material.REDSTONE_ORE);
        materials.remove(Material.REDSTONE_WALL_TORCH);
        materials.remove(Material.REDSTONE_WIRE);
        materials.remove(Material.REPEATING_COMMAND_BLOCK);
        materials.remove(Material.PACKED_ICE);
        materials.remove(Material.PANDA_SPAWN_EGG);
        materials.remove(Material.PARROT_SPAWN_EGG);
        materials.remove(Material.PETRIFIED_OAK_SLAB);
        materials.remove(Material.PHANTOM_MEMBRANE);
        materials.remove(Material.PHANTOM_SPAWN_EGG);
        materials.remove(Material.PIG_SPAWN_EGG);
        materials.remove(Material.PILLAGER_SPAWN_EGG);
        materials.remove(Material.PINK_BANNER);
        materials.remove(Material.PINK_SHULKER_BOX);
        materials.remove(Material.PINK_WALL_BANNER);
        materials.remove(Material.PISTON_HEAD);
        materials.remove(Material.PLAYER_HEAD);
        materials.remove(Material.PLAYER_WALL_HEAD);
        materials.remove(Material.PODZOL);
        materials.remove(Material.POLAR_BEAR_SPAWN_EGG);
        materials.remove(Material.POISONOUS_POTATO);
        materials.remove(Material.POTTED_BAMBOO);
        materials.remove(Material.POTTED_ACACIA_SAPLING);
        materials.remove(Material.POTTED_ALLIUM);
        materials.remove(Material.POTTED_AZURE_BLUET);
        materials.remove(Material.POTTED_BIRCH_SAPLING);
        materials.remove(Material.POTTED_BLUE_ORCHID);
        materials.remove(Material.POTTED_BROWN_MUSHROOM);
        materials.remove(Material.POTTED_CACTUS);
        materials.remove(Material.POTTED_CORNFLOWER);
        materials.remove(Material.POTTED_DANDELION);
        materials.remove(Material.POTTED_DARK_OAK_SAPLING);
        materials.remove(Material.POTTED_DEAD_BUSH);
        materials.remove(Material.POTTED_FERN);
        materials.remove(Material.POTTED_JUNGLE_SAPLING);
        materials.remove(Material.POTTED_LILY_OF_THE_VALLEY);
        materials.remove(Material.POTTED_OAK_SAPLING);
        materials.remove(Material.POTTED_ORANGE_TULIP);
        materials.remove(Material.POTTED_OXEYE_DAISY);
        materials.remove(Material.POTTED_PINK_TULIP);
        materials.remove(Material.POTTED_POPPY);
        materials.remove(Material.POTTED_RED_MUSHROOM);
        materials.remove(Material.POTTED_RED_TULIP);
        materials.remove(Material.POTTED_SPRUCE_SAPLING);
        materials.remove(Material.POTTED_WHITE_TULIP);
        materials.remove(Material.POTTED_WITHER_ROSE);
        materials.remove(Material.POPPED_CHORUS_FRUIT);
        materials.remove(Material.PRISMARINE);
        materials.remove(Material.PRISMARINE_BRICKS);
        materials.remove(Material.PRISMARINE_BRICK_SLAB);
        materials.remove(Material.PRISMARINE_BRICK_STAIRS);
        materials.remove(Material.PRISMARINE_CRYSTALS);
        materials.remove(Material.PRISMARINE_SHARD);
        materials.remove(Material.PRISMARINE_SLAB);
        materials.remove(Material.PRISMARINE_STAIRS);
        materials.remove(Material.PRISMARINE_WALL);
        materials.remove(Material.PUFFERFISH);
        materials.remove(Material.PUFFERFISH_BUCKET);
        materials.remove(Material.PUFFERFISH_SPAWN_EGG);
        materials.remove(Material.PUMPKIN_STEM);

        while (items.size() < 9) {
            items.add(materials.get(new Random().nextInt(materials.size())));
        }

        bingoItems.clear();
        bingoItems.addAll(items);
    }

    public void reset() {
        genItems();
        entityBingoMap.clear();
    }
}
