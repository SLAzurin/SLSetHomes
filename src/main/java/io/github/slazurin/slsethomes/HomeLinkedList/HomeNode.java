package io.github.slazurin.slsethomes.HomeLinkedList;

public class HomeNode {
    public String homeName;
    public String desc;
    public HomeNode next;

    public HomeNode() {
    }
    
    public HomeNode(String homeName, String desc) {
        this.homeName = homeName;
        this.desc = desc;
        next = null;
    }
}
