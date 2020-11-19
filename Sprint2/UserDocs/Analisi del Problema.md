# Analisi del Problema

**Gestione Client multipla**

La gestione multipla dei Client ci pone davanti ad un problema non affrontato precedentemente, in quanto con un solo Client, le operazioni avvengono tutte in modo sequenziale.
Nel caso di più Client ci aspettiamo invece di ricevere più messaggi contemporaneamente. Per questo motivo dobbiamo essere sicuri di non perderli e di avere un criterio per dare una priorità alle operazioni.

Per evitare la perdita di messaggi non è stato necessario implementare un sistema ad hoc. La tecnologia Qactor adottata prevede infatti la gestione di messaggi tramite la struttura dati a coda, che li immagazzina e ne permette la lettura in un secondo momento.
Utilizzando questo meccanismo la priorità maggiore viene quindi data al primo messaggio arrivato.

**Gestione MaxStayTime**

Lo scorrere del tempo per i Client al tavolo viene modellato utilizzando due timer, uno per ogni tavolo. 
Ognuno dei timer è realizzato con un attore indipendente, che viene inizializzato dal Waiter nel momento in cui consegna il tè a un certo tavolo. In questo modo i timer sono lasciati liberi di svolgere il proprio conteggio senza nessuna interferenza, e possono mandare notifiche al Waiter alla scadenza del MaxStayTime.
Se un Client effettua il pagamento prima della scadenza del timer, quest'ultimo verrà bloccato dal Waiter.

**Osservazione dei cambiamenti del sistema**

Da requisito è necessario costruire un’interfaccia web per l’utente Manager.
Ciò rende obbligatorio condividere lo stato del sistema con l’interfaccia web.

Sono stati valutati due approcci.
Il primo, più semplice, prevede che la parte web lanci delle richieste verso il sistema ad intervalli regolari, per richiedere lo stato.
Questo approccio di polling è stato scartato perché poco efficiente in quanto effettua delle richieste anche quando non c’è un effettivo cambiamento di stato, e potrebbe portare in casi limite alla congestione del protocollo di rete.

Il secondo approccio è tramite interrupt.
In questo caso, la parte web osserva i cambiamenti delle componenti del sistema utilizzando il pattern observer.
In tal modo riceve delle notifiche soltanto quando c’è un cambiamento di stato.

É stato adottato quindi quest’ultimo utilizzando come supporto software il protocollo COAP che offre le API adatte al paradigma sopra citato.



**Comunicazione interfaccia grafica con sistema attori**

Il sistema in essere evidenzia una criticità nelle comunicazioni con le componenti esterne.
Queste componenti costituiscono quella che è l’interfaccia grafica.
Tale interfaccia infatti deve adeguarsi a un sistema già in essere con un impatto d’integrazione minimo.
La scelta del protocollo di comunicazione non deve quindi inficiare negativamente sulle caratteristiche intrinseche del sistema.
A tal proposito si è scelto di adottare una comunicazione full-duplex mediante l’ausilio di una WebSocket.
Le diverse componenti del sistema, dotate di piena autonomia comunicativa, hanno fatto in modo che si creassero i presupposti nei quali il protocollo publish/subscribe ha trovato terreno fertile. L’adozione di tale protocollo permette di sottoscriversi al canale desiderato leggendo i messaggi degli altri o pubblicarne dei propri. In questo modo è possibile interagire con il sistema mentre se ne osservano i cambiamenti, i quali vengono mostrati a video mediante apposita interfaccia.

**Rappresentazione dello stato del sistema**

Per rappresentare lo stato sono stati presi in considerazione due approcci: stato centralizzato in un solo attore oppure stato distribuito.
Le caratteristiche dell’approccio con stato centralizzato sono:
*   una rappresentazione uniforme e standardizzata dei dati
*   reperibilità dell’intero stato del sistema in unico punto
*   in ottica del collegamento con l'interfaccia grafica, la comunicazione avviene soltanto tra GUI e attore designato al mantenimento dello stato
*   è necessario conoscere soltanto l’indirizzo dell’attore che mantiene lo stato centralizzato

Nel caso di stato distribuito, le caratteristiche sono:
*   in caso di down di un attore, si continuano a ricevere notifiche dagli altri
*   semplicità del sistema ad attori, in quanto non è necessario introdurre messaggi e attori utili unicamente a rappresentare lo stato
*   immediatezza dello sviluppo del software

Valutando le proprietà appena descritte, si procede con l’approccio distribuito, in quanto abbiamo a che fare con un sistema di dimensioni ridotte e non particolarmente orientato a grandi espansioni future (non ci si aspetta un grande aumento nel numero di attori).

Detto ciò, si punta all’approccio distribuito vista la sua semplicità ed immediatezza.

