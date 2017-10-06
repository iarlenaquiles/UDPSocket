import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.TimeUnit;

public class ServerUDP implements Runnable {
	public Integer reqnum;
	long tempoInicio;
	DatagramSocket serverSocket;
	DatagramPacket request;
	DatagramPacket response;

	public ServerUDP() {
		this.reqnum = 0;
		this.tempoInicio = System.currentTimeMillis();
		this.serverSocket = null;
		this.request = null;
		this.response = null;

	}

	public static void main(String args[]) {
		ServerUDP s1 = new ServerUDP();
		Thread t = new Thread(s1);
		t.start();
		
		ServerUDP s2 = new ServerUDP();
		Thread t2 = new Thread(s2);
		t2.start();
		
		ServerUDP s3 = new ServerUDP();
		Thread t3 = new Thread(s3);
		t3.start();

	}

	@Override
	public void run() {

		try {
			// Instancia socker UDP (define que ele deve usar a porta 6789)
			this.serverSocket = new DatagramSocket();
			System.out.println("Servidor em execução! Porta: " + this.serverSocket.getLocalPort());
			// Cria array de bytes que será enviado para o servidor
			byte[] receiveData = new byte[1024];
			int id = 0;

			// Cria loop para receber mais de uma msg
			while (true) {
				id++;

				System.out.println("Esperando Msg " + id + " ...");
				// Cria pacote para receber a mensagem UDP
				this.request = new DatagramPacket(receiveData, receiveData.length);
				// Espera a chegada de uma msg (bloqueante)
				this.serverSocket.receive(this.request);
				// Armazena a mensagem que chegou no formato String
				String sentence = new String(this.request.getData(), 0, this.request.getLength());
				// Mostra informações do cliente
				System.out.println("Cliente: " + this.request.getAddress().getHostAddress() + " - Porta: "
						+ this.request.getPort());
				byte[] responseData;
				
				if (sentence.equals("/REQNUM")) {
					String numreq = "Número de requisições: " + this.reqnum;
					responseData = numreq.getBytes();
					this.response = new DatagramPacket(responseData, responseData.length, this.request.getAddress(),
							this.request.getPort());

					this.serverSocket.send(this.response);
				} else if (sentence.equals("/UPTIME")) {
					long total = System.currentTimeMillis() - this.tempoInicio;
					String uptime = "Tempo de execução: "
							+ String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(total),
									TimeUnit.MILLISECONDS.toMinutes(total)
											- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(total)),
									TimeUnit.MILLISECONDS.toSeconds(total)
											- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(total)));

					responseData = uptime.getBytes();
					this.response = new DatagramPacket(responseData, responseData.length, this.request.getAddress(),
							this.request.getPort());

					this.serverSocket.send(this.response);
				} else {
					this.reqnum++;
					responseData = sentence.getBytes();
					this.response = new DatagramPacket(responseData, responseData.length, this.request.getAddress(),
							this.request.getPort());
					this.serverSocket.send(this.response);
				}
				System.out.println("Msg: " + sentence);
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// Fecha o socket
			if (this.serverSocket != null)
				this.serverSocket.close();
		}
	}
}
