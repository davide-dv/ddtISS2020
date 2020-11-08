%====================================================================================
% tearoom description   
%====================================================================================
context(ctxtearoom, "192.168.1.125",  "TCP", "8050").
context(ctxbasicrobot, "localhost",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( waiter, ctxtearoom, "it.unibo.waiter.Waiter").
  qactor( smartbell, ctxtearoom, "it.unibo.smartbell.Smartbell").
  qactor( barman, ctxtearoom, "it.unibo.barman.Barman").
  qactor( walker, ctxtearoom, "it.unibo.walker.Walker").
