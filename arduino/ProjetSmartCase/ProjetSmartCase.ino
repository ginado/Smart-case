
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

/* pour les capteurs 0,4,8 on envoie un signal sur le pin 22 
       //            1,3,9                    //          23 
      //             2,6,10                   //          24
      //             3,7,11                   //          25
      
 On lit les valeurs reçues sur les entrées :
 A0 pour les capteurs de 0 à 3
 A1        //            4 à 7
 A2        //            8 à 11
                                                          */
int lireCapteur (int numCapteur){
  Serial.println("On lit la valeur du capteur " + String(numCapteur));
  int pin=22+numCapteur%4;
  digitalWrite(pin, HIGH);
  int in=numCapteur/4;
  
  /*switch (in){ 
  case 0:
     analogRead(0);
     break;
  case 1:
     analogRead(A11);
     break;
  case 2:
     analogRead(A2);
     break;
  default :
     Serial.println("erreur, mauvais numero de casier");
  }*/
  int valeurlue=analogRead(in);
  digitalWrite(pin,LOW);
  return valeurlue;
}

/*
  On utilise un multiplexeur 12->1 (commande sur 4 bits) pour selectionner le capteur à lire. 
   ArduinoMega(26..29) <--> CommandeMUX(0..3)
        ArduinoMega(3) <--> SortieMUX(0)
       ArduinoMega(30) <--> CapteursIN (0..11)
    CapteursOUT(0..11) <--> EntreeMUX(0..11)
 Resistance (470 Ohms) <--> GND
 */
int lireCapteurMux (int numCapteur){
  //On ecrit la commande du multiplexeur (=numCapteur en binaire)
  digitalWrite(26 , (numCapteur%2 == 1)?HIGH:LOW);
  digitalWrite(27 , ((numCapteur/2)%2 == 1)?HIGH:LOW);
  digitalWrite(28 , ((numCapteur/4)%2 == 1)?HIGH:LOW);
  digitalWrite(29 , ((numCapteur/8)%2 == 1)?HIGH:LOW);

  //On alimente les capteurs
  digitalWrite(30, HIGH);

  //On lit le resultat analogique
  int res = analogRead(30);

  //On arrete l'alimentation des capteur et du multiplexeur
  digitalWrite(26, LOW);
  digitalWrite(27, LOW);
  digitalWrite(28, LOW);
  digitalWrite(29, LOW);
  digitalWrite(30, LOW);

  return res;
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
  pinMode(22,OUTPUT);
  pinMode(23,OUTPUT);
  pinMode(24,OUTPUT);
  pinMode(25,OUTPUT);
  digitalWrite(22, LOW);
  digitalWrite(23, LOW);
  digitalWrite(24, LOW);
  digitalWrite(25, LOW);
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








