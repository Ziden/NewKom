package devsBrsMarotos.mechanic.list;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.FieldAccessException;
import devsBrsMarotos.mechanic.Mecanica;
import io.netty.buffer.Unpooled;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.server.v1_8_R3.ContainerMerchant;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.IMerchant;
import net.minecraft.server.v1_8_R3.Item;
import net.minecraft.server.v1_8_R3.MerchantRecipe;
import net.minecraft.server.v1_8_R3.MerchantRecipeList;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class AttributeHider
        extends Mecanica {

    private Field cC;

    public void onEnable() {

        try {
            this.cC = EntityPlayer.class.getDeclaredField("containerCounter");
            this.cC.setAccessible(true);
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        Set<PacketType> packets = new HashSet();
        packets.add(PacketType.Play.Server.SET_SLOT);
        packets.add(PacketType.Play.Server.WINDOW_ITEMS);
        packets.add(PacketType.Play.Server.CUSTOM_PAYLOAD);
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin, packets) {
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                PacketType type = packet.getType();
                if (type == PacketType.Play.Server.WINDOW_ITEMS) {
                    try {
                        org.bukkit.inventory.ItemStack[] read = (org.bukkit.inventory.ItemStack[]) packet.getItemArrayModifier().read(0);
                        for (int i = 0; i < read.length; i++) {
                            read[i] = AttributeHider.removeAttributes(read[i]);
                        }
                        packet.getItemArrayModifier().write(0, read);
                    } catch (FieldAccessException e) {
                        Logger.getLogger(AttributeHider.class.getName()).log(Level.SEVERE, null, e);
                    }
                } else if (type == PacketType.Play.Server.CUSTOM_PAYLOAD) {
                    if (!((String) packet.getStrings().read(0)).equalsIgnoreCase("MC|TrList")) {
                        return;
                    }
                    try {
                        EntityPlayer p = ((CraftPlayer) event.getPlayer()).getHandle();
                        ContainerMerchant cM = (ContainerMerchant) p.activeContainer;
                        Field fieldMerchant = cM.getClass().getDeclaredField("merchant");
                        fieldMerchant.setAccessible(true);
                        IMerchant imerchant = (IMerchant) fieldMerchant.get(cM);
                        MerchantRecipeList merchantrecipelist = imerchant.getOffers(p);
                        MerchantRecipeList nlist = new MerchantRecipeList();
                        for (Object orecipe : merchantrecipelist) {
                            MerchantRecipe recipe = (MerchantRecipe) orecipe;
                            int uses = recipe.k().getInt("uses");
                            int maxUses = recipe.k().getInt("maxUses");
                            MerchantRecipe nrecipe = new MerchantRecipe(AttributeHider.removeAttributes(recipe.getBuyItem1()), AttributeHider.removeAttributes(recipe.getBuyItem2()), AttributeHider.removeAttributes(recipe.getBuyItem3()));
                            nrecipe.a(maxUses - 7);
                            for (int i = 0; i < uses; i++) {
                                nrecipe.f();
                            }
                            nlist.add(nrecipe);
                        }
                        PacketDataSerializer packetdataserializer = new PacketDataSerializer(Unpooled.buffer());
                        packetdataserializer.writeInt(AttributeHider.this.cC.getInt(p));
                        nlist.a(packetdataserializer);
                        byte[] b = packetdataserializer.array();
                        packet.getByteArrays().write(0, b);
                        packet.getIntegers().write(0, Integer.valueOf(b.length));
                    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | FieldAccessException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        packet.getItemModifier().write(0, AttributeHider.removeAttributes((org.bukkit.inventory.ItemStack) packet.getItemModifier().read(0)));
                    } catch (FieldAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static org.bukkit.inventory.ItemStack removeAttributes(org.bukkit.inventory.ItemStack i) {
        if (i == null) {
            return i;
        }
        if (i.getType() == Material.BOOK_AND_QUILL) {
            return i;
        }
        org.bukkit.inventory.ItemStack item = i.clone();
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag;
        if (!nmsStack.hasTag()) {
            tag = new NBTTagCompound();
            nmsStack.setTag(tag);
        } else {
            tag = nmsStack.getTag();
        }
        NBTTagList am = new NBTTagList();
        tag.set("AttributeModifiers", am);
        nmsStack.setTag(tag);
        return CraftItemStack.asCraftMirror(nmsStack);
    }

    public static net.minecraft.server.v1_8_R3.ItemStack removeAttributes(net.minecraft.server.v1_8_R3.ItemStack i) {
        if (i == null) {
            return i;
        }
        if (Item.getId(i.getItem()) == 386) {
            return i;
        }
        net.minecraft.server.v1_8_R3.ItemStack item = i.cloneItemStack();
        NBTTagCompound tag;
        if (!item.hasTag()) {
            tag = new NBTTagCompound();
            item.setTag(tag);
        } else {
            tag = item.getTag();
        }
        NBTTagList am = new NBTTagList();
        tag.set("AttributeModifiers", am);
        item.setTag(tag);
        return item;
    }

    @Override
    public String getName() {
        return "Attribute Hider";
    }
}
