## Jade

Jade è un framework che permette di implementare sistemi distribuiti in linguaggio Java, tramite un'architettura che segue le specifiche FIPA (Foundation for Intelligent Physical Agent).
I componenti principali del framework sono i container, gli agenti, i messaggi che possono scambiare, i comportamenti eseguibili ed infine il sistema di white e yellow pages.
Essendo distribuito, è possibile eseguire gli agenti Jade su nodi diversi a patto che esista sulll’host un environment Jade capace di ospitare almeno un container, il main container responsabile di mantenere un registro di tutti gli altri container sulla stessa piattaforma Jade (tramite i quali gli agenti possono trovarsi tra loro).
Gli agenti sono di tipo passivo, dato che per specifiche FIPA devono essere implementati con attenzione all’interazione piuttosto che al “ragionamento”. All’atto pratico, si tratta di estendere la classe Java _Agent_.

Per quanto riguarda i messaggi, ne esistono di diverso tipo[^1] (anche questi seguono il FIPA ACL (Agent Communication Language) Message Structure Specification).
Ogni messaggio è costituito da:
*   Performative: tipo di messaggio FIPA
*   Addressing: receiver o sender
*   Content
*   ConversationID
*   Language
*   Ontology
*   Protocol
*   ReplyWith
*   InReplayTo
*   ReplyBy

I tipi di performative sono i seguenti:
1. Accept proposal: The action of accepting a previously submitted proposal to perform an action.
2. Agree: The action of agreeing to perform some action, possibly in the future.
3. Cancel: The action of one agent informing another agent that the first agent no longer has the intention that the second agent performs some action.
4. Call for Proposal: The action of calling for proposals to perform a given action.
5. Confirm: The sender informs the receiver that a given proposition is true, where the receiver is known to be uncertain about the proposition.
6. Disconfirm: The sender informs the receiver that a given proposition is false, where the receiver is known to believe, or believe it likely that, the proposition is true.
7. Failure: The action of telling another agent that an action was attempted but the attempt failed.
8. Inform: The sender informs the receiver that a given proposition is true.
9. Inform If: A macro action for the agent of the action to inform the recipient whether or not a proposition is true.
10. Inform Ref: A macro action for sender to inform the receiver the object which corresponds to a descriptor, for example, a name.
11. Not Understood: The sender of the act (for example, i) informs the receiver (for example, j) that it perceived that j performed some action, but that i did not understand what j just did. A particular common case is that i tells j that i did not understand the message that j has just sent to i.
12. Propagate: The sender intends that the receiver treat the embedded message as sent directly to the receiver, and wants the receiver to identify the agents denoted by the given descriptor and send the received propagate message to them.
13. Propose: The action of submitting a proposal to perform a certain action, given certain preconditions.
14. Proxy: The sender wants the receiver to select target agents denoted by a given description and to send an embedded message to them.
15. Query If: The action of asking another agent whether or not a given proposition is true.
16. Query Ref: The action of asking another agent for the object referred to by a referential expression.
17. Refuse: The action of refusing to perform a given action, and explaining the reason for the refusal.
18.  Reject Proposal: The action of rejecting a proposal to perform some action during a negotiation.
19. Request: The sender requests the receiver to perform some action. One important class of uses of the request act is to request the receiver to perform another communicative act.
20. Request When: The sender wants the receiver to perform some action when some given proposition becomes true.
21.  Request Whenever: The sender wants the receiver to perform some action as soon as some proposition becomes true and thereafter each time the proposition becomes true again.
22. Subscribe: The act of requesting a persistent intention to notify the sender of the value of a reference, and to notify again whenever the object identified by the reference changes.

La comunicazione attraverso questi messaggi avviene in modo asicrono, ogni agente avrà infatti una coda in cui accumulare messaggi in ingresso.
I comportamenti sono definiti attraverso una vera e propria specializzazione di classe Behaviour e possono essere di vario tipo, i principali sono:
*   OneShotBehaviour
*   CyclicBehaviour
*   SequentialBehaviour
*   ParallelBehaviour
*   FSMBehaviour

Le wihite pages introdotte precedentemente sono un servizio fornito dall’Agent Management System (AMS) che permette ad un agente di trovarne un altro conoscendo soltanto il nome.
Esiste un solo AMS per piattaforma Jade ed ha il compito di tenere traccia di tutti gli agenti presenti nella piattaforma, sia di agenti presenti in container locali, sia in quelli distribuiti. 

Le yellow pages, in modo simile alle white pages mantengono traccia dei servizi offerti dagli agenti (invece che dei nomi). In questo caso al posto dell’AMS si parla di Directory Facilitator (DF).

Inoltre Jade offer degli strumenti di gestione come il Remote Monitoring Agent (RMA), che tramite GUI permette di controllare l'intero ciclo di vita degli agenti.
Permette inoltre di costruire messaggi da inviare “manualmente” simulando un agente sender; permette anche di “sniffare” messaggi tramite un apposito Sniffer Agent.

La necessità di aderire al protocollo FIPA, se da una parte costringe uno sviluppo rigoroso e con minori problemi di comunicazione tra agenti, va senz’altro ad introdurre una complessità inutilmente elevata per il progetto preso in analisi.
Ad esempio, basti guardare al numero di tipologie di messaggi tra cui scegliere in Jade, ben 22, e quelli di Qactor che sono soltanto 3.
Inoltre lo sviluppo in Jade è molto verboso, a differenza dello stile di scrittura conciso di Qactor, cosa che porterà sicuramente a tempi di sviluppo più brevi.

## Spring Framework

Spring è un framework utilizzato per la realizzazione di sistemi distribuiti. 
Si presta bene ad essere impiegato in architetture orientate ai servizi (SOA), in cui abbiamo dei componenti dotati di una forte autonomia (per quanto possano comunque collaborare tra loro). 
Tipicamente si opta per un’architettura SOA nel caso in cui ci troviamo a realizzare un sistema con molti componenti indipendenti, che abbia una certa complessità, che potenzialmente si interfacci con sistemi esterni e che necessiti di più o meno frequenti sostituzioni di componenti. 

Inoltre in generale Spring Framework è fortemente legato al mondo Java. Ciò potrebbe rappresentare un vincolo tecnologico nel caso in cui il sistema debba essere eterogeneo, o comunque realizzato con un’altra tecnologia. La modellazione del nostro sistema deve essere invece il più possibile technology independent. 
Spring presenta delle caratteristiche e possibilità che si adattano meglio a sistemi distribuiti più grandi e complessi. Nel nostro caso abbiamo sì a che fare con un sistema distribuito potenzialmente eterogeneo, ma si tratta pur sempre di un sistema embedded di piccole dimensioni, con componenti hardware che si trovano fisicamente vicini gli uni agli altri. E’ quindi opportuno evitare di introdurre overhead per garantire una maggiore semplicità e immediatezza nella comunicazione tra componenti. 

Tra le possibilità aggiuntive offerte da Spring c’è il supporto ai DAO (Data Access Object), che consente di interfacciarsi con diverse tecnologie (JDBC, JPA, Hibernate) per la gestione di un database. Questo sistema però non richiede la presenza di un database, per cui questa possibilità risulta superflua. 
Spring offre anche un supporto per la sicurezza del sistema, ma anche questo aspetto non figura tra i requisiti del sistema che si vuole realizzare in questo caso. 

Non supporta nativamente il concetto di attore, ma si potrebbe decidere di implementare i vari servizi come attori. Anche in questo caso però dovremmo comunque tenere la complessità aggiuntiva del framework Spring.
Lo scambio di messaggi in Spring avviene tramite Java Message Service (JMS). Questo componente offre delle funzionalità più avanzate come la persistenza dei messaggi o un sistema di acknowledge per verificare che i messaggi non vengano persi. Queste caratteristiche rischiano di appesantire un sistema semplice come quello che stiamo considerando. In particolare la richiesta di acknowledge rende la produzione del messaggio bloccante (si attende finchè non arriva la conferma di ricezione), questo renderebbe il nostro sistema meno reattivo. 

Nel contesto in cui ci troviamo ad operare possiamo ritenere che la perdita di messaggi o il partizionamento della rete siano improbabili, visti il basso numero di entità in gioco e messaggi prodotti, oltre alla vicinanza fisica dei dispositivi hardware.   


<!-- Footnotes themselves at the bottom. -->
## Notes

[^1]:
     http://www.fipa.org/specs/fipa00037/SC00037J.html
