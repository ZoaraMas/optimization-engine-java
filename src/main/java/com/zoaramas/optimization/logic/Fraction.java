package com.zoaramas.optimization.logic;

public class Fraction {
    protected int num;
    protected int  den;

    public Fraction(int num, int den) {
        this.num = num;
        this.den = den;
    }

    public Fraction(float val) throws Exception {
        if(val % 1 != 0) throw new Exception("Erreur creation de fraction a partir d'un float, le float ne peu pas etre converti en int");
        this.num = (int)val;
        this.den = 1;
    }

    public Fraction() {
        this.num = 0;
        this.den = 1;
    }

    // Verifie si la fraction est entiere
    public boolean isEntier() {
        if(this.getValue() % 1 == 0) return true;
        return false;
    }

    public Fraction clone() {
        return new Fraction(this.num, this.den);
    }

    @Override
    public String toString() {
        return str();
    }

    public String str(){
        if(this.num == 0) return "0  ";
        if(isEntier()) return "" + (this.getValue()) + "  ";
        return num + "/" + den;
    }
    public static boolean inferieur(Fraction f1, Fraction f2) {
        if(f1.getValue() < f2.getValue()) return true;
        return false;
    }

    public static boolean superieur(Fraction f1, Fraction f2) {
        if(f1.getValue() > f2.getValue()) return true;
        return false;
    }

    public static Fraction rendre1(Fraction f) {
        f.num = 1;
        f.den = 1;
        return f;
    }
    public boolean inferieurOuEgalZero() {
        if(this.num == 0 || ((this.num > 0 && this.den < 0)) ||(this.num < 0 && this.den > 0)) return true;
        return false;
    }
    public boolean superieurStrictZero() {
        if(this.num != 0 && ((this.num > 0 && this.den > 0) ||(this.num < 0 && this.den < 0))) return true;
        return false;
    }
    public static Fraction multiplier(Fraction f1, Fraction f2) {
        Fraction result = new Fraction(f1.getNum() * f2.getNum(), f1.getDen() * f2.getDen());
        return result;
    }

    public Fraction mult(float mult) throws Exception {
        if(mult % 1 != 0) throw new Exception("Erreur creation de fraction a partir d'un float, le float ne peu pas etre converti en int");
        Fraction result = new Fraction(this.num * (int)mult, this.den);
        result.simplifier();
        return result;
    }

    public static Fraction additioner(Fraction f1, Fraction f2) {
        // rendre au meme denominateur d'abord
       Fraction[] fractionsMemeDen = rendreMemeDenominateur(f1, f2);
       Fraction result = new Fraction(fractionsMemeDen[0].getNum() + fractionsMemeDen[1].getNum(), fractionsMemeDen[0].getDen());
       result.simplifier();
       return result;
    }

    public static Fraction soustraire(Fraction f1, Fraction f2) {
        // rendre au meme denominateur d'abord
       Fraction[] fractionsMemeDen = rendreMemeDenominateur(f1, f2);
       Fraction result = new Fraction(fractionsMemeDen[0].getNum() - fractionsMemeDen[1].getNum(), fractionsMemeDen[0].getDen());
       result.simplifier();
       return result;
    }

    // retourne deux fractions avec les memes denominateurs
    public static Fraction[] rendreMemeDenominateur(Fraction f1, Fraction f2) {
        float den1 = f1.den;
        float den2 = f2.den;
        f1.den *= den2;
        f1.num *= den2;
        f2.den *= den1;
        f2.num *= den1;
        Fraction[] result = new Fraction[2];
        result[0] = f1;
        result[1] = f2;
        return result;
    }

    public static Fraction diviser(Fraction f1, Fraction f2) {
        int newNum = f1.getNum() * f2.getDen();
        int  newDen = f1.getDen() * f2.getNum();
        Fraction result = new Fraction(newNum, newDen);
        result.simplifier();
        return result;
    }
    // Simplifier pour avoir 1 en denominateur
    public void simplifier() {
        float pgcd = pgcd(this.num, this.den);
        this.num /= pgcd;
        this.den /= pgcd;
    }

    public static int pgcd(int a, int b) { // la grandeur de a par rapport a b n'a pas d'importance
        if(a == 0) return b;
        if(b == 0) return a;
        while(b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    // Obtenir le resultat de la fraction
    public float getValue() {
        return (float)((float)this.num / (float)this.den);
    }

    public int getNum() {
        return num;
    }
    public void setNum(int num) {
        this.num = num;
    }
    public int getDen() {
        return den;
    }
    public void setDen(int den) {
        this.den = den;
    }

    
}
