package gg.services;

import gg.data.GameDao;
import gg.data.GameImplementation;
import gg.models.Game;
import gg.models.Round;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Repository
public class guessServiceImplementation implements guessService{
    @Autowired
    GameDao gameDao;
    private final JdbcTemplate jdbcTemplate;




    @Autowired
    public guessServiceImplementation(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

@Override
    public String calcResult(Round r){

        int exact = 0;
        int partial = 0;
        Game currentGame = gameDao.getGameByIdAdmin(r.getGameId());
        String answer = currentGame.getAnswer();
        String guess = String.valueOf(r.getGuess());
        System.out.println(currentGame.getAnswer());
        for(int i = 0; i < answer.length(); i++){

            if(answer.charAt(i) == (guess.charAt(i))){
                System.out.println("exact" + guess.charAt(i));
                exact+=1;
            }else if(answer.contains(String.valueOf(guess.charAt(i)))){
                System.out.println("partial" + guess.charAt(i));
                partial+=1;
            }
        }
        if(exact == 4){

            currentGame.setStatus(true);
            gameDao.changeGameState(currentGame.getGameId());

        }
        String result = "e:"+String.valueOf(exact) +":p:" +String.valueOf(partial);
        return result;
    }
    @Override
    public Timestamp getTime(){
    Date d = new Date();
    Timestamp ts = new Timestamp(d.getTime());

    return ts;
    }

}
