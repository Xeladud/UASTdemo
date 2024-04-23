package com.example.UASTdemo.service;
import java.io.File;
import java.io.IOException;

import java.util.ArrayList;

import java.nio.charset.Charset;
import java.nio.file.Files;

import com.example.UASTdemo.antlr.Java8Lexer;
import com.example.UASTdemo.antlr.Java8Parser;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

public class ASTGenerator {

    static ArrayList<String> LineNum = new ArrayList<String>();
    static ArrayList<String> Type = new ArrayList<String>();
    static ArrayList<String> Content = new ArrayList<String>();

    public void main() throws IOException{
        CharStream codePointCharStream = CharStreams.fromFileName("C:\\VPsaver\\VPsaver\\Form1.cs");
        Java8Lexer lexer = new Java8Lexer(codePointCharStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Java8Parser parser = new Java8Parser(tokens);
        ParserRuleContext ctx = parser.compilationUnit();

        generateAST(ctx, false, 0);

        System.out.println("digraph G {");
        printDOT();
        System.out.println("}");
    }

    public void test() throws IOException {
        CharStream codePointCharStream = CharStreams.fromFileName("C:\\Users\\Алексей\\IdeaProjects\\OPAintegration\\src\\main\\java\\fp\\integration\\opa\\YAMLreader.java");
        Java8Lexer lexer = new Java8Lexer(codePointCharStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Java8Parser parser = new Java8Parser(tokens);
        parser.setBuildParseTree(true);
        System.out.println(parser.classBody());
    }

    private static void generateAST(RuleContext ctx, boolean verbose, int indentation) {
        boolean toBeIgnored = !verbose && ctx.getChildCount() == 1 && ctx.getChild(0) instanceof ParserRuleContext;

        if (!toBeIgnored) {
            String ruleName = Java8Parser.ruleNames[ctx.getRuleIndex()];
            LineNum.add(Integer.toString(indentation));
            Type.add(ruleName);
            Content.add(ctx.getText());
        }
        for (int i = 0; i < ctx.getChildCount(); i++) {
            ParseTree element = ctx.getChild(i);
            if (element instanceof RuleContext) {
                generateAST((RuleContext) element, verbose, indentation + (toBeIgnored ? 0 : 1));
            }
        }
    }

    private static void printDOT(){
        printLabel();
        int pos = 0;
        for(int i=1; i<LineNum.size();i++){
            pos=getPos(Integer.parseInt(LineNum.get(i))-1, i);
            System.out.println((Integer.parseInt(LineNum.get(i))-1)+Integer.toString(pos)+"->"+LineNum.get(i)+i);
        }
    }

    private static void printLabel(){
        for(int i =0; i<LineNum.size(); i++){
            try {
                System.out.println(LineNum.get(i) + i + "[label=\"" + Type.get(i) + "\\n " + Content.get(i).replaceAll("\"", "&quote;") + " \"]");
            }
            catch (Exception ex) {
                System.out.println(Content.get(i));
            }
        }
    }

    private static int getPos(int n, int limit){
        int pos = 0;
        for(int i=0; i<limit;i++){
            if(Integer.parseInt(LineNum.get(i))==n){
                pos = i;
            }
        }
        return pos;
    }
}
