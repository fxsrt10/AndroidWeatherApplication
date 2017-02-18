package model;

/**
 * Created by User on 2/17/2017.
 */

public class Weather {
    public Place place;
    public String iconData;
    public CurrentCondition currentCondition = new CurrentCondition();
    public Temperature temperature = new Temperature();
    public Snow snow = new Snow();
    public Wind wind = new Wind();
    public Clouds clouds = new Clouds();

}
