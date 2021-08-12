
import java.awt.*;
import java.util.ArrayList;

public class fourpvp_1 {
    //    private List<Edge> edges= new ArrayList<>();
    //    private List<Dot> dots = new ArrayList<>();
    private ArrayList<Edge> edges = new ArrayList<>();
    private ArrayList<Dot> dots = new ArrayList<>();
    private ArrayList<Square_1> squares = new ArrayList<>();
    /*ArrayList <Integer> step1 = new ArrayList<>();
    ArrayList <Integer> step2 = new ArrayList<>();
    int s=0;

    private Button withdraw=new Button(150, 700, 130,40,"Withdraw");
    private boolean MousePress=false;
    private boolean previousMouse=false;*/

    private Color currentColor = Color.RED;
    private Font font = new Font("Tw Cen MT",Font.BOLD,50);
    private Font font1= new Font("Times New Roman",Font.BOLD,25);
    int d = 0;
    int f = 0;
    int p = 0;

    public fourpvp_1(int canvasWidth, int canvasHeight) {
        StdDraw.setCanvasSize(canvasWidth, canvasHeight);
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 800);
        StdDraw.setYscale(0, 800);
        initialize();
    }

    public void initialize() {
        edges.add(new Edge(145, 95, 170, 10));
        edges.add(new Edge(145, 265, 170, 10));
        edges.add(new Edge(145, 435, 170, 10));
        edges.add(new Edge(145, 605, 170, 10));
        edges.add(new Edge(315, 95, 170, 10));
        edges.add(new Edge(315, 265, 170, 10));
        edges.add(new Edge(315, 435, 170, 10));
        edges.add(new Edge(315, 605, 170, 10));
        edges.add(new Edge(485, 95, 170, 10));
        edges.add(new Edge(485, 265, 170, 10));
        edges.add(new Edge(485, 435, 170, 10));
        edges.add(new Edge(485, 605, 170, 10));

        edges.add(new Edge(140, 100, 10, 170));
        edges.add(new Edge(310, 100, 10, 170));
        edges.add(new Edge(480, 100, 10, 170));
        edges.add(new Edge(650, 100, 10, 170));
        edges.add(new Edge(140, 270, 10, 170));
        edges.add(new Edge(310, 270, 10, 170));
        edges.add(new Edge(480, 270, 10, 170));
        edges.add(new Edge(650, 270, 10, 170));
        edges.add(new Edge(140, 440, 10, 170));
        edges.add(new Edge(310, 440, 10, 170));
        edges.add(new Edge(480, 440, 10, 170));
        edges.add(new Edge(650, 440, 10, 170));


        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                dots.add(new Dot(145 + 170 * i, 100 + 170 * j, 15));
            }
        }

        squares.add(new Square_1(edges.get(0), edges.get(1), edges.get(12), edges.get(13)));
        squares.add(new Square_1(edges.get(4), edges.get(5), edges.get(13), edges.get(14)));
        squares.add(new Square_1(edges.get(8), edges.get(9), edges.get(14), edges.get(15)));
        squares.add(new Square_1(edges.get(1), edges.get(2), edges.get(16), edges.get(17)));
        squares.add(new Square_1(edges.get(5), edges.get(6), edges.get(17), edges.get(18)));
        squares.add(new Square_1(edges.get(9), edges.get(10), edges.get(18), edges.get(19)));
        squares.add(new Square_1(edges.get(2), edges.get(3), edges.get(20), edges.get(21)));
        squares.add(new Square_1(edges.get(6), edges.get(7), edges.get(21), edges.get(22)));
        squares.add(new Square_1(edges.get(10), edges.get(11), edges.get(22), edges.get(23)));
    }

    public void update() {
        Point mousePoint = new Point((int) StdDraw.mouseX(), (int) StdDraw.mouseY());
        boolean isMousePressed = StdDraw.isMousePressed();
        boolean foundEdge = false;
        for (Edge edge : edges) {
            if (edge.isFree()) {
                if (!foundEdge && edge.getBounds().contains(mousePoint)) {
                    edge.setColor(currentColor);
                    edge.setVisible(true);

                    if (isMousePressed) {
                        currentColor = currentColor == Color.RED ? Color.BLUE : Color.RED;
                        edge.setFree(false);
                        for(Square_1 square:squares){
                            if(square.getFree()){
                                square.isSquare();
                                if(!square.getFree() && d == 0 ){
                                    currentColor = currentColor == Color.RED ? Color.BLUE : Color.RED;
                                    square.setColor(currentColor);
                                    if(currentColor.equals(Color.RED)){
                                        f++;
                                    }else {
                                        p++;
                                    }
                                    d++;
                                }else{
                                    if(!square.getFree()){
                                        square.setColor(currentColor);
                                        if(currentColor.equals(Color.RED)){
                                            f++;
                                        }else {
                                            p++;
                                        }
                                    }
                                }
                            }
                        }
                        d = 0;
                    }
                    foundEdge = true; // Avoid multiple selections.
                } else {
                    edge.setVisible(false);
                }
            }
        }
    }

    public void paint(String name) {
        StdDraw.clear();
        // Paint edges first, so dots will be on the top layer.
        StdDraw.picture(400,400, name, 800,800);
        StdDraw.setPenColor(Color.magenta);
        StdDraw.setFont(font1);
        StdDraw.text(200, 700, "Spider Man:"+f);
        StdDraw.text(450, 700, "Batman:"+p);

        for(Square_1 square:squares){
            square.Text(square.getColor());
        }

        for (Edge edge: edges) {
            edge.paint();
        }
        for (Dot dot: dots) {
            dot.paint(Color.BLACK);
        }
        //Square_1.points(f,p,200,700,200,5);

        StdDraw.setPenColor(currentColor);
        StdDraw.setFont(new Font("Times New Roman",Font.BOLD,30));
        if(currentColor.equals(Color.RED))
            StdDraw.text(150, 50, "Spider Man's turn.");
        else{
            StdDraw.text(150, 50, "Batman's turn.");
        }

//      edges.forEach(Edge::paint);
//      dots.forEach(Dot::paint);
        if(f + p == 9){
            StdDraw.clear();
            StdDraw.setFont(font);
            if(f > p){
                StdDraw.picture(400,400,"3.jpg",800,800);
                StdDraw.setPenColor(StdDraw.CYAN);
                StdDraw.text(400,400,"Winner is Spider Man");
                StdDraw.text(400,350,"Score is :"+f);
                StdDraw.text(400,300,"Loser's score is :"+p);
            }
            else {
                if(f < p){
                    StdDraw.picture(400,400,"8.jpg",800,800);
                    StdDraw.setPenColor(StdDraw.CYAN);
                    StdDraw.text(400,400,"Winner is Batman");
                    StdDraw.text(400,350,"Score is :"+p);
                    StdDraw.text(400,300,"Loser's score is :"+f);
                }else {
                    StdDraw.picture(400,400,"hah.jpg",800,800);
                    StdDraw.setPenColor(StdDraw.CYAN);
                    StdDraw.text(400,400,"Draw!");
                    StdDraw.text(400,350,"Both score is :"+f);
                }
            }
        }
        StdDraw.show();
    }

    public static void main(String[] args) {
        fourpvp_1 game = new fourpvp_1(800, 800);

        while (true) {
            game.update();
            game.paint("8.jpg");
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
