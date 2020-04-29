package DataClasses;

public class Coordinates {
    private int x; //Максимальное значение поля: 857
    private Double y; //Поле не может быть null

    public Coordinates(int x, Double y){
        this.x = x;
        this.y = y;
    }

    public int getX(){return x;}
    public Double getY(){return y;}

    @Override
    public String toString(){
        return "Coordinates{x="+x+", y="+y+"}";
    }
}