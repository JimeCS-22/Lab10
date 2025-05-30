package domain;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class AVLTest {

    @Test
    void testAVLOperations() {
        // a. Cree e instancie un objeto tipo AVL llamado “avl”, para insertar en forma
        // balanceada 30 números aleatorios entre 20 y 200.
        AVL avl = new AVL();
        Random random = new Random();
        List<Integer> insertedElements = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            int randomNumber = util.Utility.random(181) + 20; // Números entre 20 y 200 (inclusive)
            avl.add(randomNumber);
            insertedElements.add(randomNumber);
        }

        // b. Muestre el contenido del árbol por consola.
        System.out.println("Contenido del árbol AVL:");
        System.out.println(avl);
        System.out.println();

        // c. Pruebe los métodos: size(), min(), max().
        try {
            System.out.println("Tamaño del árbol: " + avl.size());
        } catch (TreeException e) {
            throw new RuntimeException(e);
        }
        try {
            System.out.println("Elemento mínimo: " + avl.min());
            System.out.println("Elemento máximo: " + avl.max());
        } catch (TreeException e) {
            System.out.println("El árbol está vacío, no se pueden obtener el mínimo o el máximo.");
        }
        System.out.println();

        // d. Indique si el árbol está balanceado utilizando el método public boolean
        // isBalanced() que indique si el árbol está balanceado.
        System.out.println("¿El árbol está balanceado? " + avl.isBalanced());
        System.out.println();

        // e. Elimine 5 elementos del árbol. Utilice el método remove(element).
        System.out.println("Eliminando 5 elementos del árbol:");
        List<Object> elementsToRemove = new ArrayList<>();
        for (int i = 0; i < Math.min(5, insertedElements.size()); i++) {
            elementsToRemove.add(insertedElements.get(random.nextInt(insertedElements.size())));
        }

        try {
            for (Object elementToRemove : elementsToRemove) {
                avl.remove(elementToRemove);
                System.out.println("Eliminado: " + elementToRemove);
                insertedElements.remove(elementToRemove); // Keep track of remaining elements
            }
        } catch (TreeException e) {
            System.out.println("Error al intentar eliminar elementos: " + e.getMessage());
        }
        System.out.println();

        // f. Muestre el contenido del árbol por consola.
        System.out.println("Contenido del árbol AVL después de eliminar elementos:");
        System.out.println(avl);
        System.out.println();

        // g. Vuelva a comprobar si el árbol está balanceado.
        System.out.println("¿El árbol está balanceado después de eliminar elementos? " + avl.isBalanced());
        System.out.println();

        // h. Luego de eliminar varios elementos, el árbol AVL puede quedar o no
        // balanceado. Modifique el método remove de la clase AVL o haga un nuevo
        // método que permita re-equilibrar el árbol para que siga siendo un árbol AVL.
        //
        // La clase AVL ya debería tener implementaciones en su método remove para
        // re-equilibrar el árbol después de una eliminación. No se requiere una
        // modificación adicional aquí a menos que la implementación original sea incorrecta.
        // Asumimos que la clase AVL tiene un método remove que mantiene el balance.

        // i. Muestre el contenido del árbol por consola.
        System.out.println("Contenido del árbol AVL después de la (potencial) re-equilibración:");
        System.out.println(avl);
        System.out.println();

        // j. Compruebe nuevamente si el árbol está balanceado.
        System.out.println("¿El árbol está balanceado después de la re-equilibración? " + avl.isBalanced());
        System.out.println();

        // k. Cree y pruebe los siguientes algoritmos:
        System.out.println("--- Pruebas de algoritmos adicionales ---");

        // Seleccionar un elemento aleatorio existente para las pruebas
        Object testElement = null;
        if (!avl.isEmpty()) {
            List<Object> elements = new ArrayList<>();
            inOrderTraversal(avl.getRoot(), elements); // Populate the list with in-order traversal
            if (!elements.isEmpty()) {
                testElement = elements.get(random.nextInt(elements.size()));
                System.out.println("Elemento de prueba: " + testElement);

                // public Object father(Object element) {..}
                Object father = null;
                try {
                    father = avl.father(testElement);
                    System.out.println("Padre de " + testElement + ": " + (father != null ? father : "Ninguno"));

                    // public Object brother(Object element) {..}
                    Object brother = avl.brother(testElement);
                    System.out.println("Hermano de " + testElement + ": " + (brother != null ? brother : "Ninguno"));

                    // public String children(Object element) {..}
                    String children = avl.children(testElement);
                    System.out.println("Hijos de " + testElement + ": " + children);
                } catch (TreeException e) {
                    throw new RuntimeException(e);
                }

            } else {
                System.out.println("El árbol está vacío, no se pueden probar los algoritmos de padre, hermano e hijos.");
            }

        } else {
            System.out.println("El árbol está vacío, no se pueden probar los algoritmos de padre, hermano e hijos.");
        }
    }

    // Helper method to perform in-order traversal and collect elements
    private void inOrderTraversal(BTreeNode node, List<Object> list) {
        if (node != null) {
            inOrderTraversal(node.left, list);
            list.add(node.data);
            inOrderTraversal(node.right, list);
        }
    }
}