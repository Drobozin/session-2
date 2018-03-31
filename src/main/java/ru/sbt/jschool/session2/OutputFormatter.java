/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.sbt.jschool.session2;

import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 */
public class OutputFormatter {
    private PrintStream out;
    private int[] columnsLen;
    private String topBorder;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    private String patternDecimal = "###,###.##";
    private DecimalFormat decimalFormat;
    private String patternNumber = "###,###";
    private DecimalFormat decimalNumber;
    public OutputFormatter(PrintStream out)

    {
        this.out = out;
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.FRANCE);
        decimalFormat = (DecimalFormat)nf;
        decimalFormat.applyPattern("###,##0.00");

        NumberFormat nf2 = NumberFormat.getNumberInstance(Locale.FRANCE);
        decimalNumber = (DecimalFormat)nf2;
        decimalNumber.applyPattern("###,###");


    }


    StringBuilder res = new StringBuilder();

    public void output(String[] names, Object[][] data) {
        initColumnsLen(names, data);
        topBorder = getTopBorder();
        int i=0;
        drawTable(names, data, i);
        out.print(topBorder);
        res.append(topBorder);
        //System.out.println(res.toString());
    }

    public void drawTable(String[] names, Object[][] data, int i){
        res.append(topBorder);
        out.print(topBorder);
        String str = getСontent(names, data, i);
        out.print(str);
        res.append(str);

        if(i >= data.length && data.length != 0)
            return;
        else if(i==0 && data.length == 0)
            return;
        else
            drawTable(names, data, ++i);

    }

    public String getСontent(String[] names, Object[][] data, int i){
        StringBuilder resBuild = new StringBuilder();
        if(i==0){
            for(int j=0; j<names.length; j++){
                StringBuilder curBuild = new StringBuilder();
                curBuild.append("|");
                if(names[j].length() == columnsLen[j]){
                    curBuild.append(names[j]);
                }else{
                    int leftSpaces = (int)(columnsLen[j] - names[j].length()) / 2;
                    while(leftSpaces-- > 0){
                        curBuild.append(" ");
                    }
                    curBuild.append(names[j]);
                    while(curBuild.length() <= columnsLen[j]){
                        curBuild.append(" ");
                    }
                }
                resBuild.append(curBuild);
            }
            resBuild.append("|\n");
        }else{

            for(int j=0; j<data[i-1].length; j++){
                StringBuilder curBuild = new StringBuilder();
                resBuild.append("|");
                if(data[i-1][j] == null){
                    while(curBuild.length() < columnsLen[j]-1){
                        curBuild.append(" ");
                    }
                    curBuild.append("-");
                }else {
                    if (data[i - 1][j] instanceof String) {
                        curBuild.append(data[i - 1][j].toString());
                        while (curBuild.length() < columnsLen[j]) {
                            curBuild.append(" ");
                        }
                    } else {
                        String str;
                        if (data[i - 1][j] instanceof Date) {
                            str = sdf.format(data[i - 1][j]);
                        } else if(data[i - 1][j] instanceof Double || data[i - 1][j] instanceof Float) {


                            //NumberFormat nf = NumberFormat.getNumberInstance(Locale.FRANCE);
                            //DecimalFormat df = (DecimalFormat)nf;
                            //df.applyPattern("###,##0.00");
                            str = decimalFormat.format(Double.valueOf(data[i - 1][j].toString()));




                        }else{
                            //str = data[i - 1][j].toString();
                            str = decimalNumber.format(Integer.valueOf(data[i-1][j].toString()));
                        }
                        int count = 0;
                        while (columnsLen[j] - str.length() > count++) {
                            curBuild.append(" ");
                        }
                        curBuild.append(str);
                    }
                }
                resBuild.append(curBuild);
            }
            resBuild.append("|\n");
        }

        return resBuild.toString();
    }
/*
    public void initColumnsLen(String[] names, Object[][] data){
        columnsLen = new int[names.length];
        for(int i=0; i<data[0].length; i++){
            int maxLen = 0;
            for (int j=0; j<data.length; j++){
                int curLen = 0;
                if(data[j][i]!= null) {
                    if (data[j][i] instanceof Date) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                        curLen = sdf.format(data[j][i]).length();
                    } else {
                        curLen = data[j][i].toString().length();
                    }

                    if (curLen > maxLen) {
                        maxLen = curLen;
                    }
                }
            }
            columnsLen[i] = maxLen;
        }
    }
*/
    public void initColumnsLen(String[] names, Object[][] data){
        columnsLen = new int[names.length];
        int maxLen = 0;
        for(int i=0; data.length!=0 && i<data[0].length; i++){
            maxLen = 0;
            for (int j=0; j<data.length; j++){
                int curLen = 0;
                if(data[j][i]!= null) {

                    if (data[j][i] instanceof String) {
                        curLen = data[j][i].toString().length();
                    } else {
                        if (data[j][i] instanceof Date) {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                            curLen = sdf.format(data[j][i]).length();
                        } else if(data[j][i] instanceof Double || data[j][i] instanceof Float){
                            //System.out.println(decimalFormat.format(Double.valueOf(data[j][i].toString())));
                            curLen = decimalFormat.format(Double.valueOf(data[j][i].toString())).length();
                        }else{
                            curLen = decimalNumber.format(Integer.valueOf(data[j][i].toString())).length();
                        }
                    }
                    if (curLen > maxLen) {
                        maxLen = curLen;
                    }
                }
            }
            columnsLen[i] = maxLen;

        }
        for (int j=0; j<names.length; j++){
            int curLen = 0;
            if(names[j]!= null) {
                curLen = names[j].length();
                if (curLen > columnsLen[j]) {
                    columnsLen[j] = curLen;
                }
            }
        }

    }
    public String getTopBorder(){
        StringBuilder resBuild = new StringBuilder();

        for(int i=0; i<columnsLen.length; i++){
            resBuild.append("+");
            StringBuilder curBuild = new StringBuilder();
            while(curBuild.length() != columnsLen[i]){
                curBuild.append("-");
            }
            resBuild.append(curBuild.toString());
        }
        resBuild.append("+\n");
        return  resBuild.toString();
    }
}

