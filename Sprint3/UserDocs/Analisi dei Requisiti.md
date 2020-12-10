# Analisi dei Requisiti

Per il secondo sprint consideriamo i seguenti vincoli:

    ● i Client eseguono le operazioni correttamente

## Miglioramento della flessibilità del sistema

Il sistema deve essere in grado di recepire eventuali aggiunte future espresse dal cliente, ad esempio la possibilità di mantenere uno storico degli stati dei vari attori. 

Allo stato attuale, il sistema non è in grado di mostrare un’evoluzione degli stati, ma solo di fornire un’istantanea in un dato momento. 

L’eventuale estensione del sistema dovrà poter avvenire nel modo più immediato possibile, senza cambiamenti sostanziali. 

I casi d’uso considerati in questo Sprint sono gli stessi definiti nell’analisi generale, e sono definiti formalmente insieme ai requisiti presi in considerazione all’interno del file .qak linkato.

## TestPlan

Il TestPlan dello Sprint comprende i seguenti punti:
*   garantire l’accesso al client soltanto se la sua temperatura è inferiore a 37.5°C e se c’è almeno un tavolo pulito. Verifica che il client riceva un Client ID dalla smartbell;
*   verificare che il client arrivi ad un tavolo pulito (accompagnato dal waiter);
*   verificare che il waiter consegni il tè giusto al tavolo da cui è partito l’ordine;
*   verificare che il waiter ottenga il pagamento se scade il tempo massimo per la consumazione del client (maxstaytime);
*   verificare che il waiter accompagni il client alla exitdoor;
*   verificare che il sistema funzioni correttamente con più Client.

La formalizzazione dei test è nel seguente [file di testing Junit](TearoomTest.kt).
