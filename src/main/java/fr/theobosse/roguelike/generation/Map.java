package fr.theobosse.roguelike.generation;

import org.bukkit.Location;
import org.bukkit.Material;

public class Map {

    private final int width;
    private final int length;
    private final int tileSize;
    private final int corridorWidth;
    private final int height;
    private final int[][] map;

    public Map(int width, int length, int tileSize, int corridorWidth, int height) {
        this.width = width;
        this.height = height;
        this.length = length;
        this.tileSize = tileSize;
        this.corridorWidth = corridorWidth;
        this.map = new int[length][width];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < length; y++) {
                map[y][x] = 0;
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getLength() {
        return length;
    }

    public int getHeight() {
        return height;
    }

    public int getTileSize() {
        return tileSize;
    }

    public int getCorridorWidth() {
        return corridorWidth;
    }

    public int get(int x, int y) {
        return map[y][x];
    }

    public void setTile(int x, int y, int value) {
        map[y][x] = value;
    }

    public int[][] getMap() {
        return map;
    }

    public void generate(Location location) {
        Location loc = location.clone();
        // Etape 1: Générer une grille de salles
        for (int xr = 0; xr < width; xr++) {
            for (int zr = 0; zr < length; zr++) {
                for (int x = 0; x < tileSize; x++) {
                    for (int z = 0; z < tileSize; z++) {
                        int xt = xr * (tileSize + 2) + x + 1;
                        int zt = zr * (tileSize + 2) + z + 1;
                        loc.clone().add(xt, 0, zt).getBlock().setType(Material.STONE);
                    }
                }
            }
        }

        // Etape 2: Générarer les murs pour ces salles
        for (int x = 0; x < width; x++) {
            for (int z = 0; z < length; z++) {
                int xt = x * (tileSize + 2);
                int zt = z * (tileSize + 2);
                for (int y = 0; y < 5; y++) {
                    for (int xr = 0; xr < tileSize + 2; xr++) {
                        loc.clone().add(xt + xr, y, zt).getBlock().setType(Material.OAK_PLANKS);
                        loc.clone().add(xt + xr, y, zt + tileSize + 1).getBlock().setType(Material.OAK_PLANKS);
                    }

                    for (int zr = 1; zr < tileSize + 1; zr++) {
                        loc.clone().add(xt, y, zt + zr).getBlock().setType(Material.OAK_PLANKS);
                        loc.clone().add(xt + tileSize + 1, y, zt + zr).getBlock().setType(Material.OAK_PLANKS);
                    }
                }
            }
        }


        // Etape 3: Générer les couloirs entre ces salles
    }
}