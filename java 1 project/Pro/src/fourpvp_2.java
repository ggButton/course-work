
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class fourpvp_2 {
    //    private List<Edge> edges = new ArrayList<>();
//    private List<Dot> dots = new ArrayList<>();
    private ArrayList <Edge> edges = new ArrayList<>();
    private ArrayList <Dot> dots = new ArrayList<>();
    private ArrayList <Square> squares = new ArrayList<>();
    ArrayList <Integer> indexOfSquare = new ArrayList<>();
    ArrayList <Integer> step1 = new ArrayList<>();
    ArrayList <Integer> step2 = new ArrayList<>();
    int s1=0;
    int s2=0;

    private Font font= new Font("Times New Roman",Font.BOLD,25);

    private Color currentColor = Color.RED;
    private boolean yourTurn=true;
    private String name1="Spider Man";
    private String name2="Batman";
    public int a=0;
    public int b=0;

    public void setYourTurn(boolean TF){
        yourTurn=TF;
    }

    public boolean getYourTurn(){
        return yourTurn;
    }

    public void setName1(String name){
        name1=name;
    }

    public String getName1(){
        return name1;
    }

    public void setName2(String name){
        name2=name;
    }

    public String getName2(){
        return name2;
    }


    public fourpvp_2(int canvasWidth, int canvasHeight) {
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

        squares.add(new Square(edges.get(0), edges.get(1), edges.get(12), edges.get(13)));
        squares.add(new Square(edges.get(4), edges.get(5), edges.get(13), edges.get(14)));
        squares.add(new Square(edges.get(8), edges.get(9), edges.get(14), edges.get(15)));
        squares.add(new Square(edges.get(1), edges.get(2), edges.get(16), edges.get(17)));
        squares.add(new Square(edges.get(5), edges.get(6), edges.get(17), edges.get(18)));
        squares.add(new Square(edges.get(9), edges.get(10), edges.get(18), edges.get(19)));
        squares.add(new Square(edges.get(2), edges.get(3), edges.get(20), edges.get(21)));
        squares.add(new Square(edges.get(6), edges.get(7), edges.get(21), edges.get(22)));
        squares.add(new Square(edges.get(10), edges.get(11), edges.get(22), edges.get(23)));
    }

    public boolean ifContinue(Edge e){

        indexOfSquare.clear();
        int n=0;
        for(int i=0; i<squares.size(); i++){
            if(squares.get(i).getPack().contains(e)){
                if(squares.get(i).isSet()){
                    n++;
                    indexOfSquare.add(i);
                }
            }
        }

        if(n!=0)
            return true;
        else
            return false;
    }


    public int Primary(int edgeNum){
        int tt = 0;
        int rer = 0;
        int yes = 0;
        int numbersquarenotfree = 0;
        ArrayList<Integer>list1 = new ArrayList<Integer>();
        Random randomEdge=new Random();
        int random=randomEdge.nextInt(edgeNum);
        while(!edges.get(random).isFree()){
            random=randomEdge.nextInt(edgeNum);
        }

        for(int u = 0;u<squares.size();u++){
            for(int q = 0;q < 4;q++){
                if(!squares.get(u).get(q).isFree()){
                    tt++;
                }
            }
            if(tt == 2){
                rer++;
            }
            while (tt == 2){
                if(squares.get(u).getPack().contains(edges.get(random))&&edges.get(random).isFree()&&yes<=2){
                    list1.add(random);
                    random=randomEdge.nextInt(edgeNum);
                    yes++;
                }else {
                    random=randomEdge.nextInt(edgeNum);
                }
                if(yes == 2){
                    break;
                }
            }
            yes = 0;
            if(tt == 3 ){
                while (true){
                    if(!edges.get(random).isFree()||!squares.get(u).getPack().contains(edges.get(random))){
                        random=randomEdge.nextInt(edgeNum);
                    }else {
                        break;
                    }
                }
                return random;
            }
            tt = 0;
        }
        for(Square square3:squares){
            if(square3.isSet()){
                numbersquarenotfree++;
            }
        }
        while(true){
            if(edges.get(random).isFree()&&!list1.contains(random)){break;}
            if(rer == 9-numbersquarenotfree && edges.get(random).isFree()){
                break;
            }
            random=randomEdge.nextInt(edgeNum);
        }
        /*Random randomEdge=new Random();
        int random=randomEdge.nextInt(edgeNum);
        while(!edges.get(random).isFree()){
            random=randomEdge.nextInt(edgeNum);
        }*/

        return random;
    }



    public boolean isEnd(){
        int n=0;
        for(Square square: squares){
            if(square.isSet())
                n++;
        }
        if(n==squares.size())
            return true;
        else return false;
    }

    public void update() {
        //System.out.println("update");
        Point mousePoint = new Point((int) StdDraw.mouseX(), (int) StdDraw.mouseY());
        boolean isMousePressed = StdDraw.isMousePressed();
        boolean foundEdge = false;

        if(getYourTurn()) {
            int index=Primary(24);
            edges.get(index).setFree(false);
            edges.get(index).setColor(currentColor);
            edges.get(index).setVisible(true);

            if(ifContinue(edges.get(index))){
                for(int i=0; i<indexOfSquare.size(); i++){
                    squares.get(indexOfSquare.get(i)).setName(name1);
                    squares.get(indexOfSquare.get(i)).setColor(currentColor);
                    a++;
                }
                setYourTurn(true);
            }
            else{
                setYourTurn(false);
                currentColor = currentColor == Color.RED ? Color.BLUE : Color.RED;
            }

        }else{
            int index=Primary(24);
            edges.get(index).setFree(false);
            edges.get(index).setColor(currentColor);
            edges.get(index).setVisible(true);

            if(ifContinue(edges.get(index))){
                for(int i=0; i<indexOfSquare.size(); i++){
                    squares.get(indexOfSquare.get(i)).setName(name2);
                    squares.get(indexOfSquare.get(i)).setColor(currentColor);
                    b++;
                }
                setYourTurn(false);
            }
            else{
                setYourTurn(true);
                currentColor = currentColor == Color.RED ? Color.BLUE : Color.RED;
            }
        }
    }

    public void paint(String name) {
        StdDraw.clear();
        // Paint edges first, so dots will be on the top layer.

        StdDraw.picture(400,400, name, 800,800);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.BLACK);

        StdDraw.setPenColor(Color.RED);
        StdDraw.textLeft(200, 700, name1+":"+a);
        StdDraw.setPenColor(Color.BLUE);
        StdDraw.textLeft(450, 700, name2+":"+b);

        if(!isEnd()){
            if(yourTurn){
                StdDraw.setPenColor(Color.RED);
                StdDraw.textLeft(150, 50, "Spider Man's turn.");
            }else{
                StdDraw.setPenColor(Color.BLUE);
                StdDraw.textLeft(150,50,"Batman's turn.");
            }
        }

        for (Edge edge: edges) {
            edge.paint();
        }
        for (Dot dot: dots) {
            dot.paint(Color.BLACK);
        }
        for(Square square:squares){
            square.paintName();
        }
//      edges.forEach(Edge::paint);
//      dots.forEach(Dot::paint);
        if(isEnd()){
            //System.out.println("qqqq");
            StdDraw.setPenColor(Color.CYAN);
            StdDraw.setFont(new Font("Tw Cen MT",Font.BOLD,75));
            StdDraw.picture(400,400,"16.jpeg",800,800);
            if(a>b){
                StdDraw.text(400,400,"Winner is "+name1);
                StdDraw.text(400,350,"Score is :"+a);
                StdDraw.text(400,300,"Loser's score is :"+b);
            }
                //StdDraw.text(400, 400, "Spider Man wins !");
            else if(a==b){
                StdDraw.text(400,400,"No Winner");
                StdDraw.text(400,350,"Both score is :"+a);
            }
                //StdDraw.text(400, 400, "Draw !");
            else if(a<b){
                StdDraw.text(400,400,"Winner is "+name2);
                StdDraw.text(400,350,"Score is :"+b);
                StdDraw.text(400,300,"Loser's score is :"+a);
            }
                //StdDraw.text(400, 400, "Batman wins !");
        }

        StdDraw.show();
    }


    public static void main(String[] args) {
        fourpvp_2 game = new fourpvp_2(800, 800);
        while (true) {
            game.update();
            game.paint("14.jpeg");
            StdDraw.pause(1000);
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
