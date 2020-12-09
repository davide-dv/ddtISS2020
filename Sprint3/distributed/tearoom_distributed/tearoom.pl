%====================================================================================
% tearoom description   
%====================================================================================
context(ctxtearoom, "tearoomhost",  "TCP", "8050").
context(ctxwaiter, "waiterhost",  "TCP", "8021").
context(ctxsmartbell, "smartbellhost",  "TCP", "8022").
context(ctxbarman, "barmanhost",  "TCP", "8023").
 qactor( smartbell, ctxsmartbell, "external").
  qactor( waiter, ctxwaiter, "external").
  qactor( barman, ctxbarman, "external").
  qactor( timer0, ctxtearoom, "it.unibo.timer0.Timer0").
  qactor( timer1, ctxtearoom, "it.unibo.timer1.Timer1").
  qactor( knowledgebase, ctxtearoom, "it.unibo.knowledgebase.Knowledgebase").
