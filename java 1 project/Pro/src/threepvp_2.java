
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class threepvp_2 {
    //    private List<Edge> edges = new ArrayList<>();
//    private List<Dot> dots = new ArrayList<>();
    private ArrayList <Edge> edges = new ArrayList<>();
    private ArrayList <Dot> dots = new ArrayList<>();
    private ArrayList <Square> squares=new ArrayList<>();
    ArrayList <Integer> indexOfSquare=new ArrayList<>();
    private boolean previousMouse=false;
    private boolean MousePress=false;

    private Font font= new Font("Times New Roman",Font.BOLD,25);
    protected Button home=new Button(550, 700, 130,40,"Home");


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


    public threepvp_2(int canvasWidth, int canvasHeight) {
        StdDraw.setCanvasSize(canvasWidth, canvasHeight);
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 800);
        StdDraw.setYscale(0, 800);
        initialize();
    }

    public void initialize() {
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


        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                dots.add(new Dot(200 + 200 * i, 150 + 200 * j, 15));
            }
        }

        squares.add(new Square(edges.get(1), edges.get(2), edges.get(9), edges.get(10)));
        squares.add(new Square(edges.get(4), edges.get(5), edges.get(10), edges.get(11)));
        squares.add(new Square(edges.get(0), edges.get(1), edges.get(6), edges.get(7)));
        squares.add(new Square(edges.get(3), edges.get(4), edges.get(7), edges.get(8)));
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
            if(rer == 4-numbersquarenotfree && edges.get(random).isFree()){
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

    /*public int Advanced(int edgeNum){



    }*/

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

    public boolean isMousePressed(){
        if(previousMouse & !MousePress){
            return true;
        }else
            return false;
    }



    public void update() {
        //System.out.println("update");
        Point mousePoint = new Point((int) StdDraw.mouseX(), (int) StdDraw.mouseY());
        previousMouse=MousePress;
        MousePress = StdDraw.isMousePressed();
        //boolean isMousePressed = StdDraw.isMousePressed();
        boolean foundEdge = false;

        if (home.getBounds().contains(mousePoint)) {
            home.setVisible(true);
            if (isMousePressed()) {
                home.setChoose(true);
            }
        } else
            home.setVisible(false);

        if(getYourTurn()) {

            int index=Primary(12);
            StdDraw.pause(500);
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
            int index=Primary(12);
            StdDraw.pause(500);
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
        StdDraw.text(300, 650, name1+":"+a);
        StdDraw.setPenColor(Color.BLUE);
        StdDraw.text(500, 650, name2+":"+b);
        home.paint();

        if(!isEnd()){
            if(yourTurn){
                StdDraw.setPenColor(Color.RED);
                StdDraw.textLeft(185, 80, "Spider Man's turn.");
            }else{
                StdDraw.setPenColor(Color.BLUE);
                StdDraw.textLeft(385,80,"Batman's turn.");
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
            StdDraw.setFont(new Font("Tw Cen MT",Font.BOLD,75));
            StdDraw.picture(400,400,"5.jpg",800,800);
            //home.paint();
            //System.out.println("qqqq");

            StdDraw.setPenColor(Color.CYAN);
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
            //home.setVisible(false);
        }

        StdDraw.show();
    }

    public void Reset(){
        indexOfSquare.clear();

        MousePress=false;
        previousMouse=false;
        a=0;
        b=0;

        for(Edge e : edges){
            e.setVisible(false);
            e.setFree(true);
        }
        for(Square s : squares){
            s.setName(" ");
        }
    }

    public static void main(String[] args) {
        threepvp_2 game = new threepvp_2(800, 800);
        while (true) {
            game.update();
            game.paint("5.jpg");
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
