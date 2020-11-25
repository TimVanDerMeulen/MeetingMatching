# MeetingMatching
Es gibt eine Java-Variante und eine HTML-Variante.
Die Java-Variante ist zuverlässiger, benötigt jedoch eine Java-Installation der Version 11 auf dem PC.
Die HTML-Version benötigt lediglich einen Browser. Hier gibt es jedoch leider Probleme auf manchen PCs und der Algorithmus selbst ist zufallsbasierter und dadurch zu beginn besser aber letztendlich anfälliger.

## Probleme?
#### HTML
Versurch die Java-Version. HTML wird nicht mehr supportet.
#### JAVA
Wenn die bat-Datei nicht mehr funktioniert:
 - überprüfe den Namen der Jar-Datei. Dieser muss 'MeetingMatch-1.0-SNAPSHOT' bzw 'MeetingMatch-1.0-SNAPSHOT.jar' lauten, je nachdem, ob eine Dateiendung mit angezeigt wird oder nicht.
 - liegt die Jar-Datei direkt neben der bat-Datei?
 - Shift + Rechsklick im Ordner -> Powershell -> ```.\MeetingMatch.bat``` -> Output lesen und ggf. mir schreiben =)

## Wie Downloade ich das?
https://github.com/TimVanDerMeulen/MeetingMatching/releases
Hier findest du alle Releases des Tools. Es gibt eine Java-Variante und eine HTML-Variante.

Unter Assets gewünschtes Format z.B. ZIP-Datei für HTML oder Jar-Datei für Java auswählen und speichern. 
Für die HTML-Variante brauchst du nur die HTML-Datei behalten. Wo du die abspeicherst ist egal.
Wenn du die Java-Variante verwenden möchtest, lohnt es sich ggf. die bat-Datei aus der Zip ebenfalls zu Downloaden/Speichern und neben die Jar-Datei zu legen. Damit kannst du das Programm starten, falls Doppelklick auf der Jar-Datei nicht funktioniert bzw. nicht direkt das Programm sondern etwas anderes (z.B. 'Wähle ein Programm zum öffnen dieser Datei aus' oder ein Ordner geht auf) öffnet. 

## Wo mache ich die Dateien hin?
Du brauchst nur die HTML- oder Jar-Datei aus dem Download.  Wo du die abspeicherst ist egal. Genauso ist es egal, wo du die Dateien speicherst, die davon generiert werden. Du solltest sie nur wiederfinden. ;)

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
- Die Jar per Doppelklick starten oder die bat-Datei verwenden. 
   - Sollte dies beides nicht funktionieren und stattdessen nichts tun oder die Datei als Ordner öffnen: Shift + Rechtsklick im Ordner  -> Powershell Konsole -> ```java -jar <Jar-Dateiname>```
- Falls vorhanden eine alte Datei laden (Bei späteren Durchläufen wird automatisch jedes Mal die intern generierte Datei geladen)
- Teilnehmerliste auswählen im zweiten Datei-Selektor
- Aktionen im entsprechenden Bereich ausführen
  - zunächst neuen Durchlauf berechnen
  - Speichern 
       - fragt ab, wo die Datei abgelegt werden soll und wie diese heien soll
	   - speichert internes Format in Applicationsordner ab, um später den aktuellen Stand wiederherstellen zu können (internes Verfahren; benötigt kein Wissen oder sonst was)

## Wie bekomme ich die bestehenden Daten weg? / Wie fange ich neu an?

#### HTML
Mit STRG + F5 werden alle default geladenen Daten verworfen und müssen erneut per Datei-Selektor / -Upload ausgewählt werden

#### JAVA
Einfach die dazugehörige Aktion im 'Aktionen'-bereich in der Applikation ausführen
