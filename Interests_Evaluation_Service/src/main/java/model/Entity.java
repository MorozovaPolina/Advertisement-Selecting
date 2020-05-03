package model;

import java.util.ArrayList;

public class Entity {
    String name;
    ArrayList<Entity> parents;
    ArrayList<Entity> children;
    ArrayList<Entity> associated;

    public Entity(String name){
        this.name = name;
        this.parents = new ArrayList<>();
        this.associated = new ArrayList<>();
        this.children = new ArrayList<>();
    }

    public void addParent(Entity parent){
        parents.add(parent);
        parent.addChild(this);
    }

    public ArrayList<Entity> getParents(){
        return parents;
    }

    public void addChild(Entity child){
        children.add(child);
    }

    public ArrayList<Entity> getChildren() {
        return children;
    }

    public void addAssociated(Entity associated){
        if(!this.associated.contains(associated))
            this.associated.add(associated);
    }

    public ArrayList<Entity> getAssociated() {
        return associated;
    }


    public void printEntity(){
        System.out.println("Name: "+name);
        System.out.print("Parents: ");
        for(Entity parent :parents)
            System.out.print(parent.name+" ");
        System.out.println();
        System.out.print("Children: ");
        for (Entity child:children)
            System.out.print(child.name+" ");
        System.out.println();
        System.out.print("Assosiated with: ");
        for(Entity assosiated: associated)
            System.out.println(assosiated.name+" ");
        System.out.println();
        System.out.println("______________________");
    }
}
