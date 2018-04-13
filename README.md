# Pagotto
Per quando 5IA è chiusa

## PendingIntentRadioButton
In questa attività si è andato a modificare l'esempio AM003, l'obiettivo è di aggiungere dei bottoni di selezione per i vari Flag di PendingIntent.

- `FLAG_CANCEL_CURRENT` Indica che, se il PendingIntent descritto esiste già quello corrente deve essere cancellato prima di poterne generare un altro.
- `FLAG_IMMUTABLE` Indica che il PendingIntent appena creato non può essere cambiato (*immutable*). Ciò vuole inoltre dire che gli argomenti passati all'intent per la popolazione dello stesso verranno ignorati.
- `FLAG_NO_CREATE` Indica che, se il PendingIntent descritto non esiste già il metodo ritornerà `NULL` al posto di creare l'intent.
- `FLAG_ONE_SHOT` Indica che il PendingIntent potrà essere usato solo una volta. dopo il comando `Send()` il PendingIntent sarà automaticamente cancellato e non sarà più utilizzabile.
- `FLAG_UPDATE_CURRENT` Indica che, se il PendingIntent descritto esiste già, il PendingIntent verrà mantenuto ma i suoi Extra Data verranno aggiornati
