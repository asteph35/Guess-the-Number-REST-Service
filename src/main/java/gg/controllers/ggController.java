package gg.controllers;
import gg.services.guessService;
import org.springframework.jdbc.core.JdbcTemplate;
import gg.data.GameDao;
import gg.data.RoundDao;
import gg.models.Game;
import gg.models.Round;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guessinggame")

public class ggController {
    @Autowired
    GameDao gameDao;

    @Autowired
    RoundDao roundDao;
@Autowired
guessService gs;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ggController(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/begin")
    @ResponseStatus(HttpStatus.CREATED)
    public int begin()
    {
        return gameDao.begin();
    }

    @GetMapping("/game")
    public List<Game> game() {
        return gameDao.game();
    }


    @GetMapping("/game/{gameId}")
    public ResponseEntity<Game> getGameById(@PathVariable int gameId) {
        Game result = gameDao.getGameById(gameId);
        if (result == null) {
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(result);


    }
    @PostMapping("/guess")
    @ResponseStatus(HttpStatus.CREATED)
    public Round guess(@RequestBody Round r)
    {
        String result = gs.calcResult(r);
        r.setResult(result);


        return roundDao.guess(r);
    }



    @GetMapping("/round/{gameId}")
    public List<Round> rounds(@PathVariable int gameId) {
        return roundDao.rounds(gameId);


    }



}
