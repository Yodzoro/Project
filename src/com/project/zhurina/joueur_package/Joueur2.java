package com.project.zhurina.joueur_package;

import com.google.common.collect.ImmutableList;
import com.project.zhurina.Color;
import com.project.zhurina.board_package.Board;
import com.project.zhurina.board_package.Cellule;
import com.project.zhurina.board_package.Deplacer;
import com.project.zhurina.board_package.Deplacer.*;
import com.project.zhurina.piece_package.Piece;
import com.project.zhurina.piece_package.Tour;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Joueur2 extends Joueur{
    //pour cette joueur les deplacements possibles sont avec pieces noires
    public Joueur2(final Board board, final Collection<Deplacer> deplaceB, final Collection<Deplacer> depalceN) {
        super(board, depalceN, deplaceB);
    }

    @Override
    public Collection<Piece> getpieceActive() {
        return this.board.getPieceN();
    }

    @Override
    public Color getColor() {
        return Color.NOIR;
    }

    @Override
    public Joueur getAdversaire() {
        return this.board.joueur1();
    }
    @Override
    protected Collection<Deplacer> roiRoque(final Collection<Deplacer> joueurPossible, final Collection<Deplacer> adversairePossible) {
        final List<Deplacer> roiR = new ArrayList<>();

        if(this.joueurRoi.premierPas() &&!this.echec()){
            //la possition en debut de roi noir est 4
            //si les cellules 5 et 6 sont libres
            if(!this.board.getCellule(5).celluleOccupee() && !this.board.getCellule(6).celluleOccupee()){
                //cellule 7 - la position de tour à cote droite
                final Cellule tourCellule = this.board.getCellule(7);
                if(tourCellule.celluleOccupee() && tourCellule.getPiece().premierPas()){
                    if (Joueur.attaqueCellule(5,adversairePossible).isEmpty() && Joueur.attaqueCellule(6,adversairePossible).isEmpty() && tourCellule.getPiece().getPieceType().estTour()){
                        //ajoute deplacement roi roque
                        roiR.add(new RoiRoqueDeplace(this.board, this.joueurRoi, 6, (Tour) tourCellule.getPiece(), tourCellule.getCelluleCoor(), 5));
                    }

                }
            }
            //les cellules à côté gauche du roi
            if(!this.board.getCellule(1).celluleOccupee() && !this.board.getCellule(2).celluleOccupee() && !this.board.getCellule(3).celluleOccupee()){
                //cellule 0 - la position de tour à cote gauche
                final Cellule tourCellule = this.board.getCellule(0);
                if(tourCellule.celluleOccupee() && tourCellule.getPiece().premierPas() && Joueur.attaqueCellule(2, adversairePossible).isEmpty() && Joueur.attaqueCellule(3, adversairePossible).isEmpty() && tourCellule.getPiece().getPieceType().estTour()){
                    //ajout le deplacement dame roque
                    roiR.add(new DameRoqueDeplace(this.board, this.joueurRoi, 2, (Tour) tourCellule.getPiece(), tourCellule.getCelluleCoor(), 3));
                }
            }
        }

        return ImmutableList.copyOf(roiR);
    }
}
