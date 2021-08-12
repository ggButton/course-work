

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class threepvp {
    //    private List<Edge> edges = new ArrayList<>();
//    private List<Dot> dots = new ArrayList<>();
    private ArrayList <Edge> edges = new ArrayList<>();
    private ArrayList <Dot> dots = new ArrayList<>();
    private ArrayList <Square> squares=new ArrayList<>();
    private ArrayList <Integer> indexOfSquare=new ArrayList<>();
    private ArrayList<Edge> recordEdge1=new ArrayList<>();
    private ArrayList<Edge> recordEdge2=new ArrayList<>();
    ArrayList <Integer> step1 = new ArrayList<>();
    ArrayList <Integer> step2 = new ArrayList<>();
    int s=0;

    private Button withdraw=new Button(150, 700, 130,40,"Withdraw");
    private boolean MousePress=false;
    private boolean previousMouse=false;

    protected Button reset=new Button(400, 700, 130,40,"Again");
    protected Button home=new Button(550, 700, 130,40,"Home");

    private Font font= new Font("Times New Roman",Font.BOLD,25);

    private Color currentColor = Color.RED;
    private boolean yourTurn;
    private String name1="A";
    private String name2="B";
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


    public threepvp(int canvasWidth, int canvasHeight, boolean TF) {
        StdDraw.setCanvasSize(canvasWidth, canvasHeight);
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 800);
        StdDraw.setYscale(0, 800);
        initialize();
        setYourTurn(TF);
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

        squares.add(new Square(edges.get(0), edges.get(1), edges.get(6), edges.get(7)));
        squares.add(new Square(edges.get(1), edges.get(2), edges.get(9), edges.get(10)));
        squares.add(new Square(edges.get(3), edges.get(4), edges.get(7), edges.get(8)));
        squares.add(new Square(edges.get(4), edges.get(5), edges.get(10), edges.get(11)));

    }

    /*public boolean isReset(){
        return reset.isChoose();
    }*/

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

        return random;
    }


    public void Reset(){
        indexOfSquare.clear();
        recordEdge1.clear();
        recordEdge2.clear();
        step1.clear();
        step2.clear();
        s=0;
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


    public void withDraw(){
        if(step1.size()>0){
            for(int i=0; i<step1.get(step1.size()-1); i++){
                int n=recordEdge1.size()-1;
                recordEdge1.get(n).setFree(true);
                recordEdge1.get(n).setVisible(false);
                recordEdge1.remove(n);
            }
            step1.remove(step1.size()-1);
        }

        if(step2.size()>0){
            for(int j=0; j<step2.get(step2.size()-1); j++){
                int m=recordEdge2.size()-1;
                recordEdge2.get(m).setFree(true);
                recordEdge2.get(m).setVisible(false);
                recordEdge2.remove(m);
            }
            step2.remove(step2.size()-1);
        }

        for(Square s:squares){
            if(!s.isSet()){
                if(!s.getName().equals(" "))
                    s.setName(" ");
            }
        }
    }


    public boolean isMousePressed(){
        if(previousMouse & !MousePress){
            return true;
        }else
            return false;
    }

    public void update() {
        Point mousePoint = new Point((int) StdDraw.mouseX(), (int) StdDraw.mouseY());
        previousMouse=MousePress;
        MousePress = StdDraw.isMousePressed();
        boolean foundEdge = false;

        if(!isEnd()){
            if (withdraw.getBounds().contains(mousePoint)) {
                withdraw.setVisible(true);
                if (isMousePressed()) {
                    withdraw.setChoose(true);
                }
            } else
                withdraw.setVisible(false);
        }

        if (reset.getBounds().contains(mousePoint)) {
            reset.setVisible(true);
            if (isMousePressed()) {
                reset.setChoose(true);
            }
        } else
            reset.setVisible(false);

        if (home.getBounds().contains(mousePoint)) {
            home.setVisible(true);
            if (isMousePressed()) {
                home.setChoose(true);
            }
        } else
            home.setVisible(false);

        if(getYourTurn()) {
            if(withdraw.isChoose()){
                withDraw();
                withdraw.setChoose(false);
                withdraw.setVisible(false);

            }else {
                for (Edge edge : edges) {

                    if (edge.isFree()) {
                        if (!foundEdge && edge.getBounds().contains(mousePoint)) {
                            edge.setColor(currentColor);
                            edge.setVisible(true);

                            if (isMousePressed()) {
                                edge.setFree(false);
                                recordEdge1.add(edge);
                                s++;
                                if (ifContinue(edge)) {
                                    for (int i : indexOfSquare) {
                                        squares.get(i).setColor(currentColor);
                                        squares.get(i).setName(name1);
                                        a++;
                                    }
                                    setYourTurn(true);
                                } else {
                                    setYourTurn(false);
                                    currentColor = currentColor == Color.RED ? Color.BLUE: Color.RED;
                                    step1.add(s);
                                    s=0;
                                }

                            }
                            foundEdge = true; // Avoid multiple selections.

                        } else {
                            edge.setVisible(false);
                        }
                    }
                }
            }
        }else {
            int index = Primary(12);
            recordEdge2.add(edges.get(index));
            StdDraw.pause(1000);
            edges.get(index).setFree(false);
            edges.get(index).setColor(currentColor);
            edges.get(index).setVisible(true);
            s++;

            if (ifContinue(edges.get(index))) {
                for (int i: indexOfSquare) {
                    squares.get(i).setName(name2);
                    squares.get(i).setColor(currentColor);
                    b++;
                }
                setYourTurn(false);
            } else {
                setYourTurn(true);
                currentColor = currentColor == Color.RED ? Color.BLUE : Color.RED;
                step2.add(s);
                s=0;
            }
        }

    }

    public void paint(String name) {
        StdDraw.clear();
        // Paint edges first, so dots will be on the top layer.
        StdDraw.picture(400,400,name, 800,800);
        StdDraw.setFont(font);

        StdDraw.setPenColor(Color.MAGENTA);
        StdDraw.textLeft(200, 650, name1+":"+a);
        StdDraw.textRight(600, 650, name2+":"+b);
        StdDraw.setPenColor(Color.BLACK);
        withdraw.paint();
        reset.paint();
        home.paint();

        if(!isEnd()){
            StdDraw.setFont(new Font("Times New Roman",Font.BOLD,25));
            if(yourTurn){
                StdDraw.setPenColor(Color.RED);
                StdDraw.textLeft(185, 80, "It's your turn!");
            }else{
                StdDraw.setPenColor(Color.BLUE);
                StdDraw.textLeft(185,80,"Please wait...");
            }
        }

        for (Edge edge: edges) {
            edge.paint();
        }
        for (Dot dot: dots) {
            dot.paint(Color.LIGHT_GRAY);
        }
        for(Square square:squares){
            square.paintName();
        }
//      edges.forEach(Edge::paint);
//      dots.forEach(Dot::paint);
        if(isEnd()){
            StdDraw.setPenColor(Color.CYAN);
            StdDraw.picture(400,400,"3.jpg",800,800);
            home.paint();
            reset.paint();
            StdDraw.setFont(new Font("Tw Cen MT",Font.BOLD,75));
            if(a>b){

                StdDraw.setPenColor(StdDraw.CYAN);
                StdDraw.text(400,400,"Winner is "+name1);
                StdDraw.text(400,350,"Score is :"+a);
                StdDraw.text(400,300,"Loser's score is :"+b);

                //StdDraw.text(400, 400, "Spider Man wins !");
            }

            else if(a==b){
                StdDraw.setPenColor(StdDraw.CYAN);
                StdDraw.text(400,400,"Draw!");
                StdDraw.text(400,350,"Both score is :"+a);
                //StdDraw.text(400, 400, "Draw !");
            }

            else if(a<b){
                StdDraw.setPenColor(StdDraw.CYAN);
                StdDraw.text(400,400,"Winner is "+name2);
                StdDraw.text(400,350,"Score is :"+b);
                StdDraw.text(400,300,"Loser's score is :"+a);

                //StdDraw.text(400, 400, "Batman wins !");
            }

            withdraw.setVisible(false);
            reset.setVisible(false);
            setYourTurn(true);
        }

        StdDraw.show();
    }


    public static void main(String[] args) {
        threepvp game = new threepvp(800, 800, true);
        while (true) {
            game.update();
            game.paint("3.jpg");
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
