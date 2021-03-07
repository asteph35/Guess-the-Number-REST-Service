package gg.data;
import gg.models.Round;

import java.util.List;

public interface RoundDao {

    Round guess(Round r);
    List<Round> rounds(int gameId);
    void update(Round r);
}
