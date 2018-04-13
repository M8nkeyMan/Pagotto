# AM003_Receiver

In questo esercizio vediamo il secondo (in ordine di esposizione) dei componenti: il `BoradcastReceiver`. Invitiamo subito alla lettura della documentazione: [qui](https://developer.android.com/guide/components/broadcasts.html). Per le API: [qui](https://developer.android.com/reference/android/content/BroadcastReceiver.html).

## Publish-Subscribe pattern

E' il modello di riferimento per questa particolare gestione dei messaggi; una sua descrizione tratta da Wikipedia (en): [qui](https://en.wikipedia.org/wiki/Publish%E2%80%93subscribe_pattern).

## Manifest-declared receivers

E' la prima tipologia, esemplificata in questo esempio ([qui](https://developer.android.com/guide/components/broadcasts.html)). Nel nostro caso chiameremo il broadcast addirittura in modo esplicito con un Intent di siffatta specie.

## Context-registered receivers

In caso si voglia procedere con una maggiore flessibilità si procede in modo programmatico ([qui](https://developer.android.com/guide/components/broadcasts.html)).

Nel nostro esempio il `BoradcastReceiver` si trova all'interno della stessa app, comunque la comunicazione avviene in questo senso
```
i) creazione dell'intent esplicito ---> 
ii) l'Intent viene consegnato al service di allarme come PendingIntent ---> 
iii) Android con il suo servizio di allarme rilancia l'intent ed il nostro Broadcast (e non altri) lo riceve.
```

## Wrapping di un Intent

Viene utilizzato un `PendingIntent`; per la API: [qui](https://developer.android.com/reference/android/app/PendingIntent.html).
