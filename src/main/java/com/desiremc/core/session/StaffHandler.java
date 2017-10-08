package com.desiremc.core.session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.Vector;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.FileHandler;
import com.desiremc.core.api.LangHandler;
import com.desiremc.core.report.Report;
import com.desiremc.core.report.ReportHandler;
import com.desiremc.core.thread.ClicksPerSecondThread;
import com.desiremc.core.utils.DateUtils;

public class StaffHandler
{

    private static StaffHandler instance;
    private HashMap<UUID, ItemStack[]> inventories;
    private HashMap<UUID, Integer> cpsTests;
    private HashMap<UUID, Boolean> frozenPlayers;
    private List<UUID> hiddenPlayers;
    private List<UUID> staffChat;
    private int numCPSTests;
    private boolean chatEnabled;

    public HashMap<UUID, Integer> pages;

    private static final LangHandler LANG = DesireCore.getLangHandler();

    public StaffHandler()
    {
        inventories = new HashMap<>();
        cpsTests = new HashMap<>();
        hiddenPlayers = new LinkedList<>();
        frozenPlayers = new HashMap<>();
        staffChat = new ArrayList<>();
        numCPSTests = 0;
        pages = new HashMap<>();
    }

    public static void initialize()
    {
        instance = new StaffHandler();
    }

    public static StaffHandler getInstance()
    {
        return instance;
    }

    public boolean inStaffMode(Player p)
    {
        return inventories.containsKey(p.getUniqueId());
    }

    public boolean runningCPSTests()
    {
        return numCPSTests > 0;
    }

    public HashMap<UUID, Integer> getCPS()
    {
        return cpsTests;
    }

    public void handleCPSTest(PlayerInteractEvent e)
    {
        CPSTest(e.getPlayer());
    }

    public boolean CPSTest(Player p)
    {
        Integer cps = cpsTests.get(p.getUniqueId());

        if (cps != null)
        {
            cpsTests.put(p.getUniqueId(), cps + 1);
            return true;
        }

        return false;
    }

    public void decreaseNumCPSTests()
    {
        numCPSTests--;
    }

    public void enableStaffMode(Player p)
    {
        inventories.put(p.getUniqueId(), p.getInventory().getContents());
        p.getInventory().clear();
        p.getInventory().addItem(new ItemStack(Material.COMPASS, 1));
        p.getInventory().addItem(new ItemStack(Material.EYE_OF_ENDER, 1));
        p.getInventory().addItem(new ItemStack(Material.CLAY, 1));
        p.getInventory().addItem(new ItemStack(Material.PAPER, 1));
        p.getInventory().addItem(new ItemStack(Material.BLAZE_ROD, 1));
        p.getInventory().addItem(new ItemStack(Material.WATCH, 1));
        p.getInventory().addItem(new ItemStack(Material.CHEST, 1));
        p.getInventory().addItem(new ItemStack(Material.LEASH, 1));
        LANG.sendString(p, "staff.staff-on");

    }

    public boolean inStaffChat(Player p)
    {
        return staffChat.contains(p.getUniqueId());
    }

    public List<UUID> getAllInStaffChat()
    {
        return staffChat;
    }

    public void toggleStaffChat(Player p)
    {
        if (inStaffChat(p))
        {
            staffChat.remove(p.getUniqueId());
        }
        else
        {
            staffChat.add(p.getUniqueId());
        }
    }

    public void disableStaffMode(Player p)
    {
        if (inStaffMode(p))
        {
            p.getInventory().setContents(inventories.get(p.getUniqueId()));
            inventories.remove(p.getUniqueId());
            LANG.sendString(p, "staff.staff-off");
        }
    }

    public void toggleStaffMode(Player p)
    {
        if (inStaffMode(p))
        {
            disableStaffMode(p);
        }

        enableStaffMode(p);
    }

    public void toggleChat()
    {
        if (chatEnabled)
        {
            chatEnabled = false;
            return;
        }
        chatEnabled = true;
    }

    public boolean isChatEnabled()
    {
        return chatEnabled;
    }

    public boolean isFrozen(Player p)
    {
        Boolean frozen = frozenPlayers.get(p.getUniqueId());

        if (frozen == null)
        {
            return false;
        }

        return frozen;
    }

    public void removeIsFrozenObject(Player p)
    {
        frozenPlayers.remove(p.getUniqueId());
    }

    public void unfreezePlayer(Player player)
    {
        if (isFrozen(player))
        {
            frozenPlayers.put(player.getUniqueId(), false);
        }
    }

    public boolean toggleFreeze(Player p)
    {
        if (isFrozen(p))
        {
            frozenPlayers.put(p.getUniqueId(), false);
            return false;
        }

        frozenPlayers.put(p.getUniqueId(), true);

        return true;
    }

    @SuppressWarnings("incomplete-switch")
    @EventHandler
    public void playerInteractEntity(PlayerInteractEntityEvent e)
    {
        if (!inStaffMode(e.getPlayer()))
        {
            return;
        }

        e.setCancelled(true);
        if (e.getHand().equals(EquipmentSlot.HAND))
        {
            if (e.getPlayer().getInventory().getItemInMainHand() != null)
            {
                Material type = e.getPlayer().getInventory().getItemInMainHand().getType();
                switch (type)
                {
                    case WATCH:
                        useClicksPerSecond(e);
                        break;
                    case LEASH:
                        useMount(e);
                        break;
                    case BLAZE_ROD:
                        useFreeze(e);
                        break;
                    case CHEST:
                        useOpenInventory(e);
                        break;
                }
            }
        }
    }

    @EventHandler
    public void playerInteract(PlayerInteractEvent e)
    {
        if (!inStaffMode(e.getPlayer()))
        {
            return;
        }

        e.setCancelled(true);
        if (e.getItem() != null)
        {
            Material type = e.getItem().getType();
            switch (type)
            {
                case COMPASS:
                    useLaunch(e);
                    break;
                case EYE_OF_ENDER:
                    useTeleport(e);
                    break;
                case CLAY:
                    useInvisibility(e);
                    break;
            }
        }
    }

    public void useLaunch(PlayerInteractEvent e)
    {
        Player staffPlayer = e.getPlayer();
        FileHandler c = DesireCore.getConfigHandler();
        double launchVelocity = c.getDouble("staff.launch_velocity");
        System.out.println(launchVelocity);
        Vector cameraVector = staffPlayer.getLocation().getDirection().normalize();

        staffPlayer.setVelocity(cameraVector.multiply(launchVelocity));
    }

    public void useTeleport(PlayerInteractEvent e)
    {
        Player staffPlayer = e.getPlayer();
        Collection<? extends Player> playerCollection = DesireCore.getInstance().getServer().getOnlinePlayers();
        List<Player> players = new ArrayList<>(playerCollection);

        // remove staff player to avoid teleporting to yourself
        for (int i = 0; i < players.size(); i++)
        {
            if (players.get(i) == staffPlayer)
            {
                players.remove(i);
                break;
            }
        }

        if (players.size() == 0)
        {
            return;
        }

        Player randomPlayer = players.get(new Random().nextInt(players.size()));
        staffPlayer.teleport(randomPlayer);
    }

    public void useInvisibility(PlayerInteractEvent e)
    {
        toggleInvisibility(e.getPlayer());
    }

    public void toggleInvisibility(Player player)
    {
        int index = hiddenPlayers.indexOf(player.getUniqueId());

        if (index == -1)
        {
            hidePlayer(player);
            hiddenPlayers.add(player.getUniqueId());
            LANG.sendString(player, "staff.set-invisible");
        }
        else
        {
            showPlayer(player);
            hiddenPlayers.remove(index);
            LANG.sendString(player, "staff.set-visible");
        }
    }

    public void useClicksPerSecond(PlayerInteractEntityEvent e)
    {
        if (e.getRightClicked() instanceof Player)
        {
            startCPSTestForPlayer(e.getPlayer(), (Player) e.getRightClicked());
        }
    }

    public void startCPSTestForPlayer(Player player, Player target)
    {
        UUID playerID = player.getUniqueId();

        if (cpsTests.containsKey(playerID))
        {
            return;
        }

        cpsTests.put(playerID, 0);
        new ClicksPerSecondThread(player, target, this).runTaskTimer(DesireCore.getInstance(), 0, 20);
        numCPSTests++;
    }

    public void useFreeze(PlayerInteractEntityEvent e)
    {
        if (e.getRightClicked() instanceof Player)
        {
            Player p = (Player) e.getRightClicked();
            toggleFreeze(p);
        }
    }

    public void useMount(PlayerInteractEntityEvent e)
    {
        mount(e.getPlayer(), (Player) e.getRightClicked());
    }

    public void mount(Player passenger, Player target)
    {
        if (!target.isDead())
        {
            target.addPassenger(passenger);
        }
    }

    private void useOpenInventory(PlayerInteractEntityEvent e)
    {
        if (e.getRightClicked() instanceof Player)
        {
            e.getPlayer().openInventory(((Player) e).getInventory());
        }
    }

    private void showPlayer(Player p)
    {
        Server server = DesireCore.getInstance().getServer();
        for (Player player : server.getOnlinePlayers())
        {
            player.showPlayer(p);
        }
    }

    private void hidePlayer(Player p)
    {
        Server server = DesireCore.getInstance().getServer();
        for (Player player : server.getOnlinePlayers())
        {
            player.hidePlayer(p);
        }
    }

    public void openReportsGUI(Player p)
    {
        Inventory inv = Bukkit.createInventory(null, 54, LANG.renderMessage("report.inventory.title"));

        List<Report> reports = ReportHandler.getInstance().getAllReports(true);

        boolean next = true;
        int itemsPerPage = 45;
        int startingIndex = (pages.getOrDefault(p.getUniqueId(), 1) - 1) * itemsPerPage;
        int endingIndex = startingIndex + itemsPerPage;

        if (endingIndex > reports.size())
        {
            endingIndex = reports.size();
            next = false;
        }

        for (Report report : reports.subList(startingIndex, endingIndex))
        {
            Session reported = SessionHandler.getSession(report.getReported());
            Session issuer = SessionHandler.getSession(report.getIssuer());
            String reason = report.getReason();
            long time = report.getIssued();

            ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta skull = (SkullMeta) item.getItemMeta();
            skull.setDisplayName(reported.getName());

            List<String> lore = new ArrayList<>();

            for (String loreString : LANG.getStringList("report.inventory.item.lore"))
            {
                lore.add(LANG.renderString(loreString, "{issuer}", issuer.getName(), "{date}",
                        DateUtils.formatDateDiff(time), "{reason}", reason));
            }

            skull.setLore(lore);
            skull.setOwner(reported.getName());
            item.setItemMeta(skull);
            inv.addItem(item);
        }

        if (next)
        {
            ItemStack nextItem = new ItemStack(Material.matchMaterial(LANG.getString("report.inventory.next.item")));
            ItemMeta nextMeta = nextItem.getItemMeta();

            nextMeta.setDisplayName(LANG.renderString("report.inventory.next.name"));

            List<String> lore = new ArrayList<>();

            for (String loreString : LANG.getStringList("report.inventory.next.lore"))
            {
                lore.add(LANG.renderString(loreString));
            }

            nextItem.setItemMeta(nextMeta);
            inv.setItem(53, nextItem);
        }

        if (pages.getOrDefault(p.getUniqueId(), 1) != 1)
        {
            ItemStack nextItem = new ItemStack(Material.matchMaterial(LANG.getString("report.inventory.back.item")));
            ItemMeta nextMeta = nextItem.getItemMeta();

            nextMeta.setDisplayName(LANG.renderString("report.inventory.back.name"));

            List<String> lore = new ArrayList<>();

            for (String loreString : LANG.getStringList("report.inventory.back.lore"))
            {
                lore.add(LANG.renderString(loreString));
            }

            nextItem.setItemMeta(nextMeta);
            inv.setItem(45, nextItem);
        }

        p.openInventory(inv);
    }

    public void addPage(Player p)
    {
        UUID uuid = p.getUniqueId();

        pages.put(uuid, pages.getOrDefault(uuid, 1) + 1);
        StaffHandler.getInstance().openReportsGUI(p);
    }

    public void minusPage(Player p)
    {
        UUID uuid = p.getUniqueId();
        pages.put(uuid, pages.getOrDefault(uuid, 1) - 1);
        StaffHandler.getInstance().openReportsGUI(p);
    }

}