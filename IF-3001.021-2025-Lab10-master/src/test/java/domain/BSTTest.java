package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class BSTTest {

    //Primera prueba con númreos
    @Test
    void test() {
        BST bst = new BST();
        for (int i = 0; i <20 ; i++) {
            bst.add(util.Utility.random(50)+1);
        }
        System.out.println(bst); //toString
        try {
            System.out.println("BST size: "+ bst.size()+". BST height: "+bst.height());
            System.out.println("BST min: " + bst.min() + ". BST max: " + bst.max());
            System.out.println();
            System.out.println("BST contains: ");
            for (int i = 0; i<5; i++){

                int r = util.Utility.random(15)+1;

                System.out.println(bst.contains(r) ?
                        "The value [ " + r + "] exists in the BST"  : "The value [" + r + " ] does not exists ");

            }
            System.out.println("BST remove ");
            for (int i = 0; i < 5; i++) {

                int m = util.Utility.random(15)+1;
                boolean isFound = bst.contains(m);

                if (isFound){

                    System.out.println("The number " + m + " remove");
                    bst.remove(m);

                }else{

                    System.out.println("The number " + m + " It is not on the list so it cannot be deleted.");
                }



            }

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
            System.out.println();


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


        } catch (TreeException e) {
            throw new RuntimeException(e);
        }
    }

}