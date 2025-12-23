package com.zoaramas.optimization.logic.Plne;

import java.util.Map;

import com.zoaramas.optimization.*;
import com.zoaramas.optimization.logic.*;
import com.zoaramas.optimization.logic.FonctionPack.Contrainte;

public class PlneExt {
    // Algorithme principal, retourne Result[0 -> resultat final ou min aussi, 1 ->
    // maxResult, ou resultFinal ne peut etre superieur], mais 0 reste la solution
    // finale
    // Fonction recursive
    public static Result[] plner(Systeme systeme, Result resultCurr, Result resultMax, boolean debut) throws Exception {
        System.out.println("fraise");
        Result[] result = new Result[2];
        result[0] = resultCurr;
        result[1] = resultMax;
        try {
            Result r = systeme.simplexer(true);
            System.out.println("Result simplex = " + r.getResultString());
            if (r.isEntier()) {
                if (result[0] == null || r.isStrictementPlusGrandQue(result[0])) {
                    System.out.println("solution admissible");
                    result[0] = r; // Solution admissible
                }
                if (debut)
                    System.out.println("Solution trouve des le depart");
                return result;
            } else {
                System.out.println("Solution non entiere");
                // verifier que la solution est plus petite que resultMax sinon on coupe
                if (result[1] == null || !r.isStrictementPlusGrandQue(result[1])) {
                    result[1] = r;
                } else { // N'apparait jamais au debut
                    // On coupe
                    System.out.println("ON COUPE, Resultat plus grand que le max");
                    return result;
                }
                // BRUNCH
                // Le choix de la variable se passe dans la fonction
                // getFirstFractionalVariableValue() utilise par getLp()
                System.out.println("pomme");
                Systeme lp1 = getLp(systeme, 1, r); // On utilise directement la premiere valeur non entiere dans la
                // liste dans le resultat
                result = PlneExt.plner(lp1, result[0], result[1], false);
                // if (result != null)
                // throw new Exception("cheese = " + result[0].getResultString());
                Systeme lp2 = getLp(systeme, 2, r);
                result = PlneExt.plner(lp2, result[0], result[1], false);
            }
        } catch (Exception e) {
            // On coupe
            return result;
        }
        return result;
    }

    // Obtenir le systeme ajoute de la nouvelle condition selong pl1 ou pl2
    public static Systeme getLp(Systeme system, int ordre, Result r) throws Exception {
        Systeme nouveauSysteme = system.cloneSysteme();
        Map.Entry<Variable, Fraction> firstFractional = r.getFirstFractionalVariableValue();
        Variable v = firstFractional.getKey();
        Fraction f = firstFractional.getValue();
        int partieEntiere = (int) f.getValue();
        Terme terme1 = new Terme(v, 1);
        Terme terme2 = new Terme(v, 1);
        Contrainte contrainte1 = new Contrainte(terme1, Signe.inferieurOuEgal, partieEntiere);
        Contrainte contrainte2 = new Contrainte(terme2, Signe.superieurOuEgal, partieEntiere + 1);
        if (ordre == 1)
            nouveauSysteme.getListeContrainte().add(contrainte1);
        else
            nouveauSysteme.getListeContrainte().add(contrainte2);
        System.out.println("prenons " + v.getNom() + " = " + f.getValue());
        return nouveauSysteme;
    }
}
