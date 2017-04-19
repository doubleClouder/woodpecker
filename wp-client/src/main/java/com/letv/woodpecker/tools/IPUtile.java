package com.letv.woodpecker.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class IPUtile {
    public IPUtile() {
    }

    public static String getIP() {
        String infor;
        try {
            InetAddress addr = InetAddress.getLocalHost();
            infor = addr.getHostAddress().toString();
            if(infor != null && !infor.equals("127.0.0.1")) {
                return infor;
            }
        } catch (UnknownHostException var15) {
            var15.printStackTrace();
        }

        infor = null;
        BufferedReader bufferedReader = null;
        Process process = null;

        String ip;
        try {
            process = Runtime.getRuntime().exec("ifconfig eth0");
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            ip = null;
            boolean ips = true;

            while((ip = bufferedReader.readLine()) != null) {
                int ips1 = ip.toLowerCase().indexOf("inet addr");
                if(ips1 >= 0) {
                    infor = ip.substring(ips1 + "inet addr".length() + 1);
                    break;
                }
            }
        } catch (IOException var16) {
            var16.printStackTrace();
        } finally {
            try {
                if(bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException var14) {
                var14.printStackTrace();
            }

            bufferedReader = null;
            process = null;
        }

        ip = "";
        if(infor != null && !infor.equals("")) {
            String[] ips2 = infor.split(" ");
            ip = ips2[0];
        }

        return ip != null && !ip.equals("")?ip.trim():getBondIp();
    }


    public static String getBondIp() {
        String infor = null;
        BufferedReader bufferedReader = null;
        Process process = null;

        String ip;
        try {
            process = Runtime.getRuntime().exec("ifconfig bond1");
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            ip = null;
            boolean ips = true;

            while((ip = bufferedReader.readLine()) != null) {
                int ips1 = ip.toLowerCase().indexOf("inet addr");
                if(ips1 >= 0) {
                    infor = ip.substring(ips1 + "inet addr".length() + 1);
                    break;
                }
            }
        } catch (IOException var16) {
            var16.printStackTrace();
        } finally {
            try {
                if(bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException var14) {
                var14.printStackTrace();
            }

            bufferedReader = null;
            process = null;
        }

        ip = "";
        if(infor != null && !infor.equals("")) {
            String[] ips2 = infor.split(" ");
            ip = ips2[0];
        }
        return ip != null && !ip.equals("")?ip.trim():"127.0.0.1";
    }


}

