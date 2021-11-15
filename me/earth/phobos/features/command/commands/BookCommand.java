/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.Unpooled
 *  net.minecraft.init.Items
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTBase
 *  net.minecraft.nbt.NBTTagList
 *  net.minecraft.nbt.NBTTagString
 *  net.minecraft.network.Packet
 *  net.minecraft.network.PacketBuffer
 *  net.minecraft.network.play.client.CPacketCustomPayload
 */
package me.earth.phobos.features.command.commands;

import io.netty.buffer.Unpooled;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import me.earth.phobos.Oyvey;
import me.earth.phobos.features.command.Command;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;

public class BookCommand
extends Command {
    public BookCommand() {
        super("book", new String[0]);
    }

    @Override
    public void execute(String[] commands) {
        ItemStack heldItem = BookCommand.mc.field_71439_g.func_184614_ca();
        if (heldItem.func_77973_b() == Items.field_151099_bA) {
            int limit = 50;
            Random rand = new Random();
            IntStream characterGenerator = rand.ints(128, 1112063).map(i -> i < 55296 ? i : i + 2048);
            String joinedPages = characterGenerator.limit(10500L).mapToObj(i -> String.valueOf((char)i)).collect(Collectors.joining());
            NBTTagList pages = new NBTTagList();
            for (int page = 0; page < 50; ++page) {
                pages.func_74742_a((NBTBase)new NBTTagString(joinedPages.substring(page * 210, (page + 1) * 210)));
            }
            if (heldItem.func_77942_o()) {
                heldItem.func_77978_p().func_74782_a("pages", (NBTBase)pages);
            } else {
                heldItem.func_77983_a("pages", (NBTBase)pages);
            }
            StringBuilder stackName = new StringBuilder();
            for (int i2 = 0; i2 < 16; ++i2) {
                stackName.append("\u0014\f");
            }
            heldItem.func_77983_a("author", (NBTBase)new NBTTagString(BookCommand.mc.field_71439_g.func_70005_c_()));
            heldItem.func_77983_a("title", (NBTBase)new NBTTagString(stackName.toString()));
            PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
            buf.func_150788_a(heldItem);
            BookCommand.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketCustomPayload("MC|BSign", buf));
            BookCommand.sendMessage(Oyvey.commandManager.getPrefix() + "Book Hack Success!");
        } else {
            BookCommand.sendMessage(Oyvey.commandManager.getPrefix() + "b1g 3rr0r!");
        }
    }
}

