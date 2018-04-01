/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

/**
 *
 * @author lakshkotian
 */
public class User {
    String userName,easy,diff;
    int min ,sec;
    User(String s1){
        userName=s1;
        
    }
    User(String s1, String s2,String s3){
        userName = s1;
        easy = s2;
        diff = s3;
    }

    
}
