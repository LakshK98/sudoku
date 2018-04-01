/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import com.mysql.jdbc.Connection;
import java.sql.*;
import java.util.*;


/**
 *
 * @author lakshkotian
 */
public class sudoApplet extends Applet implements Runnable {
    String time;
    volatile boolean clock;
    int score=0,select,limit,m,s;
    int arr[][]=new int [9][9];
    String userName;
    Label scrabble,exists,iScore,invalid,min,sec,incorrect,timer,dot;
    Label l[]=new Label[20];       
    Panel parent1,parent2,parent3,parent4,parent5,parent6,parent7;
    TextField tf;
    TextField[][] grid=new TextField[9][9];
    Button startGame,quit,submit,easy,diff,submitA,end,next,pg;
    Font myFont = new Font("TimesRoman", Font.BOLD, 35);
    Font myFont2 = new Font("TimesRoman",Font.PLAIN, 25);
    Font myFont3 = new Font("TimesRoman",Font.ITALIC, 23);
   
    
    ArrayList<User> users = new ArrayList<>();
    User curUser;
    Connection con = null;
    {
    try{
        con = (Connection)DriverManager.getConnection("jdbc:mysql://localhost:3306/loginuser?useSSL=false","root","lkjhpoiu");
        System.out.println("Success");
        Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs=st.executeQuery("SELECT * FROM loginuser.sudo");
        while(rs.next()){
            
            users.add(new User(rs.getString("name"),rs.getString("easy"),rs.getString("diff")));
              
        }
    }
    catch(SQLException e){
    System.out.println(e);
    }
    }

//    public sudoApplet() {
//        this.clock = true;
//    }
    public void init(){
        resize(500,600);
       
        setLayout(null);
        par1();
      
    }
    public void par1(){
    //    clock=true;
        parent1 = new Panel(null);
        parent1.setBounds(0, 0, 500, 600);
        parent1.setBackground(Color.BLUE);
        System.out.println("par1");
         
        Panel p1= new Panel();
        p1.setLayout(null);
        p1.setBounds(0, 0, 500, 300);
        scrabble = new Label("  Sudoku");
        scrabble.setFont(myFont);
        scrabble.setBounds(175,240,150,60);
        p1.add(scrabble);
        Panel p2= new Panel();
        p2.setBounds(0, 300, 500, 300);
        p2.setLayout(null);
        startGame = new Button("Start Game");
        startGame.setFont(myFont2);
        
        startGame.addActionListener(new myHandler());
        startGame.setBounds(175,0,150,85);
        p2.add(startGame);
        
        parent1.add(p1);
        parent1.add(p2);
        add(parent1);
    }
    public void par2(){
        
        parent2 = new Panel(null);
        parent2.setBounds(0, 0, 500, 600);
        parent2.setFont(myFont2);
        parent2.setBackground(Color.BLUE);
        Label entrName =new Label("Enter Name");
        entrName.setBounds(75, 200, 150, 50);
        parent2.add(entrName);
        
        exists=new Label("");
        exists.setBounds(80, 300, 245, 50);
        parent2.add(exists);
        tf = new TextField("");
        tf.setBounds(75, 250, 350, 50);
        exists.setFont(myFont3);
        parent2.add(tf);
        
        submit = new Button("Submit");
        submit.setBounds(325, 300, 100, 50);
        parent2.add(submit);
        submit.addActionListener(new myHandler());
        
        
        
        add(parent2);
    }
    public void par3(){
        parent3 = new Panel(null);
        parent3.setBounds(0, 0, 500, 600);
        parent3.setFont(myFont);

        easy = new Button("Easy");
        easy.addActionListener(new myHandler());
        easy.setBounds(165,215,170,85);
        
        diff = new Button("Difficult");
        diff.addActionListener(new myHandler());
        diff.setBounds(165,300,170,85);
        
        
       parent3.add(easy);
       parent3.add(diff);
       
       
        add(parent3);
        
    }
    public void par4(int x){
         timer = new Label("Timer");
        timer.setBounds(200, 0, 100, 50);
        timer.setFont(myFont);
        add(timer);
        
        incorrect = new Label("         Incorrect!");
        incorrect.setBounds(150,263,207,80);
        incorrect.setFont(myFont2);
        incorrect.setVisible(false);
        add(incorrect);    
        
        SudokuMaker obj = new SudokuMaker();    
        arr = obj.makeGrid();   
        int arr2[][]= new int[9][9];
        for(int i=0;i<9;i++){
            for(int j = 0;j<9;j++){
                System.out.print(arr[i][j]);
                arr2[i][j]=arr[i][j];
            }
            System.out.println();
        }
        arr2 = obj.disappear(arr2,x);
        
    
        int temp1,temp2,temp3=0;
        for(int i=0;i<9;i++){
            temp2=0;
            if(i==3 )
               temp3=3;
            if(i==6)
               temp3=6;
            
                for (int j= 0 ;j<9;j++){
                  
                    if(j==3)
                        temp2=3;
                    if(j==6)
                        temp2=6;
                    temp1=arr2[i][j];
                    if(temp1==0)
                    grid[i][j]=new TextField("");
                    else{
                    grid[i][j]=new TextField(arr2[i][j]+"");
                    grid[i][j].setEditable(false);
                    }
                    grid[i][j].addKeyListener(new KeyListener(){
                        public void keyTyped(KeyEvent e){
                            incorrect.setVisible(false);
                        }
                        public void keyPressed(KeyEvent e){

                        }
                        public void keyReleased(KeyEvent e){

                        }
                    });
                    grid[i][j].setBounds(70+(j*40)+temp2, 120+(i*40)+temp3, 40, 40);
                    add(grid[i][j]);
                }


        }
        min = new Label("0");
        min.setBounds(205, 40, 40, 60);
        min.setFont(myFont);
        add(min);
        
        
        dot = new Label(":");
        dot.setBounds(245, 40, 20, 60);
        dot.setFont(myFont);
        add(dot);
        
        sec = new Label("0");
        sec.setBounds(265, 40, 40, 60);
        sec.setFont(myFont);
        sec.setText("0");
        add(sec);
     
        submitA = new Button("Submit");
        submitA.setBounds(175, 540, 150, 60);
        submitA.setFont(myFont);
        submitA.addActionListener(new myHandler());
        add(submitA);
        
        quit = new Button("Quit");
        quit.setBounds(400, 0, 100, 50);
        quit.addActionListener(new myHandler());
        add(quit);
      
        this.addMouseListener(new MouseListener(){
             @Override
             public void mouseClicked(MouseEvent e) {
                 incorrect.setVisible(false);
             }

             @Override
             public void mousePressed(MouseEvent e) {
              //   incorrect.setVisible(false);
             }

             @Override
             public void mouseReleased(MouseEvent e) {
             }

             @Override
             public void mouseEntered(MouseEvent e) {
             }

             @Override
             public void mouseExited(MouseEvent e) {
             }
            
        });
        Thread t ;
        t = new Thread(this);
        t.start();
    
        
       
    }
    
    public void paint(Graphics g){
        for(int i=0;i<9;i++){
        g.drawLine(110+(40*i), 120, 110+(40*i), 480);
        g.drawLine(70, 160+(40*i),430 ,160+(40*i) );
        
        }
        Graphics2D g2D = (Graphics2D) g;
        g2D.setStroke(new BasicStroke(5F));
        g2D.drawLine(190, 120, 190, 483);
        g2D.drawLine(315, 120, 315, 483);
        g2D.drawLine(70, 242, 433, 242);
        g2D.drawLine(70, 365, 433, 365);
        g2D.drawRect(68, 118, 369, 369);
    }
    
    
    @Override
    
    public void run(){
        Thread t = Thread.currentThread();
        int m=1;
        clock =true;
        int s =0;
         try {
             while(clock){
               sec.setText(s++ +"");
               t.sleep(1000);
                
                if(s==60){
                    
                    min.setText(m+"");
                    
                    m++;
                    s=0;
                }
                
                
                
            
             }
             
              
         
         } catch (InterruptedException ex) {
             
             System.out.println("Sleep exception");
         }
        
      
    }
    public void par5(){
        parent5 = new Panel();
        parent5.setBounds(0, 0, 500, 600);
        
        Label ldr =new Label("Highscores");
        ldr.setBounds(150, 0, 175, 60);
        ldr.setFont(myFont);
        parent5.add(ldr);
        
        next = new Button("Next");
        next.setBounds(400, 550, 100, 50);
        next.setFont(myFont);
        next.addActionListener(new myHandler());
        parent5.add(next);
        
        for(int i=0;i<10;i++){
            l[i]= new Label("");
            l[i].setBounds(100,60+(50*i) , 150, 50);
            l[i].setFont(myFont2);
            parent5.add(l[i]);
        }
        for(int i=10;i<20;i++){
            l[i]= new Label("");
            l[i].setBounds(300,60+(50*(i-10)) , 75, 50);
            l[i].setFont(myFont2);
            parent5.add(l[i]);
        }
        add(parent5);
          
    }
    public void eHighSc(){
        int i=0; 
        if (select!=3)
        { 
            curUser = new User(userName);
            curUser.easy=time;
            String sql=
                    "INSERT INTO `sudo` (`name`,`easy`) VALUES (?,?)";


            try {
                PreparedStatement stmt=con.prepareStatement(sql);
                stmt.setString(1, userName);
                stmt.setString(2, (time));
                stmt.execute();
            } catch (SQLException ex) {

                System.out.println("fuckalle");
            }
            
        users.add(curUser);
        }
        par5();
        
        Collections.sort(users, new Comparator<User>(){
            public int compare(User one,User two){
                if(one.min!=two.min)
                    return two.min-one.min;
                
                return two.sec-one.sec;
            }
        });
        
        for(User x:users){
            if(x.easy!=null){
                l[i+10].setText(x.easy);
                l[i++].setText(x.userName);
            }                      
            if(i==10)
                break;
        }
    }
    public void par6(){
         
     timer.setVisible(false);
     min.setVisible(false);
     sec.setVisible(false);
     submitA.setLabel("Next");
     dot.setBounds(175, 40, 150, 50);
     dot.setText("Solution");
     quit.setVisible(false);
     for(int i=0;i<9;i++){ 
        for(int j = 0;j<9;j++){
            
                grid[i][j].setText(arr[i][j]+"");
                grid[i][j].setEditable(false);
        }  
      }
    }
    public void par7(){
        parent7 = new Panel(null);
        parent7.setBounds(0, 0, 500, 600);
        
        pg = new Button("Play Again");
        pg.setFont(myFont);
        pg.setBounds(150, 250, 200, 100);
        pg.addActionListener(new myHandler());
        parent7.add(pg);
        
        add(parent7);
        
    }
    public void dHighSc(){
        int i=0;
        if (select!=3)
        { 
            curUser = new User(userName);
            curUser.diff=time;
            curUser.min=m;
            curUser.sec=s;
            String sql=
                    "INSERT INTO `sudo` (`name`,`diff`) VALUES (?,?)";


            try {
                PreparedStatement stmt=con.prepareStatement(sql);
                stmt.setString(1, userName);
                stmt.setString(2, (time));
                stmt.execute();
            } catch (SQLException ex) {

                System.out.println("err");
            }
        }
        par5();
        
        users.add(curUser);
        Collections.sort(users, new Comparator<User>(){
            public int compare(User one,User two){
                if(one!=null &&two!=null){

                if(one.min!=two.min)
                    return two.min-one.min;

                return two.sec-one.sec;
            }
                
            else return 0;
            }
        });
        
        for(User x:users){
            if(x!=null && x.diff!=null){
                l[i+10].setText(x.diff);
                l[i++].setText(x.userName);
            }                      
            if(i==10)
                break;
        }
    }
    public boolean comp(){
      try{  
      for(int i=0;i<9;i++){
        for(int j = 0;j<9;j++){
            if(Integer.parseInt(grid[i][j].getText())!=arr[i][j])
                return false;
        }  
      }
      }
      catch(NumberFormatException e){
          return false;
      }
      return true;
    }
    public class myHandler implements ActionListener{
    public void actionPerformed(ActionEvent e){
            
                
            if(e.getSource()==startGame){
            parent1.setVisible(false);
              
                par2();
            }
            
        
            if(e.getSource()==submit){
                userName=tf.getText();
                if(userName.equals(""))
                    exists.setText("Invalid username");
                else{
                    
                    removeAll();
                    
                    par3();
     
                   
                }  
                
                
            }
            if(e.getSource()==easy){
                
                parent3.setVisible(false);
                par4(25);
                
            }
            if(e.getSource()==diff){
                select=1;
                parent3.setVisible(false);
               
                par4(45);
                
            }
            if(e.getSource()==quit){
                clock=false;
                select=3;
                
               // removeAll();
                par6();
                
            }
            if(e.getSource()==submitA){
                if(comp()){
                    m=Integer.parseInt(min.getText());
                    s=Integer.parseInt(sec.getText());
                    time=m+":"+s;
                    clock=false;
                    removeAll();
                    if(select==0)
                    eHighSc();
                    else if(select==1)
                    dHighSc();
                    else
                        par7();
                }
                else
                    incorrect.setVisible(true);
                
            }
            if(e.getSource()==next){
                removeAll();
                if(select==0)
                    eHighSc();
                    else 
                    dHighSc();
                
            }
            if(e.getSource()==pg){
                removeAll();
                par1();
            }
        }
    
    }
}
