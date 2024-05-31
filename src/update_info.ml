(* update_info.ml *)
#use "Combattant.ml"

(* La fonction principale qui sera exécutée lorsque le script sera appelé *)
let () =
  (* Récupérer les arguments de la ligne de commande *)
  let file = Sys.argv.(1) in    (* Le nom du fichier contenant les combattants *)
  let id = Sys.argv.(2) in      (* L'identifiant (nom) du combattant à mettre à jour *)
  let field = Sys.argv.(3) in   (* Le champ à mettre à jour, par exemple "poids", "taille" *)
  let value = Sys.argv.(4) in   (* La nouvelle valeur pour le champ *)

  (* Charger les combattants à partir du fichier *)
  let combattants = Charger_combattants_depuis_fichier file in

  (* Trouver le combattant correspondant à l'identifiant fourni *)
  let combattant = List.find (fun c -> c.nom = id) combattants in

  (* Mettre à jour le champ spécifié du combattant avec la nouvelle valeur *)
  Combattant.mettre_a_jour_info combattant field value;

  (* Sauvegarder les modifications dans le fichier *)
  Sauvegarder_combattants_dans_fichier file combattants;

  (* Afficher un message de confirmation *)
  Printf.printf "Informations mises à jour avec succès\n"
