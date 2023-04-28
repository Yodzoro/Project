package com.project.zhurina.piece_package;

import com.google.common.collect.ImmutableList;
import com.project.zhurina.Color;
import com.project.zhurina.board_package.Board;
import com.project.zhurina.board_package.Deplacer;
import com.project.zhurina.board_package.Deplacer.*;
import com.project.zhurina.board_package.Utilitaires;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class Pion extends Piece{//PAWN
    //pour pion deplacement est different, il peut être attaqué(1 deplacement diagonal) et regulière(1-2 (8 et 16) deplacements verticals)
    private final int[] DEPLACEMENT_POTENTIEL = {8, 7, 9, 16};
    public Pion( Color color, int position) {
        super(Type.PION, color, position, true);
    }
    public Pion(Color color, int position, boolean premierPas){
        super(Type.PION,color, position, premierPas);
    }
    @Override
    public Collection<Deplacer> possibleDeplacer(final Board board) {
        //liste avec les deplacementes possibles
        final List<Deplacer> deplacePossible = new ArrayList<>();

        for (final int positionCur : DEPLACEMENT_POTENTIEL){
            //ici on choisit la direction pour pion, mais pour cela on doit connetre son couleur
            //donc pour pion blanch on a les direction negative(A2->A3)
            //si pion noit on a les direction positives(A7->A6)
            int coordonneeDestination = this.position+(this.color.getDrection() * positionCur);

            if(!Utilitaires.coordonneePossible(coordonneeDestination)){
                continue;
            }
            //si nous somme sur possition 8 et cellule suivante n'est pas occupée
            if(positionCur == 8  && !board.getCellule(coordonneeDestination).celluleOccupee()){
                deplacePossible.add(new PieceMajeure(board, this, coordonneeDestination));
            }//si c'est 1 pas on a deplacé sur 2 cellule pour cette pas
            else if(positionCur == 16 && this.premierPas() && (Utilitaires.SEPTIEME_LIGNE[this.position] && this.getColor().estNoir()) || (Utilitaires.DEUXIEME_LIGNE[this.position] && this.getColor().estBlanch())){
                //les coordonnees de derriere cellule
                int coordonneeDerriere = this.position + (this.color.getDrection()*8);
                //si la derriere et prochaine cellules  ne sont pas occupées
                if(!board.getCellule(coordonneeDerriere).celluleOccupee() && !board.getCellule(coordonneeDestination).celluleOccupee()){
                    deplacePossible.add(new PionSaut(board, this, coordonneeDestination));

                }
            }//pour les attaques
            //pour attaque à coté droite et les barieres pour ne pas dépasser le board
            else if(positionCur == 7 && !((Utilitaires.HUITIEME_COLONNE[this.position]&& this.color.estBlanch()) || (Utilitaires.PREMIER_COLONNE[this.position] && this.color.estNoir()))){
                if(board.getCellule(coordonneeDestination).celluleOccupee()){
                    //la piece qui on peut attaquer
                    final Piece pAttack = board.getCellule(coordonneeDestination).getPiece();
                    //si la couleur de la piece n'est pas la même, on peut attaquer
                    if(this.color != pAttack.getColor()){
                        //TODO à terminer
                        deplacePossible.add(new PionAttaque(board, this, pAttack, coordonneeDestination));
                    }
                }
            }//pour attaque à coté gauche et les barieres pour ne pas dépasser le board
            else if(positionCur == 9&& !((Utilitaires.PREMIER_COLONNE[this.position]&& this.color.estBlanch()) || (Utilitaires.HUITIEME_COLONNE[this.position] && this.color.estNoir()))){
                if(board.getCellule(coordonneeDestination).celluleOccupee()){
                    //la piece qui on peut attaquer
                    final Piece pAttack = board.getCellule(coordonneeDestination).getPiece();
                    //si la couleur de la piece n'est pas la même, on peut attaquer
                    if(this.color != pAttack.getColor()){
                        //TODO à terminer
                        deplacePossible.add(new PionAttaque(board, this, pAttack, coordonneeDestination));
                    }
                }
            }
        }

        return ImmutableList.copyOf(deplacePossible);
    }

    @Override
    public Pion deplacePiece(Deplacer deplacer) {
        return new Pion(deplacer.getPieceDeplacee().getColor(), deplacer.getCoordonnee());
    }
    //pour affichage sur terminal
    @Override
    public String toString(){
        return Type.PION.toString();
    }
}
