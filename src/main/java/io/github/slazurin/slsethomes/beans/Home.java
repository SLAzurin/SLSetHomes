package io.github.slazurin.slsethomes.beans;

public class Home {
    private String name;
    private String desc;
    private String world;

    public Home() {
    }

    public Home(String name, String desc, String world) {
        this.name = name;
        this.desc = desc;
        this.world = world;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }
}
