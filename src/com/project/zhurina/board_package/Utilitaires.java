package com.project.zhurina.board_package;

import java.util.*;

public class Utilitaires {
    //exeption pour les certaines colonnes
    public static final int NUM_CELLULES = 64;
    public static final int NUM_COLONNE = 8;
    public static final List<String> NOTATION = notations();
    public static final Map<String, Integer> COORDONNES = coodonneeMap();



    //les coordonnes [A1, A2, A3, A4, A5, A6, A7, A8] = true pour premier colonne
    public static final boolean[] PREMIER_COLONNE = colonne(0);
    //les coordonnes [B1, B2, B3, B4, B5, B6, B7, B8] = true pour deuxieme colonne
    public static final boolean[] DEUXIEME_COLONNE = colonne(1);
    //les coordonnes [G1, G2, G3, G4, G5, G6, G7, G8] = true pour septieme colonne
    public static final boolean[] SEPTIEME_COLONNE = colonne(6);
    //les coordonnes [H1, H2, H3, H4, H5, H6, H7, H8] = true pour huitieme colonne
    public static final boolean[] HUITIEME_COLONNE = colonne(7);


    //pour les pion on doit declarer les lignes où ils se trouvent
    //les coordonnes [A8, B8, C8, D8, E8, F8, G8, H8] = true pour huitieme ligne
    public static final boolean[] HUITIEME_LIGNE = ligne(0);
    //les coordonnes [A7, B7, C7, D7, E7, F7, G7, H7] = true pour septieme ligne
    public static final boolean[] SEPTIEME_LIGNE = ligne(8);
    //les coordonnes [A6, B6, C6, D6, E6, F6, G6, H6] = true pour sixieme ligne
    public static final boolean[] SIXIEME_LIGNE = ligne(16);
    //les coordonnes [A5, B5, C5, D5, E5, F5, G5, H5] = true pour cinguieme ligne
    public static final boolean[] CINQUIEME_LIGNE = ligne(24);
    //les coordonnes [A4, B4, C4, D4, E4, F4, G4, H4] = true pour quatrieme ligne
    public static final boolean[] QUATRIEME_LIGNE = ligne(32);
    //les coordonnes [A3, B3, C3, D3, E3, F3, G3, H3] = true pour troisieme ligne
    public static final boolean[] TROISIEME_LIGNE = ligne(40);
    //les coordonnes [A2, B2, C2, D2, E2, F2, G2, H2] = true pour deuxieme ligne
    public static final boolean[] DEUXIEME_LIGNE = ligne(48);
    //les coordonnes [A1, B1, C1, D1, E1, F1, G1, H1] = true pour premiere ligne
    public static final boolean[] PREMIER_LIGNE = ligne(56);

    private static boolean[] colonne(int num){
        //on a 64 cellules et 8 colonnes
        final boolean[] c = new boolean[NUM_CELLULES];
        do{
            //on  divise les cellulse en 8 colonnes
            c[num] = true;
            num+=NUM_COLONNE;
        }while (num<NUM_CELLULES);
        return c;
    }
    private static boolean[] ligne(int num){
        //on a 64 cellules et 8 colonnes
        final boolean[] l = new boolean[NUM_CELLULES];
        do{
            //on  divise les cellulse en 8 colonnes
            l[num] = true;
            num++;
        }while (num % NUM_COLONNE != 0);
        return l;
    }

    public Utilitaires() {
        throw new RuntimeException("Pas possible de créer.");
    }
    public static boolean coordonneePossible(final int coordonnee) {
        return coordonnee >= 0 && coordonnee < NUM_CELLULES;
    }

    public static int getCoordonnee(String coordonnee) {
        return COORDONNES.get(coordonnee);
    }
    public static String getPosition(int position){
        return NOTATION.get(position);
    }
    private static List<String> notations() {
        return Collections.unmodifiableList(Arrays.asList("a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
                "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
                "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
                "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
                "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
                "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
                "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
                "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"));
    }
    private static Map<String, Integer> coodonneeMap() {
        final Map<String, Integer> coordonnne = new HashMap<>();
        for(int i = 0; i<NUM_CELLULES; i++){
            coordonnne.put(NOTATION.get(i), i);
        }
        return Collections.unmodifiableMap(coordonnne);
    }

}
