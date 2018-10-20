/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

/**
 *
 * @author lakshkotian
 */
public class SudokuMaker {
         private final Random r=new Random();
         private int count=0; 
         private int arr[][]=new int [9][9];
        
         private final int boxi [][]= new int[9][9];
        
        
         private HashMap<Integer,ArrayList<Integer>> possi =new HashMap<>();
         private HashMap<Integer,HashSet<Integer>> possi2 =new HashMap<>();        
         private ArrayList<HashSet<Integer>> col=new ArrayList<>();
         private ArrayList<HashSet<Integer>> row=new ArrayList<>();
         private ArrayList<HashSet<Integer>> box=new ArrayList<>();
        
    public int [][] makeGrid(){
        initialize();
        sudoku(0,0);
        return arr;
    }
    
    private void initialize(){
        
        int count=0;
        
        for(int i=0;i<9;i++){
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
    private boolean sudoku(int a,int b){
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
    private void populate(ArrayList<Integer> h,int a,int b){
       
        h.clear();
        for(int i=1;i<10;i++){
            
            if(!col.get(b).contains(i) && !row.get(a).contains(i)
                                && !box.get(boxi[a][b]).contains(i))
                {
                    h.add(i);
                }
        }
       
       
        
    }
//     takes a grid and number of required vacancy and creates puzzle
    public  int [][] disappear(int x[][],int limit){
        int temp1,a,b,temp;
        ArrayList<Integer> seen = new ArrayList<>();
        for(int i=0;i<9;i++){
            for(int j =0;j<9;j++ ){
                seen.add((i*10)+j);
            }
            
        }
        ArrayList<Integer> vacancy = new ArrayList<>();
        while(vacancy.size()<limit){
            
            temp=seen.remove(r.nextInt(seen.size()));
            
            a=temp/10;
            b=temp%10;
            
           
            temp1=x[a][b];
            x[a][b]=0;
            
            vacancy.add(temp);
            System.out.println(vacancy.size());
            if(!solve(x,vacancy)){
                
                x[a][b]=temp1;
                vacancy.remove(new Integer(temp));
            }
            else{
// remove diagnolly opposite number to create symmetric puzzle
                temp=(8-a)*10+(8-b);
                seen.remove(new Integer(temp));
                a=8-a;
                b=8-b;
                temp1=x[a][b];
                x[a][b]=0;
                
                vacancy.add(temp);
                if(!solve(x,vacancy)){
                
                x[a][b]=temp1;
                vacancy.remove(new Integer(temp));
            }
            }
            
        }
        return x;
    }
    private  boolean solve(int x[][],ArrayList<Integer> vacancy){
        int y[][]=new int[9][9];
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                y[i][j]=x[i][j];
            }
        }
        
        preProcess(y);
            count=0;
        int temp=mSolver(y,vacancy,0);
        if( count==1)
            return true;
        else
            return false;
    }
    private int mSolver(int x[][],ArrayList<Integer> vacancy,int index){
        
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
           
 
            
            col.get(b).remove(x[a][b]);
            row.get(a).remove(x[a][b]);
            box.get(boxi[a][b]).remove(x[a][b]);
            
        }
        return 3;
            
        
    }
    
    private  void preProcess(int x[][]){
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
