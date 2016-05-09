package devsBrsMarotos.mechanic.list.Skills.SkillList.Lumberjacking;

import devsBrsMarotos.mechanic.list.Harvesting.HarvestCache;
import devsBrsMarotos.mechanic.list.Harvesting.HarvestConfig;
import devsBrsMarotos.mechanic.list.Harvesting.Harvestable;
import devsBrsMarotos.mechanic.list.Skills.SkillEnum;
import org.bukkit.Material;
import org.bukkit.TreeSpecies;

/**
 *
 * @author Carlos
 */
public enum TreeType {

    NORMAL(WoodType.NORMAL, 20 * 30, 0, 1),
    EUCALIPTUS(WoodType.EUCALIPTUS, 60 * 20, 10, 1.2),
    OAK(WoodType.OAK, 120 * 20, 20, 1.2),
    WILLOW(WoodType.WILLOW, 150 * 20, 30, 1.3),
    MAPPLE(WoodType.MAPPLE, 180 * 20, 50, 1.4),
    YEW(WoodType.YEW, 240 * 20, 70, 1.6);

    //WOOD
    Material wmat;
    byte wdata;

    Material leaves;

    String name;

    WoodType wood;
    //IN TICKS
    int cd;

    double minSkill;
    double expRatio;

    private TreeType(WoodType type, int cd, double minSkill, double expRatio) {
        this.wdata = type.data;
        this.wmat = type.m;
        this.cd = cd;
        this.minSkill = minSkill;
        this.expRatio = expRatio;
        this.wood = type;
        this.name = type.ch + type.name + " Tree";
        if (Material.LOG == type.m) {
            leaves = Material.LEAVES;
        } else {
            leaves = Material.LEAVES_2;
        }
    }

    public static void fix() {
        for (TreeType tr : values()) {
            Harvestable h = HarvestCache.getHarvestable(tr.wmat, tr.wdata);
            if (h == null) {
                HarvestConfig.add(new Harvestable(tr.wmat, tr.wdata, SkillEnum.Lumberjacking, tr.minSkill, tr.cd / 20, tr.expRatio));
            } else {
                tr.minSkill = h.minSkill;
                tr.expRatio = h.expRatio;

                tr.cd = (h.cooldown) * 20;
            }
        }
    }

}
