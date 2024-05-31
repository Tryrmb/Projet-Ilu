(* Combattant.ml *)

(* Définition du type `sexe` qui peut être soit Homme soit Femme *)
type sexe = Homme | Femme

(* Définition du type `categorie_poids` qui représente les différentes catégories de poids possibles pour les combattants *)
type categorie_poids = 
  | PoidsMouches        (* entre 48 et 51 kg *)
  | PoidsCoqs           (* entre 51 et 54 kg *)
  | PoidsPlumes         (* entre 54 et 57 kg *)
  | PoidsLegers         (* entre 57 et 60 kg *)
  | PoidsSuperLegers    (* entre 60 et 64 kg *)
  | PoidsWelters        (* entre 64 et 69 kg *)
  | PoidsMoyens         (* entre 69 et 75 kg *)
  | PoidsMiLourds       (* entre 75 et 91 kg *)
  | PoidsLourds         (* plus de 91 kg *)

(* Définition du type `niveau` qui représente le niveau du combattant *)
type niveau = Debutant | Amateur | Professionnel

(* Définition du type `combattant` avec des champs mutables pour permettre la mise à jour des informations *)
type combattant = {
  mutable nom : string;          (* Nom du combattant *)
  mutable taille : int;          (* Taille du combattant en cm *)
  mutable poids : int;           (* Poids du combattant en kg *)
  mutable palmares : string;     (* Palmarès du combattant *)
  mutable sexe : sexe;           (* Sexe du combattant *)
  mutable club : string;         (* Club du combattant *)
  mutable vitesse : int;         (* Vitesse du combattant *)
  mutable puissance : int;       (* Puissance du combattant *)
  mutable endurance : int;       (* Endurance du combattant *)
  mutable niveau : niveau;       (* Niveau du combattant *)
  mutable categorie_poids : categorie_poids;  (* Catégorie de poids du combattant *)
}

(* Fonction pour créer un nouveau combattant avec les valeurs fournies *)
let creer_combattant nom taille poids palmares sexe club vitesse puissance endurance niveau categorie_poids =
  { nom; taille; poids; palmares; sexe; club; vitesse; puissance; endurance; niveau; categorie_poids }

(* Fonction pour mettre à jour les informations du combattant *)
let mettre_a_jour_info comb champ nouvelle_valeur =
  match champ with
  | "poids" -> comb.poids <- int_of_string nouvelle_valeur      (* Met à jour le poids *)
  | "taille" -> comb.taille <- int_of_string nouvelle_valeur    (* Met à jour la taille *)
  | "palmares" -> comb.palmares <- nouvelle_valeur              (* Met à jour le palmarès *)
  | "club" -> comb.club <- nouvelle_valeur                      (* Met à jour le club *)
  | _ -> failwith "Champ non reconnu"                           (* Lève une exception si le champ n'est pas reconnu *)
