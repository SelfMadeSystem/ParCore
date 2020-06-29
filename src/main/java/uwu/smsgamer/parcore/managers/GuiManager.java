package uwu.smsgamer.parcore.managers;

import de.themoep.inventorygui.*;
import de.themoep.inventorygui.GuiPageElement.PageAction;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import uwu.smsgamer.parcore.ParCore;
import uwu.smsgamer.parcore.utils.*;

import java.util.*;

/**
 * Unused. Will be used in final release to have an
 * inventory based GUI to select maps and do stuff.
 */
public class GuiManager {
    public static HashMap<String, GuiManager> guis = new HashMap<>();
    static ParCore pl;
    Player player;
    List<Elm> list;

    private GuiManager(Player player) {
        this.player = player;
        guis.put(player.getName(), this);
    }

    public static GuiManager getManager(Player player) {
        if (guis.containsKey(player.getName())) return guis.get(player.getName());
        return new GuiManager(player);
    }

    public static void setup(ParCore plugin) {
        pl = plugin;
    }

    public void openMapsGui(String playerOnly) {
        String[] guiSetup = {
          "         ", //Alpha, reverse aLpha, Rating
          "ggggggggg",
          "ggggggggg",
          "fp     nl" //first prev next last
        };
        if (playerOnly == null || playerOnly.isEmpty()) playerOnly = "";
        else if (playerOnly.equalsIgnoreCase("mine")) playerOnly = player.getName();
        InventoryGui gui = new InventoryGui(pl, player, playerOnly.isEmpty() ? "Maps" :
          (playerOnly.equalsIgnoreCase(player.getName()) ? "Your" : playerOnly) + " maps", guiSetup);
        gui.setFiller(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5));
        GuiElementGroup group = new GuiElementGroup('g');

        // First page
        gui.addElement(new GuiPageElement('f', new ItemStack(Material.ARROW), PageAction.FIRST, "Go to first page (current: %page%)"));

        // Previous page
        gui.addElement(new GuiPageElement('p', new ItemStack(Material.SIGN), PageAction.PREVIOUS, "Go to previous page (%prevpage%)"));

        // Next page
        gui.addElement(new GuiPageElement('n', new ItemStack(Material.SIGN), PageAction.NEXT, "Go to next page (%nextpage%)"));

        // Last page
        gui.addElement(new GuiPageElement('l', new ItemStack(Material.ARROW), PageAction.LAST, "Go to last page (%pages%)"));

        for (Elm elm : list = getFirstRow(false, playerOnly)) {
            List<String> text = new ArrayList<>(Arrays.asList(ChatColor.RESET + elm.name, (elm.description == null || elm.description.isEmpty()) ? "No description... Find out what the map offers by yourself!" : elm.description,
              ChatColor.RESET + "By: " + elm.player, ChatColor.RESET + "Rating: " + elm.rating));
            if (elm.player.equalsIgnoreCase(player.getName())) {
                if (elm.mf.isVerified()) text.add(ChatColor.GREEN + "Verified.");
                else text.add(ChatColor.RED + "Not verified.");
                if (elm.mf.isPublished()) text.add(ChatColor.GREEN + "Published.");
                else text.add(ChatColor.RED + "Not published.");
            }
            group.addElement(new StaticGuiElement('e', elm.stack, click -> {
                try {
                    Elm clm = list.get(click.getSlot() - 9);
                    if (clm.player.equalsIgnoreCase(player.getName())) {
                        WorldManager.newMapArena(player, player.getName(), clm.name, false);
                        Chat.send(player, "&aPlaying map: &7" + clm.name + "&a, made by you.");
                        Chat.send(player, "If you complete the map, it will be verified.");
                    } else {
                        WorldManager.newMapArena(player, player.getName(), clm.name);
                        Chat.send(player, "&aPlaying map: &7" + clm.name + "&a, made by: &7" + clm.player);
                    }
                    click.getGui().close();
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    Chat.send(player, "&4An error occurred. Please contact administration or look at console for more information.");
                    return true;
                }
            }, text.toArray(new String[0])));
        }
        gui.addElement(group);
        gui.show(player);
    }

    /*public static String[][] getFirstRowPlayerOnly(String player) {
        FileManager.mapFiles.values().stream().filter(val -> val.getPlayer().equalsIgnoreCase(player));
    }*/

    public List<Elm> getFirstRow(boolean ranking, String playersOnly) {
        List<MapFile> files = new ArrayList<>(FileManager.mapFiles.values());
        List<Elm> pElms = new ArrayList<>(); //Player elms.
        int pp = 0; //player published elms index uwu
        List<Elm> elms = new ArrayList<>(); //All the other ones.
        boolean only = !playersOnly.isEmpty();
        for (MapFile f : files) {
            if (only) {
                if (f.isPublished() && f.getPlayer().equalsIgnoreCase(playersOnly))
                    elms.add(new Elm(f.getPlayer(), f.getName(), f.getDescription(), Elm.normal, MathUtils.getRating(f.getLikes()), f));
            } else {
                Elm elm = new Elm(f.getPlayer(), f.getName(), f.getDescription(), MathUtils.getRating(f.getLikes()), f);
                if (f.getPlayer().equalsIgnoreCase(player.getName())) {
                    if (f.isPublished()) {
                        elm.stack = Elm.published;
                        pElms.add(0, elm);
                        pp++;
                    } else if (f.isVerified()) {
                        elm.stack = Elm.verified;
                        pElms.add(pp, elm);
                    } else {
                        elm.stack = Elm.nver;
                        pElms.add(elm);
                    }
                } else {
                    if (!f.isPublished()) continue;
                    elm.stack = Elm.normal;
                    elms.add(elm);
                }
            }
        }
        if (ranking)
            elms.sort(Comparator.comparingDouble(f -> f.rating));
        else
            elms.sort(Comparator.comparing(f -> f.name));
        pElms.addAll(elms);
        return pElms;
    }

    static class Elm {
        public static ItemStack nver = new ItemStack(Material.STAINED_CLAY, 1, (short) 3); //not verified
        public static ItemStack verified = new ItemStack(Material.STAINED_CLAY, 1, (short) 13);
        public static ItemStack published = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
        public static ItemStack normal = new ItemStack(Material.SLIME_BALL, 1);
        public String player;
        public String name;
        public String description;
        public ItemStack stack;
        public double rating;
        public final MapFile mf;

        public Elm(String player, String name, String description, double rating, MapFile mf) {
            this.player = player;
            this.name = name;
            this.description = description;
            this.rating = rating;
            this.mf = mf;
        }

        public Elm(String player, String name, String description, ItemStack stack, double rating, MapFile mf) {
            this.player = player;
            this.name = name;
            this.description = description;
            this.stack = stack;
            this.rating = rating;
            this.mf = mf;
        }
    }
}
