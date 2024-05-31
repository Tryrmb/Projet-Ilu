(* add_combat_details.ml *)
#use "Historique.ml"

(* La fonction principale qui sera exécutée lorsque le script sera appelé *)
let () =
  (* Récupérer les arguments de la ligne de commande *)
  let file = Sys.argv.(1) in                        (* Le nom du fichier contenant les historiques *)
  let id = Sys.argv.(2) in                          (* L'identifiant (nom) du combattant *)
  let coups_reussis = int_of_string Sys.argv.(3) in (* Nombre de coups réussis *)
  let coups_manques = int_of_string Sys.argv.(4) in (* Nombre de coups manqués *)
  let types_coups = String.split_on_char ',' Sys.argv.(5) in (* Liste des types de coups *)
  let zones_touchees = String.split_on_char ',' Sys.argv.(6) in (* Liste des zones touchées *)

  (* Charger les historiques à partir du fichier *)
  let historiques = Charger_historiques_depuis_fichier file in

  (* Trouver le dernier combat du combattant *)
  let historique = List.find (fun h -> h.id = id) historiques in
  let combat = List.hd historique in

  (* Ajouter les détails du combat *)
  combat.coups_reussis <- coups_reussis;
  combat.coups_manques <- coups_manques;
  combat.types_coups <- List.map (fun tc -> { type_coup = tc; zone = "" }) types_coups; (* Zone à définir selon les types *)
  combat.zones_touchees <- zones_touchees;

  (* Sauvegarder les modifications *)
  Sauvegarder_historiques_dans_fichier file historiques;

  (* Afficher un message de confirmation *)
  Printf.printf "Détails du combat ajoutés avec succès\n"
