package com.project.zhurina.board_package;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.project.zhurina.Color;
import com.project.zhurina.joueur_package.*;
import com.project.zhurina.piece_package.*;
import com.project.zhurina.board_package.Deplacer.*;

import java.util.*;

import static com.project.zhurina.Color.BLANCH;
import static com.project.zhurina.Color.NOIR;

public class Board {
    //liste des
    private final List<Cellule> jeuBoard;
    //collection pour les pieces blanches
    private final Collection<Piece> pieceB;
    //collection pour les pieces noires
    private final Collection<Piece> pieceN;
    private final Joueur1 joueur1;
    private final Joueur2 joueur2;
    private final Joueur joueurCur;
    private final Deplacer transDeplace;


    private Board(Constructeur construct){
        this.jeuBoard = creerBoard(construct);
        //les types de pieces sur board
        this.pieceB = pieceActive(this.jeuBoard, BLANCH);
        this.pieceN = pieceActive(this.jeuBoard, NOIR);

        //les deplacements qui on peut realiser sur board
        Collection<Deplacer> deplaceB = deplacePossible(this.pieceB);//calculateLegaleMoves
        Collection<Deplacer> depalceN = deplacePossible(this.pieceN);

        this.joueur1 = new Joueur1(this, deplaceB, depalceN);
        this.joueur2 = new Joueur2(this, deplaceB, depalceN);
        this.joueurCur = construct.nouvDeplace.choisirJoueur(this.joueur1, this.joueur2);
        this.transDeplace = construct.transDeplace != null ? construct.transDeplace : DeplaceUsine.getNullDeplace();//TODO

    }
    //pour afficher les cellules sur terminale
    @Override
    public String toString(){//V
        StringBuilder builder = new StringBuilder();
        for (int i=0; i<Utilitaires.NUM_CELLULES; i++){
            String celluleText = this.jeuBoard.get(i).toString();
            builder.append(String.format("%3s", celluleText));
            if((i+1) % Utilitaires.NUM_COLONNE==0){
                builder.append("\n");
            }
        }
        return builder.toString();
    }
    public Collection<Piece> getPieceB(){//V
        return this.pieceB;
    }//V
    public Collection<Piece> getPieceN(){//V
        return this.pieceN;
    }

    private Collection<Deplacer> deplacePossible(Collection<Piece> piece) {//calculateLegaleMoves
        List<Deplacer> deplacePos = new ArrayList<>();
        for (Piece p : piece){
            //on donne à chaque pieces ces deplacement possible en utilisant possibleDeplacer() et on les mettre dans liste avec les deplacement possible
            deplacePos.addAll(p.possibleDeplacer(this));
        }
        return ImmutableList.copyOf(deplacePos);
    }

    private static Collection<Piece> pieceActive(List<Cellule> jeuBoard, Color color) {
        List<Piece> pieceA = new ArrayList<>();
        for(Cellule c : jeuBoard){
            if(c.celluleOccupee()){
                Piece p = c.getPiece();
                if(p.getColor()== color){
                    pieceA.add(p);
                }
            }
        }
        return ImmutableList.copyOf(pieceA);
    }

    public Cellule getCellule(final int coordonneeCellule) {//V
        return jeuBoard.get(coordonneeCellule);
    }
    //ici on va creer le board de jeu
    private static List<Cellule> creerBoard(Constructeur construct) {//V
        Cellule[] c = new Cellule[Utilitaires.NUM_CELLULES];
        for(int i=0; i<Utilitaires.NUM_CELLULES; i++){
            c[i]=Cellule.craetionCellule(i, construct.boardMap.get(i));
        }
        return ImmutableList.copyOf(c);
    }
    public static Board boardDefaut(){
        Constructeur constructeur = new Constructeur();
        //Les pieces noires
        constructeur.setPiece(new Fou(NOIR, 0));
        constructeur.setPiece(new Cavalier(NOIR, 1));
        constructeur.setPiece(new Tour(NOIR, 2));
        constructeur.setPiece(new Dame(NOIR, 3));
        constructeur.setPiece(new Roi(NOIR, 4));
        constructeur.setPiece(new Tour(NOIR, 5));
        constructeur.setPiece(new Cavalier(NOIR, 6));
        constructeur.setPiece(new Fou(NOIR, 7));
        constructeur.setPiece(new Pion(NOIR, 8));
        constructeur.setPiece(new Pion(NOIR, 9));
        constructeur.setPiece(new Pion(NOIR, 10));
        constructeur.setPiece(new Pion(NOIR, 11));
        constructeur.setPiece(new Pion(NOIR, 12));
        constructeur.setPiece(new Pion(NOIR, 13));
        constructeur.setPiece(new Pion(NOIR, 14));
        constructeur.setPiece(new Pion(NOIR, 15));

        constructeur.setPiece(new Pion(BLANCH, 48));
        constructeur.setPiece(new Pion(BLANCH, 49));
        constructeur.setPiece(new Pion(BLANCH, 50));
        constructeur.setPiece(new Pion(BLANCH, 51));
        constructeur.setPiece(new Pion(BLANCH, 52));
        constructeur.setPiece(new Pion(BLANCH, 53));
        constructeur.setPiece(new Pion(BLANCH, 54));
        constructeur.setPiece(new Pion(BLANCH, 55));
        constructeur.setPiece(new Fou(BLANCH, 56));
        constructeur.setPiece(new Cavalier(BLANCH, 57));
        constructeur.setPiece(new Tour(BLANCH, 58));
        constructeur.setPiece(new Dame(BLANCH, 59));
        constructeur.setPiece(new Roi(BLANCH, 60));
        constructeur.setPiece(new Tour(BLANCH, 61));
        constructeur.setPiece(new Cavalier(BLANCH, 62));
        constructeur.setPiece(new Fou(BLANCH, 63));

        constructeur.setnouvDeplace(BLANCH);
        return constructeur.creer();

    }
    //methodes pour que la classe Joueur povait vaoire qui est l'adversaire de qui
    public Joueur joueur1() {
        return this.joueur1;
    }
    public Joueur joueur2() {
        return this.joueur2;
    }
    public Joueur joueurCur() {
        return this.joueurCur;
    }

    public Iterable<Deplacer> getToutdeplacePossible() {
        return Iterables.unmodifiableIterable(Iterables.concat(this.joueur1.getdeplacementPossible(), this.joueur2.getdeplacementPossible()));
    }

    public static class Constructeur{
        //board va être creer en manier de pile, donc chaque nouv linge est le contenue de pile(ex : A8 = 0 et A7 = 8)
        Map<Integer, Piece> boardMap;
        Color nouvDeplace;
        Pion pionEnPas;
        Deplacer transDeplace;
        public Constructeur(){
            this.boardMap = new HashMap<>();
        }

        public Constructeur setPiece(final Piece piece){//V
            this.boardMap.put(piece.getPosition(),piece);
            return this;
        }
        public Constructeur setnouvDeplace(Color nouvDeplace){//V setMoveMaker
            this.nouvDeplace=nouvDeplace;
            return this;
        }
        public Constructeur setDeplaceTans(Deplacer transDeplace){
            this.transDeplace = transDeplace;
            return this;
        }
        public void setEnPasPion(Pion pionEnPas) {
            this.pionEnPas=pionEnPas;
        }
        public Board creer(){
            return new Board(this);
        }
    }
}
