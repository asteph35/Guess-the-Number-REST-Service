package gg.services;

import gg.models.Round;

import java.sql.Timestamp;

public interface guessService {

String calcResult(Round r);
Timestamp getTime();

}
