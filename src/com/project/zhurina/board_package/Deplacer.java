package com.project.zhurina.board_package;

import com.project.zhurina.piece_package.Piece;
import com.project.zhurina.board_package.Board.*;
import com.project.zhurina.piece_package.Pion;
import com.project.zhurina.piece_package.Tour;

public abstract class Deplacer {
    //où on deplace
    protected final Board board;
    //qui on deplace
    protected final Piece piece;
    //où exactement on deplace
    protected final int coordonnee;
    protected final boolean premierPas;
    public static final Deplacer RIEN = new ImpossibleDeplace();

    private Deplacer(final Board board, final Piece piece, final int coordonnee) {
        this.board = board;
        this.piece = piece;
        this.coordonnee = coordonnee;
        this.premierPas = piece.premierPas();
    }

    private Deplacer(Board board, int coordonnee){//под вопросом
        this.board=board;
        this.coordonnee=coordonnee;
        this.piece=null;
        this.premierPas=false;
    }
    @Override
    public boolean equals(final Object auther){
        if(this == auther){return true;}
        if(!(auther instanceof Deplacer)){return false;}
        final Deplacer autherD = (Deplacer) auther;
        return getCoordonneeCur()==autherD.getCoordonneeCur() && getCoordonnee()==autherD.getCoordonnee() && getPieceDeplacee().equals(autherD.getPieceDeplacee());
    }
    @Override
    public int hashCode(){
        int result = 1;
        result = 31*result+this.coordonnee;
        result = 31*result+this.piece.hashCode();
        result = 31*result+this.piece.getPosition();
        return result;
    }
    public int getCoordonnee() {
        return this.coordonnee;
    }//getDestinationCoordonnee
    public Piece getPieceDeplacee(){
        return this.piece;
    }//getMovedPiece
    public int getCoordonneeCur() {
        return this.getPieceDeplacee().getPosition();
    }
    public boolean estAttaque(){
        return false;
    }
    public boolean estRoqueDeplace(){
        return false;
    }
    public Piece getAttaqueP(){
        return null;
    }

    public Board executer() {//ТУТ ДОЛЖНО БЫТЬ!!!!!!!!!!!!!!!!!!
        //nouv board qui va être sur notre board generale
        final Constructeur constructeur = new Constructeur();
        //pour la piece active de joueur current(blanch par ex)
        for(final Piece p : this.board.joueurCur().getpieceActive()){
            //faire hashcode et equals(p)
            if(!this.piece.equals(p)){
                constructeur.setPiece(p);
            }
        }
        //pour la piece active de joueur adversaire(noir par ex)
        for (final Piece p : this.board.joueurCur().getAdversaire().getpieceActive()){
            constructeur.setPiece(p);
        }
        //deplacer la piece deplacée
        constructeur.setPiece(this.piece.deplacePiece(this));
        constructeur.setnouvDeplace(this.board.joueurCur().getAdversaire().getColor());//setMoveMaker
        return constructeur.creer();
    }


    /////////////////////////////////////////////////////////////////////////
    // Deplacement de piece plus puissante
    ////////////////////////////////////////////////////////////////////////
    public static class PieceMajeure extends Deplacer{
        public PieceMajeure(final Board board, final Piece piece, final int coordonnee) {
            super(board, piece, coordonnee);
        }

        @Override
        public boolean equals(Object other){
            return this == other || other instanceof PieceMajeure && super.equals(other);
        }
        @Override
        public String toString(){
            return piece.getPieceType().toString() + Utilitaires.getPosition(this.coordonnee);
        }
    }
    /////////////////////////////////////////////////////////////////////////
    // Deplacement pour attaquer
    ////////////////////////////////////////////////////////////////////////
    public static class DeplaceAttaque extends Deplacer{
        final Piece pAttaquee;
        public DeplaceAttaque(final Board board, final Piece piece, final Piece pAttaquee, final int coordonnee) {
            super(board, piece, coordonnee);
            this.pAttaquee=pAttaquee;
        }
        //A FAIRE
        @Override
        public Board executer() {
            return null;
        }
        @Override
        public boolean equals(final Object auther){//
            if(this == auther){return true;}
            if(!(auther instanceof DeplaceAttaque)){return false;}
            final DeplaceAttaque autherA = (DeplaceAttaque) auther;
            return super.equals(autherA) && getAttaqueP().equals(autherA.getAttaqueP());
        }
        @Override
        public int hashCode(){
            return this.pAttaquee.hashCode() + super.hashCode();
        }//V
        @Override
        public boolean estAttaque(){
            return true;
        }
        @Override
        public Piece getAttaqueP(){
            return this.pAttaquee;
        }
    }
    /////////////////////////////////////////////////////////////////////////
    // Deplacement reguliers pour les pions
    ////////////////////////////////////////////////////////////////////////
    public static class PionDeplace extends Deplacer{
        private PionDeplace(final Board board, final Piece piece, final int coordonnee) {
            super(board, piece, coordonnee);
        }

        @Override
        public Board executer() {
            return null;
        }
    }
    /////////////////////////////////////////////////////////////////////////
    // Deplacement attaque pour les pions
    ////////////////////////////////////////////////////////////////////////
    public static class PionAttaque extends DeplaceAttaque{
        public PionAttaque(final Board board, final Piece piece, final Piece pAttaquee, final int coordonnee) {
            super(board, piece, pAttaquee, coordonnee);
        }
    }
    /////////////////////////////////////////////////////////////////////////
    public static class PionEnPasAttaque extends PionAttaque {
        private PionEnPasAttaque(final Board board, final Piece piece, final Piece pAttaquee, final int coordonnee) {
            super(board, piece, pAttaquee, coordonnee);
        }
    }
    /////////////////////////////////////////////////////////////////////////
    public static class PionSaut extends Deplacer{
        public PionSaut(final Board board, final Piece piece, final int coordonnee) {
            super(board, piece, coordonnee);
        }
        @Override
        public Board executer(){
            final Constructeur constructeur = new Constructeur();
            //pour les pions de joueyr current
            for(final Piece p : this.board.joueurCur().getpieceActive()){
                if(!this.piece.equals(p)){
                    constructeur.setPiece(p);
                }
            }
            //pour les pion de adversaire
            for(final Piece p : this.board.joueurCur().getAdversaire().getpieceActive()){
                    constructeur.setPiece(p);
            }
            //pion deplacée
            final Pion pionD = (Pion) this.piece.deplacePiece(this);
            constructeur.setPiece(pionD);
            constructeur.setEnPasPion(pionD);
            constructeur.setnouvDeplace(this.board.joueurCur().getAdversaire().getColor());
            return constructeur.creer();
        }

    }
    /////////////////////////////////////////////////////////////////////////
    // Déménagement roque(рокировка)
    ////////////////////////////////////////////////////////////////////////
    static abstract class RoqueDeplace extends Deplacer{
        protected final Tour tourR;
        //coordonnees de tour en position initiale
        protected final int tourCoorD;
        //les coordonnees de tour
        protected final int tourCoor;
        private RoqueDeplace(final Board board, final Piece piece, final int coordonnee, final Tour tourR, final int tourCoor,final int tourCoorD) {
            super(board, piece, coordonnee);
            this.tourR = tourR;
            this.tourCoor = tourCoor;
            this.tourCoorD = tourCoorD;
        }
        public Tour getTourR(){
            return this.tourR;
        }
        @Override
        public boolean estRoqueDeplace(){
            return true;
        }
        @Override
        public Board executer(){
            final Constructeur constructeur= new Constructeur();
            //pour les pieces de joueyr current
            for(final Piece p : this.board.joueurCur().getpieceActive()){
                if(!this.piece.equals(p) && !this.tourR.equals(p)){
                    constructeur.setPiece(p);
                }
            }
            //pour les pieces de adversaire
            for(final Piece p : this.board.joueurCur().getAdversaire().getpieceActive()){
                constructeur.setPiece(p);
            }

            constructeur.setPiece(this.piece.deplacePiece(this));
            //TODO БЛЯТЬ
            constructeur.setPiece(new Tour(this.tourR.getColor(), this.tourCoorD));//, false
            constructeur.setnouvDeplace(this.board.joueurCur().getAdversaire().getColor());
            return constructeur.creer();
        }
    }
    ////////////////////////////////////////////////////////////////////////
    public static class RoiRoqueDeplace extends RoqueDeplace {
        public RoiRoqueDeplace(final Board board, final Piece piece, final int coordonnee, final Tour tourR, final int tourCoor, final int tourCoorD) {
            super(board, piece, coordonnee, tourR, tourCoor, tourCoorD);
        }
        @Override
        public String toString(){
            return "O-O";
        }
    }
    ////////////////////////////////////////////////////////////////////////
    public static class DameRoqueDeplace extends RoqueDeplace {
        public DameRoqueDeplace(final Board board, final Piece piece, final int coordonnee, final Tour tourR, final int tourCoor, final int tourCoorD) {
            super(board, piece, coordonnee, tourR, tourCoor, tourCoorD);
        }
        @Override
        public String toString(){
            return "O-O-O";
        }
    }
    /////////////////////////////////////////////////////////////////////////
    // Deplacement impossible
    ////////////////////////////////////////////////////////////////////////
    public static class ImpossibleDeplace extends Deplacer{//NullMove
        private ImpossibleDeplace() {
            super(null, -1);
        }
        @Override
        public Board executer(){
            throw new RuntimeException("Pas possible d'executer null deplacement.");
        }
    }
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    // Deplacement impossible
    ////////////////////////////////////////////////////////////////////////

    public static class DeplaceUsine{
        private static final Deplacer PAS_DEPLACES = new ImpossibleDeplace();
        private DeplaceUsine(){
            throw new RuntimeException("Pas possible de creer.");
        }
        public static Deplacer creerDeplace(final Board board, final int coordonneeCur, final int coordonnee){
            for(final Deplacer deplacer : board.getToutdeplacePossible()){
                if(deplacer.getCoordonneeCur()==coordonneeCur && deplacer.getCoordonnee()==coordonnee){
                    return deplacer;
                }
            }
            return RIEN;
        }

        public static Deplacer getNullDeplace() {
            return PAS_DEPLACES;
        }
    }



}
