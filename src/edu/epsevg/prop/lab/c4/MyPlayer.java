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
    private int mytorn = 1;
    
    public MyPlayer(int profunditat){
        nom = "CodeConnect4";
        profun = profunditat;
    } 
    @Override
    public int moviment(Tauler t, int color) {
        mytorn = color;
        int col = 0;
        int value = Integer.MIN_VALUE;
        for(int i = 0; i < t.getMida(); i++){
                if(t.movpossible(i)){
                    Tauler newtable= new Tauler(t);
                    newtable.afegeix(i,mytorn);
                   int newval = minimax(newtable,Integer.MIN_VALUE,Integer.MAX_VALUE, profun-1, false, i);
                   if(newval > value){
                       value = newval;
                       col = i;
                   }
                }
            }
        
        return col;
    }
    
    /**
     * Devuelve el valor que tiene menor/mayor heuristica con el movimiento del jugador
     * @param table copia de la tabla con la partida actual
     * @param alfa  alpha para hacer la poda de alfa-beta
     * @param beta  beta para hacer la poda de alfa-beta
     * @param profunditat Indica la profundidad maxima del arbol 
     * @param maxPlayer Indica si estamos con nuestro jugador o el oponente
     * @param jugAnt  Contiene la jugada que se ha hecho anteriormente 
     * @return  Valor con la heuristica mayor 
     */
    public int minimax(Tauler table,int alfa, int beta, int profunditat, boolean maxPlayer, int jugAnt){
        
        /*Comprobamos si con la jugada anterior y nuestro jugador ya hemos encontrado
        una solucion, si es asi, devuelve +infinito*/
        if(table.solucio(jugAnt, mytorn)){
            return Integer.MAX_VALUE-83647;
        
        /*En este caso, comprobamos si el oponente ya encontro una solución, es decir gano
            En este caso, devolvemos -infinito*/    
        }else if(table.solucio(jugAnt, -(mytorn))){
            return Integer.MIN_VALUE+83647;
            
        /*O si hemos llegado a profundidad 0 o si ya esta lleno la tabla actual y entonces 
            Llamamos a nuestra heuristica*/
        }else if(profunditat == 0 || (!(table.espotmoure()))){
            return 0; //Se llamaria a la heuristica 
        }
        
        /*Turno de nuestro jugador*/
        if(maxPlayer){
            int value = Integer.MIN_VALUE;;
            for(int i = 0; i < table.getMida(); i++){
                if(table.movpossible(i)){
                    Tauler newtable= new Tauler(table);
                    newtable.afegeix(i,mytorn);
                   int newval = minimax(newtable,alfa, beta, profunditat-1, false, i);
                   if(newval > value){
                       value = newval;
                   }
                   alfa = Math.max(value, alfa);
                   if(alfa>=beta) break;
                }
            }
           return value; 
        }else{ //Turno del oponente 
            int value = Integer.MAX_VALUE;
            for(int i = 0; i < table.getMida(); i++){
                if(table.movpossible(i)){
                    Tauler newtable= new Tauler(table);
                    newtable.afegeix(i,-(mytorn));
                    
                   int newval = minimax(newtable,alfa,beta, profunditat-1, true, i);
                   if(newval < value){
                       value = newval;
                   }
                   beta = Math.min(value, beta);
                   if(alfa>=beta) break;
                }
            }
            return value;
        }
    }
    

    @Override
    public String nom() {
        return nom;
    }
    
}
