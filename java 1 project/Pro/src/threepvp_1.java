
import java.awt.*;
import java.util.ArrayList;

public class threepvp_1 {
    //    private List<Edge> edges= new ArrayList<>();
//    private List<Dot> dots = new ArrayList<>();
    private ArrayList<Edge> edges = new ArrayList<>();
    private ArrayList<Dot> dots = new ArrayList<>();
    private ArrayList<Square_1> squares = new ArrayList<>();
    private Color currentColor = Color.RED;
    private Font font = new Font("Tw Cen MT",Font.BOLD,75);

    protected Button home=new Button(550, 700, 130,40,"Home");

    int d = 0;
    int f = 0;
    int p = 0;

    public threepvp_1(int canvasWidth, int canvasHeight) {
        StdDraw.setCanvasSize(canvasWidth, canvasHeight);
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 800);
        StdDraw.setYscale(0, 800);
        initialize();
    }

    public void initialize() {
        /*edges.add(new Edge(75, 70, 150, 10));
        edges.add(new Edge(75, 220, 150, 10));
        edges.add(new Edge(75, 370, 150, 10));
        edges.add(new Edge(225, 70, 150, 10));
        edges.add(new Edge(225, 220, 150, 10));
        edges.add(new Edge(225, 370, 150, 10));
        edges.add(new Edge(70, 75, 10, 150));
        edges.add(new Edge(220, 75, 10, 150));
        edges.add(new Edge(370, 75, 10, 150));
        edges.add(new Edge(70, 225, 10, 150));
        edges.add(new Edge(220, 225, 10, 150));
        edges.add(new Edge(370, 225, 10, 150));*/

        edges.add(new Edge(200, 145, 200, 10));
        edges.add(new Edge(200, 345, 200, 10));
        edges.add(new Edge(200, 545, 200, 10));
        edges.add(new Edge(400, 145, 200, 10));
        edges.add(new Edge(400, 345, 200, 10));
        edges.add(new Edge(400, 545, 200, 10));
        edges.add(new Edge(195, 150, 10, 200));
        edges.add(new Edge(395, 150, 10, 200));
        edges.add(new Edge(595, 150, 10, 200));
        edges.add(new Edge(195, 350, 10, 200));
        edges.add(new Edge(395, 350, 10, 200));
        edges.add(new Edge(595, 350, 10, 200));

        squares.add(new Square_1(edges.get(0),edges.get(1),edges.get(6),edges.get(7)));
        squares.add(new Square_1(edges.get(1),edges.get(2),edges.get(9),edges.get(10)));
        squares.add(new Square_1(edges.get(3),edges.get(4),edges.get(7),edges.get(8)));
        squares.add(new Square_1(edges.get(4),edges.get(5),edges.get(10),edges.get(11)));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                dots.add(new Dot(200 + 200 * i, 150 + 200 * j, 15));
            }
        }

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
        StdDraw.setFont(new Font("Times New Roman",Font.BOLD,25));
        StdDraw.text(200, 700, "Spider Man:"+f);
        StdDraw.text(450, 700, "Batman:"+p);

        for(Square_1 square:squares){
            square.Text(square.getColor());
        }

        for (Edge edge: edges) {
            edge.paint();
        }
        for (Dot dot: dots) {
            dot.paint(Color.LIGHT_GRAY);
        }
        //Square_1.points(f,p,200,700,200,5);

        StdDraw.setPenColor(currentColor);
        StdDraw.setFont(new Font("Times New Roman",Font.BOLD,30));
        if(currentColor.equals(Color.RED))
            StdDraw.text(150, 100, "Spider Man's turn.");
        else{
            //StdDraw.setPenColor(Color.BLUE);
            StdDraw.text(150, 100, "Batman's turn.");
        }


//      edges.forEach(Edge::paint);
//      dots.forEach(Dot::paint);
        if(f + p == 4){
            StdDraw.setFont(font);
            StdDraw.picture(400,400, name, 800,800);
            if(f > p){
                StdDraw.setPenColor(StdDraw.CYAN);
                StdDraw.text(400,400,"Winner is Spider Man");
                StdDraw.text(400,350,"Score is :"+f);
                StdDraw.text(400,300,"Loser's score is :"+p);
            }
            else {
                if(f < p){
                    StdDraw.setPenColor(StdDraw.CYAN);
                    StdDraw.text(400,400,"Winner is Batman");
                    StdDraw.text(400,350,"Score is :"+p);
                    StdDraw.text(400,300,"Loser's score is :"+f);
                }else {
                    StdDraw.setPenColor(StdDraw.CYAN);
                    StdDraw.text(400,400,"Draw!");
                    StdDraw.text(400,350,"Both score is :"+f);
                }
            }
        }
        StdDraw.show();
    }

    public static void main(String[] args) {
        threepvp_1 game = new threepvp_1(800, 800);

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
