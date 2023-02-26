package com.example.parser;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class ParserController {
    private ArrayList<String> golangOperators = new ArrayList<>(List.of(
            "+", "++", "--", "-", "/", "*", "%", "==", "!=", "<", ">", "<=", ">=", "&&", "||", "!", "&", "|", "^", ",", ";", "(",
            ".", "len", "&", ">>=", "<<=", "^=", "|=", "&=", "%=", "/=", "*=", "-=", "+=", ":=", "=", ">>", "<<", "~",
            "break", "case", "chan", "const", "continue", "default", "defer", "else", "fallthrough", "for", "func",
            "go", "goto", "if", "import", "interface", "map", "package", "range", "return", "select", "struct", "switch",
            "type", "var"));
    private ArrayList<String> operands = new ArrayList<>();
    @FXML
    private TextArea AmountOfOperandEntriesInput;

    @FXML
    private TextArea AmountOfOperatorEntriesInput;

    @FXML
    private MenuItem OpenFileMenu;

    @FXML
    private TextArea OperandDictInput;

    @FXML
    private TextField OperandsAmountInput;

    @FXML
    private TextArea OperatorDictInput;

    @FXML
    private TextField OperatorsAmountInput;

    @FXML
    private TextField ProgDictInput;

    @FXML
    private TextField ProgLengthInput;

    @FXML
    private TextField ProgVolumeInput;
    private HashMap<String, Integer> operandsEntries = new HashMap<>();
    private HashMap<String, Integer> operatorsEntries = new HashMap<>();
    private void readFile(){
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(ParserApplication.stStage);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while (reader.ready()){
                parseString(reader.readLine());
            }
            reader = new BufferedReader(new FileReader(file));
            while(reader.ready()){
                parseOperands(reader.readLine());
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private void parseString(String line) {
        parseOperators(line);
    }
    private void parseOperands(String line){
        String newLine;
        for(String operand : operandsEntries.keySet()){
            while(line.contains(operand)){
                newLine = "";
                newLine += line.substring(0, line.indexOf(operand));
                newLine += line.substring(line.indexOf(operand) + operand.length(), line.length());
                line = newLine;
                operandsEntries.replace(operand, operandsEntries.get(operand) + 1);
            }
        }
    }
    private void addOperand(String line, String operator){
        String operand = "";
        if(operator.equals(":=")){
            int i = 0;
            while(!line.split(" ")[i].equals(":=")){
                i++;
            }
            operand = line.split(" ")[i - 1];
        } else {
            int i = 0;
            while(!line.split(" ")[i].equals("var")){
                i++;
            }
            operand = line.split(" ")[i + 1];
        }
        operandsEntries.put(operand, 0);
    }
    private void parseOperators(String line){
        String newLine;
        for(int i = 0; i < golangOperators.size(); i++){
            String operator = golangOperators.get(i);
            while(line.contains(operator)){
                if(operator.equals(":=") || operator.equals("var")){
                    addOperand(line, operator);
                }
                if(operator.equals("func")){
                    int index = 0;
                    while(!line.split(" ")[index].equals("func")){
                        index++;
                    }
                    golangOperators.add(line.split(" ")[index + 1].substring(0, line.split(" ")[index + 1].indexOf("(")));
                }
                newLine = "";
                newLine += line.substring(0, line.indexOf(operator));
                newLine += line.substring(line.indexOf(operator) + operator.length(), line.length());
                line = newLine;

                if (operatorsEntries.get(operator) != null) {
                    operatorsEntries.replace(operator, operatorsEntries.get(operator) + 1);
                } else {
                    operatorsEntries.put(operator, 1);
                }

            }
        }
    }
    private void resetFields(){
        operandsEntries.clear();
        operatorsEntries.clear();
    }
    private void getAnswer(){
        StringBuilder operatorsEntriesBuilder = new StringBuilder();
        StringBuilder operatorsBuilder = new StringBuilder();
        StringBuilder operandsEntriesBuilder = new StringBuilder();
        StringBuilder operandsBuilder = new StringBuilder();
        int operatorsAmount = 0;
        int operandsAmount = 0;
        for(String key : operatorsEntries.keySet()){
            operatorsEntriesBuilder.append(key + "\t" + operatorsEntries.get(key) + "\n");
            operatorsAmount += operatorsEntries.get(key);
            operatorsBuilder.append(key + "\n");
        }
        for(String key : operandsEntries.keySet()){
            operandsEntriesBuilder.append(key + "\t" + operandsEntries.get(key) + "\n");
            operandsAmount += operandsEntries.get(key);
            operandsBuilder.append(key + "\n");
        }
        AmountOfOperatorEntriesInput.setText(operatorsEntriesBuilder.toString());
        OperatorDictInput.setText(operatorsBuilder.toString());
        AmountOfOperandEntriesInput.setText(operandsEntriesBuilder.toString());
        OperandDictInput.setText(operandsBuilder.toString());
        OperatorsAmountInput.setText(String.valueOf(operatorsAmount));
        ProgDictInput.setText(String.valueOf(operandsEntries.size() + operatorsEntries.size()));
        ProgLengthInput.setText(String.valueOf(operandsAmount + operatorsAmount));
        ProgVolumeInput.setText(String.valueOf(Integer.parseInt(ProgLengthInput.getText()) * Math.log(Integer.parseInt(ProgDictInput.getText())) / Math.log(2)));
    }
    @FXML
    private void onClick(ActionEvent e) {
        Comparator<String> comparator = (s1, s2) -> s2.length() - s1.length();
        golangOperators.sort(comparator);
        resetFields();
        readFile();
        getAnswer();
    }
}