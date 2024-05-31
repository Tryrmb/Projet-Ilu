(* add_performance.ml *)
#use "Historique.ml"

(* La fonction principale qui sera exécutée lorsque le script sera appelé *)
let () =
  (* Récupérer les arguments de la ligne de commande *)
  let file = Sys.argv.(1) in       (* Le nom du fichier contenant les historiques *)
  let id = Sys.argv.(2) in         (* L'identifiant (nom) du combattant dont la performance doit être ajoutée *)
  let adversaire = Sys.argv.(3) in (* Nom de l'adversaire *)
  let date = Sys.argv.(4) in       (* Date du combat *)
  let resultat = Sys.argv.(5) in   (* Résultat du combat, par exemple "victoire", "defaite", "nul" *)
  let type_victoire = Sys.argv.(6) in (* Type de victoire, par exemple "KO", "TKO", "points" *)

  (* Charger les historiques à partir du fichier *)
  let historiques = Charger_historiques_depuis_fichier file in

  (* Créer la nouvelle performance *)
  let combat = {
    adversaire;           (* Nom de l'adversaire *)
    date;                 (* Date du combat *)
    resultat = (match resultat with
                | "victoire" -> Victoire
                | "defaite" -> Defaite
                | "nul" -> Nul
                | _ -> failwith "Résultat inconnu"); (* Transformer le résultat en type `resultat_combat` *)
    type_victoire = (match type_victoire with
                     | "KO" -> KO
                     | "TKO" -> TKO
                     | "points" -> Points
                     | _ -> failwith "Type de victoire inconnu"); (* Transformer le type de victoire en type `type_victoire` *)
    coups_reussis = 0;    (* Par défaut, à modifier plus tard *)
    coups_manques = 0;    (* Par défaut, à modifier plus tard *)
    types_coups = [];     (* Liste vide, à remplir plus tard *)
    zones_touchees = [];  (* Liste vide, à remplir plus tard *)
  } in

  (* Ajouter la performance à l'historique du combattant *)
  let historique = List.find (fun h -> h.id = id) historiques in
  Historique.ajouter_combat historique combat;

  (* Sauvegarder les modifications *)
  Sauvegarder_historiques_dans_fichier file historiques;

  (* Afficher un message de confirmation *)
  Printf.printf "Performance ajoutée avec succès\n"
