Journal de développement - Algorithme Simplex en Java
14-04-25
SIMPLEX - Implémentation en Java

Objectif
Maximiser ou minimiser une fonction sous une ou plusieurs contraintes

Données :

1 fonction objectif

Une ou plusieurs contraintes

Contraintes de signe

Résultat attendu : Trouver les valeurs des variables optimisant la fonction objectif

Méthode : Algorithme du Simplex
Architecture
Classes principales :

Variable (nom, type)

Fonction (liste de variables)

Contrainte (étend Fonction, ajoute signe et second membre)

Système (variables, fonction objectif, contraintes)

Base (variables de base)

TableSimplex (système, base, tableau)

Fonctionnalités implémentées
Vérification si l'algorithme nécessite 1 ou 2 phases

Transformation du système avec variables d'écart

Initialisation de la base de départ

Construction du tableau initial

Algorithme itératif avec pivotage

Gestion des cas simples et complexes

16-04-2025
Création des classes de base

Mise en place de la vérification de cohérence des variables dans le système

17-04-2025
Implémentation des tests de contraintes

Gestion des contraintes de signe

Clonage des objets pour transformation du système

18-04-2025
Adaptation pour contraintes d'égalité

Ajout des variables d'écart selon le type de contrainte

22-04-2025
Gestion avancée des contraintes d'égalité

24-04-2025
Correction du nombre de variables d'écart selon les contraintes

Débuggage de l'initialisation de base

29-04-2025
Création et correction de TableSimplex

Implémentation de la permutation de base

30-04-2025
Normalisation du pivot (valeur = 1)

Opérations d'annulation sur les colonnes

01-05-2025
Finalisation du Simplex en 1 phase

Validation des résultats

Simplex en 2 phases
Cas nécessitant des variables artificielles
Étapes implémentées :

Détection de la nécessité de 2 phases

Ajout des variables artificielles

Fonction objectif artificielle

Initialisation du tableau à 2 phases

Transformation pour la phase 2

02-05-2025
Implémentation de la fonction artificielle

Transformation du tableau entre phases

Gestion des contraintes complexes

03-05-2025
Implémentation de la minimisation

Gestion des contraintes d'égalité directe

06-05-2025
Correction des contraintes de type y = 3

Gestion des tableaux partiellement remplis

07-05-2025
Gestion de la dégénérescence

Adaptation max/min selon les phases

09-05-2025
Clarification de la fonction artificielle

[min = -max(-f)] pour la conversion

Problèmes résolus
Inversion des signes pour la minimisation

Gestion des erreurs d'arrondi avec epsilon

Correction de la dernière ligne du tableau

10-05-2025
Expression de la fonction objectif selon les variables de base

Opérations sur la dernière ligne du tableau

11-05-2025
Prévention des boucles infinies

Sélection robuste des variables entrantes/sortantes

12-05-2025
Renforcement de la sélection des variables

14-05-2025
Implémentation du système de fractions

Classe Fraction pour éviter les erreurs de précision

15-05-2025
Optimisation des fractions (numérateur/dénominateur entiers)

Amélioration de l'affichage

Version stable avec fractions

16-05-2025
Planification de l'interface graphique

Préparation pour l'extension aux graphes

19-05-2025
Début de l'implémentation Swing

20-05-2025
Liaison interface/logique

Gestion des résultats et affichage

Préparation des itérations pour visualisation

21-05-2025
Améliorations interface :

Gestion min/max

Variables de base par tableau

Synchronisation des formulaires

Extension PLNE (Programmation Linéaire en Nombres Entiers)
Algorithme Branch and Bound implémenté
Processus :

Relaxation du problème

Si solution non entière, création de sous-problèmes

Ajout de contraintes de branchement

Élagage selon la borne supérieure

Sélection de la meilleure solution entière

22-05-2025
Finalisation de l'algorithme PLNE

Gestion des exceptions et cas limites

Vérification des variables directes uniquement

30-06-25
Préparation des tests PLNE

Logging complet du processus de résolution