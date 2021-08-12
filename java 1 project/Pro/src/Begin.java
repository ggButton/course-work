
import java.awt.*;
import java.util.ArrayList;

public class Begin {
    private ArrayList<Button> button = new ArrayList<>();
    private boolean previousMouse=false;
    private boolean MousePress=false;
    private Font font = new Font("DFPCraftYu-W5", Font.BOLD, 40);

    public Begin(int canvasWidth, int canvasHeight, Button...button) {
        StdDraw.setCanvasSize(canvasWidth, canvasHeight);
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 800);
        StdDraw.setYscale(0, 800);
        for(Button b:button){
            this.button.add(b);
        }
    }

    public void update(){
        Point mousePoint = new Point((int) StdDraw.mouseX(), (int) StdDraw.mouseY());
        previousMouse=MousePress;
        MousePress = StdDraw.isMousePressed();

        for (Button buttons : button) {
            if (buttons.getBounds().contains(mousePoint)) {
                buttons.setVisible(true);
                if (isMousePressed()) {
                    buttons.setChoose(true);
                }

            } else
                buttons.setVisible(false);
        }
    }

    public void paint(String sentence, String name){
        StdDraw.clear();
        StdDraw.picture(400,400,name,800,800);
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.setFont(font);
        StdDraw.setPenColor(110,55,18);
        StdDraw.text(400, 700, "Welcome to Dots and Boxes !");
        StdDraw.setFont(new Font("Times New Roman", Font.BOLD, 32));
        StdDraw.text(400, 600, sentence);
        for(Button b: button){
            b.paint();
        }

        StdDraw.show();
    }

    public boolean isMousePressed(){
        if(previousMouse & !MousePress){
            return true;
        }else
            return false;
    }

    public static void main(String []args) {
        Ball ball = new Ball(400, 400, 140, Color.blue, Color.CYAN, 0.03);

        Button start = new Button(400, 400, 120, 50, "Start!");
        Button MM = new Button(400, 500, 250, 50, "Machine vs Machine");
        Button HH = new Button(400, 310, 250, 50, "Human vs Human");
        Button HM = new Button(400, 120, 250, 50, "Human vs Machine");
        Button spiderman = new Button(400, 450, 150, 50, "Spider Man");
        Button batman = new Button(400, 250, 150, 50, "Batman");
        Button firstY = new Button(400, 450, 120, 50, "Yes");
        Button firstN = new Button(400, 250, 120, 50, "No");
        Button three = new Button(400, 500, 120, 50, "3×3");
        Button four = new Button(400, 310, 120, 50, "4×4");
        Button five = new Button(400, 120, 120, 50, "5×5");

        Begin Start = new Begin(800, 800, start);
        Begin mode = new Begin(800, 800, MM, HH, HM);
        Begin player = new Begin(800, 800, spiderman, batman);
        Begin isFirst = new Begin(800, 800, firstY, firstN);
        Begin size = new Begin(800, 800, three, four, five);

        threepvp HM3 = new threepvp(800, 800, true);
        fourpvp HM4 = new fourpvp(800, 800, true);
        fivepvp HM5 = new fivepvp(800, 800, true);

        threepvp_2 MM3 = new threepvp_2(800, 800);
        fourpvp_2 MM4 = new fourpvp_2(800, 800);
        fivepvp_2 MM5 = new fivepvp_2(800, 800);

        threepvp_1 HH3 = new threepvp_1(800, 800);
        fourpvp_1 HH4 = new fourpvp_1(800, 800);
        fivepvp_1 HH5 = new fivepvp_1(800, 800);

        int i=0,j=0,k=0;
        int ii=0, jj=0, kk=0;



        while (true) {
            if(HM3.home.isChoose() | HM4.home.isChoose() | HM5.home.isChoose() | MM3.home.isChoose()){
                HH.setChoose(false);
                HM.setChoose(false);
                MM.setChoose(false);
                spiderman.setChoose(false);
                batman.setChoose(false);
                three.setChoose(false);
                four.setChoose(false);
                five.setChoose(false);
                firstN.setChoose(false);
                firstY.setChoose(false);
                HM3.home.setChoose(false);
                HM3.reset.setChoose(true);
                HM4.home.setChoose(false);
                HM4.reset.setChoose(true);
                HM5.home.setChoose(false);
                HM5.reset.setChoose(true);
                MM3.home.setChoose(false);
                MM3.Reset();

            }

            if (!start.isChoose()) {
                Start.update();
                ball.update();

                StdDraw.clear();
                StdDraw.picture(400,400,"27.jpg",800,800);
                StdDraw.setPenColor(110,55,18);
                StdDraw.setFont(new Font("DFPCraftYu-W5", Font.BOLD, 40));
                StdDraw.text(400, 700, "Welcome to Dots and Boxes !");
                start.paint();
                ball.paint();
                StdDraw.show();

            } else if (!MM.isChoose() & !HH.isChoose() & !HM.isChoose()) {
                mode.update();
                mode.paint(" ","28.jpg");
                StdDraw.show();

            } else {
                if (HM.isChoose()) {
                    if (!spiderman.isChoose() & !batman.isChoose()) {
                        player.update();
                        player.paint("Which player do you want to be ?", "25.jpg");

                    } else if (!three.isChoose() & !four.isChoose() & !five.isChoose()) {

                        size.update();
                        size.paint("Which size do you want to play ?", "25.jpg");

                    } else if (!firstY.isChoose() & !firstN.isChoose()) {
                        isFirst.update();
                        isFirst.paint("Are you first ?","25.jpg");

                    } else {
                        if (three.isChoose()) {
                            HM3.setName1(spiderman.isChoose() ? "Spider Man" : "Batman");
                            HM3.setName2(batman.isChoose() ? "Spider Man" : "Batman");
                            if(i==0){
                                HM3.setYourTurn(firstY.isChoose());
                                i++;
                            }
                            if(HM3.reset.isChoose()){
                                HM3.Reset();
                                HM3.setYourTurn(firstY.isChoose());
                                HM3.reset.setChoose(false);
                                HM3.reset.setVisible(false);

                            }
                            HM3.update();
                            HM3.paint("3.jpg");
                        } else if (four.isChoose()) {
                            HM4.setName1(spiderman.isChoose() ? "Spider Man" : "Batman");
                            HM4.setName2(batman.isChoose() ? "Spider Man" : "Batman");
                            if(j==0){
                                HM4.setYourTurn(firstY.isChoose());
                                j++;
                            }
                            if(HM4.reset.isChoose()){
                                HM4.Reset();
                                HM4.setYourTurn(firstY.isChoose());
                                HM4.reset.setChoose(false);
                                HM4.reset.setVisible(false);

                            }
                            HM4.update();
                            HM4.paint("10.jpg");
                        } else {
                            HM5.setName1(spiderman.isChoose() ? "Spider Man" : "Batman");
                            HM5.setName2(batman.isChoose() ? "Spider Man" : "Batman");
                            if(k==0){
                                HM5.setYourTurn(firstY.isChoose());
                                k++;
                            }
                            if(HM5.reset.isChoose()){
                                HM5.Reset();
                                HM5.setYourTurn(firstY.isChoose());
                                HM5.reset.setChoose(false);
                                HM5.reset.setVisible(false);

                            }
                            HM5.update();
                            HM5.paint("2.jpg");
                        }
                    }
                } else if (MM.isChoose()) {
                    if (!three.isChoose() & !four.isChoose() & !five.isChoose()) {
                        size.update();
                        size.paint("Which size do you want to play ?", "24.jpg");

                    } else {
                        if (three.isChoose()) {
                            MM3.update();
                            MM3.paint("5.jpg");
                        } else if (four.isChoose()) {
                            StdDraw.pause(500);
                            MM4.update();
                            MM4.paint("16.jpeg");
                        }
                        if (five.isChoose()) {
                            StdDraw.pause(500);
                            MM5.update();
                            MM5.paint("1.jpg");
                        }
                    }
                } else {
                    if (!three.isChoose() & !four.isChoose() & !five.isChoose()) {
                        size.update();
                        size.paint("Which size do you want to play ?", "23.png");

                    } else {
                        if (three.isChoose()) {
                            HH3.update();
                            HH3.paint("11.jpg");
                        } else if(four.isChoose()){
                            HH4.update();
                            HH4.paint("12.jpg");
                        } else
                        if(five.isChoose()){
                            HH5.update();
                            HH5.paint("13.jpg");
                        }
                    }
                }
            }
            try {
                Thread.sleep(2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
