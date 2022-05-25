# Gruppe_1_3

ParliamentSentimentRadar

NLP (spaCy) basierte Analyse aller Bundestag Reden
! Achtung MongoDB nurnoch Read möglich !

Benutzerhandbuch der Projektarbeit

Starten des Projekt:

Damit das Projekt überhaupt funktioniert muss man die Datei server.java als main ausführen.
Das starten den Spark Server und die Restschnittstelle ist mit der Datenbank verbunden.

Der nächste Schritt wäre dann die Index.html im frontend Ordner zu öffnen. Das öffnet im Standardbrowser unser Dashboard.
Es dauert einen kurzen Moment bis alle daten geladen sind.


Daten in die Datenbank einlesen:

Um die Daten in die Datenbank einzulesen muss der Protokollhandler gestartet werden. Dieser funktioniert auch wenn man von mehreren Geräten aus hochlädt denn es wird überprüft ob das jeweilige Protokoll schon uploaded ist oder nicht. Dazu import Button im Dashboard betätigen.



Erklärung des Dashbaords:

Im oberen Teil des Dashbaords findet man dann ein Titel und einige Filter Informationen um ein neues Dashbaord hinzuzufügen. 
Das neue Dashbaord wird am unteren Ende der Siete angefügt und kann auch wieder entfernt werden.
Die Filter behinalten Filter nach Partei, Fraktion und Redner sowie Start und Enddatum von wann bis wann man manche Daten haben will.
Die einzelnen Visualisierungen sind dementsprechend benannt und es wurden alle Charts implementiert die in der Aufgabenstellung verlangt wurde.
