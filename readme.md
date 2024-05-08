# PCAD 23/24 - Laboratorio 4


L’obiettivo di questo laboratorio è di programmare un applicazione distribuita di produttore/consumatore in una rete.

L’idea è di avere un server TCP che aspetta delle connessione su un port scelto da voi e su questo server si possono connettere
due tipi di clienti:

1. I clienti produttore che mandano delle string,
2. I clienti consumatore che ricevono delle string prodotte.

Il protocollo di comunicazione deve funzionare in questo modo:

- per i clienti produttore: dopo la connessione, il cliente manda un messaggio"producer\n"(le aspice non fanno parte dei messaggi) poi aspetta un messaggio dal server"okprod\n"e poi il cliente manda una string senza carattere'\n'in mezzo che finisce con'\n'e si scollega. La string prodotta viene aggiunta ad una struttura FIFO dal server.
- per i clienti consumatore: dopo la connessione, il cliente manda un messaggio"consumer\n"poi aspetta un primo messaggio dal server "okcons\n"e un altro messaggio senza carattere '\n' in mezzo che finisce con '\n' e chi corrisponde alla string più vecchia contenuta nella struttura FIFO del server. La string consumata viene cancellata dalla struttura FIFO.

Il client rimane connesso finche ottiene una string (se la struttura FIFO è vuota, aspetterà). Dopo avere ottenuto la string, il cliente si scollega.

Dovete fare due versione del server, una in cui la lunghezza della struttura FIFO non è limitata, ed un altra dove è limitata.
Per questo ultimo caso, un produttore non può aggiungere una string quando la struttura è piena e deve aspettare (senza scollegarsi di potere inserire la string nella struttura).

Il vostro server deve potere accettare più connessione di clienti allo stesso momento, deve proteggere l’accesso concorrente alla struttura FIFO e deve fare in modo che quando un cliente aspetta, il thread associato non è in attesa attiva.

Dovete anche programmare dei clienti per testare il comportamento del vostro server è puo essere una buona idea, provare ad usare i clienti/server di altri gruppi di studenti.