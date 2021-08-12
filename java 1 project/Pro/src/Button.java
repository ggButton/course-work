
import java.awt.*;
import java.util.ArrayList;

public class Button {
    private int x;
    private int y;
    private int length;
    private int height;
    public boolean isVisible=false;
    public boolean choose=false;
    private Color color;
    public static Color[] colours={Color.LIGHT_GRAY, Color.PINK, Color.red};
    private Font font= new Font("DFPSumo-W12",Font.BOLD,25);
    private String text;

    public Button(int x, int y, int length, int height, String text){
        this.x=x;
        this.y=y;
        this.length=length;
        this.height=height;
        color=colours[0];
        this.text=text;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setCurrentColor(Color currentColor) {
        this.color = currentColor;
    }

    public Color getCurrentColor(){
        return color;
    }

    public Rectangle getBounds(){
        return (new Rectangle(x-length/2, y-height/2, length, height));
    }

    public boolean isChoose(){
        return choose;
    }

    public void setChoose(boolean TF){
        choose=TF;
    }

    public boolean isVisible(){
        return isVisible;
    }

    public void setVisible(boolean TF){
        isVisible=TF;
    }

    public void paint(){
        if(isVisible){
            StdDraw.setPenColor(colours[0]);
            StdDraw.filledRectangle(x,y,length/2+5,height/2+5);
            StdDraw.setPenColor(colours[1]);
            StdDraw.filledRectangle(x,y,length/2,height/2);

        }
        StdDraw.setPenColor(Color.black);
        StdDraw.setFont(font);
        StdDraw.text(x,y,text);
    }


}
