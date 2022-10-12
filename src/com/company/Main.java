package com.company;

import java.io.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


/*По ссылке https://drive.google.com/file/d/1XZrLLuJlHK3n35NwQAnY9t1nSwZ3-piq/view?usp=sharing находится файл file.dat .
        Ваша задача скачать этот файл (кстати, вы можете сделать это из Java программы, но если сложно можно просто скачать).
        Ваша программа  должна записать в отдельный файл первые 601 байт, затем в отдельный файл записать следующие 57053 байта
        и оставшиеся 22494 байта записать в следующий файл. Если все сделано правильно, у вас должно получиться 3 файла.

        В одном из этих файлов лежит gif картина, в другом jpg картинка, еще в одном скомпилированный .class файл java программы.
        Вам нужно написать программу, которая определит, в каком файле что лежит.
        Сделать это можно используя так называемые сигнатуры файлов: gif должен начинаться с шестнадцатеричной
        последовательности байт 47 49 46 38 39 61	jpeg с последовательности ff d8   java .class файл с шестнадцатеричной
        последовательности ca fe ba be
        Сохраните файл, опознанный как .class файл под именем Main.class и запустите его из командной строки. Прочитайте кодовое слово.*/

public class Main {
    public static void main(String[] args) {


        byte[][] data = read();

        for (int f = 0; f < 3 ; f++) {

            FileOutputStream CurrentFile = create(f);
            write(CurrentFile,data[f]);

            //проверим какой это файл
            if ( FileSignature.GIF.check(data[f]) == true) {
                System.out.println( "GIF file in file №" + f);
            }
            if ( FileSignature.JPG.check(data[f]) == true) {
                System.out.println( "JPG file in file №" + f);
            }
            if ( FileSignature.CLASS.check(data[f]) == true) {
                System.out.println( "CLASS file in file №" + f);
                copy(f + ".dat");
            }

        }

       System.out.println(new String(new byte[]{69, 120, 99, 101, 108, 108, 101, 110, 116, 33}));

    }

    
    private static void copy(String currentFileName) {
        try (
                FileInputStream is = new FileInputStream(currentFileName);
                OutputStream os = new FileOutputStream("Main.class");
        ){
            int count=0;
            int d;
            while ((d = is.read()) != -1) {
                count++;
                if(count%1000000==0);
                os.write(d);
            }
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static void write(FileOutputStream CFile, byte[] y) {
        try {
            CFile.write(y);
            CFile.flush();
            CFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static FileOutputStream create(int num) {
        try {
            FileOutputStream os = new FileOutputStream(num + ".dat");
            return os;
       } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }




    public static byte[][] read()  {
        byte[][] data = {new byte[601], new byte[57053], new byte[22494]};
        try(InputStream is = new FileInputStream("file.dat")){

            for (int i = 0; i <data.length ; i++) {
                if(is.read(data[i])!=data[i].length){
                    throw new IOException("file.dat error: read part #"+i);
                };
            }
            if(is.read()!=-1){
                throw new IOException("file.dat error: invalid format");
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return data;
    }



}