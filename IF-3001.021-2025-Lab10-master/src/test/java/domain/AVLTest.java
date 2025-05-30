package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AVLTest {

    @Test
    void test() {
        AVL avl = new AVL();
        for (int i = 0; i < 30; i++) {

            avl.add(util.Utility.random(50)+1);
        }
        System.out.println(avl);//toString

        try {

            System.out.println("Contains 15 : " + avl.contains(8));
            System.out.println();

            System.out.println("Size: " + avl.size());
            System.out.println("Maximo: " + avl.max());
            System.out.println("Minimo: " + avl.min());
            System.out.println("Height: " + avl.height());
            System.out.println();

            System.out.println("Remove 5");
            avl.remove(5);
            System.out.println();

            System.out.println("Lista despues del remove: ");
            System.out.println(avl);
            System.out.println();



        } catch (TreeException e) {
            throw new RuntimeException(e);
        }
    }
}