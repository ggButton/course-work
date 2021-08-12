
import java.awt.*;
import java.util.ArrayList;

public class fivepvp_1 {
    //    private List<Edge> edges= new ArrayList<>();
//    private List<Dot> dots = new ArrayList<>();
    private ArrayList<Edge> edges = new ArrayList<>();
    private ArrayList<Dot> dots = new ArrayList<>();
    private ArrayList<Square_1> squares = new ArrayList<>();
    private Color currentColor = Color.RED;
    private Font font = new Font("Tw Cen MT",Font.BOLD,50);
    private Font font1= new Font("Times New Roman",Font.BOLD,25);
    int d = 0;
    int f = 0;
    int p = 0;

    public fivepvp_1(int canvasWidth, int canvasHeight) {
        StdDraw.setCanvasSize(canvasWidth, canvasHeight);
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 800);
        StdDraw.setYscale(0, 800);
        initialize();
    }

    public void initialize() {

        edges.add(new Edge(148, 95, 126, 10));
        edges.add(new Edge(148, 221, 126, 10));
        edges.add(new Edge(148, 347, 126, 10));
        edges.add(new Edge(148, 473, 126, 10));
        edges.add(new Edge(148, 599, 126, 10));

        edges.add(new Edge(274, 95, 126, 10));
        edges.add(new Edge(274, 221, 126, 10));
        edges.add(new Edge(274, 347, 126, 10));
        edges.add(new Edge(274, 473, 126, 10));
        edges.add(new Edge(274, 599, 126, 10));

        edges.add(new Edge(400, 95, 126, 10));
        edges.add(new Edge(400, 221, 126, 10));
        edges.add(new Edge(400, 347, 126, 10));
        edges.add(new Edge(400, 473, 126, 10));
        edges.add(new Edge(400, 599, 126, 10));

        edges.add(new Edge(526, 95, 126, 10));
        edges.add(new Edge(526, 221, 126, 10));
        edges.add(new Edge(526, 347, 126, 10));
        edges.add(new Edge(526, 473, 126, 10));
        edges.add(new Edge(526, 599, 126, 10));


        edges.add(new Edge(143, 100, 10, 126));
        edges.add(new Edge(269, 100, 10, 126));
        edges.add(new Edge(395, 100, 10, 126));
        edges.add(new Edge(521, 100, 10, 126));
        edges.add(new Edge(647, 100, 10, 126));

        edges.add(new Edge(143, 226, 10, 126));
        edges.add(new Edge(269, 226, 10, 126));
        edges.add(new Edge(395, 226, 10, 126));
        edges.add(new Edge(521, 226, 10, 126));
        edges.add(new Edge(647, 226, 10, 126));

        edges.add(new Edge(143, 352, 10, 126));
        edges.add(new Edge(269, 352, 10, 126));
        edges.add(new Edge(395, 352, 10, 126));
        edges.add(new Edge(521, 352, 10, 126));
        edges.add(new Edge(647, 352, 10, 126));

        edges.add(new Edge(143, 478, 10, 126));
        edges.add(new Edge(269, 478, 10, 126));
        edges.add(new Edge(395, 478, 10, 126));
        edges.add(new Edge(521, 478, 10, 126));
        edges.add(new Edge(647, 478, 10, 126));

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                dots.add(new Dot(148 + 126 * i, 100 + 126 * j, 15));
            }
        }

        squares.add(new Square_1(edges.get(0), edges.get(1), edges.get(20), edges.get(21)));
        squares.add(new Square_1(edges.get(1), edges.get(2), edges.get(25), edges.get(26)));
        squares.add(new Square_1(edges.get(2), edges.get(3), edges.get(30), edges.get(31)));
        squares.add(new Square_1(edges.get(3), edges.get(4), edges.get(35), edges.get(36)));
        squares.add(new Square_1(edges.get(5), edges.get(6), edges.get(21), edges.get(22)));
        squares.add(new Square_1(edges.get(6), edges.get(7), edges.get(26), edges.get(27)));
        squares.add(new Square_1(edges.get(7), edges.get(8), edges.get(31), edges.get(32)));
        squares.add(new Square_1(edges.get(8), edges.get(9), edges.get(36), edges.get(37)));
        squares.add(new Square_1(edges.get(10), edges.get(11), edges.get(22), edges.get(23)));
        squares.add(new Square_1(edges.get(11), edges.get(12), edges.get(27), edges.get(28)));
        squares.add(new Square_1(edges.get(12), edges.get(13), edges.get(32), edges.get(33)));
        squares.add(new Square_1(edges.get(13), edges.get(14), edges.get(37), edges.get(38)));
        squares.add(new Square_1(edges.get(15), edges.get(16), edges.get(23), edges.get(24)));
        squares.add(new Square_1(edges.get(16), edges.get(17), edges.get(28), edges.get(29)));
        squares.add(new Square_1(edges.get(17), edges.get(18), edges.get(33), edges.get(34)));
        squares.add(new Square_1(edges.get(18), edges.get(19), edges.get(38), edges.get(39)));
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
        //Square_1.points(f,p,200,5400,200,5);

        StdDraw.setPenColor(currentColor);
        StdDraw.setFont(new Font("Times New Roman",Font.BOLD,30));
        if(currentColor.equals(Color.RED))
            StdDraw.text(150, 50, "Spider Man's turn.");
        else{
            StdDraw.text(150, 50, "Batman's turn.");
        }
//      edges.forEach(Edge::paint);
//      dots.forEach(Dot::paint);
        if(f + p == 16){
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
        fivepvp_1 game = new fivepvp_1(800, 800);

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
