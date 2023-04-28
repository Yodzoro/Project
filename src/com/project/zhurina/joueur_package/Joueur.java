package com.project.zhurina.joueur_package;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.project.zhurina.Color;
import com.project.zhurina.board_package.Board;
import com.project.zhurina.board_package.Deplacer;
import com.project.zhurina.piece_package.Roi;
import com.project.zhurina.piece_package.Piece;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public abstract class Joueur {
    protected final Board board;
    protected final Roi joueurRoi;
    protected final Collection<Deplacer> deplacePossible;//legalMoves
    private final boolean echec;

    //constructeur avec board, où on jou, deplacement possible et les deplacement de l'adversaire
    public Joueur(Board board, Collection<Deplacer> deplacePossible, Collection<Deplacer> deplaceAdver) {
        this.board = board;
        this.joueurRoi = posRoi();
        //concat les deplacement pour deplacePossible et les deplacement de roi Roque
        this.deplacePossible = ImmutableList.copyOf(Iterables.concat(deplacePossible, roiRoque(deplacePossible, deplaceAdver)));//this.deplacePossible = deplacePossible;
        //si les deplacement d'advairsaire sont les attaques sur le Roi de joueur current et s'ils ne sont pas vide => joueur current est en echec
        this.echec = !Joueur.attaqueCellule(this.joueurRoi.getPosition(), deplaceAdver).isEmpty();

    }

    protected static Collection<Deplacer> attaqueCellule(int position, Collection<Deplacer> deplaces) {//V
        //liste de deplacement qui attaque le roi
        final List<Deplacer> attaques = new ArrayList<>();
        for(final Deplacer deplace : deplaces){
            if(position==deplace.getCoordonnee()){//getDestinationCoordonnee
                attaques.add(deplace);
            }
        }
        return ImmutableList.copyOf(attaques);
    }

    private Roi posRoi() {//establishKing
        for(final Piece p : getpieceActive()){
            if(p.getPieceType().estRoi()){
                return (Roi) p;
            }
        }
        throw new RuntimeException("Pas sur le board.");
    }
    //pour savoir si nos deplacement sont possible où pas
    public boolean deplacementPossible(final Deplacer deplacer){//isLegalMove
        return this.deplacePossible.contains(deplacer);
    }

    public boolean echec(){//шах
        return this.echec;
    }
    public boolean echecMat(){//шах и мат
        return this.echec && !echapper();
    }
    public boolean impasse(){//тупик//isInStaleMate
        return !this.echec && !echapper();
    }

    //methode qui indique que la piece peut echapper
    protected boolean echapper() {//hasEscapeMoves //V
        for (final Deplacer deplace : this.deplacePossible){
            //les deplacement hypothétiques pour regarder où on peut aller
            final DeplaceTrans trans = faireDeplace(deplace);
            if(trans.getDeplaceEtat().estFait()){
                return true;
            }
        }
        return false;
    }
    //TODO ATTANTION
    public boolean roque(){//рокировка, ход когда одновременно ставят короля и лодью по разные стороны.//isCastled
        return false;
    }

    public DeplaceTrans faireDeplace(final Deplacer deplace){//makeMove
        //si le deplacement n'est pas possible
        if(!deplacementPossible(deplace)){
            return new DeplaceTrans(this.board, deplace, DeplaceEtat.IMPOSSIBLE);
        }
        final Board transBoard = deplace.executer();
        final Collection<Deplacer> attaqueRoi = Joueur.attaqueCellule(transBoard.joueurCur().getAdversaire().getJoueurRoi().getPosition(), transBoard.joueurCur().getdeplacementPossible());

        if(!attaqueRoi.isEmpty()){
            return new DeplaceTrans(this.board, deplace, DeplaceEtat.SOUS_ECHEC);
        }

        return new DeplaceTrans(transBoard, deplace, DeplaceEtat.FAIT);
    }

    public Collection<Deplacer> getdeplacementPossible() {
        return this.deplacePossible;
    }//getlegalmoves

    private Roi getJoueurRoi() {
        return this.joueurRoi;
    }

    public abstract Collection<Piece> getpieceActive();
    public abstract Color getColor();
    //advairsaires de joueurs
    public abstract Joueur getAdversaire();
    //collection des deplacemnts pour roque
    protected abstract Collection<Deplacer> roiRoque(Collection<Deplacer> joueurPossible, Collection<Deplacer> adversairePossible);
}
