package thedrake;

public class TroopTile implements Tile {


    private final Troop troop;
    private final PlayingSide side;
    private final TroopFace face;

    public TroopTile(Troop troop, PlayingSide side, TroopFace face) {
        this.troop = troop;
        this.side = side;
        this.face = face;
    }

    public PlayingSide side(){
        return side;
    }

    public TroopFace face(){
        return face;
    }

    public Troop troop(){
        return troop;
    }

    @Override
    public boolean canStepOn(){
        return false;
    }

    @Override
    public boolean hasTroop(){
        return true;
    }

    public TroopTile flipped(){
        TroopFace newFace = TroopFace.AVERS;
        if (face == TroopFace.AVERS)
        {
            newFace = TroopFace.REVERS;
        }
        return new TroopTile(troop,side,newFace);
    }

}
