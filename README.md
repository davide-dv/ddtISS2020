# Progetto esame ISS2020
## Organizzazione Sprint
|Sprint           |Obiettivi                      |
|-----------------|-------------------------------|
|Sprint0	      |Scrittura dell'analisi dei requisiti per l'intero progetto, con confronto delle tecnologie da adottare        |
|Sprint1          |Analisi e sviluppo di una prima versione del sistema, compreso di collegamento al simulatore ed interfaccia grafica            |
|Sprint2          |Analisi e sviluppo del sistema, considerando il caso di *Client* multipli; raffinamento dell'interfaccia grafica|
|Sprint3          |Revisione della modalità di rappresentazione dello stato del sistema; distribuzione su più nodi|

## Esecuzione Sprint
In tutti gli Sprint sono presenti  le classi di testing **Junit** utili per i test automatizzati di varie funzionalità.
Inoltre a partire dallo Sprint1 è possible:
* visualizzare gli spostamenti del *Waiter* tramite il simulatore (Virtualrobot);
* simulare le azioni del *Client* grazie all'interfaccia grafica;
* visualizzare lo stato corrente della *Tearoom* dall'interfaccia grafica del *Manager*.

É quindi possibile testare il codice sia in modo automatico che manuale.

### Esecuzione test Junit

1. posizionarsi nella cartella `[bin](Utils/it.unibo.qak20.basicrobot-1.0/bin)`
2. eseguire il file `[it.unibo.qak20.basicrobot](Utils/it.unibo.qak20.basicrobot-1.0/bin/it.unibo.qak20.basicrobot)` (o con estensione `.bat` su Windows) per avviare il **basicrobot**
4.  posizionarsi nella cartella `tearoom`
5. eseguire il comando `gradle -b build_ctxtearoom.gradle test` per avviare i test

**NOTA**: di default sarà attivo soltanto un test. Per eseguire tutti i test disponibili è necessario aprire il file TestTearoom.kt relativo allo sprint di interesse e selezionare il metodo di test da eseguire.

### Esecuzione con interfaccia grafica e Simulatore
1. posizionarsi nella cartella `[bin](Utils/it.unibo.qak20.basicrobot-1.0/bin)`
2. eseguire il file `[it.unibo.qak20.basicrobot](Utils/it.unibo.qak20.basicrobot-1.0/bin/it.unibo.qak20.basicrobot)` (o con estensione `.bat` su Windows) per avviare il **basicrobot**
3. posizionarsi nella cartella `[src](Utils/it.unibo.virtualRobot2020/node/WEnv/server/src)` 
4. eseguire il comando `node main 8999` per avviare il **VirtualRobot** (simulatore)
5. andare alla pagina web con indirizzo `localhost:8090` per vedere la simulazione
6. posizionarsi nella cartella `Sprint* > WebUI > manager`
7. eseguire il comando `gradle run`
8. aprire la pagina web del *Client* al percorso `Sprint* > WebUI > frontend > index.html` e/o quella del *Manager* al percorso `Sprint* > WebUI > frontend > manager.html`
9. posizionarsi nella cartella `Sprint* > tearoom`
10. eseguire il comando `gradle -b build_ctxtearoom.gradle test` per **avviare i test** o in alternativa utilizzare `gradle -b build_ctxtearoom.gradle run` per **avviare il sistema** (senza test Junit)

## Utilizzo interfaccia web Client
Per effettuare dei test manuali è possibile utilizzare l'interfaccia web Client come descritto al punto 8. del paragrafo precedente.
Di seguito vengono mostrate le operazioni possibili.
