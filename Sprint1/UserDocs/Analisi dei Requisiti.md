# Analisi dei Requisiti

Per il primo sprint, verranno considerati i seguenti vincoli:
*   nel sistema c’è soltanto un Client 
*   il Client esegue le operazioni correttamente 
*   un nuovo Client non chiede mai di entrare prima che sia uscito l’altro
*   il MaxStayTime non viene considerato

I casi d’uso considerati in questo Sprint sono gli stessi definiti nell’analisi generale, e sono definiti formalmente insieme ai requisiti presi in considerazione all’interno del file [qak](analisi.qak).

## TestPlan
Il TestPlan dello Sprint comprende i seguenti punti:
*   garantire l’accesso al client soltanto se la sua temperatura è inferiore a 37.5°C e se c’è almeno un tavolo pulito. Verifica che il client riceva un Client ID dalla smartbell;
*   verificare che il client arrivi ad un tavolo pulito (accompagnato dal waiter);
*   verificare che il waiter consegni il tè giusto al tavolo da cui è partito l’ordine;
*   verificare che il waiter accompagni il client alla exitdoor.

La formalizzazione dei test è nel seguente [file di testing Junit](../tearoom/src/test/kotlin/tearoom/TearoomTest.kt).
