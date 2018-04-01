/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;
import java.util.*;
/**
 *
 * @author lakshkotian
 */
public class Sudoku {

    /**
     * @param args the command line arguments
     */
     static Random r=new Random();
      static int count=0; 
        static int arr[][]=new int [9][9];
        
        static int boxi [][]= new int[9][9];
        
        static ArrayList<ArrayList<Integer>> revBox = new ArrayList<>();
                
        static HashMap<Integer,ArrayList<Integer>> possi =new HashMap<>();
        static HashMap<Integer,HashSet<Integer>> possi2 =new HashMap<>();        
        static ArrayList<HashSet<Integer>> col=new ArrayList<>();
        static ArrayList<HashSet<Integer>> row=new ArrayList<>();
        static ArrayList<HashSet<Integer>> box=new ArrayList<>();
        
        
    public static void initialize(){
        
        int count=0;
        
        for(int i=0;i<9;i++){
            revBox.add(new ArrayList());
            col.add(new HashSet<>(9));
            row.add(new HashSet<>(9));
            box.add(new HashSet<>(9));
            
            count=0;
            if(i>=3)
                count=3;
             if( i>=6)
                count =6;
             
            for(int j=0;j<9;j++){
               
                possi.put(i*10+j,new ArrayList());
                if(j==3 || j==6){
                    count++;
                }
                boxi[i][j]=count;
                
            }
        }
        
    }
    public static void main(String[] args) {
        // TODO code application logic here
       
       
       initialize();
    
        sudoku(0,0);
        col.clear();
        row.clear();
        box.clear();
        possi.clear();
        revBox.clear();
        initialize();
        
        int arr2[][]= new int [9][9];
        //System.out.println(row.get(4));
        
        Scanner sc =new Scanner (System.in);
        
        
        for(int i=0;i<9;i++){
            //System.out.println(revBox.get(i));
            for(int j=0;j<9;j++){
               System.out.print(arr[i][j]+" ");
               
               arr2[i][j]=arr[i][j];
              // System.out.println(revBox.get(i));
            }
            System.out.println();
        }
        
        ArrayList<Integer> n = new ArrayList<Integer>();
        for(int i=0;i<9;i++){
            
            for(int j=0;j<9;j++){
                arr2[i][j]=arr[i][j];
           // arr2[i][j]=sc.nextInt();
            if(arr2[i][j]==0)
                n.add(i*10+j);
            }
        }
                
        System.out.println(n);
        disappear(arr2);
        //System.out.println("uuu"+solve(arr2,n));
        System.out.println(count);
        System.out.println();
        for(int i=0;i<9;i++){
           // System.out.println("ssup");
            for(int j=0;j<9;j++){
               System.out.print(arr2[i][j]+" ");
               //arr2[i][j]=arr[i][j];
              // System.out.println(revBox.get(i));
            }
            System.out.println();
        }
        
    }
    
    public static boolean sudoku(int a,int b){
        if(b==9)
        {
            a++;
            b=0;
        }
        ArrayList<Integer> poss;
        poss = possi.get((a*10+b));
        
        populate(poss,a,b);
        while(!poss.isEmpty())
        {
            
               
            arr[a][b]=poss.get(r.nextInt(poss.size()));
            poss.remove(new Integer(arr[a][b]));
            col.get(b).add(arr[a][b]);
            row.get(a).add(arr[a][b]);
            box.get(boxi[a][b]).add(arr[a][b]);
           
            if(a==8 && b==8)
                return true;
            if(sudoku(a,b+1))
                return true;
            
            
            col.get(b).remove(arr[a][b]);
            row.get(a).remove(arr[a][b]);
            box.get(boxi[a][b]).remove(arr[a][b]);
        }
        
        return false;
    }
    
    public static void populate(ArrayList<Integer> h,int a,int b){
       
        h.clear();
        for(int i=1;i<10;i++){
            
            if(!col.get(b).contains(i) && !row.get(a).contains(i)
                                && !box.get(boxi[a][b]).contains(i))
                {
                    h.add(i);
                }
        }
       
       
        
    }
    
    public static void disappear(int x[][]){
        int temp1,a,b,count=0,temp;
        ArrayList<Integer> seen = new ArrayList<>();
        for(int i=0;i<9;i++){
            for(int j =0;j<9;j++ ){
                seen.add((i*10)+j);
            }
            
        }
       // HashSet<Integer> seen= new HashSet<>();
        
        ArrayList<Integer> vacancy = new ArrayList<>();
        while(vacancy.size()<50){
            
            temp=seen.remove(r.nextInt(seen.size()));
            a=temp/10;
            b=temp%10;
            
           
                //System.out.println(seen.size());
               
            temp1=x[a][b];
            x[a][b]=0;
            System.out.print("hey ");
            vacancy.add(temp);
            System.out.println(seen.size());
            if(!solve(x,vacancy)){
                System.out.println("budd ");
                x[a][b]=temp1;
                vacancy.remove(new Integer(a*10+b));
            }

            
        }
    }
    public static boolean solve(int x[][],ArrayList<Integer> vacancy){
        int y[][]=new int[9][9];
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                y[i][j]=x[i][j];
                //System.out.print(y[i][j]+ " ");
            }
           // System.out.println();
        }
        
        //System.out.println(vacancy);
        preProcess(y);
            count=0;
        int temp=mSolver(y,vacancy,0);
        if( count==1)
            return true;
        else
            return false;
    }
    public static int mSolver(int x[][],ArrayList<Integer> vacancy,int index){
        
        int temp= vacancy.get(index);
        int set=0;
        int a=temp/10,b=temp%10;
//        ArrayList sameBox =revBox.get(boxi[a][b]);
        ArrayList <Integer>poss=possi.get(temp);
        populate (poss,a,b);
      //  System.out.println(poss);
        while(!poss.isEmpty()){
            
            x[a][b]=poss.get(0);
            poss.remove(0);
            col.get(b).add(x[a][b]);
            row.get(a).add(x[a][b]);
            box.get(boxi[a][b]).add(x[a][b]);
            
           if(index==vacancy.size()-1){
                System.out.println("yo bud");
                
                col.get(b).remove(x[a][b]);
                row.get(a).remove(x[a][b]);
                box.get(boxi[a][b]).remove(x[a][b]);
                count++;
                return 1;
                
            }
            
            mSolver(x,vacancy,index+1);
               if(count>1){
                return 3;
              }
           
//            if(t==2)
//            {
//                System.out.println("yo bud");
//                return 2;
//            }
//            else if(t==1 && set==0)
//            {
//                set=1;
//               
//            }
//            else if(t==1 && set==1)
//            {
//                //System.out.println("yo bud");
//                return 2;
//            }
//            
//                    
            
            
            col.get(b).remove(x[a][b]);
            row.get(a).remove(x[a][b]);
            box.get(boxi[a][b]).remove(x[a][b]);
            
        }
        
       
      
       
        return 3;
            
      //  else
      //      return set;
    }
    public static int solver(int x[][],ArrayList<Integer> vacancy,int index){
        
        int temp= vacancy.get(index);
        int set=0;
        int a=temp/10,b=temp%10;
        ArrayList <Integer>poss=possi.get(temp);
        populate (poss,a,b);
        while(!poss.isEmpty()){
            
            x[a][b]=poss.get(0);
            poss.remove(0);
            col.get(b).add(x[a][b]);
            row.get(a).add(x[a][b]);
            box.get(boxi[a][b]).add(x[a][b]);
           if(index==vacancy.size()-1){
               System.out.println("yoo");
               col.get(b).remove(x[a][b]);
                row.get(a).remove(x[a][b]);
                box.get(boxi[a][b]).remove(x[a][b]);
                count++;
                return 1;
                
            }
            
            int t=solver(x,vacancy,index+1);
            
            if(t==1)
                return 1;
           
                    
            
            
            col.get(b).remove(x[a][b]);
            row.get(a).remove(x[a][b]);
            box.get(boxi[a][b]).remove(x[a][b]);
            
        }
        
            return 3;
    
    }
    public static void preProcess(int x[][]){
       col.clear();
        row.clear();
        box.clear();
        possi.clear();
        initialize();
            for(int a=0;a<9;a++){
                for(int b=0;b<9;b++){
                    if(x[a][b]!=0){
                        col.get(b).add(x[a][b]);
                        row.get(a).add(x[a][b]);
                        box.get(boxi[a][b]).add(x[a][b]);
                    }
                }
            }
            
    }
    
            
}