import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        String host = (args.length > 0) ? args[0] : "localhost";
        int port = (args.length > 1) ? Integer.parseInt(args[1]) : 1234;

        try (Socket socket = new Socket()) {
            // InetAddress + InetSocketAddress pour viser un serveur distant
            InetAddress serverAddress = InetAddress.getByName(host);
            socket.connect(new InetSocketAddress(serverAddress, port), 5000);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner clavier = new Scanner(System.in);

            // Le serveur envoie d'abord le numéro d'ordre du client
            System.out.println(in.readLine());

            while (true) {
                System.out.print("Message (bye pour quitter) > ");
                if (!clavier.hasNextLine()) break;
                String msg = clavier.nextLine();
                out.println(msg);

                String reponse = in.readLine();
                if (reponse == null) break;
                System.out.println(reponse);

                if (msg.equalsIgnoreCase("bye") || msg.equalsIgnoreCase("exit")) break;
            }
        } catch (IOException e) {
            System.err.println("Impossible de communiquer avec le serveur : " + e.getMessage());
        }
    }
}