package com.liango.monkey;

import javax.sound.midi.Soundbank;
import java.io.*;
import java.net.Socket;

/**
 * 专门处理不同的客户端请求的多线程类。
 *
 * @author liango
 * @version 1.0
 * @since 2015-08-08 13:51
 */
public class HandlerRequestThread implements Runnable {

    private InputStream in;
    private PrintStream out;

    public static final String WEB_ROOT = "D:" + File.separator + "temp" + File.separator;

    public HandlerRequestThread(Socket socket) {
        try {
            in = socket.getInputStream();
            out = new PrintStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析请求头，获得客户端请求的资源名称；
     *
     * @return
     */
    public String parseRequestHead(InputStream in) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        String s = bufferedReader.readLine();
        System.out.println("s = " + s); // s = GET /ttt.html HTTP/1.1

        String s1 = s.split(" ")[1];
        System.out.println("s1 = " + s1);
        return s1.endsWith("/") ? "index.html" : s1;
    }

    public void getFile(String fileName) throws IOException {
        File file = new File(WEB_ROOT + fileName);
        System.out.println("fil = " + file);

        if (!file.exists()) {
            System.out.println("资源" + fileName + "不存在");
            sendError("404", "NOT FOUND");
        } else {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            byte[] buff = new byte[(int) file.length()];
            int read = bis.read(buff);

            out.println("HTTP/1.1 200 OK");
            out.println();
            out.write(buff);
            out.flush();
            out.close();
        }
    }

    public void sendError(String errorCode, String errorMsg) throws IOException {
        out.println("HTTP/1.1 404 NOT FOUND");
        out.println();
        out.print("<html><body>" + errorCode + " - " + errorMsg + "</body></html>");
        out.flush();
        out.close();
    }

    public void run() {
        System.out.println("---------处理用户请求---------");
        try {
            String s = parseRequestHead(this.in);
            getFile(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
