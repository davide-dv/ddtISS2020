%====================================================================================
% tearoom description   
%====================================================================================
context(ctxtearoom, "tearoomhost",  "TCP", "8050").
context(ctxbasicrobot, "localhost",  "TCP", "8020").
context(ctxwaiter, "waiterhost",  "TCP", "8021").
context(ctxsmartbell, "smartbellhost",  "TCP", "8022").
context(ctxbarman, "barmanhost",  "TCP", "8023").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( smartbell, ctxsmartbell, "external").
  qactor( barman, ctxbarman, "external").
  qactor( timer0, ctxtearoom, "external").
  qactor( timer1, ctxtearoom, "external").
  qactor( waiter, ctxwaiter, "it.unibo.waiter.Waiter").
  qactor( walker, ctxwaiter, "it.unibo.walker.Walker").
