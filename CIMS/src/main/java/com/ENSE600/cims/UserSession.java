/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ENSE600.cims;

/**
 *
 * @author gggob
 */
public class UserSession {
     private UserRole user;
     
     public UserSession(UserRole user){
         this. user = user;
     }
     
     public UserRole getRole()
     {
         return this.user;
     }
}
