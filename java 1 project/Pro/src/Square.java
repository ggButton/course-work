
import java.awt.*;
import java.util.ArrayList;

public class Square {
    private Edge x1;
    private Edge x2;
    private Edge x3;
    private Edge x4;
    public ArrayList<Edge> pack=new ArrayList<>();

    private Color color=Color.RED;
    private String name=" ";

    public Square(Edge x1, Edge x2, Edge x3, Edge x4){
        this.x1=x1;
        this.x2=x2;
        this.x3=x3;
        this.x4=x4;
        //point.setLocation(x1.getX(), x3.getY());
        pack.add(x1);
        pack.add(x2);
        pack.add(x3);
        pack.add(x4);
    }

    public ArrayList<Edge> getPack(){
        return pack;
    }

    public boolean isSet(){
        if(!x1.isFree() & !x2.isFree() & !x3.isFree() & !x4.isFree())
            return true;
        else return false;
    }

    public Edge get(int t) {
        if(t == 0)return x1;
        else if(t == 1)return x2;
        else if(t == 2)return x3;
        else return x4;
    }

    public void setColor(Color color){
        this.color=color;
   }

    public int getSquareX(){
        return x1.getX()+x1.getWidth()/2;
    }

    public int getSquareY(){
        return x3.getY()+x3.getHeight()/2;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void paintName(){
        StdDraw.setPenColor(color);
        StdDraw.setFont(new Font("Times New Roman",Font.BOLD,28));
        StdDraw.text(getSquareX(), getSquareY(), name);
    }
}
