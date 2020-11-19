# Analisi dei Requisiti

Per il secondo sprint consideriamo i seguenti vincoli:● nel sistema ci sono fino a 3 Client
    ● i Client eseguono le operazioni correttamente

Di conseguenza il Waiter deve poter gestire più messaggi provenienti da uno dei Client, dalla Smartbell o dal Barman in modo parallelo, il tutto senza perdita di messaggi.

E’ necessario anche controllare che ognuno dei Client impieghi un tempo minore o uguale al MaxStayTime per la consumazione.

Il conteggio del tempo deve partire dal momento in cui il tè viene consegnato al Client, in modo da non essere influenzato da eventuali ritardi del Waiter o del Barman.

La scadenza del tempo massimo è individuale per ogni tavolo.

I casi d’uso considerati in questo Sprint sono gli stessi definiti nell’analisi generale, e sono definiti formalmente insieme ai requisiti presi in considerazione all’interno del file [qak linkato](analisi.qak).

Lo stesso vale per il testPlan definito all’interno del seguente [file di testing Junit](TearoomTest.kt).
