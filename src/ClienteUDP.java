import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClienteUDP {
	public static void main(String args[]) {
		// Declara o socket UDP
		DatagramSocket clientSocket = null;
		try {
			// Instancia o socker UDP (define que ele deve usar a porta 10.000)
			clientSocket = new DatagramSocket();
			// Cria array de bytes que será enviado para o servidor
			Scanner teclado = new Scanner(System.in);
			// Ip e porta do servidor
			System.out.println("Informe o ip do servidor");
			InetAddress IpServidor = InetAddress.getByName(teclado.nextLine());

			System.out.println("Informe a porta");
			int port = teclado.nextInt();

			String mensagem;
			System.out.println("Informe a mensagem");
			mensagem = teclado.nextLine();
			while (teclado.hasNextLine()) {
				mensagem = teclado.nextLine();
				if (!mensagem.equals("/CLOSE")) {
					byte[] sendArray = mensagem.getBytes();
					byte[] receiveData = new byte[1024];
					// Cria um pacote UDP (array, tamanho do array, ip, porta)
					DatagramPacket sendPacket = new DatagramPacket(sendArray, sendArray.length, IpServidor, port);
					// Envia o pacote UDP
					clientSocket.send(sendPacket);
					System.out.println("Pacote enviado!");
					DatagramPacket receiveServer = new DatagramPacket(receiveData, receiveData.length);
					clientSocket.receive(receiveServer);
					String response = new String(receiveServer.getData());
					System.out.println("Requisição: " + mensagem);
					System.out.println("Resposta: " + response);
				} else {
					clientSocket.close();
					System.exit(0);
				}
			}

		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// Fecha o socket
			if (clientSocket != null)
				clientSocket.close();
		}
	}
}
