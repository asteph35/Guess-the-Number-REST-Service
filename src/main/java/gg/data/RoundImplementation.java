package gg.data;

import gg.models.Game;
import gg.models.Round;
import gg.services.guessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
@Repository
public class RoundImplementation implements RoundDao{
    @Autowired
    guessService gs;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RoundImplementation(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }



    @Override
    public Round guess(Round r){
        final String sql = "INSERT INTO round(gameId, guess, guessTime) VALUES(?,?, ?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update((Connection conn) -> {

            PreparedStatement statement = conn.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS);
            Timestamp curr = gs.getTime();
            statement.setInt(1, r.getGameId());
            statement.setInt(2, r.getGuess());
            statement.setTimestamp(3,curr);
            r.setGuessTime(curr);

            return statement;

        }, keyHolder);
        r.setRoundId(keyHolder.getKey().intValue());

        update(r);

        return r;
    }

    @Override
    public void update(Round r) {
System.out.println(r.getRoundId());
        final String sql = "UPDATE round SET "
                + "result = ? "
                + "WHERE roundId = ?;";

        jdbcTemplate.update(sql,
                r.getResult(),
                r.getRoundId());
    }
    @Override
    public List<Round> rounds(int gameId){
        final String sql = "SELECT roundId, gameId, guess, guessTime, result FROM Round WHERE gameId = ? order by guessTime;";

        return jdbcTemplate.query(sql, new RoundMapper(), gameId);
    }

    private static final class RoundMapper implements RowMapper<Round> {

        @Override
        public Round mapRow(ResultSet rs, int index) throws SQLException {
            Round td = new Round();
            td.setRoundId(rs.getInt("RoundId"));
            td.setGameId(rs.getInt("GameId"));
            td.setGuess(rs.getInt("Guess"));
            td.setGuessTime(rs.getTimestamp("GuessTime"));
            td.setResult(rs.getString("Result"));
            return td;
        }
    }
}


