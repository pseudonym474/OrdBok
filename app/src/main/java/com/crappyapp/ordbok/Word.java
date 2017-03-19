package com.crappyapp.ordbok;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.HashSet;
import java.util.Set;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TIKE on 27.12.2016.
 */
public class Word {

    public Word(String akeyword, String akeytype){
        this.constructCore(akeyword, akeytype);
    }


    public void constructCore(String akeyword, String akeytype){
        this.keyword = akeyword;
        this.keytype = akeytype;
        this.wordGroup="other";
        if (akeytype.startsWith("m") || akeytype.startsWith("f") || akeytype.startsWith("n"))
            this.wordGroup = "noun";
        else if (akeytype.startsWith("adv"))
            this.wordGroup = "adverb";
        else if (akeytype.startsWith("a"))
            this.wordGroup = "adjective";
        else if (akeytype.startsWith("v"))
            this.wordGroup = "verb";
        else if (akeytype.startsWith("prep"))
            this.wordGroup = "preposition";

        if (wordGroup == "verb"){
            this.ledetekst = new ArrayList<>();
            this.infinitiv = new ArrayList<>();
            this.presens = new ArrayList<>();
            this.preteritum = new ArrayList<>();
            this.presensPerfektum = new ArrayList<>();
            this.imperativ = new ArrayList<>();
            this.perfpHankjonn = new ArrayList<>();
            this.perfpIntetskjonn = new ArrayList<>();
            this.perfpBestemtForm = new ArrayList<>();
            this.perfpFlertall = new ArrayList<>();
            this.presp = new ArrayList<>();
        }
        else if (wordGroup == "adverb");
        else if (wordGroup == "other");
        else if (wordGroup == "adjective"){
            this.ledetekst = new ArrayList<>();
            this.entallHankjonn = new ArrayList<>();
            this.entallHunkjonn = new ArrayList<>();
            this.entallIntetkjonn = new ArrayList<>();
            this.entallBestemtForm = new ArrayList<>();
            this.flertall = new ArrayList<>();
            this.komparativ = new ArrayList<>();
            this.superlativ = new ArrayList<>();
            this.superlativBestemtForm = new ArrayList<>();
        }
        else if (wordGroup == "noun"){
            this.ledetekst = new ArrayList<>();
            this.entallBestemtForm = new ArrayList<>();
            this.entallUbestemtForm = new ArrayList<>();
            this.flertallBestemtForm = new ArrayList<>();
            this.flertallUbestemtForm = new ArrayList<>();
        }
    }

    public String getKeyword() {
        return keyword;
    }

    public String getKeytype() {
        return keytype;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWordGroup() {
        return wordGroup;
    }

    public void setWordGroup(String wordGroup) {
        this.wordGroup = wordGroup;
    }


    public long write(RandomAccessFile raf) throws IOException {
        long ptr = raf.getFilePointer();
        raf.writeUTF(this.keyword);
        raf.writeUTF(this.keytype);
        if (this.ledetekst == null)
            raf.writeInt(0);
        else
            raf.writeInt(this.ledetekst.size());

        if (this.wordGroup == "verb"){
            for (int i=0; i < this.ledetekst.size(); i++){
                raf.writeUTF(this.ledetekst.get(i));
                raf.writeUTF(this.infinitiv.get(i));
                raf.writeUTF(this.presens.get(i));
                raf.writeUTF(this.preteritum.get(i));
                raf.writeUTF(this.presensPerfektum.get(i));
                raf.writeUTF(this.imperativ.get(i));
                raf.writeUTF(this.perfpHankjonn.get(i));
                raf.writeUTF(this.perfpIntetskjonn.get(i));
                raf.writeUTF(this.perfpBestemtForm.get(i));
                raf.writeUTF(this.perfpFlertall.get(i));
                raf.writeUTF(this.presp.get(i));
            }
        }
        else if (this.wordGroup == "adjective"){

            if (this.komparativ.size() != 0)
                raf.writeBoolean(true);
            else
                raf.writeBoolean(false);

            //System.out.println(this.komparativ.size());
            for (int i=0; i < this.ledetekst.size(); i++) {
                raf.writeUTF(this.ledetekst.get(i));
                raf.writeUTF(this.entallHankjonn.get(i));
                raf.writeUTF(this.entallHunkjonn.get(i));
                raf.writeUTF(this.entallIntetkjonn.get(i));
                raf.writeUTF(this.entallBestemtForm.get(i));
                raf.writeUTF(this.flertall.get(i));
                if (this.komparativ.size() != 0) {
                    raf.writeUTF(this.komparativ.get(i));
                    raf.writeUTF(this.superlativ.get(i));
                    raf.writeUTF(this.superlativBestemtForm.get(i));
                }
            }
        }
        else if (this.wordGroup == "noun"){
            for (int i=0; i < this.ledetekst.size(); i++) {
                raf.writeUTF(this.ledetekst.get(i));
                raf.writeUTF(this.entallBestemtForm.get(i));
                raf.writeUTF(this.entallUbestemtForm.get(i));
                raf.writeUTF(this.flertallBestemtForm.get(i));
                raf.writeUTF(this.flertallUbestemtForm.get(i));
            }
        }

        raf.writeUTF(this.description);
        return  ptr;
    }



    public Word(RandomAccessFile raf, long pos) throws IOException {

        raf.seek(pos);
        String keyw = raf.readUTF();
        String keyt = raf.readUTF();
        this.constructCore(keyw, keyt);

        int N = raf.readInt();
        System.out.println("Len:" + N);

        if (this.wordGroup == "verb"){
            for (int i=0; i < N; i++){
                this.ledetekst.add(raf.readUTF());
                this.infinitiv.add(raf.readUTF());
                this.presens.add(raf.readUTF());
                this.preteritum.add(raf.readUTF());
                this.presensPerfektum.add(raf.readUTF());
                this.imperativ.add(raf.readUTF());
                this.perfpHankjonn.add(raf.readUTF());
                this.perfpIntetskjonn.add(raf.readUTF());
                this.perfpBestemtForm.add(raf.readUTF());
                this.perfpFlertall.add(raf.readUTF());
                this.presp.add(raf.readUTF());
            }
            /*for (int i=0; i < N; i++){
                System.out.println("ledetekst: " + this.ledetekst.get(i));
                System.out.println("infinitiv: " + this.infinitiv.get(i));
                System.out.println("presens: " + this.presens.get(i));
                System.out.println("preteritum: " + this.preteritum.get(i));
                System.out.println("presensPerfektum: " + this.presensPerfektum.get(i));
                System.out.println("imperativ: " + this.imperativ.get(i));
                System.out.println("perfpHankjønn: " + this.perfpHankjonn.get(i));
                System.out.println("perfpIntetskjønn: " + this.perfpIntetskjonn.get(i));
                System.out.println("perfpBestemtForm: " + this.perfpBestemtForm.get(i));
                System.out.println("perfpFlertall: " + this.perfpFlertall.get(i));
                System.out.println("presp: " + this.presp.get(i));
            }*/

        }
        else if (this.wordGroup == "adjective"){
            boolean comparativeAvailable = raf.readBoolean();
            for (int i=0; i < N; i++) {
                this.ledetekst.add(raf.readUTF());
                this.entallHankjonn.add(raf.readUTF());
                this.entallHunkjonn.add(raf.readUTF());
                this.entallIntetkjonn.add(raf.readUTF());
                this.entallBestemtForm.add(raf.readUTF());
                this.flertall.add(raf.readUTF());
                if (comparativeAvailable) {
                    this.komparativ.add(raf.readUTF());
                    this.superlativ.add(raf.readUTF());
                    this.superlativBestemtForm.add(raf.readUTF());
                }
            }
        }
        else if (this.wordGroup == "noun"){
            for (int i=0; i < N; i++) {
                this.ledetekst.add(raf.readUTF());
                this.entallBestemtForm.add(raf.readUTF());
                this.entallUbestemtForm.add(raf.readUTF());
                this.flertallBestemtForm.add(raf.readUTF());
                this.flertallUbestemtForm.add(raf.readUTF());
            }
        }

        System.out.println(this.getWordGroup());
        //System.exit(0);
        this.description = raf.readUTF();
        //System.out.println(this.description);
        //System.exit(0);
    }





    public Set<String> getKeywords(){
        Set<String> collection = new HashSet<>();
        collection.add(keyword);
        collection.add(keyword.replace("|","")); //u|viselig=>usviselig
        if (this.wordGroup == "verb"){
            collection.addAll(this.infinitiv);
            collection.addAll(this.presens);
            collection.addAll(this.preteritum);
            collection.addAll(this.presensPerfektum);
            collection.addAll(this.imperativ);
            collection.addAll(this.perfpHankjonn);
            collection.addAll(this.perfpIntetskjonn);
            collection.addAll(this.perfpBestemtForm);
            collection.addAll(this.perfpFlertall);
            collection.addAll(this.perfpFlertall);
            collection.addAll(this.presp);
        }
        else if (this.wordGroup == "adjective"){
            collection.addAll(this.entallHankjonn);
            collection.addAll(this.entallHunkjonn);
            collection.addAll(this.entallIntetkjonn);
            collection.addAll(this.entallBestemtForm);
            collection.addAll(this.flertall);
            collection.addAll(this.komparativ);
            collection.addAll(this.superlativ);
            collection.addAll(this.superlativBestemtForm);
        }
        else if (wordGroup == "noun") {
            collection.addAll(this.entallBestemtForm);
            collection.addAll(this.entallUbestemtForm);
            collection.addAll(this.flertallBestemtForm);
            collection.addAll(this.flertallUbestemtForm);
        }
        return collection;
    }



    public String toString() {

        String outputString;

        outputString = String.format("%-10s : %s\n%-10s : %s\n%-10s : %s\n\n",
                        "Word", this.keyword, "Type", this.keytype, "Definition", this.description);


        if (this.wordGroup == "verb"){
            outputString += String.format("%-15s %-15s %-15s %-15s %-15s %-15s\n",
                        "Ledetekst","Infinitiv", "Presens","Preteritum",
                        "Presens Pret", "Imperativ");
            for (int i = 0; i< ledetekst.size(); i++){
                outputString += String.format("%-15s %-15s %-15s %-15s %-15s %-15s\n",
                        this.ledetekst.get(i), this.infinitiv.get(i), this.presens.get(i),
                        this.preteritum.get(i), this.presensPerfektum.get(i),
                        this.imperativ.get(i));
            }

            outputString += String.format("\n%-15s %-15s %-15s %-15s %-15s %-15s\n",
                        "Ledetekst", "PerP Han/hunk","PerP Intetk", "PerP Bestemt","PerP Flertall", "PresP");
            for (int i = 0; i< ledetekst.size(); i++){
                outputString += String.format("%-15s %-15s %-15s %-15s %-15s %-15s\n",
                        this.ledetekst.get(i), this.perfpHankjonn.get(i), this.perfpIntetskjonn.get(i),
                        this.perfpBestemtForm.get(i), this.perfpFlertall.get(i),
                        this.presp.get(i));
            }
        }
        else if (wordGroup == "noun"){
            outputString += String.format("%-15s %-15s %-15s %-15s %-15s\n",
                        "Ledetekst", "E. Ubestemt","E. Bestemt", "F. Bestemt","F. Ubestemt");
            for (int i = 0; i< ledetekst.size(); i++){
                outputString += String.format("%-15s %-15s %-15s %-15s %-15s\n",
                        this.ledetekst.get(i), this.entallUbestemtForm.get(i), this.entallBestemtForm.get(i),
                        this.flertallUbestemtForm.get(i), this.flertallBestemtForm.get(i));
            }
        }
        else if (wordGroup == "adjective"){
            outputString += String.format("%-15s %-15s %-15s %-15s %-15s %-15s\n",
                        "Ledetekst", "E. Hanskjønn", "E. Hunskjønn", "E. Intetkjønn", "E. Bestemt","Flertall");
            for (int i = 0; i< ledetekst.size(); i++){
                outputString += String.format("%-15s %-15s %-15s %-15s %-15s %-15s\n",
                        this.ledetekst.get(i), this.entallHankjonn.get(i), this.entallHunkjonn.get(i),
                        this.entallIntetkjonn.get(i),
                        this.entallBestemtForm.get(i), this.flertall.get(i));
            }
            if (this.superlativ.size() != 0){
                outputString += String.format("%-15s %-15s %-15s %-15s\n",
                            "Ledetekst", "Komparativ","Superlativ", "Sup. Bestemt");
                for (int i = 0; i< ledetekst.size(); i++) {
                    outputString += String.format("%-15s %-15s %-15s %-15s\n",
                            this.ledetekst.get(i), this.komparativ.get(i),
                            this.superlativ.get(i), this.superlativBestemtForm.get(i));
                }
            }
        }
        return outputString;
    }


    String keyword;
    String keytype;
    String wordGroup;


    // Nouns
    List<String> ledetekst = null;
    List<String> entallUbestemtForm = null;
    List<String> entallBestemtForm = null;
    List<String> flertallUbestemtForm = null;
    List<String> flertallBestemtForm = null;

    // Verbs
    List<String> infinitiv = null;
    List<String> presens = null;
    List<String> preteritum = null;
    List<String> presensPerfektum = null;
    List<String> imperativ = null;
    List<String> perfpHankjonn = null;
    List<String> perfpIntetskjonn = null;
    List<String> perfpBestemtForm = null;
    List<String> perfpFlertall = null;
    List<String> presp = null;

    // For adjectives
    List<String> entallHankjonn = null;
    List<String> entallHunkjonn = null; // for lita!!!
    List<String> entallIntetkjonn = null;
    //List<String> entallBestemtForm; (covered by Noun!)
    List<String> flertall = null;
    List<String> komparativ = null;
    List<String> superlativ = null;
    List<String> superlativBestemtForm = null;

    // General description
    String description;


    public static void main(String[] args)  {
        System.out.println("Testing");
    }

}
