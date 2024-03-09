package com.aki.mcutils.APICore.Utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class GuiDebugHelper {
    private static final CopyOnWriteArrayList<Consumer<List<String>>> MinecraftDebugReplaceConsumers = new CopyOnWriteArrayList<>();
    private static final CopyOnWriteArrayList<List<String>> StringList = new CopyOnWriteArrayList<>();

    public static CopyOnWriteArrayList<Consumer<List<java.lang.String>>> getMinecraftDebugReplaceConsumers() {
        return MinecraftDebugReplaceConsumers;
    }

    public static CopyOnWriteArrayList<List<java.lang.String>> getStringList() {
        return StringList;
    }

    /*public static int addLastBeforeMinecraftStringConsumer(Consumer<List<String>> consumer) {
        MinecraftDebugReplaceConsumers.addLast(consumer);
        return MinecraftDebugReplaceConsumers.size() - 1;
    }*/

    //Return ReplaceIndex
    public static int ReplaceMinecraftDebugConsumers(Consumer<List<String>> oldConsumer, Consumer<List<String>> NewConsumer) {
        synchronized (MinecraftDebugReplaceConsumers) {
            int index = MinecraftDebugReplaceConsumers.indexOf(oldConsumer);
            if (index >= 0 && MinecraftDebugReplaceConsumers.size() > 0) {
                MinecraftDebugReplaceConsumers.remove(index);
                MinecraftDebugReplaceConsumers.add(index, NewConsumer);
                return index;
            } else {
                MinecraftDebugReplaceConsumers.add(NewConsumer);
                return MinecraftDebugReplaceConsumers.size() - 1;
            }
        }
    }

    /*public static int addLastDebugStringList(List<String> stringList) {
        StringList.addLast(stringList);
        return StringList.size() - 1;
    }*/

    //Return ReplaceIndex
    public static int ReplaceDebugStringList(List<String> oldDebugStringList, List<String> NewDebugStringList) {
        synchronized (StringList) {
            int index = StringList.indexOf(oldDebugStringList);//うまくいかない
            if (index >= 0 && StringList.size() > 0) {
                StringList.remove(index);
                StringList.add(index, new ArrayList<>(NewDebugStringList));
                return index;
            } else {
                StringList.add(new ArrayList<>(NewDebugStringList));
                return StringList.size() - 1;
            }
        }
    }
}
