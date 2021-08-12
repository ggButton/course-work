
import java.awt.*;

public class Square_1 {
    private Edge x1;
    private Edge x2;
    private Edge x3;
    private Edge x4;
    private Font font1 = new Font("Ink Free",Font.BOLD,25);
    private boolean square = false;
    private boolean free = true;
    private Color color;

    public void setFree(boolean free) {
        this.free = free;
    }

    public void setSquare(boolean square) {
        this.square = square;
    }

    public void setColor(Color color) {
        if(color == Color.RED){
            this.color = Color.RED;
        }
        else{
            this.color = Color.BLUE;
        }
    }
    public int getSquareX(){
        return x1.getX()+x1.getWidth()/2;
    }

    public int getSquareY(){
        return x3.getY()+x3.getHeight()/2;
    }

    public boolean getFree(){
        return this.free;
    }
    public boolean getSquare(){
        return this.square;
    }

    public Color getColor() {
        return color;
    }

    public Square_1(Edge x1, Edge x2, Edge x3, Edge x4){
        this.x1 = x1;
        this.x2 = x2;
        this.x3 = x3;
        this.x4 = x4;
    }

    public void Text(Color color){
        if(!x1.isFree()&&!x2.isFree()&&!x3.isFree()&&!x4.isFree()){
            StdDraw.setPenColor(color);
            StdDraw.setFont(font1);
            if(color == Color.RED){
                StdDraw.text(getSquareX(),getSquareY(),"SpiderMan");
            }else{
                StdDraw.text(getSquareX(),getSquareY(),"Batman");
            }
        }
    }
    public void isSquare(){
        if(!x1.isFree()&&!x2.isFree()&&!x3.isFree()&&!x4.isFree()){
            setFree(false);
            setSquare(true);
        }
    }
    public static void points(int f ,int p,int x, int y,int width,int height){
        if(f > 0){
            StdDraw.setPenColor(Color.RED);
            StdDraw.filledRectangle(x-150,y,width/4,height);
            if(f > 1){
                StdDraw.setPenColor(Color.RED);
                StdDraw.filledRectangle(x-100,y,width/2,height);
                if(f > 2){
                    StdDraw.setPenColor(Color.RED);
                    StdDraw.filledRectangle(x-50,y,3*width/4,height);
                    if(f > 3){
                        StdDraw.setPenColor(Color.RED);
                        StdDraw.filledRectangle(x,y,width,height);
                    }
                }
            }
        }
        if(p > 0){
            StdDraw.setPenColor(Color.BLUE);
            StdDraw.filledRectangle(x-150,y-10,width/4,height);
            if(p > 1){
                StdDraw.setPenColor(Color.BLUE);
                StdDraw.filledRectangle(x-100,y-10,width/2,height);
                if(p > 2){
                    StdDraw.setPenColor(Color.BLUE);
                    StdDraw.filledRectangle(x-50,y-10,3*width/4,height);
                    if(p > 3){
                        StdDraw.setPenColor(Color.BLUE);
                        StdDraw.filledRectangle(x,y,width,height);
                    }
                }
            }
        }
    }

}
