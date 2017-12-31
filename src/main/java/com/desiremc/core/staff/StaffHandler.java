package com.desiremc.core.staff;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.desiremc.core.DesireCore;
import com.desiremc.core.report.Report;
import com.desiremc.core.report.ReportHandler;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.tasks.FrozenTask;
import com.desiremc.core.thread.ClicksPerSecondThread;
import com.desiremc.core.utils.DateUtils;
import com.desiremc.core.utils.TargetBlock;

public class StaffHandler
{

    private static StaffHandler instance;
    private HashMap<UUID, ItemStack[]> staffInventories;
    private HashMap<UUID, ItemStack[]> deathInventories;
    private HashMap<UUID, ItemStack[]> deathArmor;
    private HashMap<UUID, Integer> cpsTests;
    private List<UUID> frozenPlayers;
    private List<UUID> hiddenPlayers;
    private List<UUID> staffChat;
    private int numCPSTests;

    private boolean chatDisabled = false;
    private boolean chatSlowed = false;

    public StaffHandler()
    {
        staffInventories = new HashMap<>();
        deathInventories = new HashMap<>();
        deathArmor = new HashMap<>();
        cpsTests = new HashMap<>();
        hiddenPlayers = new LinkedList<>();
        frozenPlayers = new ArrayList<>();
        staffChat = new ArrayList<>();
        numCPSTests = 0;
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
        return staffInventories.containsKey(p.getUniqueId());
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

    public boolean isCPSTested(Player p)
    {
        return cpsTests.containsKey(p.getUniqueId());
    }

    public void decreaseNumCPSTests()
    {
        numCPSTests--;
    }

    private void enableStaffMode(Player p)
    {
        staffInventories.put(p.getUniqueId(), p.getInventory().getContents());
        p.getInventory().clear();

        for (Gadget gadget : GadgetHandler.getInstance().gadgets.values())
        {
            if (gadget.getName().equalsIgnoreCase("vanish"))
            {
                p.getInventory().setItem(gadget.getSlot(), GadgetHandler.getInstance().buildGadget(gadget, true));
            }
            else
            {
                p.getInventory().setItem(gadget.getSlot(), GadgetHandler.getInstance().buildGadget(gadget, false));
            }
        }
        DesireCore.getLangHandler().sendRenderMessage(p, "staff.staff_on", true, false);
        p.setGameMode(GameMode.CREATIVE);
        p.setFoodLevel(20);
        hidePlayer(p);
        if (!hiddenPlayers.contains(p.getUniqueId()))
        {
            hiddenPlayers.add(p.getUniqueId());
        }
    }

    public boolean inStaffChat(UUID uuid)
    {
        return staffChat.contains(uuid);
    }

    public List<UUID> getAllInStaffChat()
    {
        return staffChat;
    }

    public void toggleStaffChat(UUID uuid)
    {
        if (inStaffChat(uuid))
        {
            staffChat.remove(uuid);
        }
        else
        {
            staffChat.add(uuid);
        }
    }

    public void disableStaffMode(Player p)
    {
        if (inStaffMode(p))
        {
            p.getInventory().clear();
            p.getInventory().setContents(staffInventories.get(p.getUniqueId()));
            staffInventories.remove(p.getUniqueId());
            p.setGameMode(GameMode.SURVIVAL);
            DesireCore.getLangHandler().sendRenderMessage(p, "staff.staff_off", true, false);
        }
    }

    public void toggleStaffMode(Player p)
    {
        if (inStaffMode(p))
        {
            disableStaffMode(p);
            return;
        }

        enableStaffMode(p);
    }

    public void toggleChat()
    {
        chatDisabled = !chatDisabled;
    }

    public boolean chatDisabled()
    {
        return chatDisabled;
    }

    public boolean isFrozen(Player p)
    {
        return frozenPlayers.contains(p.getUniqueId());
    }

    public void toggleFreeze(Player target, Session source)
    {

        Session targetSession = SessionHandler.getOnlineSession(target.getUniqueId());
        Session sourceSession = SessionHandler.getOnlineSession(source.getUniqueId());

        if (targetSession.getRank().getId() >= sourceSession.getRank().getId())
        {
            DesireCore.getLangHandler().sendRenderMessage(source, "sender_doesnt_outrank", true, false);
            return;
        }

        if (isFrozen(target))
        {
            frozenPlayers.remove(target.getUniqueId());

            DesireCore.getLangHandler().sendRenderMessage(targetSession, "staff.unfrozen", true, false, "{player}", source.getName());
            DesireCore.getLangHandler().sendRenderMessage(sourceSession, "staff.target-unfrozen", true, false, "{player}", target
                    .getName());
        }
        else
        {
            frozenPlayers.add(target.getUniqueId());
            DesireCore.getLangHandler().sendRenderMessage(sourceSession, "staff.target-frozen", true, false, "{player}", target.getName());
            new FrozenTask(target, source.getPlayer());
        }
    }

    public void useLaunch(PlayerInteractEvent e)
    {
        Player player = e.getPlayer();
        Session session = SessionHandler.getOnlineSession(player.getUniqueId());

        TargetBlock aiming = new TargetBlock(player, 1000, 0.2);
        Block block = aiming.getTargetBlock();

        if (block == null || block.getY() <= 1)
        {
            DesireCore.getLangHandler().sendRenderMessage(session, "staff.invalid-block", true, false);
        }
        else
        {
            boolean passed = false;

            while ((block = aiming.getNextBlock()) != null)
            {
                if (block.getY() <= 1)
                {
                    DesireCore.getLangHandler().sendRenderMessage(session, "staff.no-free-space", true, false);
                    return;
                }

                Location to = new Location(block.getWorld(), block.getX(), block.getY() + 1, block.getZ(), player
                        .getLocation().getYaw(), player.getLocation().getPitch());

                to.setX(to.getX() + .5D);
                to.setZ(to.getZ() + .5D);

                if (!block.getWorld().isChunkLoaded(to.getBlockX() >> 4, to.getBlockZ() >> 4))
                {
                    block.getWorld().loadChunk(to.getBlockX() >> 4, to.getBlockZ() >> 4);
                }

                player.teleport(to);

                passed = true;
                break;
            }

            if (!passed)
            {
                DesireCore.getLangHandler().sendRenderMessage(session, "staff.no-free-space", true, false);
            }
        }
    }

    public void useTeleport(PlayerInteractEvent e)
    {
        Player staffPlayer = e.getPlayer();
        Collection<? extends Player> playerCollection = DesireCore.getInstance().getServer().getOnlinePlayers();
        List<Player> players = new ArrayList<>(playerCollection);

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

        Location loc = randomPlayer.getLocation();

        loc.setYaw(staffPlayer.getLocation().getYaw());
        loc.setPitch(staffPlayer.getLocation().getPitch());

        staffPlayer.teleport(loc);
    }

    public void useInvisibility(PlayerInteractEvent e)
    {
        toggleInvisibility(e.getPlayer(), true);
    }

    public void toggleInvisibility(Player player, boolean item)
    {
        int index = hiddenPlayers.indexOf(player.getUniqueId());

        if (index == -1)
        {
            hidePlayer(player);
            hiddenPlayers.add(player.getUniqueId());
            DesireCore.getLangHandler().sendRenderMessage(player, "staff.set-invisible", true, false);

            if (item)
            {
                Gadget gadget = GadgetHandler.getInstance().getGadget("vanish");
                player.getInventory().setItem(gadget.getSlot(), GadgetHandler.getInstance().buildGadget(gadget,
                        true));
                player.updateInventory();
            }
        }
        else
        {
            showPlayer(player);
            hiddenPlayers.remove(index);
            DesireCore.getLangHandler().sendRenderMessage(player, "staff.set-visible", true, false);

            if (item)
            {
                Gadget gadget = GadgetHandler.getInstance().getGadget("vanish");
                player.getInventory().setItem(gadget.getSlot(), GadgetHandler.getInstance().buildGadget(gadget, false));
                player.updateInventory();
            }
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
        UUID playerID = target.getUniqueId();

        if (cpsTests.containsKey(playerID))
        {
            return;
        }

        cpsTests.put(playerID, 0);
        new ClicksPerSecondThread(player, target, this).runTaskTimer(DesireCore.getInstance(), 0, 20);
        numCPSTests++;
    }

    public void useFreeze(PlayerInteractEntityEvent event)
    {
        if (event.getRightClicked() instanceof Player)
        {
            Player p = (Player) event.getRightClicked();
            toggleFreeze(p, SessionHandler.getSession(event.getPlayer()));
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
            target.setPassenger(passenger);
        }
    }

    public void useOpenInventory(PlayerInteractEntityEvent e)
    {
        if (e.getRightClicked() instanceof Player)
        {
            Player target = (Player) e.getRightClicked();
            e.getPlayer().openInventory(target.getInventory());
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
        Session session = SessionHandler.getOnlineSession(p.getUniqueId());
        Server server = DesireCore.getInstance().getServer();
        for (Player player : server.getOnlinePlayers())
        {
            Session target = SessionHandler.getOnlineSession(player.getUniqueId());
            if (target.getRank().getId() < session.getRank().getId())
            {
                player.hidePlayer(p);
            }
        }
    }

    public void openReportsGUI(Player p, int page)
    {
        Inventory inv = Bukkit.createInventory(null, 54, DesireCore.getLangHandler().renderMessage("report.inventory.title", false, false));

        List<Report> reports = ReportHandler.getInstance().getAllReports(true);

        boolean next = true;
        int itemsPerPage = 45;
        int startingIndex = (page - 1) * itemsPerPage;
        int endingIndex = startingIndex + itemsPerPage;

        if (endingIndex > reports.size())
        {
            endingIndex = reports.size();
            next = false;
        }

        for (Report report : reports.subList(startingIndex, endingIndex))
        {
            Session reported = SessionHandler.getGeneralSession(report.getReported());
            Session issuer = SessionHandler.getGeneralSession(report.getIssuer());
            String reason = report.getReason();
            long time = report.getIssued();

            ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta skull = (SkullMeta) item.getItemMeta();
            skull.setDisplayName(reported.getName());

            List<String> lore = new ArrayList<>();

            for (String loreString : DesireCore.getLangHandler().getStringList("report.inventory.item.lore"))
            {
                lore.add(DesireCore.getLangHandler().renderString(loreString, "{issuer}", issuer.getName(), "{date}",
                        DateUtils.formatDateDiff(time), "{reason}", reason));
            }

            skull.setLore(lore);
            skull.setOwner(reported.getName());
            item.setItemMeta(skull);
            inv.addItem(item);
        }

        if (next)
        {
            ItemStack nextItem = new ItemStack(Material.matchMaterial(DesireCore.getLangHandler().getString("report.inventory.next.item")));
            ItemMeta nextMeta = nextItem.getItemMeta();

            nextMeta.setDisplayName(DesireCore.getLangHandler().renderString("report.inventory.next.name"));

            List<String> lore = new ArrayList<>();

            for (String loreString : DesireCore.getLangHandler().getStringList("report.inventory.next.lore"))
            {
                lore.add(DesireCore.getLangHandler().renderString(loreString));
            }

            nextMeta.setLore(lore);

            nextItem.setItemMeta(nextMeta);
            inv.setItem(53, nextItem);
        }

        if (page != 1)
        {
            ItemStack nextItem = new ItemStack(Material.matchMaterial(DesireCore.getLangHandler().getString("report.inventory.back.item")));
            ItemMeta nextMeta = nextItem.getItemMeta();

            nextMeta.setDisplayName(DesireCore.getLangHandler().renderString("report.inventory.back.name"));

            List<String> lore = new ArrayList<>();

            for (String loreString : DesireCore.getLangHandler().getStringList("report.inventory.back.lore"))
            {
                lore.add(DesireCore.getLangHandler().renderString(loreString));
            }

            nextMeta.setLore(lore);

            nextItem.setItemMeta(nextMeta);
            inv.setItem(45, nextItem);
        }

        p.openInventory(inv);
    }

    public void saveInventory(Player player)
    {
        if (player.getInventory().getContents() != null)
        {
            deathInventories.put(player.getUniqueId(), player.getInventory().getContents());
        }

        if (player.getInventory().getArmorContents() != null)
        {
            deathArmor.put(player.getUniqueId(), player.getInventory().getArmorContents());
        }
    }

    public void restoreInventory(Session player, Session target)
    {

        if (!deathInventories.containsKey(target.getUniqueId()) && !deathArmor.containsKey(target.getUniqueId()))
        {
            DesireCore.getLangHandler().sendRenderMessage(player, "staff.no-restore", true, false, "{player}", target.getName());
            return;
        }

        target.getPlayer().getInventory().setContents(deathInventories.get(target.getUniqueId()));
        target.getPlayer().getInventory().setArmorContents(deathArmor.get(target.getUniqueId()));
        deathInventories.remove(target.getUniqueId());
        deathArmor.remove(target.getUniqueId());
        target.getPlayer().updateInventory();

        DesireCore.getLangHandler().sendRenderMessage(player, "staff.restore", true, false, "{player}", target.getName());
        DesireCore.getLangHandler().sendRenderMessage(target, "staff.restore-target", true, false, "{player}", player.getName());
    }

    public boolean isChatSlowed()
    {
        return chatSlowed;
    }

    public void toggleChatSlowed()
    {
        chatSlowed = !chatSlowed;
    }
}