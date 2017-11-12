package com.desiremc.core.commands.timings;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.spigotmc.CustomTimingsHandler;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.google.gson.Gson;
import com.desiremc.google.gson.JsonObject;

public class TimingsReportCommand extends ValidCommand
{

    public static long timingStart;

    public TimingsReportCommand()
    {
        super("report", "Generate and upload a report of the timings.", Rank.DEVELOPER, new String[0]);
        Bukkit.getScheduler().runTask(DesireCore.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                timingStart = org.bukkit.command.defaults.TimingsCommand.timingStart;
            }
        });
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        long sampleTime = System.nanoTime() - timingStart;
        File timingFolder = new File("timings");
        timingFolder.mkdirs();
        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        PrintStream fileTimings = null;
        try
        {
            fileTimings = new PrintStream(bout);

            CustomTimingsHandler.printTimings(fileTimings);
            fileTimings.println("Sample time " + sampleTime + " (" + sampleTime / 1E9 + "s)");

            fileTimings.println("<spigotConfig>");
            fileTimings.println(Bukkit.spigot().getConfig().saveToString());
            fileTimings.println("</spigotConfig>");

            new PasteThread(sender, bout).start();
            return;
        }
        finally
        {
            if (fileTimings != null)
            {
                fileTimings.close();
            }
        }
    }

    private static class PasteThread extends Thread
    {

        private final CommandSender sender;
        private final ByteArrayOutputStream bout;

        public PasteThread(CommandSender sender, ByteArrayOutputStream bout)
        {
            super("Timings paste thread");
            this.sender = sender;
            this.bout = bout;
        }

        @Override
        public synchronized void start()
        {
            if (sender instanceof RemoteConsoleCommandSender)
            {
                run();
            }
            else
            {
                super.start();
            }
        }

        @Override
        public void run()
        {
            try
            {
                HttpURLConnection con = (HttpURLConnection) new URL("https://timings.spigotmc.org/paste").openConnection();
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                con.setInstanceFollowRedirects(false);

                OutputStream out = con.getOutputStream();
                out.write(bout.toByteArray());
                out.close();

                JsonObject location = new Gson().fromJson(new java.io.InputStreamReader(con.getInputStream()), JsonObject.class);
                con.getInputStream().close();

                String pasteID = location.get("key").getAsString();
                sender.sendMessage(ChatColor.GREEN + "Timings results can be viewed at https://www.spigotmc.org/go/timings?url=" + pasteID);
            }
            catch (IOException ex)
            {
                sender.sendMessage(ChatColor.RED + "Error pasting timings, check your console for more information");
                Bukkit.getServer().getLogger().log(Level.WARNING, "Could not paste timings", ex);
            }
        }
    }

}
