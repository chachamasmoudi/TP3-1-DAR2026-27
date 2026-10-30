import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadServer {

    private static final int PORT = 1234;

    public static void main(String[] args) {
        int clientCount = 0;

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Serveur démarré, en attente de clients sur le port " + PORT + "...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                clientCount++;

                // Variable "effectively final" nécessaire pour être utilisée dans la lambda
                final int numero = clientCount;

                System.out.println("Client n°" + numero + " connecté : "
                        + clientSocket.getRemoteSocketAddress());

                // Un thread par client, défini directement ici (pas de classe dédiée)
                Thread t = new Thread(() -> traiterClient(clientSocket, numero));
                t.start();
            }
        } catch (IOException e) {
            System.err.println("Erreur serveur : " + e.getMessage());
        }
    }

    /** Dialogue avec UN client (exécuté dans le thread de ce client). */
    private static void traiterClient(Socket socket, int numero) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // Envoi du numéro d'ordre au client
            out.println("Bienvenue ! Vous êtes le client n°" + numero);

            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("[Client n°" + numero + "] " + line);

                if (line.equalsIgnoreCase("bye") || line.equalsIgnoreCase("exit")) {
                    out.println("Au revoir client n°" + numero);
                    break;
                }
                out.println("Serveur -> (client n°" + numero + ") : " + line);
            }
        } catch (IOException e) {
            System.err.println("Erreur avec le client n°" + numero + " : " + e.getMessage());
        } finally {
            try { socket.close(); } catch (IOException ignored) { }
            System.out.println("Client n°" + numero + " déconnecté.");
        }
    }
}