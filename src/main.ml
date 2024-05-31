(* main.ml *)
open Types
open Csv_operations
open User_verification
open Performance_operations
open Update_info
open Combat_operations
open Percentage_calculation

let () =
  lire_combattants "combattants.csv";
  let user_verified = verify_user "users.csv" "john.berger" "password123" in
  Printf.printf "User verified: %b\n" user_verified;

  let new_performance = {
    id = "1";
    adversaire = "adversaire1";
    date = "2024-06-01";
    resultat = "victoire";
    type_victoire = "KO";
  } in
  add_performance new_performance;

  update_info "1" "taille" "185";

  let new_combat_detail = {
    id = "1";
    coups_reussis = 30;
    coups_manques = 10;
    types_coups = "jab,hook";
    zones_touchees = "visage,corps";
  } in
  add_combat_details new_combat_detail;

  let percentage = calculate_percentages "1" in
  Printf.printf "Percentage: %f\n" percentage
