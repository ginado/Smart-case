
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
  commandeLue="";
}



