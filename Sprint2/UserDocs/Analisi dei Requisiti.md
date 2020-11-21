# Analisi dei Requisiti

Per il secondo sprint consideriamo i seguenti vincoli:
*  nel sistema ci sono fino a 3 Client 
*  i Client eseguono le operazioni correttamente

Di conseguenza il Waiter deve poter gestire più messaggi provenienti da uno dei Client, dalla Smartbell o dal Barman in modo parallelo, il tutto senza perdita di messaggi.

E’ necessario anche controllare che ognuno dei Client impieghi un tempo minore o uguale al MaxStayTime per la consumazione.

Il conteggio del tempo deve partire dal momento in cui il tè viene consegnato al Client, in modo da non essere influenzato da eventuali ritardi del Waiter o del Barman.

La scadenza del tempo massimo è individuale per ogni tavolo.

I casi d’uso considerati in questo Sprint sono gli stessi definiti nell’analisi generale, e sono definiti formalmente insieme ai requisiti presi in considerazione all’interno del file [qak linkato](analisi.qak).

Il Manager deve poter visualizzare lo [stato del sistema](../../Sprint0/Analisi%20dei%20Requisiti.md#componenti-del-sistema) attraverso l'interfaccia grafica.

## TestPlan
Il TestPlan dello Sprint comprende i seguenti punti:
*  garantire l’accesso al client soltanto se la sua temperatura è inferiore a 37.5°C e se c’è almeno un tavolo pulito. Verifica che il client riceva un Client ID dalla smartbell;
*  verificare che il client arrivi ad un tavolo pulito (accompagnato dal waiter);
*  verificare che il waiter consegni il tè giusto al tavolo da cui è partito l’ordine;
*  verificare che il waiter ottenga il pagamento se scade il tempo massimo per la consumazione del client (maxstaytime);
*  verificare che il waiter accompagni il client alla exitdoor;
*  verificare che il sistema funzioni correttamente con più Client.

La formalizzazione dei test è nel seguente [file di testing Junit](TearoomTest.kt).
