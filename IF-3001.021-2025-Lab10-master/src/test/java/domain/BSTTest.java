package domain;

import org.junit.jupiter.api.Test;
import domain.BST;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
class BSTTest {

    //Primera prueba con númreos
    @Test
    void test() {
        BST bst = new BST();
        for (int i = 0; i <100 ; i++) {
            bst.add(util.Utility.random(500)+1);
        }
        System.out.println(bst); //toString
        try {
            System.out.println("BST size: "+ bst.size()+". BST height: "+bst.height());
            System.out.println("BST min: " + bst.min() + ". BST max: " + bst.max());
            System.out.println();
            System.out.println("BST contains: ");
            for (int i = 0; i<5; i++){

                int r = util.Utility.random(500)+1;

                System.out.println(bst.contains(r) ?
                        "The value [ " + r + "] exists in the BST"  : "The value [" + r + " ] does not exists ");

            }
            System.out.println("BST remove ");
            for (int i = 0; i < 5; i++) {

                int m = util.Utility.random(500)+1;
                boolean isFound = bst.contains(m);

                if (isFound){

                    System.out.println("The number " + m + " remove");
                    bst.remove(m);

                }else{

                    System.out.println("The number " + m + " It is not on the list so it cannot be deleted.");
                }

            }
            System.out.println();
            System.out.println("Add and Balance");
            for (int i = 0 ; i<10; i++){
                bst.add(util.Utility.random(500)+1);
            }
            System.out.println(bst);
            System.out.println("Is balance ? " + bst.isBalanced());
            System.out.println("Height de element");




        } catch (TreeException e) {
            throw new RuntimeException(e);
        }
    }

    //Segunda prueba con letras del alfabeto
    @Test
    void test2 (){
        
        BST bst = new BST();

        String [] Letters = util.Utility.RandomAlphabet(20);
        for (String letter : Letters) {
            bst.add(letter);
        }
        System.out.println(bst);

        try {

            System.out.println("BST size: " + bst.size() + " BST height: " + bst.height());
            System.out.println("BST min: " + bst.min() + " BST max " + bst.max());
            System.out.println();
            System.out.println("Contains");

            for (int i = 0; i < 10; i++) {

                String letra = util.Utility.RandomAlphabet(1)[0];

                System.out.println(bst.contains(letra) ?
                        "The letter [ " + letra + " ] exists in the BST"  :
                        "The letter [ " + letra + " ] does not exist");

            }
            System.out.println();
            System.out.println("Remove");

            for (int i = 0; i < 5; i++) {

                String letra = util.Utility.RandomAlphabet(1)[0];
                boolean isFound = bst.contains(letra);

                if (isFound){

                    System.out.println("The letter " + letra+ " remove");
                    bst.remove(letra);

                }else{

                    System.out.println("The letter " + letra + " It is not on the list so it cannot be deleted.");
                }

            }
            System.out.println();
            System.out.println("Add and balance");
            for (int i = 0; i < 5; i++) {

                String lettres = util.Utility.RandomAlphabet(1)[0];
                bst.add(lettres);

            }
            System.out.println(bst);
            System.out.println("Is balance ? " + bst.isBalanced());



        } catch (TreeException e) {
            throw new RuntimeException(e);
        }
    }

    //Tercera prueba con nombres
    @Test
    void test3 (){

        BST bst = new BST();

        String [] names = util.Utility.randomNames(20);
        for (String name : names){
            bst.add(name);
        }

        System.out.println(bst);

        try {

            System.out.println("BST size: " + bst.size() + " BST height: " + bst.height());
            System.out.println("BST min: " + bst.min() + " BST max: " + bst.max());
            System.out.println();
            System.out.println("Conatins");

            for (int i = 0; i < 5; i++) {

                String name = util.Utility.randomNames(1)[0];

                System.out.println(bst.contains(name) ?
                        "The name [ " + name + " ] exists in the BST"  :
                        "The name [ " + name + " ] does not exist");

            }
            System.out.println();
            System.out.println("Remove");
            for (int i = 0; i < 5; i++) {

                String name = util.Utility.randomNames(1)[0];
                boolean isFound = bst.contains(name);

                if (isFound){

                    System.out.println("The name " + name + " remove");
                    bst.remove(name);

                }else{

                    System.out.println("The name " + name + " It is not on the list so it cannot be deleted.");
                }

            }
            System.out.println();
            System.out.println("Add and balance");
            for (int i = 0; i < 10; i++) {

                String name = util.Utility.randomNames(1)[0];
                bst.add(name);

            }
            System.out.println(bst);
            System.out.println("Is balance: " + bst.isBalanced());


        } catch (TreeException e) {
            throw new RuntimeException(e);
        }
    }



}