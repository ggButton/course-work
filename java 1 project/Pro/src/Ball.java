
import java.awt.*;

public class Ball{
    private double X;
    private double Y;
    //private double x;
    //private double y;
    private double v;
    private double t=0;
    private int radius;
    private Color c1;
    private Color c2;


    public Ball(int x, int y, int radius, Color color1, Color color2, double velocity){
        X=x;
        Y=y;
        this.radius=radius;
        c1=color1;
        c2=color2;
        v=velocity;
    }

    public void paint(){
        //StdDraw.picture(X+radius * Math.sin(t), Y+radius * Math.cos(t),"spider logo.png",200,100);
        //StdDraw.picture(X-radius * Math.sin(t), Y-radius * Math.cos(t),"bat logo.png",100,100);
        //for (double t = 0.0; true; t += 0.02) {
            //double x = this.x + radius * Math.sin(t);
            //double y = this.y + radius * Math.cos(t);
            //StdDraw.clear();

            StdDraw.setPenColor(c1);
            StdDraw.filledCircle(X+radius * Math.sin(t), Y+radius * Math.cos(t), 13);
            StdDraw.setPenColor(c2);
            StdDraw.filledCircle(X-radius * Math.sin(t), Y-radius * Math.cos(t), 13);

            //StdDraw.show();
            //StdDraw.pause(20);

        //}
    }

    public void update(){
        /*for(double t=0; true; t+=0.02){
            x = X + radius * Math.sin(t);
            y = Y + radius * Math.cos(t);
        }*/
        t=t+v;
    }


    public static void main(String []args){
        StdDraw.setScale(0, 400);
        StdDraw.enableDoubleBuffering();
        Ball ball=new Ball(200,200, 120, Color.BLACK, Color.BLUE, 0.005);
        while(true){
            ball.update();
            ball.paint();
        }

        /*try {
            Thread.sleep(2);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }


}

