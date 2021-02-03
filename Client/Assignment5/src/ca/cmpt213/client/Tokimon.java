package ca.cmpt213.client;

//tokimon object
public class Tokimon {
    private long id;
    private String name;
    private double weight;
    private double height;
    private String ability;
    private double strength;
    private String colour;

    public Tokimon(){
        id = -1;
        name = "";
        weight = -1;
        height = -1;
        ability = "";
        strength = -1;
        colour = "";

    }
    public Tokimon(String name, double weight, double height, String ability, double strength, String colour){
        id = -1;
        this.name = name;
        this.weight = weight;
        this.height = height;
        this.ability = ability;
        this.strength = strength;
        this.colour = colour;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }

    public double getHeight() {
        return height;
    }

    public String getAbility() {
        return ability;
    }

    public double getStrength() {
        return strength;
    }

    public String getColour() {
        return colour;
    }

    public void setId(long id){
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }

    public void setStrength(double strength) {
        this.strength = strength;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }
}
