# Analisi del Problema

## Comunicazione tra Attori
In uno scenario distribuito è necessario scegliere un protocollo di comunicazione tra gli attori. 
Sono stati valutati due protocolli, entrambi messi a disposizione da QActor: MQTT e CoAP. 

MQTT è un protocollo many-to-many che necessita di una connessione TCP sempre attiva con un broker di messaggi. Utilizza un approccio publish/subscribe, per cui i produttori inviano messaggi a dei topic a cui i consomatori si iscrivono.
Ogni attore iscritto ad un certo topic riceverà tutti i messaggi pubblicati su di esso. 

CoAP è un protocollo basato sul modello client/server one-to-one, con i client che contattano i server e questi ultimi che rispondono. E’ particolarmente mirato ai device con risorse computazionali limitate, e proprio per questo risulta molto leggero ed efficiente. 
Prevede un meccanismo di discovery delle risorse in quanto chi fa da server mette a disposizione una lista delle proprie risorse. 
Infine è presente la possibilità di osservare le risorse, in modo che i client possano ricevere notifiche ogni volta che c’è un cambiamento di stato nella risorsa osservata. 

Nel caso di questo progetto la maggior parte delle comunicazioni sono one-to-one, e ci si aspetta che il sistema esegua su dispositivi embedded con limitazioni computazionali. 
Con questi presupposti un protocollo più complesso come MQTT risulta meno adatto, per cui la scelta ricade su CoAP.

## Navigazione

Dall’analisi dei requisiti emerge un primo problema, ovvero la gestione della navigazione del waiter/robot.

Il primo punto da chiarire è quindi la localizzazione del waiter all’interno della tearoom.
Per questo motivo è necessario costruire una rappresentazione della tearoom come mappa, che contenga gli elementi chiave del sistema, cioè: tables, home, entrancedoor, exitdoor, barman ed il waiter stesso.
Nello specifico la mappa è costituita da una griglia dove ogni elemento sopra elencato occupa una cella. In questo modo, ognuno di essi può essere individuato con delle coordinate bidimensionali.

Per quanto riguarda il coverage, non è necessaria una scansione dell’ambiente a tempo di esecuzione in quanto la mappa e quindi la copertura dell’area viene fornita al waiter già pronta, in quanto è già nota la posizione di ciascun elemento.

Il secondo punto è quello della ricerca degli obiettivi da raggiungere ed il planning del percorso.
Per questo diventa indispensabile utilizzare un algoritmo capace di individuare tutti i passi per portare il waiter a destinazione.

L’algoritmo allo stato dell’arte in ambito path finding è l’A*, che permette di trovare la soluzione ottima al problema.
Per questo motivo sarà utilizzata l’implementazione del planner già presente _in house_.

## Architettura logica
[!Architettura logica](./Architettura logica.png)
