package components;

import entities.Entity;

import java.util.HashMap;

public class Component {

    public String type;
    public String Name;
    public double ID;
    public boolean isDeleted = false;

    public Entity parent;

    public Component() {

    }

    public void onCreate() {}

    public void onRender() {}

    public HashMap<String, Object> onSave() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("type", this.type);
        hashMap.put("ID", this.ID);
        return hashMap;
    }

    public void onLoad(HashMap<String, Object> hashMap) {
        this.ID = (int) hashMap.get("ID");
        this.type = (String) hashMap.get("type");
    }



}
