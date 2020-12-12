# Analisi dei Requisiti

Dopo la retrospective dello Sprint precedente è emersa la necessità di avere un sistema più flessibile per nuove funzionalità relative allo stato dello stesso.
Per questo motivo, in questo Sprint non si procederà alla analisi dei requisiti definiti del committente come fatto in quelli precedenti, ma si andrà ad operare su un requisito non funzionale.

## Miglioramento della flessibilità del sistema

Il sistema deve essere in grado di recepire eventuali richieste future espresse dal cliente, ad esempio la possibilità di mantenere uno storico degli stati dei vari attori. 

Attualmente il sistema non è in grado di mostrare un’evoluzione degli stati, ma solo di fornire un’istantanea in un dato momento. 

L’eventuale estensione del sistema dovrà poter avvenire nel modo più immediato possibile, senza cambiamenti sostanziali. 

## TestPlan

Per il terzo sprint consideriamo i seguenti vincoli:
* i Client eseguono le operazioni correttamente

Il TestPlan dello Sprint comprende i seguenti punti:
*   garantire l’accesso al client soltanto se la sua temperatura è inferiore a 37.5°C e se c’è almeno un tavolo pulito. Verifica che il client riceva un Client ID dalla smartbell;
*   verificare che il client arrivi ad un tavolo pulito (accompagnato dal waiter);
*   verificare che il waiter consegni il tè giusto al tavolo da cui è partito l’ordine;
*   verificare che il waiter ottenga il pagamento se scade il tempo massimo per la consumazione del client (maxstaytime);
*   verificare che il waiter accompagni il client alla exitdoor;
*   verificare che il sistema funzioni correttamente con più Client.

La formalizzazione dei test è nel seguente [file di testing Junit](../tearoom/src/test/kotlin/tearoom/TearoomTest.kt).
