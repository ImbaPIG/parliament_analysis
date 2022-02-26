# Gruppe_1_3

ParliamentSentimentRadar


Benutzerhandbuch unserer Projektarbeit

Starten des Projekt:

Damit das Projekt überhaupt funktioniert muss man die Datei server.java als main ausführen.
Das starten den Spark Server und die Restschnittstelle ist mit der Datenbank verbunden.

Der nächste Schritt wäre dann die Index.html im frontend Ordner zu öffnen. Das öffnet im Standardbrowser unser Dashboard.
Es dauert einen kurzen Moment bis alle daten geladen sind.


Daten in die Datenbank einlesen:

Um die Daten in die Datenbank einzulesen muss der Protokollhandler gestartet werden. Dieser funktioniert auch wenn man von mehreren Geräten aus hochlädt denn es wird überprüft
ob das jeweilige Protokoll schon drin ist oder nicht. Es gibt kein explitzen Button oder Konsoleneingabe um den Projekthandler zu starten also muss man das selber machen. Dazu kann man einfach die Main im Projekthandler ausführen.



Erklärung des Dashbaords:

Im oberen Teil des Dashbaords findet man dann ein Titel und einige Filter Informationen um ein neues Dashbaord hinzuzufügen. 
Das neue Dashbaord wird am unteren Ende der Siete angefügt und kann auch wieder entfernt werden.
Die Filter behinalten Filter nach Partei, Fraktion und Redner sowie Start und Enddatum von wann bis wann man manche Daten haben will.
Die einzelnen Visualisierungen sind dementsprechend benannt und es wurden alle Charts implementiert die in der Aufgabenstellung verlangt wurde.


Volltextsuche:
Anfnags ist ein umakierter Text als Dummy Text geladen. Mit der Suchfunktion im Header von der Card kann man einzelne Reden suchen und
die ausgewählte Rede wird dann gehighlighted. Es werden zudem auch die Legende anzeigt. Die Sentiments können mit dem InfomationsIcon am Ende eines Satztes angezeogt werden,
indem auf das Icon mit der Maus geklickt wird.


Das GanttDiagramm und die Projektdokumentation sowie alle von der Aufgabenstellung verlangten Diagramme sind auch im Proejektordner zu finden.
