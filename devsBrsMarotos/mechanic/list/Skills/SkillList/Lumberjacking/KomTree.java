package devsBrsMarotos.mechanic.list.Skills.SkillList.Lumberjacking;

import de.inventivegames.hologram.Hologram;
import de.inventivegames.hologram.HologramAPI;
import devsBrsMarotos.util.TimeUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.material.MaterialData;

/**
 *
 * @author Carlos
 */
public class KomTree {

    private TreeType type;

    private List<Block> logs = new ArrayList();
    private List<Block> leaves = new ArrayList();

    private Block chao = null;
    private long quebrada;

    private HashMap<Block, MaterialData> dlogs = new HashMap<>();
    private HashMap<Block, MaterialData> dleaves = new HashMap<>();

    private Block inicial;

    private Hologram holo;

    public KomTree(TreeType type, Block inicial) {

        this.type = type;
        this.inicial = inicial;
        chao = inicial;
        quebrada = System.currentTimeMillis();

        procura();

        for (Block b : logs) {
            dlogs.put(b, new MaterialData(b.getType(), b.getData()));
        }
        for (Block b : leaves) {
            dleaves.put(b, new MaterialData(b.getType(), b.getData()));
        }
    }

    public void create() {
        holo = HologramAPI.createHologram(chao.getRelative(BlockFace.UP).getLocation().add(0.5, 1.25, 0.5), type.name);
        holo.spawn();
        int secods = (int) (((System.currentTimeMillis() + (type.cd * 50)) - quebrada));

        String t = TimeUtils.getTimeToString(secods / 1000);
        holo.addLineBelow("§a" + t);
    }

    public void procura() {
        HashSet<Block> treeBlocks = new HashSet();
        HashSet<Block> blocksToSearch = new HashSet();
        HashSet<Block> searched = new HashSet();
        Block startingPoint = inicial;
        blocksToSearch.add(startingPoint);
        searched.add(inicial);
        List<Block> possiveis = new ArrayList();
        for (int i = 0; i < 1500; i++) {
            if (blocksToSearch.isEmpty()) {
                break;
            }
            Block block = (Block) blocksToSearch.iterator().next();
            blocksToSearch.remove(block);

            searched.add(block);
            if (getDiff(block, startingPoint) > 4) {
                continue;
            }
            if ((block.getType() == Material.LOG) || (block.getType() == Material.LOG_2) || (block.getType() == Material.LEAVES) || (block.getType() == Material.LEAVES_2)) {

                treeBlocks.add(block);
                if (!searched.contains(block.getRelative(BlockFace.UP))) {
                    blocksToSearch.add(block.getRelative(BlockFace.UP));
                }
                if (!searched.contains(block.getRelative(BlockFace.DOWN))) {
                    blocksToSearch.add(block.getRelative(BlockFace.DOWN));
                }
                if (!searched.contains(block.getRelative(BlockFace.WEST))) {
                    blocksToSearch.add(block.getRelative(BlockFace.WEST));
                }
                if (!searched.contains(block.getRelative(BlockFace.EAST))) {
                    blocksToSearch.add(block.getRelative(BlockFace.EAST));
                }
                if (!searched.contains(block.getRelative(BlockFace.NORTH))) {
                    blocksToSearch.add(block.getRelative(BlockFace.NORTH));
                }
                if (!searched.contains(block.getRelative(BlockFace.SOUTH))) {
                    blocksToSearch.add(block.getRelative(BlockFace.SOUTH));
                }
            } else if (!block.getType().isTransparent()) {
                if (block.getRelative(BlockFace.UP).getType() == Material.LOG || block.getRelative(BlockFace.UP).getType() == Material.LOG_2) {
                    possiveis.add(block);
                }

            }
        }

        for (Block block : treeBlocks) {
            if (block.getType() == Material.LOG || block.getType() == Material.LOG_2) {

                if (block.getType() == type.wmat && Lumberjacking.getRealData(block) == type.wdata) {
                    logs.add(block);

                }
            }
            if (block.getType() == Material.LEAVES || block.getType() == Material.LEAVES_2) {
                if (block.getType() == type.leaves && type.wdata == Lumberjacking.getRealData(block)) {
                    leaves.add(block);
                }
            }
        }
        int lasty = 255;
        Block cha = null;
        for (Block b : possiveis) {
            if (b.getY() < lasty) {
                lasty = b.getY();
                cha = b;
            }
        }
        if (cha != null) {
            chao = cha;
        }

    }

    public Block getSapling() {
        return getChao().getRelative(BlockFace.UP);
    }

    public Location getDropsLocation() {
        return getSapling().getLocation().add(0.5, 0, 0.5);
    }

    public TreeType getType() {
        return type;
    }

    private int getDiff(Block b, Block c) {
        int zb = b.getZ();
        int xb = b.getX();
        int xc = c.getX();
        int zc = c.getZ();

        int dif = 0;
        int difz = zb - zc;
        if (difz < 0) {
            difz *= -1;
        }
        int difx = xb - xc;
        if (difx < 0) {
            difx *= -1;
        }
        dif = difx + difz;
        return dif;
    }

    public long getQuebrada() {
        return quebrada;
    }

    public Block getInicial() {
        return inicial;
    }

    public Hologram getHolo() {
        return holo;
    }

    public List<Block> getLogs() {
        return logs;
    }

    public List<Block> getLeaves() {
        return leaves;
    }

    public HashMap<Block, MaterialData> getDefaultLogs() {
        return dlogs;
    }

    public HashMap<Block, MaterialData> getDefaultLeaves() {
        return dleaves;
    }

    public Block getChao() {
        return chao;
    }

}
