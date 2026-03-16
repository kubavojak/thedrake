package thedrake;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class BoardTroops implements JSONSerializable {
    private final PlayingSide playingSide;
    private final Map<BoardPos, TroopTile> troopMap;
    private final TilePos leaderPosition;
    private final int guards;

    public BoardTroops(PlayingSide playingSide) {
        this.playingSide = playingSide;
        this.troopMap = Collections.emptyMap();
        this.leaderPosition = TilePos.OFF_BOARD;
        this.guards = 0;
    }

    public BoardTroops(
            PlayingSide playingSide,
            Map<BoardPos, TroopTile> troopMap,
            TilePos leaderPosition,
            int guards) {
        this.playingSide = playingSide;
        this.troopMap = troopMap;
        this.leaderPosition = leaderPosition;
        this.guards = guards;
    }

    public Optional<TroopTile> at(TilePos pos) {
        if (!(pos instanceof BoardPos)) {
            return Optional.empty();
        }
        return Optional.ofNullable(troopMap.get(pos));
    }

    public PlayingSide playingSide() {
        return playingSide;
    }

    public TilePos leaderPosition() {
        return leaderPosition;
    }

    public int guards() {
        return guards;
    }

    public boolean isLeaderPlaced() {
        return !leaderPosition.equals(TilePos.OFF_BOARD);
    }

    public boolean isPlacingGuards() {
        return isLeaderPlaced() && guards < 2;
    }

    public Set<BoardPos> troopPositions() {
        return troopMap.keySet();
    }

    public BoardTroops placeTroop(Troop troop, BoardPos target) {
        if (troopMap.containsKey(target)) {
            throw new IllegalArgumentException();
        }

        Map<BoardPos, TroopTile> newTroopMap = new HashMap<>(troopMap);
        newTroopMap.put(target,
                new TroopTile(troop, playingSide, TroopFace.AVERS));

        if (!isLeaderPlaced()) {
            return new BoardTroops(playingSide, newTroopMap, target, 0);
        } else if (isPlacingGuards()) {
            return new BoardTroops(playingSide, newTroopMap, leaderPosition, guards + 1);
        } else {
            return new BoardTroops(playingSide, newTroopMap, leaderPosition, guards);
        }
    }

    public BoardTroops troopStep(BoardPos origin, BoardPos target) {

        if (!isLeaderPlaced() || isPlacingGuards()) {
            throw new IllegalStateException();
        }

        Optional<TroopTile> originTile = at(origin);
        Optional<TroopTile> targetTile = at(target);

        if (originTile.isEmpty() || targetTile.isPresent()) {
            throw new IllegalArgumentException();
        }

        Map<BoardPos, TroopTile> newTroopMap = new HashMap<>(troopMap);

        newTroopMap.remove(origin);
        newTroopMap.put(target, originTile.get().flipped());

        TilePos newLeaderPos = origin.equals(leaderPosition) ? target : leaderPosition;

        return new BoardTroops(playingSide, newTroopMap, newLeaderPos, guards);
    }

    public BoardTroops troopFlip(BoardPos origin) {
        if (!isLeaderPlaced()) {
            throw new IllegalStateException(
                    "Cannot move troops before the leader is placed.");
        }

        if (isPlacingGuards()) {
            throw new IllegalStateException(
                    "Cannot move troops before guards are placed.");
        }

        if (!at(origin).isPresent())
            throw new IllegalArgumentException();

        Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
        TroopTile tile = newTroops.remove(origin);
        newTroops.put(origin, tile.flipped());

        return new BoardTroops(playingSide(), newTroops, leaderPosition, guards);
    }

    public BoardTroops removeTroop(BoardPos target) {

        if (!isLeaderPlaced() || isPlacingGuards()) {
            throw new IllegalStateException();
        }
        if (at(target).isEmpty()) {
            throw new IllegalArgumentException();
        }

        Map<BoardPos, TroopTile> newTroopMap = new HashMap<>(troopMap);
        newTroopMap.remove(target);

        TilePos newLeaderPos = target.equals(leaderPosition) ? TilePos.OFF_BOARD : leaderPosition;

        return new BoardTroops(playingSide, newTroopMap, newLeaderPos, guards);
    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.print("{");
        writer.print("\"side\":");
        playingSide.toJSON(writer);
        writer.print(",\"leaderPosition\":");
        leaderPosition.toJSON(writer);
        writer.print(",\"guards\":" + guards);
        writer.print(",\"troopMap\":{");

        List<BoardPos> sortedKeys = new ArrayList<>(troopMap.keySet());
        sortedKeys.sort(Comparator.comparing(BoardPos::toString));

        for (int i = 0; i < sortedKeys.size(); i++) {
            BoardPos pos = sortedKeys.get(i);
            pos.toJSON(writer);
            writer.print(":");
            troopMap.get(pos).toJSON(writer);
            if (i < sortedKeys.size() - 1) {
                writer.print(",");
            }
        }

        writer.print("}");
        writer.print("}");
    }
}