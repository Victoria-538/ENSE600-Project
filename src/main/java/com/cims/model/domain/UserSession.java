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
     private UserRole role;
     private final String userName;
     public UserSession(UserRole user, String name){
         this. role = user;
         this.userName = name;
     }
     
     public UserRole getRole()
     {
         return this.role;
     }
     public String getUserName()
     {
         return this.userName;
     }
}
