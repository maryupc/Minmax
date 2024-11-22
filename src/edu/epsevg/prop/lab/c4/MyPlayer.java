/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.epsevg.prop.lab.c4;

/**
 * Nuestro propio jugador
 * "CodeConnect4"
 * @author maryx Mrmar
 */
public class MyPlayer
implements Jugador, IAuto{
    
    private String nom;
    private int profun;
    private int mytorn = 1;
    private int num_jug = 0;
    private boolean activa_poda = true;
    private int orden[];
    private boolean orden_mig = false;
    
    /**
     * Definicion de nuestro jugador 
     * @param profunditat Profundidad pasada por el constructor que se quiere aplicar
     * @param poda Booleano que decide si se hace uso de poda alfa-beta o no
     * @param medio indica si queremos que se empiece por el medio a añadir las fichas, o a partir de la izq del todo
     */
    public MyPlayer(int profunditat, boolean poda, boolean medio){
        nom = "CodeConnect4";
        profun = profunditat;
        activa_poda = poda;
        orden_mig = medio;
        
        //Creamos array que contiene el orden que debera seguir, si empezamos por el medio 
        orden = new int[8]; //Array que contiene el número de columnas del tablero
        boolean right = true; //indicamos si empezamos a ir a la izq o a la derecha a contar 
        int aux = 4;
        for(int i = 0; i < 8; i++){
            if(right){
                aux +=i;
                right = false;
            }else{
                aux -=i;
                right = true;
            }
            orden[i] = aux;
        }
       
    } 
    
    /**
     * Función que calcula el mejor movimiento del jugador con tal de poder ganar a su oponente  
     * @param t  contiene el tablero de la jugada actual 
     * @param color tiene el color de la ficha que pertenece a nuestro jugador
     * @return devuelve la columna que se ha decidido en base del minmax y la heuristica con tal de ganar
     */
    @Override
    public int moviment(Tauler t, int color) {
        mytorn = color;
        int col = 0;
        int value = Integer.MIN_VALUE;
        int aux = 0;
        for(int i = 0; i < t.getMida(); i++){
            if(orden_mig) aux = orden[i];
            else aux = i;
                if(t.movpossible(aux)){
                    Tauler newtable= new Tauler(t);
                    newtable.afegeix(aux,mytorn);
                   int newval = minimax(newtable,value,Integer.MAX_VALUE, profun-1, false, aux);
                   if(newval > value){
                       value = newval;
                       col = aux;
                   }
                }
                
            }
        System.out.println("Número de jugades finals explorades: " + num_jug);
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
        int aux = 0;
        /*Comprobamos si con la jugada anterior y nuestro jugador ya hemos encontrado
        una solucion, si es asi, devuelve +infinito*/
        if(table.solucio(jugAnt, mytorn)){
            return Integer.MAX_VALUE-83647;
        
        /*En este caso, comprobamos si el oponente ya encontro una solución, es decir gano
            En este caso, devolvemos -infinito*/    
        }else if(table.solucio(jugAnt, -(mytorn))){
            return Integer.MIN_VALUE+83647;
            
        /*Si hemos llegado a profundidad máx o si ya esta lleno la tabla actual, entonces 
            Llamamos a nuestra heuristica*/
        }else if(profunditat == 0 || (!(table.espotmoure()))){
           return calculaHeuristica(table);
        }
        
        /*Turno de nuestro jugador*/
        if(maxPlayer){
            int value = Integer.MIN_VALUE;;
            
            for(int i = 0; i < table.getMida(); i++){
                if(orden_mig) aux = orden[i];
                else aux = i;
                if(table.movpossible(aux)){
                    Tauler newtable= new Tauler(table);
                    newtable.afegeix(aux,mytorn);
                   int newval = minimax(newtable,alfa, beta, profunditat-1, false,aux);
                   if(newval > value){
                       value = newval;
                   }
                   if(activa_poda){
                        alfa = Math.max(value, alfa);
                        if(alfa>=beta) break;
                   }

                }
            }
           return value; 
        }else{ //Turno del oponente 
            int value = Integer.MAX_VALUE;
            
            for(int i = 0; i < table.getMida(); i++){
                if(orden_mig) aux = orden[i];
                else aux = i;
                if(table.movpossible(aux)){
                    Tauler newtable= new Tauler(table);
                    newtable.afegeix(aux,-(mytorn));
                   int newval = minimax(newtable,alfa,beta, profunditat-1, true,aux);
                   if(newval < value){
                       value = newval;
                   }
                   if(activa_poda){
                       beta = Math.min(value, beta);
                       if(alfa>=beta) break;
                   }

                }
            }
            return value;
        }
    }
    
     /**
     * Calcula la puntuación total del tablero combinando todas las heurísticas.
     * @param tauler Estado actual del tablero
     * @return Puntuación total del tablero
     */
    public int calculaHeuristica(Tauler tauler) {
        ++num_jug;
        //Calculamos todas las heurisiticas posibles(horizontales, verticales y las 2 diagonales)
        return heuristicaHorizontal(tauler) +
               heuristicaVertical(tauler) +
               heuristicaDiagonalED(tauler) +
               heuristicaDiagonalDE(tauler);
    }

    /**
     * Heurística para filas horizontales.
     * @param tauler Estado actual del tablero
     * @return Puntuación basada en filas horizontales
     */
    private int heuristicaHorizontal(Tauler tauler) {
        int puntuacion = 0;
        int filas = tauler.getMida();
        int columnas = tauler.getMida();
        for (int fila = 0; fila < filas; fila++) {
            //Evaluamos 4 espacios consecutivos
            for (int col = 0; col < columnas - 3; col++) {
                int contador = 0;
                 //Recorremos las 4 posiciones
                for (int offset = 0; offset < 4; offset++) {
                    int valor = tauler.getColor(fila, col + offset);
                    //Fichas del jugador suman
                    if (valor == mytorn) contador++;
                    //Fichas del oponente restan
                    else if (valor == -mytorn) contador--;
                }
                //Sumamos a la puntuación total
                puntuacion += contador;
            }
        }
        //Devolvemos
        return puntuacion;
    }

    /**
     * Heurística para columnas verticales.
     * @param tauler Estado actual del tablero
     * @return Puntuación basada en columnas verticales
     */
    private int heuristicaVertical(Tauler tauler) {
        int puntuacion = 0;
        int filas = tauler.getMida();
        int columnas = tauler.getMida();
        for (int col = 0; col < columnas; col++) {
            //Evaluamos 4 espacios consecutivos
            for (int fila = 0; fila < filas - 3; fila++) {
                int contador = 0;
                //Recorremos las 4 posiciones
                for (int offset = 0; offset < 4; offset++) {
                    int valor = tauler.getColor(fila + offset, col);
                    //Fichas del jugador suman
                    if (valor == mytorn) contador++;
                    //Fichas del oponente restan
                    else if (valor == -mytorn) contador--;
                }
                //Sumamos a la puntuación total
                puntuacion += contador;
            }
        }
        //Devolvemos
        return puntuacion;
    }

    /**
     * Heurística para diagonales de izquierda a derecha.
     * @param tauler Estado actual del tablero
     * @return Puntuación basada en diagonales izquierda-derecha
     */
    private int heuristicaDiagonalED(Tauler tauler) {
        int puntuacion = 0;
        int filas = tauler.getMida();
        int columnas = tauler.getMida();
        for (int fila = 0; fila < filas - 3; fila++) {
            //Evaluamos 4 espacios consecutivos
            for (int col = 0; col < columnas - 3; col++) {
                int contador = 0;
                //Recorremos las 4 posiciones
                for (int offset = 0; offset < 4; offset++) {
                    int valor = tauler.getColor(fila + offset, col + offset);
                    //Fichas del jugador suman
                    if (valor == mytorn) contador++;
                    //Fichas del oponente restan
                    else if (valor == -mytorn) contador--;
                }
                //Sumamos a la puntuación total
                puntuacion += contador;
            }
        }
        //Devolvemos
        return puntuacion;
    }

    /**
     * Heurística para diagonales de derecha a izquierda.
     * @param tauler Estado actual del tablero
     * @return Puntuación basada en diagonales derecha-izquierda
     */
    private int heuristicaDiagonalDE(Tauler tauler) {
        int puntuacion = 0;
        int filas = tauler.getMida();
        int columnas = tauler.getMida();
        for (int fila = 3; fila < filas; fila++) {
            //Evaluamos 4 espacios consecutivos
            for (int col = 0; col < columnas - 3; col++) {
                int contador = 0;
                //Recorremos las 4 posiciones
                for (int offset = 0; offset < 4; offset++) {
                    int valor = tauler.getColor(fila - offset, col + offset);
                    //Fichas del jugador suman
                    if (valor == mytorn) contador++;
                    //Fichas del oponente restan
                    else if (valor == -mytorn) contador--;
                }
                
                puntuacion += contador;
            }
        }
       //Devovemos
        return puntuacion;
    }

    /**
     * 
     * @return nombre de nuestro jugador 
     */
    @Override
    public String nom() {
        return nom;
    }
    
}
