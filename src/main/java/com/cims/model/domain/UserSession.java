/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cims.model.domain;

/**
 *
 * @author gggob
 */
public class UserSession {
     private UserRole user;
     private final String userName = "unknown";
     public UserSession(UserRole user){
         this. user = user;
     }
     
     public UserRole getRole()
     {
         return this.user;
     }
     public String getUserName()
     {
         return this.userName;
     }
}
