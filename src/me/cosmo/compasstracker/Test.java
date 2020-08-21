package me.dhruv.nightmare.Events;



import me.dhruv.nightmare.Commands.commands;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityToggleSwimEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class events extends commands implements Listener {

    @EventHandler
    public void creaturespawn(CreatureSpawnEvent cse, EntityToggleSwimEvent playerSwim, PlayerRespawnEvent respawnplayer, PlayerItemConsumeEvent playerate) {

        if (nightmareStart = true) {
            playerSwim.setCancelled(true);
            if (cse.getEntity().getType().name().equalsIgnoreCase("Player"))
                return;
            cse.getEntity().setInvulnerable(true);
            if (cse.getEntityType() == EntityType.CREEPER) {

                Creeper creeper = (Creeper) cse.getEntity();



                creeper.setPowered(true);

            }
            if (cse.getEntityType() == EntityType.SKELETON) {

                Skeleton skel = (Skeleton) cse.getEntity();

                skel.setInvulnerable(true);

                ItemStack opbow = new ItemStack(Material.BOW);
                opbow.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 10);
                opbow.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 10);
                opbow.addUnsafeEnchantment(Enchantment.DURABILITY, 1000);
                opbow.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1000);


                skel.getEquipment().setItemInMainHand(new ItemStack(opbow));
                skel.getEquipment().setItemInMainHandDropChance(0);

            }
            if (cse.getEntityType() == EntityType.ZOMBIE) {

                Zombie zomb = (Zombie) cse.getEntity();

                zomb.setBaby(true);
                zomb.setInvulnerable(true);
                ItemStack opSword = new ItemStack(Material.DIAMOND_SWORD);
                opSword.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 10);
                opSword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1000);
                opSword.addUnsafeEnchantment(Enchantment.DURABILITY, 1000);

                zomb.getEquipment().setItemInMainHand(new ItemStack(opSword));
                zomb.getEquipment().setItemInMainHandDropChance(0);

            }
            if (cse.getEntityType() == EntityType.SPIDER) {

                Spider spid = (Spider) cse.getEntity();

                spid.setInvulnerable(true);

                ItemStack opSword = new ItemStack(Material.DIAMOND_SWORD);
                opSword.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 10);
                opSword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1000);
                opSword.addUnsafeEnchantment(Enchantment.DURABILITY, 1000);

                spid.getEquipment().setItemInMainHand(opSword);
                spid.getEquipment().setItemInMainHandDropChance(0);

            }

            if (cse.getEntityType() == EntityType.ENDERMAN) {

                Enderman enderM = (Enderman) cse.getEntity();

                enderM.setInvulnerable(true);

                ItemStack opSword = new ItemStack(Material.DIAMOND_SWORD);
                opSword.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 10);
                opSword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1000);
                opSword.addUnsafeEnchantment(Enchantment.DURABILITY, 1000);

                enderM.getEquipment().setItemInMainHand(opSword);
                enderM.getEquipment().setItemInMainHandDropChance(0);

            }
            if (cse.getEntityType() == EntityType.ENDER_DRAGON) {

                EnderDragon enderD = (EnderDragon) cse.getEntity();
                enderD.setHealth(10000);


            }
            if (playerate.getItem().getType().name().equalsIgnoreCase("potion"))
                return;
            if (playerate.getItem().getType().name().equalsIgnoreCase("bucket"))
                return;
            playerate.getPlayer().setHealth(.5);





        }
    }
}









