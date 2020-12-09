%====================================================================================
% tearoom description   
%====================================================================================
context(ctxtearoom, "127.0.0.1",  "TCP", "8050").
context(ctxbasicrobot, "localhost",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( waiter, ctxtearoom, "it.unibo.waiter.Waiter").
  qactor( smartbell, ctxtearoom, "it.unibo.smartbell.Smartbell").
  qactor( barman, ctxtearoom, "it.unibo.barman.Barman").
  qactor( walker, ctxtearoom, "it.unibo.walker.Walker").
  qactor( timer0, ctxtearoom, "it.unibo.timer0.Timer0").
  qactor( timer1, ctxtearoom, "it.unibo.timer1.Timer1").
  qactor( knowledgebase, ctxtearoom, "it.unibo.knowledgebase.Knowledgebase").
