package Game.Piece.Pieces;

import Game.Board.Board;
import Game.Board.Square;
import Game.Colour;
import Game.Coordinate;
import Game.Move.Move;
import Game.Piece.Piece;
import Game.Piece.PieceType;
import LibaryFunctions.Utility;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {
    /**
     * Constructor for a queen piece
     *
     * @param coordinate A coordinate object identifying the tile coordinate of the piece
     * @param colour     The colour of a piece
     * @param type       The piece type which is inheriting from the piece class (King, Queen Bishop etc..)
     */
    public Queen(Coordinate coordinate, Colour colour, PieceType type) {
        super(coordinate, colour, type);
    }

    /**
     * Takes a board object and calculates the available queen moves so that
     * illegal moves cannot be made
     * Takes into account that check may be present on the board etc..
     *
     * @param board An instance of the current board (which contains an array of squares)
     * @return a list of all available/legal moves (where move is a move object)
     */
    @Override
    public List<Move> CalculateValidMoves(Board board) {
        Square[][] BoardArray = board.getBoardArray();
        List<Square> PossibleDestinations = CalculateDiagonals(BoardArray);
        PossibleDestinations.addAll(CalculateStraights(BoardArray));
        
        /*Remove Square that moving piece is occupying and squares which
        cannot be captured (because a piece of equal colour occupies it)*/
        PossibleDestinations = RemoveRemainingInvalidDestinations(PossibleDestinations);

        return removeIllegalMoves(board, DestinationsToMoves(PossibleDestinations, BoardArray));
    }

    /**
     * Calculates the valid diagonal moves of the queen
     *
     * @param BoardArray a 2-Dimensional array of squares so that the diagonal line that the piece occupies can be found
     *                   in the board array
     * @return a list of possible destination squares
     */
    private List<Square> CalculateDiagonals(final Square[][] BoardArray) {
        //Find Diagonal moves
        List<Square> PositiveDiagonal = new ArrayList<>();
        List<Square> NegativeDiagonal = new ArrayList<>();

        for (Square[] Row : BoardArray) {
            for (Square square : Row) {
                Coordinate Destination = square.ReturnCoordinate();
                int XDisplacement = getPieceCoordinate().getFile() - Destination.getFile();
                int YDisplacement = getPieceCoordinate().getRank() - Destination.getRank();

                if ((XDisplacement != 0) && Math.abs(XDisplacement) == Math.abs(YDisplacement)) {
                    if (YDisplacement / XDisplacement == 1) {
                        PositiveDiagonal.add(square);
                    } else if (YDisplacement / XDisplacement == -1) {
                        NegativeDiagonal.add(square);
                    }
                } else if ((XDisplacement == 0) && (YDisplacement == 0)) {
                    PositiveDiagonal.add(square);
                    NegativeDiagonal.add(square);
                }
            }
        }

        PositiveDiagonal = CheckCollisions(PositiveDiagonal);
        NegativeDiagonal = CheckCollisions(NegativeDiagonal);

        List<Square> PossibleDestinations = PositiveDiagonal;
        PossibleDestinations.addAll(NegativeDiagonal);
        return PossibleDestinations;
    }

    /**
     * Calculates the valid horizontal and vertical (straight) line moves of the queen
     *
     * @param BoardArray a 2-Dimensional array of squares so that the straight line that the piece occupies can be found
     *                   in the board array
     * @return a list of possible destination squares
     */
    private List<Square> CalculateStraights(final Square[][] BoardArray) {
        List<Square> PossibleDestinations = new ArrayList<>();

        List<Square> Row = Utility.ArrayToRow(BoardArray, getPieceCoordinate().GetSquareAt(BoardArray));
        List<Square> Column = Utility.ArrayToColumn(BoardArray, getPieceCoordinate().GetSquareAt(BoardArray));

        //Check For any path obstructions regardless of piece colour
        Column = CheckCollisions(Column);
        Row = CheckCollisions(Row);
        PossibleDestinations.addAll(Column);
        PossibleDestinations.addAll(Row);

        return PossibleDestinations;
    }

    /**
     * Converts the type of piece to its Notation equivalent
     *
     * @return a String with the type notation (Queen = Q)
     */
    @Override
    public String PieceTypeToNotation() {
        return "Q";
    }

    /**
     * @return Returns the Unicode character of the piece type.
     */
    @Override
    public String ReturnPieceIcon() {
        return "♛";
    }
}
