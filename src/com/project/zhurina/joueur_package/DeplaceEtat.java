package com.project.zhurina.joueur_package;

public enum DeplaceEtat {
    FAIT{
        @Override
        public boolean  estFait(){
            return true;
        }
    },
    IMPOSSIBLE{
        @Override
        public boolean estFait(){
            return false;
        }

    },
    SOUS_ECHEC {
        @Override
        public boolean estFait() {
            return false;
        }
    };
    public abstract boolean estFait();
}
