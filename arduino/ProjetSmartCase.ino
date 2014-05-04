
String lireCommande(){
  String commande = "";
  char dernierLu;
  while(true){
    while(Serial.available() > 0){
      dernierLu = Serial.read();
      if (dernierLu != '\n'){
        commande = commande + dernierLu;
      } 
      else {
        return commande;
      }
    }
    if(!commande.equals("")){
      return commande;
    }
    delay(1000);
  }
}  

int lireCapteur (int numCapteur){
  Serial.println("On lit la valeur du capteur " + String(numCapteur));
  return 1337;
}

int ouvrirSerrure (int numSerrure){
  Serial.println("On ouvre la serrure " + String(numSerrure));
  return 0;
}

int fermerSerrure (int numSerrure){
  Serial.println("On ferme la serrure " + String(numSerrure));
  return 0;
}

String commandeLue;
void setup(){
  Serial.begin(9600);
  commandeLue = "";
}

void loop(){
  while (commandeLue.equals("")){
    commandeLue = lireCommande();
  } 
  Serial.print(commandeLue);

  switch (commandeLue.charAt(0)){

    //Commande "capteur"
  case 'c':
    {
      int numeroCapteur = commandeLue.substring(1).toInt();
      int valeurCapteur = lireCapteur(numeroCapteur);
      Serial.println(String(valeurCapteur));
      break;
    }
    //Commande "ouverture serrure"
  case 'o':
    {
      int numeroSerrure = commandeLue.substring(1).toInt();
      int resultat = ouvrirSerrure(numeroSerrure);
      if (resultat != 0){
        //traiter l'erreur
        Serial.println("erreur");
      }
      break;
    }
    //Commande "fermeture serrure"
  case 'f':
    {
      int numeroSerrure = commandeLue.substring(1).toInt();
      int resultat = fermerSerrure(numeroSerrure);
      if (resultat != 0){
        //traiter l'erreur
        Serial.println("erreur");  
      }
      break;
    }

  default:
    //traiter l'erreur
    Serial.println("erreur");
    break;
  }


  commandeLue="";
}








