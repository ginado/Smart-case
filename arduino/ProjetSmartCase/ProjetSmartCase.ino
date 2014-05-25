#define PremiereEntreeCapteur 0
#define PremiereSortieCapteur 22
#define EntreeCapteur(n) (PremiereEntreeCapteur+(n/4))
#define SortieCapteur(n) (PremiereSortieCapteur+(n%4))

#define SortieSerrure(n) (30+n)


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
 1,3,9                                23 
 2,6,10                               24
 3,7,11                               25
 
 On lit les valeurs reçues sur les entrées :
 A0 pour les capteurs de 0 à 3
 A1                      4 à 7
 A2                      8 à 11
 */
int lireCapteur (int numCapteur){
  if(numCapteur < 0 || numCapteur > 11){
    return -1;
  }

  int pin=SortieCapteur(numCapteur);
  digitalWrite(pin, HIGH);

  int in=EntreeCapteur(numCapteur);

  int valeurlue=analogRead(in);

  digitalWrite(pin,LOW);
  return valeurlue;
}

int ouvrirSerrure (int numSerrure){
  if(numSerrure < 0 || numSerrure > 11){
    return -1;
  }
  digitalWrite(SortieSerrure(numSerrure), HIGH);
  return 0;
}

int fermerSerrure (int numSerrure){
  if(numSerrure < 0 || numSerrure > 11){
    return -1;
  }
  digitalWrite(SortieSerrure(numSerrure), LOW);
  return 0;
}

String commandeLue;
void setup(){
  pinMode(13,OUTPUT);
  for(int i = 0 ; i < 11 ; i++){
    pinMode(SortieSerrure(i), OUTPUT);
    digitalWrite(SortieSerrure(i),LOW);
    pinMode(SortieCapteur(i), OUTPUT);
    digitalWrite(SortieCapteur(i),LOW);
    pinMode(EntreeCapteur(i), INPUT);
  }
  Serial.begin(9600);
  commandeLue = "";
}

void loop(){
  Serial.println("1");
  while (commandeLue.equals("")){
    Serial.println("2");
    commandeLue = lireCommande();
  } 
  //Serial.println("Commande lue: " + commandeLue);

  switch (commandeLue.charAt(0)){
    //Commande "capteur"
  case 'c':
    {
      int numeroCapteur = commandeLue.substring(1).toInt();
      int valeurCapteur = lireCapteur(numeroCapteur);
      if(valeurCapteur < 0){
        Serial.println("KO");
      } 
      else {
        Serial.println(String(valeurCapteur));
      }
      break;
    }
    //Commande "ouverture serrure"
  case 'o':
    {
      int numeroSerrure = commandeLue.substring(1).toInt();
      int resultat = ouvrirSerrure(numeroSerrure);
      if (resultat == 0){
        //traiter l'erreur
        Serial.println("OK");
      } 
      else {
        Serial.println("KO");
      }
      break;
    }
    //Commande "fermeture serrure"
  case 'f':
    {
      int numeroSerrure = commandeLue.substring(1).toInt();
      int resultat = fermerSerrure(numeroSerrure);
      if (resultat == 0){
        //traiter l'erreur
        Serial.println("OK");
      } 
      else {
        Serial.println("KO");
      }
      break;
    }

  case 'x':
    {
      int i = 1;
      while(i<20){
        if(i%2){
          digitalWrite(13,HIGH);     
          Serial.println("ping");
        } 
        else {
          digitalWrite(13,LOW);
          Serial.println("pong");
        }
        i++;
        delay(500);
      }
      break;
    }

  case 'x':
    {
      int i = 1;
      while(i<20){
        if(i%2){
          digitalWrite(13,HIGH);     
          Serial.println("ping");
        } 
        else {
          digitalWrite(13,LOW);
          Serial.println("pong");
        }
        i++;
        delay(500);
      }
      Serial.println("4");
      /*while (Serial.available()){
        Serial.read();  
      }*/
      break;
    }
  
  default:
    //traiter l'erreur
    Serial.println("KO");
    break;
  }


  commandeLue="";
}











