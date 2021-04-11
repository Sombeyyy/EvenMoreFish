package com.oheers.fish.selling;

import com.oheers.fish.config.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;

public class SellGUI {

    private final Player player;

    private Inventory menu;

    public boolean modified;

    private ItemStack sellIcon, filler, confirmIcon;

    public SellGUI(Player p) {
        this.player = p;
        this.modified = false;
        makeMenu();
        addFiller();
        setSellItem();
        this.player.openInventory(menu);
    }

    private void makeMenu() {
        menu = Bukkit.createInventory(null, 36, ChatColor.translateAlternateColorCodes('&', Messages.worthGUIName));
    }

    public Player getPlayer() {
        return player;
    }

    public void addFiller() {
        ItemStack fill = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta fillMeta = fill.getItemMeta();
        fillMeta.setDisplayName(ChatColor.RESET + "");
        fill.setItemMeta(fillMeta);

        this.filler = WorthNBT.attributeDefault(fill);

        menu.setItem(27, fill);
        menu.setItem(28, fill);
        menu.setItem(29, fill);
        menu.setItem(30, fill);
        // Sell icon
        menu.setItem(32, fill);
        menu.setItem(33, fill);
        menu.setItem(34, fill);
        menu.setItem(35, fill);
    }

    public void setSellItem() {
        System.out.println("SETTING SELL ITEM!");
        ItemStack sIcon = new ItemStack(Material.GOLD_INGOT);
        ItemMeta sellMeta = sIcon.getItemMeta();
        sellMeta.setDisplayName("" + ChatColor.GOLD + ChatColor.BOLD + "SELL");
        sellMeta.setLore(Arrays.asList(
                ChatColor.YELLOW + "value: " + ChatColor.UNDERLINE + getTotalWorth(),
                ChatColor.GRAY + "LEFT CLICK to sell the fish.",
                ChatColor.GRAY + "RIGHT CLICK to cancel."
        ));
        sIcon.setItemMeta(sellMeta);
        this.sellIcon = WorthNBT.attributeDefault(sIcon);
        menu.setItem(31, this.sellIcon);
    }

    public ItemStack getSellIcon() {
        return this.sellIcon;
    }

    public ItemStack getConfirmIcon() {
        return this.confirmIcon;
    }

    public void createConfirmIcon() {
        ItemStack confirm = new ItemStack(Material.GOLD_BLOCK);
        ItemMeta cMeta = confirm.getItemMeta();
        cMeta.setDisplayName("" + ChatColor.GOLD + ChatColor.BOLD + "CONFIRM");
        cMeta.setLore(Arrays.asList(
                ChatColor.YELLOW + "value: " + ChatColor.UNDERLINE + getTotalWorth(),
                ChatColor.GRAY + "LEFT CLICK to sell the fish.",
                ChatColor.GRAY + "RIGHT CLICK to cancel."
        ));
        confirm.setItemMeta(cMeta);
        this.confirmIcon = WorthNBT.attributeDefault(confirm);
    }

    public void setConfirmIcon() {
        this.menu.setItem(31, null);
        this.menu.setItem(31, this.confirmIcon);
    }

    public Inventory getMenu() {
        return menu;
    }

    public String getTotalWorth() {
        if (this.menu == null) return Double.toString(0.0d);

        double val = 0.0d;

        for (ItemStack is : this.menu.getContents()) {
            if (WorthNBT.getValue(is) != -1.0) {
                val += WorthNBT.getValue(is);
            }
        }

        return "$" + NumberFormat.getInstance(Locale.US).format(val);
    }

    public void close() {
        player.closeInventory();
        rescueAllItems();
    }

    public ItemStack getFiller() {
        return this.filler;
    }

    public void setMenu(Inventory inv) {
        this.menu = inv;
    }

    public void setModified(boolean mod) {
        this.modified = mod;
    }

    public boolean getModified() {
        return this.modified;
    }

    public void rescueAllItems() {
        for (ItemStack i : this.menu) {
            if (i != null) {
                if (!WorthNBT.isDefault(i)) {
                    this.player.getLocation().getWorld().dropItem(this.player.getLocation(), i);
                }
            }
        }
    }
}
