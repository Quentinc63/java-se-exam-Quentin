# Electricity Business - Quentin Cas

## Utilisation
1. Lancez l'application à partir de la classe `App`.
2. Suivez les instructions du menu principal pour naviguer dans l'application.
3. Commencez par créer un compte utilisateur, puis validez-le avec le code fourni.
4. Une fois connecté, vous pouvez rechercher et réserver des bornes de recharge.
5. Les reçus générés sont disponibles dans le répertoire `exports/`.

## Workflow
1. Inscription → affichage du code dans la console 
2. Validation du compte avec ce code
3. Connexion
4. Recherche d'une borne libre pour un créneau
5. Gérer Réservation (statut initial EN_ATTENTE)
6. Mode opérateur pour accepter ou refuser la réservation
7. Sur ACCEPTÉE → génération du fichier recu_<id>.txt dans /exports

## Architecture 
j'ai respecté le format demandé 
Un Package pour interfaces un autre pour model, services et ui.
Et le tout est initialisé dans le app.java

## Contenu developper 
1. Inscription ➜ génération d’un code - Validation du compte par le code - Connexion / déconnexion
2. Ajouter / modifier un Lieu - Ajouter / modifier une Borne - Supprimer une borne si aucune réservation future
3. Chercher bornes DISPONIBLES pour un créneau - Créer une réservation (statut : EN_ATTENTE) - Accepter / refuser une réservation
4. Générer un reçu texte (.txt) lors de l’acceptation
5. Menu principal clair - Validation des entrées utilisateur