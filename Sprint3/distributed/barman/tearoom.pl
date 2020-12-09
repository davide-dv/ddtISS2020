%====================================================================================
% tearoom description   
%====================================================================================
context(ctxwaiter, "waiterhost",  "TCP", "8021").
context(ctxbarman, "barmanhost",  "TCP", "8023").
 qactor( waiter, ctxwaiter, "external").
  qactor( barman, ctxbarman, "it.unibo.barman.Barman").
