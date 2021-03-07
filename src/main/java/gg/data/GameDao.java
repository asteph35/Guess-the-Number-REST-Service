package gg.data;
import gg.models.Game;

import java.util.List;

public interface GameDao {


    int begin();
    List<Game> game();
    Game getGameByIdAdmin(int gameId);
    Game getGameById(int gameId);
    void changeGameState(int gameId);

}
