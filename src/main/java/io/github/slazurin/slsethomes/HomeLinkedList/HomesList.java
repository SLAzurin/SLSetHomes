package io.github.slazurin.slsethomes.HomeLinkedList;

public class HomesList {
    private int count;
    private HomeNode list;

    public HomesList() {
        this.count = 0;
        this.list = null;
    }

    public HomesList(HomeNode list, int count) {
        this.count = count;
        this.list = list;
    }
    
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public HomeNode getList() {
        return list;
    }

    public void setList(HomeNode list) {
        this.list = list;
    }
}
