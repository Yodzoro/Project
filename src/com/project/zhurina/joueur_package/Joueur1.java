package com.project.zhurina.joueur_package;

import com.google.common.collect.ImmutableList;
import com.project.zhurina.Color;
import com.project.zhurina.board_package.*;
import com.project.zhurina.board_package.Deplacer.*;
import com.project.zhurina.piece_package.Piece;
import com.project.zhurina.piece_package.Tour;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Joueur1 extends Joueur{
    //pour cette joueur les deplacements possibles sont avec pieces blanches
    public Joueur1(final Board board, final Collection<Deplacer> deplaceB,final Collection<Deplacer> deplaceN) {
        super(board, deplaceB, deplaceN);
    }
    @Override
    public Collection<Piece> getpieceActive(){
        return this.board.getPieceB();
    }

    @Override
    public Color getColor() {
        return Color.BLANCH;
    }

    @Override
    public Joueur getAdversaire() {
        return this.board.joueur2();
    }

    @Override
    protected Collection<Deplacer> roiRoque(final Collection<Deplacer> joueurPossible, final Collection<Deplacer> adversairePossible) {
        final List<Deplacer> roiR = new ArrayList<>();

        if(this.joueurRoi.premierPas() &&!this.echec()){
            //la possition en debut de roi blanch est 60
            //si les cellules 61 et 62 sont libres
            if(!this.board.getCellule(61).celluleOccupee() && !this.board.getCellule(62).celluleOccupee()){
                //cellule 63 - la position de tour à cote droite
                final Cellule tourCellule = this.board.getCellule(63);
                if(tourCellule.celluleOccupee() && tourCellule.getPiece().premierPas()){
                    if (Joueur.attaqueCellule(61,adversairePossible).isEmpty() && Joueur.attaqueCellule(62,adversairePossible).isEmpty() && tourCellule.getPiece().getPieceType().estTour()){
                        //ajout le deplacement roi roque
                        roiR.add(new RoiRoqueDeplace(this.board, this.joueurRoi, 62, (Tour) tourCellule.getPiece(), tourCellule.getCelluleCoor(), 61));
                    }

                }
            }
            //les cellules à côté gauche du roi
            if(!this.board.getCellule(59).celluleOccupee() && !this.board.getCellule(58).celluleOccupee() && !this.board.getCellule(57).celluleOccupee()){
                //cellule 63 - la position de tour à cote gauche
                final Cellule tourCellule = this.board.getCellule(56);
                if(tourCellule.celluleOccupee() && tourCellule.getPiece().premierPas() && Joueur.attaqueCellule(58, adversairePossible).isEmpty() && Joueur.attaqueCellule(59, adversairePossible).isEmpty() && tourCellule.getPiece().getPieceType().estTour()){
                    //ajout le deplacement dame roque
                    roiR.add(new DameRoqueDeplace(this.board, this.joueurRoi, 58, (Tour) tourCellule.getPiece(), tourCellule.getCelluleCoor(), 59));
                }
            }
        }

        return ImmutableList.copyOf(roiR);
    }
}
