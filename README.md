# Matrikelnummern

| Matrikelnummer | Aufgabe |
|----------------|---------|
| 2447899        | S01     |
| 1360712        | S02     | 



# II Basisaufgabe

## [01] Green Vehicle Problem
Das Green Vechicle Route Problem ist eine Erweiterung des Traveling Salesmen Problem. Das Traveling Salesmen Problem ist ein Mathematisches Problem. Dabei geht es darum zwischen verschiedenen Orten die richtige Reihenfolge der Besuche zu ermitteln um die kürzeste Route zu finden. Dabei besteht das Problem darin, dass viele Routen zwischen den einzelnen Orten bestehen und die kürzesten Routen nicht immer die schnellsten sind. Die Route dabei ist durch die folgenden Bedingungen charackterisiert:
- jeder Ort muss genau einmal besucht werden
- die Route muss an dem Ort aufhören, an dem die Route gestartet ist

Das Problem hat demzufolge in sich eine Komplexität von $n!$, da aller Routenkombinationen ausprobiert werden müssen um das beste Ergebnis zu errechnen.

Das Green Vehicle Routing Problem ist eine Erweitrerung des Problems. Dabei Wird die Regel, dass jeder Ort nur einmal angefahren werden dürfen teilweise aufgehoben. Jetzt gibt es drei Arten von Nodes/ Orte. Customer-Nodes, Depot-Node und Station-Nodes. Sowohl auf die Depot als auch die Station Nodes gilt die Regel des einmaligen Besuchens nicht. Dadurch wird es möglich mehrmals zwischen zwei Station-Nodes hin und her zu fahren bzw. kann eine Route mehrmals über die Selbe node gehen. Auf die Customer Nodes wirkt die Regel immer noch.

Des Weiteren wird neben der Variabel der Distanz auch zwei Faktoren hinzugeüfgt von denen das Optimum abhängig ist. Sowohl die Zeit als der Tank des Vehicles bzw. der Enität die entlang der Route läuft und mit der Zeit weniger wird. Die Zeit ist abhängig von der Distanz und zudem von der Art der Node. Wenn ein Vehicle einen Customer beliefert, dauert dies eine halbe Stunde. Bei den anderen eine Viertelstunde für das aufladen der Fahrzeuge. Wenn die Route zwar die kürzeste ist, allerdings über eine Gewisse Zeit heraus geht dann wird die Route auch als unmöglich eingestuft, was es komplexer macht. Weiterhin, können nicht alle Customer nodes direkt angefahren werden, da der Tank nach einer Zeit ausgeht. Somit muss der Tank aufgefüllt werden nach einer Weile, was es notwendig macht zu den einzelnen Station-Nodes oder dem Depot zu fahren. 

Es wird also die beste Route weiterhin gesucht Distanz mäßig, allerdings wird diese durch 2 weitere Variablen $c=\text{Kapzität}, t=\text{Zeit}$ abhängig.

Damit wird das Problem von einer $n!$ Komplexität noch komplizierteer mit einer Komplexität von $(n+p)!$ mit $n=\text{Anzahl Customer Nodes}$ und $p=\text{Variable Anzahl Stationnodes+ Depotnode}$. Mit  zusätzlichen Bedingungen. Bei dieser Komplexität wird es schwierig alle möglichen Kombinationen herauszubekommen, da diese unendlich viele sind. Ab einer gewissen Größer wird die LAufzeit auch so hoch, dass es selbst mit superrechnern nicht mehr effizient Errechenbar ist. Somit muss hier auf andere Arten der Problemlösung zurückgegriffen werden.

Der Name des GVRP kommt davon, dass hier versucht wird Pakete zu beliefern zu verschiedenen Personen und verschiedene Orte. Diese sind allerdings weit voneinander entfernt und das Fahrzeug muss immer wieder den Tank auffüllen um weiter gehen zu können bzw. die Batterie aufladen. Dadurch entstehen die oben genannten zusätzlichen Probleme.