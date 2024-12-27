DROP DATABASE IF EXISTS UniMeet;
CREATE DATABASE UniMeet;
USE UniMeet;

CREATE TABLE studente(
matricola VARCHAR(30) PRIMARY KEY NOT NULL,
nome VARCHAR(30) NOT NULL,
cognome VARCHAR(30) NOT NULL,
email VARCHAR(30) NOT NULL,
passwordHash VARCHAR(255) NOT NULL,
domandaSicurezza VARCHAR(30) NOT NULL,
risposta VARCHAR(30) NOT NULL
);
CREATE TABLE  professore(
codice VARCHAR(30) PRIMARY KEY NOT NULL,
nome VARCHAR(30) NOT NULL,
cognome VARCHAR(30) NOT NULL,
ufficio VARCHAR(30) NOT NULL,
email VARCHAR(30) NOT NULL,
passwordHash VARCHAR(255) NOT NULL,
domandaSicurezza VARCHAR(30) NOT NULL,
risposta VARCHAR(30) NOT NULL
);
CREATE TABLE insegnamento(
    codice INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
    nome VARCHAR(30) NOT NULL,
    codiceProfessore VARCHAR(30) NOT NULL,
    FOREIGN KEY(codiceProfessore) REFERENCES professore(codice) 
);

CREATE TABLE ricevimento(
    codice INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
    giorno VARCHAR(30) NOT NULL,
    ora VARCHAR(30) NOT NULL,
    note VARCHAR(200) NOT NULL,
    codiceProfessore VARCHAR(30) NOT NULL,
    FOREIGN KEY(codiceProfessore) REFERENCES professore(codice)
);

CREATE TABLE prenotazioneRicevimento(
    codice INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
    giorno VARCHAR(30) NOT NULL,
    ora VARCHAR(30) NOT NULL,
    note VARCHAR(200) NOT NULL,
    codiceProfessore VARCHAR(30) NOT NULL,
    matricolaStudente VARCHAR(30) NOT NULL,
    FOREIGN KEY (codiceProfessore) REFERENCES professore(codice),
    FOREIGN KEY (matricolaStudente) REFERENCES studente(matricola)
);
