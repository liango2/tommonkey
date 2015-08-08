package com.liango.monkey;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * һ���߶˴����ϵ��ε�ɽկ��Web�������� ���ӹ���
 * 1. ���֧�ֶ����������ʣ����̣߳���
 * 2. ����ṩ����socket����
 * 3. ��η�����Ӧ��IO������
 *
 * @author liango
 * @version 1.0
 * @since 2015-08-08 13:26
 */
public class TomMonkey {

    private static int PORT =   80;

    public static void main(String[] args) {
        new TomMonkey().start(args.length > 0 ?  Integer.valueOf(args[0]) : PORT );
    }

    public void start(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("��������" + port + "�˿ڵķ�����-----------");
            while (true) {
                Socket accept = serverSocket.accept();
                System.out.println("�пͻ������� accept = " + accept);

                ExecutorService pool = Executors.newFixedThreadPool(100); // ����һ���̳߳�
                pool.submit(new HandlerRequestThread(accept)); // �������ύ���̳߳�ȥ����
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
