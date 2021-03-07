package gg.data;

import gg.models.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import gg.services.answerService;
import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Repository
public class GameImplementation implements GameDao{

@Autowired
    answerService as;
    private final JdbcTemplate jdbcTemplate;




    @Autowired
    public GameImplementation(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public int begin(){
        final String sql = "INSERT INTO Game(Answer, Status) VALUES(?,?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        Random rand = new Random();

        jdbcTemplate.update((Connection conn) -> {

            PreparedStatement statement = conn.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS);

            String ans = as.generateAnswer();
            //call service for answer int ans =
            System.out.println(ans);
            statement.setString(1, ans);
            statement.setBoolean(2,false);
            return statement;

    }, keyHolder);

        return keyHolder.getKey().intValue();
    }





    @Override
    public List<Game> game(){
        final String sql = "SELECT gameId, status , CASE status WHEN  'false' THEN  '0000' ELSE  answer END  AS 'Answer' FROM Game;";

        return  jdbcTemplate.query(sql, new GameMapper());


    }
    @Override
    public Game getGameById(int gameId){
        final String sql = "SELECT gameId, status , CASE status WHEN 'false' THEN  '0000' ELSE  answer END  AS 'Answer' FROM Game WHERE gameId = ?;";

        return jdbcTemplate.queryForObject(sql, new GameMapper(), gameId);

    }
    @Override
    public Game getGameByIdAdmin(int gameId){

        final String sql = "SELECT gameId, answer, status FROM Game WHERE gameId = ?;";
        return jdbcTemplate.queryForObject(sql, new GameMapper(), gameId);

    }
    @Override
    public void changeGameState(int gameId){
        final String sql = "update Game set status = 1 where gameId = ?;";

        jdbcTemplate.update(sql, gameId);

    }



    private static final class GameMapper implements RowMapper<Game> {

        @Override
        public Game mapRow(ResultSet rs, int index) throws SQLException {
            Game td = new Game();
            td.setGameId(rs.getInt("GameId"));
            td.setAnswer(rs.getString("Answer"));
            td.setStatus(rs.getBoolean("Status"));

            return td;
        }
    }

}
