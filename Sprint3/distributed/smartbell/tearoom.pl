%====================================================================================
% tearoom description   
%====================================================================================
context(ctxtearoom, "tearoomhost",  "TCP", "8050").
context(ctxwaiter, "waiterhost",  "TCP", "8021").
context(ctxsmartbell, "smartbellhost",  "TCP", "8022").
 qactor( waiter, ctxwaiter, "external").
  qactor( smartbell, ctxsmartbell, "it.unibo.smartbell.Smartbell").
