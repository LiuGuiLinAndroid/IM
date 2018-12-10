package com.liuguilin.im.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * FileName: DimenUtils
 * Founder: LiuGuiLin
 * Create Date: 2018/12/10 19:19
 * Email: lgl@szokl.com.cn
 * Profile: 适配
 */
public class DimenUtils {

    public static void main(String[] args) {

        //适配屏幕
        AutoSizeForDimens();
    }

    /**
     * 自动适配
     */
    public static void AutoSizeForDimens() {

        //定义变量
        BufferedReader mReader = null;

        //参照物
        File mFile = new File("./app/src/main/res/values/dimens.xml");

        //适配列表
        StringBuilder sw1184 = new StringBuilder();
        StringBuilder sw1280 = new StringBuilder();
        StringBuilder sw1334 = new StringBuilder();
        StringBuilder sw1776 = new StringBuilder();
        StringBuilder sw1788 = new StringBuilder();
        StringBuilder sw1920 = new StringBuilder();
        StringBuilder sw2240 = new StringBuilder();
        StringBuilder sw2560 = new StringBuilder();

        /*
         * 1184 x 720   0.9
         * 1280 x 720   0.9
         * 1334 x 750   1
         * 1776 x 1080  1.4
         * 1788 x 1080  1.4
         * 1920 x 1080  1.5
         * 2240 x 1440  1.9
         * 2560 x 1440  2
         */

        try {
            PrintLog(" start read file ");

            mReader = new BufferedReader(new FileReader(mFile));
            String temp;
            int line = 1;
            while ((temp = mReader.readLine()) != null) {
                if (temp.contains("</dimen>")) {
                    String start = temp.substring(0, temp.indexOf(">") + 1);
                    String end = temp.substring(temp.lastIndexOf("<") - 2);
                    //截取<dimen></dimen>标签内的内容，从>右括号开始，到左括号减2，取得配置的数字
                    Double num = Double.parseDouble(temp.substring(temp.indexOf(">") + 1, temp.indexOf("</dimen>") - 2));
                    sw1184.append(start).append(num * 0.9).append(end).append("\r\n");
                    sw1280.append(start).append(num * 0.9).append(end).append("\r\n");
                    sw1334.append(start).append(num * 1).append(end).append("\r\n");
                    sw1776.append(start).append(num * 1.4).append(end).append("\r\n");
                    sw1788.append(start).append(num * 1.4).append(end).append("\r\n");
                    sw1920.append(start).append(num * 1.5).append(end).append("\r\n");
                    sw2240.append(start).append(num * 1.9).append(end).append("\r\n");
                    sw2560.append(start).append(num * 2).append(end).append("\r\n");
                } else {
                    sw1184.append(temp).append("\r\n");
                    sw1280.append(temp).append("\r\n");
                    sw1334.append(temp).append("\r\n");
                    sw1788.append(temp).append("\r\n");
                    sw1920.append(temp).append("\r\n");
                    sw2240.append(temp).append("\r\n");
                    sw2560.append(temp).append("\r\n");
                }
                line++;
            }
            mReader.close();

            PrintLog(" read file done ");

            String sw1184file = "./app/src/main/res/values-1184x720/dimens.xml";
            String sw1280file = "./app/src/main/res/values-1280x720/dimens.xml";
            String sw1334file = "./app/src/main/res/values-1334x750/dimens.xml";
            String sw1788file = "./app/src/main/res/values-1788x1080/dimens.xml";
            String sw1776file = "./app/src/main/res/values-1776x1080/dimens.xml";
            String sw1920file = "./app/src/main/res/values-1920x1080/dimens.xml";
            String sw2240file = "./app/src/main/res/values-2240x1440/dimens.xml";
            String sw2560file = "./app/src/main/res/values-2560x1440/dimens.xml";

            PrintLog(" start write file ");

            writeFile(sw1184file, sw1184.toString());
            writeFile(sw1280file, sw1280.toString());
            writeFile(sw1334file, sw1334.toString());
            writeFile(sw1788file, sw1788.toString());
            writeFile(sw1776file, sw1788.toString());
            writeFile(sw1920file, sw1920.toString());
            writeFile(sw2240file, sw2240.toString());
            writeFile(sw2560file, sw2560.toString());

            PrintLog(" write file done ");

        } catch (FileNotFoundException e) {
            PrintLog(e.toString());
        } catch (IOException e) {
            PrintLog(e.toString());
        } finally {
            if (mReader != null) {
                try {
                    mReader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

    }

    /**
     * 写入文件
     *
     * @param file 文件
     * @param text 内容
     */
    public static void writeFile(String file, String text) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            out.println(text);
        } catch (IOException e) {
            PrintLog(e.toString());
        }
        out.close();
    }

    /**
     * 输出Log
     *
     * @param str
     */
    public static void PrintLog(String str) {
        System.out.println(str);
    }


}
