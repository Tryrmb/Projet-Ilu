(* Historique.ml *)

(* Définition du type `coup` qui représente un coup porté dans un combat *)
type coup = {
  type_coup : string; (* Le type de coup, par exemple "Jab", "Uppercut", etc. *)
  zone : string;      (* La zone touchée, par exemple "Visage", "Corps", etc. *)
}

(* Définition du type `resultat_combat` qui représente le résultat d'un combat *)
type resultat_combat = 
  | Victoire  (* Victoire du combattant *)
  | Defaite   (* Défaite du combattant *)
  | Nul       (* Match nul *)

(* Définition du type `type_victoire` qui représente le type de victoire *)
type type_victoire = 
  | KO        (* Victoire par Knockout *)
  | TKO       (* Victoire par Technical Knockout *)
  | Points    (* Victoire aux points *)

(* Définition du type `combat` qui représente un combat avec toutes ses caractéristiques *)
type combat = {
  adversaire : string;         (* Nom de l'adversaire *)
  date : string;               (* Date du combat *)
  resultat : resultat_combat;  (* Résultat du combat *)
  type_victoire : type_victoire; (* Type de victoire *)
  coups_reussis : int;         (* Nombre de coups réussis *)
  coups_manques : int;         (* Nombre de coups manqués *)
  types_coups : coup list;     (* Liste des types de coups utilisés *)
  zones_touchees : string list; (* Liste des zones touchées *)
}

(* Définition du type `historique` qui est une liste de combats *)
type historique = combat list

(* Fonction pour ajouter un combat à l'historique *)
let ajouter_combat historique combat =
  combat :: historique  (* Ajoute le combat au début de l'historique *)

(* Fonction pour calculer le pourcentage de coups réussis *)
let calculer_pourcentage_reussite historique =
  (* Calcul du nombre total de coups portés *)
  let total_coups = List.fold_left (fun acc combat -> acc + combat.coups_reussis + combat.coups_manques) 0 historique in
  (* Calcul du nombre total de coups réussis *)
  let total_reussis = List.fold_left (fun acc combat -> acc + combat.coups_reussis) 0 historique in
  (* Si le nombre total de coups est 0, le pourcentage de réussite est 0.0, sinon on calcule le pourcentage *)
  if total_coups = 0 then 0.0 else (float_of_int total_reussis /. float_of_int total_coups) *. 100.0

(* Fonction pour afficher l'historique des combats *)
let afficher_historique historique =
  List.iter (fun combat ->
    Printf.printf "Adversaire: %s\nDate: %s\nRésultat: %s\nType de victoire: %s\nCoups réussis: %d\nCoups manqués: %d\n\n"
      combat.adversaire
      combat.date
      (match combat.resultat with Victoire -> "Victoire" | Defaite -> "Défaite" | Nul -> "Nul")
      (match combat.type_victoire with KO -> "KO" | TKO -> "TKO" | Points -> "Points")
      combat.coups_reussis
      combat.coups_manques
  ) historique
