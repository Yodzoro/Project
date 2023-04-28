package com.project.zhurina;

import com.project.zhurina.joueur_package.Joueur;
import com.project.zhurina.joueur_package.Joueur1;
import com.project.zhurina.joueur_package.Joueur2;

public enum Color {
    BLANCH(){
        @Override
        public boolean estBlanch(){
            return true;
        }
        @Override
        public boolean estNoir() {
            return false;
        }

        @Override
        public Joueur choisirJoueur(final Joueur1 joueur1, final Joueur2 joueur2) {
            return joueur1;
        }
    },
    NOIR(){
        @Override
        public boolean estNoir() {
            return true;
        }

        @Override
        public boolean estBlanch(){
            return false;
        }
        @Override
        public Joueur choisirJoueur(final Joueur1 joueur1, final Joueur2 joueur2) {
            return joueur2;
        }
    };


    public int getDrection() {
        return this == BLANCH ? -1 : 1;
    }
    public abstract boolean estBlanch();
    public abstract boolean estNoir();

    public abstract Joueur choisirJoueur(Joueur1 joueur1, Joueur2 joueur2);
}
