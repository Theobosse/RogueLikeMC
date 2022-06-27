package fr.theobosse.roguelike.game;

import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;

public class User {

    private final static List<User> users = new ArrayList<>();

    private final OfflinePlayer player;
    private Role role;

    public User(OfflinePlayer player) {
        this.player = player;
        users.add(this);
    }

    public static User getUser(OfflinePlayer player) {
        for (User u : getUsers())
            if (u.getPlayer() == player)
                return u;
        return null;
    }

    public static boolean isUser(OfflinePlayer player) {
        return getUser(player) != null;
    }

    public OfflinePlayer getPlayer() {
        return player;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public static List<User> getUsers() {
        return users;
    }
}
