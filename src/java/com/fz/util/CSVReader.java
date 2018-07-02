/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fz.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author Eri Fizal
 */
public class CSVReader {
    
   private static final char DEFAULT_SEPARATOR = ',';
   private static final char DEFAULT_QUOTE = '"';

    public static ArrayList<List<String>> read(String csvFile) throws Exception {

        ArrayList<List<String>> lines = new ArrayList<List<String>>();

        BufferedReader br = new BufferedReader(new FileReader(csvFile));
        int lineNum = 0;
        try {
            Reader in = new FileReader(csvFile);
            Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
            for (CSVRecord record : records) {
                lineNum++;
                List<String> line = new ArrayList<String>();
                for (int i=0; i<record.size(); i++){
                    line.add(record.get(i));
                }
                lines.add(line);
            }
        } catch (Exception e) {
            throw new Exception("At Line " + lineNum + " file " + csvFile, e);
        }
        return lines;
    }

//    public static ArrayList<List<String>> readOLD2(String csvFile) throws Exception {
//
//        ArrayList<List<String>> lines = new ArrayList<List<String>>();
//
//        BufferedReader br = new BufferedReader(new FileReader(csvFile));
//        int lineNum = 0;
//        try {
//            String lineStr = br.readLine();
//            while (lineStr != null) {
//                lineNum++;
//                List<String> line = parseLine(lineStr);
//                lines.add(line);
//                lineStr = br.readLine();
//            }
//        } catch (Exception e) {
//            throw new Exception("At Line " + lineNum + " file " + csvFile, e);
//        }
//        return lines;
//    }
//
//    public static List<String> parseLine(String cvsLine) {
//        return parseLine(cvsLine, DEFAULT_SEPARATOR, DEFAULT_QUOTE);
//    }
//
//    public static List<String> parseLine(String cvsLine, char separators) {
//        return parseLine(cvsLine, separators, DEFAULT_QUOTE);
//    }
//
//    public static List<String> parseLine(String cvsLine, char separators
//            , char customQuote) {
//
//        List<String> result = new ArrayList<>();
//
//        //if empty, return!
//        if (cvsLine == null && cvsLine.isEmpty()) {
//            return result;
//        }
//
//        if (customQuote == ' ') {
//            customQuote = DEFAULT_QUOTE;
//        }
//
//        if (separators == ' ') {
//            separators = DEFAULT_SEPARATOR;
//        }
//
//        StringBuffer curVal = new StringBuffer();
//        boolean inQuotes = false;
//        boolean startCollectChar = false;
//        boolean doubleQuotesInColumn = false;
//
//        char[] chars = cvsLine.toCharArray();
//
//        for (char ch : chars) {
//
//            if (inQuotes) {
//                startCollectChar = true;
//                if (ch == customQuote) {
//                    inQuotes = false;
//                    doubleQuotesInColumn = false;
//                } else {
//
//                    //Fixed : allow "" in custom quote enclosed
//                    if (ch == '\"') {
//                        if (!doubleQuotesInColumn) {
//                            curVal.append(ch);
//                            doubleQuotesInColumn = true;
//                        }
//                    } else {
//                        curVal.append(ch);
//                    }
//
//                }
//            } else {
//                if (ch == customQuote) {
//
//                    inQuotes = true;
//
//                    //Fixed : allow "" in empty quote enclosed
//                    if (chars[0] != '"' && customQuote == '\"') {
//                        curVal.append('"');
//                    }
//
//                    //double quotes in column will hit this!
//                    if (startCollectChar) {
//                        curVal.append('"');
//                    }
//
//                } else if (ch == separators) {
//
//                    result.add(curVal.toString());
//
//                    curVal = new StringBuffer();
//                    startCollectChar = false;
//
//                } else if (ch == '\r') {
//                    //ignore LF characters
//                    continue;
//                } else if (ch == '\n') {
//                    //the end, break!
//                    break;
//                } else {
//                    curVal.append(ch);
//                }
//            }
//
//        }
//
//        result.add(curVal.toString());
//
//        return result;
//    }

}