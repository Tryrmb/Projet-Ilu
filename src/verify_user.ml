(* verify_user.ml *)
#use "Combattant.ml"

(* La fonction principale qui sera exécutée lorsque le script sera appelé *)
let () =
  (* Récupérer les arguments de la ligne de commande *)
  let file = Sys.argv.(1) in    (* Le nom du fichier contenant les identifiants *)
  let id = Sys.argv.(2) in      (* L'identifiant (nom) de l'utilisateur à vérifier *)
  let password = Sys.argv.(3) in (* Le mot de passe de l'utilisateur *)

  (* Charger les identifiants à partir du fichier *)
  let identifiants = Charger_identifiants_depuis_fichier file in

  (* Vérifier les identifiants *)
  let correct_password = List.assoc id identifiants in
  if correct_password = password then
    Printf.printf "true\n"
  else
    Printf.printf "false\n"
