package uwu.smsgamer.parcore.managers;

import de.themoep.inventorygui.*;
import de.themoep.inventorygui.GuiPageElement.PageAction;
import net.wesjd.anvilgui.AnvilGUI;
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
// TODO: 2020-06-28 Update DOCS and fix error kthx <:
public class GuiManager {
    public static HashMap<String, GuiManager> guis = new HashMap<>();
    static ParCore pl;
    final Player player;
    MapFile mapFile;
    String name;
    String plr;
    boolean deleted;

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

    public static List<Elm> getFirstRow(boolean ranking, String playersOnly, boolean justPlayer, String owner) {
        List<Elm> pElms = new ArrayList<>(); //Player elms.
        int pp = 0; //player published elms index uwu
        List<Elm> elms = new ArrayList<>(); //All the other ones.
        boolean only = !playersOnly.isEmpty();
        for (MapFile f : FileManager.mapFiles.values()) {
            if (only) {
                if (justPlayer && f.getPlayer().equalsIgnoreCase(playersOnly)) {
                    Elm elm = new Elm(f.getPlayer(), f.getName(), f.getDescription(), MathUtils.getRating(f.getLikes()), f);
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
                } else if (!justPlayer && (f.isPublished() && f.getPlayer().equalsIgnoreCase(playersOnly)))
                    elms.add(new Elm(f.getPlayer(), f.getName(), f.getDescription(), Elm.normal, MathUtils.getRating(f.getLikes()), f));
            } else {
                Elm elm = new Elm(f.getPlayer(), f.getName(), f.getDescription(), MathUtils.getRating(f.getLikes()), f);
                try {
                    if (f.getPlayer().equalsIgnoreCase(owner)) {
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
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    System.err.println(f.toString());
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

    public void openMapSettings(String mapName) {
        if (mapName == null || mapName.isEmpty()) {
            AnvilGUI.Builder builder = new AnvilGUI.Builder();
            builder.plugin(pl);
            builder.text("Map Name Here");
            Chat.send(player, "Select map name.");
            builder.onComplete((plr, txt) -> {
                if (txt == null || txt.isEmpty() || !txt.matches("[a-zA-Z0-9]+")) {
                    return AnvilGUI.Response.text("Invalid name.");
                }
                openMapSettings0(txt);
                return AnvilGUI.Response.close();
            });
            builder.open(player);
        } else
            openMapSettings0(mapName);
    }

    public void openMapsGui(String playerOnly) {
        String[] guiSetup = {
          "    e    ", //make map or smth idk
          "ggggggggg",
          "ggggggggg",
          "fp     nl" //first prev next last
        };
        if (playerOnly == null || playerOnly.isEmpty()) playerOnly = "";
        else if (playerOnly.equalsIgnoreCase("mine")) playerOnly = player.getName();
        InventoryGui gui = new InventoryGui(pl, player, /*playerOnly.isEmpty() ? */"Left click to play."/* :
          (playerOnly.equalsIgnoreCase(player.getName()) ? "Your" : playerOnly) + " maps"*/, guiSetup);
        gui.setFiller(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5));
        GuiElementGroup group = new GuiElementGroup('g');

        //Edit
        gui.addElement(new StaticGuiElement('e', new ItemStack(Material.WORKBENCH), click -> {
            Chat.send(player, "&aSetting up creator map...");
            WorldManager.newBuildArena(player);
            Chat.send(player, "&aCreator map set up!");
            return true;
        }, "Make a new map."));
        // First page
        gui.addElement(new GuiPageElement('f', new ItemStack(Material.ARROW), PageAction.FIRST, "Go to first page (current: %page%)"));

        // Previous page
        gui.addElement(new GuiPageElement('p', new ItemStack(Material.SIGN), PageAction.PREVIOUS, "Go to previous page (%prevpage%)"));

        // Next page
        gui.addElement(new GuiPageElement('n', new ItemStack(Material.SIGN), PageAction.NEXT, "Go to next page (%nextpage%)"));

        // Last page
        gui.addElement(new GuiPageElement('l', new ItemStack(Material.ARROW), PageAction.LAST, "Go to last page (%pages%)"));

        for (Elm elm : getFirstRow(false, playerOnly, false, player.getName())) {
            List<String> text = new ArrayList<>(Arrays.asList(ChatColor.RESET + elm.name, (elm.description == null || elm.description.isEmpty()) ? "No description... Find out what the map offers by yourself!" : elm.description,
              ChatColor.RESET + "By: " + elm.player, ChatColor.RESET + "Rating: " + elm.rating));
            if (elm.player.equalsIgnoreCase(player.getName())) {
                if (elm.mf.isVerified()) text.add(ChatColor.GREEN + "Verified.");
                else text.add(ChatColor.RED + "Not verified.");
                if (elm.mf.isPublished()) text.add(ChatColor.GREEN + "Published.");
                else text.add(ChatColor.RED + "Not published.");
                text.add(ChatColor.RESET + "Right click to edit map.");
                // TODO: 2020-06-28 Middle click to publish or test'n'publish.
            }
            group.addElement(new StaticGuiElement('e', elm.stack, click -> {
                try {
                    if (elm.player.equalsIgnoreCase(player.getName())) {
                        if (click.getType().isRightClick()) {
                            WorldManager.newBuildArena(player, elm.name);
                            Chat.send(player, "&aYou are now editing: &6" + elm.name);
                        } else {
                            WorldManager.newMapArena(player, elm.player, elm.name, false);
                            Chat.send(player, "&aPlaying map: &6" + elm.name + "&a, made by you.");
                            Chat.send(player, "If you complete the map, it will be verified.");
                        }
                    } else {
                        WorldManager.newMapArena(player, elm.player, elm.name);
                        Chat.send(player, "&aPlaying map: &6" + elm.name + "&a, made by: &7" + elm.player);
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

    private void openMapSettings0(String mapName) {
        deleted = false;
        mapFile = FileManager.getMapFile(player.getName(), mapName);
        name = mapFile.getName();
        plr = mapFile.getPlayer();
        String[] guiSetup = {
          "n m r o d", //Name Material Remove mOde Description
        };
        InventoryGui gui = new InventoryGui(pl, player, mapName + " Settings", guiSetup);
        gui.setCloseAction(close -> true); // TODO: 2020-06-29 do shit
        gui.setFiller(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5));
        gui.addElement(new StaticGuiElement('n', new ItemStack(Material.NAME_TAG),
          click -> {
              AnvilGUI.Builder builder = new AnvilGUI.Builder();
              builder.plugin(pl);
              builder.onClose((plr) -> gui.show(player));
              builder.onComplete((plr, txt) -> {
                  if (txt == null || txt.isEmpty() || !txt.matches("[a-zA-Z0-9]+")) {
                      return AnvilGUI.Response.text("Invalid name.");
                  }
                  mapFile.setName(txt);
                  gui.setTitle(ChatColor.GREEN + "Name set to: " + txt);
                  return AnvilGUI.Response.close();
              });
              builder.open(player);
              return true;
          }, ChatColor.RESET + "Name", ChatColor.RESET + "Click this to change the name."));
        gui.addElement(new StaticGuiElement('m', new ItemStack(Material.STONE),
          click -> {
              AnvilGUI.Builder builder = new AnvilGUI.Builder();
              builder.plugin(pl);
              builder.onClose((plr) -> gui.show(player));
              builder.onComplete((plr, txt) -> {
                  if (txt == null || txt.isEmpty() || !txt.matches("[a-zA-Z0-9]+")) {
                      return AnvilGUI.Response.text("Invalid name.");
                  }
                  mapFile.setWallMaterial(Material.matchMaterial(txt));
                  gui.setTitle(ChatColor.GREEN + "Wall Material set to: " + txt);
                  return AnvilGUI.Response.close();
              });
              builder.open(player);
              return true;
          }, ChatColor.RESET + "Wall Material", ChatColor.RESET + "Click this to set the name of the wall material."));
        gui.addElement(new StaticGuiElement('r', new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14),
          click -> {
              AnvilGUI.Builder builder = new AnvilGUI.Builder();
              Chat.send(player, "Type \"Yes\" to confirm deletion.");
              builder.text("No");
              builder.plugin(pl);
              builder.preventClose();
              builder.onComplete((plr, txt) -> {
                  if (txt.equals("Yes")) {
                      SchemUtils.deleteSchematic(player.getName(), txt);
                      Chat.send(player, "&aDeleted map: &6" + mapName.split(":")[1]);
                      deleted = true;
                  } else {
                      Chat.send(player, "&aDidn't deleted map.");
                  }
                  return AnvilGUI.Response.close();
              });
              builder.open(player);
              return true;
          }, ChatColor.RED + "Remove", ChatColor.RESET + "Click this to remove this map."));
        gui.addElement(new StaticGuiElement('o', new ItemStack(Material.POTION),
          click -> {
              return true;
          }, ChatColor.RESET + "Mode", ChatColor.RESET + "Click this to change the map mode.",
          ChatColor.RESET + "Modes:", ChatColor.RESET + "Normal - No modifications",
          ChatColor.RESET + "Jump - Has jump II to jump 2 blocks.",
          ChatColor.RESET + "Block - Has 6 placeable blocks that", ChatColor.RESET + "get removed after 2 seconds and replenish on ",
          ChatColor.RESET + "checkpoint and pressure plate activation."));
        gui.addElement(new StaticGuiElement('d', new ItemStack(Material.NAME_TAG),
          click -> {
              AnvilGUI.Builder builder = new AnvilGUI.Builder();
              Chat.send(player, "Type the description here.");
              builder.plugin(pl);
              builder.onClose((plr) -> gui.show(player));
              builder.onComplete((plr, txt) -> {
                  if (txt == null || txt.isEmpty() || !txt.matches("[a-zA-Z0-9]+")) {
                      gui.setTitle(ChatColor.GREEN + "Description reset!");
                      return AnvilGUI.Response.close();
                  }
                  mapFile.setDescription(txt);
                  gui.setTitle(ChatColor.GREEN + "Description set to: " + txt);
                  return AnvilGUI.Response.close();
              });
              builder.open(player);
              return true;
          }, ChatColor.RESET + "Description", ChatColor.RESET + "Click this to change the description."));

        gui.setCloseAction(action -> {
            if (!deleted) {
                SchemUtils.deleteSchematic(plr, name);
                WorldManager.saveBuildArena(player, mapFile.getName(), mapFile.getDescription(),
                  mapFile.getWallMaterial(), mapFile.getMode());
                Chat.send(player, "&aSaved settings.");
            }
            return true;
        });

        gui.show(player);
    }

    public void openSelector(Act action, String... txt) {
        String[] guiSetup = {
          "ggggggggg",
        };
        InventoryGui gui = new InventoryGui(pl, player, "Select a map", guiSetup);
        gui.setFiller(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5));
        GuiElementGroup group = new GuiElementGroup('g');

        for (Elm elm : getFirstRow(false, player.getName(), true, player.getName())) {
            List<String> text = new ArrayList<>(Arrays.asList(ChatColor.RESET + elm.name, (elm.description == null ||
                elm.description.isEmpty()) ? "No description... Find out what the map offers by yourself!" : elm.description,
              ChatColor.RESET + "By: " + elm.player, ChatColor.RESET + "Rating: " + elm.rating));
            if (elm.player.equalsIgnoreCase(player.getName())) {
                if (elm.mf.isVerified()) text.add(ChatColor.GREEN + "Verified.");
                else text.add(ChatColor.RED + "Not verified.");
                if (elm.mf.isPublished()) text.add(ChatColor.GREEN + "Published.");
                else text.add(ChatColor.RED + "Not published.");
                text.addAll(Arrays.asList(txt));
                group.addElement(new StaticGuiElement('e', elm.stack, click -> action.onClick(click, gui, elm),
                  text.toArray(new String[0])));
            }
        }
        gui.addElement(group);
        gui.show(player);
    }

    public interface Act {
        boolean onClick(GuiElement.Click click, InventoryGui gui, Elm elm);
    }

    public static class Elm {
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
