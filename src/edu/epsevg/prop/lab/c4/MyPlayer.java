/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.epsevg.prop.lab.c4;

/**
 *
 * @author maryx
 */
public class MyPlayer
implements Jugador, IAuto{
    
    private String nom;
    private int profun;
    
    public MyPlayer(int profunditat){
        nom = "CodeConnect4";
        profun = profunditat;
    } 
    @Override
    public int moviment(Tauler t, int color) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String nom() {
        return nom;
    }
    
}
