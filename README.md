# MeetingMatching
Es gibt eine Java-Variante und eine HTML-Variante.
Die Java-Variante ist zuverlässiger, benötigt jedoch eine Java-Installation der Version 11 auf dem PC.
Die HTML-Version benötigt lediglich einen Browser. Hier gibt es jedoch leider Probleme auf manchen PCs und der Algorithmus selbst ist zufallsbasierter und dadurch zu beginn besser aber letztendlich anfälliger.
## Wie Downloade ich das?
https://github.com/TimVanDerMeulen/MeetingMatching/releases
Hier findest du alle Releases des Tools. Es gibt eine Java-Variante und eine HTML-Variante.

Unter Assets gewünschtes Format z.B. ZIP-Datei für HTML oder Jar-Datei für Java auswählen und speichern. Du brauchst nur die HTML-Datei behalten. Wo du die abspeicherst ist egal.

## Wo mache ich die Dateien hin?
Du brauchst nur die HTML-Datei aus dem Download.  Wo du die abspeicherst ist egal. Genauso ist es egal, wo du die Dateien speicherst, die davon generiert werden. Du solltest sie nur wiederfinden. ;)

## Was mache ich jetzt damit?
#### HTML
- HTML-Datei mit Browser öffnen (Sollte normalerweise mit Doppelklick funktionieren)
- Option für 
 - neue Teilnehmerliste oder 
 - Verwenden von Daten aus vorherigem Durchlauf 
 auswählen. (Da muss dann noch die richtige Datei eingefügt werden)
- aktuelle Teilnehmerliste auswählen und Datei einfügen
- neuen Durchlauf mit Button unter Aktionen generieren
- Speichern von Ergebnis CSV und internen Daten für späteren Durchlauf mit Buttons unter Aktionen

#### JAVA
- Die Jar per Doppelklick starten. 
   - Sollte dies nicht funktionieren und stattdessen nichts tun oder die Datei als Ordner öffnen: Windowstaste + X  ->  ```java -jar <Jar-Dateiname>```
- Falls vorhanden eine alte Datei laden (Bei späteren Durchläufe wird automatisch jedes Mal die intern generierte Datei geladen)
- Teilnehmerliste auswählen im zweiten Datei-Selektor
- Aktionen im entsprechenden Bereich ausführen
  - zunächst neuen Durchlauf berechnen
  - Speichern 
       - fragt ab, wo die Datei abgelegt werden soll und wie diese heien soll
	   - speichert internes Format in Applicationsordner ab, um später den aktuellen Stand wiederherstellen zu können (internes Verfahren; benötigt kein Wissen oder sonst was)

## Wie bekomme ich die bestehenden Daten weg? / Wie fange ich neu an?
Mit STRG + F5 werden alle default geladenen Daten verworfen und müssen erneut per Datei-Selektor / -Upload ausgewählt werden
