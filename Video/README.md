# Descrizione Video
In questa cartella sono presenti i video demo dei test Junit e di una esecuzione manuale.

Per quanto riguarda i nomi dei file `test*` corrispondono a quelli delle funzioni incluse nei file di Junit dei vari Sprint.

Il file `runGUI` mostra invece una esecuzione manuale del sistema, compreso di simulatore, interfaccia *Manager* e *Client*.

Di seguito una breve descrizione dei test:
|FILE|DESCRIZIONE|
|----|---|
|[testCoap](testCoap.mp4)|test con un *Client* che esegue tutte le operazioni (`notify`,`order`,`pay`)|
|[testCoapFullRoom](testCoapFullRoom.mp4)|test con 2 *Client* che accedono ed un terzo che prova ad entrare senza successo|
|[testCoapFullRoomOneTimer](testCoapFullRoomOneTimer.mp4)|test con 2 *Client*, un *Client* fa scadere il *MaxStayTime*|
|[testCoapFullRoomTwoTimer](testCoapFullRoomTwoTimer.mp4)|test con 2 *Client*, entrambi fanno scadere il *MaxStayTime*|
|[testCoapFullRoomThirdClient](testCoapFullRoomThirdClient.mp4)|test in cui il terzo *Client* chiede di entrare dopo che si sono liberati i 2 tavoli|
|[testKnowledgeBase](testKnowledgeBase.mp4)|test con un *Client* per verificare il funzionamento della *knowledgebase* centralizzata|


