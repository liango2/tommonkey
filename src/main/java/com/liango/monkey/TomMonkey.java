package com.liango.monkey;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 一个高端大气上档次的山寨的Web服务器： 猴子归来
 * 1. 如何支持多个浏览器访问（多线程）：
 * 2. 如何提供服务（socket）；
 * 3. 如何返回响应（IO流）；
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
            System.out.println("启动监听" + port + "端口的服务器-----------");
            while (true) {
                Socket accept = serverSocket.accept();
                System.out.println("有客户端请求 accept = " + accept);

                ExecutorService pool = Executors.newFixedThreadPool(100); // 创建一个线程池
                pool.submit(new HandlerRequestThread(accept)); // 将任务提交给线程池去处理
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
