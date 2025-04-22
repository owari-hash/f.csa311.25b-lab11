package game;

import java.io.IOException;
import java.util.Map;
import fi.iki.elonen.NanoHTTPD;

public class App extends NanoHTTPD {

    public static void main(String[] args) {
        try {
            new App();
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
        }
    }

    private Game game;

    /**
     * Start the server at :8080 port.
     * 
     * @throws IOException
     */
    public App() throws IOException {
        super(8080);

        this.game = new Game();

        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nRunning!\n");
    }

    private Response handleNewGame(IHTTPSession session) {
        this.game = new Game();
        return newFixedLengthResponse(GameState.forGame(this.game).toString());
    }

    private Response handlePlay(IHTTPSession session) {
        Map<String, String> params = session.getParms();
        int x = Integer.parseInt(params.get("x"));
        int y = Integer.parseInt(params.get("y"));
        this.game = this.game.play(x, y);
        return newFixedLengthResponse(GameState.forGame(this.game).toString());
    }

    // Fixed method to handle undo requests
    private Response handleUndo(IHTTPSession session) {
        if (this.game.canUndo()) {
            this.game = this.game.undo();
        }
        return newFixedLengthResponse(GameState.forGame(this.game).toString());
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();
        switch (uri) {
            case "/newgame":
                return handleNewGame(session);
            case "/play":
                return handlePlay(session);
            case "/undo":
                return handleUndo(session);
            default:
                return newFixedLengthResponse("Unknown request");
        }
    }

    public static class Test {
        public String getText() {
            return "Hello World!";
        }
    }
}